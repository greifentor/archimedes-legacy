package archimedes.legacy.acf.event;

/**
 * An interface for classes whose objects are able to fire code factory progression events.
 *
 * @author ollie (12.01.2020)
 */
public interface CodeFactoryProgressionEventProvider {

	/**
	 * Adds the passed listener to the provider.
	 * 
	 * @param listener The listener to add to the provider.
	 */
	void addCodeFactoryProgressionListener(CodeFactoryProgressionListener listener);

	/**
	 * Removes the passed listener from the provider.
	 * 
	 * @param listener The listener to remove from the provider.
	 */
	void removeCodeFactoryProgressionListener(CodeFactoryProgressionListener listener);

}