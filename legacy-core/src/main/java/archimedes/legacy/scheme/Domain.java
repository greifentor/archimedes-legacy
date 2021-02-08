/*
 * Domain.java
 *
 * 01.04.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;

import java.sql.Types;
import java.util.List;
import java.util.StringTokenizer;

import archimedes.acf.util.ParameterUtil;
import archimedes.legacy.Archimedes;
import archimedes.legacy.app.ArchimedesEditorDescriptor;
import archimedes.legacy.gui.CommentSubEditorFactory;
import archimedes.legacy.gui.HistoryOwnerSubEditorFactory;
import archimedes.model.DomainModel;
import archimedes.model.OptionModel;
import archimedes.scheme.Option;
import corent.base.Attributed;
import corent.base.Direction;
import corent.base.StrUtil;
import corent.db.DBExecMode;
import corent.db.DBType;
import corent.db.DBUtil;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.DefaultSubEditorDescriptor;
import corent.djinn.DefaultTabDescriptor;
import corent.djinn.DefaultTabbedPaneFactory;
import corent.djinn.EditorDescriptorList;
import corent.djinn.ListViewSelectable;
import corent.djinn.TabDescriptor;
import corent.djinn.TabbedEditable;
import corent.djinn.TabbedPaneFactory;
import corent.xml.ToXMLAttributes;
import corent.xml.ToXMLTag;

/**
 * Diese Klasse stellt eine einfache Implementierung des DomainModels dar.
 * 
 * @author ollie
 * 
 * @changed OLI 11.05.2008 - Erweiterung der Implementierung des Interfaces
 *          <TT>TabbedEditable</TT> um die Methode <TT>isTabEnabled(int)</TT>.
 * @changed OLI 10.06.2010 - Erweiterung um die Implementierung der Methode
 *          <TT>getType(DBExecMode)</TT>. Dabei Formatanpassungen.
 * @changed OLI 01.11.2011 - Erweiterung um den <TT>HistoryOwner</TT>.
 * @changed OLI 14.11.2011 - Erweiterung um die Umsetzung von BLOB's durch die
 *          <TT>getType(DBExecMode)</TT>-Methode.
 */

public class Domain implements Attributed, DomainModel, ListViewSelectable, TabbedEditable, ToXMLAttributes, ToXMLTag {

	/** Ein Bezeichner zum Zugriff auf den Namen der Domain. */
	public static final int ID_NAME = 0;
	/** Ein Bezeichner zum Zugriff auf den SQL-Typ der Domain. */
	public static final int ID_SQLTYP = 1;
	/** Ein Bezeichner zum Zugriff auf die Beschreibung zur Domain. */
	public static final int ID_COMMENT = 2;
	/** Ein Bezeichner zum Zugriff auf die Types-Konstante zur Domain. */
	public static final int ID_DATATYPE = 3;
	/** Ein Bezeichner zum Zugriff auf die Feldl&auml;nge zur Domain. */
	public static final int ID_LENGTH = 4;
	/** Ein Bezeichner zum Zugriff auf die Nachkommastellen zur Domain. */
	public static final int ID_NKS = 5;
	/** Ein Bezeichner zum Zugriff auf den Initialwert zur Domain. */
	public static final int ID_INITIALWERT = 6;
	/** Ein Bezeichner zum Zugriff auf die Historie. */
	public static final int ID_HISTORY = 7;
	/** Ein Bezeichner zum Zugriff auf den Parameterstring. */
	public static final int ID_PARAMETERS = 8;

	/* Ein Z&auml;hler f&uuml;r die Anzahl der bereits erzeugten Instanzen. */
	private static int count = 0;

	/* Der Connection-kompatible Datentyp zur Domain. */
	private int datatype = Types.INTEGER;
	/* die Feldl&auml;nge zur Domain. */
	private int length = 0;
	/* Der Nachkommastellen zur Domain. */
	private int nks = 0;
	private String comment = "";
	/* Die Historie zur Domain. */
	private String historie = "";
	/* Der Initialwert zur Domain. */
	private String initialwert = "NULL";
	/* Der Name der Domain. */
	private String name = "Domain" + count++;
	/* Die Parameter zur Domain. */
	private String parameters = "";

	/** Generiert eine Domain mit Standardeinstellungen. */
	public Domain() {
		super();
	}

	/**
	 * Generiert eine Domain mit den angegebenen Parametern.
	 * 
	 * @param name
	 *            Der Name der Domain.
	 * @param dt
	 *            Der Datatype zur Domain.
	 * @param len
	 *            Eine Feldl&auml;ngenangabe.
	 * @param nks
	 *            Eine Angabe zu den Nachkommastellen der Domain.
	 */
	public Domain(String name, int dt, int len, int nks) {
		super();
		this.setName(name);
		this.setDataType(dt);
		this.setLength(len);
		this.setDecimalPlace(nks);
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public String getComment() {
		return this.comment;
	}

	@Override
	public int compareTo(Object o) {
		Domain dom = (Domain) o;
		int erg = this.getName().compareTo(dom.getName());
		if (erg == 0) {
			return this.getType().compareTo(dom.getType());
		}
		return erg;
	}

	@Override
	public Object createObject() {
		return Archimedes.Factory.createDomain();
	}

	@Override
	public Object createObject(Object blueprint) throws ClassCastException {
		if (!(blueprint instanceof Domain)) {
			throw new ClassCastException("Instance of Domain required!");
		}
		Domain source = (Domain) blueprint;
		Domain dom = (Domain) this.createObject();
		dom.setName(source.getName());
		dom.setComment(source.getComment());
		dom.setDataType(source.getDataType());
		dom.setLength(source.getLength());
		dom.setDecimalPlace(source.getDecimalPlace());
		dom.setInitialValue(source.getInitialValue());
		// Parameters field will be copied via the options.
		for (OptionModel om : source.getOptions()) {
			dom.addOption(new Option(om.getName(), om.getParameter()));
		}
		return dom;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Domain)) {
			return false;
		}
		Domain dom = (Domain) o;
		return this.getName().equals(dom.getName()) && this.getType().equals(dom.getType());
	}

	@Override
	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_NAME:
			return this.getName();
		case ID_SQLTYP:
			return this.getType();
		case ID_COMMENT:
			return this.getComment();
		case ID_DATATYPE:
			String s = DBUtil.TypeToString(this.getDataType());
			s = s.substring(s.indexOf(".") + 1, s.length());
			return s;
		case ID_LENGTH:
			return new Integer(this.getLength());
		case ID_NKS:
			return new Integer(this.getDecimalPlace());
		case ID_INITIALWERT:
			return this.getInitialValue();
		case ID_HISTORY:
			return this.getHistory();
		case ID_PARAMETERS:
			return this.getParameters();
		}
		throw new IllegalArgumentException("Klasse Domain verfuegt nicht ueber ein Attribut " + id + " (get)!");
	}

	@Override
	public int getDataType() {
		return this.datatype;
	}

	@Override
	public EditorDescriptorList getEditorDescriptorList() {
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		DefaultComponentFactory dcftypes = new DefaultComponentFactory(DBUtil.GetTypes());
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_NAME, dlf, dcf, "Name", 'N', null,
				"Der Name der Domain"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_SQLTYP, dlf, dcf, "SQL-Typ", '\0', null,
				"Der SQL-Typ der Domain", true));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_DATATYPE, dlf, dcftypes, "Datatype", 'T', null,
				"Der Types-Typ der Domain"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_LENGTH, dlf, dcf, StrUtil.FromHTML("L&auml;nge"), 'L',
				null, StrUtil.FromHTML("Die Feldl&auml;nge" + " der Domain")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_NKS, dlf, dcf, "Nachkommastellen", 'N', null,
				"Die Nachkommastellen der Domain"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_INITIALWERT, dlf, dcf, "Initialwert", 'I', null,
				"Der Initialwert der Domain"));
		dedl.addElement(new ArchimedesEditorDescriptor("Parameter", 0, this, ID_PARAMETERS, dlf, dcf, "Parameter", 'P',
				null, "Die Parameter zur Domain."));
		dedl.addElement(new DefaultSubEditorDescriptor(1, this, new CommentSubEditorFactory()));
		dedl.addElement(new DefaultSubEditorDescriptor(2, this, new HistoryOwnerSubEditorFactory()));
		return dedl;
	}

	@Override
	public String getHistory() {
		return this.historie;
	}

	@Override
	public String getInitialValue() {
		return this.initialwert;
	}

	@Override
	public int getLength() {
		return this.length;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getDecimalPlace() {
		return this.nks;
	}

	@Override
	public TabbedPaneFactory getTabbedPaneFactory() {
		return new DefaultTabbedPaneFactory(new TabDescriptor[] { new DefaultTabDescriptor("Daten", 'A', null),
				new DefaultTabDescriptor("Beschreibung", 'B', null), new DefaultTabDescriptor("Historie", 'H', null) });
	}

	@Override
	public String getType() {
		DBType dbtype = DBType.Convert(this.getDataType());
		String s = dbtype.toSQLType(Archimedes.DBMODE);
		if (dbtype.hasLength()) {
			s += "(" + this.getLength();
			if (dbtype.hasNKS()) {
				s += ", " + this.getDecimalPlace();
			}
			s += ")";
		}
		return s;
	}

	@Override
	/**
	 * @changed OLI 10.06.2010 - Hinzugef&uuml;gt.
	 * @changed OLI 14.11.2011 - Erweiterung um die Umsetzung f&uuml;r das BLOB.
	 */
	public String getType(DBExecMode dbmode) {
		String t = null;
		if (dbmode == null) {
			throw new IllegalArgumentException("db mode can not be null");
		}
		t = this.getType();
		if (dbmode == DBExecMode.HSQL) {
			if (this.getDataType() == Types.BLOB) {
				t = "longvarbinary";
			} else if (this.getDataType() == Types.LONGVARCHAR) {
				t = "longvarchar";
			}
		} else if (dbmode == DBExecMode.MSSQL) {
			if (this.getDataType() == Types.BLOB) {
				t = "varbinary(max)";
			}
		} else if (dbmode == DBExecMode.MYSQL) {
			if (this.getDataType() == Types.BLOB) {
				t = "blob";
			}
		} else if (dbmode == DBExecMode.POSTGRESQL) {
			if (this.getDataType() == Types.BLOB) {
				t = "bytea";
			} else if (this.getDataType() == Types.DOUBLE) {
				t = "double precision";
			}
		}
		return t;
	}

	@Override
	public boolean isSelected(Object[] criteria) throws IllegalArgumentException {
		if (((criteria != null) && (criteria.length > 0) && !(criteria[0] instanceof String))
				|| ((criteria != null) && (criteria.length > 1))) {
			throw new IllegalArgumentException("Domain does only except one " + "String-criteria!");
		} else if (criteria == null) {
			return true;
		}
		StringTokenizer st = new StringTokenizer((String) criteria[0]);
		while (st.hasMoreTokens()) {
			String c = st.nextToken();
			String s = this.getName().concat("|" + this.getType()).toLowerCase();
			if ((c == null) || (s.indexOf(c.toLowerCase()) < 0)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isTabEnabled(int no) {
		return true;
	}

	@Override
	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_NAME:
			this.setName((String) value);
			return;
		case ID_SQLTYP:
			return;
		case ID_COMMENT:
			this.setComment((String) value);
			return;
		case ID_DATATYPE:
			this.setDataType(DBUtil.GetType((String) value));
			return;
		case ID_LENGTH:
			this.setLength(((Integer) value).intValue());
			return;
		case ID_NKS:
			this.setDecimalPlace(((Integer) value).intValue());
			return;
		case ID_INITIALWERT:
			this.setInitialValue((String) value);
			return;
		case ID_HISTORY:
			this.setHistory((String) value);
			return;
		case ID_PARAMETERS:
			this.setParameters((String) value);
			return;
		}
		throw new IllegalArgumentException("Klasse Domain verfuegt nicht ueber ein Attribut " + id + " (set)!");
	}

	@Override
	public void setComment(String comment) {
		if (comment == null) {
			comment = "";
		}
		this.comment = comment;
	}

	@Override
	public void setDataType(int dt) {
		this.datatype = dt;
	}

	@Override
	public void setHistory(String newHistory) {
		this.historie = newHistory;
	}

	@Override
	public void setInitialValue(String iw) {
		this.initialwert = iw;
	}

	@Override
	public void setLength(int len) {
		this.length = len;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setDecimalPlace(int nks) {
		this.nks = nks;
	}

	@Override
	public String toListViewString() {
		return (StrUtil.PumpUp(new StringBuffer(this.getName()), " ", 20, Direction.RIGHT).append("(").append(
				this.getType()).append(") ").append(this.getInitialValue())).toString();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(this.getName()).append(" (").append(this.getType()).append(")");
		if ((this.initialwert.length() > 0) && !this.initialwert.equals("NULL")) {
			sb.append(" - ").append(this.initialwert);
		}
		return sb.toString();
	}

	public String toXMLAttributes() {
		StringBuffer sb = new StringBuffer();
		sb.append("name=\"").append(StrUtil.ToHTML(this.getName())).append("\" ");
		sb.append("datatype=\"").append(this.getDataType()).append("\" ");
		sb.append("description=\"").append(StrUtil.ToHTML(this.getComment())).append("\" ");
		sb.append("intialvalue=\"").append(StrUtil.ToHTML(this.getInitialValue())).append("\" ");
		sb.append("length=\"").append(this.getLength()).append("\" ");
		sb.append("nks=\"").append(this.getDecimalPlace()).append("\" ");
		sb.append("parameters=\"").append(StrUtil.ToHTML(this.getParameters())).append("\" ");
		return sb.toString();
	}

	@Override
	public String toXMLTag() {
		StringBuffer sb = new StringBuffer();
		sb.append("<domain ").append(this.toXMLAttributes()).append(" />");
		return sb.toString();
	}

	/**
	 * @changed OLI 23.07.2013 - Added.
	 */
	@Override
	public String getParameters() {
		return this.parameters;
	}

	/**
	 * @changed OLI 23.07.2013 - Added.
	 */
	@Override
	public void setParameters(String parameters) throws IllegalArgumentException {
		ensure(parameters != null, "parameters cannot be null.");
		this.parameters = parameters;
	}

	/**
	 * @changed OLI 24.05.2016 - Added.
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
	 * @changed OLI 25.05.2016 - Added.
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
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public void addOption(OptionModel option) {
		ensure(option != null, "option to add cannot be null.");
		for (OptionModel o : this.getOptions()) {
			if (o.equals(option)) {
				return;
			}
		}
		this.setParameters(this.getParameters()
				+ (this.getParameters().length() > 0 ? "|" : "")
				+ option.getName()
				+ ((option.getParameter() != null) && !option.getParameter().isEmpty() ? ":" + option.getParameter()
						: ""));
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public OptionModel getOptionAt(int i) {
		return this.getOptions()[i];
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
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
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public int getOptionCount() {
		return this.getOptions().length;
	}

	/**
	 * @changed OLI 26.05.2016 - Added.
	 */
	@Override
	public boolean isOptionSet(String optionName) {
		return this.getOptionByName(optionName) != null;
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public void removeOption(OptionModel option) {
		OptionModel[] os = this.getOptions();
		this.setParameters("");
		for (OptionModel o : os) {
			if (!o.equals(option)) {
				this.addOption(o);
			}
		}
	}

}
