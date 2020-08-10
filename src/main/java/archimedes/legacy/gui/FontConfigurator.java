/*
 * FontConfigurator.java
 *
 * 17.09.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.UIManager;

import corentx.io.FileUtil;
import logging.Logger;

/**
 * This class configures the font for the application by an JavaScript and via
 * the UIManager. <BR>
 * The JavaScript contains statements to fill a map (String, Font) which is
 * processed by the <CODE>FontConfigurator</CODE>. The JavaScript is could be
 * placed in the users home folder or in the home folder of the Archimedes
 * installation. It has to be named as ".archimedes-fonts.js".
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.09.2015 - Added.
 */

public class FontConfigurator {

	private static final Logger LOG = Logger.getLogger(FontConfigurator.class);
	private static final String FILENAME = ".archimedes-fonts.js";

	/**
	 * Reads the configuration script file (if existing) and adds the settings to
	 * the UIManager.
	 *
	 * @throws IOException     If something goes wrong while reading the file.
	 * @throws ScriptException If something goes wrong while executing the script.
	 *
	 * @changed OLI 17.09.2015 - Added.
	 */
	public void readFonts() throws IOException, ScriptException {
		/*
		 * SortedVector<String> sv = new SortedVector<String>(); for (Map.Entry<Object,
		 * Object> entry : javax.swing.UIManager.getDefaults().entrySet()) { Object key
		 * = entry.getKey(); Object value = javax.swing.UIManager.get(key); // if (value
		 * instanceof FontUIResource) { // int style = ((FontUIResource)
		 * value).getStyle(); // sv.add("m.put(\"" + key +
		 * "\", new FontUIResource(fontName, Font." // + (style == Font.PLAIN ? "PLAIN"
		 * : (style == Font.BOLD ? "BOLD" : // (style == Font.ITALIC ? "ITALIC" :
		 * "PLAIN"))) + ", fontSize));"); sv.add(key + " > " + value); // } } for
		 * (String se : sv) { LOG.info(se); }
		 */
		Map<String, Font> m = new Hashtable<String, Font>();
		String s = this.readScriptFile();
		if (s != null) {
			this.fillMapWithScript(m, s);
			for (String key : m.keySet()) {
				Font f = m.get(key);
				if (f != null) {
					UIManager.put(key, f);
				}
			}
		} else {
			LOG.warn("No font configuration file found.");
		}
	}

	private String readScriptFile() throws IOException {
		String s = this.checkForFileName(FileUtil.completePath(System.getProperty("user.home")));
		return (s != null ? s : this.checkForFileName("."));
	}

	private String checkForFileName(String path) throws IOException {
		String fn = FileUtil.completePath(path) + FILENAME;
		if (new File(fn).exists()) {
			LOG.info("Reading font configuration from file: " + fn);
			return FileUtil.readTextFromFile(fn);
		}
		return null;
	}

	private void fillMapWithScript(Map<String, Font> m, String s) throws ScriptException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		engine.put("m", m);
		engine.eval(s);
	}

}