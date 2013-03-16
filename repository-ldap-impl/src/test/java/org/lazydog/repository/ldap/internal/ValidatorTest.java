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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.lazydog.repository.ldap.internal.Validator.validCriteria;
import static org.lazydog.repository.ldap.internal.Validator.validEntity;
import static org.lazydog.repository.ldap.internal.Validator.validEntityClass;
import static org.lazydog.repository.ldap.internal.Validator.validId;
import static org.lazydog.repository.ldap.internal.Validator.validList;
import org.lazydog.test.model.Account;
import org.lazydog.test.model.Group;

/**
 * Validator test.
 *
 * @author  Ron Rickard
 */

public class ValidatorTest {

    @Test
    public void testValidCriteria() throws Exception {
        Set<String> objectClassValues = new HashSet<String>();
        objectClassValues.add("groupOfNames");
        objectClassValues.add("top");
        Map<String,String> propertyAttributeMap = new HashMap<String,String>();
        propertyAttributeMap.put("accounts", "member");
        propertyAttributeMap.put("description", "description");
        propertyAttributeMap.put("name", "cn");
        String searchBase = "ou=system";
        SearchScope searchScope = SearchScope.SUBTREE;
        validCriteria(new CriteriaImpl<Group>(objectClassValues, propertyAttributeMap, searchBase, searchScope));
    }
	
    @Test
    public void testValidEntity() throws Exception {
        Account account = new Account();
        account.setId("uid=testaccount1,ou=accounts,o=test,ou=system");
        validEntity(account);
    }
    
    @Test
    public void testValidEntitySupported() throws Exception {
    	Account account = new Account();
        account.setId("uid=testaccount1,ou=accounts,o=test,ou=system");
    	Set<Class<?>> entityClasses = new HashSet<Class<?>>();
    	entityClasses.add(Account.class);
    	entityClasses.add(Group.class);
    	validEntity(account, entityClasses);
    }
    
    @Test
    public void testValidEntityClass() throws Exception {
    	validEntityClass(Account.class);
    }
    
    @Test
    public void testValidEntityClassSupported() throws Exception {
    	Set<Class<?>> entityClasses = new HashSet<Class<?>>();
    	entityClasses.add(Account.class);
    	entityClasses.add(Group.class);
    	validEntityClass(Account.class, entityClasses);
    }
    
    @Test
    public void testValidId() throws Exception {
    	validId("uid=testaccount1,ou=accounts,o=test,ou=system");
    }
    
    @Test
    public void testValidList() throws Exception {
    	List<String> ids = new ArrayList<String>();
    	ids.add("uid=testaccount1,ou=accounts,o=test,ou=system");
    	ids.add("uid=testaccount1,ou=accounts,o=test,ou=system");
    	validList(ids);
    }
}
