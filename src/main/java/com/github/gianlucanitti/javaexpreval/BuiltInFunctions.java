package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * TODO
 */
public class BuiltInFunctions {

    public static abstract class BuiltInFunction extends Function{
        private int argCount;

        public BuiltInFunction(String name, int argCount) {
            super(name);
            this.argCount = argCount;
        }

        public int getArgCount() {
            return argCount;
        }

        protected double evalFunction(Expression[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
            double[] evaluatedArgs = new double[args.length];
            for(int i = 0; i < args.length; i++)
                evaluatedArgs[i] = args[i].eval(context, logWriter);
            return eval(evaluatedArgs);
        }

        public abstract double eval(double[] args);
    }

    public static class SinFunction extends BuiltInFunction{
        public SinFunction() {
            super("sin", 1);
        }

        public double eval(double[] args) {
            return Math.sin(args[0]);
        }
    }

    public static class CosFunction extends BuiltInFunction{
        public CosFunction() {
            super("cos", 1);
        }

        public double eval(double[] args) {
            return Math.cos(args[0]);
        }
    }

    public static class TanFunction extends BuiltInFunction{
        public TanFunction() {
            super("tan", 1);
        }

        public double eval(double[] args) {
            return Math.tan(args[0]);
        }
    }

    public static class LogFunction extends BuiltInFunction{
        public LogFunction() {
            super("log", 1);
        }

        public double eval(double[] args) {
            return Math.log(args[0]);
        }
    }

    public static class SqrtFunction extends BuiltInFunction{
        public SqrtFunction() {
            super("sqrt", 1);
        }

        public double eval(double[] args) {
            return Math.sqrt(args[0]);
        }
    }

    public static class AbsFunction extends BuiltInFunction{
        public AbsFunction() {
            super("abs", 1);
        }

        public double eval(double[] args) {
            return Math.abs(args[0]);
        }
    }

    public static ArrayList<BuiltInFunction> getList(){
        ArrayList<BuiltInFunction> result = new ArrayList<BuiltInFunction>();
        result.add(new SinFunction());
        result.add(new CosFunction());
        result.add(new TanFunction());
        result.add(new LogFunction());
        result.add(new SqrtFunction());
        result.add(new AbsFunction());
        return result;
    }

}
