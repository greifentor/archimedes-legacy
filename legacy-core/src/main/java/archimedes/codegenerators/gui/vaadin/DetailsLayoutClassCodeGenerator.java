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
		String abstractMasterDataBaseLayoutClassName = nameGenerator.getAbstractMasterDataBaseLayoutClassName();
		String buttonFactoryClassName = nameGenerator.getButtonFactoryClassName(model);
		String modelClassName = serviceNameGenerator.getModelClassName(table);
		String resourceManagerInterfaceName = localizationNameGenerator.getResourceManagerInterfaceName();
		String serviceInterfaceName = getServiceInterfaceName(table);
		String sessionDataClassName = nameGenerator.getSessionDataClassName(model);
		List<GUIReferenceData> guiReferenceData = getGUIReferenceData(table);
		context.put("AbstractMasterDataBaseLayoutClassName", abstractMasterDataBaseLayoutClassName);
		context.put("ButtonFactoryClassName", buttonFactoryClassName);
		context.put("ClassName", getClassName(model, table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("GUIColumnDataCollection", getGUIColumnDataCollection(new GUIColumnDataCollection(), table));
		context.put("GUIReferences", guiReferenceData);
		context.put("ModelClassName", modelClassName);
		context.put("PackageName", getPackageName(model, table));
		context.put("ResourceManagerInterfaceName", resourceManagerInterfaceName);
		context.put("SessionDataClassName", sessionDataClassName);
		context.put("ServiceInterfaceName", serviceInterfaceName);
		importDeclarations
				.add(nameGenerator.getVaadinComponentPackageName(model), abstractMasterDataBaseLayoutClassName);
		importDeclarations.add(nameGenerator.getVaadinComponentPackageName(model), buttonFactoryClassName);
		importDeclarations
				.add(
						localizationNameGenerator.getResourceManagerPackageName(model, table),
						resourceManagerInterfaceName);
		importDeclarations.add(serviceNameGenerator.getModelPackageName(model, table), modelClassName);
		importDeclarations.add(serviceNameGenerator.getServiceInterfacePackageName(model, table), serviceInterfaceName);
		importDeclarations.add(nameGenerator.getSessionDataPackageName(model), sessionDataClassName);
		addGUIReferencesToFieldDeclarations(guiReferenceData);
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
								.map(column -> {
									String type = getType(column);
									String fieldTypeName =
											getType(
													column,
													model,
													referenceMode,
													c -> serviceNameGenerator.getModelClassName(c.getReferencedTable()),
													(c, m) -> serviceNameGenerator
															.getModelClassName(c.getDomain(), model));
									String typePackage = getTypePackage(column);
									if (GUIColumnData.TYPE_ENUM.equals(type)) {
										importDeclarations.add(typePackage, fieldTypeName);
									}
									return new GUIColumnData()
											.setFieldNameCamelCase(nameGenerator.getCamelCase(column.getName()))
											.setFieldOwnerClassName(serviceNameGenerator.getModelClassName(table))
											.setFieldTypeName(fieldTypeName)
											.setMax(getMax(column))
											.setMin(getMin(column))
											.setPosition(getPosition(column))
											.setResourceName(nameGenerator.getAttributeName(column).toLowerCase())
											.setSimpleBoolean(isSimpleBoolean(column))
											.setType(getType(column))
											.setTypePackage(typePackage);
								})
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