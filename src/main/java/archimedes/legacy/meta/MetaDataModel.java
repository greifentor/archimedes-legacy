/*
 * MetaDataModel.java
 *
 * 04.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import java.util.*;

import corentx.util.*;


/**
 * A representation of the meta data model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.12.2015 - Added.
 */

public class MetaDataModel {

    private String schemeName = null;
    @Deprecated private List<MetaDataIndex> indices = new SortedVector<MetaDataIndex>();
    private List<MetaDataSequence> sequences = new SortedVector<MetaDataSequence>();
    private List<MetaDataTable> tables = new SortedVector<MetaDataTable>();
    private String temporaryPrefix = null;

    /**
     * Creates a new meta data model with the passed parameter.
     *
     * @param schemeName The name of the data scheme.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataModel(String schemeName) {
        this(schemeName, null);
    }

    /**
     * Creates a new meta data model with the passed parameter.
     *
     * @param schemeName The name of the data scheme.
     * @param temporaryPrefix A prefix for temporary data tables if the temporary update mode is
     *         required (otherwise set a <CODE>null</CODE> pointer here).  
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataModel(String schemeName, String temporaryPrefix) {
        super();
        this.schemeName = schemeName;
        this.temporaryPrefix = temporaryPrefix;
    }

    /**
     * Adds the passed index to the model.
     *
     * @param index The index to add.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    @Deprecated public void addIndex(MetaDataIndex index) {
        if (!this.indices.add(index)) {
            this.indices.add(index);
        }
    }

    /**
     * Adds the passed sequence to the model.
     *
     * @param sequence The sequence to add.
     *
     * @changed OLI 10.12.2015 - Added.
     */
    public void addSequence(MetaDataSequence sequence) {
        if (!this.sequences.add(sequence)) {
            this.sequences.add(sequence);
        }
    }

    /**
     * Adds the passed table to the model.
     *
     * @param table The table to add to the model.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public void addTable(MetaDataTable table) {
        if (!this.tables.contains(table)) {
            this.tables.add(table);
        }
    }

    /**
     * Returns the indices of the scheme.
     *
     * @return The indices of the scheme.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    @Deprecated public MetaDataIndex[] getIndices() {
        return this.indices.toArray(new MetaDataIndex[0]);
    }

    /**
     * Returns the name of the scheme.
     *
     * @return The name of the scheme.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public String getSchemeName() {
        return this.schemeName;
    }

    /**
     * Returns the sequence of the data model with the passed name.
     *
     * @param name The name of the sequence which should be returned, or <CODE>null</CODE> if
     *         there is no sequence with the passed name.
     * @return The sequence of the data model with the passed name.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public MetaDataSequence getSequence(String name) {
        for (MetaDataSequence s : this.sequences) {
            if (s.getName().equalsIgnoreCase(name)) {
                if ((this.getTemporaryPrefix() == null) || (s.getName().startsWith(
                        this.getTemporaryPrefix()))) {
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * Returns the sequences of the scheme.
     *
     * @return The sequences of the scheme.
     *
     * @changed OLI 10.12.2015 - Added.
     */
    public MetaDataSequence[] getSequences() {
        if (this.getTemporaryPrefix() != null) {
            List<MetaDataSequence> l = new LinkedList<MetaDataSequence>();
            for (MetaDataSequence mds : this.sequences) {
                if (mds.getName().startsWith(this.getTemporaryPrefix())) {
                    l.add(mds);
                }
            }
            return l.toArray(new MetaDataSequence[0]);
        }
        return this.sequences.toArray(new MetaDataSequence[0]);
    }

    /**
     * Returns the table of the data model with the passed name.
     *
     * @param name The name of the table which should be returned, or <CODE>null</CODE> if there
     *         is no table with the passed name.
     * @return The table of the data model with the passed name.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataTable getTable(String name) {
        for (MetaDataTable t : this.tables) {
            if (t.getName().equalsIgnoreCase(name)) {
                if ((this.getTemporaryPrefix() == null) || (t.getName().startsWith(
                        this.getTemporaryPrefix()))) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * Returns the tables of the data model.
     *
     * @return The tables of the data model.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataTable[] getTables() {
        if (this.getTemporaryPrefix() != null) {
            List<MetaDataTable> l = new LinkedList<MetaDataTable>();
            for (MetaDataTable mdt : this.tables) {
                if (mdt.getName().startsWith(this.getTemporaryPrefix())) {
                    l.add(mdt);
                }
            }
            return l.toArray(new MetaDataTable[0]);
        }
        return this.tables.toArray(new MetaDataTable[0]);
    }

    /**
     * Returns the temporary prefix for the meta model if there is one (otherwise a
     * <CODE>null</CODE> pointer is returned.
     *
     * @return The temporary prefix for the meta model if there is one (otherwise a
     *         <CODE>null</CODE> pointer is returned.
     *
     * @changed OLI 13.06.2017 - Added.
     */
    public String getTemporaryPrefix() {
        return this.temporaryPrefix;
    }

    /**
     * Sets the passed value as new value for the temporary prefix.
     *
     * @param temporaryPrefix The new for the temporary prefix. 
     *
     * @changed OLI 13.06.2017 - Added.
     */
    public void setTemporaryPrefix(String temporaryPrefix) {
        this.temporaryPrefix = temporaryPrefix;
    }

}