package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;

/**
 * Represents a user-defined function, defined as an expression.
 */
public class CustomFunction extends Function {

    private String[] argNames;
    private Expression expr;

    /**
     * Initializes a new instance of CustomFunction.
     * @param name The name of this function.
     * @param expr The expression that defines this function; can contain variables with the names specified in argNames,
     * that will be replaced by the arguments values when this is evaluated.
     * @param argNames The names of the arguments of this function.
     */
    public CustomFunction(String name, Expression expr, String ... argNames) {
        super(name);
        this.argNames = argNames;
        this.expr = expr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getArgCount() {
        return argNames.length;
    }

    /**
     * TODO
     * @param args The values to pass to the function.
     * @param context The context this function must be evaluated into.
     * @param logWriter The {@link java.io.Writer} to write evaluation steps onto.
     * @return The result of the evaluation.
     * @throws UndefinedException TODO
     */
    @Override
    protected double evalFunction(double[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
        return 0; //TODO
    }

}
