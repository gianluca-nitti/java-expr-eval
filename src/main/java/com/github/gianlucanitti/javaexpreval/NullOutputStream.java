package com.github.gianlucanitti.javaexpreval;

import java.io.*;

/**
 * Utility class that represents an OutputStream that discards anything that gets written to it. Like /dev/null but cross-platform.
 */
public class NullOutputStream extends OutputStream {

    @Override
    public void write(int b){}

    /**
     * Utility method to get an equivalent "null writer", i.e. a {@link Writer} that discards anything that gets written to it.
     * @return An {@link OutputStreamWriter} that discards anything that gets written to it.
     */
    public static OutputStreamWriter getWriter(){
        return new OutputStreamWriter(new NullOutputStream());
    }
}