package com.github.gianlucanitti.javaexpreval;

/**
 * Exception thrown when an expression can't be parsed because it contains a symbol (variable or function) not defined in the current context.
 */
public class UndefinedException extends ExpressionException{

    /**
     * Initializes a new UndefinedException related to the specified variable.
     * @param varName The name of the undefined variable.
     */
    public UndefinedException(String varName){
        super("The variable \"" + varName + "\" is not defined.");
    }

    /**
     * Initializes a new UndefinedException that states that there is no function with the specified name defined for the specified number of arguments.
     * @param funcName The name of the function.
     * @param argCount The number of arguments the function is trying to be called.
     */
    public UndefinedException(String funcName, int argCount){
        super("The function \"" + funcName + "\" is not defined for " + argCount + " arguments.");
    }

}
