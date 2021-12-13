package archimedes.codegenerators;

import static corentx.util.Checks.ensure;

import archimedes.model.TableModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TableUtil {

	public boolean hasCompositeKey(TableModel table) {
		ensure(table != null, "table cannot be null.");
		return table.getPrimaryKeyColumns().length > 1;
	}

}