/*
 * DoubleClassesChecker.java
 *
 * 15.12.2014
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import corentx.util.SortedVector;
import logging.Logger;

/**
 * A checker for double classes.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.12.2014 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class DoubleClassesChecker {

	private static final Logger log = Logger.getLogger(DoubleClassesChecker.class);

	private Map<String, DoubleClassesCheckerContainer> m = new HashMap<String, DoubleClassesCheckerContainer>();

	public void addPath(String fileName) {
		String name = fileName;
		name = name.replace("\\", "/");
		int pos = name.lastIndexOf("/java/");
		if (pos >= 0) {
			name = name.substring(pos + 6);
		}
		name = name.replace("/", ".");
		DoubleClassesCheckerContainer c = this.m.get(name);
		if (c == null) {
			this.m.put(name, new DoubleClassesCheckerContainer(fileName));
		} else {
			c.addPath(fileName);
		}
	}

	public String[] getFileNames() {
		List<String> l = new SortedVector<String>();
		for (String name : this.m.keySet()) {
			l.add(name);
		}
		return l.toArray(new String[0]);
	}

	public String[] getFilePathes(String name) {
		DoubleClassesCheckerContainer c = this.m.get(name);
		List<String> l = new SortedVector<String>();
		if (c != null) {
			for (String path : c.getPathes()) {
				l.add(path);
			}
		}
		return l.toArray(new String[0]);
	}

	public String getReport() {
		String s = "";
		for (String name : this.m.keySet()) {
			DoubleClassesCheckerContainer c = this.m.get(name);
			if (c.getCount() > 1) {
				log.info("DOUBLE CLASS FILE FOUND: " + name + "\n" + c.toString());
			}
		}
		return s;
	}

	public boolean hasWarnings() {
		for (DoubleClassesCheckerContainer c : this.m.values()) {
			if (c.getCount() > 1) {
				return true;
			}
		}
		return false;
	}

}

class DoubleClassesCheckerContainer {

	public List<String> pathes = new LinkedList<String>();

	public DoubleClassesCheckerContainer(String path) {
		super();
		this.addPath(path);
	}

	public void addPath(String path) {
		if (!this.pathes.contains(path)) {
			this.pathes.add(path);
		}
	}

	public int getCount() {
		return this.pathes.size();
	}

	public String[] getPathes() {
		return this.pathes.toArray(new String[0]);
	}

	@Override
	public String toString() {
		String s = "";
		for (String path : this.pathes) {
			if (s.length() > 0) {
				s += "\n";
			}
			s += "    " + path;
		}
		return s;
	}

}