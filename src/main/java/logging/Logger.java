package logging;

import org.apache.logging.log4j.LogManager;

/**
 * A logger wrapper.
 *
 * @author ollie (22.06.2020)
 */
public class Logger {

	public static Logger getLogger(Class<?> cls) {
		return new Logger(cls);
	}

	private org.apache.logging.log4j.Logger logger = null;

	public Logger(Class<?> cls) {
		super();
		logger = LogManager.getLogger(cls);
	}

	public void debug(String message) {
		logger.debug(message);
	}

	public void error(String message) {
		logger.error(message);
	}

	public void error(String message, Throwable throwable) {
		logger.error(message, throwable);
	}

	public void info(String message) {
		logger.info(message);
	}

	public void warn(String message) {
		logger.warn(message);
	}

}