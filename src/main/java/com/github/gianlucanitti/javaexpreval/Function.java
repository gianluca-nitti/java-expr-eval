package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * Defines a generic function, with a name a number of arguments and a read-only flag.
 */
public abstract class Function {

    private String name;
    private boolean readOnly;

    /**
     * Initializes an instance of the Function class representing a function with the specified name.
     * @param name The name of the function; in an expression, a function is called with the syntax name(arg1, arg2,...argN).
     * @param readOnly Whether this function is read-only or it can be redefined.
     * @throws InvalidSymbolNameException if <code>name</code> isn't a valid symbol name (see {@link NamedSymbolExpression}).
     */
    public Function(String name, boolean readOnly) throws InvalidSymbolNameException{
        NamedSymbolExpression.assertValidSymbolName(name);
        this.name = name;
        this.readOnly = readOnly;
    }

    /**
     * @return The name of this function (the 1st argument passed to the {@link #Function(String, boolean)} constructor).
     */
    public String getName(){
        return name;
    }

    /**
     * @return A boolean indicating if this function was defined as read-only (the 2nd argument passed to the {@link #Function(String, boolean)} constructor).
     */
    public boolean isReadOnly(){
        return readOnly;
    }

    /**
     * Evaluates this function for the specified arguments in the specified context.
     * This is a public wrapper for {@link #evalFunction(BigDecimal[], ExpressionContext, PrintWriter)}: ensures argument count is as expected, then calls it.
     * @param args The values to pass to the function.
     * @param context The context this function must be evaluated into
     * (this is necessary, for example, if this is a {@link CustomFunction} defined as an expression with references to variables or other functions).
     * @param logWriter The {@link java.io.Writer} to write evaluation steps onto
     * (only used if this is a {@link CustomFunction}, defined as a function that needs to be evaluated).
     * @return The result of the evaluation.
     * @throws UndefinedException if the number of arguments supplied is different than the number of arguments expected by this function
     * (see {@link #getArgCount()}) or if the evaluation process throws an {@link UndefinedException} itself.
     */
    public final BigDecimal eval(BigDecimal[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
        if(args.length != getArgCount())
            throw new UndefinedException(getName(), args.length);
        return evalFunction(args, context, logWriter);
    }

    /**
     * @return The number of arguments expected by this function.
     */
    public abstract int getArgCount();

    /**
     * Evaluates this function for the specified arguments in the specified context.
     * This must be overridden by subclasses and doesn't ensure that the number of arguments is correct
     * (this is done by the public wrapper {@link #eval(BigDecimal[], ExpressionContext, PrintWriter)}).
     * @param args The values to pass to the function.
     * @param context The context this function must be evaluated into.
     * @param logWriter The {@link java.io.Writer} to write evaluation steps onto.
     * @return The result of the evaluation.
     * @throws UndefinedException If the function can't be evaluated because its definition references undefined variables or functions.
     */
    protected abstract BigDecimal evalFunction(BigDecimal[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException;

    /**
     * Compares this function with another one.
     * @param other The object (should be a {@link Function}) to compare to.
     * @return <code>true</code> if <code>other</code> is a {@link Function} with the same name and number of arguments of this one, <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object other){
        if(!(other instanceof Function))
            return false;
        Function otherFun = (Function)other;
        return name.equals(otherFun.name) && getArgCount() == ((Function) other).getArgCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode(){
        return name.hashCode() ^ getArgCount();
    }

    /**
     * @return A string representation of this {@link Function} that specifies the name, number of arguments and if it's readonly.
     */
    @Override
    public String toString(){
        return (readOnly ? "readonly " : "") + name + "(" + getArgCount() + " arguments)";
    }

}
