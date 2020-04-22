/*
 * CodeFactoryListener.java
 *
 * 18.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.event;

import archimedes.model.*;

/**
 * A listener interface which allows to monitor the <CODE>CodeFactory</CODE> implementation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public interface CodeFactoryListener {

    /**
     * This method is called if the code factory fires an event.
     *
     * @param event The fired event.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    abstract public void eventFired(CodeFactoryEvent event);

    /**
     * This method will be called if the code factory detects an error.
     *
     * @param cf The code factory which caused the exception.
     * @param e The exception which causes the error.
     *
     * @changed OLI 07.12.2016 - Added.
     */
    abstract public void exceptionDetected(CodeFactory cf, Throwable e);

}