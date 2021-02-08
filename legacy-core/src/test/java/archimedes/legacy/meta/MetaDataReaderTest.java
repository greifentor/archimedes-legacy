/*
 * MetaDataReaderTest.java
 *
 * 04.12.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.meta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Diagramm;
import archimedes.meta.MetaDataColumn;
import archimedes.meta.MetaDataForeignKeyConstraint;
import archimedes.meta.MetaDataIndex;
import archimedes.meta.MetaDataModel;
import archimedes.meta.MetaDataModelComparator;
import archimedes.meta.MetaDataReader;
import archimedes.meta.MetaDataSequence;
import archimedes.meta.MetaDataTable;
import archimedes.meta.MetaDataUniqueConstraint;
import archimedes.meta.chops.AbstractChangeOperation;
import archimedes.meta.chops.ScriptSectionType;
import archimedes.util.NameGenerator;
import corent.db.ConnectionManager;
import corent.db.DBExecMode;
import corent.db.JDBCDataSourceRecord;
import corent.files.StructuredTextFile;
import corent.util.SysUtil;
import corentx.util.Direction;
import corentx.util.Str;

/**
 * Tests of the class <CODE>MetaDataReader</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 04.12.2015 - Added.
 */

public class MetaDataReaderTest {

	private DiagrammModel diagram = null;
	private NameGenerator nameGenerator = new NameGenerator(null);
	private MetaDataReader unitUnderTest = null;

	/**
	 * @changed OLI 04.12.2015 - Added.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.unitUnderTest = new MetaDataReader();
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERCOLUMN", "99");
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERROW", "99");
		System.setProperty("corent.base.StrUtil.suppress.html.note", "true");
		System.setProperty("corent.files.StructuredTextFile.suppress.FileNotFound", "true");
		FileInputStream in = new FileInputStream(new File("unittests/dm/TST2.ads"));
		StructuredTextFile stf = new StructuredTextFile(in);
		try {
			stf.load();
			this.diagram = new Diagramm();
			this.diagram = (Diagramm) this.diagram.createDiagramm(stf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.setProperty("archimedes.gui.ArchimedesCommandProcessor.suppress.gui", "true");
	}

	/**
	 * @changed OLI 06.12.2015 - Added.
	 */
	@AfterEach
	public void tearDown() throws SQLException {
		if ("KLEISTHENES".equalsIgnoreCase(SysUtil.GetHostname()) && (this.getDBMode() == DBExecMode.HSQL)) {
			Connection c = ConnectionManager.GetConnection(this.getConnectionData());
			c.createStatement().execute("SHUTDOWN");
			c.close();
		}
	}

	/**
	 * @changed OLI 04.12.2015 - Added.
	 */
	@Disabled("A test with database access. Really ?!?")
	@Test
	public void test() throws SQLException {
		MetaDataModel mdb = this.unitUnderTest.read(this.getConnectionData(),
				(this.getDBMode() == DBExecMode.MYSQL ? null : "TST"), DBExecMode.POSTGRESQL);
		assertNotNull(mdb);
		this.printMetaData(mdb);
		// System.out.println("-----------------------------------------------------------------");
		MetaDataModel mdm = this.unitUnderTest.read(this.diagram, this.getDBMode());
		assertNotNull(mdm);
		// this.printMetaData(mdm);
		// System.out.println("-----------------------------------------------------------------");
		AbstractChangeOperation[] r = new MetaDataModelComparator(this.nameGenerator).compare(mdb, mdm);
		assertNotNull(r);
		ScriptSectionType section = null;
		for (AbstractChangeOperation o : r) {
			if (section != o.getSection()) {
				section = o.getSection();
				System.out.println(Str.pumpUp(section + " ", "-", 65, Direction.RIGHT));
			}
			System.out.println("    " + o);
		}
		assertEquals(0, r.length);
	}

	private JDBCDataSourceRecord getConnectionData() {
		JDBCDataSourceRecord dsr = new JDBCDataSourceRecord("org.postgresql.Driver",
				"jdbc:postgresql://mykene/TEST_OLI_ISIS_2", "op1", "op1");
		if ("KLEISTHENES".equalsIgnoreCase(SysUtil.GetHostname())) {
			dsr = new JDBCDataSourceRecord("org.hsqldb.jdbcDriver", "jdbc:hsqldb:unittests/db/tst", "sa", "");
		}
		return dsr;
	}

	private DBExecMode getDBMode() {
		DBExecMode m = DBExecMode.POSTGRESQL;
		if ("KLEISTHENES".equalsIgnoreCase(SysUtil.GetHostname())) {
			m = DBExecMode.MYSQL;
		}
		return m;
	}

	private void printMetaData(MetaDataModel m) {
		for (MetaDataTable t : m.getTables()) {
			System.out.println(t.getName());
			for (MetaDataColumn c : t.getColumns()) {
				System.out.println("    " + c.getName() + " (" + c.getSQLType(this.getDBMode())
						+ (c.isPrimaryKey() ? ", PRIMARY KEY" : "") + (c.isNotNull() ? ", NOT NULL" : "")
						+ (c.isUnique() ? ", UNIQUE" : "") + ")");
				for (MetaDataForeignKeyConstraint fkc : c.getForeignKeyConstraints()) {
					System.out.println(
							"        FK \"" + fkc.getName() + "\" -> " + fkc.getReferencedColumn().getFullName());
				}
			}
			for (MetaDataIndex i : t.getIndices()) {
				String cns = "";
				for (String cn : i.getColumnNames()) {
					if (cns.length() > 0) {
						cns += ", ";
					}
					cns += cn;
				}
				System.out.println(
						"    I \"" + i.getName() + "\" ON \"" + i.getTable().getName() + "\" (\"" + cns + "\")");
			}
			for (MetaDataUniqueConstraint u : t.getUniqueConstraints()) {
				String cns = "";
				for (MetaDataColumn c : u.getColumns()) {
					if (cns.length() > 0) {
						cns += ", ";
					}
					cns += c.getName();
				}
				System.out.println(
						"    U \"" + u.getName() + "\" ON \"" + u.getTable().getName() + "\" (\"" + cns + "\")");
			}
		}
		if (m.getSequences().length > 0) {
			System.out.println();
			for (MetaDataSequence s : m.getSequences()) {
				System.out.println(
						"SEQUENCE \"" + s.getName() + "\" START " + s.getStartValue() + " INC " + s.getIncrementBy());
			}
		}
	}

}