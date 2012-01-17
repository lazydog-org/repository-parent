package org.lazydog.repository.ldap.internal;


/**
 * Logging level.
 * 
 * @author  Ron Rickard
 */
public enum LoggingLevel {
    ALL,
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    OFF;

    public static LoggingLevel getLoggingLevel(String value) {
        return LoggingLevel.valueOf(value.toUpperCase());
    }
}
