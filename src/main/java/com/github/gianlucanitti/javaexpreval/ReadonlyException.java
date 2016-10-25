package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when a read-only function or variable is trying to be redefined.
 */
public class ReadonlyException extends ExpressionException {

    /**
     * Initializes a new ReadonlyException related to the specified variable or function.
     * @param name The name of the read-only function or variable.
     * @param argCount The number of arguments of the read-only function, or a negative value if it's a variable.
     */
    public ReadonlyException(String name, int argCount){
        super("The " + (argCount < 0 ? "variable" : "function") + " \"" + name + "\" is defined as read-only" + (argCount < 0 ? "" : " for " + argCount + " arguments") + ".");
    }

}
