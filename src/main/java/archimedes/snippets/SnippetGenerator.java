package archimedes.snippets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * An interface for snippet generators.
 *
 * @author ollie (11.08.2020)
 */
public interface SnippetGenerator {

	public enum ParameterType {
		BOOLEAN, //
		LONG, //
		STRING, //
		STRING_LIST //
	}

	@Accessors(chain = true)
	@Data
	public class ParameterDescription {
		private String name;
		private ParameterType type;
	}

	/**
	 * Generates the snippet code with the passed parameters.
	 * 
	 * @param parameters A map (key - value) with parameters for the snippet generator.
	 * @returns The code for the snippet.
	 */
	String generate(Map<String, Object> parameters);

	/**
	 * A name for the snippet generator.
	 * 
	 * @return A name for the snippet generator.
	 */
	String getName();

	/**
	 * Returns a list of parameter description for the snippet generator.
	 * 
	 * @return A list of parameter description for the snippet generator. Maybe empty if no parameters are required for
	 *         the snippet generator.
	 */
	default List<ParameterDescription> getParameterDescriptions() {
		return new ArrayList<>();
	}

	/**
	 * The current version of the snippet generator.
	 * 
	 * @return The version of the snippet generator.
	 */
	String getVersion();

}