package archimedes.codegenerators.gui.vaadin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CommonImportAdder;
import archimedes.codegenerators.FieldDeclarations;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for the item label generator collection.
 *
 * @author ollie (08.10.2023)
 */
public class ItemLabelGeneratorCollectionClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	public static final String PREFERENCE = "PREFERENCE";

	public ItemLabelGeneratorCollectionClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("ItemLabelGeneratorCollectionClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		commonImportAdder = new CommonImportAdder();
		fieldDeclarations = new FieldDeclarations();
		List<ItemLabelGeneratorData> itemLabelGeneratorData = getItemLabelGeneratorData(model, table);
		context.put("ClassName", getClassName(model, table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("ItemLabelGeneratorData", itemLabelGeneratorData);
		context
				.put(
						"ItemLabelGeneratorCollectionClassName",
						nameGenerator.getItemLabelGeneratorCollectionClassName(model, table));
		context
				.put(
						"ItemLabelGeneratorCollectionPackageName",
						nameGenerator.getItemLabelGeneratorCollectionPackageName(model));
		context.put("PackageName", getPackageName(model, table));
	}

	private List<ItemLabelGeneratorData> getItemLabelGeneratorData(DataModel model, TableModel table) {
		return getAllColumns(new ArrayList<>(), table)
				.stream()
				.filter(c -> isEnum(c))
				.map(c -> c.getDomain())
				.map(
						d -> new ItemLabelGeneratorData()
								.setAttributeName(nameGenerator.getAttributeName(d.getName()))
								.setTypeName(nameGenerator.getClassName(d.getName()))
								.setTypePackageName(serviceNameGenerator.getModelPackageName(model, d)))
				.collect(Collectors.toSet())
				.stream()
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getItemLabelGeneratorCollectionClassName(model, table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getItemLabelGeneratorCollectionPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel t) {
		return !t.isOptionSet(GENERATE_MASTER_DATA_GUI);
	}

}