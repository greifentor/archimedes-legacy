package archimedes.snippets.impl;

import java.util.Map;

import archimedes.legacy.model.DataModel;
import archimedes.snippets.SnippetGenerator;

/**
 * An example snippel generator for simple exceptions.
 *
 * @author ollie (11.08.2020)
 */
public class ExceptionClassSnippetGenerator implements SnippetGenerator {

	@Override
	public String generate(Map<String, Object> parameters, DataModel dataModel) {
		// TODO Auto-generated method stub
		return "data model is: " + dataModel.getName();
	}

	@Override
	public String getName() {
		return "Exception Generator";
	}

	@Override
	public String getVersion() {
		return "0.0.1";
	}

}
