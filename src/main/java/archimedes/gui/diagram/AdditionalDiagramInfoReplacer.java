package archimedes.gui.diagram;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Replaces some variables in a string for additional diagram info display.
 *
 * @author ollie (30.06.2020)
 */
public class AdditionalDiagramInfoReplacer {

	public String replace(String s, PropertyGetter propertyGetter) {
		Map<String, String> m = propertyGetter.getProperties();
		for (Entry<String, String> entry : m.entrySet()) {
			s = s.replace("${Option." + entry.getKey() + "}", String.valueOf(entry.getValue()));
		}
		return s;
	}

}