/*
 * DefaultListViewComponentFactory.java
 *
 * 18.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.base.*;

import java.util.*;


/**
 * Eine Musterimplementierung der ViewComponentFactory auf Basis einer JList zur Abdeckung von
 * Standardf&auml;llen.
 *
 * <P><H3>Properties:</H3>
 * <TABLE BORDER=1>
 *     <TR VALIGN=TOP>
 *         <TH>Property</TH>
 *         <TH>Default</TH>
 *         <TH>Typ</TH>
 *         <TH>Beschreibung</TH>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.djinn.ViewComponentFactory.records.none.found</TD>
 *         <TD>Es wurden keine Datens&auml;tze gefunden!</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Ein Text zur Anzeige, wenn keine Datens&auml;tze gefunden werden konnten.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.djinn.ViewComponentFactory.records.in.limit</TD>
 *         <TD>Es wurden %d Datens&auml;tze gefunden</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Ein Text zur Anzeige, wenn eine Anzahl von Datens&auml;tzen gefunden wurde, die 
 *             sich noch innerhalb eines vorgegebenen Limits bewegt (von der Anzahl her). Die 
 *             Konfiguration solcher Limits erfolgt &uuml;ber in dr Klasse 
 *             <TT>DBFListViewComponentFactory</TT> beschriebene Properties.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.djinn.ViewComponentFactory.records.off.limit</TD>
 *         <TD>Das Limit von %d Datens&auml;tzen wurde &uuml;berschritten!</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Ein Text zur Anzeige, wenn eine Anzahl von Datens&auml;tzen gefunden wurde, die 
 *             sich au&szlig;erhalb eines vorgegebenen Limits bewegt (von der Anzahl her). Die 
 *             Konfiguration solcher Limits erfolgt &uuml;ber in dr Klasse 
 *             <TT>DBFListViewComponentFactory</TT> beschriebene Properties.
 *         </TD>
 *     </TR>
 * </TABLE>
 * <P>&nbsp;
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 05.06.2008 - Umstellung der Konfiguration der Limit-String auf Properties.
 *     <P>
 *
 */
 
public class DefaultListViewComponentFactory implements ViewComponentFactory {
    
    /* Referenz auf den Vector, dessen Inhalt die generierte ViewComponent anzeigen soll. */
    private Vector view = null;
    
    /**
     * Generiert eine Factory anhand der &uuml;bergebenen Parameter.
     *
     * @param view Die Liste, deren Inhalt in den produzierten Views angezeigt werden soll.
     */
    public DefaultListViewComponentFactory(Vector view) {
        super();
        this.view = view;
    }
    
    
    /* Implementierung des Interfaces ViewComponentFactory. */
    
    /**
     * @changed
     *     OLI 03.10.2008 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public SelectionComponent createSelectionComponent() {
        return null;
    }
    
    public ViewComponent createViewComponent() {
        return new DefaultListViewComponent(this.view);
    }

    public Class getServedClass() {
        return null;
    }

    /**
     * @changed
     *     OLI 05.06.2008 - Konfiguration des Ausgabetextes auf Property umgestellt.
     *     <P>
     *
     */
    public String getFormatNoRecordsFound() {
        return Utl.GetProperty("corent.djinn.ViewComponentFactory.records.none.found", 
                "Es wurden keine Datens&auml;tze gefunden!");
    }
     
    /**
     * @changed
     *     OLI 05.06.2008 - Konfiguration des Ausgabetextes auf Property umgestellt.
     *     <P>
     *
     */
    public String getFormatRecordCountInLimit() {
        return Utl.GetProperty("corent.djinn.ViewComponentFactory.records.in.limit", 
                "Es wurden %d Datens&auml;tze gefunden");
    }
     
    /**
     * @changed
     *     OLI 05.06.2008 - Konfiguration des Ausgabetextes auf Property umgestellt.
     *     <P>
     *
     */
    public String getFormatRecordCountOffLimit() {
        return Utl.GetProperty("corent.djinn.ViewComponentFactory.records.off.limit",
                "Das Limit von %d Datens&auml;tzen wurde &uuml;berschritten!");
    }
}
