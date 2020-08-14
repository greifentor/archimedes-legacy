package archimedes.snippets.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.junit.platform.commons.PreconditionViolationException;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ReflectionUtils;

import archimedes.legacy.model.DataModel;
import archimedes.snippets.SnippetGenerator;
import archimedes.snippets.SnippetGeneratorProvider;
import baccara.gui.GUIBundle;

/**
 * A builder for the snippet generator menu.
 *
 * @author ollie (11.08.2020)
 */
public class SnippetGeneratorMenuBuilder {

	public static final String RES_SNIPPET_GENERATOR_MENU_TITLE = "menu.snippetgenerator";

	static String packageName = "archimedes.snippets";

	public JMenu createSnippetGeneratorMenu(List<Object> possibleSnippetGeneratorProviders, GUIBundle guiBundle,
			Supplier<DataModel> dataModelSupplier) {
		JMenu menu = new JMenu(guiBundle.getResourceText(RES_SNIPPET_GENERATOR_MENU_TITLE + ".title"));
		updateSnippetGeneratorMenu(menu, possibleSnippetGeneratorProviders, dataModelSupplier, guiBundle);
		return menu;
	}

	public void updateSnippetGeneratorMenu(JMenu menu, List<Object> possibleSnippetGeneratorProviders,
			Supplier<DataModel> dataModelSupplier, GUIBundle guiBundle) {
		List<SnippetGenerator> generatorsByClassPath = getSnippetGeneratorsByClassPath();
		List<SnippetGenerator> generatorsByProviders = getSnippetGeneratorsByProviders(
				possibleSnippetGeneratorProviders);
		for (SnippetGenerator sg : generatorsByClassPath) {
			addMenuItem(menu, sg, dataModelSupplier, guiBundle);
		}
		if (!generatorsByClassPath.isEmpty() && !generatorsByProviders.isEmpty()) {
			menu.addSeparator();
		}
		for (SnippetGenerator sg : generatorsByProviders) {
			addMenuItem(menu, sg, dataModelSupplier, guiBundle);
		}
	}

	private List<SnippetGenerator> getSnippetGeneratorsByClassPath() {
		List<SnippetGenerator> l = new ArrayList<>();
		try {
			List<Class<?>> sgcs = ReflectionUtils.findAllClassesInPackage(packageName,
					ClassFilter.of(cls -> isImplementationOfSnippetGenerator(cls.getInterfaces())));
			for (Class<?> sgc : sgcs) {
				SnippetGenerator sg = (SnippetGenerator) sgc.getDeclaredConstructor().newInstance();
				l.add(sg);
			}
		} catch (NoSuchMethodException nsme) {
			// TODO log the error ...
		} catch (InvocationTargetException ite) {
			// TODO log the error ...
		} catch (InstantiationException ie) {
			// TODO log the error ...
		} catch (IllegalAccessException iae) {
			// TODO log the error ...
		} catch (PreconditionViolationException pve) {
			// OLI: package does not exist ...
		}
		return l;
	}

	private boolean isImplementationOfSnippetGenerator(Class<?>[] interfaces) {
		for (Class<?> i : interfaces) {
			if (i == SnippetGenerator.class) {
				return true;
			}
		}
		return false;
	}

	private List<SnippetGenerator> getSnippetGeneratorsByProviders(List<Object> possibleSnippetGeneratorProviders) {
		List<SnippetGenerator> l = new ArrayList<>();
		for (Object o : possibleSnippetGeneratorProviders) {
			if (o instanceof SnippetGeneratorProvider) {
				for (SnippetGenerator sg : ((SnippetGeneratorProvider) o).getSnippetGenerators()) {
					l.add(sg);
				}
			}
		}
		return l;
	}

	private void addMenuItem(JMenu menu, SnippetGenerator sg, Supplier<DataModel> dataModelSupplier,
			GUIBundle guiBundle) {
		JMenuItem item = new JMenuItem(sg.getName() + " (" + sg.getVersion() + ")");
		item.addActionListener(event -> {
			SnippetGeneratorDialog sdg = new SnippetGeneratorDialog(guiBundle, sg, dataModelSupplier.get());
			// TODO: implement input of parameters here.
			System.out.println(sg.generate(new HashMap<>(), dataModelSupplier.get()));
		});
		menu.add(item);
	}

}