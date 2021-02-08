/*
 * ModelCheckerThreadObserver.java
 *
 * 18.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.checker;


/**
 * An interface which has to be implemented by classes which are observing a model checker
 * thread.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 18.05.2016 - Added.
 */

public interface ModelCheckerThreadObserver {

    /**
     * Sends the result of the model checking process to the observer.
     *
     * @param mcms An array of model checker messages which are created by the checker process
     *        or an empty array if no problems has been detected.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    abstract public void notifyModelCheckerResult(ModelCheckerMessage[] mcms);

}