/*
 * SelectionMember.java
 *
 * 20.08.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.scheme;

import java.util.Arrays;
import java.util.List;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.SelectionAttribute;
import archimedes.legacy.model.SelectionMemberModel;
import corent.base.Attributed;
import corent.base.StrUtil;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.Editable;
import corent.djinn.EditorDescriptorList;

/**
 * A default implementation of the <CODE>SelectionMemberModel</CODE> interface.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 20.08.2013 - Added.
 * @changed OLI 02.08.2016 - Extended by the "printExpression".
 */

public class SelectionMember implements Attributed, Editable, SelectionMemberModel {

	public static final int ID_ATTRIBUTE = 0;
	public static final int ID_COLUMN = 1;
	public static final int ID_PRINT_EXPRESSION = 2;

	private SelectionAttribute attribute = SelectionAttribute.OPTIONAL;
	private ColumnModel column = null;
	private String printExpression = null;

	/**
	 * Creates a new selection member with the passed parameters.
	 *
	 * @param column          The column which should be in the selection view.
	 * @param attribute       The attribute of the selection.
	 * @param printExpression The print expression for the selection member (or <CODE>null</CODE> or an empty String if
	 *                        none is defined for the selection member).
	 *
	 * @changed OLI 30.12.2015 - Added.
	 * @changed OLI 02.08.2016 - Added the parameter "printExpression".
	 */
	public SelectionMember(ColumnModel column, SelectionAttribute attribute, String printExpression) {
		super();
		this.attribute = attribute;
		this.column = column;
		this.printExpression = printExpression;
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public Object createObject() {
		return new SelectionMember(null, SelectionAttribute.OPTIONAL, "");
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public Object createObject(Object blueprint) throws ClassCastException {
		SelectionMemberModel s0 = (SelectionMemberModel) blueprint;
		return new SelectionMember(s0.getColumn(), s0.getAttribute(), s0.getPrintExpression());
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_ATTRIBUTE:
			return this.getAttribute();
		case ID_COLUMN:
			return this.getColumn();
		case ID_PRINT_EXPRESSION:
			return this.getPrintExpression();
		}
		throw new IllegalArgumentException("Class SelectionMember doesn't owns an attribute: " + id + " (get)!");
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public SelectionAttribute getAttribute() {
		return this.attribute;
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public ColumnModel getColumn() {
		return this.column;
	}

	/**
	 * @changed OLI 02.08.2016 - Added.
	 */
	@Override
	public String getPrintExpression() {
		return this.printExpression;
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public EditorDescriptorList getEditorDescriptorList() {
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		List<ColumnModel> columns = Arrays.asList(this.getColumn().getTable().getDataModel().getAllColumns());
		DefaultComponentFactory dcfcol = new DefaultComponentFactory(columns);
		DefaultComponentFactory dcfdir = new DefaultComponentFactory(Arrays.asList(SelectionAttribute.values()));
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_COLUMN, dlf, dcfcol, "Tabellenspalte", 'T', null,
				StrUtil.FromHTML("Die Tabellenspalte, die in der " + "Auswahl angezeigt werden soll."), true));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_ATTRIBUTE, dlf, dcfdir, "Attribut", 'A', null,
				StrUtil.FromHTML("Das Attribut zur Tabellenspalte.")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_PRINT_EXPRESSION, dlf, dcf, "Print expression", 'P',
				null, StrUtil.FromHTML("A print expression for " + "the selection member.")));
		return dedl;
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_ATTRIBUTE:
			this.setAttribute((SelectionAttribute) value);
			return;
		case ID_COLUMN:
			this.setColumn((ColumnModel) value);
			return;
		case ID_PRINT_EXPRESSION:
			this.setPrintExpression((String) value);
			return;
		}
		throw new IllegalArgumentException("Class SelectionMember doesn't owns an attribute: " + id + " (set)!");
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public void setAttribute(SelectionAttribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public void setColumn(ColumnModel column) {
		this.column = column;
	}

	/**
	 * @changed OLI 02.08.2016 - Added.
	 */
	@Override
	public void setPrintExpression(String printExpression) {
		this.printExpression = printExpression;
	}

	/**
	 * @changed OLI 20.08.2013 - Added.
	 */
	@Override
	public String toString() {
		return this.getColumn().getFullName() + " (" + this.getAttribute() + ")"
				+ ((this.getPrintExpression() != null) && !this.getPrintExpression().isEmpty()
						? " -> " + this.getPrintExpression()
						: "");
	}

}