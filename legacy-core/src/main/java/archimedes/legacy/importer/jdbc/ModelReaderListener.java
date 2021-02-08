package archimedes.legacy.importer.jdbc;

/**
 * An interface which is to implement by objects observing a model reader.
 * 
 * @author ollie (12.06.2020)
 *
 */
public interface ModelReaderListener {

	/**
	 * Is called if a model reader fires an event.
	 * 
	 * @param event The event fired by the model reader.
	 */
	void eventCaught(ModelReaderEvent event);

}