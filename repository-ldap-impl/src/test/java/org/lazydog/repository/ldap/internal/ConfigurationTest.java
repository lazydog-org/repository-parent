package org.lazydog.repository.ldap.internal;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.directory.server.core.integ.annotations.CleanupLevel;
import org.apache.directory.server.integ.SiRunner;
import org.apache.directory.server.ldap.LdapServer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lazydog.test.model.Account;
import org.lazydog.test.model.Group;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;


/**
 * Configuration test.
 *
 * @author  Ron Rickard
 */
@RunWith(SiRunner.class)
@CleanupLevel(org.apache.directory.server.core.integ.Level.CLASS)
public class ConfigurationTest {

    public static LdapServer ldapServer;

    @BeforeClass
    public static void initialize() {
        Logger logger = (Logger)LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.ERROR);
        logger = (Logger)LoggerFactory.getLogger(Configuration.class);
        logger.setLevel(Level.ALL);
    }
	
    @Test
    public void testGetAttributeName() throws Exception {
    	String expected = "member";
    	Configuration configuration = Configuration.newInstance();
    	String actual = configuration.getAttributeName(Group.class, "accounts");
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetAttributeNames() throws Exception {
    	Set<String> expected = new HashSet<String>();
    	expected.add("member");
    	expected.add("description");
    	expected.add("cn");
    	Configuration configuration = Configuration.newInstance();
    	Set<String> actual = configuration.getAttributeNames(Group.class);
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetEntityClasses() throws Exception {
    	Set<Class<?>> expected = new HashSet<Class<?>>();
    	expected.add(Account.class);
    	expected.add(Group.class);
    	Configuration configuration = Configuration.newInstance();
    	Set<Class<?>> actual = configuration.getEntityClasses();
    	assertEquals(expected, actual);
    }

    @Test
    public void testGetFetchType() throws Exception {
    	FetchType expected = FetchType.EAGER;
    	Configuration configuration = Configuration.newInstance();
    	FetchType actual = configuration.getFetchType(Group.class, "accounts");
    	assertEquals(expected, actual);
    }

    @Test
    public void testGetObjectClassValues() throws Exception {
    	Set<String> expected = new HashSet<String>();
    	expected.add("groupOfNames");
    	expected.add("top");
    	Configuration configuration = Configuration.newInstance();
    	Set<String> actual = configuration.getObjectClassValues(Group.class);
    	assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyAttributeMap() throws Exception {
    	Map<String,String> expected = new HashMap<String,String>();
    	expected.put("accounts", "member");
    	expected.put("description", "description");
    	expected.put("name", "cn");
    	Configuration configuration = Configuration.newInstance();
    	Map<String,String> actual = configuration.getPropertyAttributeMap(Group.class);
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPropertyName() throws Exception {
    	String expected = "accounts";
    	Configuration configuration = Configuration.newInstance();
    	String actual = configuration.getPropertyName(Group.class, "member");
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPropertyNames() throws Exception {
    	Set<String> expected = new HashSet<String>();
    	expected.add("accounts");
    	expected.add("description");
    	expected.add("name");
    	Configuration configuration = Configuration.newInstance();
    	Set<String> actual = configuration.getPropertyNames(Group.class);
    	assertEquals(expected, actual);
    }

    @Test
    public void testGetReferentialIntegrityAttribute() throws Exception {
    	String expected = "mail";
    	Configuration configuration = Configuration.newInstance();
    	String actual = configuration.getReferentialIntegrityAttribute(Group.class, "member");
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetReferentialIntegrityMap() throws Exception {
    	Map<String,String> expected = new HashMap<String,String>();
    	expected.put("member", "mail");
    	Configuration configuration = Configuration.newInstance();
    	Map<String,String> actual = configuration.getReferentialIntegrityMap(Group.class);
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSearchBase() throws Exception {
    	String expected = "ou=system";
    	Configuration configuration = Configuration.newInstance();
    	String actual = configuration.getSearchBase(Group.class);
    	assertEquals(expected, actual);
    }

    @Test
    public void testGetTargetEntityClass() throws Exception {
    	Class<?> expected = Account.class;
    	Configuration configuration = Configuration.newInstance();
    	Class<?> actual = configuration.getTargetEntityClass(Group.class, "accounts");
    	assertEquals(expected, actual);
    }

    @Test
    public void isEntityType() throws Exception {
    	boolean expected = true;
    	Configuration configuration = Configuration.newInstance();
        boolean actual = configuration.isEntityType(Group.class, "accounts");
        assertEquals(expected, actual);
    }

    @Test
    public void testNewInstance() throws Exception {
    	Configuration.newInstance();
    }
}
