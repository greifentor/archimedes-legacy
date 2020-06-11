/*
 * CodeGeneratorException.java
 *
 * 07.12.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;

import static corentx.util.Checks.*;

/**
 * A specialized exception for code generator misbehavior. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 07.12.2016 - Added.
 */

public class CodeGeneratorException extends RuntimeException {

    private Object[] additionalParameters = null;
    private String codeFactoryName = null;
    private CodeGenerator codeGenerator = null;
    private String messageResourceId = null;

    /**
     * Creates a new code generator exception with the passed parameters.
     *
     * @param codeFactoryName The name of the code factory which causes the error.
     * @param codeGenerator The code generator which causes the error.
     * @param message An English message with the cause of the exception.
     * @param messageResourceId A resource id for the message.
     * @param additionalParameters For the localized message.
     * @throws IllegalArgumentException Passing an empty string or a null pointer.
     *
     * @changed OLI 07.12.2016 - Added.
     */
    public CodeGeneratorException(String codeFactoryName, CodeGenerator codeGenerator,
            String message, String messageResourceId, Object... additionalParameters)
            throws IllegalArgumentException {
        super(message);
        ensure(codeFactoryName != null, "code factory name cannot be null.");
        ensure(!codeFactoryName.isEmpty(), "code factory name cannot be empty.");
        ensure(codeGenerator != null, "code generator cannot be empty.");
        ensure(message != null, "message cannot by null.");
        ensure(!message.isEmpty(), "message cannot by empty.");
        ensure(messageResourceId != null, "message resource id cannot by null.");
        ensure(!messageResourceId.isEmpty(), "message resource id cannot by empty.");
        this.additionalParameters = additionalParameters;
        this.codeFactoryName = codeFactoryName;
        this.codeGenerator = codeGenerator;
        this.messageResourceId = messageResourceId;
    }

    /**
     * Returns the additional parameters of the exception (e. g. to pass for localized
     * messages).
     *
     * @return The additional parameters of the exception (e. g. to pass for localized
     *         messages).
     *
     * @changed OLI 08.12.2016 - Added.
     */
    public Object[] getAdditionalParameters() {
        return this.additionalParameters;
    }

    /**
     * Returns the name of the code factory which causes the exception.
     *
     * @return The name of the code factory which causes the exception.
     *
     * @changed OLI 07.12.2016 - Added.
     */
    public String getCodeFactoryName() {
        return this.codeFactoryName;
    }

    /**
     * Returns the code generator which causes the exception.
     *
     * @return The code generator which causes the exception.
     *
     * @changed OLI 07.12.2016 - Added.
     */
    public CodeGenerator getCodeGenerator() {
        return this.codeGenerator;
    }

    /**
     * Returns a message resource id to have a localized error message.
     *
     * @return A message resource id to have a localized error message.
     *
     * @changed OLI 07.12.2016 - Added.
     */
    public String getMessageResourceId() {
        return this.messageResourceId;
    }

}