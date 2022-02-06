package archimedes.codegenerators;

import archimedes.model.ColumnModel;

public class NullableUtils {

	public static boolean isNullable(ColumnModel column) {
		return !column.isNotNull();
	}

}