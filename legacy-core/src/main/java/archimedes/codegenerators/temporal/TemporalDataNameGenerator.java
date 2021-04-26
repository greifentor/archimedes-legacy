package archimedes.codegenerators.temporal;

import archimedes.codegenerators.NameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * Specialization of the name generator for REST controllers.
 *
 * @author ollie (13.03.2021)
 */
public class TemporalDataNameGenerator extends NameGenerator {

	public String getIdSOClassName(TableModel table) {
		return table != null ? getClassName(table) + "IdSO" : null;
	}

	public String getIdSOClassPackageName(DataModel model, TableModel table) {
		return model != null ? getBasePackageNameWithDotExtension(model, table) + "service" : null;
	}

	public String getPersistencePortAdapterGeneratedClassName(TableModel table) {
		return table != null ? getClassName(table) + "PersistencePortAdapterGenerated" : null;
	}

	public String getPersistencePortGeneratedInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + "PersistencePortGenerated" : null;
	}

	public String getPersistencePortInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + "PersistencePort" : null;
	}

	public String getPersistencePortAdapterPackageName(DataModel model, TableModel table) {
		return model != null ? getBasePackageNameWithDotExtension(model, table) + "persistence" : null;
	}

	public String getPersistencePortPackageName(DataModel model, TableModel table) {
		return model != null ? getBasePackageNameWithDotExtension(model, table) + "service.ports" : null;
	}

	public String getServiceGeneratedInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + "ServiceGenerated" : null;
	}

	public String getServiceInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + "Service" : null;
	}

	public String getServiceImplGeneratedClassName(TableModel table) {
		return table != null ? getClassName(table) + "ServiceImplGenerated" : null;
	}

	public String getServiceImplPackageName(DataModel model, TableModel table) {
		return model != null ? getBasePackageNameWithDotExtension(model, table) + "service.impl" : null;
	}

	public String getServicePackageName(DataModel model, TableModel table) {
		return model != null ? getBasePackageNameWithDotExtension(model, table) + "service" : null;
	}

	// -----------------------------------------------------------------------------------------------------------------

	public String getDTOConverterClassName(TableModel table) {
		return table != null ? getClassName(table) + "DTOConverter" : null;
	}

	public String getDTOConverterPackageName(DataModel model, TableModel table) {
		return model != null ? getBasePackageNameWithDotExtension(model, table) + "rest.converter" : null;
	}

	public String getListDTOClassName(TableModel table) {
		return table != null ? getClassName(table) + "ListDTO" : null;
	}

	public String getRESTControllerClassName(TableModel table) {
		return table != null ? getClassName(table) + "RESTController" : null;
	}

	public String getRESTControllerPackageName(DataModel model, TableModel table) {
		return model != null ? getBasePackageNameWithDotExtension(model, table) + "rest" : null;
	}

}