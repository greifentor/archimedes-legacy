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
		DataModelCMO expected =
				DataModelCMO
						.of(
								SchemaCMO
										.of(
												"",
												TableCMO
														.of(
																"ATable",
																idColumn,
																ColumnCMO
																		.of(
																				"Name",
																				TypeCMO.of(Types.VARCHAR, 100, 0),
																				false,
																				false))));
		// Run
		DataModelCMO returned = unitUnderTest.convert(dataModel);
		// Check
		assertEquals(expected, returned);
	}

}