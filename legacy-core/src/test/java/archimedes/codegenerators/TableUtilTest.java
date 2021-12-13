package archimedes.codegenerators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import archimedes.model.ColumnModel;
import archimedes.model.TableModel;

public class TableUtilTest {

	@Nested
	class TestsOfMethod_hasCompositeKey_TableModel {

		@Test
		void passANullValue_throwsAnException() {
			assertThrows(IllegalArgumentException.class, () -> TableUtil.hasCompositeKey(null));
		}

		@Test
		void passATableWithMoreThanOnePrimaryKeyColumn_returnTrue() {
			// Prepare
			TableModel table = mock(TableModel.class);
			ColumnModel column0 = mock(ColumnModel.class);
			ColumnModel column1 = mock(ColumnModel.class);
			when(table.getPrimaryKeyColumns()).thenReturn(new ColumnModel[] { column0, column1 });
			// Run & Check
			assertTrue(TableUtil.hasCompositeKey(table));
		}

		@Test
		void passATableWithOneKeyColumn_returnFalse() {
			// Prepare
			TableModel table = mock(TableModel.class);
			ColumnModel column = mock(ColumnModel.class);
			when(table.getPrimaryKeyColumns()).thenReturn(new ColumnModel[] { column });
			// Run & Check
			assertFalse(TableUtil.hasCompositeKey(table));
		}

		@Test
		void passATableWithZeroKeyColumns_returnFalse() {
			// Prepare
			TableModel table = mock(TableModel.class);
			when(table.getPrimaryKeyColumns()).thenReturn(new ColumnModel[] {});
			// Run & Check
			assertFalse(TableUtil.hasCompositeKey(table));
		}

	}

}
