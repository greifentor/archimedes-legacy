/*
 * CPJDiagramm.java
 *
 * 10.04.2004
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.print;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.JobAttributes;
import java.awt.PageAttributes;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import archimedes.legacy.gui.DiagramComponentPanel;
import archimedes.legacy.gui.diagram.ComponentDiagramm;
import archimedes.legacy.model.gui.GUIDiagramModel;
import corent.dates.PDate;
import corent.dates.PTime;

// import corelib.basics.util.*;
// import corelib.basics.dates.*;
import corent.print.CorePrintJob;

/**
 * Diese Klasse leitet einen speziellen PrintJob f&uuml;r Archimedes-Diagramm ab.<BR>
 * <HR>
 *
 * @param <OT> Der Typ der Objekte, die in der Komponente angezeigt werden sollen.
 * 
 * @author O.Lieshoff
 *
 * @changed OLI 01.01.2008 - Anpassungen an die Erweiterung der Signatur der paint-Methode der Klasse
 *          ComponentDiagramm.<BR>
 *
 */

public class CPJDiagramm extends CorePrintJob {

	private DiagramComponentPanel comp = null;
	private BufferedImage diagramm = null;
	private GUIDiagramModel dm = null;

	/**
	 * Generiert einen CPJ-Diagramm anhand der &uuml;bergebenen Parameter.
	 *
	 * @param ja   Die JobAttribute des PrintJobs.
	 * @param pa   Die PageAttribute zum PrintJob.
	 * @param comp Die Component, in der das Diagramm abgebildet wird.
	 */
	public CPJDiagramm(JobAttributes ja, PageAttributes pa, DiagramComponentPanel comp) {
		super(ja, pa);
		this.changeDPI(150);
		this.margins[CorePrintJob.LEFT] = 2.0;
		this.margins[CorePrintJob.RIGHT] = 1.0;
		this.margins[CorePrintJob.TOP] = 1.0;
		this.margins[CorePrintJob.BOTTOM] = 2.0;
		this.changeDPINoPA(this.dpi);
		this.comp = comp;
		this.dm = comp.getDiagram();
		this.diagramm = new BufferedImage(comp.getImgWidth(), comp.getImgHeight(), BufferedImage.TYPE_INT_RGB);
		this.comp.paint(this.diagramm.getGraphics(), false, true);
	}

	/**
	 * Diese Methode malt anhand des aufgearbeiteten Vector den eigentlichen Ausdruck.
	 *
	 * @param g    Der Grafik-Kontext, auf den gedruckt werden soll.
	 * @param page Die Seite, die gedruckt werden soll.
	 *
	 * @changed OLI 01.01.2008 - Anpassungen an die Erweiterung der Signatur der paint-Methode der Klasse
	 *          ComponentDiagramm.<BR>
	 */
	@Override
	public void paint(Graphics g, int page) {
		Point p = this.comp.getLeftUpper(this.comp.getPrintPage(page));
		int x0 = (int) p.getX();
		int y0 = (int) p.getY();
		BufferedImage img = this.diagramm.getSubimage(x0, y0, ComponentDiagramm.DOTSPERPAGEX,
				ComponentDiagramm.DOTSPERPAGEY);
		ImageIcon ic = new ImageIcon(img);
		g.drawImage(ic.getImage(), 75, 130, ic.getImageObserver());
		PDate d = new PDate();
		PTime t = new PTime();
		String s = dm.getName() + " - " + d.toString() + " " + t.toString() + " - Seite "
				+ this.comp.getPrintPage(page);
		g.setFont(new Font("sansserif", Font.BOLD, 12));
		int w = g.getFontMetrics().stringWidth(s);
		g.drawString(s, (ComponentDiagramm.DOTSPERPAGEX / 2 - w / 2) + 75, 100);
		// und abschicken ...
		g.dispose();
	}

	/** L&ouml;scht Verbindungen des PrintJobs zu anderen Objekten. */
	public void release() {
		this.diagramm = null;
		this.dm = null;
	}

}
