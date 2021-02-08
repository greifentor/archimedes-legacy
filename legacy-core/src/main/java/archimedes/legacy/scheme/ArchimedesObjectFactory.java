/*
 * ArchimedesObjectFactory.java
 *
 * 20.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.scheme;

import gengen.metadata.ClassMetaData;

import java.awt.Color;

import archimedes.legacy.Archimedes;
import archimedes.legacy.gui.ToStringContainer;
import archimedes.legacy.model.TabellenModel;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.IndexMetaData;
import archimedes.model.NReferenceModel;
import archimedes.model.OrderMemberModel;
import archimedes.model.PanelModel;
import archimedes.model.RelationModel;
import archimedes.model.SequenceModel;
import archimedes.model.StereotypeModel;
import archimedes.model.TableModel;
import archimedes.model.ToStringContainerModel;
import archimedes.model.ViewModel;
import archimedes.scheme.xml.ObjectFactory;
import corent.base.Direction;
import corent.gui.AbstractExtendedColorPalette;

/**
 * An implementation of the object factory interface for Archimedes (version 1).
 * 
 * @author ollie
 * 
 * @changed OLI 20.06.2016 - Added.
 */

public class ArchimedesObjectFactory implements ObjectFactory {

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public ColumnModel createColumn(String name, DomainModel domain) {
		return new Tabellenspalte(name, domain);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public DataModel createDataModel() {
		return new Diagramm();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public DomainModel createDomain(String name, int type, int len, int fpp) {
		return new Domain(name, type, len, fpp);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public IndexMetaData createIndexMetaData(String name, ClassMetaData table) {
		return new DefaultIndexMetaData(name, table);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public ViewModel createMainView(String name, String comment, boolean showReferences) {
		return new MainView(name, comment, showReferences);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public NReferenceModel createNReference(TableModel t) {
		return new NReferenz((TabellenModel) t);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public OrderMemberModel createOrderMember(ColumnModel c) {
		return new OrderMember(c);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public PanelModel createPanel() {
		return new Panel();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public RelationModel createRelation(ViewModel view, ColumnModel c, Direction direction1, int offset1,
			ColumnModel r, Direction direction2, int offset2) {
		return new Relation(view, c, direction1, offset1, r, direction2, offset2);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public SequenceModel createSequence(String name, int start, int increment) {
		return new Sequence(name, start, increment);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public StereotypeModel createStereotype(String name, String comment) {
		return new Stereotype(name, comment);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public TableModel createTable(DataModel dm, ViewModel view) {
		return new Tabelle(view, 0, 0, (Diagramm) dm);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public ToStringContainerModel createToStringContainer(ColumnModel c, String prefix, String suffix) {
		return new ToStringContainer(c, prefix, suffix);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public ViewModel createView() {
		return new View();
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public Color getColor(String name, Color color) {
		return this.getPalette().get(name, color);
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public AbstractExtendedColorPalette getPalette() {
		return Archimedes.PALETTE;
	}

}