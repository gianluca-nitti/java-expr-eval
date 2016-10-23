package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when a read-only function or variable is trying to be redefined.
 */
public class ReadonlyException extends ExpressionException {

    public ReadonlyException(String name, boolean isFunction){
        super("The " + (isFunction ? "function" : "variable") + " \"" + name + "\" is read-only.");
    }

}
