/*
 * Version.java
 *
 * 10.10.2017
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf;


import javax.swing.*;


/**
 * A class with the applications version number.
 *
 * @author O.Lieshoff
 *
 * @changed ??? 10.10.2017 - Added.
 */

public class Version {

    public static final Version INSTANCE = new Version();

    /*
     * Generates an instance of the class with default values.
     */
    private Version() {
        super();
    }

    /**
     * Returns the applications version number.
     *
     * @return The applications version number.
     */
    public String getVersion() {
        return "1.11.1";
    }

    @Override public String toString() {
        return this.getVersion();
    }

    public static void main(String[] args) {
        System.out.println("Baccara-ACF version is " + INSTANCE.getVersion());
        new Thread(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, "Version of Baccara-ACF is: "
                        + INSTANCE.getVersion(), "Baccara-ACF version",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();
    }

}
