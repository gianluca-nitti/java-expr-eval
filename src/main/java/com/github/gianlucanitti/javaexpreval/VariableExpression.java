package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;

/**
 * An expression representing a variable. It's value is determined by reading from the expression context.
 */
public class VariableExpression extends NamedSymbolExpression{

    /**
     * Initializes a new VariableExpression with the specified variable name.
     * @param varName The variable's name.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     */
    public VariableExpression(String varName) throws InvalidSymbolNameException{
        super(varName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
        return context.getVariable(getName());
    }

    /**
     * Returns a string representation of this variable expression.
     * @return The string passed to {@link #VariableExpression(String)}.
     */
    @Override
    public String toString() {
        return getName();
    }

}
