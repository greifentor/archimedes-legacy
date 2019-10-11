package de.ollie.archimedes.alexandrian.service.exception;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for class "ColumnNotFoundException".
 * 
 * @author ollie (29.09.2019)
 */
public class ColumnNotFoundExceptionTest {

	private static final String COLUMN_NAME = "ColumnName";
	private static final String TABLE_NAME = "TableName";

	private ColumnNotFoundException unitUnderTest = new ColumnNotFoundException(TABLE_NAME, COLUMN_NAME);

	@Test
	public void constructorSetColumnNameCorrectly() {
		assertThat(this.unitUnderTest.getColumnName(), equalTo(COLUMN_NAME));
	}

	@Test
	public void constructorSetTableNameCorrectly() {
		assertThat(this.unitUnderTest.getTableName(), equalTo(TABLE_NAME));
	}

}