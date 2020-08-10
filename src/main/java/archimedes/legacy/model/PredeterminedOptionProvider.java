/*
 * PredeterminedOptionProvider.java
 *
 * 01.10.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * An interface for classes which provide predetermined options.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.10.2015 - Added.
 */

public interface PredeterminedOptionProvider {

    /**
     * Returns an array with selectable options.
     *
     * @param optionType The option type which the selectable options a to return for.
     * @return An array with selectable options.
     *
     * @changed OLI 01.10.2015 - Added.
     */
    abstract public String[] getSelectableOptions(OptionType optionType);

}