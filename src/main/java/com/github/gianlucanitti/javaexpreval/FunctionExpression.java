package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;

/**
 * An expression representing a function (built-in, like sqrt, log,... or user-defined in the context).
 */
public class FunctionExpression extends NamedSymbolExpression{

    public enum BuiltInFunctions{
        SIN, COS, TAN, LOG, SQRT, ABS
    }

    private Expression[] args;

    public FunctionExpression(String funcName, Expression ... args) throws InvalidSymbolNameException{
        super(funcName);
        this.args = args;
    }

    protected double evalExpr(ExpressionContext context, PrintWriter logWriter) throws UndefinedException {
        //TODO: check argument count
        switch(BuiltInFunctions.valueOf(getName().toUpperCase())){
            case SIN:
                return Math.sin(args[0].eval(context, logWriter));
            case COS:
                return Math.cos(args[0].eval(context, logWriter));
            case TAN:
                return Math.tan(args[0].eval(context, logWriter));
            case LOG:
                return Math.log(args[0].eval(context, logWriter));
            case SQRT:
                return Math.sqrt(args[0].eval(context, logWriter));
            case ABS:
                return Math.abs(args[0].eval(context, logWriter));
            default:
                return 0; //TODO: check if it's a custom function defined in context
        }
    }

    public String toString() {
        String result = getName() + "(";
        for(Expression x: args)
            result += x.toString() + ",";
        result += ")";
        return result.replace(",)", ")");
    }

}
