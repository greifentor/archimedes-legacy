/*
 * ModelCheckerThread.java
 *
 * 18.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.checker;

import static corentx.util.Checks.ensure;

import java.util.LinkedList;
import java.util.List;

import archimedes.legacy.model.DataModel;

/**
 * A thread which starts a checking process for the passed model.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 18.05.2016 - Added.
 * @changed OLI 24.06.2020 - Changed code factory parameter by a list of model checkers.
 */

public class ModelCheckerThread implements Runnable {

	private List<ModelChecker> modelCheckers = null;
	private DataModel model = null;
	private ModelCheckerThreadObserver observer = null;

	/**
	 * Creates and starts a new model checker thread for the passed observer.
	 *
	 * @param observer      The observer of the model checker thread.
	 * @param model         The data model which the check is to start for.
	 * @param modelCheckers A list of model checkers.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 18.05.2016 - Added.
	 * @changed OLI 24.06.2020 - Changed code factory parameter by a list of model checkers.
	 */
	public ModelCheckerThread(ModelCheckerThreadObserver observer, DataModel model, List<ModelChecker> modelCheckers) {
		super();
		ensure(modelCheckers != null, "model checkers cannot be null.");
		ensure(model != null, "data model cannot be null.");
		ensure(observer != null, "model checker thread observer cannot be null.");
		this.modelCheckers = modelCheckers;
		this.model = model;
		this.observer = observer;
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * @changed OLI 18.05.2016 - Added.
	 */
	@Override
	public void run() {
		List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
		for (ModelChecker mc : this.modelCheckers) {
			ModelCheckerMessage[] mcms = mc.check(this.model);
			for (ModelCheckerMessage mcm : mcms) {
				l.add(mcm);
			}
		}
		this.observer.notifyModelCheckerResult(l.toArray(new ModelCheckerMessage[0]));
	}

}