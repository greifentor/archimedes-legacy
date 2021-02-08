/*
 * OrderClauseDirection.java
 *
 * 17.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


import java.io.*;


/**
 * Mit Hilfe der Bezeichner dieses enum-Typen k&ouml;nnen die Sortierungsrichtungen einer 
 * Order-By-Klausel festgelegt werden.
 *
 * @author O.Lieshoff
 *
 */
 
public enum OrderClauseDirection implements Serializable {
    
    ASC,
    DESC
    
}
