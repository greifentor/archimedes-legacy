package archimedes.updater;

import java.util.Arrays;

import archimedes.meta.MetaDataModel;
import archimedes.meta.MetaDataModelComparator;
import archimedes.meta.chops.AbstractChangeOperation;
import archimedes.util.NameGenerator;
import corent.db.DBExecMode;

/**
 * A class which is able to update an Archimedes model by a given source.
 *
 * @author ollie (07.02.2021)
 */
public class ModelUpdater {

	private MetaDataModel toUpdate;
	private MetaDataModel source;

	/**
	 * Creates a new model updater object with passed parameters.
	 * 
	 * @param toUpdate The model to update.
	 * @param source   The source for the model to update.
	 */
	public ModelUpdater(MetaDataModel toUpdate, MetaDataModel source) {
		super();
		this.toUpdate = toUpdate;
		this.source = source;
	}

	/**
	 * Updates the model to update by the information of the source model.
	 */
	public void update() {
		System.out.println("GOTCHA!!!");
		AbstractChangeOperation[] chops =
				new MetaDataModelComparator(new NameGenerator(DBExecMode.STANDARDSQL)).compare(toUpdate, source);
		Arrays.asList(chops).forEach(chop -> System.out.println(chop));
	}

}