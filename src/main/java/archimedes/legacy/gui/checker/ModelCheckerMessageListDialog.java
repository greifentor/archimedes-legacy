/*
 * ModelCheckerMessageListDialog.java
 *
 * 11.07.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.gui.checker;

import java.awt.Frame;

import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.legacy.gui.checker.ModelCheckerMessageListFrameListener.Event;
import baccara.gui.GUIBundle;
import corent.gui.JDialogWithInifile;

/**
 * A dialog which is able to show a list of model checker messages.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 11.07.2016 - Added.
 */

public class ModelCheckerMessageListDialog extends JDialogWithInifile implements ModelCheckerMessageListEventFirer {

	private static final String RES_MODEL_CHECKER_LIST_FRAME_TITLE = "ModelChecker.list.frame.title";

	private Event.Type returnType = null;

	/**
	 * Creates a new dialog for model checker message display and selection.
	 *
	 * @param parent              The parent frame of the dialog.
	 * @param guiBundle           A bundle with GUI information.
	 * @param mcms                The list of model checker messages to show.
	 * @param checkBeforeGenerate Set this flag if the frame is called as check before generating the code.
	 * @param listeners           Which should observe the frame.
	 *
	 * @changed OLI 11.07.2016 - Added.
	 */
	public ModelCheckerMessageListDialog(Frame parent, GUIBundle guiBundle, ModelCheckerMessage[] mcms,
			boolean checkBeforeGenerate, ModelCheckerMessageListFrameListener... listeners) {
		super(parent, guiBundle.getResourceText(RES_MODEL_CHECKER_LIST_FRAME_TITLE), guiBundle.getInifile());
		this.setContentPane(new ModelCheckerMessageListPanel(guiBundle, mcms, checkBeforeGenerate, this, listeners));
		this.pack();
		this.setVisible(true);
	}

	/**
	 * @changed OLI 11.07.2016 - Added.
	 */
	@Override
	public void fireAction(Event e) {
		this.returnType = e.getType();
		if ((e.getType() == Event.Type.CLOSED) || (e.getType() == Event.Type.GENERATE)) {
			this.setVisible(false);
			this.dispose();
		}
	}

	/**
	 * Returns the return type of the last panel action.
	 *
	 * @return The return type of the last panel action.
	 *
	 * @changed OLI 11.07.2016 - Added.
	 */
	public Event.Type getReturnType() {
		return this.returnType;
	}

}