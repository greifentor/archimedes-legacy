package archimedes.snippets;

import java.util.ArrayList;
import java.util.List;

/**
 * An interface for snippet generators. Should be implemented by code factories for instance.
 *
 * @author ollie (11.08.2020)
 */
public interface SnippetGeneratorProvider {

	/**
	 * Returns a list of snippet generators provided by the implementing class.
	 * 
	 * @return A list of snippet generators.
	 */
	default List<SnippetGenerator> getSnippetGenerators() {
		return new ArrayList<>();
	}

}