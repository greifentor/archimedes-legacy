package archimedes.snippets.impl;

import java.util.Arrays;
import java.util.List;
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
		return "package ${packageName}\n\n" + //
				"public ${className}() extends Exception {\n\n" + //
				"}" //
		;
	}

	@Override
	public String getName() {
		return "Exception Generator";
	}

	@Override
	public List<ParameterDescription> getParameterDescriptions() {
		return Arrays.asList( //
				new ParameterDescription() //
						.setName("className") //
						.setType(ParameterType.STRING), //
				new ParameterDescription() //
						.setName("packageName") //
						.setType(ParameterType.STRING) //
		);
	}

	@Override
	public String getResourcePrefix() {
		return "exceptionClassSnippetGenerator";
	}

	@Override
	public String getVersion() {
		return "0.0.1";
	}

}