package org.lazydog.repository.ldap.internal.parser;

import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.lazydog.repository.ldap.internal.LoggingLevel;


/**
 * Persistence LDAP parser test.
 *
 * @author  Ron Rickard
 */
public class PersistenceLDAPParserTest {

    private static final String JNDI_PATHNAME = "META-INF/persistence-ldap-jndi.xml";

    @Test
    public void testGetJndiName() throws Exception {
        String expected = "ldapProperties";
        PersistenceLDAPParser parser = PersistenceLDAPParser.newInstance(JNDI_PATHNAME);
        String actual = parser.getJndiName();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetInitialContextFactory() throws Exception {
    	String expected = "com.sun.jndi.ldap.LdapCtxFactory";
        PersistenceLDAPParser parser = PersistenceLDAPParser.newInstance();
        String actual = parser.getInitialContextFactory();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetLoggingLevel() throws Exception {
    	LoggingLevel expected = LoggingLevel.WARN;
    	PersistenceLDAPParser parser = PersistenceLDAPParser.newInstance();
        LoggingLevel actual = parser.getLoggingLevel();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetMappingFileNames() throws Exception {
        Set<String> expected = new HashSet<String>();
        expected.add("org/lazydog/test/em/group-em.xml");
        expected.add("org/lazydog/test/em/account-em.xml");
        PersistenceLDAPParser parser = PersistenceLDAPParser.newInstance();
        Set<String> actual = parser.getMappingFileNames();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetProviderUrl() throws Exception {
        String expected = "ldap://localhost:1024";
        PersistenceLDAPParser parser = PersistenceLDAPParser.newInstance();
        String actual = parser.getProviderUrl();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSecurityAuthentication() throws Exception {
        String expected = "simple";
        PersistenceLDAPParser parser = PersistenceLDAPParser.newInstance();
        String actual = parser.getSecurityAuthentication();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSecurityCredentials() throws Exception {
        String expected = "secret";
        PersistenceLDAPParser parser = PersistenceLDAPParser.newInstance();
        String actual = parser.getSecurityCredentials();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSecurityPrincipal() throws Exception {
        String expected = "uid=admin,ou=system";
        PersistenceLDAPParser parser = PersistenceLDAPParser.newInstance();
        String actual = parser.getSecurityPrincipal();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsJndiSetup() throws Exception {
    	boolean expected = true;
        PersistenceLDAPParser parser = PersistenceLDAPParser.newInstance(JNDI_PATHNAME);
        boolean actual = parser.isJndiSetup();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testNewInstance() throws Exception {
    	PersistenceLDAPParser.newInstance();
    }
}
