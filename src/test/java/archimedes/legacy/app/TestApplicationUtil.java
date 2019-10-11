/*
 * TestApplicationUtil.java
 *
 * 26.01.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import archimedes.model.SimpleIndexMetaData;

/**
 * Testf&auml;lle f&uuml;r die ApplicationUtil-Klasse.
 * 
 * @author ollie
 * 
 * @changed OLI 26.01.2009 - Hinzugef&uuml;gt.
 * @changed OLI 14.12.2011 - Anpassung an JUnit 4 und Erweiterung um den Test zum Lesen der Indexmetadaten.
 */

public class TestApplicationUtil {

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetMetaDataPassingANullPointerAsConnection() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ApplicationUtil.GetIndexMetaData(null, null);
		});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetMetaDataExtractsTheIndexInformations() throws Exception {
		Connection c = this.createConnectionMockForGettingIndexInformations();
		List<SimpleIndexMetaData> limd = ApplicationUtil.GetIndexMetaData(c, null);
		assertNotNull(limd);
		assertEquals(2, limd.size());
		assertEquals("ITableA", limd.get(0).getName());
		assertEquals(1, limd.get(0).getColumns().length);
		assertEquals("Column0", limd.get(0).getColumns()[0]);
		assertEquals("TableA", limd.get(0).getTable());
		assertEquals("ITableB", limd.get(1).getName());
		assertEquals(2, limd.get(1).getColumns().length);
		assertEquals("Column1", limd.get(1).getColumns()[0]);
		assertEquals("Column2", limd.get(1).getColumns()[1]);
		assertEquals("TableB", limd.get(1).getTable());
	}

	private Connection createConnectionMockForGettingIndexInformations() throws Exception {
		Connection c = EasyMock.createMock(Connection.class);
		EasyMock.expect(c.getMetaData()).andReturn(this.createDatabaseMetaData()).times(3);
		EasyMock.replay(c);
		return c;
	}

	private DatabaseMetaData createDatabaseMetaData() throws Exception {
		DatabaseMetaData dmd = EasyMock.createMock(DatabaseMetaData.class);
		EasyMock.expect(dmd.getTables((String) EasyMock.anyObject(), (String) EasyMock.anyObject(),
				(String) EasyMock.anyObject(), (String[]) EasyMock.anyObject()))
				.andReturn(this.createResultSetForTableMetaData());
		EasyMock.expect(dmd.getIndexInfo(null, null, "TableA", false, false))
				.andReturn(this.createResultSetForIndexMetaDataTableA());
		EasyMock.expect(dmd.getIndexInfo(null, null, "TableB", false, false))
				.andReturn(this.createResultSetForIndexMetaDataTableB()).times(2);
		EasyMock.replay(dmd);
		return dmd;
	}

	private ResultSet createResultSetForTableMetaData() throws Exception {
		ResultSet rs = EasyMock.createMock(ResultSet.class);
		EasyMock.expect(rs.next()).andReturn(true).times(2);
		EasyMock.expect(rs.next()).andReturn(false);
		EasyMock.expect(rs.getString("TABLE_NAME")).andReturn("TableB");
		EasyMock.expect(rs.getString("TABLE_NAME")).andReturn("TableA");
		EasyMock.replay(rs);
		return rs;
	}

	private ResultSet createResultSetForIndexMetaDataTableA() throws Exception {
		ResultSet rs = EasyMock.createMock(ResultSet.class);
		EasyMock.expect(rs.next()).andReturn(true).times(1);
		EasyMock.expect(rs.next()).andReturn(false);
		EasyMock.expect(rs.getString("INDEX_NAME")).andReturn("ITableA").times(1);
		EasyMock.expect(rs.getString("COLUMN_NAME")).andReturn("Column0").times(2);
		EasyMock.expect(rs.getBoolean("NON_UNIQUE")).andReturn(true).times(3);
		EasyMock.replay(rs);
		return rs;
	}

	private ResultSet createResultSetForIndexMetaDataTableB() throws Exception {
		ResultSet rs = EasyMock.createMock(ResultSet.class);
		EasyMock.expect(rs.next()).andReturn(true).times(2);
		EasyMock.expect(rs.next()).andReturn(false);
		EasyMock.expect(rs.getBoolean("NON_UNIQUE")).andReturn(true).times(3);
		EasyMock.expect(rs.getString("INDEX_NAME")).andReturn("ITableB");
		EasyMock.expect(rs.getString("COLUMN_NAME")).andReturn("Column1");
		EasyMock.expect(rs.getString("INDEX_NAME")).andReturn("ITableB");
		EasyMock.expect(rs.getString("COLUMN_NAME")).andReturn("Column2");
		EasyMock.replay(rs);
		return rs;
	}

	/**
	 * Test der Methode ReadProperties(Properties, String) - Einfaches Einlesen.
	 */
	@Test
	public void testReadPropertiesSimpleRead() throws Exception {
		Properties p = new Properties();
		String fn = "src/test/resources/conf/test.properties";
		ApplicationUtil.ReadProperties(p, fn);
		assertEquals(p.getProperty("test.ReadProperties"), "Test successful");
	}

	/**
	 * Test der Methode ReadProperties(Properties, String) - &Uuml;berschreibendes Einlesen.
	 */
	@Test
	public void testReadPropertiesOverride() throws Exception {
		Properties p = new Properties();
		String fn = "src/test/resources/conf/test.properties";
		p.setProperty("test.ReadProperties", "undefined");
		p.setProperty("test.ReadProperties.another", "Another");
		ApplicationUtil.ReadProperties(p, fn);
		assertEquals(p.getProperty("test.ReadProperties"), "Test successful");
		assertEquals(p.getProperty("test.ReadProperties.another"), "Another");
	}

	/** Test der Methode ReadProperties(String) - Einfaches Einlesen. */
	@Test
	public void testReadPropertiesSimpleReadSystem() {
		String fn = "src/test/resources/conf/test.properties";
		ApplicationUtil.ReadProperties(fn);
		assertEquals(System.getProperty("test.ReadProperties"), "Test successful");
	}

	/**
	 * Test der Methode ReadProperties(String) - &Uuml;berschreibendes Einlesen.
	 */
	@Test
	public void testReadPropertiesOverrideSystem() {
		String fn = "src/test/resources/conf/test.properties";
		System.setProperty("test.ReadProperties", "undefined");
		System.setProperty("test.ReadProperties.another", "Another");
		ApplicationUtil.ReadProperties(fn);
		assertEquals(System.getProperty("test.ReadProperties"), "Test successful");
		assertEquals(System.getProperty("test.ReadProperties.another"), "Another");
	}

}