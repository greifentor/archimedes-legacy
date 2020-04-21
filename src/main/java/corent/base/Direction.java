/*
 * Direction.java
 *
 * 01.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base;


/**
 * Mit Hilfe dieser Implementierung eines typsicheren Enum werden Richtungen repr&auml;sentiert,
 * die in verschiedenen Klassen der corent-Bibliotheken benutzt werden.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public final class Direction {
    
    private final String name;
    
    private Direction(String name) {
        super();
        this.name = name;
    }
    
    public String toString() {
        return this.name;
    }
    
    public static Direction CreateDirection(String n) {
        if (n.equalsIgnoreCase("DOWN")) {
            return DOWN;
        } else if (n.equalsIgnoreCase("LEFT")) {
            return LEFT;
        } else if (n.equalsIgnoreCase("RIGHT")) {
            return RIGHT;
        } else if (n.equalsIgnoreCase("UP")) {
            return UP;
        }
        throw new IllegalArgumentException("There is no Direction for \"" + n + "\"!");
    }
        
    public static final Direction DOWN = new Direction("DOWN");
    public static final Direction LEFT = new Direction("LEFT");
    public static final Direction RIGHT = new Direction("RIGHT");
    public static final Direction UP = new Direction("UP");
    
}
