/*
 * AbstractCOMassiveListSelector.java
 *
 * 03.12.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;


/**
 * Bei dieser Klasse handelt es sich um einen AbstractMassiveListSelector mit 
 * ContextOwner-Implementierung.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 31.03.2008 - Umstellung auf das Interface <TT>Ressourced</TT>.
 *
 */
 
abstract public class AbstractCOMassiveListSelector extends AbstractMassiveListSelector 
        implements Ressourced {
    
    private RessourceManager rm = new PropertyRessourceManager(); 
    
    /* Der Context zur Komponente. */
    private String context = null;
    
    /** 
     * Generiert einen AbstractCOMassiveListSelector anhand der angegebenen Parameter.
     *
     * @param mlscf Die MassiveListSelectorComponentFactory, aus der die Komponente ihre 
     *     Bestandteile beziehen soll.
     * @param selected Ein gegebenenfalls vorausgew&auml;hltes Objekt.
     * @param c Der Context-Name zum AbstractCOMassiveListSelector.
     */
    public AbstractCOMassiveListSelector(MassiveListSelectorComponentFactory mlscf, 
            Object selected, String c) {
        super(mlscf, selected);
        this.setContext(c);
    }
    

    /* Implementierung des Interfaces Ressourced. */
    
    public String getContext() {
        return this.context;
    }
    
    public void setContext(String n) {
        this.context = n;
    }
    
    /**
     * @changed
     *     OLI 31.03.2008 - Hinzugef&uuml;gt.
     *
     */
    public void update(RessourceManager rm) {
        Component[] comp = this.getComponents();
        for (int i = 0; i < comp.length; i++) {
            COUtil.Update(comp[i], this.getContext(), this.rm);
        }
        this.doUpdateDisplay();
    }
    
}
