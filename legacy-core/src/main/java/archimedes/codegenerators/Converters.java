package archimedes.codegenerators;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Utility class for converters.
 * 
 * @author ollie (05.02.2022)
 */
public class Converters {

	@Accessors(chain = true)
	@Data
	public static class ConverterData {
		private String attributeName;
		private String className;
		private String columnName;
	}

}