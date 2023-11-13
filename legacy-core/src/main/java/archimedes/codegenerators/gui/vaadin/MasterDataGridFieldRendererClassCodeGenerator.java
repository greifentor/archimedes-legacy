package archimedes.codegenerators.gui.vaadin;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.EnumData;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for the master data grid field renderers.
 *
 * @author ollie (03.12.2022)
 */
public class MasterDataGridFieldRendererClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	public MasterDataGridFieldRendererClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("MasterDataGridFieldRendererClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(model, table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("ComponentFactoryClassName", nameGenerator.getComponentFactoryClassName(model));
		context.put("ComponentFactoryPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("EnumColumns", getEnumColumnData(table, model));
		context.put("HasEnums", hasEnums(table.getColumns()));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context
				.put(
						"ModelDescription",
						serviceNameGenerator.getDescriptionName(serviceNameGenerator.getModelClassName(table)));
		context.put("ModelClassPackageName", serviceNameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("ReferencedColumnData", getGUIReferenceData(table));
	}

	private List<EnumData> getEnumColumnData(TableModel table, DataModel model) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(this::isEnum)
				.map(
						c -> new EnumData()
								.setEnumAttributeName(serviceNameGenerator.getAttributeName(c))
								.setEnumAttributeNameCamelCase(serviceNameGenerator.getClassName(c.getName()))
								.setEnumClassName(serviceNameGenerator.getModelClassName(c.getDomain(), model))
								.setEnumPackageName(serviceNameGenerator.getModelPackageName(model, c.getDomain()))
								.setItemLabelGeneratorClassName(
										nameGenerator.getItemLabelGeneratorClassName(model, c.getDomain()))
								.setItemLabelGeneratorPackageName(
										nameGenerator.getItemLabelGeneratorPackageName(model)))
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getMasterDataGridFieldRendererClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getPageViewPackageName(model, table);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel t) {
		return t.isOptionSet(GENERATE_MASTER_DATA_GUI)
				&& model
						.findOptionByName(AbstractClassCodeGenerator.REFERENCE_MODE)
						.map(om -> om.getParameter())
						.filter(s -> "OBJECT".equals(s))
						.isPresent()
				&& hasReferencesInGrid(t) && !hasEnums(t.getColumns());
	}

	private boolean hasReferencesInGrid(TableModel table) {
		Function<ColumnModel, Boolean> isInGrid =
				hasGridFieldColumn(table)
						? column -> column.isOptionSet(PageViewClassCodeGenerator.GRID_FIELD)
						: column -> column.isOptionSet(GUI_EDITOR_POS);
		return List
				.of(table.getColumns())
				.stream()
				.anyMatch(column -> (column.getReferencedColumn() != null) && isInGrid.apply(column));
	}

	private boolean hasGridFieldColumn(TableModel table) {
		return table.getColumnsWithOption(PageViewClassCodeGenerator.GRID_FIELD).length > 0;
	}

}
