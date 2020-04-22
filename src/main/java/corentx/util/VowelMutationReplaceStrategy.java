/*
 * VowelMutationReplaceStrategy.java
 *
 * 04.06.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.util;


/**
 * Ein Enum zur Bezeichnung einer Austauschstrategie f&uuml;r Umlaute.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.06.2009 - Hinzugef&uuml;gt
 * @changed OLI 12.08.2009 - &Uuml;bernahme nach corentx.
 *
 */

public enum VowelMutationReplaceStrategy {

    /** Austausch gegen einfache Vokale (&auml; &gt; a, &ouml; &gt; o, &szlig &gt; ss usw.) */
    SIMPLE_REPLACE,

    /** Austausch gegen doppelte Vokale (&auml; &gt; ae, &ouml; &gt; oe, &szlig &gt; ss usw.) */
    TWO_CHAR_REPLACE;

}
