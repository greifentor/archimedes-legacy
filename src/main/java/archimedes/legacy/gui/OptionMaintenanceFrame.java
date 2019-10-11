/*
 * OptionMaintenanceFrame.java
 *
 * 15.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

import archimedes.model.OptionModel;
import archimedes.model.OptionType;
import archimedes.model.PredeterminedOptionProvider;
import baccara.gui.GUIBundle;
import baccara.gui.generics.AbstractEditorFrame;
import baccara.gui.generics.ComponentData;
import baccara.gui.generics.EditorFrameEventType;

/**
 * A maintenance frame for options.
 * 
 * @author ollie
 * 
 * @changed OLI 15.10.2013 - Added.
 */

public class OptionMaintenanceFrame extends
		AbstractEditorFrame<OptionModel, OptionMaintenanceFrame, OptionMaintenanceFrameEvent, String> implements
		KeyListener, MouseListener {

	private String action = null;
	private GUIBundle guiBundle = null;
	private OptionType optionType = null;
	private PredeterminedOptionProvider predeterminedOptionProvider = null;
	private JTextField textFieldName = null;

	/**
	 * Creates a new maintenance frame for options.
	 * 
	 * @param option
	 *            The option to edit.
	 * @param action
	 *            The action which is performed with the frame.
	 * @param guiBundle
	 *            A bundle with GUI information.
	 * 
	 * @changed OLI 15.10.2013 - Added.
	 */
	public OptionMaintenanceFrame(OptionModel option, String action, GUIBundle guiBundle,
			PredeterminedOptionProvider predeterminedOptionProvider, OptionType optionType) {
		super(option, guiBundle.getResourceText("option.maintenance.frame.title").replace("$ACTION$", action).replace(
				"$NAME$", option.getName()), guiBundle);
		this.action = action;
		this.guiBundle = guiBundle;
		this.optionType = optionType;
		this.predeterminedOptionProvider = predeterminedOptionProvider;
		Component c = this.getEditorComponent("Name");
		if (c instanceof JTextField) {
			this.textFieldName = (JTextField) c;
			this.textFieldName.addKeyListener(this);
			this.textFieldName.addMouseListener(this);
		}
	}

	/**
	 * @changed OLI 15.07.2013 - Added.
	 */
	@Override
	protected ComponentData<?>[] getComponentData(OptionModel option) {
		return new ComponentData[] {
				new ComponentData<String>("Name", baccara.gui.generics.Type.STRING, option.getName()),
				new ComponentData<String>("Parameter", baccara.gui.generics.Type.STRING, option.getParameter()) };
	}

	/**
	 * @changed OLI 15.07.2013 - Added.
	 */
	@Override
	protected void transferChangesToObject() {
		this.object.setName(this.getTextFromComponent("Name"));
		this.object.setParameter(this.getTextFromComponent("Parameter"));
	}

	/**
	 * @changed OLI 01.10.2015 - Added.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof OptionsPopupMenuItem) {
			this.textFieldName.setText(e.getActionCommand());
		} else {
			super.actionPerformed(e);
		}
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public OptionMaintenanceFrameEvent createEvent(EditorFrameEventType eventType) {
		return new OptionMaintenanceFrameEvent(eventType, this.object, this);
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	protected String getMainResourceIdentifierPrefix() {
		return "option.maintenance";
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == this.textFieldName) {
			if ((e.getKeyCode() == KeyEvent.VK_S) && e.isControlDown()) {
				new OptionsPopupMenu(this.predeterminedOptionProvider, this.optionType, this.textFieldName.getX(),
						this.textFieldName.getY(), this, this);
			} else {
				this.setTitle(this.guiBundle.getResourceText("option.maintenance.frame.title").replace("$ACTION$",
						this.action).replace("$NAME$", this.textFieldName.getText()));
			}
		}
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * @changed OLI 01.10.2015 - Added.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if ((e.getSource() == this.textFieldName) && (e.getButton() == MouseEvent.BUTTON3)) {
			if (this.predeterminedOptionProvider != null) {
				new OptionsPopupMenu(this.predeterminedOptionProvider, this.optionType, e.getX(), e.getY(), this, this);
			}
		}
	}

	/**
	 * @changed OLI 01.10.2015 - Added.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * @changed OLI 01.10.2015 - Added.
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * @changed OLI 01.10.2015 - Added.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * @changed OLI 01.10.2015 - Added.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

}