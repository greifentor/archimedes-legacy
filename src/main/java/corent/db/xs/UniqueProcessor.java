/*
 * UniqueProcessor.java
 *
 * 08.07.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


import corent.dates.*;
import corent.db.*;

import java.util.*;


/**
 * Diese Klasse bietet eine Funktion, die aus einem Einzigartigkeits-Ausdruck aus einem 
 * PersistenceDescriptor eine SQL-Where-Klausel macht. Der Einzigartigkeitsausdruck mu&szlig;
 * nach folgender EBNF gebildet sein:
 * <PRE>
 * ausdruck = orausdruck (',' orausdruck)
 * orausdruck = spaltenname ('|' orausdruck | andausdruck) | andausdruck
 * andausdruck = spaltenname ('&' andausdruck)
 * spaltenname = zeichen (zeichen)
 * zeichen = '0'..'9' | 'a'..'z' | 'A'..'Z' | '_'
 * </PRE>
 *
 * @author O.Lieshoff
 *
 */
 
public class UniqueProcessor {
    
    /* Die Formel, nach der die SQL-Where-Klausel erzeugt werden soll. */
    private String formula = null;
    
    /**
     * Generiert einen UniqueProcessor anhand der &uuml;bergebenen Parameter.
     *
     * @param formula Die in eine SQL-Where-Klausel umzusetzende Formel.
     */
    public UniqueProcessor(String formula) {
        super();
        this.formula = formula.replace(" ", "");
    }
    
    private String spaltenname(Stack<Character> st) {
        String erg = "";
        while (!st.empty() && (st.peek() == ' ')) {
            st.pop();
        }
        while (!st.empty() && (((st.peek() >= '0') && (st.peek() <= '9')) || ((st.peek() >= 'a')
                && (st.peek() <= 'z')) || ((st.peek() >= 'A') && (st.peek() <= 'Z')) || 
                (st.peek() == '_'))) {
            erg = erg.concat(st.pop().toString());
        }      
        return erg;
    }
    
    private boolean nextOperator(Stack<Character> st, char op) {
        while (!st.empty() && (st.peek() == ' ')) {
            st.pop();
        }
        if (!st.empty() && (st.peek() == op)) {
            return true;
        }
        return false;
    }
    
    private String andausdruck(Stack<Character> st, Hashtable<String, Object> attributes) {
        String erg = this.spaltenname(st);
        Object value = attributes.get(erg);
        if (value != null) {
            if (value instanceof Boolean) {
                boolean b = ((Boolean) value).booleanValue();
                erg = erg.concat("=").concat((b ? "1" : "0"));
            } else if (value instanceof Number) {
                erg = erg.concat("=").concat(value.toString());
            } else if (value instanceof PDate) {
                erg = erg.concat("=").concat("" + ((PDate) value).toInt());
            } else if (value instanceof PTime) {
                erg = erg.concat("=").concat("" + ((PTime) value).toInt());
            } else if (value instanceof PTimestamp) {
                erg = erg.concat("=").concat("" + ((PTimestamp) value).toLong());
            } else if (value instanceof LongPTimestamp) {
                erg = erg.concat("=").concat("" + ((LongPTimestamp) value).toLong());
            } else {
                erg = erg.concat(" like ").concat(DBUtil.DBString(value.toString()));
            }
        } else {
            System.out.println("WARNING: value in UniqueProcessor.andausdruck(Stack, "
                    + "Hashtable) is null!");
            erg = erg.concat(" is null");
        }
        if (this.nextOperator(st, '&')) {
            st.pop();
            erg = erg.concat(" and ").concat(this.andausdruck(st, attributes));
        }        
        return erg;
    }
    
    private String ausdruck(Stack<Character> st, Hashtable<String, Object> attributes) {
        String erg = "";
        if (st.peek() == '(') {
            st.pop();       
            erg = "(".concat(this.ausdruck(st, attributes)).concat(")");
            while (!st.empty() && (st.peek() == ')')) {
                st.pop();
            }
            st.pop();      
        } else {
            erg = this.spaltenname(st);
            Object value = attributes.get(erg);
            if (value != null) {
                if (value instanceof Boolean) {
                    boolean b = ((Boolean) value).booleanValue();
                    erg = erg.concat("=").concat((b ? "1" : "0"));
                } else if (value instanceof Number) {
                    erg = erg.concat("=").concat(value.toString());
                } else if (value instanceof PDate) {
                    erg = erg.concat("=").concat("" + ((PDate) value).toInt());
                } else if (value instanceof PTime) {
                    erg = erg.concat("=").concat("" + ((PTime) value).toInt());
                } else if (value instanceof PTimestamp) {
                    erg = erg.concat("=").concat("" + ((PTimestamp) value).toLong());
                } else if (value instanceof LongPTimestamp) {
                    erg = erg.concat("=").concat("" + ((LongPTimestamp) value).toLong());
                } else {
                    erg = erg.concat(" like ").concat(DBUtil.DBString(value.toString()));
                }
            } else {
                System.out.println("WARNING: value in UniqueProcessor.ausdruck(Stack, "
                        + "Hashtable) is null!");
                erg = erg.concat(" is null");
            }
            if (this.nextOperator(st, '&')) {
                st.pop();
                erg = "(".concat(erg).concat(" and ").concat(this.andausdruck(st, attributes)
                        ).concat(")");
            }
            if (this.nextOperator(st, '|')) {
                st.pop();
                erg = erg.concat(" or ").concat(this.ausdruck(st, attributes));
            }
        }       
        return erg;
    }
    
    /**
     * Diese Methode wertet die dem Konstruktor &uuml;bergebene Formel aus und erstellt eine auf
     * das &uuml;bergebene Persistent-Object zugeschittene Unique-Where-Klausel.
     *
     * @param p Das Persistent, zu dem die Unique-Where-Klausel erzeugt werden soll.
     */
    public String evaluate(Persistent p) {
        Stack<Character> st = new Stack<Character>();
        for (int i = this.formula.length()-1; i >= 0; i--) {
            st.push(this.formula.charAt(i));
        }
        Hashtable<String, Object> attributes = new Hashtable<String, Object>(); 
        PersistenceDescriptor desc = p.getPersistenceDescriptor();
        Vector<Integer> attr = desc.getAttributes();
        for (Integer it : attr) {
            ColumnRecord cr = desc.getColumn(it);
            if (cr != null) {
                Object o = p.get(cr.getAttribute());
                if (o != null) {
                    if  ((o instanceof String) && cr.isKodiert()) {
                        if (DBFactoryUtil.CODER == null) {
                            System.out.println("\n\nWARNUNG: Kodierung kann nicht durchgefuehrt"
                                    + " werden wenn DBFactoryUtil.CODER == null in "
                                    + "UniqueProzessor.evaluate(Persistent)!");
                        } else {
                            o = DBFactoryUtil.CODER.encode(o.toString());
                        }
                    }
                    attributes.put(cr.getColumnname(), o);
                }
            }
        }
        return this.ausdruck(st, attributes);
    }
    
}
