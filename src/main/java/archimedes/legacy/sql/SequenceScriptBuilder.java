/*
 * SequenceScriptBuilder.java
 *
 * 23.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static corentx.util.Checks.ensure;

import java.util.Vector;

import archimedes.legacy.metadata.SequenceMetaData;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.SequenceModel;
import archimedes.legacy.script.sql.SQLScript;

/**
 * A class which add the changes in the sequences to the SQL script an the HTML
 * description.
 * 
 * @author ollie
 * 
 * @changed OLI 23.04.2013 - Added.
 */

public class SequenceScriptBuilder {

	private SQLScriptFactory factory = null;
	private DataModel model = null;
	private SequenceMetaData[] sequences = null;

	/**
	 * Creates a new sequence script builder with the passed parameters.
	 * 
	 * @param mode
	 *            The db mode for which the script is build.
	 * @param model
	 *            The diagram model whose data are the base of the script.
	 * @param sequences
	 *            The meta data of the sequences of the data scheme which is to
	 *            update.
	 * @param quote
	 *            The character sequence to quote names.
	 * @throws IllegalArgumentException
	 *             In case of passing a null pointer.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public SequenceScriptBuilder(DataModel model, SQLScriptFactory factory, SequenceMetaData[] sequences)
			throws IllegalArgumentException {
		super();
		ensure(factory != null, "SQL script factory cannot be null.");
		ensure(model != null, "data model cannot be null.");
		ensure(sequences != null, "sequences cannot be null.");
		this.factory = factory;
		this.model = model;
		this.sequences = sequences;
	}

	/**
	 * Adds the changes to the passed SQL script and to the HTML description.
	 * 
	 * @param sqlScript
	 *            The SQL script to extend by the sequence changes.
	 * @param htmlDescription
	 *            The description of the changes in HTML.
	 * @return <CODE>true</CODE> if changes where detected, <CODE>false</CODE>
	 *         otherwise.
	 * @throws IllegalArgumentException
	 *             Passing a null pointer.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public boolean build(SQLScript sqlScript, Vector<String> htmlDescription) throws IllegalArgumentException {
		ensure(htmlDescription != null, "HTML description cannot be null.");
		ensure(sqlScript != null, "SQL script cannot be null.");
		boolean changed = this.buildAddsForNewSequences(sqlScript, htmlDescription);
		changed = this.buildRemoveDeletedSequences(sqlScript, htmlDescription, changed) || changed;
		return changed;
	}

	public boolean buildAddsForNewSequences(SQLScript sqlScript, Vector<String> htmlDescription) {
		boolean changed = false;
		for (SequenceModel s : this.model.getSequences()) {
			if (!this.isSequenceInMetaData(s)) {
				this.addToExtendScript(sqlScript, this.factory.createSequenceStatement(s));
				if (!changed) {
					htmlDescription.add("        <h2>Sequenzen</h2>");
					htmlDescription.add("        <hr SIZE=1 WIDTH=\"100%\">");
				}
				htmlDescription.add("        Sequenz <B>" + s.getName() + "</B> " + "mit Increment " + s.getIncrement()
						+ " und Startwert " + s.getStartValue() + " hinzugef&uuml;gt.<BR>");
				changed = true;
			}
		}
		return changed;
	}

	private boolean isSequenceInMetaData(SequenceModel s) {
		for (SequenceMetaData smd : this.sequences) {
			if (smd.getName().equals(s.getName())) {
				return true;
			}
		}
		return false;
	}

	public boolean buildRemoveDeletedSequences(SQLScript sqlScript, Vector<String> htmlDescription,
			boolean sequenceAdded) {
		boolean changed = false;
		for (SequenceMetaData smd : this.sequences) {
			if (!this.isSequenceMetaDataInModel(smd)) {
				if (!changed && sequenceAdded) {
					sqlScript.addReducingStatement("");
				}
				this.addToReduceScript(sqlScript, this.factory.dropSequenceStatement(smd));
				if (!changed) {
					if (!sequenceAdded) {
						htmlDescription.add("        <h2>Sequenzen</h2>");
						htmlDescription.add("        <hr SIZE=1 WIDTH=\"100%\">");
					} else {
						htmlDescription.add("        <BR>");
					}
				}
				htmlDescription.add("        Sequenz <B>" + smd.getName() + "</B> gel&ouml;scht.<BR>");
				changed = true;
			}
		}
		return changed;
	}

	private void addToExtendScript(SQLScript script, String s) {
		if (s != null) {
			script.addExtendingStatement(s);
		}
	}

	private void addToReduceScript(SQLScript script, String s) {
		if (s != null) {
			script.addReducingStatement(s);
		}
	}

	private boolean isSequenceMetaDataInModel(SequenceMetaData smd) {
		for (SequenceModel s : model.getSequences()) {
			if (s.getName().equals(smd.getName())) {
				return true;
			}
		}
		return false;
	}

}