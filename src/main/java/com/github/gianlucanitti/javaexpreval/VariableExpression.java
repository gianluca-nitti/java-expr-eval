package com.github.gianlucanitti.javaexpreval;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/**
 An expression representing a variable. It's value is determined by reading from the expression context.
 */
public class VariableExpression extends Expression{

    private String varName;

    /**
     * Initializes a new VariableExpression with the specified variable name.
     * @param var The variable's name.
     */
    public VariableExpression(String var){
        varName = var;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
        return context.getVariable(varName);
    }

    /**
     * Returns a string representation of this variable expression.
     * @return The string passed to {@link #VariableExpression(String)}.
     */
    @Override
    public String toString() {
        return varName;
    }

}
