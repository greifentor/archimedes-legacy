package archimedes.legacy.importer;

import java.util.Map;
import java.util.stream.Collectors;

import archimedes.acf.util.OptionFromStringConverter;
import archimedes.acf.util.ParameterUtil;
import archimedes.model.OptionModel;
import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import de.ollie.archimedes.alexandrian.service.so.TypeSO;
import de.ollie.dbcomp.util.TypeConverter;

/**
 * A generator for model exploration reports.
 * 
 * @author ollie (01.03.2021)
 *
 */
public class JDBCModelExplorerReportGenerator {

	private static final TypeConverter TYPE_CONVERTER = new TypeConverter();

	private OptionFromStringConverter optionFromStringConverter = new OptionFromStringConverter();

	private String schemePattern;
	private DatabaseSO database;
	private Map<String, OptionModel> options;

	public JDBCModelExplorerReportGenerator(String schemePattern, DatabaseSO database, String optionStr)   {
		this.database = database;
		this.options = new ParameterUtil()
				.getParameters(optionStr != null ? optionStr : "")
				.stream()
				.map(optionFromStringConverter::convert)
				.collect(Collectors.toMap(OptionModel::getName, o -> o));
		this.database = database;
	}

	public String createReport() {
		String report = (schemePattern != null
				? "Scheme Pattern: " + (schemePattern.isEmpty() ? "%" : schemePattern) + "\n\n"
				: "")
				+ database
						.getSchemes()
						.stream()
						.sorted((s0, s1) -> getName(s0).compareTo(getName(s1)))
						.map(this::getString)
						.reduce((s0, s1) -> s0 + "\n" + s1)
						.orElse("No Schemes");
		return options.containsKey("TO_UPPER_CASE") ? report.toUpperCase() : report;
	}

	private String getName(SchemeSO s) {
		return toUpperCaseByOption(s.getName());
	}

	private String toUpperCaseByOption(String s) {
		return options.containsKey("TO_UPPER_CASE") ? s.toUpperCase() : s;
	}

	private String getString(SchemeSO scheme) {
		return scheme.getName() + "\n"
				+ scheme
						.getTables()
						.stream()
						.sorted((t0, t1) -> getName(t0).compareTo(getName(t1)))
						.map(this::getString)
						.reduce((t0, t1) -> t0 + "\n" + t1)
						.orElse("No Tables")
				+ "\n";
	}

	private String getName(TableSO t) {
		return toUpperCaseByOption(t.getName());
	}

	private String getString(TableSO table) {
		return "- " + table.getName()
				+ "\n"
				+ table
						.getColumns()
						.stream()
						.sorted((c0, c1) -> getName(c0).compareTo(getName(c1)))
						.map(this::getString)
						.reduce((c0, c1) -> c0 + "\n" + c1)
						.orElse("No Columns")
				+ "\n"
				+ table
						.getForeignKeys()
						.stream()
						.sorted((fk0, fk1) -> getName(fk0).compareTo(getName(fk1)))
						.map(this::getString)
						.reduce((fk0, fk1) -> fk0 + "\n" + fk1)
						.orElse("  No Foreign Keys");
	}

	private String getName(ColumnSO c) {
		return toUpperCaseByOption(c.getName());
	}

	private String getName(ForeignKeySO fk) {
		return toUpperCaseByOption(
				options.containsKey("FK_BY_COLUMNNAME")
						? fk.getReferences().get(0).getReferencingColumn().getNameWithTable()
						: fk.getName());
	}

	private String getString(ColumnSO column) {
		return "  + " + column.getName()
				+ " ("
				+ getString(column.getType())
				+ ")"
				+ (column.isPkMember() ? " PRIMARY KEY" : "")
				+ (!column.isNullable() ? " NOT NULL" : "")
				+ (column.isUnique() ? " UNIQUE" : "");
	}

	private String getString(TypeSO type) {
		return TYPE_CONVERTER.convert(type.getSqlType()) + getLengthAndPrecisionString(type);
	}

	private String getLengthAndPrecisionString(TypeSO type) {
		if (options.containsKey("SUPPRESS_LENGTH")) {
			return "()";
		}
		return (type.getLength() != null) && (type.getLength() > 0)
				? " (" + type.getLength() + getPrecisionString(type) + ")"
				: "";
	}

	private String getPrecisionString(TypeSO type) {
		return (type.getPrecision() != null) && (type.getPrecision() > 0) ? ", " + type.getPrecision() : "";
	}

	private String getString(ForeignKeySO fk) {
		return "  FK: " + getName(fk)
				+ "\n"
				+ fk
						.getReferences()
						.stream()
						.sorted(
								(s0, s1) -> s0
										.getReferencedColumn()
										.getNameWithTable()
										.compareTo(s1.getReferencedColumn().getNameWithTable()))
						.map(this::getString)
						.reduce((s0, s1) -> s0 + "\n" + s1)
						.orElse("No References");
	}

	private String getString(ReferenceSO reference) {
		return "  + " + reference.getReferencingColumn().getNameWithTable()
				+ " -> "
				+ reference.getReferencedColumn().getNameWithTable();
	}

}