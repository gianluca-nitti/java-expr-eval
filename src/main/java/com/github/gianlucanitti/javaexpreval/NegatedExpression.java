package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * Represents an expression that evaluates to the opposite number of the specified sub-expression's value.
 */
public class NegatedExpression extends Expression {

    private Expression subExpression;

    /**
     * Initializes a NegatedExpression representing the opposite value of the specified expression.
     * @param expressionToNegate The expression whose sign must be changed.
     */
    public NegatedExpression(Expression expressionToNegate){
        subExpression = expressionToNegate;
    }

    /**
     * @return The argument passed to the {@link #NegatedExpression(Expression)} constructor.
     */
    @Override
    public Expression[] getSubExpressions(){
        return new Expression[]{subExpression};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigDecimal evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException {
        return subExpression.eval(context, logWriter).negate();
    }

    /**
     * Returns a string representation of this expression.
     * @return The expression specified in the call to the {@link #NegatedExpression(Expression)} constructor enclosed in parenthesis and prepended with the minus sign.
     */
    @Override
    public String toString() {
        return "(-(" + subExpression.toString() + "))";
    }

}
