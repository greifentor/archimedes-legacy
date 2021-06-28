package archimedes.codegenerators;

import java.util.Optional;

import archimedes.model.OptionListProvider;
import archimedes.model.OptionModel;

/**
 * @author ollie (30.03.2021)
 */
public class OptionGetter {

	public static Optional<OptionModel> getOptionByName(OptionListProvider optionListProvider, String name) {
		return Optional.ofNullable(optionListProvider.getOptionByName(name));
	}

	public static Optional<String> getParameterOfOptionByName(OptionListProvider optionListProvider, String name) {
		return Optional
				.ofNullable(
						getOptionByName(optionListProvider, name).map(option -> option.getParameter()).orElse(null));
	}

}