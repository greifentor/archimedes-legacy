/*
 * ReferenceWeight.java
 *
 * 07.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.legacy.model;


/**
 * Diese Enumeration bietet Bezeichner f&uuml;r die Bestimmung der in einer Referenz zu 
 * erwartenden Anzahl an Auswahlm&ouml;glichkeiten an.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public enum ReferenceWeight {
    
    NONE {
        public String toString() {
            return "keine";
        }
    },
    SMALL {
        public String toString() {
            return "wenige";
        }
    },
    MASSIVE {
        public String toString() {
            return "massiv";
        }
    };

    public static ReferenceWeight valueOf2(String s) {
        for (int i = 0; i < ReferenceWeight.values().length; i++) {
            ReferenceWeight rw = ReferenceWeight.values()[i];
            if (rw.toString().equals(s)) {
                return rw;
            }
        }
        throw new IllegalArgumentException("value not found: " + s);
    }
    
}
