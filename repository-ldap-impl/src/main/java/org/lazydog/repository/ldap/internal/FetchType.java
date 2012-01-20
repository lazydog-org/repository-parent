package org.lazydog.repository.ldap.internal;


/**
 * Fetch type.
 * 
 * @author  Ron Rickard
 */
public enum FetchType {
    EAGER,
    LAZY;

    public static FetchType getFetchType(final String value) {
        return FetchType.valueOf(value.toUpperCase());
    }
}
