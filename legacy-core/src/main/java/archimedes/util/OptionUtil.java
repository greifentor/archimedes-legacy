package archimedes.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import archimedes.model.OptionListProvider;
import archimedes.model.OptionModel;

/**
 * @changed OLI 29.09.2023 - Added.
 */
public class OptionUtil {

	public static OptionModel getOptionAt(OptionListProvider olp, int i) {
		return olp.getOptions()[i];
	}

	public static OptionModel getOptionByName(OptionListProvider olp, String name) {
		for (OptionModel o : olp.getOptions()) {
			if (o.getName().equals(name)) {
				return o;
			}
		}
		return null;
	}

	public static OptionModel[] getOptionsByName(OptionListProvider olp, String name) {
		List<OptionModel> l = new corentx.util.SortedVector<OptionModel>();
		for (OptionModel o : olp.getOptions()) {
			if (o.getName().equals(name)) {
				l.add(o);
			}
		}
		return l.toArray(new OptionModel[0]);
	}

	public static Optional<OptionModel> findOptionByName(OptionListProvider olp, String name) {
		return Optional.ofNullable(getOptionByName(olp, name));
	}

	public static int getOptionCount(OptionListProvider olp) {
		return olp.getOptions().length;
	}

	public static void ifOptionSetWithValueDo(OptionListProvider olp, String optionName, String value,
			Consumer<OptionModel> consumer) {
		findOptionByName(olp, optionName)
				.filter(om -> (om.getParameter() != null) && om.getParameter().equals(value))
				.ifPresent(om -> consumer.accept(om));
	}

	public static boolean isOptionSet(OptionListProvider olp, String optionName) {
		return getOptionByName(olp, optionName) != null;
	}

	public static boolean isOptionSetWithValue(OptionListProvider olp, String optionName, String value) {
		return Arrays
				.asList(getOptionsByName(olp, optionName))
				.stream()
				.filter(option -> option.getParameter() != null)
				.anyMatch(option -> option.getParameter().equals(value));
	}

}
