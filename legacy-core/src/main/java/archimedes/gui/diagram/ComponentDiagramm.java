/*
 * ComponentDiagramm.java
 *
 * 20.03.2004
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.gui.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.swing.JComponent;

import archimedes.gui.PaintMode;
import archimedes.gui.StatusBarOwner;
import archimedes.model.DataModel;
import archimedes.model.StereotypeModel;
import archimedes.model.TableModel;
import archimedes.model.gui.GUIDiagramModel;
import archimedes.model.gui.GUIObjectModel;
import archimedes.model.gui.GUIRelationModel;
import archimedes.model.gui.GUIViewModel;
import archimedes.transfer.CopyAndPasteController;
import corent.base.StrUtil;

/**
 * Mit Hilfe dieser Klasse kann ein Archimedes-Diagramm auf dem Bildschirm dargestellt werden.
 * <P>
 * &Uuml;ber die Property <I>archimedes.gui.ComponentDiagramm.HIT_TOLERANCE</I> kann die Entfernung beeinflu&szlig;t
 * werden, in der ein Objekt von der Mouse-Position liegen mu&szlig;, um noch als getroffen zu gelten. Die
 * Defaulteinstellung liegt bei 10.
 * <P>
 * Die Property <I>archimedes.gui.ComponentDiagramm.SmartPositioner.mode</I> erm&ouml;glicht die Beeinflussung der
 * SmartPositioner-Position. &Uuml;ber <TT>CENTER</TT> wird der SmartPositioner in die Mitte der Tabelle gesetzt. Mit
 * dem Schl&uuml;sselwort <TT>LEFTTOP</TT> erscheint er an der linken, oberen Ecke der Tabelle. Weitere Modi sind nicht
 * implementiert.
 *
 * @param <T> An enumeration which contains the identifiers for creating objects.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 29.08.2007 - Anpassung der Methoden <TT>paint(Graphics)</TT> an die M&ouml;glichkeit, Tabellen &uuml;ber
 *          Stereotypen aus dem Diagramm auszublenden. Hierzu war auch eine Anpassung der bisherigen Methodik, Tabellen
 *          auszublenden, n&ouml;tig.
 * @changed OLI 18.12.2007 - Erweiterungen im Rahmen der Copy-&amp;-Paste-Funktion f&uuml;r Tabellen.
 * @changed OLI 01.01.2008 - Erweiterung der Methode <TT>paint(Graphics, boolean, boolean)</TT> um den zweiten
 *          Boolean-Parameter, der anzeigt, ob die Methode zur Generierung einer Bildschirmanzeige oder eines Ausdrucks
 *          (bzw. Grafikexports) genutzt werden soll.
 * @changed OLI 16.12.2008 - Entstatifizierung des Zoomfaktors.
 * @changed OLI 20.12.2011 - Einf&uuml;gen der Bereinigung der komplexen Indices beim L&ouml;schen einer Tabelle.
 */

public class ComponentDiagramm<T extends Enum<?>> extends JComponent implements CoordinateConverter {

	/** Anzahl der x-Punkte pro Seite. */
	public static int DOTSPERPAGEX = 1050;
	/** Anzahl der y-Punkte pro Seite. */
	public static int DOTSPERPAGEY = 1500;
	/**
	 * Der Radius in Pixels innerhalb dessen eine Diagrammkomponente als getroffen gelten soll.
	 */
	public static int HIT_TOLERANCE = Integer.getInteger("archimedes.gui.ComponentDiagramm.HIT_TOLERANCE", 10);
	/** Anzahl der Seiten pro Spalte. */
	public static int PAGESPERCOLUMN = Integer.getInteger("archimedes.gui.ComponentDiagramm.PAGESPERCOLUMN", 3);
	/** Anzahl der Seiten pro Zeile. */
	public static int PAGESPERROW = Integer.getInteger("archimedes.gui.ComponentDiagramm.PAGESPERROW", 4);
	/** Maximale Seitenanzahl. */
	public static int MAXPAGECOUNT = PAGESPERROW * PAGESPERCOLUMN;
	/** Der Zoom-Faktor der aktuellen Darstellung (Defaultwert). */
	public static double DEFAULTZOOMFACTOR = 1.0;

	/* Diese Flagge wird gesetzt, wenn der Benutzer eine Diagrammkomponente 'dragg't. */
	private boolean dragging = false;
	/*
	 * Diese Flagge ist gesetzt, wenn sich die Anzeigekomponente im Einf&uuml;gemodus f&uuml;r Tabelle befindet.
	 */
	private boolean insertmode = false;
	/*
	 * Wenn diese Flagge gesetzt ist, wird eine Tabelle mit Standardeinstellungen und -Spalten generiert.
	 */
	private boolean standardmode = false;
	/* Referenz auf das darzustellende Diagramm. */
	private GUIDiagramModel dm = null;
	/* Der Zoomfaktor des Diagramm. */
	private double zoomFactor = DEFAULTZOOMFACTOR;
	/* X-Koordinaten-Offset im Falle eines Tabellen-Dragging. */
	private int draggingOffsetX = 0;
	/* Y-Koordinaten-Offset im Falle eines Tabellen-Dragging. */
	private int draggingOffsetY = 0;
	/*
	 * Wird dieser Wert gesetzt, so wird ein Raster auf die Arbeitsfl&auml;che gemalt. Der Abstand der Rasterpunkte
	 * entspricht dem gesetzten Wert. Ein Wert von <= 0 schaltet das Raster aus.
	 */
	private int rasterwidth = 25;
	private int exposedRasterPointAnyXPointsHeight =
			Integer.getInteger("archimedes.gui.ComponentDiagramm.exposed.raster.point.any.x.points.height", 5);
	private int exposedRasterPointAnyXPointsWidth =
			Integer.getInteger("archimedes.gui.ComponentDiagramm.exposed.raster.point.any.x.points.width", 10);
	/* Radius des Smart-Positioners. */
	private int smartposradius = 100;
	/* Das geclickte Objekt. */
	private Object clicked = null;
	/* Das gedraggte Objekt. */
	private Object dragged = null;
	/* Eine Liste mit den aktuell im Diagramm abgebildeten Shapes. */
	private Vector shapes = new Vector();

	private ComponentDiagramListener<T> componentDiagramListener = null;
	private CopyAndPasteController copyAndPasteController = null;
	private GUIObjectCreator<T> guiObjectCreator = null;
	private T guiObjectToCreateIdentifier = null;
	private GUIViewModel view = null;
	private StatusBarOwner statusBarOwner = null;
	private AdditionalDiagramInfoReplacer additionalDiagramInfoReplacer = new AdditionalDiagramInfoReplacer();

	/**
	 * Erzeugt eine neue Komponente zur Abbildung eines Diagramms anhand der &uuml;bergebenen Parameter.
	 *
	 * @param vm                       Der View des Diagramms, der in der Komponente dargestellt werden soll.
	 * @param width                    Die Breite des Zeichenfl&auml;che.
	 * @param height                   Die H&ouml;he der Zeichenfl&auml;che.
	 * @param diagram                  Das Diagramm-Modell, das die Komponente darstellen soll.
	 * @param guiObjectCreator         Eine factory f&uuml;r die Tabellenerzeugung.
	 * @param componentDiagramListener A listener for the events of the component diagram.
	 * @param statusBarOwner           A component which is to update if the status of the component is changed.
	 *
	 * @changed OLI 18.12.2007 - Erweiterung um die Funktionen zur Copy-&amp;-Paste-Funktion f&uuml;r Tabellen.<BR>
	 *
	 */
	public ComponentDiagramm(
			GUIViewModel vm,
			int width,
			int height,
			GUIDiagramModel diagram,
			GUIObjectCreator<T> guiObjectCrtr,
			ComponentDiagramListener<T> componentDiagramListener,
			StatusBarOwner statusBarOwner,
			CopyAndPasteController capc) {
		super();
		this.componentDiagramListener = componentDiagramListener;
		this.copyAndPasteController = capc;
		this.dm = diagram;
		this.guiObjectCreator = guiObjectCrtr;
		this.statusBarOwner = statusBarOwner;
		this.setView(vm);
		Dimension dim = new Dimension(width, height);
		this.setMaximumSize(dim);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Vector v = hits(e.getX(), e.getY());
				int len = v.size();
				if ((e.getButton() == MouseEvent.BUTTON3) && e.isControlDown()) {
					if (v.size() > 0) {
						if (v.get(0) instanceof TableModel) {
							TableModel tm = (TableModel) v.get(0);
							Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
							String n = tm.getName();
							tm.setName("COPY OF " + n);
							Transferable t = copyAndPasteController.tableToTransferable(tm);
							cb.setContents(t, null);
							tm.setName(n);
						}
					}
				} else if ((e.getButton() == MouseEvent.BUTTON3) && e.isShiftDown()) {
					Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
					try {
						String s = (String) cb.getData(DataFlavor.stringFlavor);
						try {
							TableModel[] tms = copyAndPasteController.transferableStringToTable((DataModel) dm, s);
							for (TableModel tm : tms) {
								((GUIObjectModel) tm)
										.setXY(
												getView(),
												roundCoor(e.getX(), e.isShiftDown()),
												roundCoor(e.getY(), e.isShiftDown()));
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						dm.raiseAltered();
						repaint();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				if ((e.getButton() == MouseEvent.BUTTON3)) {
					for (int i = 0; i < len; i++) {
						if (v.elementAt(i) instanceof GUIRelationModel) {
							GUIRelationModel r = (GUIRelationModel) v.elementAt(i);
							Point p = r
									.getPoint(
											getView(),
											roundCoor(convertReverse(e.getX()), e.isShiftDown()),
											roundCoor(convertReverse(e.getY()), e.isShiftDown()));
							r.removePoint(getView(), p);
							dm.raiseAltered();
							repaint();
						}
					}
				}
				if ((e.getClickCount() == 2) && (len > 0) && (v.elementAt(0) instanceof GUIObjectModel)) {
					objectDoubleClicked((GUIObjectModel) v.elementAt(0), e.getButton());
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (insertmode) {
					insertmode = false;
					if (!e.isPopupTrigger()) {
						GUIObjectModel om = guiObjectCreator
								.create(
										getView(),
										roundCoor(e.getX(), e.isShiftDown()),
										roundCoor(e.getY(), e.isShiftDown()),
										guiObjectToCreateIdentifier,
										isStandardMode());
						dm.addGUIObject(om);
						dm.raiseAltered();
					}
					repaint();
				} else {
					Vector v = hits(e.getX(), e.getY());
					int len = v.size();
					if (len > 0) {
						clicked = v.elementAt(0);
					}
				}
				updateLabelPosition(e.getX(), e.getY());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (insertmode && e.isPopupTrigger()) {
					insertmode = false;
					repaint();
					return;
				}
				clicked = null;
				if (dragging) {
					dragging = false;
					if (dragged instanceof GUIObjectModel) {
						GUIObjectModel t = (GUIObjectModel) dragged;
						t
								.setXY(
										getView(),
										roundCoor(convertReverse(e.getX()) - draggingOffsetX, e.isShiftDown()),
										roundCoor(convertReverse(e.getY()) - draggingOffsetY, e.isShiftDown()));
					} else if (dragged instanceof RelationPointContainer) {
						RelationPointContainer rpc = (RelationPointContainer) dragged;
						if (rpc.relation.collisionWithNeighbour(getView(), rpc.point)) {
							rpc.relation.removePoint(getView(), rpc.point);
						}
					}
					dm.raiseAltered();
					dragged = null;
					repaint();
				}
				updateLabelPosition(e.getX(), e.getY());
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = roundCoor(convertReverse(e.getX()), e.isShiftDown());
				int y = roundCoor(convertReverse(e.getY()), e.isShiftDown());
				if (!dragging) {
					Vector v = hits(e.getX(), e.getY());
					int len = v.size();
					if (len > 0) {
						if (v.elementAt(0) instanceof GUIObjectModel) {
							dragging = true;
							dragged = v.elementAt(0);
							GUIObjectModel t = (GUIObjectModel) dragged;
							draggingOffsetX = x - t.getX(getView());
							draggingOffsetY = y - t.getY(getView());
						} else if (v.elementAt(0) instanceof GUIRelationModel) {
							dragging = true;
							GUIRelationModel r = (GUIRelationModel) v.elementAt(0);
							Point p = r
									.getPoint(
											getView(),
											convertReverse(roundCoor(e.getX(), e.isShiftDown())),
											convertReverse(roundCoor(e.getY(), e.isShiftDown())));
							dragged = new RelationPointContainer(r, p);
						}
						if ((len > 1) && (v.elementAt(1) instanceof GUIRelationModel)) {
							dragging = true;
							int index = 0;
							if (len > 1) {
								index = 1;
							}
							GUIRelationModel r = (GUIRelationModel) v.elementAt(index);
							Point p = r
									.getPoint(
											getView(),
											roundCoor(e.getX(), e.isShiftDown()),
											roundCoor(e.getY(), e.isShiftDown()));
							if (r.isEndPoint(getView(), p)) {
								dragged = new RelationEndPointContainer(r, p, r.getObjectPointed(getView(), p));
							} else {
								dragged = new RelationPointContainer(r, p);
							}
						}
					}
				} else {
					if (dragged instanceof GUIObjectModel) {
						GUIObjectModel t = (GUIObjectModel) dragged;
						t.setXY(getView(), x - draggingOffsetX, y - draggingOffsetY);
						repaint();
					} else if (dragged instanceof RelationEndPointContainer) {
						RelationEndPointContainer rpc = (RelationEndPointContainer) dragged;
						rpc.relation
								.computeOffset(
										getView(),
										rpc.object,
										roundCoor(e.getX(), e.isShiftDown()),
										roundCoor(e.getY(), e.isShiftDown()));
						repaint();
					} else if (dragged instanceof RelationPointContainer) {
						RelationPointContainer rpc = (RelationPointContainer) dragged;
						rpc.point.move(x, y);
						/*
						 * rpc.point.move(roundCoor(e.getX(), e.isShiftDown()), roundCoor(e.getY(), e.isShiftDown()));
						 */
						repaint();
					}
				}
				updateLabelPosition(e.getX(), e.getY());
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				updateLabelPosition(e.getX(), e.getY());
			}
		});
	}

	private int roundCoor(int c, boolean shiftdown) {
		if (!shiftdown) {
			int c0 = (c + (this.rasterwidth / 2)) / this.rasterwidth;
			c0 = c0 * this.rasterwidth;
			return c0;
		}
		return c;
	}

	/**
	 * This method will be called if the user double clicks an entity (box) in the diagram.
	 *
	 * @param object   The object which has been double clicked.
	 * @param buttonId The id of the button which is clicked.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	public void objectDoubleClicked(GUIObjectModel object, int buttonId) {
		if (this.componentDiagramListener != null) {
			this.componentDiagramListener.objectDoubleClicked(object, this, buttonId);
		}
	}

	/** Fordert ein Repaint-Event bei der Komponente an. */
	public void doRepaint() {
		this.repaint();
	}

	/**
	 * Setzt das &uuml;bergebene Diagramm als neues Diagramm f&uuml;r die Komponente ein.
	 *
	 * @param dm Das neue Diagramm.
	 */
	public void setDiagramm(GUIDiagramModel dm) {
		this.dm = dm;
		this.doRepaint();
	}

	/** @return Referenz auf das Diagramm, das in der Komponente dargestellt wird. */
	public GUIDiagramModel getDiagramm() {
		return this.dm;
	}

	/**
	 * @return Eine Liste mit den ShapeContainern, deren Shapes durch die angegebenen Koordinaten getroffen werden.
	 */
	public Vector hits(int x, int y) {
		Vector v = this.hits(x, y, HIT_TOLERANCE, HIT_TOLERANCE);
		return v;
	}

	/**
	 * @return Eine Liste mit den ShapeContainern, deren Shapes durch die angegebenen Koordinaten getroffen werden.
	 */
	public Vector hits(int x, int y, int xht, int yht) {
		Vector v = new Vector();
		for (int i = 0, len = this.shapes.size(); i < len; i++) {
			ShapeContainer sc = (ShapeContainer) this.shapes.elementAt(i);
			if ((sc.obj != null) && (sc.shape != null) && ((sc.shape.contains((double) x, (double) y)
					|| sc.shape.intersects((double) x - xht / 2, (double) y - yht / 2, xht, yht)))) {
				v.addElement(sc.obj);
			}
		}
		return v;
	}

	/* Accessoren & Mutatoren. */

	/**
	 * @return Das aktuell gedraggte Element, falls es ein solches gibt, sonst <TT>null</TT>.
	 */
	public Object getDragged() {
		return this.dragged;
	}

	/**
	 * @return Das aktuell angeclickte Element, falls es ein solches gibt, sonst <TT>null</TT>.
	 */
	public Object getClicked() {
		return this.clicked;
	}

	/** @return Der Wert f&uuml;r den Abstand zwischen zwei Rasterpunkten. */
	public int getRasterWidth() {
		return this.rasterwidth;
	}

	/**
	 * Setzt den &uuml;bergebenen Wert f&uuml;r die Eigenschaft Rasterwidth ein.
	 *
	 * @deprecated Hier hat sich der Fehlerteufel eingeschlichen. Richtig mu&szlig; es heissen: setRasterWidth(int).
	 *
	 * @param rasterwidth Der neue Wert f&uuml;r die Eigenschaft Rasterwidth.
	 */
	@Deprecated
	public void setRasterwidth(int rasterwidth) {
		this.rasterwidth = rasterwidth;
	}

	/**
	 * Setzt den &uuml;bergebenen Wert f&uuml;r die Eigenschaft Rasterwidth ein.
	 *
	 * @param rasterwidth Der neue Wert f&uuml;r die Eigenschaft Rasterwidth.
	 */
	public void setRasterWidth(int rasterwidth) {
		this.rasterwidth = rasterwidth;
	}

	/** @return Der Wert Radius des Smart-Positioners. */
	public int getSmartPosRadius() {
		return this.smartposradius;
	}

	/**
	 * Setzt den &uuml;bergebenen Wert f&uuml;r die Eigenschaft Smartposradius ein.
	 *
	 * @param spr Der neue Wert f&uuml;r die Eigenschaft Smartposradius.
	 */
	public void setSmartPosRadius(int spr) {
		this.smartposradius = spr;
	}

	/**
	 * @return <TT>true</TT>, wenn sich die Komponente im Einf&uuml;gemodus f&uuml;r Tabellen befindet, <TT>false</TT>
	 *         sonst.
	 */
	public boolean isInsertMode() {
		return this.insertmode;
	}

	/**
	 * Setzt den Wert f&uuml;r die Eigenschaft Insertmode.
	 *
	 * @param im Der neue Wert f&uuml;r die Eigenschaft Insertmode.
	 */
	public void setInsertMode(boolean im) {
		this.insertmode = im;
	}

	/**
	 * @return <TT>true</TT>, wenn im Insertmodus eine Standard-Tabelle generiert werden soll.
	 */
	public boolean isStandardMode() {
		return this.standardmode;
	}

	/**
	 * Setzt den Wert f&uuml;r die Eigenschaft Standardmode.
	 *
	 * @param sm Der neue Wert f&uuml;r die Eigenschaft Standardmode.
	 */
	public void setStandardMode(boolean sm) {
		this.standardmode = sm;
	}

	/** @return Referenz auf den View, der in der Komponente angezeigt wird. */
	public GUIViewModel getView() {
		return this.view;
	}

	/**
	 * Setzt den Wert f&uuml;r die Eigenschaft View.
	 *
	 * @param view Der neue Wert f&uuml;r die Eigenschaft View.
	 */
	public void setView(GUIViewModel view) {
		this.view = view;
	}

	/**
	 * Liefert den aktuellen Zoomfaktors des Diagramms.
	 * 
	 * @return Der aktuelle Zoomfaktor des Diagramms.
	 *
	 * @changed OLI 16.12.2008 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	@Override
	public double getZoomFactor() {
		return this.zoomFactor;
	}

	/**
	 * Setzt den &uuml;bergebenen Wert als neuen Zoomfaktor f&uuml;r das Diagramm ein.
	 * 
	 * @param zf Ein neuer Wert f&uuml;r den Zoomfaktor.
	 *
	 * @changed OLI 16.12.2008 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	public void setZoomfactor(double zf) {
		this.zoomFactor = zf;
	}

	/**
	 * Liefert zur angegebenen Seite den linken, oberen Eckpunkt.
	 *
	 * @param page Die Seite welche ;o)
	 * @return Der linke, obere Eckpunkt der angegebenen Seite.
	 */
	public Point getLeftUpper(int page) {
		int count = 1;
		int x0 = 0;
		int y0 = 0;
		while (page != count) {
			x0++;
			if (x0 >= PAGESPERROW) {
				x0 = 0;
				y0++;
			}
			count++;
		}
		x0 = x0 * ComponentDiagramm.DOTSPERPAGEX;
		y0 = y0 * ComponentDiagramm.DOTSPERPAGEY;
		/*
		 * switch (page) { case 1: break; case 2: x0 = ComponentDiagramm.DOTSPERPAGEX; break; case 3: x0 =
		 * ComponentDiagramm.DOTSPERPAGEX * 2; break; case 4: x0 = ComponentDiagramm.DOTSPERPAGEX * 3; break; case 5: y0
		 * = ComponentDiagramm.DOTSPERPAGEY; break; case 6: x0 = ComponentDiagramm.DOTSPERPAGEX; y0 =
		 * ComponentDiagramm.DOTSPERPAGEY; break; case 7: x0 = ComponentDiagramm.DOTSPERPAGEX * 2; y0 =
		 * ComponentDiagramm.DOTSPERPAGEY; break; case 8: x0 = ComponentDiagramm.DOTSPERPAGEX * 3; y0 =
		 * ComponentDiagramm.DOTSPERPAGEY; break; case 9: y0 = ComponentDiagramm.DOTSPERPAGEY * 2; break; case 10: x0 =
		 * ComponentDiagramm.DOTSPERPAGEX; y0 = ComponentDiagramm.DOTSPERPAGEY * 2; break; case 11: x0 =
		 * ComponentDiagramm.DOTSPERPAGEX * 2; y0 = ComponentDiagramm.DOTSPERPAGEY * 2; break; case 12: x0 =
		 * ComponentDiagramm.DOTSPERPAGEX * 3; y0 = ComponentDiagramm.DOTSPERPAGEY * 2; break; }
		 */
		return new Point(x0, y0);
	}

	/** @return Maximale Ausdehnung der bedruckten Seiten. */
	public Point getPrintPageCountXY() {
		int x = 1;
		int y = 1;
		int xmax = 1;
		int ymax = 1;
		int page = 1;
		while (page <= MAXPAGECOUNT) {
			Point p = this.getLeftUpper(page);
			if (this
					.hits(
							(int) p.getX() + DOTSPERPAGEX / 2,
							(int) p.getY() + DOTSPERPAGEY / 2,
							DOTSPERPAGEX,
							DOTSPERPAGEY)
					.size() > 0) {
				xmax = (x > xmax ? x : xmax);
				ymax = (y > ymax ? y : ymax);
			}
			x++;
			if (x > PAGESPERROW) {
				x = 1;
				y++;
			}
			page++;
		}
		return new Point(xmax, ymax);
	}

	/**
	 * Konvertiert die angegebene absolute Seite in eine Druckseite.
	 *
	 * @param page Die absolute Seitenangabe.
	 * @return Die konvertierte Seitenangabe im Verh&auml;ltnis zu den Druckseiten.
	 */
	public int getPrintPage(int page) {
		Point p = this.getPrintPageCountXY();
		int rb = (page - 1) / (int) p.getX();
		int d = PAGESPERROW - (int) p.getX();
		return page + rb * d;
	}

	/** @return Die Anzahl der zu druckenden Seiten. */
	public int getPrintPageCount() {
		Point p = this.getPrintPageCountXY();
		return (int) p.getX() * (int) p.getY();
	}

	/**
	 * Setzt die Ansicht des Tabellenschemas auf die Tabelle mit dem angegebenen Namen. Ist eine Tabelle mit dem
	 * &uuml;bergebenen Namen nicht bekannt, wird eine Exception geworfen.
	 *
	 * @param tn Der Name der Tabelle, auf die die Diagrammsicht gesetzt werden soll.
	 * @throws NoSuchElementException wenn der Tabellenname in dem der Anzeige zugrundeliegenden Modell nicht existiert.
	 */
	public void setDiagramViewToTable(String tn) throws NoSuchElementException {
		GUIObjectModel tm = this.dm.getGUIObject(tn);
		Rectangle r = this.getVisibleRect();
		int rwidth = r.width;
		int rheight = r.height;
		int tx = tm.getX(this.view);
		int ty = tm.getY(this.view);
		r.x = (tx - rwidth / 2);
		r.y = (ty - rheight / 2);
		this.scrollRectToVisible(r);
	}

	/* Ueberschreiben von Methoden der Superklasse. */

	/**
	 * @changed OLI 01.01.2008 - Anpassung an die neue Signatur der Methode <TT>paint(Graphics, 
	 *         boolean, boolean)</TT>.
	 */
	@Override
	public void paint(Graphics g) {
		this.paint(g, (this.rasterwidth > 0), false);
	}

	/**
	 *
	 * @param printout Diese Flagg mu&szlig; gesetzt werden, wenn es sich bei dem Malvorgang um einen Ausdruck oder
	 *                 Grafikexport handelt.
	 *
	 * @changed OLI 29.08.2007 - Anpassung an die M&ouml;glichkeit, Tabellen &uuml;ber Stereotypen aus dem Diagramm
	 *          auszublenden. Hierzu war auch eine Anpassung der bisherigen Methodik, Tabellen auszublenden,
	 *          n&ouml;tig.<BR>
	 *          OLI 01.01.2008 - Erweiterung um die Flagge <TT>printout</TT> in der Parameterliste der Methode. Hiermit
	 *          wird angezeigt, ob es sich um einen Druck (bzw. Grafikexport) oder eine Bildschirmausgabe handelt.<BR>
	 *
	 */
	public void paint(Graphics g, boolean raster, boolean printout) {
		this.shapes.removeAllElements();
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.lightGray);
		for (int i = 0; i < this.getWidth(); i += ComponentDiagramm.DOTSPERPAGEX) {
			g.drawLine(convert(i), 0, convert(i), convert(this.getHeight()));
			g.drawLine(convert(i - 1), 0, convert(i - 1), convert(this.getHeight()));
		}
		for (int i = 0; i < this.getHeight(); i += ComponentDiagramm.DOTSPERPAGEY) {
			g.drawLine(0, convert(i), convert(this.getWidth()), convert(i));
			g.drawLine(0, convert(i - 1), convert(this.getWidth()), convert(i - 1));
		}
		if (raster) {
			int page = 1;
			g.setFont(new Font("sansserif", Font.BOLD, 18));
			for (int j = 0; j < this.getHeight(); j += ComponentDiagramm.DOTSPERPAGEY) {
				for (int i = 0; i < this.getWidth(); i += ComponentDiagramm.DOTSPERPAGEX) {
					g.drawString("Seite " + page++, i + 10, j + 30);
				}
			}
			for (int i = 0; i < this.getWidth(); i += this.rasterwidth) {
				for (int j = 0; j < this.getHeight(); j += this.rasterwidth) {
					if ((j % (this.rasterwidth * this.exposedRasterPointAnyXPointsHeight) == 0)
							&& (i % (this.rasterwidth * this.exposedRasterPointAnyXPointsWidth) == 0)) {
						g.setColor(Color.darkGray);
					} else {
						g.setColor(Color.lightGray);
					}
					g.drawLine(i - 1, j, i + 1, j);
					g.drawLine(i, j - 1, i, j + 1);
				}
			}
		}
		g.setColor(Color.black);
		int y = 30;
		g
				.setFont(
						new Font(
								"sansserif",
								Font.BOLD,
								(int) ((double) this.dm.getFontSizeDiagramHeadline() * this.getZoomFactor())));
		g.drawString(dm.getName(), convert(20), convert(y));
		g
				.setFont(
						new Font(
								"sansserif",
								Font.BOLD,
								(int) ((double) this.dm.getFontSizeSubtitles() * this.getZoomFactor())));
		y += g.getFontMetrics().getHeight() + 5;
		g.drawString(this.dm.getAuthor(), convert(20), convert(y));
		y += g.getFontMetrics().getHeight() + 5;
		g.drawString(getVersionString(), convert(20), convert(y));
		y += g.getFontMetrics().getHeight() + 5;
		g.drawString(this.getView().getName(), convert(20), convert(y));
		if (this.dm != null) {
			for (GUIObjectModel tm : this.dm.getGUIObjects()) {
				boolean showTable = (!tm.isDeprecated() || !this.dm.isDeprecatedTablesHidden());
				if (!tm.isInView(this.getView())) {
					continue;
				}
				for (StereotypeModel stm : tm.getStereotypes()) {
					if (stm.isHideTable() || (printout && stm.isDoNotPrint())) {
						showTable = false;
						break;
					}
				}
				if (showTable) {
					this.shapes
							.addElement(
									tm
											.paintObject(
													this,
													this.getView(),
													g,
													(printout ? PaintMode.PRINTER : PaintMode.EDITOR)));
					if (this.dragged == tm) {
						g.setColor(new Color(0, 153, 102));
						int tmx = tm.getX(this.getView());
						int tmy = tm.getY(this.getView());
						int xm = tmx + (tm.getWidth() / 2);
						int ym = tmy + (tm.getHeight() / 2);
						int sm = this.getSmartPosRadius();
						String mode = System
								.getProperty("archimedes.gui.ComponentDiagramm.SmartPositioner.mode", "CENTER")
								.toUpperCase();
						if (mode.equals("CENTER")) {
							g.drawLine(convert(xm - sm), convert(ym), convert(xm + sm), convert(ym));
							g.drawLine(convert(xm), convert(ym - sm), convert(xm), convert(ym + sm));
							g.drawOval(convert(xm - sm), convert(ym - sm), convert(sm * 2), convert(sm * 2));
						} else if (mode.equals("LEFTTOP")) {
							g.drawLine(convert(tmx - sm), convert(tmy), convert(tmx + sm), convert(tmy));
							g.drawLine(convert(tmx), convert(tmy - sm), convert(tmx), convert(tmy + sm));
							g.drawOval(convert(tmx - sm), convert(tmy - sm), convert(sm * 2), convert(sm * 2));
						}
						g.setColor(Color.black);
					}
				}
			}
			for (GUIObjectModel tm : this.dm.getGUIObjects()) {
				boolean showTable = (!tm.isDeprecated() || !this.dm.isDeprecatedTablesHidden());
				if (!tm.isInView(this.getView())) {
					continue;
				}
				for (StereotypeModel stm : tm.getStereotypes()) {
					if (stm.isHideTable()) {
						showTable = false;
						break;
					}
				}
				if (showTable) {
					g.setColor(Color.black);
					for (ShapeContainer sc : tm
							.paintRelations(
									this,
									this.getView(),
									g,
									(printout ? PaintMode.PRINTER : PaintMode.EDITOR))) {
						this.shapes.addElement(sc);
					}
				}
			}
		}
	}

	private String getVersionString() {
		String s = "Version " + this.dm.getVersion() + " - " + this.dm.getDateOfCreation();
		if (this.dm.getAdditionalDiagramInfo() != null) {
			s += additionalDiagramInfoReplacer.replace(this.dm.getAdditionalDiagramInfo(), this.dm);
		}
		return s;
	}

	/** Rechnet die &uuml;bergebene Koordinate anhand des Zoomfaktors um. */
	@Override
	public int convert(int k) {
		return (int) ((double) k * this.getZoomFactor());
	}

	/** Rechnet die &uuml;bergebene Koordinate anhand des Zoomfaktors um. */
	@Override
	public int convert(double k) {
		return (int) ((double) k * (double) this.getZoomFactor());
	}

	@Override
	public int convertReverse(int k) {
		return (int) ((double) k / (double) this.getZoomFactor());
	}

	/**
	 * Sets a new identifier for the type to create new objects.
	 *
	 * @param type The identifier for creating the next GUI object.
	 *
	 * @changed OLI 29.05.2013 - Added.
	 */
	public void setGUIObjectToCreateIdentifier(T type) {
		this.guiObjectToCreateIdentifier = type;
	}

	/**
	 * Removes all points from all relations of the shown view.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	public void removeAllAnglesFromRelations() {
		GUIViewModel v = this.getView();
		for (GUIObjectModel o : v.getObjects()) {
			for (GUIRelationModel r : o.getRelations()) {
				for (Point p : r.getPointsForView(v)) {
					r.removePoint(v, p);
					this.dm.raiseAltered();
				}
			}
		}
		if (this.dm.isAltered()) {
			this.doRepaint();
		}
	}

	public void updateLabelPosition(int x, int y) {
		String clicked = null;
		if ((this.getDragged() == null) && (this.getClicked() != null)) {
			if (this.getClicked() instanceof GUIObjectModel) {
				clicked = this.statusBarOwner.getRenderer().renderObject((GUIObjectModel) this.getClicked());
			} else if (this.getClicked() instanceof GUIRelationModel) {
				clicked = this.statusBarOwner.getRenderer().renderRelation((GUIRelationModel) this.getClicked());
				/*
				 * Relation r = (Relation) this.getClicked(); TabellenspaltenModel ts0 = r.getgetReferencer();
				 * TabellenModel t0 = (ts0 != null ? ts0.getTabelle() : null); TabellenspaltenModel ts1 =
				 * r.getReferenced(); TabellenModel t1 = (ts1 != null ? ts1.getTabelle() : null); clicked = (t0 != null
				 * ? t0.getName() : "<null>") + " -> " + (t1 != null ? t1.getName() : "<null>");
				 */
			}
		}
		this.statusBarOwner
				.updateStatusMessage(
						"(" + x + "," + y + ")"
								+ (this.getDragged() != null
										? "  - " + this.getDragged().toString()
										: (clicked != null ? "  - " + clicked : ""))
								+ (this.isInsertMode() ? StrUtil.FromHTML(" EINF&Uuml;GEN!") : ""));
	}

}