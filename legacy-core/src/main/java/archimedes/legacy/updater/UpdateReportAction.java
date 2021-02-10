package archimedes.legacy.updater;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A container for a single update report action.
 *
 * @author ollie (09.02.2021)
 */
@Accessors(chain = true)
@Data
public class UpdateReportAction {

	public enum Status {
		DONE,
		FAILED,
		MANUAL;
	}

	public enum Type {
		ADD_COLUMN,
		ADD_TABLE,
		DROP_COLUMN,
		DROP_TABLE,
		MODIFY_COLUMN_CONSTRAIN_NOT_NULL,
		MODIFY_COLUMN_DATATYPE;
	}

	private String message;
	private Status status;
	private Type type;
	private String[] values;

	public UpdateReportAction setValues(Object... values) {
		List<String> l = new ArrayList<>(values.length);
		for (Object o : values) {
			l.add(String.valueOf(o));
		}
		this.values = l.toArray(new String[l.size()]);
		return this;
	}

}