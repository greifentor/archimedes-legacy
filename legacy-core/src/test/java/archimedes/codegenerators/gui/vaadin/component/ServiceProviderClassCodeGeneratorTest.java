package archimedes.codegenerators.gui.vaadin.component;

import static archimedes.codegenerators.DataModelReader.EXAMPLE_XMLS;
import static archimedes.codegenerators.DataModelReader.readDataModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.DataModel;

@ExtendWith(MockitoExtension.class)
public class ServiceProviderClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ServiceProviderClassCodeGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRun() {
			// Prepare
			String expected =
					"package de.ollie.bookstore.gui.vaadin.component;\n" //
							+ "\n" //
							+ "import javax.inject.Named;\n" //
							+ "\n" //
							+ "import org.springframework.beans.factory.annotation.Autowired;\n" //
							+ "\n" //
							+ "import de.ollie.bookstore.core.service.BookService;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.Getter;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * A service provider.\n" //
							+ " *\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Generated\n" //
							+ "@Getter\n" //
							+ "@Named\n" //
							+ "public class ServiceProvider {\n" //
							+ "\n" //
							+ "	@Autowired(required = false)\n" //
							+ "	private BookService bookService;\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRun_detailsReferencesDetails() {
			// Prepare
			String expected =
					"package de.ollie.task.gui.vaadin.component;\n" //
							+ "\n" //
							+ "import javax.inject.Named;\n" //
							+ "\n" //
							+ "import org.springframework.beans.factory.annotation.Autowired;\n" //
							+ "\n" //
							+ "import de.ollie.task.core.service.TaskService;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.Getter;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * A service provider.\n" //
							+ " *\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Generated\n" //
							+ "@Getter\n" //
							+ "@Named\n" //
							+ "public class ServiceProvider {\n" //
							+ "\n" //
							+ "	@Autowired(required = false)\n" //
							+ "	private TaskService taskService;\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Example-Tasks.xml", EXAMPLE_XMLS);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
			// Check
			assertEquals(expected, returned);
		}

	}

}
