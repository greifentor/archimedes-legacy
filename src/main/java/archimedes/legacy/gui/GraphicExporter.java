/*
 * GraphicExporter.java
 *
 * 23.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui;

import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

import corent.files.*;


/**
 * A class which is able to export the diagram content to an image file.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.05.2013 - Added.
 */

public class GraphicExporter {

    /**
     * Exports the diagram content to an image file.
     *
     * @param frame The frame which works as parent for the export dialog.
     * @param currentFileName The name of the currently shown diagram file. It is used to
     *         generate a preference for the file name.
     * @param path The path which is opened in the file name chooser dialog.
     * @param component The component whose content is to print.
     *
     * @changed OLI 23.05.2013 - Added.
     */
    public void export(JFrame frame, String currentFileName, String path,
            DiagramComponentPanel component) {
        JFileChooser fc = new JFileChooser(path);
        fc.setAcceptAllFileFilterUsed(false);
        ExtensionFileFilter eff = new ExtensionFileFilter(new String[] {"jpg"});
        fc.setFileFilter(eff);
        fc.setCurrentDirectory(new File(path));
        int lp = currentFileName.lastIndexOf(".");
        String tdn = currentFileName.substring(0, lp-1).concat(".jpg");
        fc.setSelectedFile(new File(tdn));
        if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                FileOutputStream fos = new FileOutputStream(new File(fc.getSelectedFile(
                        ).getAbsolutePath()));
                BufferedImage img = new BufferedImage(component.getImgWidth(),
                        component.getImgHeight(), BufferedImage.TYPE_INT_RGB);
                int rw = component.getRasterWidth();
                component.setRasterWidth(Integer.MAX_VALUE);
                component.paint(img.getGraphics(), false, true);
                component.setRasterWidth(rw);
                ImageIO.write(img, "jpeg", fos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}