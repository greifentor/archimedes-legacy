/*
 * GeneratorResult.java
 *
 * 19.05.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.report;


import static corentx.util.Checks.*;

import corent.dates.*;

import corentx.util.*;

import java.util.*;


/**
 * A container for generator results.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.05.2015 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class GeneratorResult {

    public enum LogLevel {
        INFO,
        WARN;
    }

    private String code = "";
    private GeneratorResultState state = null;
    private List<String> logLines = new LinkedList<String>(); 
    private List<ResourceData> resourceIdsRequired = new LinkedList<ResourceData>();

    /**
     * Creates a new generator result with the passed parameters.
     *
     * @changed OLI 06.07.2015 - Added.
     */
    public GeneratorResult() {
        super();
    }

    /**
     * Creates a new generator result with the passed parameters.
     *
     * @param code The code which is treat as the generator result.
     * @param state The state of the generation process.
     *
     * @changed OLI 19.05.2015 - Added.
     */
    public GeneratorResult(String code, GeneratorResultState state) {
        this();
        this.code = code;
        this.state = state;
    }

    /**
     * Creates a new generator result with the passed parameters.
     *
     * @param code The code which is treat as the generator result.
     * @param state The state of the generation process.
     * @param logLines Some log lines or an empty array if no log lines are to pass.
     * @throws IllegalArgumentException Passing a null pointer for log lines.
     *
     * @changed OLI 19.05.2015 - Added.
     */
    public GeneratorResult(String code, GeneratorResultState state, String[] logLines) {
        this(code, state);
        ensure(logLines != null, "log lines cannot be null.");
        for (String s : logLines) {
            this.logLines.add(s);
        }
    }

    /**
     * Creates a new generator result with the passed parameters.
     *
     * @param code The code which is treat as the generator result.
     * @param state The state of the generation process.
     * @param logLines Some log lines or an empty array if no log lines are to pass.
     * @param resourceIds Some resource id's which should be checked after generation process. 
     * @throws IllegalArgumentException Passing a null pointer for log lines.
     *
     * @changed OLI 07.07.2016 - Added.
     */
    public GeneratorResult(String code, GeneratorResultState state, String[] logLines,
            ResourceData[] resourceIds) {
        this(code, state);
        ensure(logLines != null, "log lines cannot be null.");
        for (String s : logLines) {
            this.logLines.add(s);
        }
        for (ResourceData resourceId : resourceIds) {
            this.addResourceId(resourceId.getResourceId(), resourceId.getDefaultValue());
        }
    }

    /**
     * Adds the passed resource id if not already present in the list.
     *
     * @param resourceId The resource id to add.
     *
     * @changed OLI 06.07.2016 - Added.
     */
    public void addResourceId(ResourceData resourceId) {
        if (!this.resourceIdsRequired.contains(resourceId)) {
            this.resourceIdsRequired.add(resourceId);
        }
    }

    /**
     * Adds the passed resource id if not already present in the list.
     *
     * @param resourceId The resource id to add.
     * @param defaultValue The value in the default language for the resource.
     *
     * @changed OLI 07.07.2016 - Added.
     */
    public void addResourceId(String resourceId, String defaultValue) {
        this.addResourceId(new ResourceData(resourceId, defaultValue));
    }

    /**
     * Returns the code which is generated.
     *
     * @return The code which is generated.
     *
     * @changed OLI 19.05.2015 - Added.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns an array with the required resource id's.
     *
     * @return An array with the required resource id's.
     *
     * @changed OLI 06.07.2016 - Added.
     */
    public ResourceData[] getResourceIds() {
        return this.resourceIdsRequired.toArray(new ResourceData[0]);
    }

    /**
     * Returns the log for the generation process.
     *
     * @return The log for the generation process (empty array if there are no log entries).
     *
     * @changed OLI 06.07.2015 - Added.
     */
    public String[] getLogLines() {
        return this.logLines.toArray(new String[0]);
    }

    /**
     * Returns the state of the generation process.
     *
     * @return The state of the generation process.
     *
     * @changed OLI 19.05.2015 - Added.
     */
    public GeneratorResultState getState() {
        return this.state;
    }

    /**
     * Adds the passed string as a new log line to the result with the passed priority.
     *
     * @param level The log level for the message.
     * @param message The message to log.
     *
     * @changed OLI 06.07.2015 - Added.
     */
    public void log(LogLevel level, String message) {
        this.logLines.add(Str.pumpUp(new LongPTimestamp().toString(), " ", 23, Direction.RIGHT)
                + " [" + level + "]: " + message);
    }

    /**
     * Sets the passed code as the code for the generator result.
     *
     * @param code The new code to set for the generator result.
     *
     * @changed OLI 06.07.2015 - Added.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets the passed state as the state for the generator result.
     *
     * @param state The new state to set for the generator result.
     *
     * @changed OLI 06.07.2015 - Added.
     */
    public void setState(GeneratorResultState state) {
        this.state = state;
    }

}