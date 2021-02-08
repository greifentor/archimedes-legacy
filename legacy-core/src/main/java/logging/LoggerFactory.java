package logging;

/**
 * 
 *
 * @author ollie (22.06.2020)
 */
public class LoggerFactory {

	public static Logger getLogger(Class cls) {
		return new Logger(cls);
	}

}
