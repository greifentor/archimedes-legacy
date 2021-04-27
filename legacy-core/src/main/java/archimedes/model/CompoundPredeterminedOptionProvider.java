package archimedes.model;

import static corentx.util.Checks.ensure;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An implementation of the PredeterminedOptionProvider interface for keep the predetermined options of many providers
 * in one provider.
 *
 * @author ollie (19.07.2020)
 */
public class CompoundPredeterminedOptionProvider implements PredeterminedOptionProvider {

	private Map<OptionType, List<String>> selectableOptions = new EnumMap<>(OptionType.class);

	public CompoundPredeterminedOptionProvider() {
		super();
		for (OptionType optionType : OptionType.values()) {
			selectableOptions.put(optionType, new ArrayList<>());
		}
	}

	/**
	 * Adds the passed strings for the passed option type.
	 * 
	 * @param optionType The option type which the strings are to add for.
	 * @param options    The string with the option names to add.
	 * @throws NullPointerException Passing the option type or one of the options as "null".
	 */
	public void addOptions(OptionType optionType, String... options) {
		ensure(optionType != null, new NullPointerException("option type to add cannot be null."));
		for (String option : options) {
			ensure(option != null, new NullPointerException("option to add cannot be null."));
			selectableOptions.get(optionType).add(option);
		}
	}

	@Override
	public String[] getSelectableOptions(OptionType optionType) {
		ensure(optionType != null, new NullPointerException("option type cannot be null to get selectable options."));
		List<String> l = selectableOptions.get(optionType);
		return l.stream().sorted().collect(Collectors.toList()).toArray(new String[l.size()]);
	}

}