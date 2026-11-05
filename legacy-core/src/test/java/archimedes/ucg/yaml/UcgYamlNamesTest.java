package archimedes.ucg.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class UcgYamlNamesTest {

	@ParameterizedTest
	@CsvSource({
			"model.yml, model.archimedes.yml",
			"model.YAML, model.archimedes.yml",
			"C:/dir.yml/model.yaml, C:/dir.yml/model.archimedes.yml",
			"model, model.archimedes.yml" })
	void getArchimedesFileName_returnsTheNameOfTheArchimedesFile_passingAModelFileName(String modelFileName,
			String expected) {
		// Run
		String returned = UcgYamlNames.getArchimedesFileName(modelFileName);
		// Check
		assertEquals(expected, returned);
	}

}
