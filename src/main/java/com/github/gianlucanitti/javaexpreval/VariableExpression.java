package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * An expression representing a variable.
 * It can be bound to a constant; if at evaluation time value a variable is not bound to a constant determined by reading from the expression context.
 */
public class VariableExpression extends NamedSymbolExpression{

    private ConstExpression binding;

    /**
     * Initializes a new VariableExpression with the specified variable name.
     * @param varName The variable's name.
     * @throws InvalidSymbolNameException if <code>varName</code> isn't a valid symbol name.
     */
    public VariableExpression(String varName) throws InvalidSymbolNameException{
        super(varName);
    }

    /**
     * Binds this VariableExpression to the specified constant value. The binding can be undone by calling <code>bind(null)</code>.
     * @param value The {@link ConstExpression} this {@link VariableExpression} must be bound to, or null to delete the binding.
     */
    public void bind(ConstExpression value){
        binding = value;
    }

    /**
     * @return An empty array of {@link Expression}s.
     */
    @Override
    public Expression[] getSubExpressions(){
        return new Expression[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigDecimal evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
        return binding == null ? context.getVariable(getName()) : binding.eval();
    }

    /**
     * Returns a string representation of this variable expression.
     * @return The string passed to {@link #VariableExpression(String)} if this isn't bound to a constant, a string representation of the constant otherwise.
     */
    @Override
    public String toString() {
        return binding == null ? getName() : binding.toString();
    }

}
