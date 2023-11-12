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
public class ApplicationPropertiesFileGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ApplicationPropertiesFileGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class Simple {

			private String getExpected(boolean suppressComment) {
				return "# " + AbstractCodeGenerator.GENERATED_CODE + "\n" //
						+ "\n" //
						+ "app.version=@project.version@\n" //
						+ "\n" //
						+ "# spring.liquibase.change-log=classpath:liquibase/change-log/change-log-master.xml\n" //
						+ "spring-jpa-hobernate.dll-auto=update\n" //
						+ "\n" //
						+ "logging.level.root=INFO\n" //
						+ "\n" //
						+ "spring.datasource.url=jdbc:hsqldb:mem:app-name\n" //
						+ "spring.datasource.driverClassName=org.hsqldb.jdbc.JDBCDriver\n" //
						+ "spring.datasource.username=sa\n" //
						+ "spring.datasource.password=\n" //
						+ "\n" //
						+ "spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl";
			}

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected(false);
				DataModel dataModel = readDataModel("Model.xml");
				dataModel.setApplicationName("App-NAME");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void worksWithCorrectFileName() {
				// Prepare
				DataModel dataModel = readDataModel("Model.xml");
				// Run & Check
				assertEquals(
						"/src/main/resources/application.properties",
						unitUnderTest.getSourceFileName("", dataModel, dataModel));
			}

		}

	}

}
