/*
 * ColorUtil.java
 *
 * 18.07.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base;


import java.awt.*;


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
    
    /** 
     * Liefert einen durch die Methode <TT>GetColor(String)</TT> in eine Farbe 
     * r&uuml;ckwandelbaren, sprechenden String, bzw. einen leeren String, wenn die Farbe nicht
     * umgewandelt werden kann.
     *
     * @param col Die Farbe, zu der der Name ermitttelt werden soll.
     * @return Der Name zur Farbe bzw. "".
     */
    public static String GetColorName(Color col) {
        if (col.equals(Color.black)) {
            return "black";
        } else if (col.equals(Color.blue)) {
            return "blue";
        } else if (col.equals(Color.cyan)) {
            return "cyan";
        } else if (col.equals(Color.darkGray)) {
            return "darkGray";
        } else if (col.equals(Color.gray)) {
            return "gray";
        } else if (col.equals(Color.green)) {
            return "green";
        } else if (col.equals(Color.lightGray)) {
            return "lightGray";
        } else if (col.equals(Color.magenta)) {
            return "magenta";
        } else if (col.equals(Color.orange)) {
            return "orange";
        } else if (col.equals(Color.pink)) {
            return "pink";
        } else if (col.equals(Color.red)) {
            return "red";
        } else if (col.equals(Color.white)) {
            return "white";
        } else if (col.equals(Color.yellow)) {
            return "yellow";
        }
        return "";
    }
    
    /**
     * Wandelt den angegebenen String in ein Color-Objekt. Die Farbe kann durch die Methode
     * <TT>GetColorName(Color)</TT> in einen String zur&uuml;ckgewandelt werden. Der Farbenname
     * wird in deutscher Sprache angegeben.
     *
     * @param s Name der Farbe.
     * @return Ein zum Namen passendes Color-Objekt.
     */
    public static Color GetGermanColor(String s) {
        if (s.equalsIgnoreCase("schwarz")) {
            return Color.black;
        } else if (s.equalsIgnoreCase("blau")) {
            return Color.blue;
        } else if (s.equalsIgnoreCase(StrUtil.FromHTML("t&uuml;rkis"))) {
            return Color.cyan;
        } else if (s.equalsIgnoreCase("dunkelgrau")) {
            return Color.darkGray;
        } else if (s.equalsIgnoreCase("grau")) {
            return Color.gray;
        } else if (s.equalsIgnoreCase(StrUtil.FromHTML("gr&uuml;n"))) {
            return Color.green;
        } else if (s.equalsIgnoreCase("hellgrau")) {
            return Color.lightGray;
        } else if (s.equalsIgnoreCase("violett")) {
            return Color.magenta;
        } else if (s.equalsIgnoreCase("orange")) {
            return Color.orange;
        } else if (s.equalsIgnoreCase("pink")) {
            return Color.pink;
        } else if (s.equalsIgnoreCase("rot")) {
            return Color.red;
        } else if (s.equalsIgnoreCase(StrUtil.FromHTML("wei&szlig;"))) {
            return Color.white;
        } else if (s.equalsIgnoreCase("gelb")) {
            return Color.yellow;
        }
        return Color.black;
    }
    
    /** 
     * Liefert einen durch die Methode <TT>GetColor(String)</TT> in eine Farbe 
     * r&uuml;ckwandelbaren, sprechenden String, bzw. einen leeren String, wenn die Farbe nicht
     * umgewandelt werden kann. Der Farbenname wird in deutscher Sprache angegeben.
     *
     * @param col Die Farbe, zu der der Name ermitttelt werden soll.
     * @return Der Name zur Farbe bzw. "".
     */
    public static String GetGermanColorName(Color col) {
        if (col.equals(Color.black)) {
            return "schwarz";
        } else if (col.equals(Color.blue)) {
            return "blau";
        } else if (col.equals(Color.cyan)) {
            return StrUtil.FromHTML("t&uuml;rkis");
        } else if (col.equals(Color.darkGray)) {
            return "dunkelgrau";
        } else if (col.equals(Color.gray)) {
            return "grau";
        } else if (col.equals(Color.green)) {
            return StrUtil.FromHTML("gr&uuml;n");
        } else if (col.equals(Color.lightGray)) {
            return "hellgrau";
        } else if (col.equals(Color.magenta)) {
            return "violett";
        } else if (col.equals(Color.orange)) {
            return "orange";
        } else if (col.equals(Color.pink)) {
            return "pink";
        } else if (col.equals(Color.red)) {
            return "rot";
        } else if (col.equals(Color.white)) {
            return StrUtil.FromHTML("wei&szlig;");
        } else if (col.equals(Color.yellow)) {
            return "gelb";
        }
        return "";
    }
    
}
