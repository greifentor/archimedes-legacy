package archimedes.codegenerators.gui.vaadin;

import static archimedes.codegenerators.DataModelReader.EXAMPLE_XMLS;
import static archimedes.codegenerators.DataModelReader.readDataModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.DataModel;
import archimedes.model.DomainModel;

@ExtendWith(MockitoExtension.class)
public class EnumItemLabelGeneratorClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private EnumItemLabelGeneratorClassCodeGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class SimpleClass {

			@Test
			void happyRunForASimpleObject() {
				String expected =
						"package de.ollie.bookstore.gui.vaadin.masterdata.renderer;\n" //
								+ "\n" //
								+ "import javax.inject.Named;\n" //
								+ "\n" //
								+ "import org.springframework.beans.factory.annotation.Autowired;\n" //
								+ "\n" //
								+ "import com.vaadin.flow.component.ItemLabelGenerator;\n" //
								+ "\n" //
								+ "import de.ollie.bookstore.core.model.PublicationType;\n" //
								+ "import de.ollie.bookstore.core.service.localization.ResourceManager;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.Getter;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Named\n" //
								+ "@Getter\n" //
								+ "@Generated\n" //
								+ "public class PublicationTypeItemLabelGenerator implements ItemLabelGenerator<PublicationType> {\n" //
								+ "\n" //
								+ "	@Autowired\n" //
								+ "	private ResourceManager resourceManager;\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	public String apply(PublicationType item) {\n" //
								+ "		return item != null ? resourceManager.getLocalizedString(\"PublicationType.\" + item.name() + \".label\") : \"-\";\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
				DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
				DomainModel domainModel = dataModel.getDomainByName("PublicationType");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, domainModel);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}
