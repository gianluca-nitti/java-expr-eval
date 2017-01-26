package com.github.gianlucanitti.javaexpreval;

/**
 * Class with static methods used to localize string messages used by the library (like exceptions, logging messages, etc.).
 */
public class LocalizationHelper {

    private static final String nl = System.getProperty("line.separator");

    /**
     * Enumeration of UI messages used by the library.
     */
    public enum Message{
        /**
         * Default value: "%s can be rewritten as %s"
         */
        REWRITE_STEP("%s can be rewritten as %s"),
        /**
         * Default value: "%s evaluates to %s"
         */
        EVAL_STEP("%s evaluates to %s"),
        /**
         * Default value: "Warning: failed to store result. Reason: %s"
         */
        FAILED_STORE_RESULT("Warning: failed to store result. Reason: %s"),
        /**
         * Default value: "All non-readonly variables and functions have been deleted."
         */
        CONTEXT_CLEARED("All non-readonly variables and functions have been deleted."),
        /**
         * Default value: the help message you get by sending "help" to an {@link InteractiveExpressionContext}, or by typing "help" in the CLI tool.
         */
        INTERACTIVE_HELP("Accepted statements are expressions, assignments and commands."
                + nl + "An expression can be formed by integer or decimal numbers, the +,-,*,/,^ binary operators, variables, functions and parenthesis."
                + nl + "A variable is a string of one or more letters and/or underscores. Variables can't be named as commands, which are reserved words."
                + nl + "When an expression is successfully evaluated, it's result is displayed and automatically assigned to the \"ans\" variable, so it can be accessed from the next statement."
                + nl + "A variable assignment is formed by a variable name followed by the = symbol and an expression, which is evaluated and bound to that variable."
                + nl + "An empty assignment (in the form \"someVariable=\") deletes the variable."
                + nl + "A function assignment is formed by a function name and its parameters, followed by the = symbol and an expression, which is bound to that function, e.g.\"sum(x,y)=x+y\"."
                + nl + "A function can be deleted with an empty assignment; the number of arguments must be specified, e.g. \"sum(2)=\" to delete the function \"sum\" defined on two arguments."
                + nl + "An assignment (of variable or function) can be prepended with the \"readonly\" word to prevent it to be modified or deleted, e.g. \"readonly x=1\", \"readonly square(a)=a^2\"."
                + nl + "The commands are: context (prints all the defined variables and functions), clear (deletes all the non-readonly variables and functions), help (shows this message) and exit (stops reading input)."),
        /**
         * Default value: "Incorrect syntax. To delete a function, the number of arguments must be specified (e.g. \"fun(2)=\" to delete \"fun(x, y)\")."
         */
        INCORRECT_DELETE("Incorrect syntax. To delete a function, the number of arguments must be specified (e.g. \"fun(2)=\" to delete \"fun(x, y)\")."),
        /**
         * Default value: "%s has been deleted."
         */
        VAR_DELETED("%s has been deleted."),
        /**
         * Default value: "%s is a reserved word and can't be used as symbol name name."
         */
        RESERVED_WORD("%s is a reserved word and can't be used as symbol name."),
        /**
         * Default value: "%s is now %s"
         */
        VAR_ASSIGNED("%s is now %s"),
        /**
         * Default value: "%s is now defined as %s"
         */
        FUNC_ASSIGNED("%s is now defined as %s"),
        /**
         * Default value: "Only one = operator per command is allowed."
         */
        ONLY_ONE_EQUALITY("Only one = operator per command is allowed."),
        /**
         * Default value: "Expression error: %s"
         */
        ERROR_PREFIX("Expression error: %s"),
        /**
         * Default value: "An empty expression was found."
         */
        EMPTY_EXPR("An empty expression was found."),
        /**
         * Default value: "Unknown operator '%s'."
         */
        INVALID_OPERATOR("Unknown operator '%s'."),
        /**
         * Default value: "\"%s\" isn't a valid symbol name because it contains the '%s' character."
         */
        INVALID_SYM_NAME("\"%s\" isn't a valid symbol name because it contains the '%s' character."),
        /**
         *  Default value: "The empty string isn't a valid symbol identifier."
         */
        EMPTY_SYM_NAME("The empty string isn't a valid symbol identifier."),
        /**
         * Default value: "The numbers of opened and closed parenthesis don't match."
         */
        PARENTHESIS_MISMATCH("The numbers of opened and closed parenthesis don't match."),
        /**
         * Default value: "The \"%s\" variable is defined as read-only."
         */
        READONLY_VAR("The \"%s\" variable is defined as read-only."),
        /**
         * Default value: "The \"%s\" function is defined as read-only for %s arguments."
         */
        READONLY_FUNC("The \"%s\" function is defined as read-only for %s arguments."),
        /**
         * Default value: "The variable \"%s\" is not defined."
         */
        UNDEFINED_VAR("The variable \"%s\" is not defined."),
        /**
         * Default value: "The function \"%s\" is not defined for %s arguments."
         */
        UNDEFINED_FUNC("The function \"%s\" is not defined for %s arguments."),
        /**
         * Default value: "An operator was expected, but an expression was found."
         */
        OPERATOR_EXPECTED("An operator was expected, but an expression was found."),
        /**
         * Default value: "A sub-expression was expected, but the end of the expression was reached."
         */
        EXPR_END_REACHED("A sub-expression was expected, but the end of the expression was reached."),
        /**
         * Default value: "A sub-expression was expected, but an operator was found."
         */
        OPERATOR_FOUND("A sub-expression was expected, but an operator was found."),
        /**
         * Default value: "Unrecognized character %s."
         */
        UNKNOWN_CHAR("Unrecognized character %s.");

        private String msg;

        Message(String msg){
            this.msg = msg;
        }
    }

    /**
     * Call this method on each {@link Message} you want to customize to localize the messages emitted by the library.
     * @param m The message you want to modify.
     * @param s The string you want to bind to the {@link Message} specified as 1st argument.
     *          The message must be in the same format as the original one (i.e. if the original message is "%s can be rewritten as %s", your message must contain two "%s" format specifiers too).
     */
    public static void setMessage(Message m, String s){
        m.msg = s;
    }

    /**
     * Method used internally by the library to retrieve the string representation of a {@link Message}.
     * @param m The {@link Message} to obtain.
     * @param args Arguments to fill the format specifiers in the messages.
     * @return The string representation of the requested message formatted with the specified arguments.
     */
    protected static String getMessage(Message m, String ... args){
        return String.format(m.msg, args);
    }

}
