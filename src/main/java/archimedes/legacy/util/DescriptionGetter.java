/*
 * DescriptionGetter.java
 *
 * 24.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.util;

import java.util.List;

import archimedes.legacy.model.DefaultCommentModel;
import archimedes.model.ColumnModel;

/**
 * 
 * 
 * @author ollie
 * 
 * @changed OLI 24.10.2013 - Added.
 */

public class DescriptionGetter {

	private List defaultComments = null;

	/**
	 * Creates a new description getter with the passed default comments.
	 * 
	 * @param defaultComments
	 *            A list of default comments.
	 * 
	 * @changed OLI 24.10.2013 - Added.
	 */
	public DescriptionGetter(List defaultComments) {
		super();
		this.defaultComments = defaultComments;
	}

	/**
	 * Returns the description for the passed column or the default description
	 * if there is one. If neither a description is given for the column nor a
	 * default description found, the String "-/-" is returned.
	 * 
	 * @param column
	 *            The column which the description is to return for.
	 * @return The columns comment, a default comment or the String "-/-" if no
	 *         comment is found.
	 * 
	 * @changed OLI 24.10.2013 - Added.
	 */
	public String getDescription(ColumnModel column) {
		return this.getDescription(column, this.defaultComments);
	}

	/**
	 * Returns the description for the passed column or the default description
	 * if there is one. If neither a description is given for the column nor a
	 * default description found, the String "-/-" is returned.
	 * 
	 * @param column
	 *            The column which the description is to return for.
	 * @param defaultComments
	 *            A list of default comments.
	 * @return The columns comment, a default comment or the String "-/-" if no
	 *         comment is found.
	 * 
	 * @changed OLI 24.10.2013 - Added.
	 */
	public String getDescription(ColumnModel column, List defaultComments) {
		String b = column.getComment();
		if (b.length() == 0) {
			for (int i = 0, len = defaultComments.size(); i < len; i++) {
				DefaultCommentModel dcm = (DefaultCommentModel) defaultComments.get(i);
				String p = dcm.getPattern();
				if (p.equals("*")) {
					return dcm.getDefaultComment();
				} else if ((p.startsWith("*")) && (column.getName().endsWith(p.substring(1, p.length())))) {
					return dcm.getDefaultComment();
				} else if ((p.endsWith("*")) && (column.getName().startsWith(p.substring(0, p.length() - 1)))) {
					return dcm.getDefaultComment();
				} else if (p.equals(column.getName())) {
					return dcm.getDefaultComment();
				}
			}
			b = "-/-";
		}
		return b;
	}

}