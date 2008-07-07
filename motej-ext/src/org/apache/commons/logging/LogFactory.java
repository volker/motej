package org.apache.commons.logging;

import org.apache.commons.logging.Log;

public class LogFactory extends Object {

    public LogFactory() {
    }

    public static Log getLog(Class clazz) {
        Log log = new Log() {
            public boolean isInfoEnabled() {
                return true;
            }
            public void info(Object message) {
                System.err.println(message);
            }
            public void info(Object message,Throwable t) {
                System.err.println(message);
            }
            public void error(Object message) {
                System.err.println(message);
            }
            public void error(Object message, Throwable t) {
                System.err.println(message);
            }
        };
        return log;
    }

}
