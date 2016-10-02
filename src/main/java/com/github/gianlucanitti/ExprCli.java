package com.github.gianlucanitti;

import com.github.gianlucanitti.javaexpreval.*;
import java.io.*;

/**
 * Main class of the command line tool that solves expression given as CLI arguments.
 * <b>WARNING:</b> when you run this tool from the command line, if your expression contains some symbols
 * such as ^ you probably (depending on your shell) need to enclose it in quotes to avoid these symbols to be
 * interpreted as shell special characters and removed from the string passed to the JVM and then to the main method.
 * This is required for example in Windows cmd.exe.
 */
public class ExprCli {

    private static class CliOptions{

        private boolean quiet = false;
        private boolean batch = false;
        private boolean failOnError = false;
        private boolean help = false;
        private String inputFile = null;
        private String outputFile = null;
        private String statements = "";

        private CliOptions(String[] args){
            int i = 0;
            while(i < args.length){
                if(args[i].equals("-q") || args[i].equals("--quiet"))
                    quiet = true;
                else if(args[i].equals("-b") || args[i].equals("--batch"))
                    batch = true;
                else if(args[i].equals("-i") || args[i].equals("--input"))
                    inputFile = args[++i];
                else if(args[i].equals("-o") || args[i].equals("--output"))
                    outputFile = args[++i];
                else if(args[i].equals("-f") || args[i].equals("--fail-on-error"))
                    failOnError = true;
                else if(args[i].equals("-h") || args[i].equals("--help"))
                    help = true;
                else
                    statements += args[i] + System.getProperty("line.separator");
                i++;
            }
        }

    }

    private static InteractiveExpressionContext context;

    private static void updateSafe() throws Exception{
        if(context.update() == InteractiveExpressionContext.Status.ERROR)
            throw new Exception();
    }

    private static void showHelp(){
        String nl = System.getProperty("line.separator");
        System.out.println("CLI tool help (for help on expression/statement syntax, run without arguments then write help in the java-expr-eval prompt)" + nl +
                " Usage: java -jar javaexpreval-VERSION.jar [options] [statements]" + nl +
                " Options:" + nl +
                "  -b, --batch                Batch mode, don't read from stdin and exit after statements in input file and/or command line are executed." + nl +
                "  -f, --fail-on-error        Exit (with status code 1) if an error occurs while executing statements from any source" + nl +
                "  -h, --help                 Show this help message" + nl +
                "  -i <file>, --input <file>  Read statements from <file> (one line = one statement)" + nl +
                "  -o <file>, --output <file> Write output to <file>" + nl +
                "  -q, --quiet                Don't be verbose (write results only, not evaluation steps)" + nl +
                " Statements will be processed in the following order: file (if -i/--input is specified), command line (if any, separated by spaces), standard input (if -b/--batch isn't specified)." + nl +
                " When specifying expressions on the command line, please note that on some shells some math operators (like ^) may be special characters" +
                " and thus won't be passed to the JVM and to the program. Enclosing expressions in double quotes can avoid this."
        );
        System.exit(0);
    }

    /**
     * Executes the CLI tool with the specified arguments.
     * @param args An array of command line arguments. Use -h or --help to see a list.
     */
    public static void main(String[] args) {
        CliOptions options = new CliOptions(args);
        if(options.help)
            showHelp();
        int exitCode = 0;
        try {
            Reader in = new InputStreamReader(System.in);
            Writer out = new OutputStreamWriter(System.out);
            try {
                context = new InteractiveExpressionContext();
                context.setStopOnError(options.failOnError);
                if (options.outputFile != null)
                    out = new FileWriter(options.outputFile);
                context.setOutputWriter(out, true);
                if (!options.quiet)
                    context.setVerboseOutputWriter(out, true);
                context.setErrorOutputWriter(new OutputStreamWriter(System.err), true);
                if (options.inputFile != null) {
                    in = new FileReader(options.inputFile);
                    context.setInputReader(in);
                    updateSafe(); //process statements from file
                    in.close();
                }
                context.setInputReader(new StringReader(options.statements));
                updateSafe(); //process statements from cli arguments
                if (!options.batch) {
                    context.setInputReader(new InputStreamReader(System.in));
                    if (options.outputFile == null)
                        context.setPrompt("> ");
                    updateSafe(); //process statements from stdin
                }
            }finally{
                in.close();
                out.close();
            }
        }catch(Exception ex){
            exitCode = 1;
        }
        System.exit(exitCode);
    }

}
