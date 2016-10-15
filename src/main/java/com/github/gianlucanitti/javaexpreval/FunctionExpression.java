package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;

/**
 * An expression representing a function (built-in, like sqrt, log,... or user-defined in the context).
 */
public class FunctionExpression extends NamedSymbolExpression{

    private Expression[] args;

    public FunctionExpression(String funcName, Expression ... args) throws InvalidSymbolNameException{
        super(funcName);
        this.args = args;
    }

    protected double evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException {
        return context.getFunction(getName(), args.length).eval(args, context, logWriter);
    }

    public String toString() {
        String result = getName() + "(";
        for(Expression x: args)
            result += x.toString() + ",";
        result += ")";
        return result.replace(",)", ")");
    }

}
