package archimedes.codegenerators.temporal;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for DTO's.
 *
 * @author ollie (10.03.2021)
 */
public class ServiceImplGeneratedClassCodeGenerator extends AbstractClassCodeGenerator<TemporalDataNameGenerator> {

	public ServiceImplGeneratedClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ServiceImplGeneratedClass.vm",
				TemporalDataCodeFactory.TEMPLATE_FOLDER_PATH,
				new TemporalDataNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		context.put("ClassName", nameGenerator.getServiceImplGeneratedClassName(table));
		context.put("ColumnData", columnData);
		context.put("DescriptionName", nameGenerator.getDescriptionName(table.getName()));
		context.put("IdSOClassName", nameGenerator.getIdSOClassName(table));
		context
				.put(
						"IdSOClassNameQualified",
						nameGenerator.getIdSOClassPackageName(model, table) + "."
								+ nameGenerator.getIdSOClassName(table));
		context.put("PersistencePortInterfaceName", nameGenerator.getPersistencePortInterfaceName(table));
		context
				.put(
						"PersistencePortInterfaceNameQualified",
						nameGenerator.getPersistencePortPackageName(model, table) + "."
								+ nameGenerator.getPersistencePortInterfaceName(table));
		context.put("ServiceInterfaceName", nameGenerator.getServiceInterfaceName(table));
		context
				.put(
						"ServiceInterfaceNameQualified",
						nameGenerator.getServicePackageName(model, table) + "."
								+ nameGenerator.getServiceInterfaceName(table));
		context.put("PackageName", getPackageName(model, table));
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns) {
		return Arrays
				.asList(columns)
				.stream()
				.filter(column -> column.isOptionSet(TemporalDataCodeFactory.TEMPORAL))
				.filter(column -> typeGenerator.getJavaTypeString(column.getDomain(), false).equals("String"))
				.map(
						column -> new ColumnData()
								.setDescriptionName(nameGenerator.getDescriptionName(column.getName()))
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false))
								.setSimpleName(nameGenerator.getClassName(column.getName())))
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getServiceImplGeneratedClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getServiceImplPackageName(model, table);
	}

}