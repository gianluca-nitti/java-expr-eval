package com.github.gianlucanitti;

import com.github.gianlucanitti.javaexpreval.*;

/**
 * Hello world!
 *
 */
public class App{

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
