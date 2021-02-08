/*
 * HistoryOwnerUtil.java
 *
 * 13.06.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import archimedes.model.HistoryOwner;

/**
 * Utility class for history owners.
 * 
 * @author ollie
 * 
 * @changed OLI 13.06.2013 - Added.
 */

public class HistoryOwnerUtil {

	/**
	 * Appends a changed-tag with the passed text to the passed history owner.
	 * 
	 * @param historyOwner
	 *            The history owner whose history text is to extend.
	 * @param text
	 *            The text to append to the change tag.
	 * 
	 * @changed OLI 13.06.2013 - Added.
	 */
	public static void addChangedTag(HistoryOwner historyOwner, String text) {
		String h = historyOwner.getHistory();
		historyOwner.setHistory(h + (h.length() > 0 ? "\n" : "") + getChangeTag(text));
	}

	/**
	 * Returns a change-tag with the personal data and the passed text.
	 * 
	 * @param text
	 *            The text to append to the change tag.
	 * 
	 * @changed OLI 13.06.2013 - Added.
	 */
	public static String getChangeTag(String text) {
		return "@changed " + System.getProperty("archimedes.user.token") + " - " + text + ".";
	}

}