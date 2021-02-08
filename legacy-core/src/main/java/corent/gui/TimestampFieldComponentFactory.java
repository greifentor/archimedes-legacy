/*
 * TimestampFieldFactory.java
 *
 * 14.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import corent.dates.*;

import javax.swing.*;


/**
 * Dieses Interface definiert das Verhalten einer Factory, die ein TimestampField mit den 
 * n&ouml;tigen Komponenten f&uuml;r die Benutzeroberfl&auml;che versorgt.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 24.02.2009 - Erweiterung um die Methoden zur Erstellung der Spezifikation der 
 *             Methoden zum Erstellen der Buttons zum Verschieben des Tagesdatums um einen Tag 
 *             nach vorn bzw. hinten.
 *     <P>
 *
 */
 
public interface TimestampFieldComponentFactory {
    
    /** @return Ein zum TimestampField geh&ouml;render Label. */
    public JLabel createLabel();
    
    /** @return Das Mnemonic zum Label. */
    public char getMnemonic();

    /** @return Das zum TimestampField geh&ouml;rendes TextField. */
    public JTextField createTextField();
    
    /** @return Der Datums-Button zum TimestampField. */
    public JButton createButtonDatum();
    
    /**
     * Liefert einen Button zum Verschieben des Tagesdatum um einen Tag nach hinten 
     * (zur&uuml;ck).
     *
     * @return Der Tag-Zur&uuml;ck-Button zum TimestampField.
     *
     * @changed
     *     OLI 24.02.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public JButton createButtonTagMinus();
    
    /**
     * Liefert einen Button zum Verschieben des Tagesdatum um einen Tag nach vorn.
     *
     * @return Der Tag-Vor-Button zum TimestampField.
     *
     * @changed
     *     OLI 24.02.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public JButton createButtonTagPlus();
    
    /** @return Der Uhrzeit-Button zum TimestampField. */
    public JButton createButtonUhrzeit();
    
    /** @return Der Clear-Button zum TimestampField. */
    public JButton createButtonClear();
    
    /** 
     * Generiert einen Dialog zur Eingabe und/oder &Auml;nderung der Uhrzeit.
     * 
     * @param pt Das TimestampModel, dessen Uhrzeit als Initialwert f&uuml;r die Anzeige genutzt 
     *     werden soll.
     * @return Eine Factory f&uuml;r die Komponenten des zum TimestampField geh&ouml;renden 
     *     Uhrzeit-Dialoges.
     */
    public TimeInputDialog createTimeInputDialog(TimestampModel pt);
    
    /**
     * @return <TT>false</TT>, wenn der Benutzer die Uhrzeit weder angezeigt bekommen, noch
     *     editieren k&ouml;nnen soll.
     */
    public boolean isTimeEnabled();
    
    /**
     * @return <TT>false</TT>, wenn der Benutzer das Datum weder angezeigt bekommen, noch
     *     editieren k&ouml;nnen soll.
     */
    public boolean isDateEnabled();
    
}
