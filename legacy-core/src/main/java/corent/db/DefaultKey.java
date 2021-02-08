/*
 * DefaultKey.java
 *
 * 10.08.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


/**
 * Diese Implementierung des Key-Interfaces bietet ein einfaches Konstrukt an, da&szlig; als 
 * Schl&uuml;ssel-Objekt genutzt werden kann.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class DefaultKey implements Key {
    
    /* Hier werden die Schl&uuml;sselattribute untergebracht. */
    private Integer[] attribute = null;
    /* Der Besitzer des Keys. */
    private Persistent owner = null;
    
    /** 
     * Generiert einen Schl&uuml;ssel mit den &uuml;bergebenen Werten.
     *
     * @param owner Der Besitzer des Keys.
     */
    public DefaultKey(Persistent owner, Integer[] attributes) {
        super();
    }
    
    /** @return Die Liste der Attribute, die dem Schl&uuml;ssel angeh&ouml;ren. */
    public Integer[] getAttributes() {
        Integer[] ia = new Integer[this.attribute.length];
        for (int i = 0; i < this.attribute.length; i++) {
            ia[i] = this.attribute[i];
        }
        return ia;
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse (Pflicht). */
    
    public boolean equals(Object o) {
        if (!(o instanceof DefaultKey)) {
            return false;
        }
        DefaultKey dk = (DefaultKey) o;
        return this.attribute.equals(dk.attribute);
    }
    
    public int hashCode() {
        return this.attribute.hashCode();
    }
    
    public String toString() {
        return this.attribute.toString();
    }
        
    
    /* Implementierung des Interfaces Key. */

    public String toSQL() {
        String s = null;
        if (this.attribute != null) {
            boolean ok = false;
            StringBuffer sb = new StringBuffer();
            for (Integer i : this.attribute) {
                ColumnRecord cr = this.owner.getPersistenceDescriptor().getColumn(i.intValue());
                Object obj = this.owner.get(i.intValue());
                if (obj != null) {
                    ok = true;
                    if (sb.length() > 0) {
                        sb.append(" and ");
                    }
                    sb.append(cr.getFullname()).append("=");
                    if (obj instanceof Number) {
                        sb.append(obj.toString());
                    } else {
                        sb.append("'").append(obj.toString()).append("'");
                    }
                }
            }
            if (ok) {
                s = sb.toString();
            }
        }
        return s;
    }
    
}
