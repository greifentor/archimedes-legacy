package archimedes.legacy.importer.jdbc;

import org.junit.jupiter.api.Test;

/**
 * Test of class "JDBCImportManager".
 *
 * @author ollie (30.09.2019)
 */
public class JDBCImportManagerTest {

	private JDBCImportManager unitUnderTest = new JDBCImportManager();

	@Test
	void test() {
		this.unitUnderTest.importDiagram(new JDBCImportConnectionData());
	}

}