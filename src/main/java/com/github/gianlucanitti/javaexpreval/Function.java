package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;

/**
 * TODO
 */
public abstract class Function {

    private String name;

    public Function(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public final double eval(Expression[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
        if(args.length != getArgCount())
            throw new UndefinedException(getName(), args.length);
        return evalFunction(args, context, logWriter);
    }

    public abstract int getArgCount();

    protected abstract double evalFunction(Expression[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException;

}
