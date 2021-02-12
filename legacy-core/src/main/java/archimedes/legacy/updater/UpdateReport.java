package archimedes.legacy.updater;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A container for an update report.
 *
 * @author ollie (09.02.2021)
 */
@Accessors(chain = true)
@Data
public class UpdateReport {

	private List<UpdateReportAction> actions = new ArrayList<>();

	public UpdateReport addUpdateReportAction(UpdateReportAction action) {
		actions.add(action);
		return this;
	}

}