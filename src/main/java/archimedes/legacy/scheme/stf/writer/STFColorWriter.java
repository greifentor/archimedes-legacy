/*
 * STFColorWriter.java
 *
 * 29.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;


import archimedes.legacy.scheme.stf.handler.*;

import corent.files.*;
import corent.gui.*;

import org.apache.log4j.*;


/**
 * A writer for colors to STF.
 *
 * @author ollie
 *
 * @changed OLI 29.04.2013 - Added.
 */

public class STFColorWriter extends STFColorHandler {

    private static final Logger LOG = Logger.getLogger(STFColorWriter.class);

    /**
     * Writes the passed domains to the STF.
     *
     * @param colors The colors which are to store in the passed STF.
     * @param stf The STF which is to update with the domain data.
     *
     * @changed OLI 23.04.2013 - Added.
     */
    public void write(StructuredTextFile stf, ExtendedColor[] colors) {
        stf.writeLong(this.createPath(COUNT), colors.length);
        for (int i = 0, len = colors.length; i < len; i++) {
            stf.writeStr(this.createPath(i, NAME), toHTML(colors[i].toString()));
            stf.writeLong(this.createPath(i, R), colors[i].getRed());
            stf.writeLong(this.createPath(i, G), colors[i].getGreen());
            stf.writeLong(this.createPath(i, B), colors[i].getBlue());
            LOG.debug("color written: " + colors[i].name);
        }
    }

}