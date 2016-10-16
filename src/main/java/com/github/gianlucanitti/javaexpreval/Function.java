package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;

/**
 * Defines a generic function, with a name and a number of arguments.
 */
public abstract class Function {

    private String name;

    /**
     * Initializes an instance of the Function class representing a function with the specified name.
     * @param name The name of the function; in an expression, a function is called with the syntax name(arg1, arg2,...argN).
     */
    public Function(String name) {
        this.name = name;
    }

    /**
     * @return The name of this function (the string passed to the {@link #Function(String)} constructor).
     */
    public String getName(){
        return name;
    }

    /**
     * Evaluates this function for the specified arguments in the specified context.
     * This is a public wrapper for {@link #evalFunction(double[], ExpressionContext, PrintWriter)}: ensures argument count is as expected, then calls it.
     * @param args The values to pass to the function.
     * @param context The context this function must be evaluated into
     * (this is necessary, for example, if this is a {@link CustomFunction} defined as an expression with references to variables or other functions).
     * @param logWriter The {@link java.io.Writer} to write evaluation steps onto
     * (only used if this is a {@link CustomFunction}, defined as a function that needs to be evaluated).
     * @return The result of the evaluation.
     * @throws UndefinedException if the number of arguments supplied is different than the number of arguments expected by this function
     * (see {@link #getArgCount()}) or if the evaluation process throws an {@link UndefinedException} itself.
     */
    public final double eval(double[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
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
     * (this is done by the public wrapper {@link #eval(double[], ExpressionContext, PrintWriter)}).
     * @param args The values to pass to the function.
     * @param context The context this function must be evaluated into.
     * @param logWriter The {@link java.io.Writer} to write evaluation steps onto.
     * @return The result of the evaluation.
     * @throws UndefinedException If the function can't be evaluated because its definition references undefined variables or functions.
     */
    protected abstract double evalFunction(double[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException;

}
