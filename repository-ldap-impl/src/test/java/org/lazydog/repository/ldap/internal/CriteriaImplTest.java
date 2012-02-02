package org.lazydog.repository.ldap.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lazydog.repository.criterion.ComparisonOperation;
import org.lazydog.repository.criterion.Criterion;
import org.lazydog.repository.criterion.LogicalOperation;
import org.lazydog.repository.criterion.Order;
import org.lazydog.test.model.Group;

import static org.junit.Assert.*;


/**
 * Criteria implementation test.
 *
 * @author  Ron Rickard
 */
public class CriteriaImplTest {

    private static CriteriaImpl<Group> criteria;
    private static Set<String> objectClassValues;
    private static Map<String,String> propertyAttributeMap;
    private static String searchBase;
    private static SearchScope searchScope;

    @BeforeClass
    public static void initialize() throws Exception {
        objectClassValues = new HashSet<String>();
        objectClassValues.add("top");
        objectClassValues.add("groupOfUniqueNames");

        propertyAttributeMap = new HashMap<String,String>();
        propertyAttributeMap.put("accounts", "uniqueMember");
    	propertyAttributeMap.put("description", "description");
    	propertyAttributeMap.put("name", "cn");
    	
    	searchBase = "o=test,ou=system";
        searchScope = SearchScope.SUBTREE;
    }
	
    @Before
    public void beforeTest() throws Exception {
        criteria = new CriteriaImpl<Group>(objectClassValues, propertyAttributeMap, searchBase, searchScope);
    }
	
    @Test
    public void testAddComparisonEquals() throws Exception {
        criteria.add(ComparisonOperation.eq("name", "testgroup*"));
        String fetchedFilter = criteria.getFilter();
        assertEquals("(&(&(objectclass=groupOfUniqueNames)(objectclass=top))(cn=testgroup*))", fetchedFilter);
    }
	
    @Test
    public void testAddLogicalAnd() throws Exception {
        criteria.add(ComparisonOperation.eq("name", "testgroup*"));
        criteria.add(LogicalOperation.and(ComparisonOperation.eq("accounts", "uid=testaccount1,ou=accounts,o=test,ou=system")));
        String fetchedFilter = criteria.getFilter();
        assertEquals("(&(&(&(objectclass=groupOfUniqueNames)(objectclass=top))(cn=testgroup*))(uniqueMember=uid=testaccount1,ou=accounts,o=test,ou=system))", fetchedFilter);
    }
	
    @Test(expected=UnsupportedOperationException.class)
    public void testAddOrder() throws Exception {
        criteria.addOrder(Order.asc("name"));
    }
	
    @Test(expected=UnsupportedOperationException.class)
    public void testAddOrders() throws Exception {
        List<Criterion> orders = new ArrayList<Criterion>();
        orders.add(Order.asc("name"));
        orders.add(Order.asc("accounts"));
        criteria.addOrders(orders);
    }
	
    @Test
    public void testGetFilter() throws Exception {
    	String fetchedFilter = criteria.getFilter();
    	assertEquals("(&(objectclass=groupOfUniqueNames)(objectclass=top))", fetchedFilter);
    }
	
    @Test
    public void testGetSearchBase() throws Exception {
    	String fetchedSearchBase = criteria.getSearchBase();
    	assertEquals("o=test,ou=system", fetchedSearchBase);
    }
	
    @Test
    public void testGetSearchScope() throws Exception {
    	SearchScope fetchedSearchScope = criteria.getSearchScope();
    	assertEquals(SearchScope.SUBTREE, fetchedSearchScope);
    }
}
