package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when an expression can't be parsed because it contains variables not defined in the current context.
 */
public class UndefinedException extends ExpressionException{

    /**
     * Initializes a new UndefinedException related to the specified variable.
     * @param varName The name of the undefined variable.
     */
    public UndefinedException(String varName){
        super("The variable \"" + varName + "\" is not defined.");
    }

}
