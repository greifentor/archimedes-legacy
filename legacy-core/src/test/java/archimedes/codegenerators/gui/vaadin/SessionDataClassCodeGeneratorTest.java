package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class SessionDataClassCodeGeneratorTest {

	private static final String APPLICATION_NAME = "Application-Name";
	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private SessionDataClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class SimpleClass {

			private String getExpected() {
				return "package base.pack.age.name.gui;\n" //
						+ "\n" //
						+ "import static base.pack.age.name.util.Check.ensure;\n" //
						+ "\n" //
						+ "import java.util.HashMap;\n" //
						+ "import java.util.List;\n" //
						+ "import java.util.Map;\n" //
						+ "import java.util.Optional;\n" //
						+ "import java.util.Stack;\n" //
						+ "\n" //
						+ "import org.springframework.stereotype.Component;\n" //
						+ "\n" //
						+ "import com.vaadin.flow.spring.annotation.VaadinSessionScope;\n" //
						+ "\n" //
						+ "import base.pack.age.name.core.model.localization.LocalizationSO;\n" //
						+ "\n" //
						+ "import lombok.AllArgsConstructor;\n" //
						+ "import lombok.Data;\n" //
						+ "import lombok.EqualsAndHashCode;\n" //
						+ "import lombok.Generated;\n" //
						+ "import lombok.Getter;\n" //
						+ "\n" //
						+ "/**\n" //
						+ " * An object to hold data during the session.\n" //
						+ " *\n" //
						+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
						+ " */\n" //
						+ "@Component\n" //
						+ "@Data\n" //
						+ "@Generated\n" //
						+ "@VaadinSessionScope\n" //
						+ "public class SessionData {\n" //
						+ "\n" //
						+ "	@AllArgsConstructor\n" //
						+ "	@EqualsAndHashCode\n" //
						+ "	@Getter\n" //
						+ "	public static class ReturnUrlData {\n" //
						+ "\n" //
						+ "		public ReturnUrlData(String url) {\n" //
						+ "			this(url, Map.of());\n" //
						+ "		}\n" //
						+ "\n" //
						+ "		private String url;\n" //
						+ "		private Map<String, List<String>> parameters;\n" //
						+ "\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private static int counter = 0;\n" //
						+ "\n" //
						+ "	private SessionId id = new SessionId(\"application-name-\" + (counter++));\n" //
						+ "	private LocalizationSO localization = LocalizationSO.DE;\n" //
						+ "	private Map<String, Object> parameters = new HashMap<>();\n" //
						+ "	private Stack<ReturnUrlData> returnUrlDatas = new Stack<>();\n" //
						+ "\n" //
						+ "	public void addReturnUrl(String url) {\n" //
						+ "		addReturnUrl(url, Map.of());\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public void addReturnUrl(String url, Map<String, List<String>> parameters) {\n" //
						+ "		ensure(url != null, \"url cannot be null!\");\n" //
						+ "		ensure(!url.isEmpty(), \"url cannot be empty!\");\n" //
						+ "		ensure(parameters != null, \"parameters cannot be null!\");\n" //
						+ "		returnUrlDatas.push(new ReturnUrlData(url, parameters));\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public Optional<ReturnUrlData> getReturnUrl() {\n" //
						+ "		return Optional.ofNullable(!returnUrlDatas.isEmpty() ? returnUrlDatas.pop() : null);\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public String getUserName() {\n" //
						+ "		return \"N/A\";\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public Optional<Object> findParameter(String id) {\n" //
						+ "		return Optional.ofNullable(parameters.get(id));\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public void setParameter(String id, Object obj) {\n" //
						+ "		parameters.put(id, obj);\n" //
						+ "	}\n" //
						+ "\n" //
						+ "}";
			}

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected();
				DataModel dataModel = readDataModel("Model.xml");
				dataModel.setApplicationName(APPLICATION_NAME);
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class CubeApplication {

			private String getExpected() {
				return "package base.pack.age.name.gui;\n" //
						+ "\n" //
						+ "import static base.pack.age.name.util.Check.ensure;\n" //
						+ "\n" //
						+ "import java.time.LocalDateTime;\n" //
						+ "import java.util.HashMap;\n" //
						+ "import java.util.List;\n" //
						+ "import java.util.Map;\n" //
						+ "import java.util.Optional;\n" //
						+ "import java.util.Stack;\n" //
						+ "\n" //
						+ "import org.springframework.stereotype.Component;\n" //
						+ "\n" //
						+ "import com.vaadin.flow.spring.annotation.VaadinSessionScope;\n" //
						+ "\n" //
						+ "import base.pack.age.name.core.model.localization.LocalizationSO;\n" //
						+ "import base.pack.age.name.core.service.JWTService.AuthorizationData;\n" //
						+ "import base.pack.age.name.gui.AccessChecker;\n" //
						+ "\n" //
						+ "import lombok.AllArgsConstructor;\n" //
						+ "import lombok.Data;\n" //
						+ "import lombok.EqualsAndHashCode;\n" //
						+ "import lombok.Generated;\n" //
						+ "import lombok.Getter;\n" //
						+ "\n" //
						+ "/**\n" //
						+ " * An object to hold data during the session.\n" //
						+ " *\n" //
						+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
						+ " */\n" //
						+ "@Component\n" //
						+ "@Data\n" //
						+ "@Generated\n" //
						+ "@VaadinSessionScope\n" //
						+ "public class SessionData {\n" //
						+ "\n" //
						+ "	@AllArgsConstructor\n" //
						+ "	@EqualsAndHashCode\n" //
						+ "	@Getter\n" //
						+ "	public static class ReturnUrlData {\n" //
						+ "\n" //
						+ "		public ReturnUrlData(String url) {\n" //
						+ "			this(url, Map.of());\n" //
						+ "		}\n" //
						+ "\n" //
						+ "		private String url;\n" //
						+ "		private Map<String, List<String>> parameters;\n" //
						+ "\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private static int counter = 0;\n" //
						+ "\n" //
						+ "	private AccessChecker accessChecker;\n" //
						+ "	private AuthorizationData authorizationData;\n" //
						+ "	private SessionId id = new SessionId(\"application-name-\" + (counter++));\n" //
						+ "	private LocalizationSO localization = LocalizationSO.DE;\n" //
						+ "	private Map<String, Object> parameters = new HashMap<>();\n" //
						+ "	private Stack<ReturnUrlData> returnUrlDatas = new Stack<>();\n" //
						+ "	private LocalDateTime validUntil;\n" //
						+ "\n" //
						+ "	public void addReturnUrl(String url) {\n" //
						+ "		addReturnUrl(url, Map.of());\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public void addReturnUrl(String url, Map<String, List<String>> parameters) {\n" //
						+ "		ensure(url != null, \"url cannot be null!\");\n" //
						+ "		ensure(!url.isEmpty(), \"url cannot be empty!\");\n" //
						+ "		ensure(parameters != null, \"parameters cannot be null!\");\n" //
						+ "		returnUrlDatas.push(new ReturnUrlData(url, parameters));\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public Optional<ReturnUrlData> getReturnUrl() {\n" //
						+ "		return Optional.ofNullable(!returnUrlDatas.isEmpty() ? returnUrlDatas.pop() : null);\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public String getUserName() {\n" //
						+ "		return authorizationData.getUser().getName();\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public Optional<Object> findParameter(String id) {\n" //
						+ "		return Optional.ofNullable(parameters.get(id));\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	public void setParameter(String id, Object obj) {\n" //
						+ "		parameters.put(id, obj);\n" //
						+ "	}\n" //
						+ "\n" //
						+ "}";
			}

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected();
				DataModel dataModel = readDataModel("Model.xml");
				dataModel.setApplicationName(APPLICATION_NAME);
				dataModel.addOption(new Option(AbstractGUIVaadinClassCodeGenerator.CUBE_APPLICATION));
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}
