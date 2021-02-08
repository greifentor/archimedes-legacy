package archimedes.legacy.scheme;


import java.sql.*;

import corent.base.*;

import archimedes.legacy.*;


public class TestDiagramm extends Diagramm {
    
    public TestDiagramm() {
        super();
        this.setCodePfad("codetmp/");
        View view = new MainView("Gesamt", "Gesamtansicht", this.isShowReferencedColumns());
        this.getViews().add(view);
        Domain dom0 = (Domain) Archimedes.Factory.createDomain("Ident", Types.INTEGER, 0, 0);
        Domain dom1 = (Domain) Archimedes.Factory.createDomain("Ganzzahl", Types.INTEGER, 0, 0);
        this.addDomain(dom0);
        this.addDomain(dom1);
        Tabelle t = (Tabelle) Archimedes.Factory.createTabelle(view, 100, 100, this, true);
        t.addTabellenspalte(Archimedes.Factory.createTabellenspalte("Id", dom0, true));
        t.addTabellenspalte(Archimedes.Factory.createTabellenspalte(
                "Besonders_Langer_Spaltenname", dom1));
        Tabellenspalte ts = (Tabellenspalte) Archimedes.Factory.createTabellenspalte("Tabelle1",
                dom0);
        t.addTabellenspalte(ts);
        this.addTabelle(t);
        Tabelle t1 = (Tabelle) Archimedes.Factory.createTabelle(view, 200, 200, this, false);
        Tabellenspalte ts0 = (Tabellenspalte) Archimedes.Factory.createTabellenspalte("Id", 
                dom0, true);
        t1.addTabellenspalte(ts0);
        this.addTabelle(t1);
        ts.setRelation(Archimedes.Factory.createRelation(view, ts, Direction.DOWN, 50, ts0, 
                Direction.LEFT, 10));
        t.removeTabellenspalte(ts);
        t.addTabellenspalte(ts);
    }
    
}
