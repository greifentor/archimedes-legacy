package archimedes.legacy.updater;

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

	private String message;
	private Status status;

}