package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;

/**
 * TODO
 */
public class CustomFunction extends Function {

    private String[] argNames;
    private Expression expr;

    public CustomFunction(String name, Expression expr, String ... argNames) {
        super(name);
        this.argNames = argNames;
        this.expr = expr;
    }

    public int getArgCount() {
        return argNames.length;
    }

    protected double evalFunction(Expression[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
        return 0; //TODO
    }

}
