package com.github.gianlucanitti.javaexpreval;

        import java.io.*;
        import java.util.HashMap;

/**
 * An {@link ExpressionContext} with additional methods to interact with the user through {@link Reader}s (input) and {@link Writer}s (output).
 * It also handles any errors ({@link ExpressionException}s) by reporting them to a proper {@link Writer}.
 */
public class InteractiveExpressionContext extends ExpressionContext {

    public enum Command{
        CONTEXT, CLEAR, HELP, EXIT
    }

    private BufferedReader inputReader;
    private PrintWriter outputWriter;
    private PrintWriter verboseWriter;
    private PrintWriter errorWriter;
    private HashMap<String, Command> commands;
    private String prompt;
    private boolean helpVerbose;

    /**
     * Initializes an InteractiveExpressionContext that takes input from the specified {@link Reader} and writes output to the specified {@link Writer}s.
     * @param input The input {@link Reader}.
     * @param output The {@link Writer} where normal output (evaluation results, errors) will be written to.
     * @param verboseOutput The {@link Writer} where verbose output (evaluation steps, variable assignments) will be written to.
     * @param error The {@link Writer} where error output will be written to.
     * @param autoFlush boolean stating if the output {@link Writer}s must be automatically flushed. This is passed to {@link PrintWriter#PrintWriter(Writer, boolean)}.
     */
    public InteractiveExpressionContext(Reader input, Writer output, Writer verboseOutput, Writer error, boolean autoFlush){
        inputReader = new BufferedReader(input);
        outputWriter = new PrintWriter(output, autoFlush);
        verboseWriter = new PrintWriter(verboseOutput, autoFlush);
        errorWriter = new PrintWriter(error, autoFlush);
        setDefaultCommands();
        helpVerbose = false;
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
     * Writes an error message about the specified command to the error {@link Writer}
     * @param cmd The command generating the error.
     * @param error The error message.
     */
    private void reportCmdError(String cmd, String error){
        errorWriter.println("Invalid usage of " + cmd + ": " + error + ".");
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
     * @return <code>false</code> if the EXIT command is found, <code>true</code> if the end of the input stream is reached
     * ({@link BufferedReader#readLine()} on the input {@link Reader} returns <code>null</code>).
     * @throws IOException in case of IO problems with the {@link Reader} or {@link Writer}.
     */
    public boolean update() throws IOException{
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
                            verboseWriter.println("All variables have been deleted.");
                            break;
                        case HELP:
                            PrintWriter helpWriter = helpVerbose ? verboseWriter : outputWriter;
                            helpWriter.println("Accepted statements are expressions, assignments and commands.");
                            helpWriter.println("An expression can be formed by integer or decimal numbers, the +,-,*,/,^ binary operators, variables and parenthesis.");
                            helpWriter.println("An assignment is formed by a variable name followed by the = symbol and an expression, which is evaluated and bound to that variable.");
                            helpWriter.println("An empty assignment (in the form \"someVariable=\") deletes the variable.");
                            helpWriter.println("A variable is a string of one or more letters and/or underscores. Variables can't be named as commands, which are reserved words.");
                            helpWriter.println("The commands are: context (prints all the defined variables), clear (deletes all the variables), help (shows this message) and exit (stops reading input).");
                            break;
                        case EXIT:
                            return false;
                    }
                else{ //if the string doesn't begin with a known command
                    if (command.contains("=")) { //if it contains an =, then it's considered a variable assignment
                        String[] sides = command.split("=");
                        if(sides.length == 1){ //an assignment with nothing on the right of the equality symbol (e.g. "someVar=") deletes the variable
                            delVariable(sides[0]);
                            verboseWriter.println(sides[0] + " has been deleted.");
                        }else if (sides.length == 2) {
                            setVariable(sides[0], Expression.parse(sides[1], verboseWriter), verboseWriter);
                            verboseWriter.println(sides[0] + " is now " + getVariable(sides[0]));
                        }else {
                            reportCmdError("=", "only one = operator per command is allowed");
                        }
                    }else //otherwise, it's parsed as an expression
                        outputWriter.println(Expression.parse(command, verboseWriter).eval(this, verboseWriter));
                }
            }catch(ExpressionException ex){
                errorWriter.println(ex.getMessage());
            }
        }
        return true;
    }

}
