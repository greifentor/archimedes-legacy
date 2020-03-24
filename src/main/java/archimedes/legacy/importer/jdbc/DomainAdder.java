package archimedes.legacy.importer.jdbc;

import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Domain;
import archimedes.model.DomainModel;
import de.ollie.archimedes.alexandrian.service.so.ColumnSO;

/**
 * A class which is able to extend the domains of a diagram.
 * 
 * @author ollie (23.03.2020)
 *
 */
public class DomainAdder {

	/**
	 * Searches for a matching domain for the passed column in the diagram or creates a new one, if no matching is
	 * found.
	 * 
	 * @param column  The column which the domain is to search for.
	 * @param diagram The diagram which should provide the domain.
	 * @return Either the found or a new domain matching to the column.
	 */
	public DomainModel findOrCreateMatchingDomain(ColumnSO column, DiagrammModel diagram) {
		Domain dom = new Domain("*", column.getType().getSqlType(),
				column.getType().getLength() == null ? -1 : column.getType().getLength(),
				column.getType().getPrecision() == null ? -1 : column.getType().getPrecision());
		String typeName = dom.getType().replace("(", "").replace(")", "").replace(" ", "");
		typeName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);
		DomainModel domain = diagram.getDomainByName(typeName);
		if (domain == null) {
			domain = new Domain(typeName, column.getType().getSqlType(),
					column.getType().getLength() == null ? -1 : column.getType().getLength(),
					column.getType().getPrecision() == null ? -1 : column.getType().getPrecision());
			diagram.addDomain(domain);
		}
		return domain;
	}

}