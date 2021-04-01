package archimedes.codegenerators;

import java.util.ArrayList;
import java.util.List;

import archimedes.acf.event.CodeFactoryListener;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.legacy.acf.event.CodeFactoryProgressionEvent;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.event.CodeFactoryProgressionListener;
import archimedes.model.CodeFactory;
import archimedes.model.DataModel;
import baccara.gui.GUIBundle;

/**
 * A base implementation of the CodeFactory interface.
 *
 * @author ollie (03.03.2021)
 */
public abstract class AbstractCodeFactory implements CodeFactory, CodeFactoryProgressionEventProvider {

	public static final String TEMPLATE_PATH =
			System.getProperty("CodeFactory.templates.path", "src/main/resources/templates");

	protected DataModel dataModel;
	protected GUIBundle guiBundle;
	protected ModelCheckerMessageListFrameListener[] modelCheckerMessagelisteners =
			new ModelCheckerMessageListFrameListener[0];

	private List<CodeFactoryListener> listeners = new ArrayList<>();
	private List<CodeFactoryProgressionListener> progressListeners = new ArrayList<>();

	@Override
	public void addCodeFactoryListener(CodeFactoryListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void addCodeFactoryProgressionListener(CodeFactoryProgressionListener listener) {
		if (listener != null) {
			this.progressListeners.add(listener);
		}
	}

	protected void fireCodeFactoryProgressEvent(CodeFactoryProgressionEvent event) {
		this.progressListeners.forEach(l -> {
			try {
				l.progressionDetected(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public GUIBundle getGUIBundle() {
		return guiBundle;
	}

	@Override
	public void removeCodeFactoryListener(CodeFactoryListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void removeCodeFactoryProgressionListener(CodeFactoryProgressionListener listener) {
		if (listener != null) {
			this.progressListeners.remove(listener);
		}
	}

	@Override
	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	@Override
	public void setGUIBundle(GUIBundle guiBundle) {
		this.guiBundle = guiBundle;
	}

	@Override
	public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... listeners) {
		this.modelCheckerMessagelisteners = listeners;
	}

}