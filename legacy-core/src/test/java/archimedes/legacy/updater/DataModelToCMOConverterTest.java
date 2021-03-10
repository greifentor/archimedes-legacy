package archimedes.legacy.updater;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;
import de.ollie.dbcomp.model.ColumnCMO;
import de.ollie.dbcomp.model.DataModelCMO;
import de.ollie.dbcomp.model.ForeignKeyCMO;
import de.ollie.dbcomp.model.ForeignKeyMemberCMO;
import de.ollie.dbcomp.model.SchemaCMO;
import de.ollie.dbcomp.model.TableCMO;
import de.ollie.dbcomp.model.TypeCMO;

@ExtendWith(MockitoExtension.class)
public class DataModelToCMOConverterTest {

	@InjectMocks
	private DataModelToCMOConverter unitUnderTest;

	@Test
	void passADataModel_ReturnsACorrectDataModelCMO() {
		// Prepare
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel = reader.read("src/test/resources/dm/DataModel2DataModelCMO-Test.xml");
		ColumnCMO idColumn = ColumnCMO.of("Id", TypeCMO.of(Types.BIGINT, 0, 0), false, false);
		ColumnCMO idColumnB = ColumnCMO.of("Id", TypeCMO.of(Types.BIGINT, 0, 0), false, false);
		ColumnCMO idColumnAnotherTable = ColumnCMO.of("Id", TypeCMO.of(Types.BIGINT, 0, 0), false, false);
		ColumnCMO idColumnIgnoredTable = ColumnCMO.of("Id", TypeCMO.of(Types.BIGINT, 0, 0), false, false);
		ColumnCMO refAnotherTableId = ColumnCMO.of("AnotherTableId", TypeCMO.of(Types.BIGINT, 0, 0), false, true);
		ColumnCMO refTableB = ColumnCMO.of("TableB", TypeCMO.of(Types.BIGINT, 0, 0), false, true);
		TableCMO aTable = TableCMO
				.of(
						"ATable",
						idColumn,
						refAnotherTableId,
						ColumnCMO.of("Name", TypeCMO.of(Types.VARCHAR, 100, 0), false, false),
						refTableB,
						ColumnCMO.of("ADate", TypeCMO.of(Types.DATE, 0, 0), false, true))
				.addPrimaryKeys(idColumn.getName());
		TableCMO tableB = TableCMO.of("TableB", idColumnB).addPrimaryKeys(idColumnB.getName());
		TableCMO anotherTable = TableCMO
				.of(
						"AnotherTable",
						idColumnAnotherTable,
						ColumnCMO.of("Name", TypeCMO.of(Types.VARCHAR, 100, 0), false, true))
				.addPrimaryKeys(idColumnAnotherTable.getName());
		aTable
				.addForeignKeys(
						ForeignKeyCMO
								.of(
										"FK_TO_" + anotherTable.getName() + "_" + idColumnAnotherTable.getName(),
										ForeignKeyMemberCMO
												.of(aTable, refAnotherTableId, anotherTable, idColumnAnotherTable)),
						ForeignKeyCMO
								.of(
										"FK_TO_" + tableB.getName() + "_" + idColumnB.getName(),
										ForeignKeyMemberCMO.of(aTable, refTableB, tableB, idColumnB)));
		TableCMO ignoredTable = TableCMO
				.of(
						"IgnoredTable",
						idColumnIgnoredTable,
						ColumnCMO.of("Name", TypeCMO.of(Types.VARCHAR, 100, 0), false, true))
				.addPrimaryKeys(idColumnIgnoredTable.getName());
		DataModelCMO expected = DataModelCMO.of(SchemaCMO.of("", anotherTable, aTable, ignoredTable, tableB));
		// Run
		DataModelCMO returned = unitUnderTest.convert(dataModel);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void passADataModelWithTableIgnore_ReturnsACorrectDataModelCMO() {
		// Prepare
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel = reader.read("src/test/resources/dm/DataModel2DataModelCMO-Test.xml");
		ColumnCMO idColumn = ColumnCMO.of("Id", TypeCMO.of(Types.BIGINT, 0, 0), false, false);
		ColumnCMO idColumnB = ColumnCMO.of("Id", TypeCMO.of(Types.BIGINT, 0, 0), false, false);
		ColumnCMO idColumnAnotherTable = ColumnCMO.of("Id", TypeCMO.of(Types.BIGINT, 0, 0), false, false);
		ColumnCMO refAnotherTableId = ColumnCMO.of("AnotherTableId", TypeCMO.of(Types.BIGINT, 0, 0), false, true);
		ColumnCMO refTableB = ColumnCMO.of("TableB", TypeCMO.of(Types.BIGINT, 0, 0), false, true);
		TableCMO aTable = TableCMO
				.of(
						"ATable",
						idColumn,
						ColumnCMO.of("Name", TypeCMO.of(Types.VARCHAR, 100, 0), false, false),
						refAnotherTableId,
						refTableB,
						ColumnCMO.of("ADate", TypeCMO.of(Types.DATE, 0, 0), false, true))
				.addPrimaryKeys(idColumn.getName());
		TableCMO tableB = TableCMO.of("TableB", idColumnB).addPrimaryKeys(idColumnB.getName());
		TableCMO anotherTable = TableCMO
				.of(
						"AnotherTable",
						idColumnAnotherTable,
						ColumnCMO.of("Name", TypeCMO.of(Types.VARCHAR, 100, 0), false, true))
				.addPrimaryKeys(idColumnAnotherTable.getName());
		aTable
				.addForeignKeys(
						ForeignKeyCMO
								.of(
										"FK_TO_" + anotherTable.getName() + "_" + idColumnAnotherTable.getName(),
										ForeignKeyMemberCMO
												.of(aTable, refAnotherTableId, anotherTable, idColumnAnotherTable)),
						ForeignKeyCMO
								.of(
										"FK_TO_" + tableB.getName() + "_" + idColumnB.getName(),
										ForeignKeyMemberCMO.of(aTable, refTableB, tableB, idColumnB)));
		DataModelCMO expected = DataModelCMO.of(SchemaCMO.of("", anotherTable, aTable, tableB));
		// Run
		DataModelCMO returned = unitUnderTest.convert(dataModel, table -> table.getOptionByName("NO_DB") != null);
		// Check
		assertEquals(expected, returned);
	}

}