/*
 * AbstractGUIDiagramModel.java
 *
 * 23.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model.gui;

import java.util.List;
import java.util.Vector;

import logging.Logger;

/**
 * An abstract implementation of the interface <CODE>GUIDiagramModel</CODE>.
 *
 * @param <OT> The type of objects which are stored in the model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.05.2013 - Added.
 */

abstract public class AbstractGUIDiagramModel implements GUIDiagramModel {

	private static final Logger LOG = Logger.getLogger(AbstractGUIDiagramModel.class);

	private boolean altered = false;
	private List<GUIDiagramModelListener> guiDiagramModelListeners = new Vector<GUIDiagramModelListener>();
	private List<GUIViewModel> views = new Vector<GUIViewModel>();

	/**
	 * @changed OLI 23.05.2013 - Added.
	 */
	@Override
	public void addView(GUIViewModel newView) {
		for (GUIViewModel view : this.views) {
			if (view.getName().equals(newView.getName())) {
				return;
			}
		}
		this.views.add(newView);
	}

	/**
	 * @changed OLI 22.05.2013 - Added.
	 */
	@Override
	public void addGUIDiagramModelListener(GUIDiagramModelListener l) {
		this.guiDiagramModelListeners.add(l);
	}

	/**
	 * @changed OLI 23.05.2013 - Added.
	 */
	@Override
	public void clearAltered() {
		this.altered = false;
		this.fireGUIDiagramModelListenerStateChanged();
	}

	protected void fireGUIDiagramModelListenerStateChanged() {
		for (int i = 0, len = this.guiDiagramModelListeners.size(); i < len; i++) {
			try {
				this.guiDiagramModelListeners.get(i).stateChanged(this.altered);
			} catch (Exception e) {
				LOG.error("an error occured while notifing : " + e.getMessage());
			}
		}
	}

	/**
	 * @changed OLI 03.06.2016 - Added.
	 */
	@Override
	public GUIViewModel getMainView() {
		if (this.views.size() > 0) {
			return this.views.get(0);
		}
		return null;
	}

	/**
	 * @changed OLI 23.05.2013 - Added.
	 */
	@Override
	public GUIViewModel getView(String name) {
		for (GUIViewModel view : this.views) {
			if (view.getName().equals(name)) {
				return view;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 23.05.2013 - Added.
	 */
	@Override
	public List<GUIViewModel> getViews() {
		return this.views;
	}

	/**
	 * @changed OLI 23.05.2013 - Added.
	 */
	@Override
	public List<GUIViewModel> getViewsForObject(String guiObjectName) {
		List<GUIViewModel> result = new Vector<GUIViewModel>();
		for (GUIViewModel view : this.views) {
			if (view.getName().equals(guiObjectName)) {
				result.add(view);
			}
		}
		return result;
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 * @changed OLI 23.05.2013 - Transfered from class <CODE>Diagramm</CODE>.
	 */
	@Override
	public boolean isAltered() {
		return this.altered;
	}

	/**
	 * @changed OLI 14.05.2013 - Approved.
	 * @changed OLI 23.05.2013 - Transfered from class <CODE>Diagramm</CODE>.
	 */
	@Override
	public void raiseAltered() {
		this.altered = true;
		this.fireGUIDiagramModelListenerStateChanged();
	}

	/**
	 * @changed OLI 22.05.2013 - Added.
	 */
	@Override
	public void removeGUIDiagramModelListener(GUIDiagramModelListener l) {
		this.guiDiagramModelListeners.remove(l);
	}

}