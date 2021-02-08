/*
 * FrameEditorDjinn.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.djinn;

import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import corent.base.StrUtil;
import corent.db.Persistent;
import corent.files.Inifile;
import corent.gui.COUtil;
import corent.gui.JInternalFrameWithInifile;
import corent.gui.RessourceManager;
import corent.gui.Ressourced;
import corent.print.JasperReportable;
import logging.Logger;

/**
 * Diese Implementierung des EditorDjinns spielt sich in einem Frame ab.
 * F&uuml;r Anwendungen, die nur zum Manipulieren eines einzelnen Datensatzes
 * erstellt werden sollen, ist diese Variante die erste Wahl.
 * <P>
 * Der Frame arbeitet eng mit dem PanelEditorDjinn zusammen, in dem die
 * eigentliche Arbeit getan wird.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 29.10.2007 - Erweiterung der Methode <TT>transferValues()</TT>
 *          um den Parameter <TT>saveOnly</TT>. Anpassungen an das
 *          DefaultEditorDjinnPanel mit Speichern-Button.
 *          <P>
 *          OLI 18.01.2008 - Erweiterung um die Implementierung des Interfaces
 *          Ressourced.
 *          <P>
 *          OLI 26.02.2008 - Umstellung auf EditorDjinnMode-Betrieb.
 *          <P>
 *          OLI 31.03.2008 - Umstellung der Info-Dialog (Verwerfen,
 *          L&ouml;schen) auf Property-Ressourcen.
 *          <P>
 *          OLI 15.06.2008 - Erweiterung um den Aufruf der Methode
 *          <TT>doOpened()</TT> des eingebundenen DefaultEditorDjinnPanels beim
 *          &Ouml;ffnen des EditorDjinns.
 *          <P>
 *          OLI 19.06.2008 - Erweiterung um die Methode <TT>doOpened()</TT> und
 *          die damit verbundene Logik.
 *          <P>
 *          OLI 05.01.2009 - Erweiterung um die Implementierung der Methode
 *          <TT>setEditable(Editable)</TT>.
 *          <P>
 *          OLI 12.01.2009 - Deaktivierung der Methode
 *          <TT>setEditable(Editable)</TT>. Um das korrekt durchzuprogrammieren
 *          fehlt im Momment die Zeit.
 *          <P>
 *          OLI 29.01.2009 - Implementierung der Erweiterung des Interfaces
 *          <TT>EditorDjinnController</TT> durch die Methode
 *          <TT>getEditorDjinnMode()</TT>.
 *          <P>
 *
 */

public class InternalFrameEditorDjinn extends JInternalFrameWithInifile implements EditorDjinnController, Ressourced {

	private static final Logger log = Logger.getLogger(InternalFrameEditorDjinn.class);

	/*
	 * Diese Flagge wird gesetzt, wenn der EditorDjinn zur Stapelpflege
	 * ge&ouml;ffnet ist.
	 */
	private boolean batch = false;
	/*
	 * Diese Flagge trifft eine Aussage dar&uuml;ber, ob das angezeigt Objekt
	 * gelockt ist.
	 */
	private boolean locked = false;
	/* Referenz auf das EditorPanel des InternalFrames. */
	private DefaultEditorDjinnPanel panel = null;
	/* Der EditorDjinnMode, mit dem der Frame aufgerufen worden ist. */
	private EditorDjinnMode mode = null;
	/* Referenz auf den desktop, in dem der Frame angezeigt werden soll. */
	private JDesktopPane desktop = null;

	/**
	 * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen
	 * Parameter.
	 *
	 * @param desktop Der Desktop, auf dem der InternalFrame abgebildet werden soll.
	 * @param titel   Der Titel des Frames.
	 * @param e       Das Editable, das in dem Frame editiert werden soll.
	 */
	public InternalFrameEditorDjinn(JDesktopPane desktop, String titel, Editable e) {
		this(desktop, titel, e, true, false, null, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen
	 * Parameter.
	 *
	 * @param desktop Der Desktop, auf dem der InternalFrame abgebildet werden soll.
	 * @param titel   Der Titel des Frames.
	 * @param e       Das Editable, das in dem Frame editiert werden soll.
	 * @param ini     Eine Inidatei zur Speicherung der Fensterkoordinaten.
	 */
	public InternalFrameEditorDjinn(JDesktopPane desktop, String titel, Editable e, Inifile ini) {
		this(desktop, titel, e, true, false, ini, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen
	 * Parameter.
	 *
	 * @param desktop           Der Desktop, auf dem der InternalFrame abgebildet
	 *                          werden soll.
	 * @param titel             Der Titel des Frames.
	 * @param e                 Das Editable, das in dem Frame editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          L&ouml;schenbutton aktiviert werden soll.
	 */
	public InternalFrameEditorDjinn(JDesktopPane desktop, String titel, Editable e, boolean loeschenAktiviert) {
		this(desktop, titel, e, loeschenAktiviert, false, null, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen
	 * Parameter.
	 *
	 * @param desktop           Der Desktop, auf dem der InternalFrame abgebildet
	 *                          werden soll.
	 * @param titel             Der Titel des Frames.
	 * @param e                 Das Editable, das in dem Frame editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          L&ouml;schenbutton aktiviert werden soll.
	 * @param ini               Eine Inidatei zur Speicherung der
	 *                          Fensterkoordinaten.
	 */
	public InternalFrameEditorDjinn(JDesktopPane desktop, String titel, Editable e, boolean loeschenAktiviert,
			Inifile ini) {
		this(desktop, titel, e, loeschenAktiviert, false, ini, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen
	 * Parameter.
	 *
	 * @param desktop           Der Desktop, auf dem der InternalFrame abgebildet
	 *                          werden soll.
	 * @param titel             Der Titel des Frames.
	 * @param e                 Das Editable, das in dem Frame editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          L&ouml;schenbutton aktiviert werden soll.
	 * @param druckenAktiviert  Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          Druckenbutton aktiviert werden soll.
	 * @param ini               Eine Inidatei zur Speicherung der
	 *                          Fensterkoordinaten.
	 */
	public InternalFrameEditorDjinn(JDesktopPane desktop, String titel, Editable e, boolean loeschenAktiviert,
			boolean druckenAktiviert, Inifile ini) {
		this(desktop, titel, e, loeschenAktiviert, druckenAktiviert, ini, false, false, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen
	 * Parameter.
	 *
	 * @param desktop           Der Desktop, auf dem der InternalFrame abgebildet
	 *                          werden soll.
	 * @param titel             Der Titel des Frames.
	 * @param e                 Das Editable, das in dem Frame editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          L&ouml;schenbutton aktiviert werden soll.
	 * @param druckenAktiviert  Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          Druckenbutton aktiviert werden soll.
	 * @param ini               Eine Inidatei zur Speicherung der
	 *                          Fensterkoordinaten.
	 * @param split             Wird diese Flagge gesetzt, erzeugt das
	 *                          EditorDjinnPanel im Falle eines TabbedEditables
	 *                          einen zweigeteilten Anzeigebereich. In der oberen
	 *                          H&auml;lfte wird das erste Panel des TabbedEditables
	 *                          angezeigt. Die untere H&auml;lfte beinhaltet die
	 *                          restlichen Panels. Ist die Flagge nicht gesetzt,
	 *                          werden alle Panels in Tabs eingef&uuml;gt.
	 * @param locked            Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          Datensatz gesperrt ist.
	 */
	public InternalFrameEditorDjinn(JDesktopPane desktop, String titel, Editable e, boolean loeschenAktiviert,
			boolean druckenAktiviert, Inifile ini, boolean split, boolean locked) {
		this(desktop, titel, e, loeschenAktiviert, druckenAktiviert, ini, split, locked, EditorDjinnMode.EDIT);
	}

	/**
	 * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen
	 * Parameter.
	 *
	 * @param desktop           Der Desktop, auf dem der InternalFrame abgebildet
	 *                          werden soll.
	 * @param titel             Der Titel des Frames.
	 * @param e                 Das Editable, das in dem Frame editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          L&ouml;schenbutton aktiviert werden soll.
	 * @param druckenAktiviert  Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          Druckenbutton aktiviert werden soll.
	 * @param ini               Eine Inidatei zur Speicherung der
	 *                          Fensterkoordinaten.
	 * @param split             Wird diese Flagge gesetzt, erzeugt das
	 *                          EditorDjinnPanel im Falle eines TabbedEditables
	 *                          einen zweigeteilten Anzeigebereich. In der oberen
	 *                          H&auml;lfte wird das erste Panel des TabbedEditables
	 *                          angezeigt. Die untere H&auml;lfte beinhaltet die
	 *                          restlichen Panels. Ist die Flagge nicht gesetzt,
	 *                          werden alle Panels in Tabs eingef&uuml;gt.
	 * @param locked            Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          Datensatz gesperrt ist.
	 * @param mode              Der Modus, in dem das Panel betrieben werden soll
	 *                          (Neuanlage, &Auml;nderung oder Duplikation).
	 */
	public InternalFrameEditorDjinn(JDesktopPane desktop, String titel, Editable e, boolean loeschenAktiviert,
			boolean druckenAktiviert, Inifile ini, boolean split, boolean locked, EditorDjinnMode mode) {
		this(desktop, titel, e, loeschenAktiviert, druckenAktiviert, false, ini, split, locked, mode, false);
	}

	/**
	 * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen
	 * Parameter.
	 *
	 * @param desktop           Der Desktop, auf dem der InternalFrame abgebildet
	 *                          werden soll.
	 * @param titel             Der Titel des Frames.
	 * @param e                 Das Editable, das in dem Frame editiert werden soll.
	 * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          L&ouml;schenbutton aktiviert werden soll.
	 * @param druckenAktiviert  Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          Druckenbutton aktiviert werden soll.
	 * @param historieAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          Historienbutton aktiviert werden soll.
	 * @param ini               Eine Inidatei zur Speicherung der
	 *                          Fensterkoordinaten.
	 * @param split             Wird diese Flagge gesetzt, erzeugt das
	 *                          EditorDjinnPanel im Falle eines TabbedEditables
	 *                          einen zweigeteilten Anzeigebereich. In der oberen
	 *                          H&auml;lfte wird das erste Panel des TabbedEditables
	 *                          angezeigt. Die untere H&auml;lfte beinhaltet die
	 *                          restlichen Panels. Ist die Flagge nicht gesetzt,
	 *                          werden alle Panels in Tabs eingef&uuml;gt.
	 * @param locked            Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          Datensatz gesperrt ist.
	 * @param mode              Der Modus, in dem das Panel betrieben werden soll
	 *                          (Neuanlage, &Auml;nderung oder Duplikation).
	 * @param batch             Diese Flagge mu&szlig; gesetzt werden, wenn der
	 *                          EditorDjinn als Stapelpflege ge&ouml;ffnet werden
	 *                          soll.
	 */
	public InternalFrameEditorDjinn(JDesktopPane desktop, String titel, Editable e, boolean loeschenAktiviert,
			boolean druckenAktiviert, boolean historieAktiviert, Inifile ini, boolean split, boolean locked,
			EditorDjinnMode mode, boolean batch) {
		super(titel, ini);
		this.batch = batch;
		this.desktop = desktop;
		this.locked = locked;
		this.mode = mode;
		this.setIdentifier(this.getClass().toString() + "-" + e.getClass().toString());
		this.setContext(".corent.djinn.InternalFrameEditorDjinn." + e.getClass().getName());
		log.debug("InternalFrameEditorDjinn opened with context " + this.getContext());
		this.panel = new DefaultEditorDjinnPanel(this, e, new DefaultEditorDjinnButtonFactory(loeschenAktiviert,
				e instanceof JasperReportable, historieAktiviert), split, locked, ini, mode, batch) {
			@Override
			public boolean isUnique(Persistent p) {
				return isObjectUnique(p);
			}
		};
		this.panel.addEditorDjinnListener(new EditorDjinnListener() {
			@Override
			public void objectReadyToPrint() {
				doReadyToPrint();
			}

			@Override
			public void objectPrinted() {
				doPrinted();
			}

			@Override
			public void objectDeleted() {
				doDeleted();
			}

			@Override
			public void objectBatchChanged(Hashtable<Integer, Object> ht) {
				doBatchChanged(ht);
			}

			@Override
			public void objectChanged(boolean saveOnly) {
				doChanged(saveOnly);
			}

			@Override
			public void objectDiscarded() {
				doDiscarded();
			}

			@Override
			public void djinnClosing() {
				doClosing();
			}

			@Override
			public void djinnClosed() {
				dispose();
				setVisible(false);
				doClosed();
			}
		});
		this.addInternalFrameListener(new InternalFrameAdapter() {
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				doClosed();
			}
		});
		this.setContentPane(this.panel);
		this.desktop.add(this);
		this.pack();
		this.setVisible(true);
		this.panel.doOpened();
		this.doOpened();
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein
	 * Stapel&auml;nderungs-Ereignis signalisiert.
	 *
	 * @param ht Eine Hashtable&lt;Integer, Object&gt; mit Attribute-Id-Wert-Paaren,
	 *           die die ge&auml;nderten Datenfelder und ihre neuen Werte enthalten.
	 */
	public void doBatchChanged(Hashtable<Integer, Object> ht) {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein &Auml;nderungs-Ereignis
	 * signalisiert.
	 *
	 * @param saveOnly Diese Flagge wird gesetzt, wenn die Daten des durch den Djinn
	 *                 bearbeiteten Objektes gespeichert, der Djinn aber nicht
	 *                 geschlossen werdens soll.
	 *
	 * @changed OLI 29.10.2007 - Erweiterung um den Parameter <TT>saveOnly</TT>.<BR>
	 *
	 */
	public void doChanged(boolean saveOnly) {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein abgeschlossenes
	 * Schliessen-Ereignis signalisiert.
	 */
	public void doClosed() {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein anstehendes
	 * Schliessen-Ereignis signalisiert.
	 */
	public void doClosing() {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein Delete-Ereignis
	 * signalisiert.
	 */
	public void doDeleted() {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein Abbruch-Ereignis
	 * signalisiert.
	 */
	public void doDiscarded() {
	}

	/**
	 * Diese Methode wird aufgerufen, nachdem der Djinn beschworen und angezeigt
	 * ist.
	 *
	 * @changed OLI 19.06.2008 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	public void doOpened() {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein abgeschlossened
	 * Print-Ereignis signalisiert.
	 */
	public void doPrinted() {
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Djinn ein anstehendes Print-Ereignis
	 * signalisiert.
	 */
	public void doReadyToPrint() {
	}

	/**
	 * Liefert den Wert <TT>true</TT>, wenn der EditorDjinn als Stapel&auml;nderung
	 * l&auml;uft.
	 *
	 * @return <TT>true</TT>, wenn der EditorDjinn als Stapel&auml;nderung
	 *         aufgerufen worden ist.
	 */
	public boolean isBatch() {
		return this.batch;
	}

	/**
	 * Diese Methode liefert den Wert <TT>true</TT>, falls das in dem EditorDjinn
	 * angezeigte Objekt gelockt ist (also nicht ge&auml;ndert werden darf).
	 *
	 * @return <TT>true</TT>, wenn das angezeigte Objekt gelockt, <TT>false</TT>
	 *         sonst.
	 */
	public boolean isLocked() {
		return this.locked;
	}

	/**
	 * Diese Methode mu&szlig; den Wert <TT>true</TT> zur&uuml;ckliefern, wenn der
	 * Datensatz einzigartig ist.
	 *
	 * @param p Das Persistent-Objekt, das auf Einzigartigkeit gepr&uuml;ft werden
	 *          soll.
	 * @return <TT>true</TT>, falls das Objekt einzigartig ist, <TT>false</TT>
	 *         sonst.
	 */
	public boolean isObjectUnique(Persistent p) {
		return true;
	}

	/**
	 * Ein Aufruf dieser Methode &uuml;bertr&auml;gt den Inhalt des EditorPanels in
	 * das Objekt. Beim Aufruf dieser Methode ist &auml;u&szlig;erste Vorsicht
	 * geboten, da Nebeneffekte in der Bearbeitungslogik des EditorDjinns nicht
	 * ausgeschlossen werden k&ouml;nnen.
	 *
	 * @param saveOnly Diese Flagge mu&szlig; gesetzt werden, wenn der Djinn seine
	 *                 Daten speichern soll, ohne geschlossen zu werden.
	 * @return <TT>true</TT>, wenn der Transfer erfolgreich abgeschossen werden
	 *         konnte, <TT>false</TT> sonst.
	 *
	 * @changed OLI 29.10.2007 - Erweiterung um den Parameter <TT>saveOnly</TT>.<BR>
	 */
	protected boolean transferValues(boolean saveOnly) {
		return this.panel.transferValues(saveOnly);
	}

	/* Implementierung des Interfaces EditorDjinnController. */

	@Override
	public boolean isDeleteConfirmed() {
		return (JOptionPane.showConfirmDialog(this,
				StrUtil.FromHTML(System.getProperty("corent.djinn.infodialog.delete.text",
						"Soll der Datensatz " + "wirklich gel&ouml;scht werden?")),
				StrUtil.FromHTML(
						System.getProperty("corent.djinn.infodialog.discard.title", "R&uuml;ckfrage L&ouml;schen")),
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
	}

	@Override
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

	/**
	 * @changed OLI 29.01.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	@Override
	public EditorDjinnMode getEditorDjinnMode() {
		return this.mode;
	}

	@Override
	public Editable getEditable() {
		return this.panel.getEditable();
	}

	/*
	 * *
	 * 
	 * @changed OLI 05.01.2009 - Hinzugef&uuml;gt. <P>OLI 12.01.2009 - Herausnahme
	 * wegen Zeitmangels und unvorhergesehener Probleme im Zusammenspiel mit dem
	 * <TT>DefaultEditorDjinnPanel</TT>. <P>
	 *
	 * / public void setEditable(Editable e) { this.panel.setEditable(e); }
	 */

	/* Implementierung des Interfaces Ressourced. */

	@Override
	public void update(RessourceManager rm) {
		String c = COUtil.GetFullContext(this);
		String c0 = "corent.djinn.InternalFrameEditorDjinn";
		String s = rm.getTitle(c);
		if (s.length() > 0) {
			this.setTitle(s);
		} else {
			s = rm.getTitle(c0);
			if (s.length() > 0) {
				this.setTitle(s);
			}
		}
		s = rm.getToolTipText(c);
		if (s.length() > 0) {
			this.setToolTipText(s);
		} else {
			s = rm.getToolTipText(c0);
			if (s.length() > 0) {
				this.setToolTipText(s);
			}
		}
		Icon icon = rm.getIcon(c);
		if (icon != null) {
			this.setFrameIcon(icon);
		} else {
			icon = rm.getIcon(c0);
			if (icon != null) {
				this.setFrameIcon(icon);
			}
		}
		/*
		 * Color color = rm.getBackground(c); if (color != null) {
		 * this.setBackground(color); } color = rm.getForeground(c); if (color != null)
		 * { this.setForeground(color); }
		 */
	}

}
