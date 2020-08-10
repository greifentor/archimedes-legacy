/*
 * OptionListSubEditor.java
 *
 * 15.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.OptionListProvider;
import archimedes.legacy.model.OptionModel;
import archimedes.legacy.model.OptionType;
import archimedes.legacy.model.PredeterminedOptionProvider;
import archimedes.legacy.scheme.Option;
import baccara.gui.GUIBundle;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;
import baccara.gui.generics.EditorFrameListener;
import corent.base.Attributed;
import corent.base.Constants;
import corent.djinn.SubEditor;
import corent.djinn.VectorPanelButtonFactory;

/**
 * 
 * 
 * @author ollie
 * 
 * @changed OLI 15.10.2013 - Added.
 */

public class OptionListSubEditor implements ActionListener, EditorFrameListener<EditorFrameEvent<?, ? extends Window>>,
		SubEditor {

	private JButton buttonAdd = null;
	private JButton buttonChange = null;
	private JButton buttonRemove = null;
	private JComponent[] components = null;
	private GUIBundle guiBundle = null;
	private JLabel[] labels = null;
	private JPanel panel = null;
	private JPanel[] panels = null;
	private OptionListProvider optionListProvider = null;
	private OptionType optionType = null;
	private PredeterminedOptionProvider predeterminedOptionProvider = null;
	private JTable view = null;

	/**
	 * Creates a new OptionListSubEditor with the passed parameters.
	 * 
	 * @param olp
	 *            The option list provider whose options are edited in this
	 *            editor.
	 * @param vpbf
	 *            The button factory which the panel gets its buttons from.
	 * @param guiBundle
	 *            A bundle with GUI information.
	 * @param predeterminedOptionProvider
	 *            A provider for predetermined options.
	 * @param optionType
	 *            The type of options maintained in the sub editor list.
	 */
	public OptionListSubEditor(OptionListProvider olp, VectorPanelButtonFactory vpbf, GUIBundle guiBundle,
			PredeterminedOptionProvider predeterminedOptionProvider, OptionType optionType) {
		super();
		this.guiBundle = guiBundle;
		this.optionListProvider = olp;
		this.optionType = optionType;
		this.predeterminedOptionProvider = predeterminedOptionProvider;
		this.buttonAdd = vpbf.createButtonEinfuegen();
		this.buttonAdd.addActionListener(this);
		this.buttonChange = vpbf.createButtonBearbeiten();
		this.buttonChange.addActionListener(this);
		this.buttonRemove = vpbf.createButtonEntfernen();
		this.buttonRemove.addActionListener(this);
		this.view = new JTable(new OptionListTableModel(olp));
		this.view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 2) && buttonChange.isVisible()) {
					doButtonChange();
				}
			}
		});
		JPanel panelButtons = new JPanel(new GridLayout(1, 3, Constants.HGAP, Constants.VGAP));
		panelButtons.add(this.buttonAdd);
		panelButtons.add(this.buttonRemove);
		panelButtons.add(this.buttonChange);
		JPanel panelView = new JPanel(new GridLayout(1, 1, Constants.HGAP, Constants.VGAP));
		panelView.setBorder(new EmptyBorder(1, 1, 1, 1));
		panelView.add(new JScrollPane(this.view));
		this.panel = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
		this.panel.add(panelView, BorderLayout.CENTER);
		this.panel.add(panelButtons, BorderLayout.NORTH);
		this.components = new JComponent[] { this.view };
		this.labels = new JLabel[] { new JLabel() };
		this.panels = new JPanel[] { panelView };
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonAdd) {
			this.doButtonNew();
		} else if (e.getSource() == this.buttonChange) {
			this.doButtonChange();
		} else if (e.getSource() == this.buttonRemove) {
			this.doButtonRemove();
		}
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void cleanupData() {
	}

	/**
	 * This method will be called if the user presses the change button
	 * 
	 * @changed OLI 15.10.2013 - Added.
	 */
	public void doButtonChange() {
		if (this.view.getSelectedRow() >= 0) {
			OptionModel option = (OptionModel) this.optionListProvider.getOptionAt(this.view.getSelectedRow());
			OptionMaintenanceFrame omf = new OptionMaintenanceFrame(option, this.guiBundle
					.getResourceText("option.change.title"), this.guiBundle, this.predeterminedOptionProvider,
					this.optionType);
			omf.addEditorFrameListener(this);
			omf.setVisible(true);
		}
	}

	/**
	 * This method will be called if the user presses the new button
	 * 
	 * @changed OLI 15.10.2013 - Added.
	 */
	public void doButtonNew() {
		OptionModel option = Archimedes.Factory.createOption();
		OptionMaintenanceFrame omf = new OptionMaintenanceFrame(option, this.guiBundle
				.getResourceText("option.new.title"), this.guiBundle, this.predeterminedOptionProvider, this.optionType);
		omf.addEditorFrameListener(this);
		omf.setVisible(true);
	}

	/**
	 * This method will be called if the user presses the remove button
	 * 
	 * @changed OLI 15.10.2013 - Added.
	 */
	public void doButtonRemove() {
		if (this.view.getSelectedRow() >= 0) {
			OptionModel option = (OptionModel) this.optionListProvider.getOptionAt(this.view.getSelectedRow());
			this.optionListProvider.removeOption(option);
			this.doRepaint();
		}
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void eventFired(EditorFrameEvent<?, ? extends Window> e) {
		if (e instanceof OptionMaintenanceFrameEvent) {
			Option o = (Option) e.getEditedObject();
			if (e.getEventType() == EditorFrameEventType.OK) {
				this.optionListProvider.removeOption(o);
				this.optionListProvider.addOption(o);
			} else if (e.getEventType() == EditorFrameEventType.DELETE) {
				this.optionListProvider.removeOption(o);
			}
			this.doRepaint();
		}
	}

	private void doRepaint() {
		((AbstractTableModel) this.view.getModel()).fireTableDataChanged();
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public JComponent getComponent(int n) {
		return this.components[n];
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public int getComponentCount() {
		return this.components.length;
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public JPanel getComponentPanel(int n) {
		return this.panels[n];
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public JLabel getLabel(int n) {
		return this.labels[n];
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public JPanel getPanel() {
		return this.panel;
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	public void setObject(Attributed attr) {
		this.optionListProvider = (archimedes.legacy.model.TableModel) attr;
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void transferData() {
	}

}

class OptionListTableModel extends AbstractTableModel {

	private String[] columnname = new String[] { "Name", "Parameter" };
	private OptionListProvider olp = null;

	public OptionListTableModel(OptionListProvider olp) {
		super();
		this.olp = olp;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return new String().getClass();
	}

	@Override
	public String getColumnName(int column) {
		if ((columnname != null) && (column >= 0) && (column < columnname.length) && (columnname[column] != null)) {
			return (String) columnname[column];
		}
		return "";
	}

	@Override
	public int getRowCount() {
		return olp.getOptionCount();
	}

	@Override
	public int getColumnCount() {
		if (this.columnname != null) {
			return this.columnname.length;
		}
		return 1;
	}

	@Override
	public Object getValueAt(int row, int column) {
		OptionModel option = (OptionModel) this.olp.getOptionAt(row);
		switch (column) {
		case 0:
			return option.getName();
		case 1:
			return (option.getParameter() != null ? option.getParameter() : "");
		}
		return "<null>";
	}

}