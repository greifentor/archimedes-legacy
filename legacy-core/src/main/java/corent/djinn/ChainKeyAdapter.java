/*
 * ChainKeyAdapter.java
 *
 * 17.01.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.djinn;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * Diese Erweiterung des KeyAdapters kommt z. B. mit der CreateButtonRow-Methode des KeyListenerDjinns zum Einsatz. Er
 * erm&ouml;glicht die Speicherung eines Vorg&auml;ngers und eines Nachfolgers zu einer Komponente und die Navigation
 * zwischen den Komponenten z. B. mit Hilfe der Cursortasten.
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
 * <TD>corent.djinn.ChainKeyAdapter.on.focus.doClick</TD>
 * <TD>false</TD>
 * <TD>boolean</TD>
 * <TD>Wenn diese Flagge gesetzt ist, wird bei Bet&auml;tigung der F12- bzw. der Escape- taste (oder deren konfigurierte
 * Ersatztasten) ein Ausf&uuml;hrung der doClick-Methode der &Uuml;bernehmen- bzw. Verwerfen-Komponente generiert, falls
 * diese ein JButton oder die Instanz einer davon abgeleiteten Klasse ist.</TD>
 * </TR>
 * </TABLE>
 * <P>
 * &nbsp;
 *
 * @author
 *         <P>
 *         O.Lieshoff
 *
 * @changed
 *          <P>
 *          OLI 10.08.2008 - Erweiterung um die M&ouml;glichkeit per Konfiguration &uuml;ber die Property
 *          <I>corent.djinn.ChainKeyAdapter.on.focus.doClick</I> erreicht werden.
 *          <P>
 *          OLI 12.08.2008 - Debugging: Die Initialisierung der alternativen "Weiter"-Taste mit <TT>VK_UNDEFINED</TT>,
 *          f&uuml;hrte dazu, da&szlig; auch Umlaute als "Weiter"-Taste interpretiert worden sind.
 *
 */

public class ChainKeyAdapter extends KeyAdapter {

	private int vkUebernehmen = KeyEvent.VK_F12;
	private int vkVerwerfen = KeyEvent.VK_ESCAPE;
	private int vkWeiter = KeyEvent.VK_RIGHT;
	private int vkWeiterAlt = KeyEvent.VK_ENTER;
	private int vkZurueck = KeyEvent.VK_LEFT;
	private JComponent komponente = null;
	private JComponent nachfolger = null;
	private JComponent uebernehmen = null;
	private JComponent verwerfen = null;
	private JComponent vorgaenger = null;

	/**
	 * Generiert einen KeyListener der bei den vorgegebenen Default-Key-Ereignissen den Eingabefokus entsprechend
	 * weiterleitet.
	 *
	 * @param vorgaenger Der Vorg&auml;nger der Komponente, f&uuml;r die der Adapter erzeugt wird.
	 * @param komponente Die Komponente, f&uuml;r die der Adapter erzeugt wird.
	 * @param nachfolger Der Nachfolger der Komponente, f&uuml;r die der Adapter erzeugt wird.
	 * @param vkZurueck  Ein KeyEvent-Code zum Zur&uuml;ckschalten des Fokus.
	 * @param vkWeiter   Ein KeyEvent-Code zum Weiterschalten des Fokus.
	 *
	 * @changed
	 *          <P>
	 *          OLI 12.08.2008 - Debugging: Die Initialisierung der alternativen "Weiter"-Taste mit
	 *          <TT>VK_UNDEFINED</TT>, f&uuml;hrte dazu, da&szlig; auch Umlaute als "Weiter"-Taste interpretiert worden
	 *          sind. Daher in <TT>VK_ENTER</TT> uminitialisiert ;o) Des&auml;hnlichen mit F12 als Initialisierung
	 *          f&uuml;r die &Uuml;bernahme.
	 *
	 */
	ChainKeyAdapter(JComponent vorgaenger, JComponent komponente, JComponent nachfolger, int vkZurueck, int vkWeiter) {
		this(vorgaenger, komponente, nachfolger, null, null, vkZurueck, vkWeiter, KeyEvent.VK_ENTER, KeyEvent.VK_F12);
	}

	/**
	 * Generiert einen KeyListener der bei den vorgegebenen Default-Key-Ereignissen den Eingabefokus entsprechend
	 * weiterleitet.
	 *
	 * @param vorgaenger    Der Vorg&auml;nger der Komponente, f&uuml;r die der Adapter erzeugt wird.
	 * @param komponente    Die Komponente, f&uuml;r die der Adapter erzeugt wird.
	 * @param nachfolger    Der Nachfolger der Komponente, f&uuml;r die der Adapter erzeugt wird.
	 * @param uebernehmen   Die Komponente, durch deren Aktivierung die Daten des Fensters &uuml;bernommen werden
	 *                      k&ouml;nnen.
	 * @param verwerfen     Die Komponente, durch deren Aktivierung die Daten des Fensters verworfen werden k&ouml;nnen.
	 * @param vkZurueck     Ein KeyEvent-Code zum Zur&uuml;ckschalten des Fokus.
	 * @param vkWeiter      Ein KeyEvent-Code zum Weiterschalten des Fokus.
	 * @param vkUebernehmen Ein KeyEvent-Code zum Fokussieren der &Uuml;bernehmen-Komponente.
	 * @param vkVerwerfen   Ein KeyEvent-Code zum Fokussieren der Verwerfen-Komponente.
	 *
	 * @changed
	 *          <P>
	 *          OLI 12.08.2008 - Debugging: Die Initialisierung der alternativen "Weiter"-Taste mit
	 *          <TT>VK_UNDEFINED</TT>, f&uuml;hrte dazu, da&szlig; auch Umlaute als "Weiter"-Taste interpretiert worden
	 *          sind. Daher in <TT>VK_ENTER</TT> uminitialisiert ;o)
	 *
	 */
	private ChainKeyAdapter(
			JComponent vorgaenger,
			JComponent komponente,
			JComponent nachfolger,
			JComponent uebernehmen,
			JComponent verwerfen,
			int vkZurueck,
			int vkWeiter,
			int vkUebernehmen,
			int vkVerwerfen) {
		this(
				vorgaenger,
				komponente,
				nachfolger,
				uebernehmen,
				verwerfen,
				vkZurueck,
				vkWeiter,
				KeyEvent.VK_ENTER,
				vkUebernehmen,
				vkVerwerfen);
	}

	/**
	 * Generiert einen KeyListener der bei den vorgegebenen Default-Key-Ereignissen den Eingabefokus entsprechend
	 * weiterleitet.
	 *
	 * @param vorgaenger    Der Vorg&auml;nger der Komponente, f&uuml;r die der Adapter erzeugt wird.
	 * @param komponente    Die Komponente, f&uuml;r die der Adapter erzeugt wird.
	 * @param nachfolger    Der Nachfolger der Komponente, f&uuml;r die der Adapter erzeugt wird.
	 * @param uebernehmen   Die Komponente, durch deren Aktivierung die Daten des Fensters &uuml;bernommen werden
	 *                      k&ouml;nnen.
	 * @param verwerfen     Die Komponente, durch deren Aktivierung die Daten des Fensters verworfen werden k&ouml;nnen.
	 * @param vkZurueck     Ein KeyEvent-Code zum Zur&uuml;ckschalten des Fokus.
	 * @param vkWeiter      Ein KeyEvent-Code zum Weiterschalten des Fokus.
	 * @param vkWeiterAlt   Ein alternativer KeyEvent-Code zum Weiterschalten des Fokus.
	 * @param vkUebernehmen Ein KeyEvent-Code zum Fokussieren der &Uuml;bernehmen-Komponente.
	 * @param vkVerwerfen   Ein KeyEvent-Code zum Fokussieren der Verwerfen-Komponente.
	 */
	ChainKeyAdapter(
			JComponent vorgaenger,
			JComponent komponente,
			JComponent nachfolger,
			JComponent uebernehmen,
			JComponent verwerfen,
			int vkZurueck,
			int vkWeiter,
			int vkWeiterAlt,
			int vkUebernehmen,
			int vkVerwerfen) {
		super();
		this.komponente = komponente;
		this.nachfolger = nachfolger;
		this.uebernehmen = uebernehmen;
		this.verwerfen = verwerfen;
		this.vorgaenger = vorgaenger;
		this.vkUebernehmen = vkUebernehmen;
		this.vkVerwerfen = vkVerwerfen;
		this.vkWeiter = vkWeiter;
		this.vkWeiterAlt = vkWeiterAlt;
		this.vkZurueck = vkZurueck;
	}

	/* Ueberschreiben der Methoden der Superklasse. */

	/**
	 * @changed
	 *          <P>
	 *          OLI 10.08.2008 - Erweiterung um die Konfiguration f&uuml;r das Ausl&ouml;sen von ActionEvents statt
	 *          Focus-Request bei Buttons (f&uuml;r die Ereignisse vkUebernehmen und vkVerwerfen). Dieses Verhalten kann
	 *          &uuml;ber das Setzen der Property "corent.djinn.ChainKeyAdapter.on.focus.doClick" erreicht werden.
	 *
	 */
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (this.komponente != null)
				&& (this.komponente instanceof JButton)) {
			((JButton) this.komponente).doClick();
		} else if (((e.getKeyCode() == this.vkWeiter) || (e.getKeyCode() == this.vkWeiterAlt))
				&& (this.nachfolger != null)) {
			this.nachfolger.requestFocus();
		} else if ((e.getKeyCode() == this.vkZurueck) && (this.vorgaenger != null)) {
			this.vorgaenger.requestFocus();
		} else if ((e.getKeyCode() == this.vkUebernehmen) && (this.uebernehmen != null)) {
			if ((this.uebernehmen instanceof JButton)
					&& Boolean.getBoolean("corent.djinn.ChainKeyAdapter.on.focus.doClick")) {
				((JButton) this.uebernehmen).doClick();
			} else {
				this.uebernehmen.requestFocus();
			}
		} else if ((e.getKeyCode() == this.vkVerwerfen) && (this.verwerfen != null)) {
			if ((this.verwerfen instanceof JButton)
					&& Boolean.getBoolean("corent.djinn.ChainKeyAdapter.on.focus.doClick")) {
				((JButton) this.verwerfen).doClick();
			} else {
				this.verwerfen.requestFocus();
			}
		}
	}

}
