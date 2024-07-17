package archimedes.codegenerators;

import static corentx.util.Checks.ensure;

import archimedes.model.ColumnModel;
public class NoKeyValueFinder {

	public static final String NO_KEY_FOUND = "NO_KEY_FOUND";

	private TypesUtil typesUtil = new TypesUtil();

	public String find(ColumnModel[] pks) {
		ensure(pks != null, "pks cannot be null!");
		if (pks.length == 0) {
			return NO_KEY_FOUND;
		}
		return pks[0].isNotNull() && typesUtil.isNumericType(pks[0].getDomain().getDataType()) ? "-1" : "null";
	}

}
