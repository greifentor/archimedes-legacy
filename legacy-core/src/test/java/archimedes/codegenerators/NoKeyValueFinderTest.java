package archimedes.codegenerators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;

@ExtendWith(MockitoExtension.class)
class NoKeyValueFinderTest {

	@Mock
	private ColumnModel columnModel;

	@Mock
	private DomainModel domainModel;

	@Mock
	private TypesUtil typesUtil;

	@InjectMocks
	private NoKeyValueFinder unitUnderTest;

	@Nested
	class TestsOfMethod_find_ColumnModelArray {

		@Test
		void throwsAnException_passingANullValueAsColumnModelArray() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.find(null));
		}

		@Test
		void returnsASpecialIdentifier_passingAnEmptyPrimaryKeyArray() {
			assertEquals(NoKeyValueFinder.NO_KEY_FOUND, unitUnderTest.find(new ColumnModel[0]));
		}

		@ParameterizedTest
		@CsvSource({ "true,-1", "false,null" })
		void returnsAStringWithMinusOne_passingANotNullPrimaryKey_inPositionZeroOfTheArray(boolean numeric,
				String expected) {
			// Prepare
			int type = 4711;
			when(columnModel.isNotNull()).thenReturn(true);
			when(columnModel.getDomain()).thenReturn(domainModel);
			when(domainModel.getDataType()).thenReturn(type);
			when(typesUtil.isNumericType(type)).thenReturn(numeric);
			// Run & Check
			assertEquals(expected, unitUnderTest.find(new ColumnModel[] { columnModel }));
		}

		@Test
		void returnsAStringWithNull_passingANullPrimaryKey_inPositionZeroOfTheArray() {
			// Prepare
			when(columnModel.isNotNull()).thenReturn(false);
			// Run & Check
			assertEquals("null", unitUnderTest.find(new ColumnModel[] {columnModel}));
		}

	}

}
