/*
 * Sequence.java
 *
 * 23.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;

import java.util.StringTokenizer;

import archimedes.legacy.Archimedes;
import archimedes.legacy.gui.CommentSubEditorFactory;
import archimedes.legacy.gui.HistoryOwnerSubEditorFactory;
import archimedes.legacy.model.SequenceModel;
import corent.base.Attributed;
import corent.base.Direction;
import corent.base.StrUtil;
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

/**
 * A default implementation of the sequence mode interface.
 * 
 * @author ollie
 * 
 * @changed OLI 23.04.2013 - Added.
 */

public class Sequence implements Attributed, SequenceModel, ListViewSelectable, TabbedEditable {

	/** An identifier for teh access to the name. */
	public static final int ID_NAME = 0;
	/** An identifier for the access to the start value. */
	public static final int ID_START_VALUE = 1;
	/** An identifier for the access to the increment value. */
	public static final int ID_INCREMENT = 2;
	/** An identifier for the access to the description. */
	public static final int ID_COMMENT = 3;
	/** An identifier for the access to the change history. */
	public static final int ID_HISTORY = 4;

	/* A counter for the number of instances created. */
	private static int count = 0;

	private String comment = "";
	private String history = "";
	private long increment = 1;
	private String name = "Sequence" + count++;
	private long startValue = 1;

	/**
	 * Creates a new sequence with default values.
	 * 
	 * @changed OLI 24.04.2013 - Added.
	 */
	public Sequence() {
		super();
	}

	/**
	 * Creates a new sequence with default values.
	 * 
	 * @param name
	 *            The name of the new sequence.
	 * @param startValue
	 *            The start value of the new sequence.
	 * @param increment
	 *            The increment of the new sequence.
	 * 
	 * @changed OLI 24.04.2013 - Added.
	 */
	public Sequence(String name, long startValue, long increment) {
		super();
		this.setIncrement(increment);
		this.setName(name);
		this.setStartValue(startValue);
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public String getComment() {
		return this.comment;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public long getIncrement() {
		return this.increment;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public long getStartValue() {
		return this.startValue;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
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
			String s = this.getName().concat("|" + this.getIncrement()).concat("|" + this.getStartValue())
					.toLowerCase();
			if ((c == null) || (s.indexOf(c.toLowerCase()) < 0)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public void setIncrement(long increment) throws IllegalArgumentException {
		ensure(increment > 0, "increment value cannot be lesser one.");
		this.increment = increment;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public void setName(String name) throws IllegalArgumentException {
		ensure(name != null, "name cannot be null.");
		ensure(!name.isEmpty(), "name cannot be empty.");
		this.name = name;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public void setStartValue(long startValue) throws IllegalArgumentException {
		ensure(startValue >= 0, "increment value cannot be lesser zero.");
		this.startValue = startValue;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public String toListViewString() {
		return (StrUtil.PumpUp(new StringBuffer(this.getName()), " ", 40, Direction.RIGHT).append("(").append(
				this.getStartValue()).append(") ").append(this.getIncrement())).toString();
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_NAME:
			return this.getName();
		case ID_COMMENT:
			return this.getComment();
		case ID_HISTORY:
			return this.getHistory();
		case ID_INCREMENT:
			return new Long(this.getIncrement());
		case ID_START_VALUE:
			return new Long(this.getStartValue());
		}
		throw new IllegalArgumentException("Klasse Sequenz verfuegt nicht ueber ein Attribut " + id + " (get)!");
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_NAME:
			this.setName((String) value);
			return;
		case ID_COMMENT:
			this.setComment((String) value);
			return;
		case ID_HISTORY:
			this.setHistory((String) value);
			return;
		case ID_INCREMENT:
			this.setIncrement(((Long) value).intValue());
			return;
		case ID_START_VALUE:
			this.setStartValue(((Long) value).intValue());
			return;
		}
		throw new IllegalArgumentException("Klasse Sequenz verfuegt nicht ueber ein Attribut " + id + " (set)!");
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public int compareTo(Object o) {
		Sequence seq = (Sequence) o;
		return this.getName().compareTo(seq.getName());
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public String getHistory() {
		return this.history;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public void setHistory(String newHistory) {
		this.history = newHistory;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public TabbedPaneFactory getTabbedPaneFactory() {
		return new DefaultTabbedPaneFactory(new TabDescriptor[] { new DefaultTabDescriptor("Daten", 'A', null),
				new DefaultTabDescriptor("Beschreibung", 'B', null), new DefaultTabDescriptor("Historie", 'H', null) });
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public boolean isTabEnabled(int no) {
		return true;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public Object createObject() {
		return Archimedes.Factory.createSequence("Sequenz" + count, 1, 1);
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public Object createObject(Object blueprint) throws ClassCastException {
		if (!(blueprint instanceof Sequence)) {
			throw new ClassCastException("Instance of Sequence required!");
		}
		Sequence source = (Sequence) blueprint;
		Sequence seq = (Sequence) this.createObject();
		seq.setComment(source.getComment());
		seq.setHistory(source.getHistory());
		seq.setIncrement(source.getIncrement());
		seq.setName(source.getName());
		seq.setStartValue(source.getStartValue());
		return seq;
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Override
	public EditorDescriptorList getEditorDescriptorList() {
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_NAME, dlf, dcf, "Name", 'N', null,
				"Der Name der Domain"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_START_VALUE, dlf, dcf, "Startwert", 'W', null,
				"Der Startwert der Sequenz."));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_INCREMENT, dlf, dcf, "Increment", 'I', null,
				"Das Increment f&uuml;r die Sequenz."));
		dedl.addElement(new DefaultSubEditorDescriptor(1, this, new CommentSubEditorFactory()));
		dedl.addElement(new DefaultSubEditorDescriptor(2, this, new HistoryOwnerSubEditorFactory()));
		return dedl;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Sequence)) {
			return false;
		}
		Sequence seq = (Sequence) o;
		return this.getName().equals(seq.getName()) && (this.getIncrement() == seq.getIncrement())
				&& (this.getStartValue() == seq.getStartValue());
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @changed OLI 16.10.2013 - Added.
	 */
	@Override
	public String toString() {
		return this.getName();
	}

}