package archimedes.model.converter;

import java.util.Arrays;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;
import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.OptionSO;
import de.ollie.archimedes.alexandrian.service.so.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import de.ollie.archimedes.alexandrian.service.so.TypeSO;

/**
 * A convert for Archimedes DataModels (old style) to SO's.
 *
 * @author ollie (moved from rest-acf 20.06.2020)
 *
 */
public class DataModelToSOConverter {

	/**
	 * Converts an old style DataModel to an SO model.
	 * 
	 * @param dataModel The model to convert.
	 * @return A SO model with the values of the passed DataModel.
	 */
	public DatabaseSO convert(DataModel dataModel) {
		if (dataModel == null) {
			return null;
		}
		SchemeSO sso = new SchemeSO().setName("public");
		for (TableModel tm : dataModel.getTables()) {
			TableSO tso = new TableSO().setName(tm.getName());
			for (ColumnModel cm : tm.getColumns()) {
				ColumnSO cso = new ColumnSO().setName(cm.getName());
				DomainModel dom = cm.getDomain();
				TypeSO tpso = new TypeSO().setSqlType(dom.getDataType());
				if (dom.getLength() > 0) {
					tpso.setLength(dom.getLength());
				}
				if (dom.getDecimalPlace() > 0) {
					tpso.setPrecision(dom.getDecimalPlace());
				}
				cso.setType(tpso);
				cso.setPkMember(cm.isPrimaryKey());
				cso.setNullable(!cm.isNotNull());
				cso.setTable(tso);
				cso.setUnique(cm.isUnique());
				tso.getColumns().add(cso);
			}
			sso.getTables().add(tso);
		}
		for (TableModel tm : dataModel.getTables()) {
			TableSO tso = getTableSOByName(tm.getName(), sso);
			for (ColumnModel cm : tm.getColumns()) {
				if ((cm.getReferencedColumn() != null) && (cm.getReferencedTable() != null)) {
					ColumnSO cso = getColumnSOByName(cm.getName(), tso);
					TableSO tsoReferenced = getTableSOByName(cm.getReferencedTable().getName(), sso);
					ColumnSO csoReferenced = getColumnSOByName(cm.getReferencedColumn().getName(), tsoReferenced);
					ReferenceSO reference = new ReferenceSO().setReferencedColumn(csoReferenced)
							.setReferencingColumn(cso);
					ForeignKeySO foreignKey = new ForeignKeySO().setReferences(Arrays.asList(reference));
					tso.getForeignKeys().add(foreignKey);
				}
			}
		}
		DatabaseSO dbso = new DatabaseSO().setName(dataModel.getName()).setSchemes(Arrays.asList(sso));
		for (OptionModel option : dataModel.getOptions()) {
			dbso.addOptions(new OptionSO().setName(option.getName()).setValue(option.getParameter()));
		}
		return dbso;
	}

	private TableSO getTableSOByName(String tableName, SchemeSO sso) {
		for (TableSO tso : sso.getTables()) {
			if (tso.getName().equals(tableName)) {
				return tso;
			}
		}
		return null;
	}

	private ColumnSO getColumnSOByName(String columnName, TableSO tso) {
		for (ColumnSO cso : tso.getColumns()) {
			if (cso.getName().equals(columnName)) {
				return cso;
			}
		}
		return null;
	}

}