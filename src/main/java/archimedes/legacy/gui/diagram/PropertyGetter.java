package archimedes.legacy.gui.diagram;

import java.util.Map;

/**
 * An interface for property getters.
 *
 * @author ollie (30.06.2020)
 */
public interface PropertyGetter {

	/**
	 * Returns a map with property names and values.
	 *
	 * @return A map with property names and values.
	 */
	Map<String, String> getProperties();

}