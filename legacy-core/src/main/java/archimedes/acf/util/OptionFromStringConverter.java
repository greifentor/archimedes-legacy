package archimedes.acf.util;

import archimedes.model.OptionModel;
import archimedes.scheme.Option;

/**
 * @author ollie (01.03.2021)
 */
public class OptionFromStringConverter {

	public OptionModel convert(String s) {
		if (s.contains(":")) {
			String v = s.substring(s.indexOf(":") + 1).trim();
			s = s.substring(0, s.indexOf(":")).trim();
			return new Option(s, v);
		}
		return new Option(s.trim());
	}

}