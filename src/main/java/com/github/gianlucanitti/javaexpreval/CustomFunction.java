package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;
import java.util.ArrayList;

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
     * @param readOnly Whether this function is read-only or it can be redefined.
     * @param argNames The names of the arguments of this function.
     * @throws InvalidSymbolNameException if <code>name</code> or one of the items in <code>argNames</code> aren't valid symbol names (see {@link NamedSymbolExpression}).
     */
    public CustomFunction(String name, Expression expr, boolean readOnly, String ... argNames) throws InvalidSymbolNameException {
        super(name, readOnly);
        ArrayList<String> argNameList = new ArrayList<String>(argNames.length);
        for(String s: argNames)
            if(s != null && s.length() > 0) {
                NamedSymbolExpression.assertValidSymbolName(s);
                argNameList.add(s);
            }
        this.argNames = argNameList.toArray(new String[0]);
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
     * Evaluates this function for the specified arguments in the specified context.
     * It takes the {@link Expression} passed as 2nd parameter to the {@link #CustomFunction(String, Expression, boolean,  String...)} constructor
     * and binds the variables that have names that match the argument names passed to the constructor to the respective values specified
     * in the <code>args</code> array, then evaluates it.
     * @param args The values to pass to the function.
     * @param context The context this function must be evaluated into.
     * @param logWriter The {@link java.io.Writer} to write evaluation steps onto.
     * @return The result of the evaluation.
     * @throws UndefinedException if the {@link Expression} passed to the {@link #CustomFunction(String, Expression, boolean, String...)}
     * constructor can't be evaluated because it contains symbol not defined in the specified <code>context</code>.
     * @throws IllegalArgumentException if the specified <code>args</code> and the number of argument names passed to the constructor have different lengths.
     */
    @Override
    protected double evalFunction(double[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
        expr.bindVariables(argNames, args);
        return expr.eval(context, logWriter);
    }

}
