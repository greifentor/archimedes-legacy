package archimedes.codegenerators.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class CompositeKeyModelClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private CompositeKeyModelClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class CompositeKey_RegularRun {

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected("core.model");
				DataModel dataModel = readDataModel("Model-CompositeKey.xml");
				// Run
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
				// Check
				assertEquals(expected, returned);
			}

			private String getExpected(String packageName) {
				return getExpected(null, packageName, false);
			}

			private String getExpected(String prefix, String packageName, boolean suppressComment) {
				return getExpected(prefix, packageName, suppressComment, false);
			}

			private String getExpected(String prefix, String packageName, boolean suppressComment,
					boolean descriptionNotNull) {
				String s =
						"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName
								+ ";\n" + //
								"\n" + //
								"import java.io.Serializable;\n" + //
								"\n" + //
								"import lombok.AllArgsConstructor;\n" + //
								"import lombok.Data;\n" + //
								"import lombok.Generated;\n" + //
								"import lombok.NoArgsConstructor;\n" + //
								"import lombok.experimental.Accessors;\n" + //
								"\n";
				if (!suppressComment) {
					s += "/**\n" + //
							" * A composite key class for a_tables.\n" + //
							" *\n" + //
							" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
							" */\n";
				}
				s += "@Accessors(chain = true)\n" + //
						"@AllArgsConstructor\n" + //
						"@Data\n" + //
						"@Generated\n" + //
						"@NoArgsConstructor\n" + //
						"public class ATableId implements Serializable {\n" + //
						"\n" + //
						"	private long id;\n" + //
						"	private String mandator;\n" + //
						"\n" + //
						"}";
				return s;
			}

		}

	}

}
