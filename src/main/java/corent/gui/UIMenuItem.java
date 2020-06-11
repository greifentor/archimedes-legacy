/*
 * UIMenuItem.java
 *
 * 28.01.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;

import javax.swing.*;


/**
 * Mit Hilfe dieser Klasse wird das JMenuItem in der Art spezialisiert, da&szlig; es zur Anzeige
 * und Auswahl von Look &amp; Feels geeignet ist. Jedes Item kennt den Namen der Klasse des Look
 * &amp; Feels, das es repr&auml;sentiert und f&uuml;hrt eine Referenz auf das Wurzelfenster der
 * Applikation mit. Auf diese Weise ist eine Instanzierung des gew&auml;hlten Look &amp; Feels 
 * und das Anwenden desselben auf die Applikation einfach zu programmieren. 
 *
 * @author O.Lieshoff
 *
 */

public class UIMenuItem extends JMenuItem {
    
    private String lnfclassname = "";
    private Window rootwindow = null;
    
    /**
     * Generiert ein UIMenuItem anhand der &uuml;bergebenen Parametern.
     *
     * @param text Der Text, der auf dem MenuItem erscheinen soll.
     * @param lnfclassname Der Name der Look&amp;Feel-Klasse, die durch das MenuItem 
     *     repr&auml;sentiert werden soll.
     * @param w Das Window, das als Wurzel f&uuml;r die Aktualisierung der dienen soll.
     */
     public UIMenuItem(String text, String lnfclassname, Window w) {
         super(text);
         this.setLookAndFeelClassname(lnfclassname);
         this.setRootwindow(w);
     }
     
     /**
      * Liefert den Wert der Eigenschaft lnfclassname.
      * 
      * @return Der Name der Look&amp;Feel-Klasse, mit der das UIMenuItem. 
      */
     public String getLookAndFeelClassname() {
         return this.lnfclassname;
     }
     
     /**
      * Setzt die Eigenschaft lnfclassname auf den angegebenen Wert. 
      * 
      * @param lnfclassname Der neue Wert der Eigenschaft lnfclassname.
      */
     public void setLookAndFeelClassname(String lnfclassname) {
         this.lnfclassname = lnfclassname;
     }
     
     /**
      * Liefert den Wert der Eigenschaft rootwindow.
      * 
      * @return Der Name der Look&amp;Feel-Klasse, mit der das UIMenuItem. 
      */
     public Window getRootwindow() {
         return this.rootwindow;
     }
     
     /**
      * Setzt die Eigenschaft rootwindow auf den angegebenen Wert. 
      * 
      * @param rootwindow Der neue Wert der Eigenschaft rootwindow.
      */
     public void setRootwindow(Window rootwindow) {
         this.rootwindow = rootwindow;
     }
     
}
