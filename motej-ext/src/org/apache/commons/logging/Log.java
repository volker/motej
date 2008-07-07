package org.apache.commons.logging;

public interface Log {
    public boolean isInfoEnabled();
    public void info(Object message);
    public void info(Object message,Throwable t);
    public void error(Object message);
    public void error(Object message, Throwable t);
}
