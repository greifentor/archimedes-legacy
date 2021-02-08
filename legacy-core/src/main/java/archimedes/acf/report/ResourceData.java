/*
 * ResourceData.java
 *
 * 06.07.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.acf.report;

import static corentx.util.Checks.*;

import org.apache.commons.lang3.builder.*;

/**
 * A container for data of required resources.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 06.07.2016 - Added.
 */

public class ResourceData {

    private String defaultValue = null;
    private String resourceId = null;

    /**
     * Creates a new resource data for the passed parameters.
     *
     * @param resourceId The id of the resource.
     * @param defaultValue A default content for the resource in the standard language.
     * @throws IllegalArgumentException Passing a null pointer or an empty string.
     *
     * @changed OLI 06.07.2016 - Added.
     */
    public ResourceData(String resourceId, String defaultValue) {
        super();
        ensure(defaultValue != null, "default value cannot be null.");
        ensure(!defaultValue.isEmpty(), "default value cannot be empty.");
        ensure(resourceId != null, "resource id cannot be null.");
        ensure(!resourceId.isEmpty(), "resource id cannot be empty.");
        this.defaultValue = defaultValue;
        this.resourceId = resourceId;
    }

    /**
     * @changed OLI 06.07.2016 - Added.
     */
    @Override public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Returns the default value for the resource.
     *
     * @return The default value for the resource.
     *
     * @changed OLI 06.07.2016 - Added.
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Returns the id for the resource.
     *
     * @return The id for the resource.
     *
     * @changed OLI 06.07.2016 - Added.
     */
    public String getResourceId() {
        return this.resourceId;
    }

    /**
     * @changed OLI 06.07.2016 - Added.
     */
    @Override public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}