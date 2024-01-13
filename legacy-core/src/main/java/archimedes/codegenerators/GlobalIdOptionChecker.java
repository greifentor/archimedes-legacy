package archimedes.codegenerators;

import java.util.List;
import java.util.Optional;

import archimedes.model.ColumnModel;
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

	private boolean hasGlobalIdType(GlobalIdType globalIdType, TableModel tableModel) {
		for (ColumnModel c : tableModel.getColumns()) {
			Optional<OptionModel> option = c.findOptionByName(AbstractModelCodeGenerator.GLOBAL_ID);
			if (option.isPresent() && globalIdType.name().equals(option.get().getParameter())) {
				return true;
			}
		}
		return false;
	}

	public GlobalIdType getGlobalIdType(ColumnModel column) {
		Optional<OptionModel> option = column.findOptionByName(AbstractModelCodeGenerator.GLOBAL_ID);
		if (option.isPresent()) {
			OptionModel om = option.get();
			if (om.getParameter() != null) {
				return (GlobalIdType.valueOf(om.getParameter()));
			}
		}
		return GlobalIdType.NONE;
	}

}
