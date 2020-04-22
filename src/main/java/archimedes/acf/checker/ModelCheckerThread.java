/*
 * ModelCheckerThread.java
 *
 * 18.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.checker;


import static corentx.util.Checks.*; 

import archimedes.model.*;

import java.util.*;


/**
 * A thread which starts a checking process for the passed model.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 18.05.2016 - Added.
 */

public class ModelCheckerThread implements Runnable {

    private CodeFactory codeFactory = null; 
    private DataModel model = null;
    private ModelCheckerThreadObserver observer = null;

    /**
     * Creates and starts a new model checker thread for the passed observer.
     *
     * @param observer The observer of the model checker thread.
     * @param model The data model which the check is to start for.
     * @param codeFactory The code factory which is used as base for the check.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public ModelCheckerThread(ModelCheckerThreadObserver observer, DataModel model,
            CodeFactory codeFactory) {
        super();
        ensure(codeFactory != null, "code factory cannot be null.");
        ensure(model != null, "data model cannot be null.");
        ensure(observer != null, "model checker thread observer cannot be null.");
        this.codeFactory = codeFactory;
        this.model = model;
        this.observer = observer;
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * @changed OLI 18.05.2016 - Added.
     */
    @Override public void run() {
        List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
        for (ModelChecker mc : this.codeFactory.getModelCheckers()) {
            ModelCheckerMessage[] mcms = mc.check(this.model);
            for (ModelCheckerMessage mcm : mcms) {
                l.add(mcm);
            }
        }
        this.observer.notifyModelCheckerResult(l.toArray(new ModelCheckerMessage[0]));
    }

}