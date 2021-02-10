package archimedes.legacy.updater;

import archimedes.legacy.updater.UpdateReportAction.Status;
import archimedes.legacy.updater.UpdateReportAction.Type;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import de.ollie.dbcomp.comparator.DataModelComparator;
import de.ollie.dbcomp.comparator.model.ChangeActionCRO;
import de.ollie.dbcomp.comparator.model.actions.DropColumnChangeActionCRO;
import de.ollie.dbcomp.comparator.model.actions.DropTableChangeActionCRO;
import de.ollie.dbcomp.comparator.model.actions.ModifyDataTypeCRO;
import de.ollie.dbcomp.comparator.model.actions.ModifyNullableCRO;

/**
 * A class which is able to update a data model by a given source.
 *
 * @author ollie (08.02.2021)
 */
public class ModelUpdater {

	private DataModel toUpdate;
	private DataModel source;

	public ModelUpdater(DataModel toUpdate, DataModel source) {
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
		UpdateReportAction action = new UpdateReportAction().setMessage(cro.toString());
		try {
			if (cro instanceof DropTableChangeActionCRO) {
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
			} else if (cro instanceof ModifyNullableCRO) {
				TableModel table = toUpdate.getTableByName(((ModifyNullableCRO) cro).getTableName());
				ColumnModel column = table.getColumnByName(((ModifyNullableCRO) cro).getColumnName());
				column.setNotNull(!((ModifyNullableCRO) cro).isNewNullable());
				action
						.setType(Type.MODIFY_COLUMN_CONSTRAIN_NOT_NULL)
						.setValues(
								((ModifyNullableCRO) cro).getTableName(),
								((ModifyNullableCRO) cro).getColumnName(),
								!((ModifyNullableCRO) cro).isNewNullable());
			} else if (cro instanceof ModifyDataTypeCRO) {
				return action
						.setStatus(Status.MANUAL)
						.setType(Type.MODIFY_COLUMN_DATATYPE)
						.setValues(
								((ModifyDataTypeCRO) cro).getTableName(),
								((ModifyDataTypeCRO) cro).getColumnName(),
								((ModifyDataTypeCRO) cro).getNewDataType());
			}
		} catch (Exception e) {
			return action.setStatus(Status.FAILED);
		}
		return action.setStatus(Status.DONE);
	}

}