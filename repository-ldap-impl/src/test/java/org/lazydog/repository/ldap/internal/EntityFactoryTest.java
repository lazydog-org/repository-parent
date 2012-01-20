package org.lazydog.repository.ldap.internal;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lazydog.repository.Entity;
import org.lazydog.test.model.Account;
import org.lazydog.test.model.Group;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;


/**
 * Entity factory test.
 *
 * @author  Ron Rickard
 */
public class EntityFactoryTest {

    private static final List<Class<?>> supportedTypes = new ArrayList<Class<?>>() {
        private static final long serialVersionUID = 1L;
        {
            add(Integer.class);
            add(String.class);
            add(Entity.class);
        }
    };

    private static Set<Account> accounts;
    private static Group group;
    private static Map<String,Object> propertyMap;
	
    @BeforeClass
    public static void initialize() {

        Logger logger = (Logger)LoggerFactory.getLogger(EntityFactory.class);
        logger.setLevel(Level.ALL);

        Account account1 = new Account();
        account1.setCanonicalName("testaccount1");
        account1.setDisplayName("Test Account1");
        account1.setId("uid=testaccount1,ou=accounts,o=test,ou=system");
        account1.setLastName("Account1");
        account1.setName("testaccount1");

        Account account2 = new Account();
        account2.setCanonicalName("testaccount2");
        account2.setDisplayName("Test Account2");
        account2.setId("uid=testaccount2,ou=accounts,o=test,ou=system");
        account2.setLastName("Account2");
        account2.setName("testaccount2");

        accounts = new HashSet<Account>();
    	accounts.add(account1);
    	accounts.add(account2);
    	
    	group = new Group();
        group.setAccounts(accounts);
        group.setDescription("Test Group");
        group.setId("cn=testgroup1,ou=groups,o=test,ou=system");
        group.setName("testgroup1");

        propertyMap = new HashMap<String,Object>();
        propertyMap.put("accounts", accounts);
        propertyMap.put("description", "Test Group");
        propertyMap.put("id", "cn=testgroup1,ou=groups,o=test,ou=system");
        propertyMap.put("name", "testgroup1");
    }

    @Test
    public void testCreateEntity() throws Exception {
    	Map<String,Object> propertyValues = new HashMap<String,Object>();
    	propertyValues.put("accounts", accounts);
    	propertyValues.put("description", "Test Group");
    	propertyValues.put("id", "cn=testgroup1,ou=groups,o=test,ou=system");
    	propertyValues.put("name", "testgroup1");
    	EntityFactory<Group> entityFactory = EntityFactory.newInstance(Group.class, supportedTypes);
    	Group createdGroup = entityFactory.createEntity(propertyValues);
    	assertEquals(group, createdGroup);
    }
    
    @Test
    public void testGetAccessorReturnType() throws Exception {
    	EntityFactory<Group> entityFactory = EntityFactory.newInstance(Group.class, supportedTypes);
    	Class<?> returnType = entityFactory.getAccessorReturnType("accounts");
    	assertEquals(Set.class, returnType);
    }
    
    @Test
    public void testGetMutatorParameterType() throws Exception {
    	EntityFactory<Group> entityFactory = EntityFactory.newInstance(Group.class, supportedTypes);
    	Class<?> parameterType = entityFactory.getMutatorParameterType("accounts");
    	assertEquals(Set.class, parameterType);
    }
    
    @Test
    public void testGetPropertyMap() throws Exception {
    	Set<String> propertyNames = new HashSet<String>();
    	propertyNames.add("accounts");
    	propertyNames.add("description");
    	propertyNames.add("id");
    	propertyNames.add("name");
    	EntityFactory<Group> entityFactory = EntityFactory.newInstance(Group.class, supportedTypes);
    	Map<String,Object> fetchedPropertyMap = entityFactory.getPropertyMap(group, propertyNames);
    	assertEquals(propertyMap, fetchedPropertyMap);
    }

    @Test
    public void testNewInstance() {
        EntityFactory.newInstance(Group.class, supportedTypes);
    }
}