package com.github.gianlucanitti.javaexpreval;

import java.io.Writer;

/**
 * Class with static methods used to localize string messages used by the library (like exceptions, logging messages, etc.).
 */
public class LocalizationHelper {

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
        FAILED_STORE_RESULT("Warning: failed to store result. Reason: %s");

        private String msg;

        Message(String msg){
            this.msg = msg;
        }
    }

    /**
     * Call this method on each {@link Message} you want to customize to localize the messages emitted by the library.
     * @param m The message you want to modify.
     * @param s The string you want to bind to the {@link Message} specified as 1st argument.
     *          The message must be in the same format as the 3original one (i.e. if the original message is "%s can be rewritten as %s", your message must contain two "%s" format specifiers too).
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
