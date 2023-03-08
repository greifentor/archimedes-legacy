package archimedes.codegenerators.gui.vaadin;

import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.CommonImportAdder;
import archimedes.codegenerators.FieldDeclarations;
import archimedes.codegenerators.ReferenceMode;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;

/**
 * A class code generator for the master data page layout.
 *
 * @author ollie (11.04.2022)
 */
public class DetailsLayoutClassCodeGenerator extends AbstractGUIVaadinClassCodeGenerator {

	public static final String PREFERENCE = "PREFERENCE";

	public DetailsLayoutClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("DetailsLayoutClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		commonImportAdder = new CommonImportAdder();
		fieldDeclarations = new FieldDeclarations();
		String abstractMasterDataBaseLayoutClassName = nameGenerator.getAbstractMasterDataBaseLayoutClassName();
		String buttonFactoryClassName = nameGenerator.getButtonFactoryClassName(model);
		String modelClassName = serviceNameGenerator.getModelClassName(table);
		String modelSuperClassName = getSuperclassName(table, serviceNameGenerator::getModelClassName);
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
		context.put("ModelSuperClassName", modelSuperClassName);
		context.put("PackageName", getPackageName(model, table));
		context.put("PreferenceData", getPreferenceData(table));
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
		if (modelSuperClassName != null) {
			importDeclarations.add(serviceNameGenerator.getModelPackageName(model, table), modelSuperClassName);
		}
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
									String fieldTypeName = getFieldTypeName(column, model, referenceMode);
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
											.setStep(getStep(column))
											.setType(getType(column))
											.setTypePackage(typePackage);
								})
								.collect(Collectors.toList()));
		return collection;
	}

	private String getFieldTypeName(ColumnModel column, DataModel model, ReferenceMode referenceMode) {
		return getType(
				column,
				model,
				referenceMode,
				c -> serviceNameGenerator.getModelClassName(c.getReferencedTable()),
				(c, m) -> serviceNameGenerator.getModelClassName(c.getDomain(), model));
	}

	private String getMax(ColumnModel column) {
		return getParameterValueFromColumn(column, AbstractClassCodeGenerator.MAX, null);
	}

	private String getParameterValueFromColumn(ColumnModel column, String parameterIdent, String defaultValue) {
		return column.isOptionSet(parameterIdent)
				? column.getOptionByName(parameterIdent).getParameter()
				: getParameterValueFromDomain(column.getDomain(), parameterIdent, defaultValue);
	}

	private String getParameterValueFromDomain(DomainModel domain, String parameterIdent, String defaultValue) {
		return domain.isOptionSet(parameterIdent) ? domain.getOptionByName(parameterIdent).getParameter() : "null";
	}

	private String getMin(ColumnModel column) {
		return getParameterValueFromColumn(column, AbstractClassCodeGenerator.MIN, null);
	}

	private String getStep(ColumnModel column) {
		return getParameterValueFromColumn(column, AbstractClassCodeGenerator.STEP, null);
	}

	private int getPosition(ColumnModel column) {
		return column.isOptionSet(GUI_EDITOR_POS)
				? Integer.valueOf(column.getOptionByName(GUI_EDITOR_POS).getParameter())
				: 0;
	}

	private String getType(ColumnModel column) {
		if (column.getReferencedTable() != null) {
			return GUIColumnData.TYPE_COMBOBOX;
		} else if (column.getDomain().getDataType() == Types.BLOB) {
			return GUIColumnData.TYPE_UPLOAD;
		} else if (column.getDomain().getDataType() == Types.BOOLEAN) {
			return GUIColumnData.TYPE_BOOLEAN;
		} else if ((column.getDomain().getDataType() == Types.DECIMAL)
				|| (column.getDomain().getDataType() == Types.DOUBLE)
				|| (column.getDomain().getDataType() == Types.FLOAT)
				|| (column.getDomain().getDataType() == Types.NUMERIC)) {
			return GUIColumnData.TYPE_NUMERIC;
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

	private List<PreferenceData> getPreferenceData(TableModel table) {
		ReferenceMode referenceMode = getReferenceMode(table.getDataModel(), table);
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> column.isOptionSet(PREFERENCE))
				.map(
						column -> new PreferenceData()
								.setAttributeName(nameGenerator.getAttributeName(column))
								.setAttributeNameCamelCase(nameGenerator.getCamelCase(column.getName()))
								.setFieldTypeName(getFieldTypeName(column, table.getDataModel(), referenceMode))
								.setFirstFieldType(getFirstFieldType(column))
								.setFirstFieldNameCamelCase(getFirstFieldNameCamelCase(column))
								.setIdColumnNameCamelCase(getIdColumnNameCamelCase(column))
								.setNextFieldType(getNextFieldType(column))
								.setNextFieldNameCamelCase(getNextFieldNameCamelCase(column))
								.setPreferenceIdName(getPreferenceIdName(column))
								.setType(getType(column)))
				.collect(Collectors.toList());
	}

	private String getIdColumnNameCamelCase(ColumnModel column) {
		ColumnModel[] pks = column.getTable().getPrimaryKeyColumns();
		if (pks.length > 0) {
			return nameGenerator
					.getCamelCase(nameGenerator.getAttributeName(column.getTable().getColumns()[0].getName()));
		}
		return "NO_PK";
	}

	private ColumnModel getFirstField(ColumnModel column) {
		return List
				.of(column.getTable().getColumns())
				.stream()
				.filter(c -> c.isOptionSet(GUI_EDITOR_POS))
				.sorted((c0, c1) -> getGuiEditorPos(c0) - getGuiEditorPos(c1))
				.findFirst()
				.orElse(null);
	}

	private String getFirstFieldType(ColumnModel column) {
		return getType(getFirstField(column));
	}

	private String getFirstFieldNameCamelCase(ColumnModel column) {
		return nameGenerator.getCamelCase(getFirstField(column).getName());
	}

	private ColumnModel getNextField(ColumnModel column) {
		List<ColumnModel> columns =
				List
						.of(column.getTable().getColumns())
						.stream()
						.filter(c -> c.isOptionSet(GUI_EDITOR_POS))
						.sorted((c0, c1) -> getGuiEditorPos(c0) - getGuiEditorPos(c1))
						.collect(Collectors.toList());
		int i = columns.indexOf(column);
		ColumnModel nextColumn = columns.get(0);
		if (i < columns.size() - 1) {
			nextColumn = columns.get(i + 1);
		}
		return nextColumn;
	}

	private String getNextFieldType(ColumnModel column) {
		return getType(getNextField(column));
	}

	private String getNextFieldNameCamelCase(ColumnModel column) {
		return nameGenerator.getCamelCase(getNextField(column).getName());
	}

	private int getGuiEditorPos(ColumnModel column) {
		if (column.isOptionSet(GUI_EDITOR_POS)) {
			return Integer.parseInt(column.getOptionByName(GUI_EDITOR_POS).getParameter());
		}
		return -1;
	}

	private String getPreferenceIdName(ColumnModel column) {
		return column.getName().toUpperCase() + "_PREFERENCE_ID";
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