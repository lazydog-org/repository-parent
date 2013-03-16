/**
 * Copyright 2010-2013 lazydog.org.
 *
 * This file is part of repository.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lazydog.repository.ldap.internal;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lazydog.repository.Entity;
import org.lazydog.test.model.Account;
import org.lazydog.test.model.Group;
import org.slf4j.LoggerFactory;

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

        Logger logger = (Logger)LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.ERROR);

        Account account1 = new Account();
        account1.setId("uid=testaccount1,ou=accounts,o=test,ou=system");
        account1.setName("testaccount1");

        Account account2 = new Account();
        account2.setId("uid=testaccount2,ou=accounts,o=test,ou=system");
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
