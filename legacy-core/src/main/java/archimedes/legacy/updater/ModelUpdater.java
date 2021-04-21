package archimedes.legacy.updater;

import java.util.Arrays;
import java.util.List;

import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.ObjectFactory;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.legacy.updater.UpdateReportAction.Status;
import archimedes.legacy.updater.UpdateReportAction.Type;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import corent.base.Direction;
import de.ollie.dbcomp.comparator.DataModelComparator;
import de.ollie.dbcomp.comparator.model.ChangeActionCRO;
import de.ollie.dbcomp.comparator.model.actions.AddColumnChangeActionCRO;
import de.ollie.dbcomp.comparator.model.actions.AddForeignKeyCRO;
import de.ollie.dbcomp.comparator.model.actions.AddPrimaryKeyCRO;
import de.ollie.dbcomp.comparator.model.actions.CreateTableChangeActionCRO;
import de.ollie.dbcomp.comparator.model.actions.DropColumnChangeActionCRO;
import de.ollie.dbcomp.comparator.model.actions.DropForeignKeyCRO;
import de.ollie.dbcomp.comparator.model.actions.DropPrimaryKeyCRO;
import de.ollie.dbcomp.comparator.model.actions.DropTableChangeActionCRO;
import de.ollie.dbcomp.comparator.model.actions.ForeignKeyMemberCRO;
import de.ollie.dbcomp.comparator.model.actions.ModifyDataTypeCRO;
import de.ollie.dbcomp.comparator.model.actions.ModifyNullableCRO;

/**
 * A class which is able to update a data model by a given source.
 *
 * @author ollie (08.02.2021)
 */
public class ModelUpdater {

	private static final TypeConverter TYPE_CONVERTER = new TypeConverter();

	private DataModel toUpdate;
	private DataModel source;
	private ObjectFactory objectFactory;

	public ModelUpdater(DataModel toUpdate, DataModel source, ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
		this.source = source;
		this.toUpdate = toUpdate;
	}

	public UpdateReport update() {
		UpdateReport updateReport = new UpdateReport();
		DataModelToCMOConverter converter = new DataModelToCMOConverter();
		DataModelComparator comparator = new DataModelComparator();
		comparator
				.compare(converter.convert(source), converter.convert(toUpdate))
				.getChangeActions()
				.forEach(cro -> updateReport.addUpdateReportAction(process(toUpdate, cro)));
		return updateReport;
	}

	private UpdateReportAction process(DataModel toUpdate, ChangeActionCRO cro) {
		UpdateReportAction action = new UpdateReportAction().setMessage(getForeignKeyString(cro) + cro.toString());
		try {
			if (cro instanceof AddColumnChangeActionCRO) {
				TableModel table = toUpdate.getTableByName(((AddColumnChangeActionCRO) cro).getTableName());
				String sqlType = ((AddColumnChangeActionCRO) cro).getSqlType();
				DomainModel domain = getDomain(
						TYPE_CONVERTER.getSQLType(sqlType),
						TYPE_CONVERTER.getLength(sqlType),
						TYPE_CONVERTER.getPrecision(sqlType),
						toUpdate);
				ColumnModel newColumn = new Tabellenspalte(((AddColumnChangeActionCRO) cro).getColumnName(), domain);
				newColumn.setPanel(table.getPanels()[0]);
				table.addColumn(newColumn);
				action
						.setType(Type.ADD_COLUMN)
						.setValues(
								((AddColumnChangeActionCRO) cro).getTableName(),
								((AddColumnChangeActionCRO) cro).getColumnName(),
								sqlType);
			} else if (cro instanceof AddForeignKeyCRO) {
				TableModel table = toUpdate.getTableByName(((AddForeignKeyCRO) cro).getTableName());
				((AddForeignKeyCRO) cro).getMembers().forEach(member -> {
					TableModel refTable = toUpdate.getTableByName(member.getReferencedTableName());
					ColumnModel refColumn = refTable.getColumnByName(member.getReferencedColumnName());
					ColumnModel column = table.getColumnByName(member.getBaseColumnName());
					ViewModel view = getPrimaryView();
					column
							.setRelation(
									objectFactory
											.createRelation(
													view,
													column,
													Direction.LEFT,
													0,
													refColumn,
													Direction.RIGHT,
													0));
				});
				action
						.setType(Type.ADD_FOREIGN_KEY)
						.setValues(((AddForeignKeyCRO) cro).getTableName(), ((AddForeignKeyCRO) cro).getMembers());
			} else if (cro instanceof AddPrimaryKeyCRO) {
				AddPrimaryKeyCRO addCRO = (AddPrimaryKeyCRO) cro;
				TableModel table = toUpdate.getTableByName(addCRO.getTableName());
				Arrays.asList(table.getColumns()).forEach(column -> {
					column.setPrimaryKey(addCRO.getPkMemberNames().contains(column.getName()));
					if (addCRO.getPkMemberNames().contains(column.getName())) {
						column.setNotNull(false);
					}
				});
				action.setType(Type.ADD_PRIMARY_KEY).setValues(addCRO.getTableName(), addCRO.getPkMemberNames());
			} else if (cro instanceof CreateTableChangeActionCRO) {
				CreateTableChangeActionCRO createCRO = (CreateTableChangeActionCRO) cro;
				TableModel table = objectFactory.createTabelle(getPrimaryView(), 0, 0, (DiagrammModel) toUpdate, false);
				createCRO.getColumns().forEach(column -> {
					ColumnModel newColumn = objectFactory
							.createTabellenspalte(
									column.getName(),
									getDomain(
											TYPE_CONVERTER.getSQLType(column.getSqlType()),
											TYPE_CONVERTER.getLength(column.getSqlType()),
											TYPE_CONVERTER.getPrecision(column.getSqlType()),
											toUpdate),
									createCRO.isPrimaryKeyMember(column.getName()));
					newColumn.setNotNull(createCRO.isPrimaryKeyMember(column.getName()) ? true : !column.isNullable());
					newColumn.setPanel(table.getPanels()[0]);
					table.addColumn(newColumn);
				});
				table.setDraft(false);
				table.setName(createCRO.getTableName());
				toUpdate.addTable(table);
				action.setType(Type.CREATE_TABLE).setValues(createCRO.getTableName());
			} else if (cro instanceof DropTableChangeActionCRO) {
				toUpdate.removeTable(toUpdate.getTableByName(((DropTableChangeActionCRO) cro).getTableName()));
				action.setType(Type.DROP_TABLE).setValues(((DropTableChangeActionCRO) cro).getTableName());
			} else if (cro instanceof DropColumnChangeActionCRO) {
				TableModel table = toUpdate.getTableByName(((DropColumnChangeActionCRO) cro).getTableName());
				table.removeColumn(table.getColumnByName(((DropColumnChangeActionCRO) cro).getColumnName()));
				action
						.setType(Type.DROP_COLUMN)
						.setValues(
								((DropColumnChangeActionCRO) cro).getTableName(),
								((DropColumnChangeActionCRO) cro).getColumnName());
			} else if (cro instanceof DropForeignKeyCRO) {
				TableModel table = toUpdate.getTableByName(((DropForeignKeyCRO) cro).getTableName());
				((DropForeignKeyCRO) cro).getMembers().forEach(member -> {
					ColumnModel column = table.getColumnByName(member.getBaseColumnName());
					column.setRelation(null);
				});
				action
						.setType(Type.DROP_FOREIGN_KEY)
						.setValues(((DropForeignKeyCRO) cro).getTableName(), ((DropForeignKeyCRO) cro).getMembers());
			} else if (cro instanceof DropPrimaryKeyCRO) {
				DropPrimaryKeyCRO dropCRO = (DropPrimaryKeyCRO) cro;
				TableModel table = toUpdate.getTableByName(dropCRO.getTableName());
				Arrays
						.asList(table.getColumns())
						.forEach(
								column -> column
										.setPrimaryKey(
												dropCRO.getPkMemberNames().contains(column.getName())
														? false
														: column.isPrimaryKey()));
				action.setType(Type.DROP_PRIMARY_KEY).setValues(dropCRO.getTableName(), dropCRO.getPkMemberNames());
			} else if (cro instanceof ModifyNullableCRO) {
				TableModel table = toUpdate.getTableByName(((ModifyNullableCRO) cro).getTableName());
				ColumnModel column = table.getColumnByName(((ModifyNullableCRO) cro).getColumnName());
				column.setNotNull(!((ModifyNullableCRO) cro).isNewNullable());
				action
						.setType(Type.MODIFY_COLUMN_CONSTRAINT_NOT_NULL)
						.setValues(
								((ModifyNullableCRO) cro).getTableName(),
								((ModifyNullableCRO) cro).getColumnName(),
								!((ModifyNullableCRO) cro).isNewNullable());
			} else if (cro instanceof ModifyDataTypeCRO) {
				TableModel table = toUpdate.getTableByName(((ModifyDataTypeCRO) cro).getTableName());
				String sqlType = ((ModifyDataTypeCRO) cro).getNewDataType();
				DomainModel domain = getDomain(
						TYPE_CONVERTER.getSQLType(sqlType),
						TYPE_CONVERTER.getLength(sqlType),
						TYPE_CONVERTER.getPrecision(sqlType),
						toUpdate);
				table.getColumnByName(((ModifyDataTypeCRO) cro).getColumnName()).setDomain(domain);
				action
						.setType(Type.MODIFY_COLUMN_DATATYPE)
						.setValues(
								((ModifyDataTypeCRO) cro).getTableName(),
								((ModifyDataTypeCRO) cro).getColumnName(),
								((ModifyDataTypeCRO) cro).getNewDataType());
			} else {
				return action.setStatus(Status.MANUAL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return action.setStatus(Status.FAILED);
		}
		return action.setStatus(Status.DONE);
	}

	private String getForeignKeyString(ChangeActionCRO cro) {
		if (cro instanceof AddForeignKeyCRO) {
			AddForeignKeyCRO addCRO = (AddForeignKeyCRO) cro;
			return getForeignKeyString(addCRO.getMembers()) + " - ";
		} else if (cro instanceof DropForeignKeyCRO) {
			DropForeignKeyCRO dropCRO = (DropForeignKeyCRO) cro;
			return getForeignKeyString(dropCRO.getMembers()) + " - ";
		}
		return "";
	}

	private String getForeignKeyString(List<ForeignKeyMemberCRO> members) {
		String s = "";
		for (ForeignKeyMemberCRO member : members) {
			if (!s.isEmpty()) {
				s += ", ";
			}
			s += member.getBaseTableName() + "." + member.getBaseColumnName() + " -> " + member.getReferencedTableName()
					+ "." + member.getReferencedColumnName();
		}
		return s;
	}

	private ViewModel getPrimaryView() {
		return toUpdate.getViewByName(toUpdate.getViews().get(0).getName());
	}

	private DomainModel getDomain(int sqlType, Integer length, Integer precision, DataModel dataModel) {
		DomainModel dom = objectFactory
				.createDomain("*", sqlType, length == null ? -1 : length, precision == null ? -1 : precision);
		String typeName = dom.getType().replace("(", "").replace(")", "").replace(" ", "");
		typeName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);
		DomainModel domain = getDomainByNameCaseInsensitive(dataModel, typeName);
		if (domain == null) {
			domain = objectFactory
					.createDomain(typeName, sqlType, length == null ? -1 : length, precision == null ? -1 : precision);
			dataModel.addDomain(domain);
		}
		return domain;
	}

	private DomainModel getDomainByNameCaseInsensitive(DataModel dataModel, String typeName) {
		return Arrays
				.asList(dataModel.getAllDomains())
				.stream()
				.filter(domain -> domain.getName().equalsIgnoreCase(typeName))
				.findFirst()
				.orElse(null);
	}

}