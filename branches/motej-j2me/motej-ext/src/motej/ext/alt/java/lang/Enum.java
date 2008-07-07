package motej.ext.alt.java.lang;

import java.io.*;
import motej.ext.alt.java.lang.CloneNotSupportedException;

public abstract class Enum
    /*implements Comparable, Serializable*/ {

    private final String name;
    private final int ordinal;


    protected Enum(String s, int i) {
        name = s;
        ordinal = i;
    }

    protected final Object clone()
        throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

}
