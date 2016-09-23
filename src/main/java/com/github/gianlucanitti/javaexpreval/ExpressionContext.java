package com.github.gianlucanitti.javaexpreval;

import java.util.HashMap;
import java.util.Map;

/**
 * The context an expression can be evaluated in. Can contain binding between variables and their values.
 */
public class ExpressionContext {

    private HashMap<String, Double> variables;

    /**
     * initializes an empty context.
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
     */
    public void setVariable(String varName, double value){
        variables.put(varName, value);
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
        return result.substring(0, result.length() - 2);
    }

}
