package com.github.gianlucanitti.javaexpreval;

import java.io.*;

/**
 * Utility class that represents an InputStream that always returns EOF when read. Like /dev/null but cross-platform.
 */
public class NullInputStream extends InputStream {

    @Override
    public int read(){
        return -1;
    }

    /**
     * Utility method to get an equivalent "null reader", i.e. a {@link Reader} that always returns EOF when read.
     * @return An {@link InputStreamReader} that always returns EOF when read.
     */
    public static InputStreamReader getReader(){
        return new InputStreamReader(new NullInputStream());
    }
}