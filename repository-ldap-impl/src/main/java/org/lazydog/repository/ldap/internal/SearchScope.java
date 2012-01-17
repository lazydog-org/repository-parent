package org.lazydog.repository.ldap.internal;

/**
 * Search scope.
 * 
 * @author  Ron Rickard
 */
public enum SearchScope {
    ONE,
    SUBTREE;
    
    public static SearchScope getSearchScope(String value) {
        return SearchScope.valueOf(value.toUpperCase());
    }
}
