/*
 * SequenceMetaData.java
 *
 * 23.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.metadata;

import static corentx.util.Checks.*;


/**
 * A class which represents the meta data of a sequence.
 *
 * @author ollie
 *
 * @changed OLI 23.04.2013 - Added.
 */

public class SequenceMetaData {

    private String name = null;

    /**
     * Creates a new meta data for the sequence with the passed parameters.
     *
     * @param name The name of the sequence.
     * @throws IllegalArgumentException Passing an empty or null string.
     *
     * @changed OLI 23.04.2013 - Added.
     */
    public SequenceMetaData(String name) throws IllegalArgumentException {
        super();
        ensure(name != null, "name cannot be null.");
        ensure(!name.isEmpty(), "name cannot be empty.");
        this.name = name;
    }

    /**
     * Returns the name of the sequence.
     *
     * @return The name of the sequence.
     *
     * @changed OLI 23.04.2013 - Added.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @changed OLI 23.04.2013 - Added.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof SequenceMetaData)) {
            return false;
        }
        SequenceMetaData co = (SequenceMetaData) o;
        return this.getName().equals(co.getName());
    }

    /**
     * @changed OLI 23.04.2013 - Added.
     */
    @Override public int hashCode() {
        int result = 31 + this.getName().hashCode();
        return result;
    }

    /**
     * @changed OLI 23.04.2013 - Added.
     */
    @Override public String toString() {
        return "Name=" + this.getName();
    }

}