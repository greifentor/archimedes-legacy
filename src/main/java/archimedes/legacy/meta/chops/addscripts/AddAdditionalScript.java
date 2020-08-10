/*
 * AddAdditionalScript.java
 *
 * 18.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.addscripts;

import static corentx.util.Checks.ensure;

import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;

/**
 * A representation of an additional script.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.12.2015 - Added.
 */

public class AddAdditionalScript extends AbstractChangeOperation {

	private String script = null;

	/**
	 * Creates a representation of an additional script.
	 *
	 * @param script  The additional script in string format.
	 * @param section The section where the script is to add on.
	 *
	 * @changed OLI 18.12.2015 - Added.
	 */
	public AddAdditionalScript(String script, ScriptSectionType section) {
		super(section);
		ensure(script != null, "additional script cannot be null.");
		ensure(!script.isEmpty(), "additional script cannot be empty.");
		this.script = script;
	}

	/**
	 * Returns the additional script.
	 *
	 * @return The additional script.
	 */
	public String getScript() {
		return this.script;
	}

}