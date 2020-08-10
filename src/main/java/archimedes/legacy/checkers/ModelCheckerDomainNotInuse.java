package archimedes.legacy.checkers;

import static corentx.util.Checks.ensure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.acf.checker.ModelCheckerMessage.Level;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.DomainModel;
import baccara.gui.GUIBundle;

/**
 * A model checker for domains which are not in use.
 *
 * @author ollie (05.04.2020)
 */
public class ModelCheckerDomainNotInuse implements ModelChecker {

	public static final String RES_MODEL_CHECKER_UNUSED_DOMAIN_PRIMARY_KEY = "ModelChecker.UnusedDomain.label";

	private GUIBundle guiBundle = null;

	public ModelCheckerDomainNotInuse(GUIBundle guiBundle) {
		super();
		ensure(guiBundle != null, "GUI bundle cannot be null.");
		this.guiBundle = guiBundle;
	}

	@Override
	public ModelCheckerMessage[] check(DataModel model) {
		ensure(model != null, "data model cannot be null.");
		List<DomainModel> domains = getDomainList(model);
		Arrays.asList(model.getTables()).stream() //
				.flatMap(t -> Arrays.asList(t.getColumns()).stream()) //
				.forEach(column -> domains.remove(column.getDomain())) //
		;
		return domains //
				.stream() //
				.map(d -> new ModelCheckerMessage( //
						Level.WARNING, //
						guiBundle.getResourceText(RES_MODEL_CHECKER_UNUSED_DOMAIN_PRIMARY_KEY, d.getName()), //
						d)) //
				.collect(Collectors.toList()) //
				.toArray(new ModelCheckerMessage[0]);
	}

	private List<DomainModel> getDomainList(DataModel model) {
		return Arrays.asList(model.getAllDomains()) //
				.stream() //
				.collect(Collectors.toList()) //
		;
	}

}