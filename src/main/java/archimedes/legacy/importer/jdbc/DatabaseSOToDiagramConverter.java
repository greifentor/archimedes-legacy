package archimedes.legacy.importer.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import archimedes.legacy.Archimedes;
import archimedes.legacy.importer.jdbc.JDBCImportConnectionData.Adjustment;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.MainView;
import archimedes.legacy.scheme.Panel;
import archimedes.legacy.scheme.Relation;
import archimedes.legacy.scheme.Tabelle;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIViewModel;
import corent.base.Direction;
import corent.base.StrUtil;
import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;

/**
 * A converter which converts database service objects into diagrams.
 *
 * @author ollie (30.09.2019)
 */
public class DatabaseSOToDiagramConverter {

	private Adjustment tableAdjustment = null;

	/**
	 * Creates a new converter for database service objects with the passed parameters.
	 * 
	 * @param tableAdjustment The adjustment of the table in the GUI.
	 */
	public DatabaseSOToDiagramConverter(Adjustment tableAdjustment) {
		super();
		this.tableAdjustment = (tableAdjustment == null ? Adjustment.LEFT_BY_NAME : tableAdjustment);
	}

	/**
	 * Converts a database service object into a diagram.
	 * 
	 * @param database The database service object to convert.
	 * @return A diagram with the data of the passed database service object.
	 */
	public DiagrammModel convert(DatabaseSO database) {
		DiagrammModel diagram = new Diagramm();
		ViewModel mainView = new MainView("Gesamt", "Gesamtsicht", true);
		diagram.addView((GUIViewModel) mainView);
		int x = 50; // adjusted on the left (default).
		int y = 50;
		if (this.tableAdjustment == Adjustment.CENTER_BY_REFERENCE_COUNT) {
			x = 5250;
		}
		SchemeSO scheme = database.getSchemes().get(0);
		List<TableSO> l = scheme.getTables() //
				.stream() //
				.sorted((t0, t1) -> tableAdjustment == Adjustment.CENTER_BY_REFERENCE_COUNT //
						? getRelations(t1, scheme) - getRelations(t0, scheme) //
						: t0.getName().compareTo(t1.getName())) //
				.collect(Collectors.toList());
		int ff = 0;
		for (TableSO table : l) {
			Tabelle tabelle = createTabelle(table, mainView, x, y, diagram);
			tabelle.setFontColor(Archimedes.PALETTE.get("schwarz"));
			tabelle.setBackgroundColor(Archimedes.PALETTE.get(StrUtil.FromHTML("wei&szlig;")));
			diagram.addTable(tabelle);
			y += 300;
			if (y > 5700) {
				y = 50;
				if (this.tableAdjustment == Adjustment.CENTER_BY_REFERENCE_COUNT) {
					ff++;
					if (ff % 2 == 0) {
						x += 500 * ff;
					} else {
						x -= 500 * ff;
					}
				} else {
					x += 500;
				}
			}
		}
		createForeignKeys(database.getSchemes().get(0).getTables(), diagram, mainView);
		return diagram;
	}

	private int getRelations(TableSO t, SchemeSO scheme) {
		int cnt = 0;
		for (ForeignKeySO fk : t.getForeignKeys()) {
			cnt += fk.getReferences().size();
		}
		cnt += getReferencingRelationCount(t, scheme);
		return cnt;
	}

	private int getReferencingRelationCount(TableSO table, SchemeSO scheme) {
		int cnt = 0;
		for (TableSO t : scheme.getTables()) {
			for (ForeignKeySO fk : t.getForeignKeys()) {
				for (ReferenceSO r : fk.getReferences()) {
					if (r.getReferencedColumn().getTable() == table) {
						cnt++;
					}
				}
			}
		}
		return cnt;
	}

	private Tabelle createTabelle(TableSO table, ViewModel mainView, int x, int y, DiagrammModel diagram) {
		Tabelle t = new Tabelle(mainView, x, y, diagram);
		t.setCodeFolder("");
		t.setDraft(false);
		t.setName(table.getName());
		Panel p = new Panel();
		p.setPanelNumber(0);
		t.addPanel(p);
		for (ColumnSO column : table.getColumns()) {
			DomainModel d = getDomain(column, diagram);
			ColumnModel c = new Tabellenspalte(column.getName(), d, column.isPkMember());
			c.setNotNull(!column.isNullable());
			c.setPanel(p);
			t.addColumn(c);
		}
		return t;
	}

	private DomainModel getDomain(ColumnSO column, DiagrammModel diagram) {
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