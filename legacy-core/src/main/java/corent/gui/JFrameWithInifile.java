/*
 * JFrameWithInifile.java
 *
 * 1999
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;

import corent.base.Constants;
import corent.files.Inifile;

/**
 * Diese Klasse erweitert die Funktionalit&auml;t eines JFrames um die
 * F&auml;higkeit, seine Daten in eine Ini-Datei abzuspeichern.
 *
 * @author O.Lieshoff
 *
 */

public class JFrameWithInifile extends JFrame implements ComponentWithInifile, ContextOwner {

	/**
	 * Wird diese Konstante auf true gesetzt, zentriert die Klasse Fenster ohne
	 * Eintr&auml;ge.
	 */
	public static boolean CENTER_NEW = true;

	/**
	 * Referenz auf die Ini-Datei, in die die Fensterdaten gespeichert werden sollen
	 * <I>(Default null)</I>.
	 */
	protected Inifile ini = null;
	/**
	 * Der Name, unter dem der Dialog seine Daten aus der Inidatei bezieht
	 * <I>(Default null</I>.
	 */
	protected String identifier = null;

	/**
	 * Ist diese Flagge gesetzt, so wird das Fenster ohne R&uuml;cksicht auf
	 * Verluste aus den Angaben der Ini-Datei restauriert
	 * <I>(Constants.BRUTERESTORE) </I>.
	 */
	private boolean bruteRestore = Constants.BRUTERESTORE;
	/**
	 * Ist diese Flagge gesetzt, so werden nur die Koordinaten, nicht aber die
	 * Ausdehnungsdaten aus der Inidatei rekonstruiert <I>(Default false)</I>.
	 */
	private boolean coordinatesOnly = false;
	/* Flagge f&uuml;r die harte Abblendung. */
	private boolean strongdisabled = false;
	/* Der Context zur Komponente. */
	private String context = null;

	/**
	 * Generiert einen einfachen Frame mit dem &uuml;bergebenen Titel.
	 *
	 * @param titel Der Titel zum Frame.
	 */
	public JFrameWithInifile(String titel) {
		this(titel, null);
	}

	/**
	 * Generiert einen einfachen, titellosen Frame mit der &uuml;bergebenen
	 * IniDatei.
	 *
	 * @param ini Die Inidatei zum Frame.
	 */
	public JFrameWithInifile(Inifile ini) {
		this("", ini);
	}

	/**
	 * Generiert einen Frame mit mit Titel und Inidatei.
	 *
	 * @param titel Der Titel zum Frame.
	 * @param ini   Die Inidatei zum Frame.
	 */
	public JFrameWithInifile(String titel, Inifile ini) {
		super(titel);
		this.setInifile(ini);
	}

	@Override
	public void setEnabled(boolean b) {
		if (b && this.strongdisabled) {
			return;
		}
		super.setEnabled(b);
	}

	/**
	 * Setzt die &uuml;bergebene IniDatei als neue IniDatei f&uuml;r die Komponente
	 * ein.
	 *
	 * @param ini Die neue IniDatei.
	 */
	@Override
	public void setInifile(Inifile ini) {
		this.ini = ini;
	}

	/** @return Eine Referenz auf die IniDatei der Komponente. */
	@Override
	public Inifile getInifile() {
		return this.ini;
	}

	/**
	 * Setzt den &uuml;bergebenen Wert als neuen Namen, unter dem die Daten der
	 * Komponente in der IniDatei gespeichert werden.
	 *
	 * @param identifier Der neue Name, unter dem die Daten der Komponente in der
	 *                   IniDatei gespeichert werden sollen.
	 */
	@Override
	public void setIdentifier(String identifier) {
		if (identifier == null) {
			this.identifier = null;
		} else {
			this.identifier = new String(identifier);
		}
	}

	/**
	 * @return Der Name, unter dem die Daten der Komponente in der IniDatei
	 *         gespeichert werden. Ist kein expliziter Name gesetzt, wird der
	 *         Klassenname der Componente zur&uuml;ckgeliefert.
	 */
	@Override
	public String getIdentifier() {
		if (this.identifier == null) {
			return this.getClass().getName();
		}
		return new String(this.identifier);
	}

	/**
	 * Wird diese Flagge im Rahmen der gesetzten Option "CoordinatesOnly" gesetzt,
	 * so wird das Fenster anhand der in der Ini-Datei gespeicherten Daten
	 * wiederhergestellt. Andernfalls wird eine bevorzugte Minimalgr&oum;&szlig;e
	 * gew&auml;hlt, falls das Fenster zu klein ist, um alle Inhalte anzuzeigen.
	 *
	 * @param b Wird hier der Wert <TT>true</TT> &uuml;bergeben, wird das Fenster
	 *          ohne R&uuml;cksicht auf Verluste wiederhergestellt.
	 */
	@Override
	public void setBruteRestore(boolean b) {
		this.bruteRestore = b;
	}

	/**
	 * @return <TT>true</TT>, wenn das Fenster vollst&auml;ndig aus den Daten der
	 *         Ini-Datei wiederhergestellt werden soll,<BR>
	 *         <TT>false</TT> sonst.
	 */
	@Override
	public boolean isBruteRestore() {
		return this.bruteRestore;
	}

	/**
	 * Mit Hilfe dieser Methode kann entschieden werden, ob die Komponente nur
	 * &uuml;ber ihre Koordinaten restauriert werden soll (die Ausdehnung wird durch
	 * ein Aufruf der <TT>pack()
	 * </TT>-Methode realisiert), oder komplett anhand der Daten der IniDatei.
	 *
	 * @param b Wird dieser Parameter mit dem Wert <TT>true</TT> &uuml;bergeben, so
	 *          werden die Koordinaten aus der IniDatei gelesen. Die Ausdehnung wird
	 *          automatisch geregelt. Andernfalls werden die kompletten Fensterdaten
	 *          &uuml;bernommen.
	 */
	@Override
	public void setCoordinatesOnly(boolean b) {
		this.coordinatesOnly = b;
	}

	/**
	 * @return <TT>true</TT>, wenn nur die Koordinaten aus der Datei restauriert
	 *         werden,<BR>
	 *         <TT>false</TT> sonst.
	 */
	@Override
	public boolean isCoordinatesOnly() {
		return this.coordinatesOnly;
	}

	/**
	 * Zeigt bzw. versteckt die Komponente.
	 *
	 * @param b Wird dieser Parameter mit dem Wert <TT>true</TT> &uuml;bergeben, so
	 *          wird die Komponente zur Anzeige gebracht, andernfalls wird sie
	 *          versteckt.
	 */
	@Override
	public void setVisible(boolean b) {
		if (b) {
			if (this.ini != null) {
				int h = 0;
				int w = 0;
				Rectangle r = null;
				this.pack();
				r = new Rectangle(this.getBounds());
				r.x = this.ini.readInt(this.getIdentifier(), "X", r.x);
				r.y = this.ini.readInt(this.getIdentifier(), "Y", r.y);
				if (!this.isCoordinatesOnly()) {
					if (this.isBruteRestore()) {
						r.width = this.ini.readInt(this.getIdentifier(), "Width", r.width);
						r.height = this.ini.readInt(this.getIdentifier(), "Height", r.height);
					} else {
						w = r.width;
						h = r.height;
						r.width = this.ini.readInt(this.getIdentifier(), "Width", r.width);
						r.height = this.ini.readInt(this.getIdentifier(), "Height", r.height);
						if (w > r.width) {
							r.width = w;
						}
						if (h > r.height) {
							r.height = h;
						}
					}
				}
				this.setBounds(r);
			} else {
				this.pack();
				if (CENTER_NEW) {
					Rectangle r = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
					this.setBounds((r.width / 2) - (this.getWidth() / 2) + r.x,
							(r.height / 2) - (this.getHeight() / 2) + r.y, this.getWidth(), this.getHeight());
				}
			}
		} else {
			if (this.ini != null) {
				Rectangle r = this.getBounds();
				try {
					this.ini.writeInt(this.getIdentifier(), "X", r.x);
					this.ini.writeInt(this.getIdentifier(), "Y", r.y);
					this.ini.writeInt(this.getIdentifier(), "Width", r.width);
					this.ini.writeInt(this.getIdentifier(), "Height", r.height);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		super.setVisible(b);
	}

	/* Implementierung des Interfaces ContextOwner. */

	@Override
	public String getContext() {
		if (this.context != null) {
			return this.context;
		}
		return this.getIdentifier();
	}

	@Override
	public boolean isStrongDisabled() {
		return this.strongdisabled;
	}

	@Override
	public void setContext(String c) {
		this.context = c;
	}

	@Override
	public synchronized void setStrongDisabled(boolean b) {
		this.setEnabled(false);
		this.strongdisabled = b;
	}

}