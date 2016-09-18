package com.github.gianlucanitti;

import com.github.gianlucanitti.javaexpreval.*;

/**
 * Main class of the command line tool that solves expression given as CLI arguments.
 * <b>WARNING:</b> when you run this tool from the command line, if your expression contains some symbols
 * such as ^ you probably (depending on your shell) need to enclose it in quotes to avoid these symbols to be
 * interpreted as shell special characters and removed from the string passed to the JVM and then to the main method.
 * This is required for example in Windows cmd.exe.
 */
public class App{

  /**
   * Executes the CLI tool with the specified arguments.
   * @param args An array of command line arguments. <code>args[0]</code> must contain the expression to solve;
   * if args[1] is present and equal to "-v", the tool will run in verbose mode, i.e. logging all the steps done to parse and evaluate the expression.
   */
  public static void main(String[] args){
    if(args.length != 1 && args.length != 2){
      System.err.println("Please specify expression as first command line argument. \"-v\" can be used as second argument to print the parsing log.");
      System.exit(1);
    }
    try{
      if(args.length == 2 && args[1].equals("-v"))
        System.out.println("Result = " + Expression.parse(args[0], System.out).eval(System.out));
      else
        System.out.println("Result = " + Expression.parse(args[0]).eval());
    }catch(/*Expression*/Exception e){
      e.printStackTrace();
    }
  }

}
