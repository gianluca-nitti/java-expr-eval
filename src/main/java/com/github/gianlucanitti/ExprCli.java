package com.github.gianlucanitti;

import com.github.gianlucanitti.javaexpreval.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Main class of the command line tool that solves expression given as CLI arguments.
 * <b>WARNING:</b> when you run this tool from the command line, if your expression contains some symbols
 * such as ^ you probably (depending on your shell) need to enclose it in quotes to avoid these symbols to be
 * interpreted as shell special characters and removed from the string passed to the JVM and then to the main method.
 * This is required for example in Windows cmd.exe.
 */
public class ExprCli{

  /**
   * Executes the CLI tool with the specified arguments.
   * @param args An array of command line arguments. <code>args[0]</code> must contain the expression to solve;
   * if args[1] is present and equal to "-v", the tool will run in verbose mode, i.e. logging all the steps done to parse and evaluate the expression.
   */
  public static void main(String[] args) throws IOException{
    InputStreamReader stdinReader = new InputStreamReader(System.in);
    OutputStreamWriter stdoutWriter = new OutputStreamWriter(System.out);
    OutputStreamWriter stderrWriter = new OutputStreamWriter(System.err);
    InteractiveExpressionContext context = new InteractiveExpressionContext(stdinReader, stdoutWriter, stdoutWriter, stderrWriter, true);
    context.setPrompt("> ");
    while(context.update());
    /*try{
      if(args.length == 2 && args[1].equals("-v"))
        System.out.println("Result = " + Expression.parse(args[0], stdoutWriter).eval(stdoutWriter));
      else
        System.out.println("Result = " + Expression.parse(args[0]).eval());
    }catch(ExpressionException e){
      e.printStackTrace();
    }*/
  }

}
