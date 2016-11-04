# java-expr-eval [![Build Status](https://travis-ci.org/gianluca-nitti/java-expr-eval.svg?branch=master)](https://travis-ci.org/gianluca-nitti/java-expr-eval) [![Release](https://jitpack.io/v/gianluca-nitti/java-expr-eval.svg)](https://jitpack.io/#gianluca-nitti/java-expr-eval)
Simple Java library and command line tool to evaluate math expressions.

# Library features
* Supports +, -, *, /, ^ operators with correct precedence (first ^ is evaluated, followed by * and / and then + and -).
* Supports logging the parsing/evaluation steps done to a `Writer` or an `OutputStream`.
* Multiple custom exceptions to accurately report syntax errors in expressions.
* Support for variables, that can be defined in a context (see the javadoc for more information).
* Support for functions, that can be built-in (like basic trigonometry, logarithms, square root) or user-defined in a context as expression with parameters.
* Functions support overloading (e.g. you can define two different functions with the same name but different number of arguments in the same context).
* Functions and variables can be defined as readonly to prevent them to be modified in the context.

# CLI tool features
* Supports reading input from command line arguments, files and stdin, and writing output to stdout or files (see -h or --help for details).
* Interactive context: variables and functions can be added, changed or removed during the session.

#Documentation
Full javadoc is available [here](https://jitpack.io/com/github/gianluca-nitti/java-expr-eval/-SNAPSHOT/javadoc/overview-summary.html).
This is always updated from latest commit. See releases page for javadoc of specific versions.

#Using as command line tool
To start in interactive mode, just run without arguments (you may need to adjust the JAR filename according to the version):
```
java -jar javaexpreval-3.0.jar
```
You will see a prompt where you can type expressions, variable/function assignments and commands (actually *context*, *clear*, *help* and *exit*); type *help* at this prompt for more instructions.
Other than results, you will notice that all the parsing and evaluation steps will be written to stdout too. Use *-q* or *--quiet* on the command line if you want to disable this.
Other available command line switches can be used to specify I/O files (*-i &lt;file&gt;*, *-o &lt;file&gt;*), if the program must exit at the first error (*-f*), and if it should exit automatically after evaluating input file and expressions from command line (*-b*). Use *-h* or *--help* for more details.
Expressions or commands can be specified as command line arguments too, separated by spaces.
**Warning:** If you write expressions as CLI arguments, quotes may be necessary depending on your shell. For example, on Windows cmd.exe, ^ is a special shell character and won't be passed to the JVM (and to the expression parser), so for example if you write 2^3 the application will read 23. This is easily solved using quotes ("2^3").

#Code example
Code:
```
Writer stdout = new OutputStreamWriter(System.out);
ExpressionContext c = new ExpressionContext();
try {
    //Bind a variable to a value
    c.setVariable("someVar", 5);
    //Define a two-argument version of the log function to calculate a logarithm in the specified base.
    //This won't conflict with the built-in 1-argument log function (which it uses) thanks to overloading.
    c.setFunction("log", Expression.parse("log(x)/log(base)"), "x", "base");
    //Parse and evaluate an expression
    Expression expr = Expression.parse("3.2+log(16, 2)*someVar-6^((15+(-someVar)*2)-3)", stdout); //Log parsing steps to System.out (no 2nd parameter can be specified if no logging is desidered)
    double result = expr.eval(c, stdout); //Evaluate in context c and log evaluation steps to System.out (same here)
    System.out.println("Result = " + result);
}catch(ExpressionException e){
    e.printStackTrace();
}
```
Output:
```
16 can be rewritten as 16.0
 2 can be rewritten as 2.0
-someVar can be rewritten as (-(someVar))
15+(-someVar)*2 can be rewritten as (15.0+((-(someVar))*2.0))
(15+(-someVar)*2)-3 can be rewritten as ((15.0+((-(someVar))*2.0))-3.0)
3.2+log(16, 2)*someVar-6^((15+(-someVar)*2)-3) can be rewritten as ((3.2+(log(16.0,2.0)*someVar))-(6.0^((15.0+((-(someVar))*2.0))-3.0)))
16.0 evaluates to 16.0
log(16.0) evaluates to 2.772588722239781
2.0 evaluates to 2.0
log(2.0) evaluates to 0.6931471805599453
(log(16.0)/log(2.0)) evaluates to 4.0
log(16.0,2.0) evaluates to 4.0
someVar evaluates to 5.0
(log(16.0,2.0)*someVar) evaluates to 20.0
(3.2+(log(16.0,2.0)*someVar)) evaluates to 23.2
someVar evaluates to 5.0
(-(someVar)) evaluates to -5.0
((-(someVar))*2.0) evaluates to -10.0
(15.0+((-(someVar))*2.0)) evaluates to 5.0
((15.0+((-(someVar))*2.0))-3.0) evaluates to 2.0
(6.0^((15.0+((-(someVar))*2.0))-3.0)) evaluates to 36.0
((3.2+(log(16.0,2.0)*someVar))-(6.0^((15.0+((-(someVar))*2.0))-3.0))) evaluates to -12.8
Result = -12.8
```

#Installing the library to your project
This project is built and published with [jitpack](https://jitpack.io).
If you use maven, add this repository
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
and this dependency (adjust the version if needed, according to the releases page; a commit hash can be used too)
```
<dependency>
    <groupId>com.github.gianluca-nitti</groupId>
    <artifactId>java-expr-eval</artifactId>
    <version>v3.0</version>
</dependency>
```
See the [jitpack page](https://jitpack.io/#gianluca-nitti/java-expr-eval/) for instructions for other build systems (gradle, sbt or leiningen).

#Building from source/contributing
This library uses [Apache Maven](https://maven.apache.org/) as build system.
To build you simply need to `mvn package` in the project root (where you cloned this repository). This will compile, run tests, and put the JAR and the javadoc in the `target/` subdirectory.
You can run `mvn install` if you want to install to your local maven repository.