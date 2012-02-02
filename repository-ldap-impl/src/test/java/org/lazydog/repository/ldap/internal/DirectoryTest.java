package org.lazydog.repository.ldap.internal;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.directory.server.core.integ.annotations.ApplyLdifs;
import org.apache.directory.server.core.integ.annotations.CleanupLevel;
import org.apache.directory.server.integ.SiRunner;
import org.apache.directory.server.ldap.LdapServer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;


/**
 * Directory test.
 * 
 * @author  Ron Rickard
 */
@RunWith(SiRunner.class)
@CleanupLevel(org.apache.directory.server.core.integ.Level.METHOD)
@ApplyLdifs( {
    "dn: o=test,ou=system\n" +
    "o: test\n" +
    "objectClass: top\n" + 
    "objectClass: organization\n\n" +

    "dn: ou=groups,o=test,ou=system\n" +
    "ou: groups\n" +
    "objectClass: organizationalunit\n" +
    "objectClass: top\n\n" +

    "dn: ou=accounts,o=test,ou=system\n" +
    "ou: accounts\n" +
    "objectClass: organizationalunit\n" + 
    "objectClass: top\n\n" +
	
    "dn: uid=testaccount1,ou=accounts,o=test,ou=system\n" +
    "uid: testaccount1\n" +
    "objectClass: account\n" +
    "objectClass: top\n\n" +
	
    "dn: uid=testaccount2,ou=accounts,o=test,ou=system\n" +
    "uid: testaccount2\n" +
    "objectClass: account\n" +
    "objectClass: top\n\n"
} ) 
public class DirectoryTest {

    public static LdapServer ldapServer;
    private static Set<String> attributeNames;
    private static Properties environment;
    private static String groupDn1;
    private static String groupDn2;
    private static Map<String,Set<String>> groupMap1;
    private static Map<String,Set<String>> groupMap2;
    private static Map<String,Map<String,Set<String>>> groupMaps;

    @BeforeClass
    public static void initialize() throws Exception {

    	Logger logger = (Logger)LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.ERROR);
        logger = (Logger)LoggerFactory.getLogger(Directory.class);
        logger.setLevel(Level.ALL);

        environment = new Properties();
    	environment.setProperty(Directory.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    	environment.setProperty(Directory.PROVIDER_URL, "ldap://localhost:1024");
    	environment.setProperty(Directory.SECURITY_AUTHENTICATION, "simple");
    	environment.setProperty(Directory.SECURITY_CREDENTIALS, "secret");
    	environment.setProperty(Directory.SECURITY_PRINCIPAL, "uid=admin,ou=system");
    }

    @Before
    public void beforeTest() throws Exception {
        attributeNames = new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("cn");
                add("description");
                add("uniqueMember");
                add("objectClass");
            }
        };
		
        groupDn1 = "cn=testgroup1,ou=groups,o=test,ou=system";
        groupMap1 = new HashMap<String,Set<String>>();
        groupMap1.put("cn", new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("testgroup1");
            }
        });
        groupMap1.put("description", new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("Test Group1");
            }
        });
        groupMap1.put("uniqueMember", new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("uid=testaccount1,ou=accounts,o=test,ou=system");
                add("uid=testaccount2,ou=accounts,o=test,ou=system");
            }
        });
        groupMap1.put("objectClass", new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("groupOfUniqueNames");
                add("top");
            }
        });
		
        groupDn2 = "cn=testgroup2,ou=groups,o=test,ou=system";
        groupMap2 = new HashMap<String,Set<String>>();
        groupMap2.put("cn", new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("testgroup2");
            }
        });
        groupMap2.put("description", new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("Test Group2");
            }
        });
        groupMap2.put("uniqueMember", new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("uid=testaccount1,ou=accounts,o=test,ou=system");
                add("uid=testaccount2,ou=accounts,o=test,ou=system");
            }
        });
        groupMap2.put("objectClass", new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("groupOfUniqueNames");
                add("top");
            }
        });

        groupMaps = new HashMap<String,Map<String,Set<String>>>();
        groupMaps.put(groupDn1, groupMap1);
        groupMaps.put(groupDn2, groupMap2);
    }

    @Test
    public void testAddEntry() throws Exception {
    	Directory directory = Directory.newInstance(environment);
    	directory.addEntry(groupDn1, groupMap1);
    	Map<String,Set<String>> actual = directory.getAttributeMap(groupDn1, attributeNames);
    	assertEquals(groupMap1, actual);
    }

    @Test
    public void testEntryExists() throws Exception {
    	Directory directory = Directory.newInstance(environment);
    	directory.addEntry(groupDn1, groupMap1);
    	boolean actual = directory.entryExists(groupDn1);
    	assertEquals(true, actual);
    }
    
    @Test
    public void testGetAttributeMap() throws Exception {
    	Directory directory = Directory.newInstance(environment);
    	directory.addEntry(groupDn2, groupMap2);
    	Map<String,Set<String>> actual = directory.getAttributeMap(groupDn2, attributeNames);
    	assertEquals(groupMap2, actual);
    }
    
    @Test
    public void testGetAttributeMaps() throws Exception {
    	Directory directory = Directory.newInstance(environment);
    	directory.addEntry(groupDn1, groupMap1);
    	directory.addEntry(groupDn2, groupMap2);
    	Map<String,Map<String,Set<String>>> actual = directory.getAttributeMaps("(&(objectClass=groupOfUniqueNames)(objectClass=top))", "o=test,ou=system", SearchScope.SUBTREE, attributeNames);
    	assertEquals(groupMaps, actual);
    }

    @Test
    public void testNewInstance() throws Exception {
    	Directory.newInstance(environment);
    }    

    @Test
    public void testRemoveEntry() throws Exception {
    	Directory directory = Directory.newInstance(environment);
    	directory.addEntry(groupDn1, groupMap1);
    	Map<String,Set<String>> actual = directory.getAttributeMap(groupDn1, attributeNames);
    	assertEquals(groupMap1, actual);
    	directory.removeEntry(groupDn1);
    	actual = directory.getAttributeMap(groupDn1, attributeNames);
    	assertEquals(new HashMap<String, Set<String>>(), actual);
    }
    
    @Test
    public void testUpdateEntryAddAttribute() throws Exception {
    	Directory directory = Directory.newInstance(environment);
    	directory.addEntry(groupDn1, groupMap1);
    	Map<String,Set<String>> actual = directory.getAttributeMap(groupDn1, attributeNames);
    	assertEquals(groupMap1, actual);
    	groupMap1.put("businessCategory", new HashSet<String>(){
            private static final long serialVersionUID = 1L;
            {
                add("Home Insurance");
                add("Auto Insurance");
            }
    	});
        attributeNames.add("businessCategory");
    	directory.updateEntry(groupDn1, groupMap1);
        actual = directory.getAttributeMap(groupDn1, attributeNames);
    	assertEquals(groupMap1, actual);
    }

    @Test
    public void testUpdateEntryIgnoreAttribute() throws Exception {
    	Directory directory = Directory.newInstance(environment);
    	directory.addEntry(groupDn1, groupMap1);
    	Map<String,Set<String>> actual = directory.getAttributeMap(groupDn1, attributeNames);
    	assertEquals(groupMap1, actual);
    	directory.updateEntry(groupDn1, groupMap1);
    	actual = directory.getAttributeMap(groupDn1, attributeNames);
    	assertEquals(groupMap1, actual);
    }
    
    @Test
    public void testUpdateEntryRemoveAttribute() throws Exception {
    	Directory directory = Directory.newInstance(environment);
    	directory.addEntry(groupDn1, groupMap1);
    	Map<String,Set<String>> actual = directory.getAttributeMap(groupDn1, attributeNames);
    	assertEquals(groupMap1, actual);
    	groupMap1.put("description", null);
    	directory.updateEntry(groupDn1, groupMap1);
    	groupMap1.remove("description");
        actual = directory.getAttributeMap(groupDn1, attributeNames);
        assertEquals(groupMap1, actual);
    }
    
    @Test
    public void testUpdateEntryReplaceAttribute() throws Exception {
    	Directory directory = Directory.newInstance(environment);
    	directory.addEntry(groupDn1, groupMap1);
    	Map<String,Set<String>> actual = directory.getAttributeMap(groupDn1, attributeNames);
    	assertEquals(groupMap1, actual);
    	groupMap1.put("description", new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("New Test Group1");
            }
        });
    	directory.updateEntry(groupDn1, groupMap1);
        actual = directory.getAttributeMap(groupDn1, attributeNames);
        assertEquals(groupMap1, actual);
    }
}