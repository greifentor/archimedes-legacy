/*
 * ComplexIndicesAdministrationFrameUtil.java
 *
 * 15.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.indices;

import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;

import java.util.List;
import java.util.Vector;

import org.easymock.EasyMock;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JComboBoxOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JListOperator;
import org.netbeans.jemmy.operators.JTableOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

import archimedes.legacy.Utils;
import archimedes.model.ChangeObserver;
import archimedes.model.DataModel;
import archimedes.model.IndexMetaData;

/**
 * Utilities zum Test der Klasse <CODE>ComplexIndicesAdministrationFrame</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
 */

public class ComplexIndicesAdministrationFrameUtil {

	private static JFrameOperator frame = null;
	private static long delayInMilliseconds = 0;

	/**
	 * Schlie&szlig;t den Frame zur Wartung komplexer Indices.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static void disposeComplexIndicesAdministrationFrame() {
		if (frame != null) {
			frame.dispose();
			frame = null;
		}
	}

	private static void delay() {
		if (delayInMilliseconds > 0) {
			try {
				Thread.sleep(delayInMilliseconds);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Liefert den gesetzten Verz&ouml;gerungswert, den das System nach jeder
	 * Anweisung warten soll.
	 * 
	 * @return
	 * 
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	public static void setDelayInMilliseconds(long delayInMilliseconds) {
		ComplexIndicesAdministrationFrameUtil.delayInMilliseconds = delayInMilliseconds;
	}

	/**
	 * Liefert den Inhalt des Eingabefeldes f&uuml;r den Indexnamen.
	 * 
	 * @return Der Inhalt des Eingabefeldes f&uuml;r den Indexnamen.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static String getIndexNameInputFieldContent() {
		return getIndexNameInputFieldOperator().getText();
	}

	private static JTextFieldOperator getIndexNameInputFieldOperator() {
		return new JTextFieldOperator(frame, 0);
	}

	/**
	 * Selektiert und liefert den Eintrag in der Listenansicht der Auswahlbox an
	 * der Position 'pos'.
	 * 
	 * @param pos
	 *            Die Position der Listenansicht, zu der das angezeigte Objekt
	 *            geliefert werden soll.
	 * @return Das Objekt, das in der Listenansicht an Position 'pos' angezeigt
	 *         wird.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static IndexMetaData getItemOfList(int pos) {
		JListOperator list = getSelectionList();
		if (list != null) {
			list.setSelectedIndex(pos);
			return (IndexMetaData) list.getSelectedValue();
		}
		return null;
	}

	private static JListOperator getSelectionList() {
		return new JListOperator(frame, 0);
	}

	/**
	 * Liefert den Namen der aktuell in der Auswahlbox selektierten Tabelle.
	 * 
	 * @return Der Name der aktuell in der Auswahlbox selektierten Tabelle.
	 * 
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	public static String getSelectedTableName() {
		JComboBoxOperator tables = getTablesComboBox();
		String name = null;
		if (tables.getSelectedItem() != null) {
			name = ((ClassMetaData) tables.getSelectedItem()).getName();
		}
		return name;
	}

	private static JComboBoxOperator getTablesComboBox() {
		return new JComboBoxOperator(frame, 0);
	}

	/**
	 * Pr&uumL;ft, ob die angegebene Tabellenspalte als Bestandteil des Index
	 * selektiert worden ist.
	 * 
	 * @param columnName
	 *            Der Name der Spalte, die gepr&uuml;ft werden soll.
	 * @return Boolean.TRUE, wenn die Spalte Teil des komplexen Index sein soll,
	 *         Boolean.FALSE, sofern dies nicht der Fall ist und
	 *         <CODE>null</CODE>, wenn f&uuml;r die Spalte in der Ansicht kein
	 *         Eintrag vorhanden ist.
	 * 
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	public static Boolean isColumnSelected(String columnName) {
		JTableOperator table = getColumnTableOperator();
		for (int i = 0, leni = table.getRowCount(); i < leni; i++) {
			if (((AttributeMetaData) (table.getValueAt(i, 0))).getName().equals(columnName)) {
				return (Boolean) table.getValueAt(i, 1);
			}
		}
		return null;
	}

	private static JTableOperator getColumnTableOperator() {
		return new JTableOperator(frame, 0);
	}

	/**
	 * &Ouml;ffnet den Frame zur Wartung komplexer Indices und reicht einen
	 * entsprechenden <CODE>JFrameOperator</CODE> zur&uuml;ck.
	 * 
	 * @param indices
	 *            Die Liste der komplexen Indices, die durch den Frame angezeigt
	 *            und bearbeitet werden soll.
	 * @return Ein <CODE>JFrameOperator</CODE> mit dem zu testenden Frame.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static JFrameOperator openComplexIndicesAdministrationFrame(DataModel dm) {
		new ComplexIndicesAdministrationFrame(":o", null, dm, createTableListMock(), createChangeObserverMock(-1));
		if (frame == null) {
			frame = new JFrameOperator(0);
		}
		delay();
		return frame;
	}

	private static List<ClassMetaData> createTableListMock() {
		List<ClassMetaData> l = new Vector<ClassMetaData>();
		l.add(createTableAMock());
		l.add(createTableBMock());
		return l;
	}

	private static ClassMetaData createTableAMock() {
		return Utils.createClassMetaDataMock("TableA", Utils.createAttributeMetaDataMock("ColumnA"), Utils
				.createAttributeMetaDataMock("ColumnB"));
	}

	private static ClassMetaData createTableBMock() {
		return Utils.createClassMetaDataMock("TableB", Utils.createAttributeMetaDataMock("ColumnC"), Utils
				.createAttributeMetaDataMock("ColumnD"));
	}

	private static ChangeObserver createChangeObserverMock(int expectedCalls) {
		ChangeObserver co = EasyMock.createMock(ChangeObserver.class);
		co.raiseAltered();
		if (expectedCalls > -1) {
			EasyMock.expectLastCall().times(expectedCalls);
		}
		EasyMock.replay(co);
		return co;
	}

	/**
	 * Dr&uuml;ckt den Schliessenbutton des Frames.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static void pressCloseButton() {
		JButtonOperator closeButton = new JButtonOperator(frame, 4);
		closeButton.doClick();
		delay();
	}

	/**
	 * Dr&uuml;ckt den Neuanlagebutton des Frames.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static void pressNewButton() {
		JButtonOperator newButton = new JButtonOperator(frame, 3);
		newButton.doClick();
		delay();
	}

	/**
	 * Dr&uuml;ckt den Neuanlagebutton des Frames.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static void pressRemoveButton() {
		JButtonOperator newButton = new JButtonOperator(frame, 1);
		newButton.doClick();
		delay();
	}

	/**
	 * Dr&uuml;ckt den Speichernbutton des Frames.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static void pressStoreButton() {
		JButtonOperator storeButton = new JButtonOperator(frame, 2);
		storeButton.doClick();
		delay();
	}

	/**
	 * W&auml;hlt die Tabellenspalte an der angegebenen Position der Liste aus.
	 * 
	 * @param pos
	 *            Die Position innerhalb der Tabellenspaltenliste, zu der die
	 *            Tabelle ausgew&auml:hlt werden soll.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static void selectColumn(int pos) {
		getColumnTableOperator().clickOnCell(pos, 1);
		delay();
	}

	/**
	 * W&auml;hlt den durch die angegebene Position gemeinten Index in der
	 * Auswahlliste an.
	 * 
	 * @param pos
	 *            Die Position Index, der ausgew&auml:hlt werden soll.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static void selectIndex(int pos) {
		getSelectionList().setSelectedIndex(pos);
		getSelectionList().clickOnItem(pos, 1);
		delay();
	}

	/**
	 * W&auml;hlt die Tabelle an der angegebenen Position der Liste aus.
	 * 
	 * @param pos
	 *            Die Position innerhalb der Tabellenliste, zu der die Tabelle
	 *            ausgew&auml:hlt werden soll.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static void selectTable(int pos) {
		getTablesComboBox().selectItem(pos);
		delay();
	}

	/**
	 * Setzt den angegebenen Namen in das Feld f&uuml;r den Namen des Index ein.
	 * 
	 * @param indexName
	 *            Der Name, der in das Namenseingabefeld zum Index eingegeben
	 *            werden soll.
	 * 
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	public static void setNameTo(String indexName) {
		JTextFieldOperator textField = getIndexNameInputFieldOperator();
		textField.clearText();
		textField.typeText(indexName);
		delay();
	}

}