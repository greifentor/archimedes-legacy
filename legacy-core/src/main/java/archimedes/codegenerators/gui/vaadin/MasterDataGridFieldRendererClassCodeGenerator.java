package archimedes.codegenerators.gui.vaadin;

import java.util.List;
import java.util.function.Function;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
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
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context.put("ModelPackageName", serviceNameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("ReferencedColumnData", getGUIReferenceData(table));
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
		return t.isOptionSet(GENERATE_MASTER_DATA_GUI) && "OBJECT".equals(model.getOptionByName("REFERENCE_MODE"))
				&& hasReferencesInGrid(t);
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
