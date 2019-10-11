/*
 * Panel.java
 *
 * 10.07.2005
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;

import java.util.List;

import archimedes.acf.util.ParameterUtil;
import archimedes.legacy.app.ArchimedesEditorDescriptor;
import archimedes.model.OptionModel;
import archimedes.model.PanelModel;
import archimedes.scheme.Option;
import corent.base.Attributed;
import corent.base.StrUtil;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.Editable;
import corent.djinn.EditorDescriptorList;

/**
 * Diese Klasse stellt eine Musterimplementierung des PanelModels zur Nutzung
 * von Archimedes in Verbindung mit der Standard-corent-Bibliothek zur
 * Verf&uuml;gung.<BR>
 * <HR>
 * 
 * @author ollie
 * 
 */

public class Panel implements Attributed, Editable, PanelModel {

	/** Ein Bezeichner zum Zugriff auf den numerischen Identifikator der Panels. */
	public static final int ID_PANELNUMBER = 0;
	/** Ein Bezeichner zum Zugriff auf den Tab-Titel des Panels. */
	public static final int ID_TABTITLE = 1;
	/** Ein Bezeichner zum Zugriff auf das Tab-Mnemonic des Panels. */
	public static final int ID_TABMNEMONIC = 2;
	/** Ein Bezeichner zum Zugriff auf den Tab-ToolTipText des Panels. */
	public static final int ID_TABTOOLTIPTEXT = 3;
	/** Ein Bezeichner zum Zugriff auf den Klasse des Panels. */
	public static final int ID_PANELCLASS = 4;

	/* Der numerische Identifikator des Panels. */
	private int panelnumber = 0;
	/*
	 * Der Name der Klasse f&uuml;r das Panel, falls eine andere als der
	 * Standard benutzt werden soll.
	 */
	private String clsname = "";
	/* Das Panel der NReferenz auf dem sie innerhalb einer Applikation. */
	private String tabtitle = "";
	/* Das Tab-Menmonic der NReferenz innerhalb einer Applikation. */
	private String tabmnemonic = "";
	/* Der Tab-ToolTipText der NReferenz innerhalb einer Applikation. */
	private String tabtooltiptext = "";

	/** Generiert ein Panel mit Default-Werten. */
	public Panel() {
		super();
	}

	/* Ueberschreiben von Methoden der Superklasse (Pflicht). */

	public boolean equals(Object o) {
		if (!(o instanceof Panel)) {
			return false;
		}
		Panel p = (Panel) o;
		return (this.getPanelNumber() == p.getPanelNumber()) && this.getTabTitle().equals(p.getTabTitle())
				&& this.getTabMnemonic().equals(p.getTabMnemonic())
				&& this.getTabToolTipText().equals(p.getTabToolTipText());
	}

	public String toString() {
		return "" + this.getPanelNumber() + " - " + this.getTabTitle();
	}

	/* Implementierung des Interfaces Attributed */

	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_PANELNUMBER:
			return new Integer(this.getPanelNumber());
		case ID_TABTITLE:
			return this.getTabTitle();
		case ID_TABMNEMONIC:
			return this.getTabMnemonic();
		case ID_TABTOOLTIPTEXT:
			return this.getTabToolTipText();
		case ID_PANELCLASS:
			return this.getPanelClass();
		}
		throw new IllegalArgumentException("Klasse Panel verfuegt nicht ueber ein Attribut " + id + " (get)!");
	}

	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_PANELNUMBER:
			this.setPanelNumber(((Integer) value).intValue());
			return;
		case ID_TABTITLE:
			this.setTabTitle((String) value);
			return;
		case ID_TABMNEMONIC:
			this.setTabMnemonic((String) value);
			return;
		case ID_TABTOOLTIPTEXT:
			this.setTabToolTipText((String) value);
			return;
		case ID_PANELCLASS:
			this.setPanelClass((String) value);
			return;
		}
		throw new IllegalArgumentException("Klasse Panel verfuegt nicht ueber ein Attribut " + id + " (set)!");
	}

	/* Implementierung des Interfaces Comparable. */

	public int compareTo(Object o) {
		return ((Integer) this.get(ID_PANELNUMBER)).compareTo((Integer) ((Panel) o).get(ID_PANELNUMBER));
	}

	/* Implementierung des Interfaces Editable. */

	public EditorDescriptorList getEditorDescriptorList() {
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_PANELNUMBER, dlf, dcf, "Panelnummer", 'P', null,
				"Der numerische Identifikator des Panels"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_TABTITLE, dlf, dcf, "Titel", 'T', null, StrUtil
				.FromHTML("Hier k&ouml;nnen sie einen Titel "
						+ "f&uuml;r das Tab angeben, unter dem der NReferenz-Editor angezeigt werden " + "soll")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_TABMNEMONIC, dlf, dcf, "Mnemonic", 'M', null, StrUtil
				.FromHTML("Hier k&ouml;nnen ein Mnemonic " + "zum Titel angeben")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_TABTOOLTIPTEXT, dlf, dcf, "ToolTipText", 'I', null,
				StrUtil.FromHTML("Hier k&ouml;nnen sie einen " + "ToolTipText zum Tab hinterlegen")));
		dedl.addElement(new ArchimedesEditorDescriptor("Parameter", 0, this, ID_PANELCLASS, dlf, dcf, "Klasse", 'K',
				null, StrUtil.FromHTML("Hier k&ouml;nnen Sie eine Klasse "
						+ "f&uuml;r das Panel angeben, falls es nicht automatisch generiert werden " + "soll.")));
		return dedl;
	}

	public Object createObject() {
		return new Panel();
	}

	public Object createObject(Object blueprint) throws ClassCastException {
		Panel p = (Panel) blueprint;
		Panel newone = new Panel();
		newone.setPanelNumber(p.getPanelNumber());
		newone.setTabTitle(p.getTabTitle());
		newone.setTabMnemonic(p.getTabMnemonic());
		newone.setTabToolTipText(p.getTabToolTipText());
		newone.setPanelClass(p.getPanelClass());
		return newone;
	}

	/* Implementierung des Interfaces NReferenzModel. */

	public String getPanelClass() {
		return this.clsname;
	}

	public void setPanelClass(String clsname) {
		this.clsname = clsname;
	}

	public int getPanelNumber() {
		return this.panelnumber;
	}

	public void setPanelNumber(int pn) {
		this.panelnumber = pn;
	}

	public String getTabTitle() {
		return this.tabtitle;
	}

	public void setTabTitle(String t) {
		if (t == null) {
			t = "";
		}
		this.tabtitle = t;
	}

	public String getTabMnemonic() {
		return this.tabmnemonic;
	}

	public void setTabMnemonic(String m) {
		if (m == null) {
			m = "";
		}
		this.tabmnemonic = m;
	}

	public String getTabToolTipText() {
		return this.tabtooltiptext;
	}

	public void setTabToolTipText(String ttt) {
		if (ttt == null) {
			ttt = "";
		}
		this.tabtooltiptext = ttt;
	}

	/**
	 * @changed OLI 13.07.2016 - Added.
	 */
	@Override
	public OptionModel[] getOptions() {
		List<OptionModel> l = new corentx.util.SortedVector<OptionModel>();
		ParameterUtil pu = new ParameterUtil();
		for (String p : pu.getParameters(this)) {
			if (p.contains(":")) {
				String v = p.substring(p.indexOf(":") + 1).trim();
				p = p.substring(0, p.indexOf(":")).trim();
				l.add(new Option(p, v));
			} else {
				l.add(new Option(p.trim()));
			}
		}
		return l.toArray(new OptionModel[0]);
	}

	/**
	 * @changed OLI 13.07.2016 - Added.
	 */
	@Override
	public OptionModel[] getOptionsByName(String name) {
		List<OptionModel> l = new corentx.util.SortedVector<OptionModel>();
		for (OptionModel o : this.getOptions()) {
			if (o.getName().equals(name)) {
				l.add(o);
			}
		}
		return l.toArray(new OptionModel[0]);
	}

	/**
	 * @changed OLI 13.07.2016 - Added.
	 */
	@Override
	public void addOption(OptionModel option) {
		ensure(option != null, "option to add cannot be null.");
		for (OptionModel o : this.getOptions()) {
			if (o.equals(option)) {
				return;
			}
		}
		this.setPanelClass(this.getPanelClass()
				+ (this.getPanelClass().length() > 0 ? "|" : "")
				+ option.getName()
				+ ((option.getParameter() != null) && !option.getParameter().isEmpty() ? ":" + option.getParameter()
						: ""));
	}

	/**
	 * @changed OLI 13.07.2016 - Added.
	 */
	@Override
	public OptionModel getOptionAt(int i) {
		return this.getOptions()[i];
	}

	/**
	 * @changed OLI 13.07.2016 - Added.
	 */
	@Override
	public OptionModel getOptionByName(String name) {
		for (OptionModel o : this.getOptions()) {
			if (o.getName().equals(name)) {
				return o;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 13.07.2016 - Added.
	 */
	@Override
	public int getOptionCount() {
		return this.getOptions().length;
	}

	/**
	 * @changed OLI 13.07.2016 - Added.
	 */
	@Override
	public boolean isOptionSet(String optionName) {
		return this.getOptionByName(optionName) != null;
	}

	/**
	 * @changed OLI 13.07.2016 - Added.
	 */
	@Override
	public void removeOption(OptionModel option) {
		OptionModel[] os = this.getOptions();
		this.setPanelClass("");
		for (OptionModel o : os) {
			if (!o.equals(option)) {
				this.addOption(o);
			}
		}
	}

}