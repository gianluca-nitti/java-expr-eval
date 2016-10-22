package com.github.gianlucanitti.javaexpreval;

import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The context an expression can be evaluated in. Can contain binding between variables and their values.
 */
public class ExpressionContext {

    private HashMap<String, Double> variables;
    private HashSet<Function> functions;

    /**
     * Initializes an empty context.
     */
    public ExpressionContext(){
        variables = new HashMap<String, Double>();
        functions = new HashSet<Function>();
        functions.addAll(BuiltInFunctions.getList());
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
     * Adds the specified {@link Function} to this context.
     * @param f The function to add.
     */
    public void setFunction(Function f) {
        if (!functions.add(f)) { //if a function with the same signature is already present, add will return false...
            //... so the old function is removed (the new one can be used as key because they have the same name and number of arguments, so they are equal according to Function.equals(Object))
            functions.remove(f);
            functions.add(f); //then the new one is added, replacing the previous
        }
    }

    /**
     * Initializes a new {@link CustomFunction} and adds it to this context.
     * Arguments are the same of the {@link CustomFunction#CustomFunction(String, Expression, String...)} constructor.
     * @param name The name for the new function.
     * @param expr The expression that defines this function; can contain variables with the names specified in argNames,
     * that will be replaced by the arguments values when this is evaluated.
     * @param argNames The names of the arguments of this function.
     * @throws InvalidSymbolNameException if <code>name</code> or one of the items in <code>argNames</code> aren't valid symbol names (see {@link NamedSymbolExpression}).
     */
    public void setFunction(String name, Expression expr, String ... argNames) throws InvalidSymbolNameException {
        setFunction(new CustomFunction(name, expr, argNames));
    }

    /**
     * Returns, if existing in this context, the function with the specified name and number of arguments.
     * @param name The name of the function.
     * @param argCount The number of arguments that the function must accept.
     * @return The requested {@link Function}, if defined in this context.
     * @throws UndefinedException if the requested function is not defined in this context.
     */
    public Function getFunction(String name, int argCount) throws UndefinedException{
        for(Function f: functions)
            if(f.getArgCount() == argCount && f.getName().equals(name))
                return f;
        throw new UndefinedException(name, argCount);
    }

    /**
     * Deletes the specified function definition from this context, if existing.
     * @param name The name of the function to remove.
     * @param argCount The number of arguments of the function to remove.
     */
    public void delFunction(String name, int argCount){
        Function toRemove = null;
        for(Function f: functions)
            if(f.getArgCount() == argCount && f.getName().equals(name))
                toRemove = f;
        if(toRemove != null)
            functions.remove(toRemove);
    }

    /**
     * Clears this context wiping all the defined variables and functions.
     */
    public void clear(){
        variables.clear();
        functions.clear();
    }

    /**
     * Returns a string representation of this context.
     * @return A string consisting of comma-separated key-value pairs of the defined variables.
     */
    public String toString(){
        String result = "";
        for(Map.Entry<String, Double> var: variables.entrySet())
            result += var.getKey() + "=" + var.getValue() + ", ";
        for(Function f: functions)
            result += f.toString() + ", ";
        return result.length() == 0 ? result : result.substring(0, result.length() - 2);
    }

}
