package archimedes.codegenerators.gui.vaadin;

import java.util.List;
import java.util.stream.Collectors;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.localization.LocalizationNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

public abstract class AbstractGUIVaadinClassCodeGenerator
		extends AbstractClassCodeGenerator<GUIVaadinNameGenerator> {

	public static final String NAME_FIELD = "NAME_FIELD";

	protected static final LocalizationNameGenerator localizationNameGenerator = new LocalizationNameGenerator();
	protected static final ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public AbstractGUIVaadinClassCodeGenerator(String templateFileName, AbstractCodeFactory codeFactory) {
		super(templateFileName, GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	protected List<GUIReferenceData> getGUIReferenceData(TableModel table) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> column.getReferencedTable() != null)
				.map(this::createGUIReferenceData)
				.collect(Collectors.toList());
	}

	protected GUIReferenceData createGUIReferenceData(ColumnModel column) {
		DataModel model = column.getTable().getDataModel();
		TableModel referencedTable = column.getReferencedTable();
		String serviceInterfaceName = serviceNameGenerator.getServiceInterfaceName(referencedTable);
		return new GUIReferenceData()
				.setFieldNameCamelCase(nameGenerator.getCamelCase(nameGenerator.getAttributeName(column)))
				.setReferencedModelClassName(serviceNameGenerator.getModelClassName(referencedTable))
				.setReferencedModelNameFieldName(getNameFieldName(referencedTable))
				.setReferencedModelPackageName(serviceNameGenerator.getModelPackageName(model, referencedTable))
				.setServiceAttributeName(nameGenerator.getAttributeName(serviceInterfaceName))
				.setServiceInterfaceName(serviceInterfaceName)
				.setServicePackageName(serviceNameGenerator.getServiceInterfacePackageName(model, referencedTable));
	}

	protected String getNameFieldName(TableModel table) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> column.isOptionSet(NAME_FIELD))
				.map(column -> nameGenerator.getCamelCase(column.getName()))
				.findFirst()
				.orElse("FIELD_NAME");
	}

}