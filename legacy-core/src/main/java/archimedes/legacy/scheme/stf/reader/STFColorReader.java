/*
 * STFColorReader.java
 *
 * 29.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;


import corent.files.*;
import corent.gui.*;
import archimedes.legacy.*;
import archimedes.legacy.model.*;
import archimedes.legacy.scheme.stf.handler.*;


/**
 * A reader for colors from a STF.
 *
 * @author ollie
 *
 * @changed OLI 29.04.2013 - Added.
 */

public class STFColorReader extends STFColorHandler  {

    /**
     * Updates the colors in the passed data model by the information stored in the STF.
     *
     * @param stf The STF whose colors should be read to the diagram.
     * @param model The diagram model which is to fill with the colors.
     *
     * @changed OLI 29.04.2013 - Added.
     */
    public void read(StructuredTextFile stf, DiagrammModel model) {
        int len = (int) stf.readLong(this.createPath(COUNT), 0);
        for (int i = 0; i < len; i++) {
            String name = fromHTML(stf.readStr(this.createPath(i, NAME), null));
            if (name.length() > 0) {
                int r = (int) stf.readLong(this.createPath(i, R), 0);
                int g = (int) stf.readLong(this.createPath(i, G), 0);
                int b = (int) stf.readLong(this.createPath(i, B), 0);
                Archimedes.PALETTE.set(name, new ExtendedColor(name, r, g, b));
            }
        }
    }

}