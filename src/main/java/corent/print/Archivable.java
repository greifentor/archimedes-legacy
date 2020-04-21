/*
 * Archivable.java
 *
 * 21.08.2007
 *
 * (c) by O.Lieshoff
 *
 */

package corent.print;


/**
 * Dieses Interface erweitert das Interface JasperReportable. In Zusammenarbeit mit dem 
 * corent.db.xs.DefaultDBFactoryController k&ouml;nnen Ausdrucke des implementierenden Objektes
 * in einem Archiv hinterlegt werden.
 *
 * @author O.Lieshoff
 *
 */
 
public interface Archivable extends JasperReportable {
    
    public String getArchiveFilename();
    
    public int getArchiveReportnumber();
    
    public ArchiveMode getArchiveMode();
    
}
