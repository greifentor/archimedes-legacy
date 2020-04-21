/*
 * Reconstructable.java
 *
 * 23.05.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


import corent.dates.*; 


/**
 * Dieses Interface mu&szlig; von Klassen implementiert werden, die sich im Konzept der 
 * ReconstructableDBFactory (Archimede) bewegen sollen.
 *
 * @author O.Lieshoff
 *
 * @changed
 *     OLI 21.01.2008 - Erweiterung um die Spezifikationen der Methoden <TT>isDeleted()</TT> und
 *             <TT>setDeleted(boolean b)</TT>.<BR>
 *     OLI 23.01.2008 - Umzug aus dem package <TT>archimedes.app</TT>.<BR>
 * 
 */
 
public interface Reconstructable {
    
    public long getId();
    
    public LongPTimestamp getModificationOf();
    
    public long getObjectnumber();
    
    public LongPTimestamp getTimestamp();
    
    public void setModificationOf(LongPTimestamp pts);
    
    public void setModifiedAt(LongPTimestamp pts);
    
    public void setObjectnumber(long r);
    
    public void setTimestamp(LongPTimestamp pts);
        
}
