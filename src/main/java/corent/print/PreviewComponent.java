/*
 * PrintPreview.java
 *
 * 10.08.2003
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.print;


import java.awt.*;


/**
 * Diese Klasse dient zum Malen eines Printpreviews.
 * <BR><HR>
 *
 * @author O.Lieshoff
 * 
 */

public class PreviewComponent extends Component {
    
    /** Die aktuelle Seite, die in der Component angezeigt werden soll <I>(Default 1)</I>. */
    protected int page = 1;
    /** Referenz auf das angezeigte Previewable. */
    protected Previewable pv = null;
    
    /** 
     * Generiert eine neue ViewComponent anhand der &uuml;bergebenen Parameter.
     *
     * @param pv Das Previewable, das in der Komponente ausgegeben werden soll.
     */
    public PreviewComponent(Previewable pv, Dimension dim) {
        super();
        this.setBackground(Color.white);
        this.setSize(new Dimension(dim));
        this.pv = pv;        
    }
            
    /** @return Justierung der bevorzugten Anzeigegr&ouml;szlig;e. */
    public Dimension getPreferredSize() {
        return new Dimension(this.getSize());
    }
    
    /**
     * Accessor f&uuml;r die Eigenschaft Page
     *
     * @return Der Wert der Eigenschaft Page
     */
    public int getPage() {
        return this.page;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Page
     *
     * @param page Der neue Wert f&uuml;r die Eigenschaft Page.
     */
    public void setPage(int page) {
        this.page = page;
    }

    
    /** Diese Methode ist f&uumL;r das eigentliche Malen verantwortlich. */
    public void paint(Graphics g) {
        if ((this.pv != null) && (g != null)) {
            this.pv.paint(g, this.page);
        }
    }
    
}
