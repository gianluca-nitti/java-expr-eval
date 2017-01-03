package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when a read-only function or variable is trying to be redefined.
 */
public class ReadonlyException extends ExpressionException {

    /**
     * Initializes a new ReadonlyException related to the specified function.
     * @param name The name of the read-only function.
     * @param argCount The number of arguments of the read-only function.
     */
    public ReadonlyException(String name, int argCount){
        super(LocalizationHelper.getMessage(LocalizationHelper.Message.READONLY_FUNC, name, Integer.toString(argCount)));
    }

    /**
     * Initializes a new ReadonlyException related to the specified variable.
     * @param name The name of the read-only variable.
     */
    public ReadonlyException(String name){
        super(LocalizationHelper.getMessage(LocalizationHelper.Message.READONLY_VAR, name));
    }

}
