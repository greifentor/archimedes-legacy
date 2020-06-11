/*
 * DiagramPrinter.java
 *
 * 23.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.gui;


import archimedes.gui.diagram.*;
import archimedes.print.*;

import corent.djinn.*;
import corent.files.*;
import corent.print.*;

import java.awt.*;
import java.util.*;

import javax.print.*;


/**
 * Prints a diagram component content to a printer.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.05.2013 - Added.
 */

public class DiagramPrinter {

    /**
     * Prints the family tree.
     *
     * @param frame The frame which works as parent for the print dialog.
     * @param ini The ini file with preferences for the printing.
     * @param comp The component whose content is to print.
     * @param toPDF The output is printed in a PDF.
     * @param standardPrinterName The name of the standard printer.
     * @param printFileName A name for the file which is to print into in case of printing to a
     *         file.
     *
     * @changed OLI 23.05.2013 - Added.
     */
    public void print(Frame frame, Inifile ini, DiagramComponentPanel comp, boolean toPDF,
            String standardPrinterName, String printFileName) {
        // JDialogWithIniFile.CENTER_NEW = true;
        final boolean pdfprint = toPDF;
        final int dppx = ComponentDiagramm.DOTSPERPAGEX;
        final JobAttributes ja = new JobAttributes();
        String printer = standardPrinterName;
        final String printfilename = printFileName;
        final DiagramComponentPanel component = comp;
        ja.setDialog(JobAttributes.DialogType.COMMON);
        ja.setPrinter(printer);
        ja.setPageRanges(new int[][] {{1, component.getPrintPageCount()}});
        ja.setMinPage(1);
        ja.setMaxPage(component.getPrintPageCount());
        ja.setFromPage(1);
        ja.setToPage(component.getPrintPageCount());
        ja.setDefaultSelection(JobAttributes.DefaultSelectionType.RANGE);
        if (pdfprint) {
            ja.setDestination(JobAttributes.DestinationType.FILE);
            ja.setFileName(printfilename + ".ps");
        }
        final PageAttributes pa = new PageAttributes();
        pa.setColor(PageAttributes.ColorType.COLOR);
        pa.setMedia(PageAttributes.MediaType.A4);
        pa.setPrintQuality(PageAttributes.PrintQualityType.HIGH);
        pa.setPrinterResolution(300);
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("\nGefundene Drucker");
        Vector<String> printers = new Vector<String>();
        for (int i = 0, len = ps.length; i < len; i++) {
            printers.addElement(ps[i].getName());
            System.out.println(ps[i].getName());
        }
        System.out.println();
System.out.println("pages: " + component.getPrintPageCount());
        PrintJobConf pjc = new PrintJobConf(ja.getPrinter(), ja.getCopies(), printers);
        // pjc.edit();
        new DialogEditorDjinn(frame, "Printer", pjc, false, false, ini) {
            @Override public void doChanged(boolean saveOnly) {
                PrintJobConf epjc = (PrintJobConf) this.getEditable();  
                ja.setCopies(epjc.getCopies());
                if (epjc.isA3()) {
                    pa.setMedia(PageAttributes.MediaType.A3);
                    pa.setOrientationRequested(PageAttributes.OrientationRequestedType.LANDSCAPE
                            );
                    ComponentDiagramm.DOTSPERPAGEX = 2100;
                    ja.setPageRanges(new int[][] {{1, component.getPrintPageCount()}});
                    ja.setMinPage(1);
                    ja.setMaxPage(component.getPrintPageCount());
                    ja.setFromPage(1);
                    ja.setToPage(component.getPrintPageCount());
                } else {
                    pa.setMedia(PageAttributes.MediaType.A4);
                    pa.setOrientationRequested(PageAttributes.OrientationRequestedType.PORTRAIT
                            );
                    ComponentDiagramm.DOTSPERPAGEX = 1050;
                    ja.setPageRanges(new int[][] {{1, component.getPrintPageCount()}});
                    ja.setMinPage(1);
                    ja.setMaxPage(component.getPrintPageCount());
                    ja.setFromPage(1);
                    ja.setToPage(component.getPrintPageCount());
                }
                ja.setPrinter(epjc.getPrintername());
                try {
                    CorePrintJob cpj = new CPJDiagramm(ja, pa, component);
                    if (cpj.preview()) {
                        cpj.print();
                        if (pdfprint) {
                            Process p = Runtime.getRuntime().exec("ps2pdf " + printfilename 
                                    + ".ps");
                            p.waitFor();
                            p = Runtime.getRuntime().exec("acroread " + printfilename + ".pdf");
                            p.waitFor();
                        }
                    }
                    ((CPJDiagramm) cpj).release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ComponentDiagramm.DOTSPERPAGEX = dppx;
                component.repaint();
            }
        };
        System.gc();
    }

}