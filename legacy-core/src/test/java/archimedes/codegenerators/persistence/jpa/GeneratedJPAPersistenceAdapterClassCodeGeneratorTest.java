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
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.GlobalIdType;
import archimedes.codegenerators.service.ServiceInterfaceCodeGenerator;
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
			s += "import java.util.ArrayList;\n" + //
					"import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import javax.annotation.PostConstruct;\n" + //
					"import javax.inject.Inject;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.Page;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.model.ATable;\n";
			s += "import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n";
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
					"	public ATable update(ATable model) {\n" + //
					"		ensureNoViolationsFound(model);\n" + //
					"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void ensureNoViolationsFound(ATable model) {\n" + //
					"		List<ValidationFailure> failures = new ArrayList<>();\n";//
			if (findByDescriptionUnique == Boolean.TRUE) {
				s +=
						"		if (!findByDescription(model.getDescription())\n" //
								+ "				.filter(aTable -> !aTable.getId().equals(model.getId()))\n" //
								+ "				.isEmpty()) {\n" //
								+ "			failures.add(new ValidationFailure(Reason.UNIQUE, \"ATable\", \"description\"));\n" //
								+ "		}\n";
			}
			s += "		if (!failures.isEmpty()) {\n" + //
					"			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void delete(ATable model) {\n" + //
					"		repository.delete(converter.toDBO(model));\n" + //
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
		void happyRunForASimpleObjectWithNoNumberPK() {
			// Prepare
			String expected =
					"package base.pack.age.name.persistence;\n" //
							+ "\n" //
							+ "import java.util.ArrayList;\n" //
							+ "import java.util.List;\n" //
							+ "import java.util.Optional;\n" //
							+ "\n" //
							+ "import javax.annotation.PostConstruct;\n" //
							+ "import javax.inject.Inject;\n" //
							+ "\n" //
							+ "import base.pack.age.name.core.model.Page;\n" //
							+ "import base.pack.age.name.core.model.PageParameters;\n" //
							+ "import base.pack.age.name.core.model.NoNumberPkTable;\n" //
							+ "import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" //
							+ "import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" //
							+ "import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n" //
							+ "import base.pack.age.name.core.service.port.persistence.NoNumberPkTablePersistencePort;\n" //
							+ "import base.pack.age.name.persistence.converter.PageConverter;\n" //
							+ "import base.pack.age.name.persistence.converter.PageParametersToPageableConverter;\n" //
							+ "import base.pack.age.name.persistence.converter.NoNumberPkTableDBOConverter;\n" //
							+ "import base.pack.age.name.persistence.entity.NoNumberPkTableDBO;\n" //
							+ "import base.pack.age.name.persistence.repository.NoNumberPkTableDBORepository;\n" //
							+ "import lombok.Generated;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * A generated JPA persistence adapter for no_number_pk_tables.\n" //
							+ " *\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Generated\n" //
							+ "public abstract class NoNumberPkTableGeneratedJPAPersistenceAdapter implements NoNumberPkTablePersistencePort {\n" //
							+ "\n" //
							+ "	@Inject\n" //
							+ "	protected NoNumberPkTableDBOConverter converter;\n" //
							+ "	@Inject\n" //
							+ "	protected NoNumberPkTableDBORepository repository;\n" //
							+ "\n" //
							+ "	@Inject\n" //
							+ "	protected PageParametersToPageableConverter pageParametersToPageableConverter;\n" //
							+ "\n" //
							+ "	protected PageConverter<NoNumberPkTable, NoNumberPkTableDBO> pageConverter;\n" //
							+ "\n" //
							+ "	@PostConstruct\n" //
							+ "	public void postConstruct() {\n" //
							+ "		pageConverter = new PageConverter<>(converter);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public NoNumberPkTable create(NoNumberPkTable model) {\n" //
							+ "		model.setId(null);\n" //
							+ "		return converter.toModel(repository.save(converter.toDBO(model)));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public List<NoNumberPkTable> findAll() {\n" //
							+ "		return converter.toModel(repository.findAll());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public Page<NoNumberPkTable> findAll(PageParameters pageParameters) {\n" //
							+ "		return pageConverter.convert(repository.findAll(pageParametersToPageableConverter.convert(pageParameters)));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public Optional<NoNumberPkTable> findById(String id) {\n" //
							+ "		return repository.findById(id).map(dbo -> converter.toModel(dbo));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public NoNumberPkTable update(NoNumberPkTable model) {\n" //
							+ "		ensureNoViolationsFound(model);\n" //
							+ "		return converter.toModel(repository.save(converter.toDBO(model)));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void ensureNoViolationsFound(NoNumberPkTable model) {\n" //
							+ "		List<ValidationFailure> failures = new ArrayList<>();\n" //
							+ "		if (!failures.isEmpty()) {\n" //
							+ "			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" //
							+ "		}\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public void delete(NoNumberPkTable model) {\n" //
							+ "		repository.delete(converter.toDBO(model));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("NO_NUMBER_PK_TABLE");
			table.getColumnByName("ID").setNotNull(true);
			// Run
			String returned =
					unitUnderTest
							.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("NO_NUMBER_PK_TABLE"));
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
			expected += "import java.util.ArrayList;\n" + //
					"import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import javax.annotation.PostConstruct;\n" + //
					"import javax.inject.Inject;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.Page;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n"
					+ //
					"import base.pack.age.name.core.service.port.persistence.ATablePersistencePort;\n" + //
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
					"	public ATable update(ATable model) {\n" + //
					"		ensureNoViolationsFound(model);\n" + //
					"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void ensureNoViolationsFound(ATable model) {\n" + //
					"		List<ValidationFailure> failures = new ArrayList<>();\n";
			if (unique) {
				expected += "		if (!findByRef(model.getRef())\n" + //
						"				.filter(aTable -> !aTable.getId().equals(model.getId()))\n" + //
						"				.isEmpty()) {\n" + //
						"			failures.add(new ValidationFailure(Reason.UNIQUE, \"ATable\", \"ref\"));\n" + //
						"		}\n";
			}
			expected += "		if (!failures.isEmpty()) {\n" + //
					"			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void delete(ATable model) {\n" + //
					"		repository.delete(converter.toDBO(model));\n" + //
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
		void happyRunForASimpleObjectWithFindByOptionSetAnEnumType() {
			// Prepare
			String expected = "package base.pack.age.name.persistence;\n" + //
					"\n" + //
					"import java.util.ArrayList;\n" + //
					"import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import javax.annotation.PostConstruct;\n" + //
					"import javax.inject.Inject;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.Page;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.model.TableWithEnumType;\n" + //
					"import base.pack.age.name.core.model.EnumType;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n"
					+ //
					"import base.pack.age.name.core.service.port.persistence.TableWithEnumTypePersistencePort;\n" + //
					"import base.pack.age.name.persistence.converter.PageConverter;\n" + //
					"import base.pack.age.name.persistence.converter.PageParametersToPageableConverter;\n" + //
					"import base.pack.age.name.persistence.converter.TableWithEnumTypeDBOConverter;\n" + //
					"import base.pack.age.name.persistence.converter.EnumTypeDBOConverter;\n" + //
					"import base.pack.age.name.persistence.entity.TableWithEnumTypeDBO;\n" + //
					"import base.pack.age.name.persistence.repository.TableWithEnumTypeDBORepository;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"/**\n" + //
					" * A generated JPA persistence adapter for table_with_enum_types.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"public abstract class TableWithEnumTypeGeneratedJPAPersistenceAdapter implements TableWithEnumTypePersistencePort {\n"
					+ //
					"\n" + //
					"	@Inject\n" + //
					"	protected TableWithEnumTypeDBOConverter converter;\n" + //
					"	@Inject\n" + //
					"	protected EnumTypeDBOConverter enumTypeDBOConverter;\n" + //
					"	@Inject\n" + //
					"	protected TableWithEnumTypeDBORepository repository;\n" + //
					"\n" + //
					"	@Inject\n" + //
					"	protected PageParametersToPageableConverter pageParametersToPageableConverter;\n" + //
					"\n" + //
					"	protected PageConverter<TableWithEnumType, TableWithEnumTypeDBO> pageConverter;\n" + //
					"\n" + //
					"	@PostConstruct\n" + //
					"	public void postConstruct() {\n" + //
					"		pageConverter = new PageConverter<>(converter);\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public TableWithEnumType create(TableWithEnumType model) {\n" + //
					"		model.setId(null);\n" + //
					"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<TableWithEnumType> findAll() {\n" + //
					"		return converter.toModel(repository.findAll());\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public Page<TableWithEnumType> findAll(PageParameters pageParameters) {\n" + //
					"		return pageConverter.convert(repository.findAll(pageParametersToPageableConverter.convert(pageParameters)));\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public Optional<TableWithEnumType> findById(Long id) {\n" + //
					"		return repository.findById(id).map(dbo -> converter.toModel(dbo));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public TableWithEnumType update(TableWithEnumType model) {\n" + //
					"		ensureNoViolationsFound(model);\n" + //
					"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void ensureNoViolationsFound(TableWithEnumType model) {\n" //
					+ "		List<ValidationFailure> failures = new ArrayList<>();\n" //
					+ "		if (!failures.isEmpty()) {\n" //
					+ "			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" //
					+ "		}\n" //
					+ "	}\n" //
					+ "\n" //
					+ "	@Override\n" + //
					"	public void delete(TableWithEnumType model) {\n" + //
					"		repository.delete(converter.toDBO(model));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public List<TableWithEnumType> findAllByEnumField(EnumType enumField) {\n" + //
					"		return converter.toModel(repository.findAllByEnumField(enumTypeDBOConverter.toDBO(enumField)));\n"
					+ //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("TABLE_WITH_ENUM_TYPE");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
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
						"import java.util.ArrayList;\n" + //
						"import java.util.List;\n" + //
						"import java.util.Optional;\n" + //
						"\n" + //
						"import javax.annotation.PostConstruct;\n" + //
						"import javax.inject.Inject;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n"
						+ //
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
						"		ensureNoViolationsFound(model);\n" + //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	private void ensureNoViolationsFound(ATable model) {\n" //
						+ "		List<ValidationFailure> failures = new ArrayList<>();\n" //
						+ "		if (model.getDescription() == null) {\n" //
						+ "			failures.add(new ValidationFailure(Reason.NOT_NULL, \"ATable\", \"description\"));\n" //
						+ "		}\n" //
						+ "		if (!failures.isEmpty()) {\n" //
						+ "			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" //
						+ "		}\n" //
						+ "	}\n" //
						+ "\n" + //
						"	@Override\n" + //
						"	public void delete(ATable model) {\n" + //
						"		repository.delete(converter.toDBO(model));\n" + //
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
						"import java.util.ArrayList;\n" + //
						"import java.util.List;\n" + //
						"import java.util.Optional;\n" + //
						"\n" + //
						"import javax.annotation.PostConstruct;\n" + //
						"import javax.inject.Inject;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n"
						+ //
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
						"		ensureNoViolationsFound(model);\n" + //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	private void ensureNoViolationsFound(ATable model) {\n" //
						+ "		List<ValidationFailure> failures = new ArrayList<>();\n" //
						+ "		if (!findByDescription(model.getDescription())\n";
				if (simpleTypeId) {
					s += "				.filter(aTable -> aTable.getId() != model.getId())\n";
				} else {
					s += "				.filter(aTable -> !aTable.getId().equals(model.getId()))\n";
				}
				s +=
						"				.isEmpty()) {\n"
						+ "			failures.add(new ValidationFailure(Reason.UNIQUE, \"ATable\", \"description\"));\n"
						+ "		}\n"
						+ "		if (!failures.isEmpty()) {\n" //
						+ "			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" //
						+ "		}\n" //
						+ "	}\n" //
						+ "\n" + //
						"	@Override\n" + //
						"	public void delete(ATable model) {\n" + //
						"		repository.delete(converter.toDBO(model));\n" + //
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
						"import java.util.ArrayList;\n" + //
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
						"import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n"
						+ //
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
						"		ensureNoViolationsFound(model);\n" + //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	private void ensureNoViolationsFound(ATable model) {\n" //
						+ "		List<ValidationFailure> failures = new ArrayList<>();\n" //
						+ "		if (!failures.isEmpty()) {\n" //
						+ "			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" //
						+ "		}\n" //
						+ "	}\n" //
						+ "\n" + //
						"	@Override\n" + //
						"	public void delete(ATable model) {\n" + //
						"		repository.delete(converter.toDBO(model));\n" + //
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

		@Nested
		class Inheritance {

			@Test
			void simpleInheritance() {
				// Prepare
				String expected = "package base.pack.age.name.persistence;\n" + //
						"\n" + //
						"import java.util.ArrayList;\n" + //
						"import java.util.List;\n" + //
						"import java.util.Optional;\n" + //
						"\n" + //
						"import javax.annotation.PostConstruct;\n" + //
						"import javax.inject.Inject;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.model.AnotherHeirTable;\n" + //
						"import base.pack.age.name.core.model.AnotherHeirTableWithSameReference;\n" + //
						"import base.pack.age.name.core.model.AnotherTable;\n" + //
						"import base.pack.age.name.core.model.HeirTableWithReference;\n" + //
						"import base.pack.age.name.core.model.IgnoredHeirTable;\n" + //
						"import base.pack.age.name.persistence.repository.AnotherHeirTableDBORepository;\n" + //
						"import base.pack.age.name.persistence.repository.AnotherHeirTableWithSameReferenceDBORepository;\n"
						+ //
						"import base.pack.age.name.persistence.repository.AnotherTableDBORepository;\n" + //
						"import base.pack.age.name.persistence.repository.HeirTableWithReferenceDBORepository;\n" + //
						"import base.pack.age.name.persistence.repository.IgnoredHeirTableDBORepository;\n" + //
						"import base.pack.age.name.persistence.converter.AnotherHeirTableDBOConverter;\n" + //
						"import base.pack.age.name.persistence.converter.AnotherHeirTableWithSameReferenceDBOConverter;\n"
						+ //
						"import base.pack.age.name.persistence.converter.AnotherTableDBOConverter;\n" + //
						"import base.pack.age.name.persistence.converter.HeirTableWithReferenceDBOConverter;\n" + //
						"import base.pack.age.name.persistence.converter.IgnoredHeirTableDBOConverter;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n"
						+ //
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
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"public abstract class ATableGeneratedJPAPersistenceAdapter implements ATablePersistencePort {\n"
						+ //
						"\n" + //
						"	@Inject\n" + //
						"	protected ATableDBOConverter converter;\n" + //
						"	@Inject\n" + //
						"	protected ATableDBORepository repository;\n" + //
						"	@Inject\n" + //
						"	protected AnotherHeirTableDBOConverter anotherHeirTableDBOConverter;\n" + //
						"	@Inject\n" + //
						"	protected AnotherHeirTableDBORepository anotherHeirTableDBORepository;\n" + //
						"	@Inject\n" + //
						"	protected AnotherHeirTableWithSameReferenceDBOConverter anotherHeirTableWithSameReferenceDBOConverter;\n"
						+ //
						"	@Inject\n" + //
						"	protected AnotherHeirTableWithSameReferenceDBORepository anotherHeirTableWithSameReferenceDBORepository;\n"
						+ //
						"	@Inject\n" + //
						"	protected AnotherTableDBOConverter anotherTableDBOConverter;\n" + //
						"	@Inject\n" + //
						"	protected AnotherTableDBORepository anotherTableDBORepository;\n" + //
						"	@Inject\n" + //
						"	protected HeirTableWithReferenceDBOConverter heirTableWithReferenceDBOConverter;\n" + //
						"	@Inject\n" + //
						"	protected HeirTableWithReferenceDBORepository heirTableWithReferenceDBORepository;\n" + //
						"	@Inject\n" + //
						"	protected IgnoredHeirTableDBOConverter ignoredHeirTableDBOConverter;\n" + //
						"	@Inject\n" + //
						"	protected IgnoredHeirTableDBORepository ignoredHeirTableDBORepository;\n" + //
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
						"	public List<AnotherHeirTable> findAllAnotherHeirTable() {\n" + //
						"		return anotherHeirTableDBOConverter.toModel(anotherHeirTableDBORepository.findAll());\n"
						+ //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public List<AnotherHeirTableWithSameReference> findAllAnotherHeirTableWithSameReference() {\n"
						+ //
						"		return anotherHeirTableWithSameReferenceDBOConverter.toModel(anotherHeirTableWithSameReferenceDBORepository.findAll());\n"
						+ //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public List<AnotherTable> findAllAnotherTable() {\n" + //
						"		return anotherTableDBOConverter.toModel(anotherTableDBORepository.findAll());\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public List<HeirTableWithReference> findAllHeirTableWithReference() {\n" + //
						"		return heirTableWithReferenceDBOConverter.toModel(heirTableWithReferenceDBORepository.findAll());\n"
						+ //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public List<IgnoredHeirTable> findAllIgnoredHeirTable() {\n" + //
						"		return ignoredHeirTableDBOConverter.toModel(ignoredHeirTableDBORepository.findAll());\n"
						+ //
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
						"		ensureNoViolationsFound(model);\n" + //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	private void ensureNoViolationsFound(ATable model) {\n" //
						+ "		List<ValidationFailure> failures = new ArrayList<>();\n" //
						+ "		if (!failures.isEmpty()) {\n" //
						+ "			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" //
						+ "		}\n" //
						+ "	}\n" //
						+ "\n" + "	@Override\n" + //
						"	public void delete(ATable model) {\n" + //
						"		repository.delete(converter.toDBO(model));\n" + //
						"	}\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Model-Inheritance.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void simpleInheritance_withSuppressedSubTypeSelection() {
				// Prepare
				String expected = "package base.pack.age.name.persistence;\n" + //
						"\n" + //
						"import java.util.ArrayList;\n" + //
						"import java.util.List;\n" + //
						"import java.util.Optional;\n" + //
						"\n" + //
						"import javax.annotation.PostConstruct;\n" + //
						"import javax.inject.Inject;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" + //
						"import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n"
						+ //
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
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
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
						"		ensureNoViolationsFound(model);\n" + //
						"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
						"	}\n" + //
						"\n" + //
						"	private void ensureNoViolationsFound(ATable model) {\n" //
						+ "		List<ValidationFailure> failures = new ArrayList<>();\n" //
						+ "		if (!failures.isEmpty()) {\n" //
						+ "			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" //
						+ "		}\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	@Override\n" + //
						"	public void delete(ATable model) {\n" + //
						"		repository.delete(converter.toDBO(model));\n" + //
						"	}\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Model-Inheritance.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				table.addOption(new Option(ServiceInterfaceCodeGenerator.SUPPRESS_SUBCLASS_SELECTORS));
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class GlobalIds {

			@Nested
			class UUID {

				@Test
				void unique() {
					// Prepare
					String expected =
							"package base.pack.age.name.persistence;\n" //
									+ "\n" //
									+ "import java.util.ArrayList;\n" //
									+ "import java.util.List;\n" //
									+ "import java.util.Optional;\n" //
									+ "\n" //
									+ "import javax.annotation.PostConstruct;\n" //
									+ "import javax.inject.Inject;\n" //
									+ "\n" //
									+ "import base.pack.age.name.core.model.Page;\n" //
									+ "import base.pack.age.name.core.model.PageParameters;\n" //
									+ "import base.pack.age.name.core.model.TableWithUuid;\n" //
									+ "import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" //
									+ "import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" //
									+ "import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n" //
									+ "import base.pack.age.name.core.service.port.persistence.TableWithUuidPersistencePort;\n" //
									+ "import base.pack.age.name.persistence.converter.PageConverter;\n" //
									+ "import base.pack.age.name.persistence.converter.PageParametersToPageableConverter;\n" //
									+ "import base.pack.age.name.persistence.converter.TableWithUuidDBOConverter;\n" //
									+ "import base.pack.age.name.persistence.entity.TableWithUuidDBO;\n" //
									+ "import base.pack.age.name.persistence.repository.TableWithUuidDBORepository;\n" //
									+ "import java.util.UUID;\n" //
									+ "\n" //
									+ "import lombok.Generated;\n" //
									+ "\n" //
									+ "/**\n" //
									+ " * A generated JPA persistence adapter for table_with_uuids.\n" //
									+ " *\n" //
									+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
									+ " */\n" //
									+ "@Generated\n" //
									+ "public abstract class TableWithUuidGeneratedJPAPersistenceAdapter implements TableWithUuidPersistencePort {\n" //
									+ "\n" //
									+ "	@Inject\n" //
									+ "	protected TableWithUuidDBOConverter converter;\n" //
									+ "	@Inject\n" //
									+ "	protected TableWithUuidDBORepository repository;\n" //
									+ "\n" //
									+ "	@Inject\n" //
									+ "	protected PageParametersToPageableConverter pageParametersToPageableConverter;\n" //
									+ "\n" //
									+ "	protected PageConverter<TableWithUuid, TableWithUuidDBO> pageConverter;\n" //
									+ "\n" //
									+ "	@PostConstruct\n" //
									+ "	public void postConstruct() {\n" //
									+ "		pageConverter = new PageConverter<>(converter);\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public TableWithUuid create(TableWithUuid model) {\n" //
									+ "		model.setId(null);\n" //
									+ "		return converter.toModel(repository.save(converter.toDBO(model)));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public List<TableWithUuid> findAll() {\n" //
									+ "		return converter.toModel(repository.findAll());\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public Page<TableWithUuid> findAll(PageParameters pageParameters) {\n" //
									+ "		return pageConverter.convert(repository.findAll(pageParametersToPageableConverter.convert(pageParameters)));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public Optional<TableWithUuid> findById(Long id) {\n" //
									+ "		return repository.findById(id).map(dbo -> converter.toModel(dbo));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public TableWithUuid update(TableWithUuid model) {\n" //
									+ "		ensureNoViolationsFound(model);\n" //
									+ "		return converter.toModel(repository.save(converter.toDBO(model)));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	private void ensureNoViolationsFound(TableWithUuid model) {\n" //
									+ "		List<ValidationFailure> failures = new ArrayList<>();\n" //
									+ "		if (!findByGlobalId(model.getGlobalId())\n" //
									+ "				.filter(tableWithUuid -> !tableWithUuid.getId().equals(model.getId()))\n" //
									+ "				.isEmpty()) {\n" //
									+ "			failures.add(new ValidationFailure(Reason.UNIQUE, \"TableWithUuid\", \"globalId\"));\n" //
									+ "		}\n" //
									+ "		if (!failures.isEmpty()) {\n" //
									+ "			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" //
									+ "		}\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public void delete(TableWithUuid model) {\n" //
									+ "		repository.delete(converter.toDBO(model));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public Optional<TableWithUuid> findByGlobalId(UUID globalId) {\n" //
									+ "		return Optional.ofNullable(converter.toModel(repository.findByGlobalId(globalId != null ? globalId.toString() : null).orElse(null)));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "}";
					DataModel dataModel = readDataModel("Model.xml");
					dataModel
							.getTableByName("TABLE_WITH_UUID")
							.getColumnByName("GLOBAL_ID")
							.setParameters(AbstractModelCodeGenerator.GLOBAL_ID + ":" + GlobalIdType.UUID + "|FIND_BY");
					dataModel.getTableByName("TABLE_WITH_UUID").getColumnByName("GLOBAL_ID").setUnique(true);
					// Run
					String returned =
							unitUnderTest
									.generate(
											BASE_PACKAGE_NAME,
											dataModel,
											dataModel.getTableByName("TABLE_WITH_UUID"));
					// Check
					assertEquals(expected, returned);
				}

				@Test
				void notUnique() {
					// Prepare
					String expected =
							"package base.pack.age.name.persistence;\n" //
									+ "\n" //
									+ "import java.util.ArrayList;\n" //
									+ "import java.util.List;\n" //
									+ "import java.util.Optional;\n" //
									+ "\n" //
									+ "import javax.annotation.PostConstruct;\n" //
									+ "import javax.inject.Inject;\n" //
									+ "\n" //
									+ "import base.pack.age.name.core.model.Page;\n" //
									+ "import base.pack.age.name.core.model.PageParameters;\n" //
									+ "import base.pack.age.name.core.model.TableWithUuid;\n" //
									+ "import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" //
									+ "import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" //
									+ "import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n" //
									+ "import base.pack.age.name.core.service.port.persistence.TableWithUuidPersistencePort;\n" //
									+ "import base.pack.age.name.persistence.converter.PageConverter;\n" //
									+ "import base.pack.age.name.persistence.converter.PageParametersToPageableConverter;\n" //
									+ "import base.pack.age.name.persistence.converter.TableWithUuidDBOConverter;\n" //
									+ "import base.pack.age.name.persistence.entity.TableWithUuidDBO;\n" //
									+ "import base.pack.age.name.persistence.repository.TableWithUuidDBORepository;\n" //
									+ "import java.util.UUID;\n" //
									+ "\n" //
									+ "import lombok.Generated;\n" //
									+ "\n" //
									+ "/**\n" //
									+ " * A generated JPA persistence adapter for table_with_uuids.\n" //
									+ " *\n" //
									+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
									+ " */\n" //
									+ "@Generated\n" //
									+ "public abstract class TableWithUuidGeneratedJPAPersistenceAdapter implements TableWithUuidPersistencePort {\n" //
									+ "\n" //
									+ "	@Inject\n" //
									+ "	protected TableWithUuidDBOConverter converter;\n" //
									+ "	@Inject\n" //
									+ "	protected TableWithUuidDBORepository repository;\n" //
									+ "\n" //
									+ "	@Inject\n" //
									+ "	protected PageParametersToPageableConverter pageParametersToPageableConverter;\n" //
									+ "\n" //
									+ "	protected PageConverter<TableWithUuid, TableWithUuidDBO> pageConverter;\n" //
									+ "\n" //
									+ "	@PostConstruct\n" //
									+ "	public void postConstruct() {\n" //
									+ "		pageConverter = new PageConverter<>(converter);\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public TableWithUuid create(TableWithUuid model) {\n" //
									+ "		model.setId(null);\n" //
									+ "		return converter.toModel(repository.save(converter.toDBO(model)));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public List<TableWithUuid> findAll() {\n" //
									+ "		return converter.toModel(repository.findAll());\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public Page<TableWithUuid> findAll(PageParameters pageParameters) {\n" //
									+ "		return pageConverter.convert(repository.findAll(pageParametersToPageableConverter.convert(pageParameters)));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public Optional<TableWithUuid> findById(Long id) {\n" //
									+ "		return repository.findById(id).map(dbo -> converter.toModel(dbo));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public TableWithUuid update(TableWithUuid model) {\n" //
									+ "		ensureNoViolationsFound(model);\n" //
									+ "		return converter.toModel(repository.save(converter.toDBO(model)));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	private void ensureNoViolationsFound(TableWithUuid model) {\n" //
									+ "		List<ValidationFailure> failures = new ArrayList<>();\n" //
									+ "		if (!failures.isEmpty()) {\n" //
									+ "			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" //
									+ "		}\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public void delete(TableWithUuid model) {\n" //
									+ "		repository.delete(converter.toDBO(model));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "	@Override\n" //
									+ "	public List<TableWithUuid> findAllByGlobalId(UUID globalId) {\n" //
									+ "		return converter.toModel(repository.findAllByGlobalId(globalId != null ? globalId.toString() : null));\n" //
									+ "	}\n" //
									+ "\n" //
									+ "}";
					DataModel dataModel = readDataModel("Model.xml");
					dataModel
							.getTableByName("TABLE_WITH_UUID")
							.getColumnByName("GLOBAL_ID")
							.setParameters(AbstractModelCodeGenerator.GLOBAL_ID + ":" + GlobalIdType.UUID + "|FIND_BY");
					dataModel.getTableByName("TABLE_WITH_UUID").getColumnByName("GLOBAL_ID").setUnique(false);
					// Run
					String returned =
							unitUnderTest
									.generate(
											BASE_PACKAGE_NAME,
											dataModel,
											dataModel.getTableByName("TABLE_WITH_UUID"));
					// Check
					assertEquals(expected, returned);
				}

			}

		}

	}

	@Nested
	class TestsOfMethod_isToIgnoreFor_DataModel_TableModel {

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

		@Test
		void passAMemberModel_returnsTrue() {
			// Prepare
			ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
			DataModel dataModel = reader.read("src/test/resources/examples/dm/Example-BookStore.xml");
			TableModel table = dataModel.getTableByName("CHAPTER");
			// Run & Check
			assertTrue(unitUnderTest.isToIgnoreFor(dataModel, table));
		}

	}

	@Nested
	class NotBlankConstraints {

		private String getExpected(String packageName) {
			String s = "package " + BASE_PACKAGE_NAME + "." + packageName + ";\n" + //
					"\n" + //
					"import java.util.ArrayList;\n" + //
					"import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import javax.annotation.PostConstruct;\n" + //
					"import javax.inject.Inject;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.Page;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException.Reason;\n" + //
					"import base.pack.age.name.core.service.exception.PersistenceFailureException.ValidationFailure;\n"
					+ //
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
					"		ensureNoViolationsFound(model);\n" + //
					"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void ensureNoViolationsFound(ATable model) {\n" //
					+ "		List<ValidationFailure> failures = new ArrayList<>();\n" //
					+ "		if ((model.getDescription() != null) && model.getDescription().isBlank()) {\n" //
					+ "			failures.add(new ValidationFailure(Reason.NOT_BLANK, \"ATable\", \"description\"));\n" //
					+ "		}\n" //
					+ "		if (!failures.isEmpty()) {\n" //
					+ "			throw new PersistenceFailureException(\"\" + model.getId(), failures);\n" //
					+ "		}\n" //
					+ "	}\n" //
					+ "\n" + //
					"	@Override\n" + //
					"	public void delete(ATable model) {\n" + //
					"		repository.delete(converter.toDBO(model));\n" + //
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
			table.getColumnByName("Description").addOption(new Option("NOT_BLANK"));
			ColumnModel column = new Tabellenspalte("order", table.getColumnByName("ID").getDomain());
			column.setNotNull(true);
			table.addColumn(column);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

	}

}