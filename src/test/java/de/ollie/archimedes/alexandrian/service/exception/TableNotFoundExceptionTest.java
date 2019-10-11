package de.ollie.archimedes.alexandrian.service.exception;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for class "TableNotFoundException".
 *
 * @author ollie (29.09.2019)
 */
public class TableNotFoundExceptionTest {

	private static final String TABLE_NAME = "TableName";

	private TableNotFoundException unitUnderTest = new TableNotFoundException(TABLE_NAME);

	@Test
	public void constructorSetTableNameCorrectly() {
		assertThat(this.unitUnderTest.getTableName(), equalTo(TABLE_NAME));
	}

}