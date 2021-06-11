/*
 * CorePrintJob.java
 *
 * 12.08.2003
 *
 * (c) by O.Lieshoff
 *
 */

package corent.print;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.JobAttributes;
import java.awt.PageAttributes;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ImageIcon;

import corent.base.ColorUtil;
import corent.base.StrUtil;
import logging.Logger;

/**
 * Mit Hilfe dieser Klasse k&ouml;nnen Druckauftr&auml;ge erzeugt, konfiguriert und ausgef&uuml;hrt werden. Sie bietet
 * zudem die M&ouml;glichkeit Druckformulare aus beschreibenden Dateien zu &uuml;bernehmen und mit &uuml;bergebenen
 * Daten zu f&uuml;llen.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public class CorePrintJob implements Previewable {

	private static final Logger log = Logger.getLogger(CorePrintJob.class);

	/** Bezeichner f&uuml;r den unteren Blattrand. */
	protected static final int BOTTOM = 0;
	/** Bezeichner f&uuml;r den linken Blattrand. */
	protected static final int LEFT = 1;
	/** Bezeichner f&uuml;r den rechten Blattrand. */
	protected static final int RIGHT = 2;
	/** Bezeichner f&uuml;r den oberen Blattrand. */
	protected static final int TOP = 3;
	/** Bezeichner f&uuml;r die Zentrierungsfunktionen. */
	protected static final int CENTER = 4;
	/** Modus f&uuml;r druckernahen Ausdruck. */
	protected static final int NATIVE = 0;
	/** Modus f&uuml;r graphischen Ausdruck. */
	protected static final int IMAGED = 1;

	/** Diese Flagge gibt Auskunft dar&uuml;ber, ob der PrintJob aktiv ist. */
	protected boolean aktiv = false;
	/** Dimensionen der Seite <I>(Default null)</I>. */
	protected Dimension dimPage = null;
	/** X-Koordinate der letzten grafischen Operation <I>(Default 0.0d)</I>. */
	protected double lastx = 0.0d;
	/** Y-Koordinate der letzten grafischen Operation <I>(Default 0.0d)</I>. */
	protected double lasty = 0.0d;
	/**
	 * Die Abst&auml;nde zu den Seitenr&auml;ndern in dots <I>(Default double[] {0.0d, 0.0d, 0.0d, 0.0d})</I>.
	 */
	protected double[] margins = new double[] { 0.0d, 0.0d, 0.0d, 0.0d };
	/** Liste der PrintJobMacros des Jobs <I>(Default new Hashtable())</I>. */
	protected Hashtable macros = new Hashtable();
	/**
	 * Referenz die Parametertabelle zum auszudruckenden Objekt <I>(Default null)</I>.
	 */
	protected Hashtable params = new Hashtable();
	/** Die horizontale Ausrichtung von Figuren <I>(Default LEFT)</I>. */
	protected int align = LEFT;
	/** Die dots per inch <I>(Default 72)</I>. */
	protected int dpi = 72;
	/** Die augenblicklich bearbeitete Druckseite <I>(Default 0)</I>. */
	protected int hpage = 0;
	/** Druckmodus <I>(Default NATIVE)</I>. */
	protected int mode = NATIVE;
	/** Die vertikale Ausrichtung von Figuren <I>(Default LEFT)</I>. */
	protected int valign = TOP;
	/** Referenz auf die JobAttributes des Druckauftrages <I>(Default null)</I>. */
	protected JobAttributes ja = null;
	/** Speicher f&uuml;r Stackoperationen <I>(Default new Object[999])</I>. */
	protected Object[] mem = new Object[999];
	/** Referenz auf die PageAttributes des Druckauftrages <I>(Default null)</I>. */
	protected PageAttributes pa = null;
	/**
	 * Die PreviewComponent, in der der Prinjob gegebenenfalls dargstellt werden soll <I>( Default) </I>.
	 */
	protected PreviewComponent pc = null;
	/**
	 * Referenz auf den systemeigenen PrintJob, &uuml;ber den das Objekt seine Ausgabe tut <I> (Default null)</I>.
	 */
	protected PrintJob job = null;
	/**
	 * Programmstack f&uuml;r die Scriptausf&uuml;hrung <I>(Default new Stack())</I>.
	 */
	protected Stack programmstack = new Stack();
	/**
	 * Referenz auf das Script, auf dessen Grundlage gemalt wird <I>(Default null)</I>.
	 */
	protected Vector script = null;

	/**
	 * Ist diese Flagge gesetzt werden die Zeichenanweisungen tats&auml;chlich ausgef&uuml;hrt, andernfalls werden
	 * lediglich die dazu notwendigen Aktionen simuliert <I>(Default true)</I>.
	 */
	private boolean drawMode = true;

	/**
	 * Generiert einen PrintJob anhand der &uuml;bergebenen Parameter.
	 *
	 * @param ja Ein JobAttributes-Objekt mit den Voreinstellungen zum Druckjob.
	 * @param pa Ein PageAttributes-Objekt mit den Voreinstellungen zum Papier des Druckjobs.
	 */
	public CorePrintJob(JobAttributes ja, PageAttributes pa) {
		super();
		this.ja = ja;
		this.pa = pa;
		this.ja.setDialog(JobAttributes.DialogType.NONE);
		// this.job = this.getPrintJob();
	}

	/**
	 * Generiert einen PrintJob anhand der &uuml;bergebenen Parameter.
	 *
	 * @param ja Ein JobAttributes-Objekt mit den Voreinstellungen zum Druckjob.
	 * @param pa Ein PageAttributes-Objekt mit den Voreinstellungen zum Papier des Druckjobs.
	 * @param fn Dateiname, der Datei, aus der das Script gelesen werden soll.
	 * @throws IOException falls ein Fehler bei der Arbeit mit dem Dateisystem auftritt.
	 */
	public CorePrintJob(JobAttributes ja, PageAttributes pa, String fn) throws IOException {
		this(ja, pa);
		this.configure(this.getScript(fn));
	}

	/**
	 * Generiert einen PrintJob anhand der &uuml;bergebenen Parameter.
	 *
	 * @param ja     Ein JobAttributes-Objekt mit den Voreinstellungen zum Druckjob.
	 * @param pa     Ein PageAttributes-Objekt mit den Voreinstellungen zum Papier des Druckjobs.
	 * @param fn     Dateiname, der Datei, aus der das Script gelesen werden soll.
	 * @param params Eine Tabelle mit Parametern zum auszudruckenden Objekt.
	 * @throws IOException falls ein Fehler bei der Arbeit mit dem Dateisystem auftritt.
	 */
	public CorePrintJob(JobAttributes ja, PageAttributes pa, String fn, Hashtable params) throws IOException {
		this(ja, pa);
		this.params = params;
		this.configure(this.getScript(fn));
	}

	/**
	 * Generiert einen PrintJob anhand der &uuml;bergebenen Parameter.
	 *
	 * @param ja     Ein JobAttributes-Objekt mit den Voreinstellungen zum Druckjob.
	 * @param pa     Ein PageAttributes-Objekt mit den Voreinstellungen zum Papier des Druckjobs.
	 * @param script Das Druck-Script in Vector-Form.
	 * @param params Eine Tabelle mit Parametern zum auszudruckenden Objekt.
	 */
	public CorePrintJob(JobAttributes ja, PageAttributes pa, Vector script, Hashtable params) {
		this(ja, pa);
		this.params = params;
		this.configure(script);
	}

	/** Holt einen systemeigenen Printjob. */
	protected PrintJob getPrintJob() {
		String jn = new String("Java-Druckauftrag");
		return Toolkit.getDefaultToolkit().getPrintJob(new Frame(), jn, this.ja, this.pa);
	}

	/** @return Die dots f&uuml;r die angegebene Strecke in cm. */
	public int dots(double cm) {
		return (int) (this.dpcm() * cm);
	}

	/** @return Die dots per cm. */
	private int dpcm() {
		return (int) (dpi / 2.54);
	}

	/** @return Aufl&ouml;sung des Druckers. */
	public int getPrinterResolution() {
		return this.getPrinterResolution(0);
	}

	/**
	 * @param dir 0 = horizontal, 1 = vertikal.
	 * @return Aufl&ouml;sung des Druckers.
	 */
	public int getPrinterResolution(int dir) {
		if ((dir < 0) || (dir > 1)) {
			dir = 0;
		}
		int dpi = this.pa.getPrinterResolution()[dir];
		if (this.pa.getPrinterResolution()[2] == 4) {
			dpi = (int) (dpi * 2.54);
		}
		return dpi;
	}

	/** @return Die dots zum Rand zur angegebenen Richtung. */
	protected int margin(int d) {
		if ((d >= BOTTOM) && (d <= TOP)) {
			return (int) (this.margins[d] * this.dpcm());
		}
		return 0;
	}

	/**
	 * @return <TT>true</TT>, wenn die Koordinaten auf den Mittelpunkt der zu zeichnenden Figur gelegt werden sollen.
	 */
	public boolean isCentered() {
		return ((this.align == CENTER) && (this.valign == CENTER));
	}

	/** @return Die horizontale Ausrichtung f&uuml;r Figuren. */
	public int getAlign() {
		return this.align;
	}

	/** @return Die vertikale Ausrichtung f&uuml;r Figuren. */
	public int getVAlign() {
		return this.valign;
	}

	/**
	 * Setzt die horizontale Ausrichtung f&uuml;r Figuren.
	 *
	 * @param align Einer der Bezeichner LEFT, RIGHT oder CENTER.
	 */
	public void setAlign(int align) {
		switch (align) {
		case CENTER:
		case LEFT:
		case RIGHT:
			this.align = align;
		}
	}

	/**
	 * Setzt die vertikale Ausrichtung f&uuml;r Figuren.
	 *
	 * @param valign Einer der Bezeichner TOP, BOTTOM oder CENTER.
	 */
	public void setVAlign(int valign) {
		switch (valign) {
		case BOTTOM:
		case CENTER:
		case TOP:
			this.valign = valign;
		}
	}

	/**
	 * Zeichnet ein Bild an die angegebenen Koordinaten.
	 *
	 * @param g     Der Grafikkontext, auf den gemalt werden soll.
	 * @param image Das zu zeichnende Image(Icon),
	 * @param x     Die X-Koordinate.
	 * @param y     Die Y-Koordinate.
	 */
	public void drawImage(Graphics g, ImageIcon image, double x, double y) {
		int ix = (int) (this.dpcm() * x);
		int iy = (int) (this.dpcm() * y);
		int ih = (int) ((double) image.getImage().getHeight(image.getImageObserver()) * ((double) this.dpi)
				/ this.getPrinterResolution(1));
		int iw = (int) ((double) image.getImage().getWidth(image.getImageObserver()) * ((double) this.dpi)
				/ this.getPrinterResolution());
		if (this.getAlign() == CENTER) {
			ix = ix - (iw / 2);
		} else if (this.getAlign() == RIGHT) {
			ix = ix - iw;
		}
		if (this.getVAlign() == CENTER) {
			iy = iy - (ih / 2);
		} else if (this.getVAlign() == BOTTOM) {
			iy = iy - ih;
		}
		if (this.isDrawing()) {
			g.drawImage(image.getImage(), ix, iy, iw, ih, image.getImageObserver());
		}
		this.lastx = x + ((double) iw / (double) this.dpcm());
		this.lasty = y + ((double) ih / (double) this.dpcm());
	}

	/**
	 * Zeichnet eine Linie an die angegebenen Koordinaten.
	 *
	 * @param g  Der Grafikkontext, auf den gemalt werden soll.
	 * @param x  Die X-Koordinate des Startpunktes.
	 * @param y  Die Y-Koordinate des Startpunktes.
	 * @param x0 Die X-Koordinate des Endpunktes.
	 * @param y0 Die Y-Koordinate des Endpunktes.
	 */
	public void drawLine(Graphics g, double x, double y, double x0, double y0) {
		int ix = (int) (this.dpcm() * x);
		int iy = (int) (this.dpcm() * y);
		int ix0 = (int) (this.dpcm() * x0);
		int iy0 = (int) (this.dpcm() * y0);
		if (this.isDrawing()) {
			g.drawLine(ix, iy, ix0, iy0);
		}
		this.lastx = x0;
		this.lasty = y0;
	}

	/**
	 * Zeichnet ein Rechteck an die angegebenen Koordinaten.
	 *
	 * @param g Der Grafikkontext, auf den gemalt werden soll.
	 * @param x Die X-Koordinate der rechten oberen Ecke des Rechtecks.
	 * @param y Die Y-Koordinate der rechten oberen Ecke des Rechtecks.
	 * @param w Die Breite des Rechtecks.
	 * @param h Die H&ouml;he des Rechtecks.
	 */
	public void drawRect(Graphics g, double x, double y, double w, double h) {
		int ix = (int) (this.dpcm() * x);
		int iy = (int) (this.dpcm() * y);
		int iw = (int) (this.dpcm() * w);
		int ih = (int) (this.dpcm() * h);
		if (this.getAlign() == CENTER) {
			ix = ix - (int) (w / 2);
		} else if (this.getAlign() == RIGHT) {
			ix = ix - (int) (w);
		}
		if (this.getVAlign() == CENTER) {
			iy = iy - (int) (h / 2);
		} else if (this.getVAlign() == BOTTOM) {
			iy = iy - (int) (h);
		}
		if (this.isDrawing()) {
			g.drawRect(ix, iy, iw, ih);
		}
		this.lastx = x + ((double) iw / (double) this.dpcm());
		this.lasty = y + ((double) ih / (double) this.dpcm());
	}

	/**
	 * Zeichnet einen String an die angegebenen Koordinaten.
	 *
	 * @param g Der Grafikkontext, auf den gemalt werden soll.
	 * @param s Der zu malende String.
	 * @param x Die X-Koordinate der rechten oberen Ecke des Rechtecks.
	 * @param y Die Y-Koordinate der rechten oberen Ecke des Rechtecks.
	 */
	public void drawString(Graphics g, String s, double x, double y) {
		int ix = (int) (this.dpcm() * x);
		int iy = (int) (this.dpcm() * y);
		int iw = g.getFontMetrics().charsWidth(s.toCharArray(), 0, s.length());
		int ih = g.getFontMetrics().getHeight();
		if (this.getAlign() == CENTER) {
			ix = ix - (iw / 2);
		} else if (this.getAlign() == RIGHT) {
			ix = ix - iw;
		}
		if (this.getVAlign() == CENTER) {
			iy = iy - (ih / 2) + g.getFontMetrics().getMaxAscent();
		} else if (this.getVAlign() == BOTTOM) {
			iy = iy - ih + g.getFontMetrics().getMaxAscent();
		}
		if (this.isDrawing()) {
			g.drawString(s, ix, iy);
		}
		this.lastx = x + ((double) iw / (double) this.dpcm());
		this.lasty = y + ((double) ih / (double) this.dpcm());
	}

	/**
	 * Schreibt den angegebenen String an die &uuml;bergebenen Koordinaten. Ist der String l&auml;nger als die
	 * angegebene Maximall&auml;nge, so werden die &uuml;berz&auml;hligen Zeichen abgetrennt und drei Punkte zur
	 * Kennzeichnung angebracht.
	 *
	 * @param g Der Graphics-KOntext, auf den geschrieben werden soll.
	 * @param s Der zu schreibende String.
	 * @param x Die X-Koordinate der Ausgabe in cm.
	 * @param y Die Y-Koordinate der Ausgabe in cm.
	 * @param w Die maximale Breite der Ausgabe in cm.
	 */
	public void drawString(Graphics g, String s, double x, double y, double w) {
		int len = g.getFontMetrics().charsWidth(s.toCharArray(), 0, s.length());
		int il = this.dots(w);
		String h = null;
		while ((len > il) && !s.equals("")) {
			s = s.substring(0, s.length() - 1);
			h = s.concat("...");
			len = g.getFontMetrics().charsWidth(h.toCharArray(), 0, h.length());
		}
		if (!s.equals("")) {
			if (h != null) {
				s = h;
			}
			this.drawString(g, s, x, y);
		}
	}

	/**
	 * Schreibt den angegebenen String an die &uuml;bergebenen Koordinaten. Ist der String l&auml;nger als die
	 * angegebene Maximall&auml;nge, so wird er umgebrochen und in der n&auml;chsten Zeile weitergef&uuml;hrt. Reicht
	 * die dazu angegebene Maximalh&ouml;he nicht aus, so wird der String entsprechend gek&uml;rzt und um drei Punkte
	 * zur Kennzeichnung erweitert.
	 *
	 * @param g  Der Graphics-KOntext, auf den geschrieben werden soll.
	 * @param s  Der zu schreibende String.
	 * @param x  Die X-Koordinate der Ausgabe in cm.
	 * @param y  Die Y-Koordinate der Ausgabe in cm.
	 * @param w  Die maximale Breite der Ausgabe in cm.
	 * @param h  Die maximale H&ouml;he der Ausgabe in cm.
	 * @param za Der Zeilenabstand (1.5 f&uuml;r anderthalb etc.).
	 */
	public void drawString(Graphics g, String s, double x, double y, double w, double h, double za) {
		int islashn = -1;
		int len = 0;
		String s0 = null;
		String[] sarr = null;
		Vector svec = new Vector();
		islashn = s.indexOf("\n");
		while (islashn >= 0) {
			s0 = s.substring(0, islashn);
			s = s.substring(islashn + 1, s.length());
			svec.addElement(s0);
			islashn = s.indexOf("\n");
		}
		svec.addElement(s);
		len = svec.size();
		sarr = new String[len];
		for (int i = 0; i < len; i++) {
			sarr[i] = (String) svec.elementAt(i);
		}
		this.drawString(g, sarr, x, y, w, h, za);
	}

	/**
	 * Schreibt den angegebenen String an die &uuml;bergebenen Koordinaten. Ist der String l&auml;nger als die
	 * angegebene Maximall&auml;nge, so wird er umgebrochen und in der n&auml;chsten Zeile weitergef&uuml;hrt. Reicht
	 * die dazu angegebene Maximalh&ouml;he nicht aus, so wird der String entsprechend gek&uml;rzt und um drei Punkte
	 * zur Kennzeichnung erweitert.
	 *
	 * @param g  Der Graphics-KOntext, auf den geschrieben werden soll.
	 * @param s  Die Zeilen des zu schreibenden Textes.
	 * @param x  Die X-Koordinate der Ausgabe in cm.
	 * @param y  Die Y-Koordinate der Ausgabe in cm.
	 * @param w  Die maximale Breite der Ausgabe in cm.
	 * @param h  Die maximale H&ouml;he der Ausgabe in cm.
	 * @param za Der Zeilenabstand (1.5 f&uuml;r anderthalb etc.).
	 */
	public void drawString(Graphics g, String[] s, double x, double y, double w, double h, double za) {
		double h0 = y + h;
		double y0 = y;
		int len = 0;
		int il = this.dots(w);
		String hs = null;
		String line = new String("");
		String token = null;
		StringTokenizer st = null;
		for (int si = 0; si < s.length; si++) {
			len = g.getFontMetrics().charsWidth(s[si].toCharArray(), 0, s[si].length());
			st = new StringTokenizer(s[si], " ");
			line = new String("");
			if ((s[si].length() == 0)) {
				y0 = y;
				y = y + ((g.getFontMetrics().getHeight()) * za / (this.dpcm()));
				if ((y > h0) && (si < s.length - 1)) {
					this.drawString(g, "...", x, y0);
					return;
				}
			}
			while (st.hasMoreTokens()) {
				token = st.nextToken();
				if (!line.equals("")) {
					token = " ".concat(token);
				}
				hs = line.concat(token);
				len = g.getFontMetrics().charsWidth(hs.toCharArray(), 0, hs.length());
				if (len > il) {
					y0 = y;
					y = y + ((g.getFontMetrics().getHeight()) * za / (this.dpcm()));
					if (y > h0) {
						line = line.substring(0, line.length() - 4).concat("...");
					}
					this.drawString(g, line, x, y0);
					if (line.endsWith("...")) {
						return;
					}
					line = new String(token.trim());
					hs = line;
				} else if (!st.hasMoreTokens()) {
					this.drawString(g, hs.trim(), x, y);
				} else {
					line = line.concat(token);
				}
				if (!st.hasMoreTokens()) {
					y0 = y;
					y = y + ((g.getFontMetrics().getHeight()) * za / (this.dpcm()));
					if (y > h0) {
						hs = hs.substring(0, hs.length() - 4).concat("...");
					}
					this.drawString(g, hs, x, y0);
					if (hs.endsWith("...")) {
						return;
					}
					line = new String("");
				}
			}
		}
	}

	/**
	 * Zeichnet ein gef&uuml;lltes Oval an die angegebenen Koordinaten.
	 *
	 * @param g Der Grafikkontext, auf den gemalt werden soll.
	 * @param x Die X-Koordinate der rechten oberen Ecke des Ovals.
	 * @param y Die Y-Koordinate der rechten oberen Ecke des Ovals.
	 * @param w Die Breite des Ovals.
	 * @param h Die H&ouml;he des Ovals.
	 */
	public void fillOval(Graphics g, double x, double y, double w, double h) {
		int ix = (int) (this.dpcm() * x);
		int iy = (int) (this.dpcm() * y);
		int iw = (int) (this.dpcm() * w);
		int ih = (int) (this.dpcm() * h);
		if (this.getAlign() == CENTER) {
			ix = ix - (int) (w / 2);
		} else if (this.getAlign() == RIGHT) {
			ix = ix - (int) (w);
		}
		if (this.getVAlign() == CENTER) {
			iy = iy - (int) (h / 2);
		} else if (this.getVAlign() == BOTTOM) {
			iy = iy - (int) (h);
		}
		if (this.isDrawing()) {
			g.fillOval(ix, iy, iw, ih);
		}
		this.lastx = x + ((double) iw / (double) this.dpcm());
		this.lasty = y + ((double) ih / (double) this.dpcm());
	}

	/**
	 * Zeichnet ein Rechteck an die angegebenen Koordinaten.
	 *
	 * @param g Der Grafikkontext, auf den gemalt werden soll.
	 * @param x Die X-Koordinate der rechten oberen Ecke des Rechtecks.
	 * @param y Die Y-Koordinate der rechten oberen Ecke des Rechtecks.
	 * @param w Die Breite des Rechtecks.
	 * @param h Die H&ouml;he des Rechtecks.
	 */
	public void fillRect(Graphics g, double x, double y, double w, double h) {
		int ix = (int) (this.dpcm() * x);
		int iy = (int) (this.dpcm() * y);
		int iw = (int) (this.dpcm() * w);
		int ih = (int) (this.dpcm() * h);
		if (this.getAlign() == CENTER) {
			ix = ix - (int) (w / 2);
		} else if (this.getAlign() == RIGHT) {
			ix = ix - (int) (w);
		}
		if (this.getVAlign() == CENTER) {
			iy = iy - (int) (h / 2);
		} else if (this.getVAlign() == BOTTOM) {
			iy = iy - (int) (h);
		}
		if (this.isDrawing()) {
			g.fillRect(ix, iy, iw, ih);
		}
		this.lastx = x + ((double) iw / (double) this.dpcm());
		this.lasty = y + ((double) ih / (double) this.dpcm());
	}

	/**
	 * Zeichnet ein gef&uuml;lltes Rechteck mit abgerundeten Ecken an die angegebenen Koordinaten.
	 *
	 * @param g  Der Grafikkontext, auf den gemalt werden soll.
	 * @param x  Die X-Koordinate der rechten oberen Ecke des Rechtecks.
	 * @param y  Die Y-Koordinate der rechten oberen Ecke des Rechtecks.
	 * @param w  Die Breite des Rechtecks.
	 * @param h  Die H&ouml;he des Rechtecks.
	 * @param rw Die Breite des Rechtecks.
	 * @param rh Die H&ouml;he des Rechtecks.
	 */
	public void fillRoundRect(Graphics g, double x, double y, double w, double h, double rw, double rh) {
		int ix = (int) (this.dpcm() * x);
		int iy = (int) (this.dpcm() * y);
		int iw = (int) (this.dpcm() * w);
		int ih = (int) (this.dpcm() * h);
		int irw = (int) (this.dpcm() * rw);
		int irh = (int) (this.dpcm() * rh);
		if (this.getAlign() == CENTER) {
			ix = ix - (int) (w / 2);
		} else if (this.getAlign() == RIGHT) {
			ix = ix - (int) (w);
		}
		if (this.getVAlign() == CENTER) {
			iy = iy - (int) (h / 2);
		} else if (this.getVAlign() == BOTTOM) {
			iy = iy - (int) (h);
		}
		if (this.isDrawing()) {
			g.fillRoundRect(ix, iy, iw, ih, irw, irh);
		}
		this.lastx = x + ((double) iw / (double) this.dpcm());
		this.lasty = y + ((double) ih / (double) this.dpcm());
	}

	/**
	 * Liest und parst ein Script aus der angegebenen Datei.
	 *
	 * @param fn Der Name der Datei aus der gelesen werden soll.
	 * @return Ein Vector mit den Zeilen der Datei.
	 * @throws IOException falls ein Fehler bei der Arbeit mit dem Dateisystem auftritt.
	 */
	protected Vector getScript(String fn) throws IOException {
		Vector script = new Vector();
		try {
			FileReader fr = new FileReader(fn);
			BufferedReader reader = new BufferedReader(fr);
			String zeile = new String();
			try {
				zeile = reader.readLine();
				while (zeile != null) {
					script.addElement(zeile);
					zeile = reader.readLine();
				}
				reader.close();
				fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return script;
	}

	/**
	 * Liest und entfernt die Konfigurationsanweisungen aus dem Script und arbeitet den Rest f&uuml;r die paint-Routine
	 * auf.
	 *
	 * @param script0 Das noch unbearbeitete Script.
	 * @return <TT>true</TT>, wenn der Konfigurationsteil des Scriptes fehlerfrei abgearbeitet werden konnte,<BR>
	 *         <TT>false</TT> sonst.
	 */
	protected boolean configure(Vector script0) {
		boolean code = false;
		boolean recording = false;
		int i = 0;
		int len = 0;
		int location = -1;
		int mode = -1;
		PrintJobMacro m = null;
		Stack stack = new Stack();
		String s = null;
		this.macros = new Hashtable();
		this.script = this.parseScript(script0);
		len = this.script.size();
		for (i = 0; i < len; i++) {
			s = (String) this.script.elementAt(i);
			if (recording) {
				if (s.equals("endmacro")) {
					this.macros.put(m.getName(), m);
					recording = false;
				} else {
					m.getAnweisungen().addElement(s);
				}
			} else {
				try {
					if (s.equals("BOTTOM")) {
						stack.push(new Double(BOTTOM));
					} else if (s.equals("CENTER")) {
						stack.push(new Double(CENTER));
					} else if (s.equals("dpi")) {
						this.changeDPI(((Double) stack.pop()).intValue());
					} else if (s.equals("IMAGED")) {
						stack.push(new Double(IMAGED));
					} else if (s.equals("LEFT")) {
						stack.push(new Double(LEFT));
					} else if (s.equals("macro")) {
						m = new PrintJobMacro(stack.pop().toString());
						recording = true;
					} else if (s.equals("margin")) {
						location = ((Double) stack.pop()).intValue();
						if ((location < BOTTOM) || (location > TOP)) {
							this.printError("location unknown", s + " (" + location + ")", i);
						} else {
							margins[location] = ((Double) stack.pop()).doubleValue();
						}
					} else if (s.equals("mode")) {
						mode = ((Double) stack.pop()).intValue();
						if ((mode == IMAGED) || (mode == NATIVE)) {
							this.mode = mode;
						} else {
							this.printError("mode unknown", s + " (" + mode + ")", i);
						}
					} else if (s.equals("NATIVE")) {
						stack.push(new Double(NATIVE));
					} else if (s.equals("RIGHT")) {
						stack.push(new Double(RIGHT));
					} else if (s.equals("start")) {
						break;
					} else if (s.equals("TOP")) {
						stack.push(new Double(TOP));
					} else if (this.processConfigure(s, stack, i)) {
					} else {
						try {
							stack.push(new Double(s));
						} catch (NumberFormatException nfe) {
							stack.push(s);
						}
					}
				} catch (EmptyStackException ese) {
					this.printError("stack empty", s, i);
					code = false;
				} catch (NumberFormatException nfe) {
					this.printError("numberformat error", s, i);
				} catch (Exception e) {
					this.printError(e.getMessage(), s, i);
				}
			}
		}
		this.changeDPI(this.dpi);
		for (int j = 0; j < i; j++) {
			this.script.remove(0);
		}
		log.info("script cleaned");
		return code;
	}

	/** Aktualisiert die Objektdaten bei Wechsel der Aufl&ouml;sung. */
	protected void changeDPI(int dpi) {
		this.changeDPINoPA(dpi);
		this.pa.setPrinterResolution(dpi);
	}

	/** Aktualisiert die Objektdaten bei Wechsel der Aufl&ouml;sung. */
	protected void changeDPINoPA(int dpi) {
		this.dpi = dpi;
		double d = this.dpcm();
		PaperFormat pf = PaperFormat.GetFormat(pa.getMedia());
		pf = (pf == null ? PaperFormat.GetFormat(PageAttributes.MediaType.A4) : pf);
		this.dimPage = new Dimension((int) (d * pf.getWidth()), (int) (d * pf.getHeight()));
	}

	/**
	 * Diese Methode kann zur Erweiterung der Konfigurationsanweisungen im Script &uuml;berschrieben werden. Auf Fehler
	 * bei der abarbeitung des Scriptes mu&szlig; mit einer Exception reagiert werden.
	 * 
	 * @param cmd   Das Kommando.
	 * @param stack Der Stack, auf dem das Script arbeitet.
	 * @param i     Die Position des Kommandos im Script.
	 * @return <TT>true</TT>, wenn das Kommando erkannt worden ist,<BR>
	 *         <TT>false</TT> sonst.
	 */
	protected boolean processConfigure(String cmd, Stack stack, int i) {
		return false;
	}

	/**
	 * Druckt den Druckauftrag mit Hilfe der <TT>paint</TT>-Methode.
	 *
	 * @return <TT>true</TT>, falls der Auftrag ordnungsgem&auml;&szlig; ausgef&uuml;hrt werden konntem,<BR>
	 *         <TT>false</TT>, sonst.
	 */
	public boolean print() {
		this.pa.setPrinterResolution(this.dpi);
		try {
			this.job = this.getPrintJob();
		} catch (InternalError e) {
			return false;
		}
		if (this.job != null) {
			this.dpi = this.job.getPageResolution();
			this.dimPage = new Dimension(this.job.getPageDimension());
			Graphics g = null;
			for (int page = this.ja.getMinPage(); page <= this.ja.getMaxPage(); page++) {
				g = this.job.getGraphics();
				this.paint(g, page);
				g.dispose();
			}
			this.job.end();
			this.job.finalize();
			return true;
		}
		return false;
	}

	/**
	 * Diese Methode malt anhand des aufgearbeiteten Vector den eigentlichen Ausdruck.
	 *
	 * @param g    Der Grafik-Kontext, auf den gedruckt werden soll.
	 * @param page Die Seite, die gedruckt werden soll.
	 * @return Die Anzahl der Seiten, die von dem Druck ben&ouml;tigt werden.
	 */
	public int makePages(Graphics g, int page) {
		if (this.script != null) {
			BufferedImage img = null;
			Graphics g0 = null;
			this.hpage = 1;
			int len = 0;
			int startline = 0;
			Stack stack = new Stack();
			this.programmstack = new Stack();
			String s = null;
			this.lastx = 0.0;
			this.lasty = 0.0;
			if (this.mode == IMAGED) {
				img = new BufferedImage(this.dimPage.width, this.dimPage.height, BufferedImage.TYPE_INT_RGB);
				g0 = g;
				g = img.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, this.dimPage.width, this.dimPage.height);
			}
			g.translate(this.margin(LEFT), this.margin(TOP));
			len = this.script.size();
			log.info("processing printscript ... (" + len + " params)");
			this.setDrawing(false);
			if (page == this.hpage) {
				this.setDrawing(true);
			}
			for (int i = startline; i < len; i++) {
				s = (String) this.script.elementAt(i);
				try {
					if (s.equals("do")) {
						this.programmstack.push(new Integer(i));
					} else if (s.equals("else")) {
						int count = 1;
						i++;
						while ((i + 1 < len) && !s.equals("endif") && (count > 0)) {
							i++;
							s = (String) this.script.elementAt(i);
							if (s.equals("else")) {
								count++;
							} else if (s.equals("endif")) {
								count--;
							}
						}
					} else if (s.equals("endif")) {
					} else if (s.equals("if")) {
						double x = ((Double) stack.pop()).doubleValue();
						if ((int) x == 0) {
							int count = 1;
							while ((i + 1 < len) && !s.equals("else") && !s.equals("endif") && (count > 0)) {
								i++;
								s = (String) this.script.elementAt(i);
								if (s.equals("if")) {
									count++;
								} else if (s.equals("endif")) {
									count--;
								}
							}
						}
					} else if (s.equals("page")) {
						this.hpage++;
						if (this.hpage == page) {
							this.setDrawing(true);
						} else {
							this.setDrawing(false);
						}
					} else if (s.equals("pagenumber")) {
						stack.push(new Double(this.hpage));
					} else if (s.equals("while")) {
						double x = ((Double) stack.pop()).doubleValue();
						if ((int) x != 0) {
							i = ((Integer) this.programmstack.peek()).intValue();
						} else {
							this.programmstack.pop();
						}
					} else {
						this.execute(s, g, stack, i, page);
					}
				} catch (EmptyStackException ese) {
					this.printError("stack empty", s, i);
					return 0;
				} catch (NumberFormatException nfe) {
					this.printError("numberformat error", s, i);
					return 0;
				} catch (Exception e) {
					log.error(" --> " + s, e);
					return 0;
				}
			}
			if (this.mode == IMAGED) {
				g = g0;
				ImageIcon ic = new ImageIcon(img);
				g.drawImage(ic.getImage(), 0, 0, ic.getImageObserver());
			}
			// g.dispose();
			log.info("page" + this.hpage);
			return this.hpage;
		}
		return 0;
	}

	/**
	 * Diese Methode malt anhand des aufgearbeiteten Vector den eigentlichen Ausdruck.
	 *
	 * @param g    Der Grafik-Kontext, auf den gedruckt werden soll.
	 * @param page Die Seite, die gedruckt werden soll.
	 */
	@Override
	public void paint(Graphics g, int page) {
		synchronized (this) {
			this.aktiv = true;
		}
		this.makePages(g, page);
		synchronized (this) {
			this.aktiv = false;
		}
	}

	/** @return <TT>true</TT>, wenn tats&auml;chlich gezeichnet werden soll. */
	protected boolean isDrawing() {
		return this.drawMode;
	}

	/**
	 * @return <TT>true</TT>, wenn der PrintJob gerade mit dem drucken besch&auml;ftigt ist
	 */
	public boolean isActive() {
		return this.aktiv;
	}

	/**
	 * Setzt den Zeichenmodus auf den angegebenen Wert.
	 *
	 * @param modus <TT>true</TT>, wenn tats&auml;chlich gezeichnet werden soll.
	 */
	protected void setDrawing(boolean modus) {
		this.drawMode = modus;
	}

	/**
	 * F&uuml;hrt das &uuml;bergebene Kommando aus.
	 *
	 * @param s     Das Kommando.
	 * @param g     Das Graphics-Objekt, auf dem die Ausgabe stattfinden soll.
	 * @param stack Der Stack, von dem das Kommando seine Parameter beziehen kann.
	 * @param line  Nummer des verarbeiteten Kommandos.
	 * @param page  Die vom PrintJob angeforderte Seite.
	 */
	protected void execute(String s, Graphics g, Stack stack, int line, int page) {
		double h = 0.0d;
		double d = 0.0d;
		double rh = 0.0d;
		double rw = 0.0d;
		double w = 0.0d;
		double x = 0.0d;
		double y = 0.0d;
		int location = 0;
		if (s.equals("align")) {
			location = ((Double) stack.pop()).intValue();
			if ((location == CENTER) || (location == LEFT) || (location == RIGHT)) {
				this.setAlign(location);
			} else {
				this.printError("location unknown", s + " (" + location + ")", line);
			}
		} else if (s.equals("BOTTOM")) {
			stack.push(new Double(BOTTOM));
		} else if (s.equals("CENTER")) {
			stack.push(new Double(CENTER));
		} else if (s.equals("center")) {
			this.setAlign(CENTER);
			this.setVAlign(CENTER);
		} else if (s.equals("color")) {
			Color cl = ColorUtil.GetColor((String) stack.pop());
			g.setColor(cl);
		} else if (s.equals("corner")) {
			this.setAlign(LEFT);
			this.setVAlign(TOP);
		} else if (s.equals("drawImage")) {
			ImageIcon image = new ImageIcon((String) stack.pop());
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			this.drawImage(g, image, x, y);
		} else if (s.equals("drawLine")) {
			h = ((Double) stack.pop()).doubleValue();
			w = ((Double) stack.pop()).doubleValue();
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			this.drawLine(g, x, y, w, h);
		} else if (s.equals("drawRect")) {
			h = ((Double) stack.pop()).doubleValue();
			w = ((Double) stack.pop()).doubleValue();
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			this.drawRect(g, x, y, w, h);
		} else if (s.equals("drawString")) {
			String out = stack.pop().toString();
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			this.drawString(g, out, x, y);
		} else if (s.equals("drawStringLine")) {
			String out = (String) stack.pop();
			w = ((Double) stack.pop()).doubleValue();
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			this.drawString(g, out, x, y, w);
		} else if (s.equals("drawStringField")) {
			String out = (String) stack.pop();
			h = ((Double) stack.pop()).doubleValue();
			w = ((Double) stack.pop()).doubleValue();
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			this.drawString(g, out, x, y, w, h, 1.2);
		} else if (s.equals("drop")) {
			stack.pop();
		} else if (s.equals("dup")) {
			Object o = stack.peek();
			stack.push(o);
		} else if (s.equals("fillOval")) {
			h = ((Double) stack.pop()).doubleValue();
			w = ((Double) stack.pop()).doubleValue();
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			this.fillOval(g, x, y, w, h);
		} else if (s.equals("fillRect")) {
			h = ((Double) stack.pop()).doubleValue();
			w = ((Double) stack.pop()).doubleValue();
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			this.fillRect(g, x, y, w, h);
		} else if (s.equals("fillRoundRect")) {
			rh = ((Double) stack.pop()).doubleValue();
			rw = ((Double) stack.pop()).doubleValue();
			h = ((Double) stack.pop()).doubleValue();
			w = ((Double) stack.pop()).doubleValue();
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			this.fillRoundRect(g, x, y, w, h, rw, rh);
		} else if (s.equals("fix")) {
			double nks = ((Double) stack.pop()).doubleValue();
			d = ((Double) stack.pop()).doubleValue();
			stack.push(StrUtil.FixedDoubleToStr(d, (int) nks));
		} else if (s.equals("font")) {
			String name = (String) stack.pop();
			s = (String) stack.pop();
			int style = Font.PLAIN;
			if (s.equals("BOLD")) {
				style = Font.BOLD;
			} else if (s.equals("ITALIC")) {
				style = Font.ITALIC;
			} else if (s.equals("PLAIN")) {
				style = Font.PLAIN;
			}
			d = ((Double) stack.pop()).doubleValue() * (this.dpi) / 72.0;
			int size = (int) d;
			g.setFont(new Font(name, style, size));
		} else if (s.equals("height")) {
			Dimension dim = this.dimPage;
			stack.push(new Double(dim.getHeight() - this.margins[TOP] - this.margins[BOTTOM]));
		} else if (s.equals("lastx")) {
			stack.push(new Double(this.lastx));
		} else if (s.equals("lasty")) {
			stack.push(new Double(this.lasty));
		} else if (s.equals("length")) {
			stack.push(new Double(stack.peek().toString().length()));
		} else if (s.equals("load")) {
			x = ((Double) stack.pop()).doubleValue();
			stack.push(mem[(int) x]);
		} else if (s.equals("LEFT")) {
			stack.push(new Double(LEFT));
		} else if (s.equals("over")) {
			Object o = stack.pop();
			Object o0 = stack.peek();
			stack.push(o);
			stack.push(o0);
		} else if (s.equals("RIGHT")) {
			stack.push(new Double(RIGHT));
		} else if (s.equals("swap")) {
			Object o = stack.pop();
			Object o0 = stack.pop();
			stack.push(o);
			stack.push(o0);
		} else if (s.equals("store")) {
			x = ((Double) stack.pop()).doubleValue();
			mem[(int) x] = stack.peek();
		} else if (s.equals("TOP")) {
			stack.push(new Double(TOP));
		} else if (s.equals("valign")) {
			location = (int) ((Double) stack.pop()).doubleValue();
			if ((location == BOTTOM) || (location == CENTER) || (location == TOP)) {
				this.setAlign(location);
			} else {
				this.printError("location unknown", s + " (" + location + ")", line);
			}
		} else if (s.equals("width")) {
			Dimension dim = this.dimPage;
			stack.push(new Double(dim.getHeight() - this.margins[LEFT] - this.margins[RIGHT]));
		} else if (s.equals("?")) {
			log.info("" + stack.peek());
		} else if (s.equals("??")) {
			log.info("" + stack.pop());
		} else if (s.equals("?<")) {
			log.info("" + stack.peek());
		} else if (s.equals("??<")) {
			log.info("" + stack.pop());
		} else if (s.equals("+")) {
			Object o0 = stack.pop();
			Object o1 = stack.pop();
			if ((o0 instanceof Double) && (o1 instanceof Double)) {
				y = ((Double) o0).doubleValue();
				x = ((Double) o1).doubleValue();
				stack.push(new Double(x + y));
			} else {
				stack.push(new String(o1.toString().concat(o0.toString())));
			}
		} else if (s.equals("-")) {
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			stack.push(new Double(x - y));
		} else if (s.equals("*")) {
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			stack.push(new Double(x * y));
		} else if (s.equals("/")) {
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			stack.push(new Double(x / y));
		} else if (s.equals("%")) {
			y = new Double(stack.pop().toString()).doubleValue();
			x = new Double(stack.pop().toString()).doubleValue();
			stack.push(new Double(x % y));
		} else if (s.equals("==")) {
			stack.push((stack.pop().equals(stack.pop()) ? new Double(1.0) : new Double(0.0)));
		} else if (s.equals("!=")) {
			stack.push((!stack.pop().equals(stack.pop()) ? new Double(1.0) : new Double(0.0)));
		} else if (s.equals("<=")) {
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			stack.push(y <= x ? new Double(1.0) : new Double(0.0));
		} else if (s.equals("<")) {
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			stack.push(y < x ? new Double(1.0) : new Double(0.0));
		} else if (s.equals(">=")) {
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			stack.push(y >= x ? new Double(1.0) : new Double(0.0));
		} else if (s.equals(">")) {
			y = ((Double) stack.pop()).doubleValue();
			x = ((Double) stack.pop()).doubleValue();
			stack.push(y > x ? new Double(1.0) : new Double(0.0));
		} else if (s.equals("ALL")) {
			stack.push(new Double(-1.0));
		} else if (s.equals("drawDataFields")) {
			Object o = null;
			String n = (String) stack.pop();
			Vector v = (Vector) stack.pop();
			int last = (int) ((Double) stack.pop()).doubleValue();
			int first = (int) ((Double) stack.pop()).doubleValue();
			n = n.substring(1, n.length());
			PrintJobMacro m0 = new PrintJobMacro((PrintJobMacro) this.macros.get(n));
			Hashtable p = null;
			int gedruckt = 0;
			if (first <= 0) {
				first = 0;
			}
			if (last > v.size()) {
				last = v.size();
			}
			int lenj = v.size();
			for (int i = first; i < last; i++) {
				p = (Hashtable) v.elementAt(i);
				lenj = m0.getAnweisungen().size();
				for (int j = 0; j < lenj; j++) {
					String cmd = (String) m0.getAnweisungen().elementAt(j);
					o = p.get(cmd);
					if (o != null) {
						stack.push(o.toString());
					} else {
						String s0 = cmd;
						if (s0.equals("do")) {
							this.programmstack.push(new Integer(j));
						} else if (s0.equals("else")) {
							int count = 1;
							j++;
							while ((j + 1 < lenj) && !s0.equals("endif") && (count > 0)) {
								j++;
								s0 = (String) m0.getAnweisungen().elementAt(j);
								if (s0.equals("else")) {
									count++;
								} else if (s0.equals("endif")) {
									count--;
								}
							}
						} else if (s0.equals("endif")) {
						} else if (s0.equals("if")) {
							x = ((Double) stack.pop()).doubleValue();
							if ((int) x == 0) {
								int count = 1;
								while ((j + 1 < lenj) && !s0.equals("else") && !s0.equals("endif") && (count > 0)) {
									j++;
									s0 = (String) m0.getAnweisungen().elementAt(j);
									if (s0.equals("if")) {
										count++;
									} else if (s0.equals("endif")) {
										count--;
									}
								}
							}
						} else if (s0.equals("page")) {
							this.hpage++;
							if (this.hpage == page) {
								this.setDrawing(true);
							} else {
								this.setDrawing(false);
							}
						} else if (s0.equals("pagenumber")) {
							stack.push(new Double(this.hpage));
						} else if (s0.equals("while")) {
							x = ((Double) stack.pop()).doubleValue();
							if ((int) x != 0) {
								j = ((Integer) this.programmstack.peek()).intValue();
							} else {
								this.programmstack.pop();
							}
						} else {
							this.execute(s0, g, stack, j, page);
						}
					}
				}
				gedruckt++;
			}
			stack.push(new Double(gedruckt));
		} else if (this.executeMacro(s, g, stack, line, page)) {
		} else if (this.processCommand(s, g, stack, line, page)) {
		} else {
			Object o = this.params.get(s);
			if (o != null) {
				if (!(o instanceof Vector)) {
					stack.push(o.toString());
				} else {
					stack.push(o);
				}
			} else {
				try {
					stack.push(new Double(s));
				} catch (NumberFormatException nfe) {
					stack.push(s);
				}
			}
		}
	}

	/**
	 * Versucht das angegebene Kommando als PrintJobMacro auszuf&uuml;hren.
	 *
	 * @param cmd   Das Kommando.
	 * @param g     Das Graphics-Objekt, auf dem die Ausgabe stattfinden soll.
	 * @param stack Der Stack, von dem das Kommando seine Parameter beziehen kann.
	 * @param line  Nummer des verarbeiteten Kommandos.
	 * @param page  Die vom PrintJob angeforderte Druckseite.
	 * @return <TT>true</TT>, wenn das Kommando als PrintJobMacro erkannt werden konnte.
	 */
	protected boolean executeMacro(String cmd, Graphics g, Stack stack, int line, int page) {
		int len = 0;
		PrintJobMacro m = (PrintJobMacro) this.macros.get(cmd);
		String s = null;
		if (m != null) {
			len = m.getAnweisungen().size();
			for (int i = 0; i < len; i++) {
				s = (String) m.getAnweisungen().elementAt(i);
				try {
					if (s.equals("do")) {
						this.programmstack.push(new Integer(i));
					} else if (s.equals("else")) {
						int count = 1;
						i++;
						while ((i + 1 < len) && !s.equals("endif") && (count > 0)) {
							i++;
							s = (String) m.getAnweisungen().elementAt(i);
							if (s.equals("else")) {
								count++;
							} else if (s.equals("endif")) {
								count--;
							}
						}
					} else if (s.equals("endif")) {
					} else if (s.equals("if")) {
						double x = ((Double) stack.pop()).doubleValue();
						if ((int) x == 0) {
							int count = 1;
							while ((i + 1 < len) && !s.equals("else") && !s.equals("endif") && (count > 0)) {
								i++;
								s = (String) m.getAnweisungen().elementAt(i);
								if (s.equals("if")) {
									count++;
								} else if (s.equals("endif")) {
									count--;
								}
							}
						}
					} else if (s.equals("page")) {
						this.hpage++;
						if (this.hpage == page) {
							this.setDrawing(true);
						} else {
							this.setDrawing(false);
						}
					} else if (s.equals("pagenumber")) {
						stack.push(new Double(this.hpage));
					} else if (s.equals("while")) {
						double x = ((Double) stack.pop()).doubleValue();
						if ((int) x != 0) {
							i = ((Integer) this.programmstack.peek()).intValue();
						} else {
							this.programmstack.pop();
						}
					} else {
						this.execute(s, g, stack, i, page);
					}
					// this.execute(s, g, stack, line, page);
				} catch (EmptyStackException ese) {
					this.printError("[PrintJobMacro:" + m.getName() + "] stack empty", s, i);
					return false;
				} catch (NumberFormatException nfe) {
					this.printError("[PrintJobMacro:" + m.getName() + "] numberformat error", s, i);
					return false;
				} catch (Exception e) {
					log.error("\n[PrintJobMacro:" + m.getName() + "] --> " + s, e);
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Bearbeitet das &uuml;bergebene Kommando und &uuml;bertr&auml;gt das Ergebnis gegebenenfalls auf den graphischen
	 * Kontext. Als Seiteneffekt wird der &uuml;bergebene Stack manipuliert. Auf Fehler mu&szlig; mit einer Exception
	 * reagiert werden.
	 *
	 * @param cmd   Das Kommando.
	 * @param g     Das Graphics-Objekt, auf dem die Ausgabe stattfinden soll.
	 * @param stack Der Stack, von dem das Kommando seine Parameter beziehen kann.
	 * @param line  Nummer des verarbeiteten Kommandos.
	 * @param page  Die vom PrintJob angeforderte Druckseite.
	 * @return <TT>true</TT>, wenn das Kommando erkannt werden konnte.
	 */
	protected boolean processCommand(String cmd, Graphics g, Stack stack, int line, int page) {
		return false;
	}

	/** Druckt eine Fehlermeldung mit den angegeben Parametern. */
	protected void printError(String message, String s, int param) {
		log.error("    " + message + (((s == null) && (param < 0)) ? "" : " -> " + s + " (param #" + param + ")"));
	}

	/**
	 * L&ouml;&szlig;st den &uuml;bergebenen Vector aus Anweisungszeilen in einzelne Strings auf.
	 *
	 * @param script Die Anweisungszeilen.
	 * @return Ein Vector mit den einzelnen Strings der Anweisungszeilen.
	 */
	protected Vector parseScript(Vector script) {
		boolean strdetected = false;
		int index = 0;
		int len = 0;
		String s = null;
		String s0 = null;
		Vector zwerg = new Vector();
		len = script.size();
		for (int i = 0; i < len; i++) {
			s0 = new String((String) script.elementAt(i)).trim();
			s = new String("");
			while (s0.length() > 0) {
				strdetected = (s0.startsWith("\"") ? true : false);
				if (strdetected) {
					s0 = s0.substring(1, s0.length());
				}
				index = s0.indexOf((strdetected ? "\"" : " "));
				if (index > 0) {
					s = s0.substring(0, index);
					s0 = s0.substring(index + (strdetected ? 2 : 1), s0.length());
				} else {
					s = s0.substring(0, s0.length());
					s0 = new String("");
				}
				if (s.trim().startsWith("#")) {
					break;
				}
				if (s.trim().length() > 0) {
					zwerg.addElement(s);
				}
			}
		}
		return zwerg;
	}

	/** @return Die Seitennummer der ersten Seite des Printjobs. */
	public int getMinPage() {
		return this.ja.getMinPage();
	}

	/** @return Die Seitennummer der letzten Seite des Printjobs. */
	public int getMaxPage() {
		return this.ja.getMaxPage();
	}

	/** Zeigt einen Dialog, der eine Vorschau auf den Druck enth&auml;lt. */
	public boolean preview() {
		int dpi = this.dpi;
		this.changeDPINoPA(90);
		PrintPreview ppv = this.createPrintPreview();
		this.changeDPINoPA(dpi);
		if (ppv.isCanceled()) {
			return false;
		}
		return true;
	}

	/** Die Dimensionen der durch den PrintJob gedruckten Seite. */
	public Dimension getDim() {
		return new Dimension(this.dimPage);
	}

	/** @return Ein PrintPreview zur Anzeige. */
	protected PrintPreview createPrintPreview() {
		return new PrintPreview(this, null, this.dimPage, this);
	}

}
