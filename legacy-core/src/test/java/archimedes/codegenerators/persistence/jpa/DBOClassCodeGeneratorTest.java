package archimedes.codegenerators.persistence.jpa;

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
public class DBOClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";
	private static final String TABLE_NAME = "ATable";

	@InjectMocks
	private DBOClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObjectWithoutAnyFields() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".persistence.entities;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import javax.persistence.Column;\n" + //
					"import javax.persistence.Entity;\n" + //
					"import javax.persistence.Id;\n" + //
					"import javax.persistence.Table;\n" + //
					"\n" + //
					"import lombok.Data;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.experimental.Accessors;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Accessors(chain = true)\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"@Entity(name = \"ATable\")\n" + //
					"@Table(name = \"A_TABLE\")\n" + //
					"public class ATableDBO {\n" + //
					"\n" + //
					"	@Id\n" + //
					"	@Column(name = \"ID\")\n" + //
					"	private long id;\n" + //
					"	@Column(name = \"ADate\")\n" + //
					"	private LocalDate aDate;\n" + //
					"	@Column(name = \"Description\")\n" + //
					"	private String description;\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}