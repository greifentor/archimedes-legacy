package archimedes.ucg.yaml;

public class YamlParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public YamlParseException(String message) {
		super(message);
	}

	public YamlParseException(String message, int lineNumber) {
		super(message + " (line " + lineNumber + ")");
	}

}
