package archimedes.codegenerators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;

@ExtendWith(MockitoExtension.class)
class GlobalIdOptionCheckerTest {

	private static final String GLOBAL_ID_OPTION_NAME = AbstractModelCodeGenerator.GLOBAL_ID;
	private static final GlobalIdType GLOBAL_ID_TYPE = GlobalIdType.UUID;
	private static final String NONE = "NONE";
	private static final String UUID = "UUID";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private OptionModel option;
	@Mock
	private OptionModel optionModel;
	@Mock
	private OptionModel optionTable;
	@Mock
	private TableModel table;

	@InjectMocks
	private GlobalIdOptionChecker unitUnderTest;

	@BeforeEach
	void setUp() {
		lenient().when(column.getTable()).thenReturn(table);
		lenient().when(table.getDataModel()).thenReturn(model);
		lenient()
				.when(column.getOptionByName(AbstractModelCodeGenerator.GLOBAL_ID))
				.thenReturn(new Option(AbstractModelCodeGenerator.GLOBAL_ID));
	}

	@Nested
	class TestsOfMethod_getGlobalIdType_ColumnModel {

		@Test
		void returnsTypeNONE_passingAColumnWithCompletelyNoConfigurationForGlobalId() {
			assertEquals(GlobalIdType.NONE, unitUnderTest.getGlobalIdType(column));
		}

		@Test
		void returnsTypeNONE_passingAColumnWithGlobalId_butNoGlobalIdTypeConfiguration() {
			// Prepare
			when(table.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.empty());
			// Run & Check
			assertEquals(GlobalIdType.NONE, unitUnderTest.getGlobalIdType(column));
		}

		@Test
		void returnsTypeNONE_passingAGlobalIdColumnWithNoConfiguration() {
			// Prepare
			when(table.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.empty());
			// Run & Check
			assertEquals(GlobalIdType.NONE, unitUnderTest.getGlobalIdType(column));
		}

		@ParameterizedTest
		@EnumSource(GlobalIdType.class)
		void returnsConfiguredType_passingAGlobalIdColumnWithConfiguredGlobalIdType(GlobalIdType globalIdType) {
			// Prepare
			when(option.getParameter()).thenReturn(globalIdType.name());
			when(column.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.of(option));
			// Run & Check
			assertEquals(globalIdType, unitUnderTest.getGlobalIdType(column));
		}

		@Test
		void returnsConfiguredTypeOfTheColumn_passingAGlobalIdColumnWithConfiguredGlobalIdTypeAndATableAlso_butAnotherOne() {
			// Prepare
			when(option.getParameter()).thenReturn(GlobalIdType.NONE.name());
			when(column.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.of(option));
			// Run & Check
			assertEquals(GlobalIdType.NONE, unitUnderTest.getGlobalIdType(column));
			verifyZeroInteractions(optionTable);
			verifyZeroInteractions(optionModel);
		}

		@ParameterizedTest
		@EnumSource(GlobalIdType.class)
		void returnsConfiguredType_passingAGlobalIdColumnWithNoConfiguredGlobalIdType_butTableHas(
				GlobalIdType globalIdType) {
			// Prepare
			when(column.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.empty());
			when(optionTable.getParameter()).thenReturn(globalIdType.name());
			when(table.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.of(optionTable));
			// Run & Check
			assertEquals(globalIdType, unitUnderTest.getGlobalIdType(column));
		}

		@ParameterizedTest
		@EnumSource(GlobalIdType.class)
		void returnsConfiguredType_passingAGlobalIdColumnWithNoConfiguredGlobalIdType_butModelHas(
				GlobalIdType globalIdType) {
			// Prepare
			when(column.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.empty());
			when(optionModel.getParameter()).thenReturn(globalIdType.name());
			when(model.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.of(optionModel));
			// Run & Check
			assertEquals(globalIdType, unitUnderTest.getGlobalIdType(column));
		}

		@Test
		void returnsConfiguredTypeOfTheColumn_passingANoGlobalIdTypeColumn_butTableHasGlobalIdTypeSet() {
			// Prepare
			when(column.getOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(null);
			// Run & Check
			assertEquals(GlobalIdType.NONE, unitUnderTest.getGlobalIdType(column));
		}

	}

	@Nested
	class TestsOfMethod_hasGlobalIdTypeConfiguration_GlobalIdType_DataModel_TableModel {

		@Test
		void returnsFalse_ifNoMatchingOptionIsConfigured() {
			// Prepare
			when(table.getColumns()).thenReturn(new ColumnModel[0]);
			// Run & Check
			assertFalse(unitUnderTest.hasGlobalIdTypeConfiguration(GLOBAL_ID_TYPE, table));
		}

		@Test
		void returnsFalse_ifTableHasNoGlobalId() {
			// Prepare
			when(column.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.empty());
			when(table.getColumns()).thenReturn(new ColumnModel[] { column });
			// Run & Check
			assertFalse(unitUnderTest.hasGlobalIdTypeConfiguration(GLOBAL_ID_TYPE, table));
		}

		@Test
		void returnsFalse_ifTableHasAColumnWithAGlobalId_butDoesNotMatchThePassedGlobalIdType() {
			// Prepare
			when(option.getParameter()).thenReturn(NONE);
			when(column.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.of(option));
			when(table.getColumns()).thenReturn(new ColumnModel[] { column });
			// Run & Check
			assertFalse(unitUnderTest.hasGlobalIdTypeConfiguration(GLOBAL_ID_TYPE, table));
		}

		@Test
		void returnsFalse_ifTableHasAColumnWithAGlobalId_butWithoutConfiguration() {
			// Prepare
			when(column.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.of(option));
			when(table.getColumns()).thenReturn(new ColumnModel[] { column });
			// Run & Check
			assertFalse(unitUnderTest.hasGlobalIdTypeConfiguration(GLOBAL_ID_TYPE, table));
		}

		@Test
		void returnsTrue_ifTableHasAColumnWithAGlobalId_andOptionMatchesThePassedGlobalIdType() {
			// Prepare
			when(option.getParameter()).thenReturn(UUID);
			when(column.findOptionByName(GLOBAL_ID_OPTION_NAME)).thenReturn(Optional.of(option));
			when(table.getColumns()).thenReturn(new ColumnModel[] { column });
			// Run & Check
			assertTrue(unitUnderTest.hasGlobalIdTypeConfiguration(GLOBAL_ID_TYPE, table));
		}

	}

}
