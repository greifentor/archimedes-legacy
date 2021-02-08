/**
 * FrameOverview.java
 *
 * 16.12.2008
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import javax.swing.JScrollPane;

import archimedes.gui.diagram.ComponentDiagramm;
import archimedes.legacy.gui.diagram.DiagramGUIObjectCreator;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.transfer.DefaultCopyAndPasteController;
import archimedes.model.gui.GUIViewModel;
import corent.files.Inifile;
import corent.gui.JFrameWithInifile;

/**
 * Dieses Fenster bietet eine &Uuml;bersicht, &uuml;ber das Datenmodell
 * (10%-Zoom).
 * 
 * @author ollie
 * 
 * @changed OLI 16.12.2008 - Hinzugef&uuml;gt.
 */

public class FrameOverview extends JFrameWithInifile {

	private ComponentDiagramm component = null;
	private DiagrammModel dm = null;

	/**
	 * Generiert einen FrameOverview mit Defaultwerten.
	 * 
	 * @param title
	 *            Ein Titel f&uuml;r das &Uuml;bersichtsfenster.
	 * @param ini
	 *            Die Inifile, aus der sich die Gestalt des Fenster
	 *            rekonstruieren soll.
	 * @param dm
	 *            Das Diagramm, das in dem &Uuml;bersichtsfenster dargestellt
	 *            werden soll.
	 * @param frame
	 *            Eine Referenz auf das Applikationshauptfenster.
	 */
	public FrameOverview(String title, Inifile ini, DiagrammModel dm, FrameArchimedes frame) {
		super(title, ini);
		this.dm = dm;
		this.component = new ComponentDiagramm((GUIViewModel) this.dm.getViews().get(0), ComponentDiagramm.DOTSPERPAGEX
				* ComponentDiagramm.PAGESPERROW, ComponentDiagramm.DOTSPERPAGEY * ComponentDiagramm.PAGESPERCOLUMN,
				this.dm, new DiagramGUIObjectCreator(dm), null, null, new DefaultCopyAndPasteController());
		this.setContentPane(new JScrollPane(this.component));
		this.setVisible(true);
		this.component.setZoomfactor(0.1);
	}

	/** Aktualisiert die Anzeige des Diagramms. */
	public void updateView() {
		this.component.doRepaint();
	}

	/**
	 * Setzt das &uuml;bergebene DiagrammModel als neues DiagrammModel f&uuml;r
	 * die Ansicht ein.
	 * 
	 * @param dm
	 *            Ein neues DiagrammModel als Grundlage f&uuml;r die Anzeige.
	 */
	public void setDiagramm(DiagrammModel dm) {
		this.dm = dm;
		this.component.setDiagramm(dm);
	}

}