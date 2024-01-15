package archimedes.codegenerators;

import java.util.List;
import java.util.Optional;

import archimedes.model.ColumnModel;
import archimedes.model.OptionListProvider;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

public class GlobalIdOptionChecker {

	public static final GlobalIdOptionChecker INSTANCE = new GlobalIdOptionChecker();

	public boolean hasGlobalIdTypeConfiguration(GlobalIdType globalIdType, TableModel table) {
		if (!hasColumnWithGlobalId(table)) {
			return false;
		}
		return hasGlobalIdType(globalIdType, table);
	}

	private boolean hasColumnWithGlobalId(TableModel table) {
		return List
				.of(table.getColumns())
				.stream()
				.anyMatch(c -> c.findOptionByName(AbstractModelCodeGenerator.GLOBAL_ID).isPresent());
	}

	private boolean hasGlobalIdType(GlobalIdType globalIdType, TableModel table) {
		for (ColumnModel c : table.getColumns()) {
			Optional<OptionModel> option = c.findOptionByName(AbstractModelCodeGenerator.GLOBAL_ID);
			if (option.isPresent() && globalIdType.name().equals(option.get().getParameter())) {
				return true;
			}
		}
		Optional<OptionModel> option = table.findOptionByName(AbstractModelCodeGenerator.GLOBAL_ID);
		if (option.isPresent() && globalIdType.name().equals(option.get().getParameter())) {
			return true;
		}
		option = table.getDataModel().findOptionByName(AbstractModelCodeGenerator.GLOBAL_ID);
		if (option.isPresent() && globalIdType.name().equals(option.get().getParameter())) {
			return true;
		}
		return false;
	}

	public GlobalIdType getGlobalIdType(ColumnModel column) {
		if (column.getOptionByName(AbstractModelCodeGenerator.GLOBAL_ID) != null) {
			for (OptionListProvider opl : List.of(column, column.getTable(), column.getTable().getDataModel())) {
				Optional<OptionModel> option = opl.findOptionByName(AbstractModelCodeGenerator.GLOBAL_ID);
				if (option.isPresent()) {
					OptionModel om = option.get();
					if (om.getParameter() != null) {
						return (GlobalIdType.valueOf(om.getParameter()));
					}
				}
			}
		}
		return GlobalIdType.NONE;
	}

}
