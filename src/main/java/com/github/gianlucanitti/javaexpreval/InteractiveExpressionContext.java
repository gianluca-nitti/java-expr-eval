package com.github.gianlucanitti.javaexpreval;

import java.io.*;
import java.util.HashMap;

/**
 * An {@link ExpressionContext} with additional methods to interact with the user through {@link Reader}s (input) and {@link Writer}s (output).
 * It also handles any errors ({@link ExpressionException}s) by reporting them to a proper {@link Writer}.
 */
public class InteractiveExpressionContext extends ExpressionContext {

    public enum Command{
        SET, DELETE, CLEAR, HELP, EXIT
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
     * @param set The string identifying the command to define a variable.
     * @param delete The string identifying the command to un-define a variable.
     * @param clear The string identifying the command to un-define all the variables.
     */
    public void setCommands(String set, String delete, String clear, String help, String exit){
        commands = new HashMap<String, Command>();
        commands.put(set, Command.SET);
        commands.put(delete, Command.DELETE);
        commands.put(clear, Command.CLEAR);
        commands.put(help, Command.HELP);
        commands.put(exit, Command.EXIT);
    }

    /**
     * Sets the strings that will be recognized as commands to the default ones.
     * Equivalent to setCommands("set", "delete", "clear").
     */
    public void setDefaultCommands(){
        setCommands("set", "delete", "clear", "help", "exit");
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
     * @return <code>false</code> if the EXIT command is found, otherwise <code>false</code>.
     * @throws IOException in case of IO problems with the {@link Reader} or {@link Writer}.
     */
    public boolean update() throws IOException{
        String command;
        while((command = getLine()) != null){
            String[] args = command.split("\\s+");
            if(args.length == 0)
                continue;
            try {
                if(commands.containsKey(args[0]))
                    switch (commands.get(args[0])) {
                        case SET:
                            if (args.length < 3) {
                                reportCmdError(args[0], "a variable and an expression must be specified, separated by spaces");
                                continue;
                            }
                            String expr = "";
                            for(int i = 2; i < args.length; i++)
                                expr += args[i];
                            setVariable(args[1], Expression.parse(expr, verboseWriter));
                            verboseWriter.println(args[1] + " is now " + getVariable(args[1]));
                            break;
                        case DELETE:
                            if (args.length != 2) {
                                reportCmdError(args[0], "a single argument (the name of a variable) must be specified");
                                continue;
                            }
                            delVariable(args[1]);
                            verboseWriter.println(args[1] + " has been deleted.");
                            break;
                        case CLEAR:
                            if (args.length != 1) {
                                reportCmdError(args[0], "no arguments must be specified");
                                continue;
                            }
                            clear();
                            verboseWriter.println("All variables has been deleted.");
                            break;
                        case HELP:
                            PrintWriter helpWriter = helpVerbose ? verboseWriter : outputWriter;
                            helpWriter.println("Available commands: set, delete, clear, help, exit"); //TODO
                            break;
                        case EXIT:
                            return false;
                    }
                else{ //if the string doesn't begin with a known command
                    if (command.contains("=")) { //if it contains an =, then it's considered a variable assignment (like with the set command)
                        String[] sides = command.split("=");
                        if (sides.length != 2)
                            reportCmdError("=", "only one = operator per command is allowed");
                        else {
                            setVariable(sides[0], Expression.parse(sides[1], verboseWriter));
                            verboseWriter.println(sides[0] + " is now " + getVariable(sides[0]));
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
