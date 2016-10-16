package com.github.gianlucanitti.javaexpreval;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Container for the built-in functions.
 */
public class BuiltInFunctions {

    /**
     * Represents a generic built-in function.
     */
    public static abstract class BuiltInFunction extends Function{
        private int argCount;

        /**
         * Initializes a new BuiltInFunction with the specified name and number of arguments.
         * @param name The name of this function.
         * @param argCount The number of arguments this function is defined for.
         */
        public BuiltInFunction(String name, int argCount) {
            super(name);
            this.argCount = argCount;
        }

        /**
         * @return The number of arguments expected by this function (the second parameter passed to the {@link #BuiltInFunction(String, int)} constructor).
         */
        @Override
        public int getArgCount() {
            return argCount;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected double evalFunction(double[] args, ExpressionContext context, PrintWriter logWriter) throws UndefinedException{
            return eval(args);
        }

        /**
         * Evaluates this function on the specified arguments using methods from the {@link Math} class.
         * @param args The values to pass to the function.
         * @return The result of the evaluation.
         */
        protected abstract double eval(double[] args);
    }

    public static class SinFunction extends BuiltInFunction{
        public SinFunction() {
            super("sin", 1);
        }

        @Override
        public double eval(double[] args) {
            return Math.sin(args[0]);
        }
    }

    public static class CosFunction extends BuiltInFunction{
        public CosFunction() {
            super("cos", 1);
        }

        @Override
        public double eval(double[] args) {
            return Math.cos(args[0]);
        }
    }

    public static class TanFunction extends BuiltInFunction{
        public TanFunction() {
            super("tan", 1);
        }

        @Override
        public double eval(double[] args) {
            return Math.tan(args[0]);
        }
    }

    public static class LogFunction extends BuiltInFunction{
        public LogFunction() {
            super("log", 1);
        }

        @Override
        public double eval(double[] args) {
            return Math.log(args[0]);
        }
    }

    public static class SqrtFunction extends BuiltInFunction{
        public SqrtFunction() {
            super("sqrt", 1);
        }

        @Override
        public double eval(double[] args) {
            return Math.sqrt(args[0]);
        }
    }

    public static class AbsFunction extends BuiltInFunction{
        public AbsFunction() {
            super("abs", 1);
        }

        @Override
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
