package com.github.gianlucanitti.javaexpreval;

import java.io.Writer;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

/**
 * The context an expression can be evaluated in. Can contain binding between variables and their values and function definitions.
 * This object is {@link Observable}; it notifies its {@link Observer}s every time it's internal state changes (i.e. a variable or function is added, changed or deleted).
 */
public class ExpressionContext extends Observable {

    /**
     * Represents the value of a variable defined in an ExpressionContext.
     */
    public static class VariableValue{
        private BigDecimal value;
        private boolean readOnly;

        /**
         * Initializes a new instance of VariableValue.
         * @param value The value of the variable.
         * @param readOnly Whether the variable must be read-only or not.
         */
        public VariableValue(BigDecimal value, boolean readOnly) {
            this.value = value;
            this.readOnly = readOnly;
        }

        /**
         * Initializes a new instance of VariableValue.
         * @param value The value of the variable; will be converted to a {@link BigDecimal}.
         * @param readOnly Whether the variable must be read-only or not.
         */
        public VariableValue(double value, boolean readOnly) {
            this(new BigDecimal(value), readOnly);
        }

        /**
         * @return The value of this variable, as specified as 1st argument to the constructor.
         */
        public BigDecimal getValue() {
            return value;
        }

        /**
         * @return Whether this variable is read-only, as specified as 2nd argument to the constructor.
         */
        public boolean isReadOnly() {
            return readOnly;
        }

        /**
         * @return A string representation of this VariableValue, which is it's numeric value prepended by the "readonly " prefix if it was initialized as read-only.
         */
        @Override
        public String toString(){
            return (readOnly ? "readonly " : "") + value;
        }
    }

    private HashMap<String, VariableValue> variables;
    private HashSet<Function> functions;
    private MathContext mc;

    /**
     * Initializes an empty context with the specified rounding settings.
     * @param m {@link MathContext} object describing the precision and rounding method to use for calculations made while evaluating expressions in this context.
     */
    public ExpressionContext(MathContext m){
        mc = m;
        variables = new HashMap<String, VariableValue>();
        functions = new HashSet<Function>();
        functions.addAll(BuiltInFunctions.getList());
    }

    /**
     * Initializes an empty context with the default rounding settings, which are those of the {@link MathContext#DECIMAL64} field (the {@link BigDecimal}s will thus behave like <code>double</code>s).
     */
    public ExpressionContext(){
        this(MathContext.DECIMAL64);
    }

    /**
     * @return The {@link MathContext} object associated to this object, which stores the settings for the calculations done when evaluating expressions in this {@link ExpressionContext}.
     */
    public MathContext getMathContext(){
        return mc;
    }

    /**
     * Changes the {@link MathContext} object associated to this object, which the specified one.
     * @param m The new {@link MathContext} object which will be used when doing calculations to evaluate expressions in this context.
     */
    public void setMathContext(MathContext m){
        mc = m;
    }

    /**
     * Marks this object as changed and notifies the observers
     * (simply calls {@link #setChanged()} and {@link #notifyObservers()} from {@link Observable}.
     */
    private void updateObservers(){
        setChanged();
        notifyObservers();
    }

    /**
     * @return A unmodifiable {@link Map} containing the names and values of the variables defined in this context.
     */
    public Map<String, VariableValue> getVariables(){
        return Collections.unmodifiableMap(variables);
    }

    /**
     * @return A unmodifiable {@link Set} containing the functions defined in this context.
     */
    public Set<Function> getFunctions(){
        return Collections.unmodifiableSet(functions);
    }

    /**
     * Returns, if existing, the value of the specified variable.
     * @param varName The name of the variable.
     * @return The value of the specified variable, if defined in this context.
     * @throws UndefinedException if the specified variable is not defined in this context.
     */
    public BigDecimal getVariable(String varName) throws UndefinedException{
        if(!variables.containsKey(varName))
            throw new UndefinedException(varName);
        return variables.get(varName).value;
    }

    /**
     * Binds the specified variable name to the specified value, flagging the variable as read-only if specified.
     * If a variable with the same name is already defined, it's value is replaced.
     * @param varName The name of the variable to add/edit.
     * @param value The value to assign to the variable.
     * @param readOnly Whether this variable must be read-only or it can be redefined later.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     * @throws ReadonlyException if the variable can't be set because it was previously defined as read-only.
     */
    public void setVariable(String varName, boolean readOnly, BigDecimal value) throws InvalidSymbolNameException, ReadonlyException{
        VariableExpression.assertValidSymbolName(varName);
        if(variables.containsKey(varName) && variables.get(varName).readOnly)
            throw new ReadonlyException(varName);
        else
            variables.put(varName, new VariableValue(value, readOnly));
        updateObservers();
    }

    /**
     * Binds the specified variable name to the specified value without flagging it as read-only (it can be redefined later).
     * Wrapper for {@link #setVariable(String, boolean, BigDecimal)} with <code>false</code> as 2nd argument.
     * @param varName The name of the variable to add/edit.
     * @param value The value to assign to the variable.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     * @throws ReadonlyException if the variable can't be set because it was previously defined as read-only.
     */
    public void setVariable(String varName, BigDecimal value) throws InvalidSymbolNameException, ReadonlyException{
        setVariable(varName, false, value);
    }

    /**
     * Binds the specified variable name to the specified value, flagging the variable as read-only if specified.
     * If a variable with the same name is already defined, it's value is replaced.
     * @param varName The name of the variable to add/edit.
     * @param value The value to assign to the variable; will be converted to a {@link BigDecimal}.
     * @param readOnly Whether this variable must be read-only or it can be redefined later.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     * @throws ReadonlyException if the variable can't be set because it was previously defined as read-only.
     */
    public void setVariable(String varName, boolean readOnly, double value) throws InvalidSymbolNameException, ReadonlyException{
        setVariable(varName, readOnly, new BigDecimal(value));
    }

    /**
     * Binds the specified variable name to the specified value without flagging it as read-only (it can be redefined later).
     * Wrapper for {@link #setVariable(String, boolean, BigDecimal)} with <code>false</code> as 2nd argument.
     * @param varName The name of the variable to add/edit.
     * @param value The value to assign to the variable; will be converted to a {@link BigDecimal}.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     * @throws ReadonlyException if the variable can't be set because it was previously defined as read-only.
     */
    public void setVariable(String varName, double value) throws InvalidSymbolNameException, ReadonlyException{
        setVariable(varName, false, value);
    }

    /**
     * Evaluates the specified {@link Expression} in this context, logging the steps done to the provided {@link Writer}, and binds its value to the specified variable name.
     * If a variable with the same name is already defined and it's not read-only, it's value is replaced.
     * @param varName The name of the variable to assign the value of the expression to.
     * @param readOnly Whether this variable must be read-only or it can be redefined later.
     * @param value The expression whose value will be assigned to the variable.
     * @param logWriter A {@link Writer} where the evaluation steps of the expression will be logged to.
     * @throws UndefinedException if <code>value</code> can't be evaluated because it contains a symbol that isn't defined in this context.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     * @throws ReadonlyException if the variable can't be set because it was previously defined as read-only.
     */
    public void setVariable(String varName, boolean readOnly, Expression value, Writer logWriter) throws UndefinedException, InvalidSymbolNameException, ReadonlyException{
        setVariable(varName, readOnly, value.eval(this, logWriter));
    }

    /**
     * Evaluates the specified {@link Expression} in this context, without logging the steps done, and binds its value to the specified variable name.
     * @param varName The name of the variable to assign the value of the expression to.
     * @param readOnly Whether this variable must be read-only or it can be redefined later.
     * @param value The expression whose value will be assigned to the variable.
     * @throws UndefinedException if <code>value</code> can't be evaluated because it contains a symbol that isn't defined in this context.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     * @throws ReadonlyException if the variable can't be set because it was previously defined as read-only.
     */
    public void setVariable(String varName, boolean readOnly, Expression value) throws UndefinedException, InvalidSymbolNameException, ReadonlyException{
        setVariable(varName, readOnly, value.eval(this));
    }

    /**
     * Deletes/un-defines the specified variable from this context, if existing.
     * @param varName The name of the variable to delete.
     * @throws ReadonlyException if the variable can't be deleted because it was defined as read-only.
     */
    public void delVariable(String varName) throws ReadonlyException{
        if(variables.get(varName) != null && variables.get(varName).readOnly)
            throw new ReadonlyException(varName);
        variables.remove(varName);
        updateObservers();
    }

    /**
     * Adds the specified {@link Function} to this context.
     * @param f The function to add.
     * @throws ReadonlyException if the function can't be set because it was previously defined as read-only.
     */
    public void setFunction(Function f) throws ReadonlyException{
        boolean isReadonly;
        try{
            isReadonly = getFunction(f.getName(), f.getArgCount()).isReadOnly(); //check if a function with the same signature is already defined as readonly
        }catch(UndefinedException ex){
            isReadonly = false;
        }
        if(isReadonly)
            throw new ReadonlyException(f.getName(), f.getArgCount());
        else //if it's not read-only...
                functions.remove(f); //...the old function is removed (the new one can be used as key because they have the same name and number of arguments, so they are equal according to Function.equals(Object))
        functions.add(f); //then the new one is added, replacing the previous
        updateObservers();
    }

    /**
     * Initializes a new {@link CustomFunction} and adds it to this context.
     * Arguments are the same of the {@link CustomFunction#CustomFunction(String, Expression, boolean, String...)} constructor.
     * @param name The name for the new function.
     * @param expr The expression that defines this function; can contain variables with the names specified in argNames,
     * that will be replaced by the arguments values when this is evaluated.
     * @param readOnly Whether this function is read-only or it can be redefined.
     * @param argNames The names of the arguments of this function.
     * @throws InvalidSymbolNameException if <code>name</code> or one of the items in <code>argNames</code> aren't valid symbol names (see {@link NamedSymbolExpression}).
     * @throws ReadonlyException if the function can't be set because it was previously defined as read-only.
     */
    public void setFunction(String name, Expression expr, boolean readOnly, String ... argNames) throws InvalidSymbolNameException, ReadonlyException {
        setFunction(new CustomFunction(name, expr, readOnly, argNames));
    }

    /**
     * Initializes a new {@link CustomFunction} and adds it to this context.
     * Wrapper for {@link #setFunction(String, Expression, boolean, String...)} with <code>false</code> as 3rd argument.
     * @param name The name for the new function.
     * @param expr The expression that defines this function; can contain variables with the names specified in argNames,
     * that will be replaced by the arguments values when this is evaluated.
     * @param argNames The names of the arguments of this function.
     * @throws InvalidSymbolNameException if <code>name</code> or one of the items in <code>argNames</code> aren't valid symbol names (see {@link NamedSymbolExpression}).
     * @throws ReadonlyException if the function can't be set because it was previously defined as read-only.
     */
    public void setFunction(String name, Expression expr, String ... argNames) throws InvalidSymbolNameException, ReadonlyException {
        setFunction(name, expr, false, argNames);
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
     * @throws  ReadonlyException if the function can't be deleted because it was defined as read-only.
     */
    public void delFunction(String name, int argCount) throws ReadonlyException {
        Function toRemove = null;
        for(Function f: functions)
            if(f.getArgCount() == argCount && f.getName().equals(name))
                toRemove = f;
        if(toRemove != null)
            if(toRemove.isReadOnly())
                throw new ReadonlyException(name, argCount);
            else
                functions.remove(toRemove);
        updateObservers();
    }

    /**
     * Clears this context wiping the non-readonly defined variables and functions.
     */
    public void clear(){
        Iterator<Map.Entry<String, VariableValue>> varIterator = variables.entrySet().iterator();
        while(varIterator.hasNext())
            if(!varIterator.next().getValue().readOnly)
                varIterator.remove();
        Iterator<Function> funcIterator = functions.iterator();
        while(funcIterator.hasNext())
            if(!funcIterator.next().isReadOnly())
                funcIterator.remove();
        updateObservers();
    }

    /**
     * Returns a string representation of this context.
     * @return A string consisting of comma-separated key-value pairs of the defined variables.
     */
    public String toString(){
        String result = "";
        String newLine = System.getProperty("line.separator");
        for(Map.Entry<String, VariableValue> var: variables.entrySet())
            result += var.getKey() + "=" + var.getValue() + newLine;
        for(Function f: functions)
            result += f.toString() + newLine;
        return result.length() == 0 ? result : result.substring(0, result.length() - newLine.length());
    }

}
