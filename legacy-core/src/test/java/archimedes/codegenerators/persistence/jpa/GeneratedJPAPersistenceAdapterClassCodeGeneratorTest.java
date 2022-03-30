package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
class GeneratedJPAPersistenceAdapterClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private GeneratedJPAPersistenceAdapterClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected(null, "persistence", false, "null");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, String noKeyValue) {
			return getExpected(prefix, packageName, suppressComment, noKeyValue, null);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, String noKeyValue,
				Boolean findByDescriptionUnique) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
							"\n";
			if (findByDescriptionUnique == Boolean.TRUE) {
				s += "import static base.pack.age.name.util.Check.ensure;\n" + //
						"\n";
			}
			s += "import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import javax.annotation.PostConstruct;\n" + //
					"import javax.inject.Inject;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.Page;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.model.ATable;\n";
			if (findByDescriptionUnique == Boolean.TRUE) {
				s += "import base.pack.age.name.core.service.exception.UniqueConstraintViolationException;\n";
			}
			s += "import base.pack.age.name.core.service.port.persistence.ATablePersistencePort;\n" + //
					"import base.pack.age.name.persistence.converter.PageConverter;\n" + //
					"import base.pack.age.name.persistence.converter.PageParametersToPageableConverter;\n" + //
					"import base.pack.age.name.persistence.converter.ATableDBOConverter;\n" + //
					"import base.pack.age.name.persistence.entity.ATableDBO;\n" + //
					"import base.pack.age.name.persistence.repository.ATableDBORepository;\n" + //
					"import lombok.Generated;\n" + //
					"\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A generated JPA persistence adapter for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s += "@Generated\n" + //
					"public abstract class ATableGeneratedJPAPersistenceAdapter implements ATablePersistencePort {\n" + //
					"\n" + //
					"	@Inject\n" + //
					"	protected ATableDBOConverter converter;\n" + //
					"	@Inject\n" + //
					"	protected ATableDBORepository repository;\n" + //
					"\n" + //
					"	@Inject\n" + //
					"	protected PageParametersToPageableConverter pageParametersToPageableConverter;\n" + //
					"\n" + //
					"	protected PageConverter<ATable, ATableDBO> pageConverter;\n" + //
					"\n" + //
					"	@PostConstruct\n" + //
					"	public void postConstruct() {\n" + //
					"		pageConverter = new PageConverter<>(converter);\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable create(ATable model) {\n" + //
					"		model.setId(" + noKeyValue + ");\n" + //
					"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<ATable> findAll() {\n" + //
					"		return converter.toModel(repository.findAll());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public Page<ATable> findAll(PageParameters pageParameters) {\n" + //
					"		return pageConverter.convert(repository.findAll(pageParametersToPageableConverter.convert(pageParameters)));\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public Optional<ATable> findById(Long id) {\n" + //
					"		return repository.findById(id).map(dbo -> converter.toModel(dbo));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable update(ATable model) {\n";
			if (findByDescriptionUnique == Boolean.TRUE) {
				s += "		ensure(\n" + //
						"				findByDescription(model.getDescription())\n" + //
						"						.filter(aTable -> !aTable.getId().equals(model.getId()))\n"
						+ //
						"						.isEmpty(),\n" + //
				        "				() -> new UniqueConstraintViolationException(\"description '\" + model.getDescription() + \"' is already set for another record\", \"ATable\", \"description\"));\n";
			}
			s += "		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void delete(ATable model) {\n" + //
					"		repository.deleteById(model.getId());\n" + //
					"	}\n";
			if (findByDescriptionUnique != null) {
				if (findByDescriptionUnique) {
					s += "\n" + //
							"	@Override\n" + //
							"	public Optional<ATable> findByDescription(String description) {\n" + //
							"		return Optional.ofNullable(converter.toModel(repository.findByDescription(description).orElse(null)));\n"
							+ //
							"	}\n";
				} else {
					s += "\n" + //
							"	@Override\n" + //
							"	public List<ATable> findAllByDescription(String description) {\n" + //
							"		return converter.toModel(repository.findAllByDescription(description));\n" + //
							"	}\n";
				}
			}
			s += "\n}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComments() {
			// Prepare
			String expected = getExpected(null, "persistence", true, "null");
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithNoSimplePK() {
			// Prepare
			String expected = getExpected(null, "persistence", false, "-1");
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("ID").setNotNull(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithAFindByOptionSet() {
			// Prepare
			String expected = getExpected(null, "persistence", false, null, true);
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			ColumnModel column = table.getColumnByName("Description");
			column.addOption(new Option("FIND_BY"));
			column.setUnique(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithAFindByOptionSetWithoutUnique() {
			// Prepare
			String expected = getExpected(null, "persistence", false, null, false);
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			ColumnModel column = table.getColumnByName("Description");
			column.addOption(new Option("FIND_BY"));
			column.setUnique(false);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpectedForObjectReferences(boolean unique) {
			String expected = "package base.pack.age.name.persistence;\n" + //
					"\n";
			if (unique) {
				expected += "import static base.pack.age.name.util.Check.ensure;\n" + //
						"\n";
			}
			expected += "import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import javax.annotation.PostConstruct;\n" + //
					"import javax.inject.Inject;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.Page;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.model.ATable;\n";
			if (unique) {
				expected += "import base.pack.age.name.core.service.exception.UniqueConstraintViolationException;\n";
			}
			expected += "import base.pack.age.name.core.service.port.persistence.ATablePersistencePort;\n" + //
					"import base.pack.age.name.persistence.converter.PageConverter;\n" + //
					"import base.pack.age.name.persistence.converter.PageParametersToPageableConverter;\n" + //
					"import base.pack.age.name.persistence.converter.ATableDBOConverter;\n" + //
					"import base.pack.age.name.persistence.entity.ATableDBO;\n" + //
					"import base.pack.age.name.persistence.repository.ATableDBORepository;\n" + //
					"import base.pack.age.name.persistence.converter.AnotherTableDBOConverter;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.AnotherTable;\n" + //
					"\n" + //
					"/**\n" + //
					" * A generated JPA persistence adapter for a_tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"public abstract class ATableGeneratedJPAPersistenceAdapter implements ATablePersistencePort {\n" + //
					"\n" + //
					"	@Inject\n" + //
					"	protected ATableDBOConverter converter;\n" + //
					"	@Inject\n" + //
					"	protected ATableDBORepository repository;\n" + //
					"	@Inject\n" + //
					"	protected AnotherTableDBOConverter anotherTableDBOConverter;\n" + //
					"\n" + //
					"	@Inject\n" + //
					"	protected PageParametersToPageableConverter pageParametersToPageableConverter;\n" + //
					"\n" + //
					"	protected PageConverter<ATable, ATableDBO> pageConverter;\n" + //
					"\n" + //
					"	@PostConstruct\n" + //
					"	public void postConstruct() {\n" + //
					"		pageConverter = new PageConverter<>(converter);\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable create(ATable model) {\n" + //
					"		model.setId(null);\n" + //
					"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<ATable> findAll() {\n" + //
					"		return converter.toModel(repository.findAll());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public Page<ATable> findAll(PageParameters pageParameters) {\n" + //
					"		return pageConverter.convert(repository.findAll(pageParametersToPageableConverter.convert(pageParameters)));\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public Optional<ATable> findById(Long id) {\n" + //
					"		return repository.findById(id).map(dbo -> converter.toModel(dbo));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public ATable update(ATable model) {\n";
			if (unique) {
				expected += "		ensure(\n" + //
						"				findByRef(model.getRef())\n" + //
						"						.filter(aTable -> !aTable.getId().equals(model.getId()))\n" + //
						"						.isEmpty(),\n" + //
				        "				() -> new UniqueConstraintViolationException(\"ref '\" + model.getRef() + \"' is already set for another record\", \"ATable\", \"ref\"));\n";
			}
			expected += "		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void delete(ATable model) {\n" + //
					"		repository.deleteById(model.getId());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n";
			if (unique) {
				expected += "	public Optional<ATable> findByRef(AnotherTable ref) {\n" + //
						"		return Optional.ofNullable(converter.toModel(repository.findByRef(anotherTableDBOConverter.toDBO(ref)).orElse(null)));\n";
			} else {
				expected += "	public List<ATable> findAllByRef(AnotherTable ref) {\n" + //
						"		return converter.toModel(repository.findAllByRef(anotherTableDBOConverter.toDBO(ref)));\n";
			}
			expected += "	}\n" + //
					"\n" + //
					"}";
			return expected;
		}

		@Test
		void happyRunForASimpleObjectWithFindByOptionSetAnObjectReference() {
			// Prepare
			String expected = getExpectedForObjectReferences(false);
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.REFERENCE_MODE,
									AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT));
			TableModel table = dataModel.getTableByName("A_TABLE");
			ColumnModel column = table.getColumnByName("REF");
			column.addOption(new Option("FIND_BY"));
			column.setUnique(false);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithFindByOptionSetAnObjectReferenceUnique() {
			// Prepare
			String expected = getExpectedForObjectReferences(true);
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.REFERENCE_MODE,
									AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT));
			TableModel table = dataModel.getTableByName("A_TABLE");
			ColumnModel column = table.getColumnByName("REF");
			column.addOption(new Option("FIND_BY"));
			column.setUnique(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Nested
		class NotNullConstraints {

			private String getExpected(String packageName) {
				String s = "package " + BASE_PACKAGE_NAME + "." + packageName + ";\n" + //
						"\n" + //
						"import static base.pack.age.name.util.Check.ensure;\n" + //
						"\n" + //
						"import java.util.List;\n" + //
						"import java.util.Optional;\n" + //
						"\n" + //
						"import javax.annotation.PostConstruct;\n" + //
						"import javax.inject.Inject;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.exception.NotNullConstraintViolationException;\n" + //
						"import base.pack.age.name.core.service.port.persistence.ATablePersistencePort;\n" + //
						"import base.pack.age.name.persistence.converter.PageConverter;\n" + //
						"import base.pack.age.name.persistence.converter.PageParametersToPageableConverter;\n" + //
						"import base.pack.age.name.persistence.converter.ATableDBOConverter;\n" + //
						"import base.pack.age.name.persistence.entity.ATableDBO;\n" + //
						"import base.pack.age.name.persistence.repository.ATableDBORepository;\n" + //
						"import lombok.Generated;\n" + //
						"\n" + //
						"/**\n" + //
						" * A generated JPA persistence adapter for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n" + //
						"@Generated\n" + //
						"public abstract class ATableGeneratedJPAPersistenceAdapter implements ATablePersistencePort {\n"
						+ //
						"\n" + //
						"	@Inject\n" + //
						"	protected ATableDBOConverter converter;\n" + //
						"	@Inject\n" + //
						"	protected ATableDBORepository repository;\n" + //
						"\n" + //
						"	@Inject\n" + //
						"	protected PageParametersToPageableConverter pageParametersToPageableConverter;\n" + //
						"\n" + //
						"	protected PageConverter<ATable, ATableDBO> pageConverter;\n" + //
						"\n" + //
						"	@PostConstruct\n" + //
						"	public void postConstruct() {\n" + //
						"		pageConverter = new PageConverter<>(converter);\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable create(ATable model) {\n" + //
						"		model.setId(-1);\n" + //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public List<ATable> findAll() {\n" + //
						"		return converter.toModel(repository.findAll());\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public Page<ATable> findAll(PageParameters pageParameters) {\n" + //
						"		return pageConverter.convert(repository.findAll(pageParametersToPageableConverter.convert(pageParameters)));\n"
						+ //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public Optional<ATable> findById(Long id) {\n" + //
						"		return repository.findById(id).map(dbo -> converter.toModel(dbo));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable update(ATable model) {\n" + //
						"		ensure(\n" + //
						"				model.getDescription() != null,\n" + //
						"				() -> new NotNullConstraintViolationException(\"ATable field description cannot be null.\", \"ATable\", \"description\"));\n"
						+ //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void delete(ATable model) {\n" + //
						"		repository.deleteById(model.getId());\n" + //
						"	}\n";
				s += "\n}";
				return s;
			}

			@Test
			void happyRunForASimpleObjectWithNotNullSet() {
				// Prepare
				String expected = getExpected("persistence");
				DataModel dataModel = readDataModel("Model.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				table.getColumnByName("ID").setNotNull(true);
				table.getColumnByName("Description").setNotNull(true);
				ColumnModel column = new Tabellenspalte("order", table.getColumnByName("ID").getDomain());
				column.setNotNull(true);
				table.addColumn(column);
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class UniqueConstraints {

			private String getExpected(String packageName, boolean simpleTypeId) {
				String s = "package " + BASE_PACKAGE_NAME + "." + packageName + ";\n" + //
						"\n" + //
						"import static base.pack.age.name.util.Check.ensure;\n" + //
						"\n" + //
						"import java.util.List;\n" + //
						"import java.util.Optional;\n" + //
						"\n" + //
						"import javax.annotation.PostConstruct;\n" + //
						"import javax.inject.Inject;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.exception.UniqueConstraintViolationException;\n" + //
						"import base.pack.age.name.core.service.port.persistence.ATablePersistencePort;\n" + //
						"import base.pack.age.name.persistence.converter.PageConverter;\n" + //
						"import base.pack.age.name.persistence.converter.PageParametersToPageableConverter;\n" + //
						"import base.pack.age.name.persistence.converter.ATableDBOConverter;\n" + //
						"import base.pack.age.name.persistence.entity.ATableDBO;\n" + //
						"import base.pack.age.name.persistence.repository.ATableDBORepository;\n" + //
						"import lombok.Generated;\n" + //
						"\n" + //
						"/**\n" + //
						" * A generated JPA persistence adapter for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n" + //
						"@Generated\n" + //
						"public abstract class ATableGeneratedJPAPersistenceAdapter implements ATablePersistencePort {\n"
						+ //
						"\n" + //
						"	@Inject\n" + //
						"	protected ATableDBOConverter converter;\n" + //
						"	@Inject\n" + //
						"	protected ATableDBORepository repository;\n" + //
						"\n" + //
						"	@Inject\n" + //
						"	protected PageParametersToPageableConverter pageParametersToPageableConverter;\n" + //
						"\n" + //
						"	protected PageConverter<ATable, ATableDBO> pageConverter;\n" + //
						"\n" + //
						"	@PostConstruct\n" + //
						"	public void postConstruct() {\n" + //
						"		pageConverter = new PageConverter<>(converter);\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable create(ATable model) {\n" + //
						"		model.setId(" + (simpleTypeId ? "-1" : "null") + ");\n" + //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public List<ATable> findAll() {\n" + //
						"		return converter.toModel(repository.findAll());\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public Page<ATable> findAll(PageParameters pageParameters) {\n" + //
						"		return pageConverter.convert(repository.findAll(pageParametersToPageableConverter.convert(pageParameters)));\n"
						+ //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public Optional<ATable> findById(Long id) {\n" + //
						"		return repository.findById(id).map(dbo -> converter.toModel(dbo));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable update(ATable model) {\n" + //
						"		ensure(\n" + //
						"				findByDescription(model.getDescription())\n";
				if (simpleTypeId) {
					s += "						.filter(aTable -> aTable.getId() != model.getId())\n";
				} else {
					s += "						.filter(aTable -> !aTable.getId().equals(model.getId()))\n";
				}
				s +=
						"						.isEmpty(),\n" + //
				                "				() -> new UniqueConstraintViolationException(\"description '\" + model.getDescription() + \"' is already set for another record\", \"ATable\", \"description\"));\n"
						+ //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void delete(ATable model) {\n" + //
						"		repository.deleteById(model.getId());\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public Optional<ATable> findByDescription(String description) {\n" + //
						"		return Optional.ofNullable(converter.toModel(repository.findByDescription(description).orElse(null)));\n"
						+ //
						"	}\n" + //
						"\n" + //
						"}";
				return s;
			}

			@Test
			void happyRunForASimpleObjectWithUniqueSet() {
				// Prepare
				String expected = getExpected("persistence", false);
				DataModel dataModel = readDataModel("Model.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				table.getColumnByName("Description").setUnique(true);
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void happyRunForASimpleObjectWithUniqueSet_SimpleIdType() {
				// Prepare
				String expected = getExpected("persistence", true);
				DataModel dataModel = readDataModel("Model.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				table.getColumnByName("ID").setNotNull(true);
				table.getColumnByName("Description").setUnique(true);
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class ListAccess {

			private String getExpected(String packageName) {
				String s = "package " + BASE_PACKAGE_NAME + "." + packageName + ";\n" + //
						"\n" + //
						"import java.util.List;\n" + //
						"import java.util.Optional;\n" + //
						"\n" + //
						"import javax.annotation.PostConstruct;\n" + //
						"import javax.inject.Inject;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.model.AnotherTable;\n" + //
						"import base.pack.age.name.core.service.port.persistence.ATablePersistencePort;\n" + //
						"import base.pack.age.name.persistence.converter.PageConverter;\n" + //
						"import base.pack.age.name.persistence.converter.PageParametersToPageableConverter;\n" + //
						"import base.pack.age.name.persistence.converter.ATableDBOConverter;\n" + //
						"import base.pack.age.name.persistence.converter.AnotherTableDBOConverter;\n" + //
						"import base.pack.age.name.persistence.entity.ATableDBO;\n" + //
						"import base.pack.age.name.persistence.repository.ATableDBORepository;\n" + //
						"import lombok.Generated;\n" + //
						"\n" + //
						"/**\n" + //
						" * A generated JPA persistence adapter for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n" + //
						"@Generated\n" + //
						"public abstract class ATableGeneratedJPAPersistenceAdapter implements ATablePersistencePort {\n"
						+ //
						"\n" + //
						"	@Inject\n" + //
						"	protected ATableDBOConverter converter;\n" + //
						"	@Inject\n" + //
						"	protected AnotherTableDBOConverter anotherTableDBOConverter;\n" + //
						"	@Inject\n" + //
						"	protected ATableDBORepository repository;\n" + //
						"\n" + //
						"	@Inject\n" + //
						"	protected PageParametersToPageableConverter pageParametersToPageableConverter;\n" + //
						"\n" + //
						"	protected PageConverter<ATable, ATableDBO> pageConverter;\n" + //
						"\n" + //
						"	@PostConstruct\n" + //
						"	public void postConstruct() {\n" + //
						"		pageConverter = new PageConverter<>(converter);\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable create(ATable model) {\n" + //
						"		model.setId(null);\n" + //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public List<ATable> findAll() {\n" + //
						"		return converter.toModel(repository.findAll());\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public Page<ATable> findAll(PageParameters pageParameters) {\n" + //
						"		return pageConverter.convert(repository.findAll(pageParametersToPageableConverter.convert(pageParameters)));\n"
						+ //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public Optional<ATable> findById(Long id) {\n" + //
						"		return repository.findById(id).map(dbo -> converter.toModel(dbo));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public ATable update(ATable model) {\n" + //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void delete(ATable model) {\n" + //
						"		repository.deleteById(model.getId());\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public List<ATable> findAllByRef(AnotherTable ref) {\n" + //
						"		return converter.toModel(repository.findAllByRef(anotherTableDBOConverter.toDBO(ref)));\n"
						+ //
						"	}\n" + //
						"\n" + //
						"}";
				return s;
			}

			@Test
			void happyRunForASimpleObjectWithUniqueSet() {
				// Prepare
				String expected = getExpected("persistence");
				DataModel dataModel = readDataModel("Model-ForeignKey.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				dataModel
						.addOption(
								new Option(
										AbstractClassCodeGenerator.REFERENCE_MODE,
										AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT));
				dataModel.getTableByName("A_TABLE").getColumnByName("REF").addOption(new Option("LIST_ACCESS"));
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

	@Nested
	class TestsOfMethod_isToIgnoreFo_DataModel_TableModel {

		@Test
		void passTableModelAsNullValue_ThrowsAnException() {
			// Prepare
			DataModel model = mock(DataModel.class);
			// Run & Check
			assertThrows(NullPointerException.class, () -> unitUnderTest.isToIgnoreFor(model, null));
		}

		@Test
		void passPassAModelWithNoDependentAttribute_ReturnsFalse() {
			// Prepare
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			// Run & Check
			assertFalse(unitUnderTest.isToIgnoreFor(dataModel, table));
		}

		@Test
		void passPassAModelWithDependentAttribute_ReturnsTrue() {
			// Prepare
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table
					.getColumnByName("Description")
					.addOption(new Option(JPAPersistenceAdapterDependentClassCodeGenerator.DEPENDENT_ATTRIBUTE));
			// Run & Check
			assertTrue(unitUnderTest.isToIgnoreFor(dataModel, table));
		}

	}

}