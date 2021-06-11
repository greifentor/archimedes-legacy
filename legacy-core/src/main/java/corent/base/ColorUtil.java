/*
 * ColorUtil.java
 *
 * 18.07.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base;


import java.awt.Color;


/**
 * Mit Hilfe der Funktionen dieser Klasse lassen sich Operationen auf Color-Objekten 
 * durchf&uuml;hren, die von der eigentlichen Color-Klasse nicht angeboten werden.
 *
 * @author O.Lieshoff
 *
 */
 
public class ColorUtil {

    /**
     * Wandelt den angegebenen String in ein Color-Objekt. Die Farbe kann durch die Methode
     * <TT>GetColorName(Color)</TT> in einen String zur&uuml;ckgewandelt werden.
     *
     * @param s Name der Farbe.
     * @return Ein zum Namen passendes Color-Objekt.
     */
    public static Color GetColor(String s) {
        if (s.equalsIgnoreCase("black")) {
            return Color.black;
        } else if (s.equalsIgnoreCase("blue")) {
            return Color.blue;
        } else if (s.equalsIgnoreCase("cyan")) {
            return Color.cyan;
        } else if (s.equalsIgnoreCase("darkgray")) {
            return Color.darkGray;
        } else if (s.equalsIgnoreCase("gray")) {
            return Color.gray;
        } else if (s.equalsIgnoreCase("green")) {
            return Color.green;
        } else if (s.equalsIgnoreCase("lightgray")) {
            return Color.lightGray;
        } else if (s.equalsIgnoreCase("magenta")) {
            return Color.magenta;
        } else if (s.equalsIgnoreCase("orange")) {
            return Color.orange;
        } else if (s.equalsIgnoreCase("pink")) {
            return Color.pink;
        } else if (s.equalsIgnoreCase("red")) {
            return Color.red;
        } else if (s.equalsIgnoreCase("white")) {
            return Color.white;
        } else if (s.equalsIgnoreCase("yellow")) {
            return Color.yellow;
        }
        return Color.black;
    }

}