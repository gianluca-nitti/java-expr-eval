package com.github.gianlucanitti.javaexpreval;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.github.gianlucanitti.javaexpreval.LocalizationHelper.*;

/**
 * An {@link ExpressionContext} with additional methods to interact with the user through {@link Reader}s (input) and {@link Writer}s (output).
 * It also handles any errors ({@link ExpressionException}s) by reporting them to a proper {@link Writer}.
 */
public class InteractiveExpressionContext extends ExpressionContext {

    /**
     * Enumeration of commands that the user can give to the context through the input {@link Reader}.
     * @see #setCommands(String, String, String, String)
     * @see #setDefaultCommands()
     */
    private enum Command{
        CONTEXT, CLEAR, HELP, EXIT
    }

    /**
     * The reasons that can cause {@link #update()} to return.
     * @see #update()
     */
    public enum Status{
        /**
         * {@link #update()} returned because the end of the input {@link Reader} was reached.
         */
        INPUT_END,
        /**
         * {@link #update()} returned because an exit command was read from the input {@link Reader}
         * ({@link BufferedReader#readLine()} returned <code>null</code>).
         */
        EXIT,
        /**
         * {@link #update()} returned because an error occurred while executing a statement
         *  and {@link #setStopOnError(boolean)} was called with <code>true</code> before calling {@link #update()}.
         */
        ERROR
    }

    private BufferedReader inputReader;
    private PrintWriter outputWriter;
    private PrintWriter verboseWriter;
    private PrintWriter errorWriter;
    private HashMap<String, Command> commands;
    private String prompt;
    private boolean helpVerbose;
    private boolean stopOnError;

    /**
     * Initializes an InteractiveExpressionContext that takes input from the specified {@link Reader} and writes output to the specified {@link Writer}s.
     * @param input The input {@link Reader}.
     * @param output The {@link Writer} where normal output evaluation results will be written to.
     * @param verboseOutput The {@link Writer} where verbose output (evaluation steps, variable assignments) will be written to.
     * @param error The {@link Writer} where error output will be written to.
     * @param autoFlush boolean stating if the output {@link Writer}s must be automatically flushed. This is passed to {@link PrintWriter#PrintWriter(Writer, boolean)}.
     */
    public InteractiveExpressionContext(Reader input, Writer output, Writer verboseOutput, Writer error, boolean autoFlush){
        setInputReader(input);
        setOutputWriter(output, autoFlush);
        setVerboseOutputWriter(verboseOutput, autoFlush);
        setErrorOutputWriter(error, autoFlush);
        setDefaultCommands();
        setPrompt("");
        helpVerbose = false;
    }

    /**
     * Initializes an InteractiveExpressionContext without attached I/O.
     * You can later use {@link #setInputReader(Reader)}, {@link #setOutputWriter(Writer, boolean)}, {@link #setVerboseOutputWriter(Writer, boolean)}, {@link #setErrorOutputWriter(Writer, boolean)}
     * to attach this object to I/O streams.
     */
    public InteractiveExpressionContext(){
        this(NullInputStream.getReader(), NullOutputStream.getWriter(), NullOutputStream.getWriter(), NullOutputStream.getWriter(), false);
    }

    /**
     * Changes the strings that will be recognized as commands by the {@link #update()} method.
     * @param context The string identifying the command to print all the defined variables.
     * @param clear The string identifying the command to un-define all the variables.
     * @param help The string identifying the help command.
     * @param exit The string identifying the exit command.
     */
    public void setCommands(String context, String clear, String help, String exit){
        commands = new HashMap<String, Command>();
        commands.put(context, Command.CONTEXT);
        commands.put(clear, Command.CLEAR);
        commands.put(help, Command.HELP);
        commands.put(exit, Command.EXIT);
    }

    /**
     * Sets the strings that will be recognized as commands to the default ones.
     * Equivalent to setCommands("set", "delete", "clear").
     */
    public void setDefaultCommands(){
        setCommands("context", "clear", "help", "exit");
    }

    /**
     * Sets the input {@link Reader}.
     * @param r The {@link Reader} from which user input will be read.
     */
    public void setInputReader(Reader r){
        inputReader = new BufferedReader(r);
    }

    /**
     * Sets the output {@link Writer} for evaluation results.
     * @param w The {@link Writer} where evaluation results will be written to.
     * @param autoFlush boolean stating if this {@link Writer} must be automatically flushed. This is passed to {@link PrintWriter#PrintWriter(Writer, boolean)}.
     */
    public void setOutputWriter(Writer w, boolean autoFlush){
        outputWriter = new PrintWriter(w, autoFlush);
    }

    /**
     * Sets the {@link Writer} for verbose output (parsing and evaluation steps, variable assignments).
     * @param w The {@link Writer} where verbose output will be written to.
     * @param autoFlush boolean stating if this {@link Writer} must be automatically flushed. This is passed to {@link PrintWriter#PrintWriter(Writer, boolean)}.
     */
    public void setVerboseOutputWriter(Writer w, boolean autoFlush){
        verboseWriter = new PrintWriter(w, autoFlush);
    }

    /**
     * Sets the {@link Writer} for error messages output.
     * @param w The {@link Writer} where error messages will be written to.
     * @param autoFlush boolean stating if this {@link Writer} must be automatically flushed. This is passed to {@link PrintWriter#PrintWriter(Writer, boolean)}.
     */
    public void setErrorOutputWriter(Writer w, boolean autoFlush){
        errorWriter = new PrintWriter(w, autoFlush);
    }

    /**
     * Sets a string to print to the output {@link Writer} before reading a line from the input {@link Reader}.
     * Note that when a prompt is set, the output writer will be flushed every time the prompt is written to it.
     * @param p The prompt string.
     */
    public void setPrompt(String p){
        prompt = p;
    }

    /**
     * Set a parameter that determines if help messages should be written to the verbose {@link Writer} rather than to the default output one.
     * @param value <code>true</code> if help messages must go to the verbose writer, <code>false</code> if they must go to the default output writer.
     */
    public void setHelpVerbose(boolean value){
        helpVerbose = value;
    }

    /**
     * Set a parameter that determines if the {@link #update()} method should stop reading input and return when an error occurs. Defaults to <code>false</code>.
     * @param value <code>true</code> if {@link #update()} should stop reading input on errors, <code>false</code> is it should continue anyway (default).
     */
    public void setStopOnError(boolean value){
        stopOnError = value;
    }

    /**
     * Prints the prompt (if set) to the output {@link Writer}, then reads a line from the input {@link Reader}.
     * @return The line read.
     * @throws IOException if an {@link IOException} is thrown while trying to read the input.
     */
    private String getLine() throws IOException{
        if(prompt != null) {
            outputWriter.print(prompt);
            outputWriter.flush();
        }
        return inputReader.readLine();
    }

    /**
     * Reads commands from the input {@link Reader}, if any, and executes them.
     * It then writes their output, if any, to the output {@link Writer}s.
     * @return One of the {@link Status} values depending on what caused the method to return (see {@link Status} for more detail).
     * @throws IOException in case of IO problems with the {@link Reader} or {@link Writer}.
     * @see Status
     */
    public Status update() throws IOException{
        String command;
        while((command = getLine()) != null){
            try {
                if(commands.containsKey(command))
                    switch (commands.get(command)) {
                        case CONTEXT:
                            outputWriter.println(toString());
                            break;
                        case CLEAR:
                            clear();
                            verboseWriter.println(getMessage(Message.CONTEXT_CLEARED));
                            break;
                        case HELP:
                            PrintWriter helpWriter = helpVerbose ? verboseWriter : outputWriter;
                            helpWriter.println(getMessage(Message.INTERACTIVE_HELP));
                            break;
                        case EXIT:
                            return Status.EXIT;
                    }
                else{ //if the string doesn't begin with a known command
                    if (command.contains("=")) { //if it contains an =, then it's considered an assignment
                        String[] sides = command.split("=");
                        if(sides.length == 1){ //an assignment with nothing on the right of the equality symbol (e.g. "someVar=" or "someFun(1)=") deletes the variable/function (if function, the number of arguments must be specified)
                            Pattern pattern = Pattern.compile("^([A-Za-z0-9_]*?)(?:\\((\\d+)\\))?$");
                            Matcher matcher = pattern.matcher(sides[0].trim()); //remove spaces and parse with regex
                            if(!matcher.matches())
                                errorWriter.println(getMessage(Message.INCORRECT_DELETE));
                            else {
                                if (matcher.group(2) == null) //no arguments, so it's a variable deletion
                                    delVariable(matcher.group(1));
                                else //argument number is specified, so it's a function deletion
                                    delFunction(matcher.group(1), Integer.parseInt(matcher.group(2)));
                                verboseWriter.println(getMessage(Message.VAR_DELETED, sides[0]));
                            }
                        }else if (sides.length == 2) {
                            Pattern pattern = Pattern.compile("^(.*?)(?:\\((.*)\\))?$");
                            Matcher matcher = pattern.matcher(sides[0].trim()); //remove spaces and parse with regex
                            matcher.matches();
                            String symName = matcher.group(1);
                            if(commands.containsKey(symName)){
                                errorWriter.println(getMessage(Message.RESERVED_WORD, symName));
                                if(stopOnError) return Status.ERROR;
                            }else {
                                boolean readOnly;
                                if(readOnly = symName.startsWith("readonly ")) //this intentionally assigns readOnly (not an equality condition typo)
                                    symName = symName.substring("readonly ".length());
                                Expression expr = Expression.parse(sides[1], verboseWriter);
                                if(matcher.group(2) == null) { //no arguments, so it's a variable definition
                                    setVariable(symName, readOnly, expr, verboseWriter);
                                    verboseWriter.println(getMessage(Message.VAR_ASSIGNED, symName, Double.toString(getVariable(symName))));
                                }else { //argument names are specified, so it's a function definition
                                    setFunction(symName, expr, readOnly, matcher.group(2).replace(" ", "").split(","));
                                    verboseWriter.println(getMessage(Message.FUNC_ASSIGNED, matcher.group(0), expr.toString()));
                                }
                            }
                        }else {
                            errorWriter.println(getMessage(Message.ONLY_ONE_EQUALITY));
                            if(stopOnError) return Status.ERROR;
                        }
                    }else //otherwise, it's parsed as an expression
                        outputWriter.println(Expression.parse(command, verboseWriter).eval(this, verboseWriter));
                }
            }catch(ExpressionException ex){
                errorWriter.println(ex.getMessage());
                if(stopOnError) return Status.ERROR;
            }
        }
        return Status.INPUT_END;
    }

}
