package com.github.gianlucanitti.javaexpreval;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * The context an expression can be evaluated in. Can contain binding between variables and their values.
 */
public class ExpressionContext {

    private HashMap<String, Double> variables;

    /**
     * Initializes an empty context.
     */
    public ExpressionContext(){
        variables = new HashMap<String, Double>();
    }

    /**
     * Returns, if existing, the value of the specified variable.
     * @param varName The name of the variable.
     * @return The value of the specified variable, if defined in this context.
     * @throws UndefinedException if the specified variable is not defined in this context.
     */
    public double getVariable(String varName) throws UndefinedException{
        if(!variables.containsKey(varName))
            throw new UndefinedException(varName);
        return variables.get(varName);
    }

    /**
     * Binds the specified variable name to the specified value.
     * If a variable with the same name is already defined, it's value is replaced.
     * @param varName The name of the variable to add/edit.
     * @param value The value to assign to the variable.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     */
    public void setVariable(String varName, double value) throws InvalidSymbolNameException{
        VariableExpression.assertValidSymbolName(varName);
        variables.put(varName, value);
    }

    /**
     * Evaluates the specified {@link Expression} in this context, logging the steps done to the provided {@link Writer}, and binds its value to the specified variable name.
     * If a variable with the same name is already defined, it's value is replaced.
     * @param varName The name of the variable to assign the value of the expression to.
     * @param value The expression whose value will be assigned to the variable.
     * @param logWriter A {@link Writer} where the evaluation steps of the expression will be logged to.
     * @throws UndefinedException if <code>value</code> can't be evaluated because it contains a symbol that isn't defined in this context.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     */
    public void setVariable(String varName, Expression value, Writer logWriter) throws UndefinedException, InvalidSymbolNameException{
        setVariable(varName, value.eval(this, logWriter));
    }

    /**
     * Evaluates the specified {@link Expression} in this context, without logging the steps done, and binds its value to the specified variable name.
     * @param varName The name of the variable to assign the value of the expression to.
     * @param value The expression whose value will be assigned to the variable.
     * @throws UndefinedException if <code>value</code> can't be evaluated because it contains a symbol that isn't defined in this context.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     */
    public void setVariable(String varName, Expression value) throws UndefinedException, InvalidSymbolNameException{
        setVariable(varName, value.eval(this));
    }

    /**
     * Deletes/un-defines the specified variable from this context, if existing.
     * @param varName The name of the variable to delete.
     */
    public void delVariable(String varName){
        variables.remove(varName);
    }

    /**
     * Clears this context wiping all the defined variables.
     */
    public void clear(){
        variables.clear();
    }

    /**
     * Returns a string representation of this context.
     * @return A string consisting of comma-separated key-value pairs of the defined variables.
     */
    public String toString(){
        String result = "";
        for(Map.Entry<String, Double> var: variables.entrySet())
            result += var.getKey() + "=" + var.getValue() + ", ";
        return result.length() == 0 ? result : result.substring(0, result.length() - 2);
    }

}
