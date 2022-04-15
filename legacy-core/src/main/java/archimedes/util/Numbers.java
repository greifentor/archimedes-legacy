package archimedes.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Numbers {

	public boolean isAnInteger(String s) {
		if ((s == null) || s.isBlank()) {
			return false;
		}
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}