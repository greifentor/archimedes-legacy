package archimedes.legacy.util;

import static corentx.util.Checks.ensure;

/**
 * A class which provides methods for string trimming.
 *
 * @author ollie (15.12.2019)
 */
public class StringTrimer {

	/**
	 * Trims the passed string to the passed length if the string is longer. Otherwise nothing is changed.
	 * 
	 * @param s        The string to trim.
	 * @param len      The length which the string is to trim to.
	 * @param add3Dots Set this flag to add three dots if the string is trimmed.
	 * @return The trimmed string if the passed string is longer than the passed length or the passed string otherwise.
	 */
	public String trimLength(String s, int len, boolean add3Dots) {
		ensure(len > 0, "length cannot be less than one.");
		ensure(s != null, "string cannot be passed as null value.");
		if (len < s.length()) {
			if (add3Dots && (s.length() > 3)) {
				return s.substring(0, len - 3) + "...";
			}
			return s.substring(0, len);
		}
		return s;
	}

}