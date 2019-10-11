package archimedes.legacy.importer.jdbc;

import java.util.List;

import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.Relation;
import archimedes.legacy.scheme.Tabelle;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.legacy.scheme.View;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIViewModel;
import corent.base.Direction;
import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import de.ollie.archimedes.alexandrian.service.so.TypeSO;

/**
 * A converter which converts database service objects into diagrams.
 *
 * @author ollie (30.09.2019)
 */
public class DatabaseSOToDiagramConverter {

	/**
	 * Converts a database service object into a diagram.
	 * 
	 * @param database The database service object to convert.
	 * @return A diagram with the data of the passed database service object.
	 */
	public DiagrammModel convert(DatabaseSO database) {
		DiagrammModel diagram = new Diagramm();
		ViewModel mainView = new View("Gesamt", "Gesamtsicht", true);
		diagram.addView((GUIViewModel) mainView);
		int x = 50;
		int y = 50;
		for (TableSO table : database.getSchemes().get(0).getTables()) {
			diagram.addTable(createTabelle(table, mainView, x, y, diagram));
			y += 50;
			if (y > 1000) {
				y = 50;
				x += 300;
			}
		}
		createForeignKeys(database.getSchemes().get(0).getTables(), diagram, mainView);
		return diagram;
	}

	private Tabelle createTabelle(TableSO table, ViewModel mainView, int x, int y, DiagrammModel diagram) {
		Tabelle t = new Tabelle(mainView, x, y, diagram);
		t.setCodeFolder("");
		t.setDraft(false);
		t.setName(table.getName());
		for (ColumnSO column : table.getColumns()) {
			DomainModel d = getDomain(column.getName(), column.getType(), diagram);
			ColumnModel c = new Tabellenspalte(column.getName(), d, column.isPkMember());
			t.addColumn(c);
		}
		return t;
	}

	private DomainModel getDomain(String columnName, TypeSO type, DiagrammModel diagram) {
		DomainModel domain = diagram.getDomainByName(columnName);
		if (domain == null) {
			System.out.println(columnName + " " + type);
			domain = new Domain(columnName, type.getSqlType(), type.getLength() == null ? -1 : type.getLength(),
					type.getPrecision() == null ? -1 : type.getPrecision());
			diagram.addDomain(domain);
		}
		return domain;
	}

	private void createForeignKeys(List<TableSO> tables, DiagrammModel diagram, ViewModel mainView) {
		for (TableSO table : tables) {
			TableModel tm = diagram.getTableByName(table.getName());
			for (ForeignKeySO foreignKey : table.getForeignKeys()) {
				for (ReferenceSO reference : foreignKey.getReferences()) {
					ColumnModel cm = tm.getColumnByName(reference.getReferencingColumn().getName());
					TableModel tmTarget = diagram.getTableByName(reference.getReferencedColumn().getTable().getName());
					ColumnModel cmTarget = tmTarget.getColumnByName(reference.getReferencedColumn().getName());
					cm.setRelation(new Relation(mainView, cm, Direction.UP, 0, cmTarget, Direction.DOWN, 0));
				}
			}
		}
	}

}