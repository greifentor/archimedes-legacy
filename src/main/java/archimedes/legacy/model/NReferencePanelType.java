/*
 * NReferencePanelType.java
 *
 * 10.06.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.legacy.model;


import corent.base.*;


/**
 * Diese Enumeration bietet Bezeichner f&uuml;r die Bestimmung NReferencePanelTypen.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public enum NReferencePanelType {
    
    STANDARD {
        public String toString() {
            return "standard";
        }
    },
    SELECTABLE {
        public String toString() {
            return StrUtil.FromHTML("ausw&auml;hlbar");
        }
    },
    EDITABLE {
        public String toString() {
            return "editierbar";
        }
    },
    STANDALONE {
        public String toString() {
            return StrUtil.FromHTML("eigenst&auml;ndig");
        }
    };

    public static NReferencePanelType valueOf2(String s) {
        for (int i = 0; i < NReferencePanelType.values().length; i++) {
            NReferencePanelType nrpt = NReferencePanelType.values()[i];
            if (nrpt.toString().equals(s)) {
                return nrpt;
            }
        }
        throw new IllegalArgumentException("value not found: " + s);
    }
    
}
