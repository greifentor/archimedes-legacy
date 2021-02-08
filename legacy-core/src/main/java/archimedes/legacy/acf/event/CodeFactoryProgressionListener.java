package archimedes.legacy.acf.event;

/**
 * An interface for classes whose objects should be able to consume code factory progression events.
 *
 * @author ollie (12.01.2020)
 */
public interface CodeFactoryProgressionListener {

	/**
	 * Is called if an code factory progression event is detected.
	 * 
	 * @param event The event which has been detected.
	 */
	void progressionDetected(CodeFactoryProgressionEvent event);

}