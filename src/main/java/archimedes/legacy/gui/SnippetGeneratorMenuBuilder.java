package archimedes.legacy.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.junit.platform.commons.PreconditionViolationException;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ReflectionUtils;

import archimedes.snippets.SnippetGenerator;
import archimedes.snippets.SnippetGeneratorProvider;
import baccara.gui.GUIBundle;

/**
 * A builder for the snippet generator menu.
 *
 * @author ollie (11.08.2020)
 */
public class SnippetGeneratorMenuBuilder {

	public static final String RES_SNIPPET_GENERATOR_MENU_TITLE = "menu.snippetgenerator.title";

	static String packageName = "archimedes.snippets";

	JMenu createSnippetGeneratorMenu(List<Object> possibleSnippetGeneratorProviders, GUIBundle guiBundle) {
		List<SnippetGenerator> generatorsByClassPath = getSnippetGeneratorsByClassPath();
		List<SnippetGenerator> generatorsByProviders = getSnippetGeneratorsByProviders(
				possibleSnippetGeneratorProviders);
		JMenu menu = new JMenu(guiBundle.getResourceText(RES_SNIPPET_GENERATOR_MENU_TITLE));
		for (SnippetGenerator sg : generatorsByClassPath) {
			addMenuItem(menu, sg);
		}
		if (!generatorsByClassPath.isEmpty() && !generatorsByProviders.isEmpty()) {
			menu.addSeparator();
		}
		for (SnippetGenerator sg : generatorsByProviders) {
			addMenuItem(menu, sg);
		}
		return menu;
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

	private void addMenuItem(JMenu menu, SnippetGenerator sg) {
		JMenuItem item = new JMenuItem(sg.getName() + " (" + sg.getVersion() + ")");
		// TODO: Start code generation Process here.
		item.addItemListener(event -> System.out.println(((JMenuItem) event.getSource()).getText() + " clicked!"));
		menu.add(item);
	}

}