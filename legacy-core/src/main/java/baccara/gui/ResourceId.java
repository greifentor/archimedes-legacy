/*
 * ResourceId.java
 *
 * 18.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui;

import static corentx.util.Checks.*;

/**
 * A container for a resource id.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.07.2016 - Added.
 */

public class ResourceId {

    private String resourceId = null;

    /**
     * Creates a container for a resource id.
     *
     * @param resourceId The resource id which is to store in the container.
     * @throws IllegalArgumentException Passing a <CODE>null</CODE> of empty string.
     *
     * @changed OLI 18.07.2016 - Added.
     */
    public ResourceId(String resourceId) {
        super();
        ensure(resourceId != null, "resource id cannot be null");
        ensure(!resourceId.isEmpty(), "resource id cannot be empty");
        this.resourceId = resourceId;
    }

    /**
     * Returns the resource id.
     *
     * @return The resource id.
     *
     * @changed OLI 18.07.2016 - Added.
     */
    public String getResourceId() {
        return this.resourceId;
    }

    /**
     * Returns the resource id.
     *
     * @param guiBundle An access to the application resources.
     * @return The resource id.
     *
     * @changed OLI 18.07.2016 - Added.
     */
    public String getResourceText(GUIBundle guiBundle) {
        ensure(guiBundle != null, "GUI bundle cannot be null.");
        return guiBundle.getResourceText(this.getResourceId());
    }

}