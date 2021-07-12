package archimedes.legacy.exporter.sql;

import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static corentx.util.Checks.ensure;

public class InsertStatementGenerator {

	public String generate(ColumnModel[] columns, Object[] values) {
		ensure(columns != null, "columns array cannot be null.");
		ensure(columns.length > 0, "columns array cannot be empty.");
		ensure(values != null, "values array cannot be null.");
		ensure(values.length > 0, "values array cannot be empty.");
		ensure(columns.length == values.length, "length of columns and values array should be equal.");
		final StringBuilder sb = new StringBuilder("INSERT INTO ").append(columns[0].getTable().getName()).append(" " +
				"(");
		sb
				.append(
						Arrays.stream(columns)
								.map(ColumnModel::getName)
								.reduce((s0, s1) -> s0 + ", " + s1)
								.orElse(""));
		sb.append(") VALUES (");
		sb
				.append(
						Arrays.stream(values)
								.map(this::toSQLString)
								.reduce((s0, s1) -> s0 + ", " + s1)
								.orElse(""));
		sb.append(");");
		return sb.toString();
	}

	private String toSQLString(Object o) {
		if (o == null) {
			return "NULL";
		} else if (o instanceof String) {
			return "'" + o + "'";
		}
		return o.toString();
	}

	public Object[] createDummyData(ColumnModel[] columns) {
		if (columns == null) {
			return null;
		}
		List<Object> l = new ArrayList<>();
		for (int i = 0, leni = columns.length; i < leni; i++) {
			DomainModel domain = columns[i].getDomain();
			if (domain.getDataType() == Types.BIGINT) {
				l.add((long) i);
			} else if (domain.getDataType() == Types.BOOLEAN) {
				l.add(false);
			} else if ((domain.getDataType() == Types.DECIMAL) || (domain.getDataType() == Types.DOUBLE) ||
					(domain.getDataType() == Types.NUMERIC)) {
				l.add((double) i);
			} else if (domain.getDataType() == Types.FLOAT) {
				l.add((float) i);
			} else if (domain.getDataType() == Types.INTEGER) {
				l.add(i);
			} else if (domain.getDataType() == Types.VARCHAR) {
				l.add("String" + i);
			} else {
				l.add(null);
			}
		}
		return l.toArray();
	}

}