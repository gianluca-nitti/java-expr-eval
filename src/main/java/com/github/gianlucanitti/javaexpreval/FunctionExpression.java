package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * An expression representing a function (built-in, like sqrt, log,... or user-defined in the context).
 */
public class FunctionExpression extends NamedSymbolExpression{

    private Expression[] args;

    /**
     * Initializes a new FunctionExpression representing a call to the specified function with the specified arguments.
     * @param funcName The name of the function that is being called.
     * @param args The arguments passed to the function.
     * @throws InvalidSymbolNameException if <code>funcName</code> isn't a valid symbol name (see {@link NamedSymbolExpression} for details).
     */
    public FunctionExpression(String funcName, Expression ... args) throws InvalidSymbolNameException{
        super(funcName);
        this.args = args;
    }

    /**
     * @return An array of the {@link Expression}s passed to the {@link #FunctionExpression(String, Expression...)} constructor.
     */
    @Override
    public Expression[] getSubExpressions(){
        return args;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigDecimal evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException {
        BigDecimal[] evaluatedArgs = new BigDecimal[args.length];
        for(int i = 0; i < args.length; i++)
            evaluatedArgs[i] = args[i].eval(context, logWriter);
        return context.getFunction(getName(), args.length).eval(evaluatedArgs, context, logWriter);
    }

    /**
     * Returns a string representation of this expression
     * @return A string in the form "function(arg1,...,argN)" where <i>function</i> is the name of the function and <i>arg1,...,argN</i> are the arguments.
     */
    @Override
    public String toString() {
        String result = getName() + "(";
        for(Expression x: args)
            result += x.toString() + ",";
        result += ")";
        return result.replace(",)", ")");
    }

}
