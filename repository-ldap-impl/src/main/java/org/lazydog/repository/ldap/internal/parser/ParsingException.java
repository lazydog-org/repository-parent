package org.lazydog.repository.ldap.internal.parser;

import java.io.Serializable;


/**
 * Parsing exception.
 *
 * @author  Ron Rickard
 */
public final class ParsingException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;
    private String configurationPathname;
    private String schemaPathname;

    /**
     * Constructs a new exception with no message.
     *
     * @param  configurationPathname  the configuration pathname.
     * @param  schemaPathname         the schema pathname.
     */
    public ParsingException(final String configurationPathname, final String schemaPathname) {
        super();
        this.configurationPathname = configurationPathname;
        this.schemaPathname = schemaPathname;
    }

    /**
     * Constructs a new exception with the specified message.
     *
     * @param  message                the message.
     * @param  configurationPathname  the configuration pathname.
     * @param  schemaPathname         the schema pathname.
     */
    public ParsingException(final String message, final String configurationPathname, final String schemaPathname) {
        super(message);
        this.configurationPathname = configurationPathname;
        this.schemaPathname = schemaPathname;
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param  message                the message.
     * @param  cause                  the cause.
     * @param  configurationPathname  the configuration pathname.
     * @param  schemaPathname         the schema pathname.
     */
    public ParsingException(final String message, final Throwable cause, final String configurationPathname, final String schemaPathname) {
        super(message, cause);
        this.configurationPathname = configurationPathname;
        this.schemaPathname = schemaPathname;
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause                  the cause.
     * @param  configurationPathname  the configuration pathname.
     * @param  schemaPathname         the schema pathname.
     */
    public ParsingException(final Throwable cause, final String configurationPathname, final String schemaPathname) {
        super(cause);
        this.configurationPathname = configurationPathname;
        this.schemaPathname = schemaPathname;
    }

    /**
     * Get the configuration pathname.
     *
     * @return  the configuration pathname.
     */
    public String getConfigurationPathname() {
        return this.configurationPathname;
    }

    /**
     * Get the schema pathname.
     *
     * @return  the schema pathname.
     */
    public String getSchemaPathname() {
        return this.schemaPathname;
    }
}
