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
         * @throws InvalidSymbolNameException if <code>name</code> isn't a valid symbol name (see {@link NamedSymbolExpression}).
         */
        public BuiltInFunction(String name, int argCount) throws InvalidSymbolNameException {
            super(name);
            this.argCount = argCount;
        }

        /**
         * @return The number of arguments expected by this function (the second parameter passed to the constructor).
         */
        @Override
        public int getArgCount() {
            return argCount;
        }

        /**
         * Evaluates this function for the specified arguments.
         * @param args The values to pass to the function.
         * @param context The context this function must be evaluated into (not used here).
         * @param logWriter The {@link java.io.Writer} to write evaluation steps onto (not used here).
         * @return The value returned from {@link #eval(double[])} with the same <code>args</code>.
         */
        @Override
        protected double evalFunction(double[] args, ExpressionContext context, PrintWriter logWriter){
            return eval(args);
        }

        /**
         * Evaluates this function on the specified arguments using methods from the {@link Math} class.
         * @param args The values to pass to the function.
         * @return The result of the evaluation.
         */
        protected abstract double eval(double[] args);
    }

    private static class SinFunction extends BuiltInFunction{
        private SinFunction() throws InvalidSymbolNameException {
            super("sin", 1);
        }

        @Override
        protected double eval(double[] args) {
            return Math.sin(args[0]);
        }
    }

    private static class CosFunction extends BuiltInFunction{
        private CosFunction() throws InvalidSymbolNameException {
            super("cos", 1);
        }

        @Override
        protected double eval(double[] args) {
            return Math.cos(args[0]);
        }
    }

    private static class TanFunction extends BuiltInFunction{
        private TanFunction() throws InvalidSymbolNameException {
            super("tan", 1);
        }

        @Override
        protected double eval(double[] args) {
            return Math.tan(args[0]);
        }
    }

    private static class LogFunction extends BuiltInFunction{
        private LogFunction() throws InvalidSymbolNameException {
            super("log", 1);
        }

        @Override
        protected double eval(double[] args) {
            return Math.log(args[0]);
        }
    }

    private static class SqrtFunction extends BuiltInFunction{
        private SqrtFunction() throws InvalidSymbolNameException {
            super("sqrt", 1);
        }

        @Override
        protected double eval(double[] args) {
            return Math.sqrt(args[0]);
        }
    }

    private static class AbsFunction extends BuiltInFunction{
        private AbsFunction() throws InvalidSymbolNameException {
            super("abs", 1);
        }

        @Override
        protected double eval(double[] args) {
            return Math.abs(args[0]);
        }
    }

    /**
     * @return An {@link ArrayList} of the built-in functions.
     */
    public static ArrayList<BuiltInFunction> getList(){
        ArrayList<BuiltInFunction> result = new ArrayList<BuiltInFunction>();
        try {
            result.add(new SinFunction());
            result.add(new CosFunction());
            result.add(new TanFunction());
            result.add(new LogFunction());
            result.add(new SqrtFunction());
            result.add(new AbsFunction());
        }catch(InvalidSymbolNameException ex){}
        return result;
    }

}
