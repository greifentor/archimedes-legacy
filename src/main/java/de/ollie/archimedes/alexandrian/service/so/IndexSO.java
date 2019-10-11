package de.ollie.archimedes.alexandrian.service.so;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A container for database index information.
 *
 * @author ollie (27.09.2019)
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class IndexSO {

	private List<ColumnSO> columns = new ArrayList<>();
	private String name;

}