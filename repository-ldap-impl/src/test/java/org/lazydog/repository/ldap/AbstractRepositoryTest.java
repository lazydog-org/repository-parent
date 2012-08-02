package org.lazydog.repository.ldap;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.apache.directory.server.core.integ.annotations.ApplyLdifs;
import org.apache.directory.server.core.integ.annotations.CleanupLevel;
import org.apache.directory.server.integ.SiRunner;
import org.apache.directory.server.ldap.LdapServer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Comparison;
import org.lazydog.repository.criterion.Logical;
import org.lazydog.test.GroupRepository;
import org.lazydog.test.model.Account;
import org.lazydog.test.model.Group;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;


/**
 * Abstract repository test.
 * 
 * @author  Ron Rickard
 */
@RunWith(SiRunner.class)
@CleanupLevel(org.apache.directory.server.core.integ.Level.CLASS)
@ApplyLdifs( {
    "dn: o=test,ou=system\n" +
    "changeType: add\n" +
    "o: test\n" +
    "objectClass: top\n" + 
    "objectClass: organization\n\n",

    "dn: ou=groups,o=test,ou=system\n" +
    "changeType: add\n" +
    "ou: groups\n" +
    "objectClass: organizationalunit\n" +
    "objectClass: top\n\n",

    "dn: ou=accounts,o=test,ou=system\n" +
    "changeType: add\n" +
    "ou: accounts\n" +
    "objectClass: organizationalunit\n" + 
    "objectClass: top\n\n"
} ) 
public class AbstractRepositoryTest {

    public static LdapServer ldapServer;
    private static GroupRepository repository;
    private static Account account1;
    private static Account account2;
    private static List<Account> accounts;
    private static Group group1;
    private static Group group2;
    private static List<Group> groups;
    private static Account lazyAccount1;
    private static Account lazyAccount2;
    private static List<Account> lazyAccounts;
    private static Group lazyGroup1;
    private static Group lazyGroup2;
    private static List<Group> lazyGroups;

    @BeforeClass
    public static void initialize() throws Exception {

    	Logger logger = (Logger)LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.ERROR);

        account1 = new Account();
        account1.setId("uid=testaccount1,ou=accounts,o=test,ou=system");
        account1.setName("testaccount1");

        account2 = new Account();
        account2.setId("uid=testaccount2,ou=accounts,o=test,ou=system");
        account2.setName("testaccount2");

        accounts = new ArrayList<Account>();
        accounts.add(account1);
        accounts.add(account2);

        group1 = new Group();
        group1.setAccounts(new HashSet<Account>(accounts));
        group1.setDescription("Test Group1");
        group1.setId("cn=testgroup1,ou=groups,o=test,ou=system");
        group1.setName("testgroup1");
        
        group2 = new Group();
        group2.setAccounts(new HashSet<Account>(accounts));
        group2.setDescription("Test Group2");
        group2.setId("cn=testgroup2,ou=groups,o=test,ou=system");
        group2.setName("testgroup2");
        
        groups = new ArrayList<Group>();
        groups.add(group1);
        groups.add(group2);
        
        lazyAccount1 = new Account();
        lazyAccount1.setId("uid=testaccount1,ou=accounts,o=test,ou=system");
        
        lazyAccount2 = new Account();
        lazyAccount2.setId("uid=testaccount2,ou=accounts,o=test,ou=system");
        
        lazyAccounts = new ArrayList<Account>();
        lazyAccounts.add(lazyAccount1);
        lazyAccounts.add(lazyAccount2);
        
        lazyGroup1 = new Group();
        lazyGroup1.setAccounts(new HashSet<Account>(lazyAccounts));
        lazyGroup1.setDescription("Test Group1");
        lazyGroup1.setId("cn=testgroup1,ou=groups,o=test,ou=system");
        lazyGroup1.setName("testgroup1");
        
        lazyGroup2 = new Group();
        lazyGroup2.setAccounts(new HashSet<Account>(lazyAccounts));
        lazyGroup2.setDescription("Test Group2");
        lazyGroup2.setId("cn=testgroup2,ou=groups,o=test,ou=system");
        lazyGroup2.setName("testgroup2");
        
        lazyGroups = new ArrayList<Group>();
        lazyGroups.add(lazyGroup1);
        lazyGroups.add(lazyGroup2);
    }

    @Before
    public void beforeTest() throws Exception {  
    	repository = new GroupRepository();
        repository.persist(account1);
    	repository.persist(account2);
    	repository.persist(group1);
    }
    
    @After
    public void afterTest() throws Exception {
    	Group fetchedGroup1 = repository.find(Group.class, group1.getId());
    	if (fetchedGroup1 != null) {
            repository.remove(Group.class, fetchedGroup1.getId());
    	}
    	Group fetchedGroup2 = repository.find(Group.class, group1.getId());
    	if (fetchedGroup2 != null) {
            repository.remove(Group.class, fetchedGroup2.getId());
    	}
    	Account fetchedAccount1 = repository.find(Account.class, account1.getId());
    	if (fetchedAccount1 != null) {
            repository.remove(Account.class, fetchedAccount1.getId());
    	}
    	Account fetchedAccount2 = repository.find(Account.class, account2.getId());
    	if (fetchedAccount2 != null) {
            repository.remove(Account.class, fetchedAccount2.getId());
    	}
    }

    @Test
    public void testFind() {
    	Group fetchedGroup = repository.find(Group.class, group1.getId());
    	assertEquals(group1, fetchedGroup);
    }

    @Test
    public void testFindByCriteria() {
    	Criteria<Group> criteria = repository.getCriteria(Group.class);
    	criteria.add(Comparison.eq("name", "testgroup1"));
    	Group fetchedGroup = repository.find(Group.class, criteria);
    	assertEquals(group1, fetchedGroup);
    }

    @Test
    public void testFindList() {
    	Group persistedGroup = repository.persist(group2);
    	assertEquals(group2, persistedGroup);
    	List<Group> fetchedGroups = repository.findList(Group.class);
    	Collections.sort(groups);
    	Collections.sort(fetchedGroups);
    	assertEquals(groups, fetchedGroups);
    }

    @Test
    public void testFindListByCriteria() {
    	Criteria<Group> criteria = repository.getCriteria(Group.class);
    	criteria.add(Comparison.eq("name", "testgroup*"));
    	criteria.add(Logical.and(Comparison.eq("accounts", "uid=testaccount1,ou=accounts,o=test,ou=system")));
    	Group persistedGroup = repository.persist(group2);
    	assertEquals(group2, persistedGroup);
    	List<Group> fetchedGroups = repository.findList(Group.class, criteria);
    	Collections.sort(groups);
    	Collections.sort(fetchedGroups);
    	assertEquals(groups, fetchedGroups);
    }

    @Test
    public void testPersist() {
    	Group persistedGroup = repository.persist(group2);
    	assertEquals(group2, persistedGroup);
    	Group fetchedGroup = repository.find(Group.class, group2.getId());
    	assertEquals(group2, fetchedGroup);
    	
    }

    @Test
    public void testRemove() {
    	repository.remove(Group.class, group1.getId());
    	Group fetchedGroup = repository.find(Group.class, group1.getId());
    	assertEquals(null, fetchedGroup);
    	Account fetchedAccount1 = repository.find(Account.class, account1.getId());
    	assertEquals(account1, fetchedAccount1);
    	Account fetchedAccount2 = repository.find(Account.class, account2.getId());
    	assertEquals(account2, fetchedAccount2);
    }

    @Test
    public void testRemoveList() {
    	repository.persist(group2);
    	List<String> ids = new ArrayList<String>();
    	ids.add(group1.getId());
    	ids.add(group2.getId());
    	repository.removeList(Group.class, ids);
    	Group fetchedGroup1 = repository.find(Group.class, group1.getId());
    	assertEquals(null, fetchedGroup1);
    	Group fetchedGroup2 = repository.find(Group.class, group2.getId());
    	assertEquals(null, fetchedGroup2);
    	Account fetchedAccount1 = repository.find(Account.class, account1.getId());
    	assertEquals(account1, fetchedAccount1);
    	Account fetchedAccount2 = repository.find(Account.class, account2.getId());
    	assertEquals(account2, fetchedAccount2);
    }
}