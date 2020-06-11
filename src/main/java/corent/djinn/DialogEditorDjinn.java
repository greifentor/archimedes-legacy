/*
 * DialogEditorDjinn.java
 *
 * 11.04.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.djinn;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import corent.base.StrUtil;
import corent.db.Persistent;
import corent.files.Inifile;
import corent.gui.COUtil;
import corent.gui.JDialogWithInifile;
import corent.gui.PropertyRessourceManager;
import corent.print.JasperReportable;

/**
 * Diese Implementierung des EditorDjinns spielt sich in einem Dialog ab. F&uuml;r Anwendungen, die nur zum Manipulieren
 * eines einzelnen Datensatzes erstellt werden sollen, ist diese Variante die erste Wahl. Alternativ kann er auch
 * genutzt werden, um InternalDialog-Applikationen zu umschiffen.
 * <P>
 * Der Dialog arbeitet eng mit dem PanelEditorDjinn zusammen, in dem die eigentliche Arbeit getan wird.
 *
 * @author O.Lieshoff
 *         <P>
 *
 * @changed OLI 29.10.2007 - Anpassungen an das DefaultEditorDjinnPanel mit Speichern-Button.
 *          <P>
 *          OLI 26.02.2008 - Umstellung auf den EditorDjinnMode-Betrieb.
 *          <P>
 *          OLI 31.03.2008 - Umstellung der Info-Dialog (Verwerfen, L&ouml;schen) auf Property-Ressourcen.
 *          <P>
 *          OLI 15.06.2008 - Erweiterung um den Aufruf der Methode <TT>doOpened()</TT> des eingebundenen
 *          DefaultEditorDjinnPanels beim &Ouml;ffnen des EditorDjinns.
 *          <P>
 *          OLI 05.01.2009 - Erweiterung um die Implementierung der Methode <TT>setEditable(Editable)</TT>.
 *          <P>
 *          OLI 12.01.2009 - Deaktivierung der Methode <TT>setEditable(Editable)</TT>. Um das korrekt
 *          durchzuprogrammieren fehlt im Momment die Zeit.
 *          <P>
 *          OLI 29.01.2009 - Implementierung der Erweiterung des Interfaces <TT>EditorDjinnController</TT> durch die
 *          Methode <TT>getEditorDjinnMode()</TT>.
 *          <P>
 *
 */

public class DialogEditorDjinn extends JDialogWithInifile implements EditorDjinnController {

	/*
	 * Diese Flagge wird vom Dialog gesetzt, wenn er &uuml;ber den Verwerfen-Button beendet wird.
	 */
	private boolean canceled = false;
	/* Diese Flagge trifft eine Aussage dar&uuml;ber, ob das angezeigt Objekt gelockt ist. */
	private boolean locked = false;
	/* Das EditorDjinn-Panel zur Anzeige im Dialog. */
	private DefaultEditorDjinnPanel panel = null;
	/* Der EditorDjinnMode, mit dem der Frame aufgerufen worden ist. */
	private EditorDjinnMode mode = null;

	/**
	 * Generiert einen EditorDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
	 *
	 * @param d     Der Dialog, der das Fenster aufruft.
	 * @param titel Der Titel des Dialogs.
	 * @param e     Das Editable, das in dem Dialog editiert werden soll.
	 */
	public DialogEditorDjinn(Dialog d, String titel, Editable e) {
		this(d, titel, e, true, false, null, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
	 *
	 * @param d     Der Dialog, der das Fenster aufruft.
	 * @param titel Der Titel des Dialogs.
	 * @param e     Das Editable, das in dem Dialog editiert werden soll.
	 * @param ini   Eine Inidatei zur Speicherung der Fensterkoordinaten.
	 */
	public DialogEditorDjinn(Dialog d, String titel, Editable e, Inifile ini) {
		this(d, titel, e, true, false, ini, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
	 *
	 * @param d                 Der Dialog, der das Fenster aufruft.
	 * @param titel             Der Titel des Dialogs.
	 * @param e                 Das Editable, das in dem Dialog editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der L&ouml;schenbutton aktiviert werden
	 *                          soll.
	 * @param historieAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der Historienbutton aktiviert werden soll.
	 */
	public DialogEditorDjinn(Dialog d, String titel, Editable e, boolean loeschenAktiviert, boolean historieAktiviert) {
		this(d, titel, e, loeschenAktiviert, historieAktiviert, null, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
	 *
	 * @param d                 Der Dialog, der das Fenster aufruft.
	 * @param titel             Der Titel des Dialogs.
	 * @param e                 Das Editable, das in dem Dialog editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der L&ouml;schenbutton aktiviert werden
	 *                          soll.
	 * @param historieAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der Historienbutton aktiviert werden soll.
	 * @param ini               Eine Inidatei zur Speicherung der Fensterkoordinaten.
	 */
	public DialogEditorDjinn(Dialog d, String titel, Editable e, boolean loeschenAktiviert, boolean historieAktiviert,
			Inifile ini) {
		this(d, titel, e, loeschenAktiviert, historieAktiviert, ini, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
	 *
	 * @param d                 Der Dialog, der das Fenster aufruft.
	 * @param titel             Der Titel des Dialogs.
	 * @param e                 Das Editable, das in dem Dialog editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der L&ouml;schenbutton aktiviert werden
	 *                          soll.
	 * @param historieAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der Historienbutton aktiviert werden soll.
	 * @param ini               Eine Inidatei zur Speicherung der Fensterkoordinaten.
	 * @param split             Wird diese Flagge gesetzt, erzeugt das EditorDjinnPanel im Falle eines TabbedEditables
	 *                          einen zweigeteilten Anzeigebereich. In der oberen H&auml;lfte wird das erste Panel des
	 *                          TabbedEditables angezeigt. Die untere H&auml;lfte beinhaltet die restlichen Panels. Ist
	 *                          die Flagge nicht gesetzt, werden alle Panels in Tabs eingef&uuml;gt.
	 * @param locked            Diese Flagge mu&szlig; gesetzt werden, wenn der Datensatz gesperrt ist.
	 * @param mode              Der Modus, in dem das Panel betrieben wird (Neuanlage, &Auml;nderung oder Duplikation).
	 */
	public DialogEditorDjinn(Dialog d, String titel, Editable e, boolean loeschenAktiviert, boolean historieAktiviert,
			Inifile ini, boolean split, boolean locked, EditorDjinnMode mode) {
		super(d, titel, true, ini);
		this.construct(titel, e, loeschenAktiviert, historieAktiviert, ini, split, locked, mode);
	}

	/**
	 * Generiert einen EditorDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
	 *
	 * @param f     Der Frame, der das Fenster aufruft.
	 * @param titel Der Titel des Dialogs.
	 * @param e     Das Editable, das in dem Dialog editiert werden soll.
	 */
	public DialogEditorDjinn(Frame f, String titel, Editable e) {
		this(f, titel, e, true, false, null, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
	 *
	 * @param f     Der Frame, der das Fenster aufruft.
	 * @param titel Der Titel des Dialogs.
	 * @param e     Das Editable, das in dem Dialog editiert werden soll.
	 * @param ini   Eine Inidatei zur Speicherung der Fensterkoordinaten.
	 */
	public DialogEditorDjinn(Frame f, String titel, Editable e, Inifile ini) {
		this(f, titel, e, true, false, ini, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
	 *
	 * @param f                 Der Frame, der das Fenster aufruft.
	 * @param titel             Der Titel des Dialogs.
	 * @param e                 Das Editable, das in dem Dialog editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der L&ouml;schenbutton aktiviert werden
	 *                          soll.
	 * @param historieAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der Historienbutton aktiviert werden soll.
	 */
	public DialogEditorDjinn(Frame f, String titel, Editable e, boolean loeschenAktiviert, boolean historieAktiviert) {
		this(f, titel, e, loeschenAktiviert, historieAktiviert, null, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
	 *
	 * @param f                 Der Frame, der das Fenster aufruft.
	 * @param titel             Der Titel des Dialogs.
	 * @param e                 Das Editable, das in dem Dialog editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der L&ouml;schenbutton aktiviert werden
	 *                          soll.
	 * @param historieAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der Historienbutton aktiviert werden soll.
	 * @param ini               Eine Inidatei zur Speicherung der Fensterkoordinaten.
	 */
	public DialogEditorDjinn(Frame f, String titel, Editable e, boolean loeschenAktiviert, boolean historieAktiviert,
			Inifile ini) {
		this(f, titel, e, loeschenAktiviert, historieAktiviert, ini, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
	 *
	 * @param f                 Der Frame, der das Fenster aufruft.
	 * @param titel             Der Titel des Dialogs.
	 * @param e                 Das Editable, das in dem Dialog editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der L&ouml;schenbutton aktiviert werden
	 *                          soll.
	 * @param historieAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der Historienbutton aktiviert werden soll.
	 * @param ini               Eine Inidatei zur Speicherung der Fensterkoordinaten.
	 * @param split             Wird diese Flagge gesetzt, erzeugt das EditorDjinnPanel im Falle eines TabbedEditables
	 *                          einen zweigeteilten Anzeigebereich. In der oberen H&auml;lfte wird das erste Panel des
	 *                          TabbedEditables angezeigt. Die untere H&auml;lfte beinhaltet die restlichen Panels. Ist
	 *                          die Flagge nicht gesetzt, werden alle Panels in Tabs eingef&uuml;gt.
	 * @param locked            Diese Flagge mu&szlig; gesetzt werden, wenn der Datensatz gesperrt ist.
	 * @param mode              Der Modus, in dem das Panel betrieben wird (Neuanlage, &Auml;nderung oder Duplikation).
	 */
	public DialogEditorDjinn(Frame f, String titel, Editable e, boolean loeschenAktiviert, boolean historieAktiviert,
			Inifile ini, boolean split, boolean locked, EditorDjinnMode mode) {
		super(f, titel, true, ini);
		this.construct(titel, e, loeschenAktiviert, historieAktiviert, ini, split, locked, mode);
	}

	/**
	 * Generiert einen EditorDjinn ohne bezug zu einen anderen Fenster anhand der &uuml;bergebenen Parameter.
	 *
	 * @param titel             Der Titel des Dialogs.
	 * @param e                 Das Editable, das in dem Dialog editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der L&ouml;schenbutton aktiviert werden
	 *                          soll.
	 * @param historieAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der Historienbutton aktiviert werden soll.
	 * @param ini               Eine Inidatei zur Speicherung der Fensterkoordinaten.
	 * @param split             Wird diese Flagge gesetzt, erzeugt das EditorDjinnPanel im Falle eines TabbedEditables
	 *                          einen zweigeteilten Anzeigebereich. In der oberen H&auml;lfte wird das erste Panel des
	 *                          TabbedEditables angezeigt. Die untere H&auml;lfte beinhaltet die restlichen Panels. Ist
	 *                          die Flagge nicht gesetzt, werden alle Panels in Tabs eingef&uuml;gt.
	 * @param locked            Diese Flagge mu&szlig; gesetzt werden, wenn der Datensatz gesperrt ist.
	 * @param mode              Der Modus, in dem das Panel betrieben wird (Neuanlage, &Auml;nderung oder Duplikation).
	 */
	public DialogEditorDjinn(String titel, Editable e, boolean loeschenAktiviert, boolean historieAktiviert,
			Inifile ini, boolean split, boolean locked, EditorDjinnMode mode) {
		super(ini);
		this.setModal(true);
		this.setTitle(titel);
		this.construct(titel, e, loeschenAktiviert, historieAktiviert, ini, split, locked, mode);
	}

	/* Diese Methode generiert den Fensterinhalt. */
	private void construct(String titel, Editable e, boolean loeschenAktiviert, boolean historieAktiviert, Inifile ini,
			boolean split, boolean locked, EditorDjinnMode mode) {
		this.locked = locked;
		this.mode = mode;
		this.setIdentifier(this.getClass().toString() + "-" + e.getClass().toString());
		this.setContext("corent.djinn.DialogEditorDjinn-" + e.getClass().toString());
		this.panel = new DefaultEditorDjinnPanel(this, e, new DefaultEditorDjinnButtonFactory(loeschenAktiviert,
				(e instanceof JasperReportable), historieAktiviert), split, locked, ini, mode) {
			public boolean isUnique(Persistent p) {
				return isObjectUnique(p);
			}
		};
		this.panel.addEditorDjinnListener(new EditorDjinnListener() {
			public void objectReadyToPrint() {
				doReadyToPrint();
			}

			public void objectPrinted() {
				doPrinted();
			}

			public void objectDeleted() {
				doDeleted();
			}

			public void objectBatchChanged(Hashtable<Integer, Object> ht) {
				doBatchChanged(ht);
			}

			public void objectChanged(boolean saveOnly) {
				doChanged(saveOnly);
			}

			public void objectDiscarded() {
				canceled = true;
				doDiscarded();
			}

			public void djinnClosing() {
				doClosing();
			}

			public void djinnClosed() {
				dispose();
				setVisible(false);
				doClosed();
			}
		});
		this.setContentPane(panel);
		this.pack();
		this.panel.doOpened();
		if (System.getProperty("corent.resource.type", "none").equalsIgnoreCase("properties")) {
			COUtil.Update(this, null, new PropertyRessourceManager());
		}
		this.setVisible(true);
	}

	/** @return Referenz auf das im EditorDjinn angezeigte Editable. */
	public Component getDjinnOwner() {
		return this.panel.getOwner();
	}

	/*
	 * *
	 * 
	 * @changed OLI 05.01.2009 - Hinzugef&uuml;gt. <P>OLI 12.01.2009 - Herausnahme wegen Zeitmangels und
	 * unvorhergesehener Probleme im Zusammenspiel mit dem <TT>DefaultEditorDjinnPanel</TT>. <P>
	 *
	 * / public void setEditable(Editable e) { this.panel.setEditable(e); }
	 */

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein anstehendes Print-Ereignis signalisiert.
	 */
	public void doReadyToPrint() {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein abgeschlossened Print-Ereignis signalisiert.
	 */
	public void doPrinted() {
	}

	/** Diese Methode wird aufgerufen, wenn der Djinn ein Delete-Ereignis signalisiert. */
	public void doDeleted() {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein Stapel&auml;nderungs-Ereignis signalisiert.
	 *
	 * @param ht Eine Hashtable&lt;Integer, Object&gt; mit Attribute-Id-Wert-Paaren, die die ge&auml;nderten Datenfelder
	 *           und ihre neuen Werte enthalten.
	 */
	public void doBatchChanged(Hashtable<Integer, Object> ht) {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein &Auml;nderungs-Ereignis signalisiert.
	 *
	 * @param saveOnly Diese Flagge wird gesetzt, wenn die Daten des durch den Djinn bearbeiteten Objektes gespeichert,
	 *                 der Djinn aber nicht geschlossen werdens soll.
	 *
	 * @changed OLI 29.10.2007 - Erweiterung um den Parameter <TT>saveOnly</TT>.
	 *
	 */
	public void doChanged(boolean saveOnly) {
	}

	/** Diese Methode wird aufgerufen, wenn der Djinn ein Abbruch-Ereignis signalisiert. */
	public void doDiscarded() {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein anstehendes Schliessen-Ereignis signalisiert.
	 */
	public void doClosing() {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein abgeschlossenes Schliessen-Ereignis signalisiert.
	 */
	public void doClosed() {
	}

	/** @return <TT>true</TT>, falls der Dialog mit dem Verwerfen-Button beendet wird. */
	public boolean isCanceled() {
		return this.canceled;
	}

	/**
	 * Diese Methode liefert den Wert <TT>true</TT>, falls das in dem EditorDjinn angezeigte Objekt gelockt ist (also
	 * nicht ge&auml;ndert werden darf).
	 *
	 * @return <TT>true</TT>, wenn das angezeigte Objekt gelockt, <TT>false</TT> sonst.
	 */
	public boolean isLocked() {
		return this.locked;
	}

	/**
	 * Diese Methode mu&szlig; den Wert <TT>true</TT> zur&uuml;ckliefern, wenn der Datensatz einzigartig ist.
	 *
	 * @param p Das Persistent-Objekt, das auf Einzigartigkeit gepr&uuml;ft werden soll.
	 * @return <TT>true</TT>, falls das Objekt einzigartig ist, <TT>false</TT> sonst.
	 */
	public boolean isObjectUnique(Persistent p) {
		return true;
	}

	/* Implementierung des Interfaces EditorDjinnController. */

	public Editable getEditable() {
		return this.panel.getEditable();
	}

	/**
	 * @changed OLI 29.01.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	public EditorDjinnMode getEditorDjinnMode() {
		return this.mode;
	}

	public boolean isDeleteConfirmed() {
		return (JOptionPane.showConfirmDialog(this,
				StrUtil.FromHTML(System.getProperty("corent.djinn.infodialog.delete.text",
						"Soll der Datensatz " + "wirklich gel&ouml;scht werden?")),
				StrUtil.FromHTML(
						System.getProperty("corent.djinn.infodialog.discard.title", "R&uuml;ckfrage L&ouml;schen")),
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}

	public boolean isDiscardConfirmed() {
		if (this.isLocked()) {
			return true;
		}
		return (JOptionPane.showConfirmDialog(this,
				StrUtil.FromHTML(System.getProperty("corent.djinn.infodialog.discard.text",
						"Sollen die &Auml;nderungen am " + "Datensatz wirklich verworfen werden?")),
				StrUtil.FromHTML(
						System.getProperty("corent.djinn.infodialog.discard.title", "R&uuml;ckfrage Verwerfen")),
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}

}
