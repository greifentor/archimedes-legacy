package archimedes.codegenerators;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import archimedes.codegenerators.FindBys.FindByData;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionListProvider;
import archimedes.model.TableModel;

public class FindByUtils {

	public static final String FIND_BY = "FIND_BY";

	public static boolean hasUniques(ColumnModel[] columns) {
		return List.of(columns).stream().anyMatch(ColumnModel::isUnique);
	}

	public static boolean hasNotNulls(ColumnModel[] columns) {
		return List.of(columns).stream().anyMatch(column -> !column.isPrimaryKey() && column.isNotNull());
	}

	public static boolean hasNoUniques(ColumnModel[] columns) {
		return List
				.of(columns)
				.stream()
				.filter(column -> column.getOptionByName(FIND_BY) != null)
				.anyMatch(column -> !column.isUnique());
	}

	public static boolean hasObjectReferences(ColumnModel[] columns) {
		return List
				.of(columns)
				.stream()
				.filter(column -> column.getOptionByName(FIND_BY) != null)
				.anyMatch(column -> column.getReferencedTable() != null);
	}

	public static List<FindByData> getFindBys(ColumnModel[] columns, ReferenceMode referenceMode,
			NameGenerator nameGenerator, Function<TableModel, String> referenceClassNameProvider,
			Function<TableModel, String> referenceClassPackageNameProvider, TypeGenerator typeGenerator,
			BiFunction<ColumnModel, DataModel, String> enumClassNameProvider,
			Function<OptionListProvider, String> enumPackagerNameProvider) {
		return getFindBys(
				columns,
				referenceMode,
				nameGenerator,
				referenceClassNameProvider,
				referenceClassPackageNameProvider,
				t -> "UNDEFINED",
				t -> "UNDEFINED",
				typeGenerator,
				enumClassNameProvider,
				enumPackagerNameProvider);
	}

	public static List<FindByData> getFindBys(ColumnModel[] columns, ReferenceMode referenceMode,
			NameGenerator nameGenerator, Function<TableModel, String> referenceClassNameProvider,
			Function<TableModel, String> referenceClassPackageNameProvider,
			Function<TableModel, String> referenceConverterClassNameProvider,
			Function<TableModel, String> referenceConverterPackageNameProvider, TypeGenerator typeGenerator,
			BiFunction<ColumnModel, DataModel, String> enumClassNameProvider,
			Function<OptionListProvider, String> enumPackagerNameProvider) {
		return List
				.of(columns)
				.stream()
				.filter(column -> (column.getOptionByName(FIND_BY) != null) || column.isUnique())
				.map(
						column -> new FindByData()
								.setAttributeName(nameGenerator.getAttributeName(column))
								.setColumnName(nameGenerator.getCamelCase(column.getName()))
								.setConverterAttributeName(
										nameGenerator
												.getAttributeName(
														getFromProvider(
																column,
																referenceConverterClassNameProvider,
																referenceMode)))
								.setConverterClassName(
										getFromProvider(column, referenceConverterClassNameProvider, referenceMode))
								.setConverterClassName(
										getFromProvider(column, referenceConverterClassNameProvider, referenceMode))
								.setConverterPackageName(
										getFromProvider(column, referenceConverterPackageNameProvider, referenceMode))
								.setObjectReference(isObjectReference(column, referenceMode))
								.setTypePackageName(
										isEnum(column)
												? enumPackagerNameProvider.apply(column.getDomain())
												: getFromProvider(
														column,
														referenceClassPackageNameProvider,
														referenceMode))
								.setTypeName(
										getType(
												column,
												referenceMode,
												referenceClassNameProvider,
												typeGenerator,
												enumClassNameProvider))
								.setUnique(column.isUnique()))
				.collect(Collectors.toList());
	}

	private static String getFromProvider(ColumnModel column, Function<TableModel, String> provider,
			ReferenceMode referenceMode) {
		TableModel referencedTable = column.getReferencedTable();
		return (referencedTable != null) && (referenceMode == ReferenceMode.OBJECT)
				? provider.apply(referencedTable)
				: null;
	}

	private static boolean isObjectReference(ColumnModel column, ReferenceMode referenceMode) {
		return (referenceMode == ReferenceMode.OBJECT) && (column.getReferencedTable() != null);
	}

	private static String getType(ColumnModel column, ReferenceMode referenceMode,
			Function<TableModel, String> referenceClassNameProvider, TypeGenerator typeGenerator,
			BiFunction<ColumnModel, DataModel, String> enumClassNameProvider) {
		if ((column.getReferencedColumn() != null) && (referenceMode == ReferenceMode.OBJECT)) {
			return referenceClassNameProvider.apply(column.getReferencedTable());
		} else if (isEnum(column)) {
			return enumClassNameProvider.apply(column, column.getTable().getDataModel());
		}
		return typeGenerator.getJavaTypeString(column.getDomain(), NullableUtils.isNullable(column));
	}

	private static boolean isEnum(ColumnModel column) {
		return column.getDomain().isOptionSet(AbstractClassCodeGenerator.ENUM);
	}

}