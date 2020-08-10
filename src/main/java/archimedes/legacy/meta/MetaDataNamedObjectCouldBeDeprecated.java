/*
 * MetaDataNamedObjectCouldBeDeprecated.java
 *
 * 16.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;


/**
 * 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 16.12.2015 - Added.
 */

public class MetaDataNamedObjectCouldBeDeprecated extends MetaDataNamedObject
        implements CouldBeDeprecated {

    private boolean isDeprecated = false;

    /**
     * Creates a new meta data named object which could be marked as deprecated.
     *
     * @param name The name of the object.
     * @param isDeprecated Set this flag, if the object should be marked as deprecated.
     *
     * @changed OLI 16.12.2015 - Added.
     */
    public MetaDataNamedObjectCouldBeDeprecated(String name, boolean isDeprecated) {
        super(name);
        this.setDeprecated(isDeprecated);
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    @Override public boolean isDeprecated() {
        return this.isDeprecated;
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    @Override public void setDeprecated(boolean isDeprecated) {
        this.isDeprecated = isDeprecated;
    }

}