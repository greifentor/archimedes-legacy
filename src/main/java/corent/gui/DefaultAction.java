/*
 * DefaultAction.java
 *
 * 18.07.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;


import java.awt.event.*;
import javax.swing.*;


/**
 * Diese Klasse erweitert die AbstractAction um eine Grundfunktionalit&auml;t f&uuml;r das 
 * Erzeugen von einfachen ActionEvents zu bieten. Um auf ein Event zu reagieren mu&szlig; die 
 * <TT>doAction()</TT>-Methode &uuml;berschrieben werden.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public class DefaultAction extends AbstractAction {

    /**
     * Das Objekt, das die eigentlichen Methoden liefert, die vom Event aufgerufen werden sollen
     * <I>(Default null)</I>.
     */
    protected Object callee = null;

    /**
     * Generiert eine DefaultAction, der der Methodenlieferanten als Objekt &uuml;bergeben wird.
     *
     * @param callee Das Objekt, dessen Methoden ActionEvent aufgerufen werden sollen.
     */
    public DefaultAction(Object callee) {
        super();
        this.callee = callee;
    }

    /**
     * Diese Methode wird ausgef&uuml;hrt, wenn das aufrufende Event eintritt. Sie mu&szlig; 
     * &uuml;berschrieben werden, wenn eine Reaktion auf das Event vorgesehen ist.
     */
    public void doAction() {
    }


    /* Ueberschreiben von Methoden der Superklasse. */
    
    public void actionPerformed(ActionEvent e) {
        this.doAction();
    }

}