/*
 * CodeFactoryEvent.java
 *
 * 18.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.event;

import static corentx.util.Checks.ensure;

import archimedes.legacy.model.CodeFactory;

/**
 * An event which is fired by the <CODE>CodeFactory</CODE> implementation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class CodeFactoryEvent {

	private String baseCodePath = null;
	private CodeFactory codeFactory = null;
	private CodeFactoryEventType type = null;

	/**
	 * Creates a new event object with the passed parameters.
	 *
	 * @param type         The type of the event.
	 * @param baseCodePath The base code path which is used to create the code.
	 * @param codeFactory  The code factory which fired this event.
	 * @throws IllegalArgumentException Passing a null pointer or an empty string.
	 *
	 * @changed OLI 18.04.2013 - Added.
	 */
	public CodeFactoryEvent(CodeFactoryEventType type, String baseCodePath, CodeFactory codeFactory)
			throws IllegalArgumentException {
		super();
		ensure(baseCodePath != null, "base code path cannot be null.");
		ensure(!baseCodePath.isEmpty(), "base code path cannot be empty.");
		ensure(codeFactory != null, "code factory cannot be null.");
		ensure(type != null, "event type cannot be empty.");
		this.baseCodePath = baseCodePath;
		this.codeFactory = codeFactory;
		this.type = type;
	}

	/**
	 * Returns the base code path which is passed to the event.
	 *
	 * @return The base code path which is passed to the event.
	 *
	 * @changed OLI 18.04.2013 - Added.
	 */
	public String getBaseCodePath() {
		return this.baseCodePath;
	}

	/**
	 * Returns the code factory which fired the event.
	 *
	 * @return The code factory which fired the event.
	 *
	 * @changed OLI 07.12.2016 - Added.
	 */
	public CodeFactory getFactory() {
		return this.codeFactory;
	}

	/**
	 * Returns the type of the event.
	 *
	 * @return The type of the event.
	 *
	 * @changed OLI 18.04.2013 - Added.
	 */
	public CodeFactoryEventType getType() {
		return this.type;
	}

}