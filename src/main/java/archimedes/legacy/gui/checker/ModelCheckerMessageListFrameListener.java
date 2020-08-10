/*
 * ModelCheckerMessageListFrameListener.java
 *
 * 19.05.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.gui.checker;

import static corentx.util.Checks.*;

import archimedes.acf.checker.*;


/**
 * A listener interface to observe model checker message list frames.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.05.2016 - Added.
 */

public interface ModelCheckerMessageListFrameListener {

    public class Event {

        public enum Type {
            CLOSED,
            GENERATE,
            MESSAGE_SELECTED;
        }

        private ModelCheckerMessage message = null;
        private Type type = null;

        public Event(Type type) {
            super();
            ensure(type != null, " type cannot be null.");
            ensure(type != Type.MESSAGE_SELECTED, "constructor cannot be called for messages "
                    + "selection."); 
            this.type = type;
        }

        public Event(ModelCheckerMessage message, Type type) {
            super();
            ensure(message != null, "message cannot be null.");
            ensure(type != null, " type cannot be null.");
            this.message = message;
            this.type = type;
        }

        public ModelCheckerMessage getMessage() {
            return this.message;
        }

        public Type getType() {
            return this.type;
        }

    }

    /**
     * This method will be called if a model checker message list frame throws an event.
     *
     * @param event The event thrown by the model checker message list frame.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    abstract public void eventDetected(Event event);

}