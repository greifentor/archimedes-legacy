/*
 * ModelCheckerMessage.java
 *
 * 18.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.checker;


import static corentx.util.Checks.*;

import corentx.annotations.*;


/**
 * A message container for messages of the model check-up.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 18.05.2016 - Added.
 */

@Immutable public class ModelCheckerMessage {

    /** Identifiers for model checker message levels. */
    public enum Level {
        ERROR,
        WARNING,
        INFO;
    }

    private Level level = null;
    private String message = null;
    private Object object = null;

    /**
     * Creates a new model checker message with the passed parameters.
     *
     * @param level The level of the message.
     * @param message The content of the message.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public ModelCheckerMessage(Level level, String message) {
        this(level,  message, null);
    }

    /**
     * Creates a new model checker message with the passed parameters.
     *
     * @param level The level of the message.
     * @param message The content of the message.
     * @param object The object which caused the problem.
     * @throws IllegalArgumentException Passing a null pointer as level or message.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public ModelCheckerMessage(Level level, String message, Object object) {
        super();
        ensure(level != null, "level cannot be null.");
        ensure(message != null, "message content cannot be null.");
        this.level = level;
        this.message = message;
        this.object = object;
    }

    /**
     * Returns the level of the message.
     *
     * @return The level of the message.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public Level getLevel() {
        return this.level;
    }

    /**
     * Returns the content of the message.
     *
     * @return The content of the message.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Returns the object which caused the problem.
     *
     * @return The object which caused the problem.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public Object getObject() {
        return this.object;
    }

}