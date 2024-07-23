package archimedes.codegenerators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.ColumnModel;
import archimedes.model.OptionModel;

@ExtendWith(MockitoExtension.class)
class FindByUtilsTest {

	@Mock
	private ColumnModel column0;
	@Mock
	private ColumnModel column1;
	@Mock
	private OptionModel option;

	private void trainColumnModel(ColumnModel mock, boolean isBlank) {
		lenient()
				.when(mock.findOptionByName(AbstractClassCodeFactory.NOT_BLANK))
				.thenReturn(isBlank ? Optional.of(option) : Optional.empty());
	}

	@Nested
	class TestsOfMethod_hasNotBlanks_ColumnModelArr {

		@Test
		void throwsAnException_passingANullValue() {
			assertThrows(NullPointerException.class, () -> FindByUtils.hasNotBlanks(null));
		}

		@Test
		void returnsFalse_passingAnEmptyArray() {
			assertFalse(FindByUtils.hasNotBlanks(new ColumnModel[0]));
		}

		@ParameterizedTest
		@CsvSource({ "false,false,false", "false,true,true", "true,false,true", "true,true,true" })
		void returnsTheCorrectValue_passingDifferentArrays(boolean isBlank0, boolean isBlank1, boolean expected) {
			trainColumnModel(column0, isBlank0);
			trainColumnModel(column1, isBlank1);
			assertEquals(expected, FindByUtils.hasNotBlanks(new ColumnModel[] { column0, column1 }));
		}

	}

}
