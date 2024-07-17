package archimedes.codegenerators;

import java.sql.Types;
import java.util.Set;

public class TypesUtil {

	private static final Set<Integer> NUMERICS = Set
			.of(
					Types.BIGINT,
					Types.DECIMAL,
					Types.DOUBLE,
					Types.FLOAT,
					Types.INTEGER,
					Types.NUMERIC,
					Types.REAL,
					Types.SMALLINT,
					Types.TINYINT);

	public boolean isNumericType(int type) {
		return NUMERICS.contains(type);
	}

}
