/*
 * ComponentWithInifile.java
 *
 * 01.07.2003
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;


import corent.files.*;


/**
 * Dieses Interface kennzeichnet Komponenten, die ihre visuellen Daten &uuml;ber eine Ini-Datei
 * persistent halten.
 *
 * @author O.Lieshoff
 *
 */

public interface ComponentWithInifile {
    
    /**
     * Setzt die &uuml;bergebene IniDatei als neue IniDatei f&uuml;r die Komponente ein.
     *
     * @param ini Die neue IniDatei.
     */
    public void setInifile(Inifile ini);

    /** @return Eine Referenz auf die IniDatei der Komponente. */
    public Inifile getInifile();

    /**
     * Setzt den &uuml;bergebenen Wert als neuen Namen, unter dem die Daten der Komponente in
     * der IniDatei gespeichert werden.
     *
     * @param identifier Der neue Name, unter dem die Daten der Komponente in der IniDatei
     *     gespeichert werden sollen.
     */
    public void setIdentifier(String identifier);

    /**
     * @return Der Name, unter dem die Daten der Komponente in der IniDatei gespeichert werden.
     *     Ist kein expliziter Name gesetzt, wird der Klassenname der Componente
     *     zur&uuml;ckgeliefert.
     */
    public String getIdentifier();

    /**
     * Wird diese Flagge im Rahmen der gesetzten Option "CoordinatesOnly" gesetzt, so wird das 
     * Fenster anhand der in der Ini-Datei gespeicherten Daten wiederhergestellt. Andernfalls 
     * wird eine bevorzugte Minimalgr&oum;&szlig;e gew&auml;hlt, falls das Fenster zu klein ist,
     * um alle Inhalte anzuzeigen.
     *
     * @param b Wird hier der Wert <TT>true</TT> &uuml;bergeben, wird das Fenster ohne 
     *     R&uuml;cksicht auf Verluste wiederhergestellt.
     */
    public void setBruteRestore(boolean b);

    /**
     * @return <TT>true</TT>, wenn das Fenster vollst&auml;ndig aus den Daten der Ini-Datei
     *     wiederhergestellt werden soll,<BR><TT>false</TT> sonst.
     */
    public boolean isBruteRestore();

    /**
     * Mit Hilfe dieser Methode kann entschieden werden, ob die Komponente nur &uuml;ber ihre
     * Koordinaten restauriert werden soll (die Ausdehnung wird durch ein Aufruf der <TT>pack()
     * </TT>-Methode realisiert), oder komplett anhand der Daten der IniDatei.
     *
     * @param b Wird dieser Parameter mit dem Wert <TT>true</TT> &uuml;bergeben, so werden die
     *     Koordinaten aus der IniDatei gelesen. Die Ausdehnung wird automatisch geregelt.
     *     Andernfalls werden die kompletten Fensterdaten &uuml;bernommen.
     */
    public void setCoordinatesOnly(boolean b);

    /**
     * @return <TT>true</TT>, wenn nur die Koordinaten aus der Datei restauriert werden,<BR>
     *     <TT>false</TT> sonst.
     */
    public boolean isCoordinatesOnly();

}