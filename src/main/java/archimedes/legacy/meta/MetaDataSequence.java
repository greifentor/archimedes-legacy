/*
 * MetaDataSequence.java
 *
 * 10.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;


import org.apache.commons.lang3.builder.*;


/**
 * A representation of the sequence meta data.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 10.12.2015 - Added.
 */

public class MetaDataSequence extends MetaDataNamedObject {

    private long incrementBy = -1;
    private long startValue = -1;

    /**
     * Creates a new meta data for a sequence based on the passed data.
     *
     * @param name The name of the sequence.
     * @param startValue The start value for the sequence.
     * @param incrementBy The value which the sequence should be incremented. 
     *
     * @changed OLI 10.12.2015 - Added.
     */
    public MetaDataSequence(String name, long startValue, long incrementBy) {
        super(name);
        this.incrementBy = incrementBy;
        this.startValue = startValue;
    }

    /**
     * @changed OLI 14.12.2015 - Added.
     */
    @Override public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
    }

    /**
     * Returns the increment value for the sequence.
     *
     * @return The increment value for the sequence.
     *
     * @changed OLI 10.12.2015 - Added.
     */
    public long getIncrementBy() {
        return this.incrementBy;
    }

    /**
     * Returns the start value of the sequence.
     *
     * @return The start value of the sequence.
     *
     * @changed OLI 10.12.2015 - Added.
     */
    public long getStartValue() {
        return this.startValue;
    }

    /**
     * @changed OLI 14.12.2015 - Added.
     */
    @Override public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    /**
     * @changed OLI 14.12.2015 - Added.
     */
    @Override public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}