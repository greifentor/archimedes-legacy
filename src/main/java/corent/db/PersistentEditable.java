/*
 * PersistentEditable.java
 *
 * 28.08.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


import corent.djinn.*;


/**
 * Dieses Interface vereint die Definitionen des Editable-Interfaces mit denen des 
 * Persistent-Interfaces, um eine klare Typsicherung bei der Benutzung der EditorDjnns im Verein
 * mit der PersistenceFactory zu erreichen.<BR>
 * <BR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface PersistentEditable extends Editable, Persistent {
}
