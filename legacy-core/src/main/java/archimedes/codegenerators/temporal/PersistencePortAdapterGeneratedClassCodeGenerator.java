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
 * A class code generator for persistence port adapters in a temporal data.
 *
 * @author ollie (25.04.2021)
 */
public class PersistencePortAdapterGeneratedClassCodeGenerator
		extends AbstractClassCodeGenerator<TemporalDataNameGenerator> {

	public PersistencePortAdapterGeneratedClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"PersistencePortAdapterGeneratedClass.vm",
				TemporalDataCodeFactory.TEMPLATE_FOLDER_PATH,
				new TemporalDataNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		context.put("AttributeName", nameGenerator.getAttributeName(table.getName()));
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("IdSOClassName", nameGenerator.getIdSOClassName(table));
		context
				.put(
						"IdSOClassNameQualified",
						nameGenerator.getIdSOClassPackageName(model, table) + "."
								+ nameGenerator.getIdSOClassName(table));
		context.put("PackageName", getPackageName(model, table));
		context
				.put(
						"PersistencePortGeneratedInterfaceName",
						nameGenerator.getPersistencePortGeneratedInterfaceName(table));
		context
				.put(
						"PersistencePortGeneratedInterfaceNameQualified",
						nameGenerator.getPersistencePortPackageName(model, table) + "."
								+ nameGenerator.getPersistencePortGeneratedInterfaceName(table));
		context.put("SimpleClassName", nameGenerator.getClassName(table));
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
								.setEnumIdentifier(nameGenerator.getEnumIdentifier(column.getName()))
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false))
								.setSimpleName(nameGenerator.getClassName(column.getName())))
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getPersistencePortAdapterGeneratedClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getPersistencePortAdapterPackageName(model, table);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel table) {
		return super.isToIgnoreFor(model, table) || isSubclass(table) || isAMember(table);
	}

}