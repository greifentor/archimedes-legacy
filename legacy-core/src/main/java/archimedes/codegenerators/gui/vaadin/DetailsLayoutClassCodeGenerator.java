package archimedes.codegenerators.gui.vaadin;

import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.ReferenceMode;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for the master data page layout.
 *
 * @author ollie (11.04.2022)
 */
public class DetailsLayoutClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	public DetailsLayoutClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("DetailsLayoutClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("AbstractMasterDataBaseLayoutClassName", nameGenerator.getAbstractMasterDataBaseLayoutClassName());
		context.put("AbstractMasterDataBaseLayoutPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ButtonFactoryClassName", nameGenerator.getButtonFactoryClassName(model));
		context.put("ButtonFactoryPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ButtonPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ClassName", getClassName(model, table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("GUIColumnDataCollection", getGUIColumnDataCollection(new GUIColumnDataCollection(), table));
		context.put("GUIReferences", getGUIReferenceData(table));
		context.put("MasterDataButtonLayoutClassName", nameGenerator.getMasterDataButtonLayoutClassName(model));
		context.put("MasterDataButtonLayoutPackageName", nameGenerator.getMasterDataButtonLayoutPackageName(model));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context.put("ModelPackageName", serviceNameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("ResourceManagerInterfaceName", localizationNameGenerator.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerPackageName",
						localizationNameGenerator.getResourceManagerPackageName(model, table));
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(model));
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
		context.put("ServiceInterfaceName", getServiceInterfaceName(table));
		context.put("ServiceInterfacePackageName", serviceNameGenerator.getServiceInterfacePackageName(model, table));
	}

	private GUIColumnDataCollection getGUIColumnDataCollection(GUIColumnDataCollection collection, TableModel table) {
		if (table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS)) {
			collection = getGUIColumnDataCollection(collection, getSuperclassTable(table));
		}
		DataModel model = table.getDataModel();
		ReferenceMode referenceMode = getReferenceMode(model, table);
		collection
				.addGUIColumnData(
						List
								.of(table.getColumns())
								.stream()
								.filter(column -> column.isOptionSet(GUI_EDITOR_POS))
								.map(
										column -> new GUIColumnData()
												.setFieldNameCamelCase(nameGenerator.getCamelCase(column.getName()))
												.setFieldOwnerClassName(serviceNameGenerator.getModelClassName(table))
												.setFieldTypeName(
														getType(
																column,
																model,
																referenceMode,
																c -> serviceNameGenerator
																		.getModelClassName(c.getReferencedTable()),
																(c, m) -> serviceNameGenerator
																		.getModelClassName(c.getDomain(), model)))
												.setMax(getMax(column))
												.setMin(getMin(column))
												.setPosition(getPosition(column))
												.setResourceName(nameGenerator.getAttributeName(column).toLowerCase())
												.setSimpleBoolean(isSimpleBoolean(column))
												.setType(getType(column))
												.setTypePackage(getTypePackage(column)))
								.collect(Collectors.toList()));
		return collection;
	}

	private String getMax(ColumnModel column) {
		return column.isOptionSet(AbstractClassCodeGenerator.MAX)
				? column.getOptionByName(AbstractClassCodeGenerator.MAX).getParameter()
				: "null";
	}

	private String getMin(ColumnModel column) {
		return column.isOptionSet(AbstractClassCodeGenerator.MIN)
				? column.getOptionByName(AbstractClassCodeGenerator.MIN).getParameter()
				: "null";
	}

	private int getPosition(ColumnModel column) {
		return column.isOptionSet(GUI_EDITOR_POS)
				? Integer.valueOf(column.getOptionByName(GUI_EDITOR_POS).getParameter())
				: 0;
	}

	private String getType(ColumnModel column) {
		if (column.getReferencedTable() != null) {
			return GUIColumnData.TYPE_COMBOBOX;
		} else if (column.getDomain().getDataType() == Types.BOOLEAN) {
			return GUIColumnData.TYPE_BOOLEAN;
		} else if (column.getDomain().isOptionSet(ENUM)) {
			return GUIColumnData.TYPE_ENUM;
		} else if (column.getDomain().getDataType() == Types.INTEGER) {
			return GUIColumnData.TYPE_INTEGER;
		} else if (((column.getDomain().getDataType() == Types.LONGVARCHAR)
				|| (column.getDomain().getDataType() == Types.VARCHAR))
				&& column.getDomain().isOptionSet(AbstractCodeGenerator.TEXT)) {
			return GUIColumnData.TYPE_TEXT;
		}
		return GUIColumnData.TYPE_STRING;
	}

	private String getTypePackage(ColumnModel column) {
		return column.getDomain().isOptionSet(ENUM)
				? serviceNameGenerator.getModelPackageName(column.getTable().getDataModel(), column)
				: null;
	}

	private String getServiceInterfaceName(TableModel table) {
		TableModel supertable = getSuperclassTable(table);
		return serviceNameGenerator.getServiceInterfaceName(supertable != null ? supertable : table);
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getDetailsLayoutClassName(model, table);
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
		return !t.isOptionSet(GENERATE_MASTER_DATA_GUI);
	}

}