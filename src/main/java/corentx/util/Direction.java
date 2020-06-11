/*
 * Direction.java
 *
 * 12.08.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.util;


/**
 * Mit Hilfe dieser Implementierung eines typsicheren Enum werden Richtungen repr&auml;sentiert,
 * die in verschiedenen Klassen der corent-Bibliotheken benutzt werden.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 12.08.2009 - Refactoring auf Basis der corent-Entsprechung.
 *
 * @todo OLI - Bezeichnungen der Richtungsangaben &uuml;ber Properties konfigurierbar machen
 *         (Beispiel: Wochentag).
 *
 */

public enum Direction {

    /** Richtungsangabe nach unten. */
    DOWN("DOWN"),
    /** Richtungsangabe nach links. */
    LEFT("LEFT"),
    /** Richtungsangabe nach rechts. */
    RIGHT("RIGHT"),
    /** Richtungsangabe nach oben. */
    UP("UP");

    private String name = null;

    private Direction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Liefert eine Richtungsangabe zum &uuml;bergebenen String.
     *
     * @param n Eine Richtungsangabe in englischer Sprache (down, left, right, up). Gro&szlig;-
     *         und Kleinschreibung wird ignoriert.
     * @return Das passende Richtungsobjekt.
     * @throws IllegalArgumentException Falls der &uuml;bergebene String keine g&uuml;ltige
     *         Richtungsangabe enth&auml;lt.
     * @throws NullPointerException Falls der String als <TT>null</TT>-Pointer &uuml;bergeben
     *         wird.
     * @precondition n != <TT>null</TT>
     *
     */
    public static Direction getDirection(String n) throws IllegalArgumentException,
            NullPointerException{
        if (n.equalsIgnoreCase("DOWN")) {
            return DOWN;
        } else if (n.equalsIgnoreCase("LEFT")) {
            return LEFT;
        } else if (n.equalsIgnoreCase("RIGHT")) {
            return RIGHT;
        } else if (n.equalsIgnoreCase("UP")) {
            return UP;
        }
        throw new IllegalArgumentException("There is no direction for \"" + n + "\"!");
    }

}
