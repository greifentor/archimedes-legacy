/*
 * OptionListSubEditorFactory.java
 *
 * 15.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.Component;
import java.util.Hashtable;

import archimedes.legacy.model.OptionListProvider;
import archimedes.legacy.model.OptionType;
import archimedes.legacy.model.PredeterminedOptionProvider;
import baccara.gui.GUIBundle;
import corent.base.Attributed;
import corent.djinn.SubEditor;
import corent.djinn.SubEditorFactory;
import corent.djinn.VectorPanelButtonFactory;

/**
 * A factory for option list sub editors.
 * 
 * @author ollie
 * 
 * @changed OLI 15.10.2013 - Added.
 */

public class OptionListSubEditorFactory implements SubEditorFactory {

	private GUIBundle guiBundle = null;
	private OptionType optionType = null;
	private PredeterminedOptionProvider predeterminedOptionProvider = null;
	private VectorPanelButtonFactory vpbf = null;

	/**
	 * Creates a new OptionListSubEditorFactory with the passed parameters.
	 * 
	 * @param vpbf
	 *            A factory which the sub editor gets the buttons from.
	 * @param guiBundle
	 *            A bundle with GUI information.
	 * @param predeterminedOptionProvider
	 *            A provider for predetermined options.
	 * @param optionType
	 *            The type of options maintained in the sub editor list.
	 * 
	 * @changed OLI 15.10.2013 - Added.
	 */
	public OptionListSubEditorFactory(VectorPanelButtonFactory vpbf, GUIBundle guiBundle,
			PredeterminedOptionProvider predeterminedOptionProvider, OptionType optionType) {
		super();
		this.guiBundle = guiBundle;
		this.optionType = optionType;
		this.predeterminedOptionProvider = predeterminedOptionProvider;
		this.vpbf = vpbf;
	}

	/**
	 * @changed OLI 15.10.2013 - Added.
	 */
	@Override
	public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, Component> components) {
		if (!(obj instanceof OptionListProvider)) {
			throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
					+ "OptionListSubEditorFactory");
		}
		return new OptionListSubEditor((OptionListProvider) obj, this.vpbf, this.guiBundle,
				this.predeterminedOptionProvider, this.optionType);
	}

}