/*
 * DefaultEditorDjinnPanel.java
 *
 * 10.01.2004
 *
 * (c) O.Lieshoff
 *
 */

package corent.djinn;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import corent.base.Attributed;
import corent.base.Constants;
import corent.base.StrUtil;
import corent.base.Utl;
import corent.dates.TimestampModel;
import corent.db.Persistent;
import corent.db.xs.CentrallyHeld;
import corent.db.xs.CentrallyHeldWithLocalFields;
import corent.db.xs.DBFactoryUtil;
import corent.files.Inifile;
import corent.gui.AbstractMassiveListSelector;
import corent.gui.COCheckBox;
import corent.gui.ComponentWithInifile;
import corent.gui.ContextOwner;
import corent.gui.GUIUtil;
import corent.gui.MassiveListSelectorEvent;
import corent.gui.MassiveListSelectorListener;
import corent.gui.TimestampField;
import corent.gui.TimestampFieldListener;
import corent.print.JasperReportable;
import corent.util.SysUtil;

/**
 * Musterimplementierung des EditorDjinns auf Basis eines JPanels.
 * <P>
 * &Uuml;ber die Property <I>corent.djinn.DefaultEditorDjinnPanel.divider.offset</I> kann ein Wert gesetzt werden, um
 * den die Defaulteinstellung des Dividers bei der ersten Anzeige f&uuml;r eine Klasse erweitert werden soll. Hier durch
 * l&auml;&szlig;t sich das Problem der swing-internen Initialisierung umgehen.
 * <P>
 * &nbsp;
 *
 * <P>
 * <H3>Properties:</H3>
 * <TABLE BORDER=1>
 * <TR VALIGN=TOP>
 * <TH>Property</TH>
 * <TH>Default</TH>
 * <TH>Typ</TH>
 * <TH>Beschreibung</TH>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.djinn.CreateCycle.vk.down</TD>
 * <TD>VK_DOWN</TD>
 * <TD>String</TD>
 * <TD>&Uuml;ber diese Property k&ouml;nnen kann der Tastatuscode zum Weiterschalten des Focus innerhalb der
 * EditorDjinn-Logik konfiguriert werden (vorw&auml;rts).</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.djinn.CreateCycle.vk.down.alt</TD>
 * <TD>VK_ENTER</TD>
 * <TD>String</TD>
 * <TD>&Uuml;ber diese Property k&ouml;nnen kann der alternative Tastatuscode zum Weiterschalten des Focus innerhalb der
 * EditorDjinn-Logik konfiguriert werden (vorw&auml;rts).</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.djinn.CreateCycle.vk.escape</TD>
 * <TD>VK_ESCAPE</TD>
 * <TD>String</TD>
 * <TD>&Uuml;ber diese Property k&ouml;nnen kann der Tastatuscode zum Verwerfen der &Auml;nderungen im Dialog
 * konfiguriert werden.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.djinn.CreateCycle.vk.f12</TD>
 * <TD>VK_F12</TD>
 * <TD>String</TD>
 * <TD>&Uuml;ber diese Property k&ouml;nnen kann der Tastatuscode zum &Uuml;bernehmen der &Auml;nderungen im Dialog
 * konfiguriert werden.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.djinn.CreateCycle.vk.up</TD>
 * <TD>VK_UP</TD>
 * <TD>String</TD>
 * <TD>&Uuml;ber diese Property k&ouml;nnen kann der Tastatuscode zum Weiterschalten des Focus innerhalb der
 * EditorDjinn-Logik konfiguriert werden (r&uuml;ckw&auml;rts).</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.djinn.DefaultEditorDjinnPanel.<BR>
 * markup.first.field</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>Setzen Sie diese Property, um im Falle einer Fokussierung eines Textfeldes als erste Komponente nach &Ouml;ffnen
 * des Panels, den Inhalt des Textfeldes zu markieren.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.djinn.DefaultEditorDjinnPanel.<BR>
 * suppress.button.save</TD>
 * <TD>true</TD>
 * <TD>Boolean</TD>
 * <TD>Wenn diese Property gesetzt wird, werden die Speichern-Buttons generell nicht mit in das EditorDjinnPanel
 * &uuml;bernommen.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.djinn.DefaultEditorDjinnPanel.<BR>
 * suppress.button.save.<I>classname</I></TD>
 * <TD>true</TD>
 * <TD>Boolean</TD>
 * <TD>Wenn diese Property gesetzt wird, werden die Speichern-Buttons f&uuml;r Objekte der Klasse mit dem angegebenen
 * Namen nicht mit in das EditorDjinnPanel &uuml;bernommen.</TD>
 * </TR>
 * </TABLE>
 * <P>
 * nbsp;
 *
 * @author O.Lieshoff
 *         <P>
 *
 * @changed OLI 23.09.2007 - Die Methode <TT>getComponentTable()</TT> ist, bedingt durch Erweiterung des Interfaces
 *          <TT>EditorDjinn</TT>, auf public gesetzt worden.
 *          <P>
 *          OLI 29.10.2007 - Erweiterung um die notwendigen Codepassagen f&uuml;r den Einsatz eines Speichern-Buttons.
 *          <P>
 *          OLI 11.11.2007 - Erweiterung des Konstruktors um das Abblenden von Drucken- (nur bei JasperPrintables, die
 *          vor dem Speichern gedruckt werden m&uuml;ssen) und Speichern-Button bei gelockten Objekten. Au&szlig;erdem:
 *          Implementierung einer M&ouml;glichkeit &uuml;ber Properties, den Speichern-Button generell oder f&uuml;r
 *          einzelne Klassen von editierten Objekten wegzuschalten.
 *          <P>
 *          OLI 26.02.2008 - Umbau auf EditorDjinnMode-Betrieb.
 *          <P>
 *          OLI 14.03.2008 - HTMLisierung verbliebener Aufrufe der Methode
 *          <TT>StrUtil.GetProperty(String, String, String)</TT> in der Methode <TT>transferValue(boolean)</TT>.
 *          <P>
 *          OLI 17.04.2008 - Korrektur der HTML-Umwandlung. Die ersetzten Feldernamen werden nun auch korrekt behandelt
 *          in der Methode <TT>transferValues(boolean)</TT>.
 *          <P>
 *          OLI 13.05.2008 - Behandlung von <TT>SplittedEditorDjinnPanelListenern</TT>.
 *          <P>
 *          OLI 15.06.2008 - Hinzuf&uuml;gen der Methode <TT>doOpened()</TT>. Im Zusammenspiel mit &Auml;nderungen an
 *          den EditorDjinns wird dadurch die Fokussierung der ersten Komponente des EditorDjinnPanels nach &Ouml;ffnen
 *          des Djinns m&ouml;glich. Durch Konfiguration kann im Falle eines Textfeldes sogar dessen Inhalt markiert
 *          werden.
 *          <P>
 *          OLI 10.08.2008 - Erweiterung um die M&ouml;glichkeit die Funktionstasten (F12, Escape etc.) &uuml;ber
 *          Properties zu konfigurieren.
 *          <P>
 *          OLI 23.09.2008 - Entsch&auml;rfung des Threads zum Setzen des Rahmens um die fokussierte Komponente.
 *          <P>
 *          OLI 15.01.2009 - Ausschlu&szlig; von Speichern-Buttons f&uuml;r EditorDjinnModes ungleich <TT>EDIT</TT>.
 *          <P>
 *          OLI 23.03.2009 - Einbau der Reaktionslogik auf <TT>CentrallyHeld</TT>-Objekte.
 *          <P>
 *
 */

public class DefaultEditorDjinnPanel extends JPanel implements EditorDjinn, Runnable {

	/* Flagge zur Kennzeichnung eines Stapel&auml;nderungspanels. */
	private boolean batch = false;
	/*
	 * Flagge zur Anzeige, da&szlig; es sich bei dem Objekt um ein zentrales Objekt in lokalem Zusammenhang zu
	 * kennzeichnen.
	 */
	private boolean centralObject = false;
	/*
	 * Flagge zur Anzeige, da&szlig; es sich bei dem Panel eines f&uuml;r ein gelocktes Objekt handelt.
	 */
	private boolean locked = false;
	/* Flagge zum Stoppen des Aktualisierungs-Threads. */
	private boolean stopped = false;
	/* Referenz auf die Component, die als erstes focussiert werden soll. */
	private Component toFocusFirst = null;
	/* Referenz auf die Component, in der das DjinnPanel abgebildet werden soll. */
	private Component owner = null;
	/*
	 * Die Komponententabelle, die die Komponenten der einzelnen Tabs enth&auml;lt und zugreifbar macht.
	 */
	private ComponentTable komponenten = new ComponentTable();
	/* Referenz auf das angezeigte und gegebenenfalls manipulierte Objekt. */
	private Editable objekt = null;
	/*
	 * Eine Liste SubEditorDescriptoren geschl&uuml;sselt nach den Nummern der Tabs, in denen sie abgebildet werden
	 * sollen.
	 */
	private Hashtable subeditors = new Hashtable();
	/* Die Inidatei der Applikation zur Weitergabe an die Komponenten. */
	private Inifile ini = null;
	/* Der Drucken-Button des Panels. */
	private JButton buttonDrucken = null;
	/* Der Historien-Button des Panels. */
	private JButton buttonHistorie = null;
	/* Der Loeschen-Button des Panels. */
	private JButton buttonLoeschen = null;
	/* Der Speichern-Button des Panels. */
	private JButton buttonSpeichern = null;
	/* Der Uebernehmen-Button des Panels. */
	private JButton buttonUebernehmen = null;
	/* Der Verwerfen-Button des Panels. */
	private JButton buttonVerwerfen = null;
	/* Eine Referenz auf das JSplitPane, falls das Panel ein solches besitzt, sonst null. */
	private JSplitPane jsplitpane = null;
	/* Eine Referenz auf ein eventuell existierendes TabbedPane. */
	private JTabbedPane jtabbedpane = null;
	/* Der Thread zur Aktualisierung der Markierung f&uuml;r die fokussierte Komponente. */
	private Thread thread = null;
	/*
	 * Liste mit den EditorDjinnListenern, die auf die Ereignisse des EditorDjinns reagieren sollen.
	 */
	private Vector listener = new Vector();
	/*
	 * Liste mit BatchCheckBoxen, &uuml;ber die bei einer Stapel&auml;nderung ge&auml;nderte Attribute markiert werden
	 * k&ouml;nnen.
	 */
	private Vector stapelmarken = new Vector();

	/**
	 * Generiert einen EditorDjinn und &uuml;bernimmt die Daten des &uuml;bergebenen Editables. Zur Produktion der
	 * Buttons wird die ebenfalls &uuml;bergebenen EditorDjinnButtonFactory benutzt.
	 *
	 * @param ownr   Die Component, in der der EditorDjinn abgebildet werden soll.
	 * @param e      Das Editable, das in dem DefaultEditorDjinnPanel angezeigt werden soll.
	 * @param edbf   Eine EditorDjinnButtonFactory, die die notwendigen Buttons f&uuml;r das DefaultEditorDjinnPanel
	 *               produziert.
	 * @param split  Wird diese Flagge gesetzt, so wird im Falle mehrerer Tabs das Panel des ersten Tabs in die obere
	 *               H&auml;lfte des Fensters gesetzt, w&auml;hrend die anderen als TabbedPane in die untere wandern.
	 *               Sonst werden alle Panels in einem TabbedPane zusammengefa&szlig;t.
	 * @param locked Diese Flagge mu&szlig; gesetzt werden, wenn der Dialog gelockt ist und daher nicht gesperrt werden
	 *               kann.
	 * @param ini    Die Inidatei, aus der die Komponenten des Panels ihre Gestalt rekonstruieren k&ouml;nnen.
	 * @param mode   Der Modus, in dem das Panel betrieben werden soll (Neuanlage, &Auml;nderung oder Duplikation).
	 */
	public DefaultEditorDjinnPanel(Component ownr, Editable e, EditorDjinnButtonFactory edbf, boolean split,
			boolean locked, Inifile ini, EditorDjinnMode mode) {
		this(ownr, e, edbf, split, locked, ini, mode, false);
	}

	/**
	 * Generiert einen EditorDjinn und &uuml;bernimmt die Daten des &uuml;bergebenen Editables. Zur Produktion der
	 * Buttons wird die ebenfalls &uuml;bergebenen EditorDjinnButtonFactory benutzt.
	 *
	 * @param ownr   Die Component, in der der EditorDjinn abgebildet werden soll.
	 * @param e      Das Editable, das in dem DefaultEditorDjinnPanel angezeigt werden soll.
	 * @param edbf   Eine EditorDjinnButtonFactory, die die notwendigen Buttons f&uuml;r das DefaultEditorDjinnPanel
	 *               produziert.
	 * @param split  Wird diese Flagge gesetzt, so wird im Falle mehrerer Tabs das Panel des ersten Tabs in die obere
	 *               H&auml;lfte des Fensters gesetzt, w&auml;hrend die anderen als TabbedPane in die untere wandern.
	 *               Sonst werden alle Panels in einem TabbedPane zusammengefa&szlig;t.
	 * @param locked Diese Flagge mu&szlig; gesetzt werden, wenn der Dialog gelockt ist und daher nicht gesperrt werden
	 *               kann.
	 * @param ini    Die Inidatei, aus der die Komponenten des Panels ihre Gestalt rekonstruieren k&ouml;nnen.
	 * @param mode   Der Modus, in dem das Panel betrieben werden soll (Neuanlage, &Auml;nderung oder Duplikation).
	 * @param batch  &Uuml;ber diese Flagge wird gesteuert, ob das EditorDjinnPanel als Stapelpflege arbeiten soll.
	 *
	 * @changed OLI 11.11.2007 - Einbau des Abblendens von Drucken- (nur bei JasperPrintables, die vor dem Speichern
	 *          gedruckt werden m&uuml;ssen) und Speichern-Button bei gelockten Objekten. Au&szlig;erdem:
	 *          Implementierung einer M&ouml;glichkeit &uuml;ber Properties, den Speichern-Button generell oder f&uuml;r
	 *          einzelne Klassen von editierten Objekten wegzuschalten.
	 *          <P>
	 *          OLI 13.05.2008 - Behandlung von <TT>SplittedEditorDjinnPanelListenern</TT>.
	 *          <P>
	 *          OLI 15.01.2009 - Ausschlu&szlig; von Speichern-Buttons f&uuml;r EditorDjinnModes ungleich <TT>EDIT</TT>.
	 *          <P>
	 *          OLI 23.04.2009 - Einbau der Ausblendung von zentralen Feldern, wenn es sich bei dem bearbeiteten Objekt
	 *          um ein zentrales Objekt in lokalem Umfeld handelt.
	 *          <P>
	 *
	 */
	public DefaultEditorDjinnPanel(Component ownr, Editable e, EditorDjinnButtonFactory edbf, boolean split,
			boolean locked, Inifile ini, EditorDjinnMode mode, boolean batch) {
		super(new BorderLayout(Constants.HGAP, Constants.VGAP));
		boolean debug = Boolean.getBoolean("corent.djinn.debug")
				|| Boolean.getBoolean("corent.djinn.DefaultEditorPanel.debug");
		int maxcomp = 0;
		java.util.List<String> nonCentralFields = (e instanceof CentrallyHeldWithLocalFields
				? ((CentrallyHeldWithLocalFields) e).getNonCentralFieldNames()
				: new java.util.Vector<String>());
		JPanel tmpPanel0 = null;
		this.batch = batch;
		this.ini = ini;
		this.locked = locked;
		this.owner = ownr;
		if ((e instanceof CentrallyHeld) && !Boolean.getBoolean("corent.db.xs.mode.central")
				&& (((CentrallyHeld) e).getGLI() > 0)) {
			this.centralObject = true;
		}
		this.setBorder(
				new CompoundBorder(new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP),
						new EtchedBorder(Constants.ETCH)));
		this.setBorder(new CompoundBorder(this.getBorder(),
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
		// &Uuml;bergebene Variablen r&uuml;ckspeichern.
		this.objekt = e;
		// Button-Panel und Buttons bauen.
		this.buttonDrucken = edbf.createButtonDrucken();
		this.buttonHistorie = edbf.createButtonHistorie();
		this.buttonLoeschen = edbf.createButtonLoeschen();
		if (!Boolean.getBoolean("corent.djinn.DefaultEditorDjinnPanel.suppress.button.save")
				&& !Boolean.getBoolean(
						"corent.djinn.DefaultEditorDjinnPanel.suppress.button." + "save." + e.getClass().getName())
				&& (mode == EditorDjinnMode.EDIT)) {
			this.buttonSpeichern = edbf.createButtonSpeichern();
		}
		this.buttonUebernehmen = edbf.createButtonUebernehmen();
		this.buttonVerwerfen = edbf.createButtonVerwerfen();
		JPanel buttons = new JPanel(new GridLayout(1, 6, Constants.HGAP, Constants.VGAP));
		buttons.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH),
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
		if (this.buttonLoeschen != null) {
			buttons.add(this.buttonLoeschen);
		} else {
			buttons.add(new JLabel(""));
		}
		if ((this.buttonDrucken != null) && !this.batch) {
			buttons.add(this.buttonDrucken);
		} else {
			buttons.add(new JLabel(""));
		}
		if ((this.buttonHistorie != null) && (this.objekt instanceof HistoryWriter) && !this.batch) {
			buttons.add(this.buttonHistorie);
		} else {
			buttons.add(new JLabel(""));
		}
		if (!(this.objekt instanceof ConfirmSuppressor)) {
			if ((!this.batch) && (this.buttonSpeichern != null)) {
				buttons.add(this.buttonSpeichern);
			}
			buttons.add(this.buttonUebernehmen);
		}
		buttons.add(this.buttonVerwerfen);
		if (this.buttonDrucken != null) {
			this.buttonDrucken.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doButtonDrucken();
				}
			});
		}
		if (this.buttonLoeschen != null) {
			this.buttonLoeschen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if ((objekt instanceof EditorDjinnMaster)
							&& (((EditorDjinnMaster) objekt).isDeleteConfirmSuppressed())) {
						// NOP
					} else if ((owner instanceof EditorDjinnController)
							&& (!((EditorDjinnController) owner).isDeleteConfirmed())) {
						return;
					}
					doButtonLoeschen();
				}
			});
		}
		if (this.buttonSpeichern != null) {
			this.buttonSpeichern.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doButtonUebernehmen(true);
				}
			});
		}
		this.buttonUebernehmen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doButtonUebernehmen(false);
			}
		});
		if (this.buttonHistorie != null) {
			this.buttonHistorie.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doButtonHistorie();
				}
			});
		}
		this.buttonVerwerfen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((objekt instanceof EditorDjinnMaster)
						&& (((EditorDjinnMaster) objekt).isDiscardConfirmSuppressed())) {
					// NOP
				} else if ((owner instanceof EditorDjinnController)
						&& (!((EditorDjinnController) owner).isDiscardConfirmed())) {
					return;
				}
				doButtonVerwerfen();
			}
		});
		Vector buttonliste = new Vector();
		if (this.buttonLoeschen != null) {
			buttonliste.addElement(this.buttonLoeschen);
		}
		if ((this.buttonDrucken != null) && (this.objekt instanceof Printable) && !this.batch) {
			buttonliste.addElement(this.buttonDrucken);
		}
		if ((this.buttonHistorie != null) && (this.objekt instanceof HistoryWriter) && !this.batch) {
			buttonliste.addElement(this.buttonHistorie);
		}
		if (!(this.objekt instanceof ConfirmSuppressor)) {
			if ((!this.batch) && (this.buttonSpeichern != null)) {
				buttonliste.addElement(this.buttonSpeichern);
			}
			buttonliste.addElement(this.buttonUebernehmen);
		}
		buttonliste.addElement(this.buttonVerwerfen);
		KeyListenerDjinn.CreateRow(buttonliste, true, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
		// Attributkomponenten bauen.
		EditorDescriptorList edl = e.getEditorDescriptorList();
		int componentOnTabOne = 0;
		EditorDescriptor ed = null;
		JComponent comp = null;
		JLabel label = null;
		SubEditor se = null;
		SubEditorDescriptor sed = null;
		for (int i = 0, len = edl.size(); i < len; i++) {
			ed = edl.elementAt(i);
			if (this.batch && !ed.isAlterInBatch()) {
				continue;
			}
			if (ed instanceof SubEditorDescriptor) {
				if (debug) {
					System.out.println("se " + i + " - " + ed.getTab());
				}
				sed = (SubEditorDescriptor) ed;
				se = sed.getSubEditorFactory().createSubEditor(this.getOwner(), (Attributed) this.objekt,
						this.getComponentTable());
				for (int j = 0, lenj = se.getComponentCount(); j < lenj; j++) {
					if (this.centralObject) {
						if (!nonCentralFields.contains(ed.getName())) {
							se.getComponent(j).setEnabled(false);
						}
					}
					if (locked) {
						se.getComponent(j).setEnabled(false);
					}
					this.komponenten.addCell(ed.getTab(), new DefaultEditorDjinnCell(sed, se.getComponent(j),
							se.getLabel(j), se.getComponentPanel(j)));
				}
				this.subeditors.put(new Integer(ed.getTab()), se);
			} else {
				if (debug) {
					System.out.println("p  " + i + " - " + ed.getTab() + " - " + ed.getClass().getName() + " - " + ed);
				}
				label = ed.getLabelFactory().createLabel(ed);
				comp = ed.getComponentFactory().createComponent(ed, owner, this.ini);
				if (this.centralObject) {
					if (!nonCentralFields.contains(ed.getName())) {
						comp.setEnabled(false);
					}
				}
				if (locked) {
					comp.setEnabled(false);
				}
				if (e instanceof EditorDjinnCellAlterListener) {
					this.createAlterListener(comp);
				}
				this.komponenten.addCell(ed.getTab(), new DefaultEditorDjinnCell(ed, comp, label, null));
				if (ed.getTab() == 0) {
					componentOnTabOne++;
					if ((this.toFocusFirst == null) && comp.isEnabled()) {
						this.toFocusFirst = comp;
					}
				}
			}
		}
		// Panel zusammensetzen.
		int tabCount = this.komponenten.getTabCount();
		JPanel panel = null;
		JTabbedPane jtp = null;
		if ((tabCount > 1) && (!(this.objekt instanceof TabbedEditable))) {
			throw new IndexOutOfBoundsException("editable object is not useable in JTabbedPane.");
		} else if (tabCount > 1) {
			jtp = ((TabbedEditable) this.objekt).getTabbedPaneFactory().createTabbedPane();
			jtp.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_F12) {
						buttonUebernehmen.requestFocus();
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						buttonVerwerfen.requestFocus();
					}
				}
			});
		}
		for (int t = 0; t < tabCount; t++) {
			JPanel panelEditor = null;
			se = (SubEditor) this.subeditors.get(new Integer(t));
			if (se != null) {
				panelEditor = se.getPanel();
			} else {
				JPanel panelStapelmarken = null;
				if (this.batch) {
					panelStapelmarken = new JPanel(
							new GridLayout(this.komponenten.getSize(t), 1, Constants.HGAP, Constants.VGAP));
				}
				JPanel panelKomponenten = new JPanel(
						new GridLayout(this.komponenten.getSize(t), 1, Constants.HGAP, Constants.VGAP));
				JPanel panelLabels = new JPanel(
						new GridLayout(this.komponenten.getSize(t), 1, Constants.HGAP, Constants.VGAP));
				for (int i = 0, len = this.komponenten.getSize(t); i < len; i++) {
					DefaultEditorDjinnCell dedc = this.komponenten.getCell(t, i);
					dedc.getLabel().setLabelFor(dedc.getComponent());
					panelLabels.add(dedc.getLabel());
					JPanel panelZelle = new JPanel(new GridLayout(1, 1, Constants.HGAP, Constants.VGAP));
					panelZelle.add(dedc.getComponent());
					dedc.setPanel(panelZelle);
					panelKomponenten.add(panelZelle);
					if (this.batch) {
						BatchCheckBox bcb = new BatchCheckBox(dedc.getEditorDescriptor(), dedc.getComponent(),
								"BatchCheckBox-" + dedc.getEditorDescriptor().getAttributeId());
						this.stapelmarken.addElement(bcb);
						panelStapelmarken.add(bcb);
					}
					maxcomp++;
				}
				for (int i = this.komponenten.getSize(t) - 1; i >= 0; i--) {
					DefaultEditorDjinnCell dedc = this.komponenten.getCell(t, i);
					if (!dedc.getComponent().isEnabled()) {
						this.komponenten.removeCell(t, i);
					}
				}
				panelEditor = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
				panelEditor.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH),
						new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
				JPanel panelNorth = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
				if (this.batch) {
					JPanel tmpPanel = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
					tmpPanel.add(panelLabels, BorderLayout.CENTER);
					tmpPanel.add(panelStapelmarken, BorderLayout.EAST);
					panelNorth.add(tmpPanel, BorderLayout.WEST);
				} else {
					panelNorth.add(panelLabels, BorderLayout.WEST);
				}
				panelNorth.add(panelKomponenten, BorderLayout.CENTER);
				panelEditor.add(panelNorth, BorderLayout.NORTH);
			}
			if (tabCount > 1) {
				JPanel p = (JPanel) jtp.getComponentAt(t);
				p.add(panelEditor);
				if (t == 0) {
					tmpPanel0 = panelEditor;
				}
			} else {
				panel = panelEditor;
				int psize = panel.getPreferredSize().height + maxcomp * 3;
				panel.setPreferredSize(new Dimension(panel.getPreferredSize().width, psize));
				panel.setMinimumSize(new Dimension(panel.getPreferredSize().width, psize));
			}
		}
		// Tastatursteuerung aufbauen.
		for (int t = 0; t < tabCount; t++) {
			KeyListenerDjinn.CreateCycle(this.komponenten.getComponents(t), this.buttonUebernehmen,
					this.buttonVerwerfen, false,
					SysUtil.GetKeyCode(System.getProperty("corent.djinn.CreateCycle.vk.up", "VK_UP")),
					SysUtil.GetKeyCode(System.getProperty("corent.djinn.CreateCycle.vk.down", "VK_DOWN")),
					SysUtil.GetKeyCode(System.getProperty("corent.djinn.CreateCycle.vk.down.alt", "VK_ENTER")),
					SysUtil.GetKeyCode(System.getProperty("corent.djinn.CreateCycle.vk.f12", "VK_F12")),
					SysUtil.GetKeyCode(System.getProperty("corent.djinn.CreateCycle.vk.excape", "VK_ESCAPE")));
		}
		// Mainpanel zusammensetzen.
		this.add(buttons, BorderLayout.SOUTH);
		if (tabCount > 1) {
			if (split) {
				if (Boolean.getBoolean("corent.djinn.DefaultEditorDjinnPanel.splitpane") || (System
						.getProperty("corent.djinn.DefaultEditorDjinnPanel.paneltype", "").equalsIgnoreCase("split"))) {
					JSplitPane panelMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tmpPanel0, jtp);
					this.jsplitpane = panelMain;
					if (this.owner instanceof ComponentWithInifile) {
						try {
							int divpos = this.ini.readInt(this.objekt.getClass().getName(), "JSplitPane-Divider", -1);
							if ((divpos > 0) && ((int) tmpPanel0.getPreferredSize().getHeight() <= divpos)) {
								panelMain.setDividerLocation(divpos);
							} else {
								panelMain.setDividerLocation(((int) tmpPanel0.getPreferredSize().getHeight())
										+ Integer.getInteger("corent.djinn.DefaultEditorDjinnPanel.divider.offset",
												componentOnTabOne * 2));
							}
						} catch (Exception e0) {
							e0.printStackTrace();
							System.out.println("WARNING: while recreating dividers location in "
									+ "JSplitPane in DefaultEditorDjinnPanel!");
						}
					}
					jtp.removeTabAt(0);
					this.add(panelMain, BorderLayout.CENTER);
				} else if (System.getProperty("corent.djinn.DefaultEditorDjinnPanel.paneltype", "")
						.equalsIgnoreCase("border")) {
					JPanel panelMain = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
					jtp.removeTabAt(0);
					panelMain.add(tmpPanel0, BorderLayout.NORTH);
					panelMain.add(jtp, BorderLayout.CENTER);
					this.add(panelMain, BorderLayout.CENTER);
				} else {
					JPanel panelMain = new JPanel(new GridLayout(2, 1, Constants.HGAP, Constants.VGAP));
					jtp.removeTabAt(0);
					panelMain.add(tmpPanel0);
					panelMain.add(jtp);
					this.add(panelMain, BorderLayout.CENTER);
				}
			} else {
				this.add(jtp, BorderLayout.CENTER);
			}
		} else {
			this.add(panel, BorderLayout.CENTER);
		}
		int psize = this.getPreferredSize().height + 8;
		this.setPreferredSize(new Dimension(this.getPreferredSize().width, psize));
		this.setMinimumSize(new Dimension(this.getPreferredSize().width, psize));
		// Aktualisierungs-Thread starten.
		this.thread = new Thread(this);
		this.thread.start();
		if (this.objekt instanceof EditorDjinnButtonMaster) {
			EditorDjinnButtonMaster edbm = (EditorDjinnButtonMaster) this.objekt;
			if (this.buttonDrucken != null) {
				this.buttonDrucken.setEnabled(edbm.isButtonPrintEnabled());
			}
			if (this.buttonHistorie != null) {
				this.buttonHistorie.setEnabled(edbm.isButtonHistoryEnabled());
			}
			if (this.buttonLoeschen != null) {
				this.buttonLoeschen.setEnabled(edbm.isButtonDeleteEnabled());
			}
			if (this.buttonSpeichern != null) {
				this.buttonSpeichern.setEnabled(edbm.isButtonSaveEnabled());
			}
			if (this.buttonUebernehmen != null) {
				this.buttonUebernehmen.setEnabled(edbm.isButtonOkEnabled());
			}
			if (this.buttonVerwerfen != null) {
				this.buttonVerwerfen.setEnabled(edbm.isButtonCancelEnabled());
			}
		}
		if (this.locked) {
			if ((this.buttonDrucken != null) && (e instanceof JasperReportable)
					&& (((JasperReportable) e).isSaveBeforePrintingRequired())) {
				this.buttonDrucken.setEnabled(false);
			}
			if (this.buttonLoeschen != null) {
				this.buttonLoeschen.setEnabled(false);
			}
			if (this.buttonSpeichern != null) {
				this.buttonSpeichern.setEnabled(false);
			}
			this.buttonUebernehmen.setEnabled(false);
		} else if (this.centralObject) {
			if ((this.buttonDrucken != null) && (e instanceof JasperReportable)
					&& (((JasperReportable) e).isSaveBeforePrintingRequired())) {
				this.buttonDrucken.setEnabled(false);
			}
			if (this.buttonLoeschen != null) {
				this.buttonLoeschen.setEnabled(false);
			}
			if (nonCentralFields.size() == 0) {
				if (this.buttonSpeichern != null) {
					this.buttonSpeichern.setEnabled(false);
				}
				this.buttonUebernehmen.setEnabled(false);
			}
		}
		if (this.objekt instanceof EditorDjinnMaster) {
			((EditorDjinnMaster) this.objekt).doAfterDjinnSummoned(this.getComponentTable(), mode);
		}
		if (this.objekt instanceof EditorDjinnObserver) {
			Hashtable<String, Object> htp = new Hashtable<String, Object>();
			htp.put("comps", this.getComponentTable());
			htp.put("mode", mode);
			((EditorDjinnObserver) this.objekt).eventDetected(EditorDjinnEventType.SUMMONED, htp);
		}
		if (this.objekt instanceof SplittedEditorDjinnPanelListener) {
			((SplittedEditorDjinnPanelListener) this.objekt).notifySplitState(split);
		}
		// Gegebenenfalls Abblendungen von Tabs (bei einem TabbedPane).
		if (jtp != null) {
			boolean enabled = false;
			for (int t = 0, tabcnt = jtp.getTabCount(); t < tabcnt; t++) {
				enabled = ((TabbedEditable) this.objekt).isTabEnabled(t);
				if (!enabled) {
					jtp.setEnabledAt(t, enabled);
					jtp.getComponentAt(t).setEnabled(enabled);
					GUIUtil.SetEnabled(jtp.getComponentAt(t), enabled);
				}
			}
		}
		this.jtabbedpane = jtp;
	}

	/**
	 * Wird aufgerufen, sobald das Fenster, in dem das Panel angezeigt wird, sichtbar wird.
	 *
	 * @changed OLI 15.06.2008 - Hinzugef&uuml;gt.
	 *
	 */
	public void doOpened() {
		if (this.toFocusFirst != null) {
			this.toFocusFirst.requestFocus();
			if (Boolean.getBoolean("corent.djinn.DefaultEditorDjinnPanel.markup.first.field")
					&& ((this.toFocusFirst instanceof TextComponent)
							|| (this.toFocusFirst instanceof JTextComponent))) {
				if (this.toFocusFirst instanceof TextComponent) {
					TextComponent tc = (TextComponent) this.toFocusFirst;
					tc.selectAll();
				} else {
					JTextComponent tc = (JTextComponent) this.toFocusFirst;
					tc.selectAll();
				}
			}
		}
	}

	/** Wird aufgerufen, wenn der Benutzer den Drucken-Button bet&auml;tigt. */
	protected void doButtonDrucken() {
		this.fireObjectReadyToPrint();
		if (this.objekt instanceof Printable) {
			((Printable) this.objekt).print();
		}
		this.fireObjectPrinted();
	}

	/** Wird aufgerufen, wenn der Benutzer den Historien-Button bet&auml;tigt. */
	protected void doButtonHistorie() {
		if (this.objekt instanceof HistoryWriter) {
			((HistoryWriter) this.objekt).doShowHistory();
		}
	}

	/** Wird aufgerufen, wenn der Benutzer den Loeschen-Button bet&auml;tigt. */
	protected void doButtonLoeschen() {
		if (this.objekt instanceof EditorDjinnMaster) {
			if (!((EditorDjinnMaster) this.objekt).doBeforeDelete(this.getComponentTable())) {
				return;
			}
		}
		this.fireObjectDeleted();
		this.doClose();
	}

	/**
	 * Diese Methode kann aufgerufen werden, um den Inhalt des EditorDjinnPanels in das dazugeh%ouml;rige Objekt zu
	 * &uuml;bernehmen. Sie wird auch von der Methode <TT>doButtonUebernehmen()</TT> aufgerufen.
	 *
	 * @param saveOnly Diese Flagge wird gesetzt, wenn die Daten aus dem EditorDjinnPanel lediglich gespeichert, der
	 *                 EditorDjinn aber nicht geschlossen werden soll.
	 * @return <TT>true</TT>, wenn der Dialog mit dem EditorDjinnPanel geschlossen werden darf.
	 *
	 * @changed OLI 29.10.2007 - Erweiterung um den Parameter <TT>saveOnly</TT> und die angebundene Logik.
	 *          <P>
	 *          OLI 14.03.2008 - HTMLisierung verbliebener Aufrufe der Methode
	 *          <TT>StrUtil.GetProperty(String, String, String)</TT>.
	 *          <P>
	 *          OLI 17.04.2008 - Korrektur der HTML-Umwandlung. Die ersetzten Feldernamen werden nun auch korrekt
	 *          behandelt.
	 *
	 */
	public boolean transferValues(boolean saveOnly) {
		if (this.objekt instanceof EditorDjinnMaster) {
			Hashtable<Integer, Object> contents = new Hashtable<Integer, Object>();
			for (int t = 0, lent = this.komponenten.getTabCount(); t < lent; t++) {
				for (int i = 0, len = this.komponenten.getSize(t); i < len; i++) {
					DefaultEditorDjinnCell dedc = (DefaultEditorDjinnCell) this.komponenten.getCell(t, i);
					if (((dedc.getComponent() != null) && (dedc.getEditorDescriptor() != null))
							&& (!(dedc.getEditorDescriptor() instanceof SubEditorDescriptor))) {
						Object value = dedc.getEditorDescriptor().getComponentFactory()
								.getValue(dedc.getEditorDescriptor(), dedc.getComponent());
						if (value != null) {
							contents.put(dedc.getEditorDescriptor().getAttributeId(), value);
						}
					}
				}
			}
			if (!((EditorDjinnMaster) this.objekt).doBeforeTransferValues(this.getComponentTable())) {
				return false;
			}
		}
		for (int t = 0, lent = this.komponenten.getTabCount(); t < lent; t++) {
			for (int i = 0, len = this.komponenten.getSize(t); i < len; i++) {
				DefaultEditorDjinnCell dedc = (DefaultEditorDjinnCell) this.komponenten.getCell(t, i);
				if (((dedc.getComponent() != null) && (dedc.getEditorDescriptor() != null))
						&& (!(dedc.getEditorDescriptor() instanceof SubEditorDescriptor))) {
					dedc.getEditorDescriptor().getComponentFactory().transferValue(dedc.getEditorDescriptor(),
							dedc.getComponent());
				}
			}
		}
		for (Iterator it = this.subeditors.keySet().iterator(); it.hasNext();) {
			Integer i = (Integer) it.next();
			SubEditor se = (SubEditor) this.subeditors.get(i);
			if (se != null) {
				se.transferData();
			}
		}
		if (this.objekt instanceof EditorDjinnMaster) {
			if (!((EditorDjinnMaster) this.objekt).doAfterTransferValues()) {
				return false;
			}
		}
		if (this.objekt instanceof Persistent) {
			Vector<String> vs = DBFactoryUtil.GetNotEmptyColumnnames((Persistent) this.objekt);
			if (vs.size() > 0) {
				String s = "";
				String s0 = "";
				for (int i = 0, len = vs.size(); i < len; i++) {
					if (s.length() > 0) {
						s = s.concat(", ");
					}
					s0 = vs.elementAt(i);
					if (System.getProperty("corent.djinn.DefaultEditorDjinnPanel.information.fields.name.for." + s0
							+ "." + this.objekt.getClass().getName()) != null) {
						s0 = System.getProperty("corent.djinn.DefaultEditorDjinnPanel.information.fields.name.for." + s0
								+ "." + this.objekt.getClass().getName());
					}
					s = s.concat(s0);
				}
				String header = StrUtil
						.FromHTML(StrUtil.GetProperty("corent.djinn.DefaultEditorDjinnPanel.information.fields.header",
								"Hinweis", this.objekt.getClass().getName()));
				String text = StrUtil.GetProperty("corent.djinn.DefaultEditorDjinnPanel.information.fields.text",
						"Die folgenden Felder m&uuml;ssen mit Werten versehen sein: $FIELDS",
						this.objekt.getClass().getName());
				text = text.replace("$FIELDS", s);
				JOptionPane.showMessageDialog(this.owner, StrUtil.FromHTML(text), header,
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			if (!this.isUnique((Persistent) this.objekt)) {
				Persistent p = (Persistent) this.objekt;
				String header = StrUtil
						.FromHTML(StrUtil.GetProperty("corent.djinn.DefaultEditorDjinnPanel.information.unique.header",
								"Hinweis", p.getClass().getName()));
				String text = StrUtil.GetProperty("corent.djinn.DefaultEditorDjinnPanel.information.unique.text",
						"Bitte beachten Sie folgende Einzigartigkeitsregeln: $UNIQUE", p.getClass().getName());
				text = text.replace("$UNIQUE", p.getPersistenceDescriptor().getUniqueClause());
				JOptionPane.showMessageDialog(this.owner, StrUtil.FromHTML(text), header,
						JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		this.fireObjectChanged(saveOnly);
		return true;
	}

	/**
	 * Wird aufgerufen, wenn der Benutzer den Uebernehmen-Button bet&auml;tigt.
	 *
	 * @param saveOnly Diese Flagge mu&szlig; gesetzt werden, wenn nur gespeichert, nicht aber beendet werden soll.
	 */
	protected void doButtonUebernehmen(boolean saveOnly) {
		if (!this.batch) {
			if (this.transferValues(saveOnly)) {
				if (!saveOnly) {
					this.doClose();
				}
			}
		} else {
			boolean changed = false;
			for (int i = 0, len = this.stapelmarken.size(); i < len; i++) {
				BatchCheckBox bcb = (BatchCheckBox) this.stapelmarken.elementAt(i);
				if (bcb.isSelected()) {
					changed = true;
				}
			}
			if (!changed) {
				JOptionPane.showMessageDialog(this.owner,
						Utl.GetProperty("corent.djinn.DefaultEditorDjinnPanel.error.batch.text",
								"Sie " + "haben kein Attribut zur Aktualisierung gekennzeichnet!"),
						Utl.GetProperty("corent.djinn.DefaultEditorDjinnPanel.error.batch.header", "Fehler"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			this.transferValues(saveOnly);
			Hashtable<Integer, Object> ht = new Hashtable<Integer, Object>();
			for (int i = 0, len = this.stapelmarken.size(); i < len; i++) {
				BatchCheckBox bcb = (BatchCheckBox) this.stapelmarken.elementAt(i);
				if (bcb.isSelected()) {
					ht.put(bcb.getAttributeId(), bcb.getComponentValue());
				}
			}
			if (ht.size() > 0) {
				this.fireObjectBatchChanged(ht);
				this.doClose();
			}
		}
	}

	/** Wird aufgerufen, wenn der Benutzer den Verwerfen-Button bet&auml;tigt. */
	protected void doButtonVerwerfen() {
		for (Iterator it = this.subeditors.keySet().iterator(); it.hasNext();) {
			Integer i = (Integer) it.next();
			SubEditor se = (SubEditor) this.subeditors.get(i);
			if (se != null) {
				se.cleanupData();
			}
		}
		if (this.objekt instanceof EditorDjinnMaster) {
			if (!((EditorDjinnMaster) this.objekt).doAfterCleanUp(this.getComponentTable())) {
				return;
			}
		}
		this.fireObjectDiscarded();
		this.doClose();
	}

	/** Schlie&szlig;t den Djinn. */
	protected void doClose() {
		this.fireDjinnClosing();
		this.stopped = true;
		try {
			this.thread.join();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		if ((this.owner instanceof ComponentWithInifile) && (this.jsplitpane != null)) {
			try {
				Inifile ini = ((ComponentWithInifile) this.owner).getInifile();
				ini.writeInt(this.objekt.getClass().getName(), "JSplitPane-Divider",
						this.jsplitpane.getDividerLocation());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("WARNING: while saving dividers location in JSplitPane "
						+ "in DefaultEditorDjinnPanel to inifile!");
			}
		}
		if (this.objekt instanceof EditorDjinnObserver) {
			((EditorDjinnObserver) this.objekt).eventDetected(EditorDjinnEventType.DISPELLED, null);
		}
		this.fireDjinnClosed();
	}

	/**
	 * Diese Methode mu&szlig; den Wert <TT>true</TT> zur&uuml;ckliefern, wenn der Datensatz einzigartig ist.
	 *
	 * @param p Das Persistent-Objekt, das auf Einzigartigkeit gepr&uuml;ft werden soll.
	 * @return <TT>true</TT>, falls das Objekt einzigartig ist, <TT>false</TT> sonst.
	 */
	public boolean isUnique(Persistent p) {
		return true;
	}

	private void createAlterListener(Component comp) {
		if (comp instanceof AbstractMassiveListSelector) {
			final AbstractMassiveListSelector mls = (AbstractMassiveListSelector) comp;
			mls.addMassiveListSelectorListener(new MassiveListSelectorListener() {
				public void selectionAltered(MassiveListSelectorEvent e) {
					if (objekt instanceof EditorDjinnCellAlterListener) {
						((EditorDjinnCellAlterListener) objekt).dataChanged(mls, getComponentTable());
					}
					if (objekt instanceof TabbedEditorDjinnCellAlterListener) {
						((TabbedEditorDjinnCellAlterListener) objekt).dataChanged(mls, getComponentTable(),
								jtabbedpane);
					}
				}

				public void selectionCleared(MassiveListSelectorEvent e) {
					if (objekt instanceof EditorDjinnCellAlterListener) {
						((EditorDjinnCellAlterListener) objekt).dataChanged(mls, getComponentTable());
					}
					if (objekt instanceof TabbedEditorDjinnCellAlterListener) {
						((TabbedEditorDjinnCellAlterListener) objekt).dataChanged(mls, getComponentTable(),
								jtabbedpane);
					}
				}
			});
		} else if (comp instanceof JCheckBox) {
			final JCheckBox jcb = (JCheckBox) comp;
			jcb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (objekt instanceof EditorDjinnCellAlterListener) {
						((EditorDjinnCellAlterListener) objekt).dataChanged(jcb, getComponentTable());
					}
					if (objekt instanceof TabbedEditorDjinnCellAlterListener) {
						((TabbedEditorDjinnCellAlterListener) objekt).dataChanged(jcb, getComponentTable(),
								jtabbedpane);
					}
				}
			});
		} else if (comp instanceof JTextField) {
			final JTextField jtf = (JTextField) comp;
			jtf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (objekt instanceof EditorDjinnCellAlterListener) {
						((EditorDjinnCellAlterListener) objekt).dataChanged(jtf, getComponentTable());
					}
					if (objekt instanceof TabbedEditorDjinnCellAlterListener) {
						((TabbedEditorDjinnCellAlterListener) objekt).dataChanged(jtf, getComponentTable(),
								jtabbedpane);
					}
				}
			});
			/*
			 * eine Alternative ?!? jtf.addCaretListener(new CaretListener() { public void caretUpdate(CaretEvent e) { }
			 * });
			 */
		} else if (comp instanceof TimestampField) {
			final TimestampField tsf = (TimestampField) comp;
			tsf.addTimestampFieldListener(new TimestampFieldListener() {
				public void dateChanged(TimestampModel ts) {
					if (objekt instanceof EditorDjinnCellAlterListener) {
						((EditorDjinnCellAlterListener) objekt).dataChanged(tsf, getComponentTable());
					}
					if (objekt instanceof TabbedEditorDjinnCellAlterListener) {
						((TabbedEditorDjinnCellAlterListener) objekt).dataChanged(tsf, getComponentTable(),
								jtabbedpane);
					}
				}

				public void timeChanged(TimestampModel ts) {
					if (objekt instanceof EditorDjinnCellAlterListener) {
						((EditorDjinnCellAlterListener) objekt).dataChanged(tsf, getComponentTable());
					}
					if (objekt instanceof TabbedEditorDjinnCellAlterListener) {
						((TabbedEditorDjinnCellAlterListener) objekt).dataChanged(tsf, getComponentTable(),
								jtabbedpane);
					}
				}
			});
		}
	}

	/* Implementierung des Interfaces EditorDjinn. */

	public void addEditorDjinnListener(EditorDjinnListener edl) {
		this.listener.addElement(edl);
	}

	public void removeEditorDjinnListener(EditorDjinnListener edl) {
		this.listener.removeElement(edl);
	}

	public void fireObjectBatchChanged(Hashtable<Integer, Object> ht) {
		for (int i = 0, len = this.listener.size(); i < len; i++) {
			((EditorDjinnListener) this.listener.elementAt(i)).objectBatchChanged(ht);
		}
	}

	public void fireObjectChanged(boolean saveOnly) {
		for (int i = 0, len = this.listener.size(); i < len; i++) {
			((EditorDjinnListener) this.listener.elementAt(i)).objectChanged(saveOnly);
		}
	}

	public void fireDjinnClosed() {
		for (int i = 0, len = this.listener.size(); i < len; i++) {
			((EditorDjinnListener) this.listener.elementAt(i)).djinnClosed();
		}
	}

	public void fireDjinnClosing() {
		for (int i = 0, len = this.listener.size(); i < len; i++) {
			((EditorDjinnListener) this.listener.elementAt(i)).djinnClosing();
		}
	}

	public void fireObjectDeleted() {
		for (int i = 0, len = this.listener.size(); i < len; i++) {
			((EditorDjinnListener) this.listener.elementAt(i)).objectDeleted();
		}
	}

	public void fireObjectDiscarded() {
		for (int i = 0, len = this.listener.size(); i < len; i++) {
			((EditorDjinnListener) this.listener.elementAt(i)).objectDiscarded();
		}
	}

	public void fireObjectPrinted() {
		for (int i = 0, len = this.listener.size(); i < len; i++) {
			((EditorDjinnListener) this.listener.elementAt(i)).objectPrinted();
		}
		if (this.objekt instanceof EditorDjinnPrintMaster) {
			((EditorDjinnPrintMaster) this.objekt).doAfterPrinting();
		}
	}

	public void fireObjectReadyToPrint() {
		if (this.objekt instanceof EditorDjinnPrintMaster) {
			((EditorDjinnPrintMaster) this.objekt).doBeforePrinting();
		}
		if (this.objekt instanceof JasperReportable) {
			if (((JasperReportable) this.objekt).isSaveBeforePrintingRequired()) {
				this.transferValues(true);
			}
		}
		for (int i = 0, len = this.listener.size(); i < len; i++) {
			((EditorDjinnListener) this.listener.elementAt(i)).objectReadyToPrint();
		}
	}

	public Component getOwner() {
		return this.owner;
	}

	public Editable getEditable() {
		return this.objekt;
	}

	/*
	 * *
	 * 
	 * @changed OLI 05.01.2009 - Hinzugef&uuml;gt. <P>OLI 12.01.2009 - Idee ist erstmal verworfen worden. <P>
	 *
	 */
	/*
	 * public void setEditable(Editable e) { DefaultEditorDjinnCell dedc = null; this.objekt = e; // OLI 05.01.2009 -
	 * Eigentlich wollte ich hier auf auf Nummer sicher gehen. Funktioniert // aber nicht, weil in
	 * transferValues(boolean) ein Speicherereignis geworfen // wird ... // OLI 09.01.2009 - Und zwar, weil die
	 * EditorDescriptoren neugesetzt werden m&uuml;ssen // (siehe untern). // this.transferValues(true); / * So geht es
	 * im Moment auch nicht ... da hakt's bei den Subeditoren ... for (int t = 0, lent = this.komponenten.getTabCount();
	 * t < lent; t++) { for (int i = 0, leni = this.komponenten.getSize(t); i < leni; i++) { dedc =
	 * (DefaultEditorDjinnCell) this.komponenten.getCell(t, i); dedc.getEditorDescriptor().setObject((Attributed)
	 * this.objekt); if (dedc.getComponent() instanceof SubEditor) { System.out.println("***** SubEditor detected."); }
	 * } } / }
	 */

	/**
	 * @changed OLI 23.09.2007 - Die Methode ist, bedingt durch Erweiterung des Interfaces <TT>EditorDjinn</TT>, auf
	 *          public gesetzt worden.
	 *          <P>
	 *
	 */
	public Hashtable<String, Component> getComponentTable() {
		Hashtable<String, Component> htc = new Hashtable<String, Component>();
		for (int t = 0, tabs = this.komponenten.getTabCount(); t < tabs; t++) {
			try {
				Vector v = this.komponenten.getComponents(t);
				for (int i = 0, len = v.size(); i < len; i++) {
					Component c = (Component) v.elementAt(i);
					if (c instanceof ContextOwner) {
						htc.put(((ContextOwner) c).getContext(), c);
					}
				}
			} catch (Exception e) {
			}
		}
		return htc;
	}

	/* Implementierung des Interfaces Runnable. */

	/**
	 * @changed OLI 23.09.2008 - Habe die Strategie zum Zeichnen des Rahmens um die fokussierte Komponenten
	 *          entsch&auml;rft. Es wird nun gegebenenfalls tats&auml;chlich nur der gesetzte Rahmen entfernt und ein
	 *          neuer gesetzt.
	 *          <P>
	 *          OLI 23.04.2009 - Einbau der Ausblendung von zentralen Feldern, wenn es sich bei dem bearbeiteten Objekt
	 *          um ein zentrales Objekt in lokalem Umfeld handelt.
	 *          <P>
	 *
	 */
	public void run() {
		DefaultEditorDjinnCell dedcbefore = null;
		java.util.List<String> nonCentralFields = (this.objekt instanceof CentrallyHeldWithLocalFields
				? ((CentrallyHeldWithLocalFields) this.objekt).getNonCentralFieldNames()
				: new java.util.Vector<String>());
		while (!this.stopped) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			try {
				for (int t = 0, lent = this.komponenten.getTabCount(); t < lent; t++) {
					for (int i = 0, len = this.komponenten.getSize(t); i < len; i++) {
						DefaultEditorDjinnCell dedc = (DefaultEditorDjinnCell) this.komponenten.getCell(t, i);
						if (dedc.getPanel() != null) {
							if (dedc.getComponent().hasFocus()) {
								if (dedcbefore != dedc) {
									dedc.getPanel().setBorder(new LineBorder(Constants.DJINNBORDER, 1));
									if (dedcbefore != null) {
										dedcbefore.getPanel().setBorder(new EmptyBorder(1, 1, 1, 1));
									}
									dedcbefore = dedc;
								}
							}
						} else {
							System.out
									.println("WARNING: component has no panel to remark (" + dedc.getComponent() + ")");
						}
					}
				}
				if (this.locked) {
					if (this.buttonLoeschen != null) {
						this.buttonLoeschen.setEnabled(false);
					}
					if (this.buttonSpeichern != null) {
						this.buttonSpeichern.setEnabled(false);
					}
					if (this.buttonUebernehmen != null) {
						this.buttonUebernehmen.setEnabled(false);
					}
				} else if (this.centralObject) {
					if (this.buttonLoeschen != null) {
						this.buttonLoeschen.setEnabled(false);
					}
					if (nonCentralFields.size() == 0) {
						if (this.buttonSpeichern != null) {
							this.buttonSpeichern.setEnabled(false);
						}
						if (this.buttonUebernehmen != null) {
							this.buttonUebernehmen.setEnabled(false);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

class BatchCheckBox extends COCheckBox {

	private JComponent comp = null;
	private EditorDescriptor ed = null;

	public BatchCheckBox(EditorDescriptor ed, JComponent comp, String c) {
		super(c);
		this.comp = comp;
		this.ed = ed;
	}

	public int getAttributeId() {
		return this.ed.getAttributeId();
	}

	public Object getComponentValue() {
		return ed.getComponentFactory().getValue(ed, comp);
		/*
		 * if (comp instanceof JCheckBox) { return ((JCheckBox) comp).isSelected(); } else if (comp instanceof
		 * JComboBox) { return ((JComboBox) comp).getSelectedItem(); } else if (comp instanceof JTextField) { return
		 * ((JTextField) comp).getText(); } else if (comp instanceof MassiveListSelector) { return
		 * ((MassiveListSelector) comp).getSelected(); } else if (comp instanceof TimestampField) { return
		 * ((TimestampField) comp).getTimestamp(); } return null;
		 */
	}

}
