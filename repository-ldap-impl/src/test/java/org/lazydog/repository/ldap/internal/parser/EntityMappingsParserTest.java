package org.lazydog.repository.ldap.internal.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.lazydog.repository.ldap.internal.FetchType;
import org.lazydog.repository.ldap.internal.SearchScope;


/**
 * Entity mappings parser test.
 *
 * @author  Ron Rickard
 */
public class EntityMappingsParserTest {

    private static final String PATHNAME = "org/lazydog/test/em/group-em.xml";

    @Test
    public void testGetAttributeReferentialIntegrity() throws Exception {
    	Map<String,String> expected = new HashMap<String,String>();
    	expected.put("uniqueMember", "description");
    	EntityMappingsParser parser = EntityMappingsParser.newInstance(PATHNAME);
    	Map<String,String> actual = parser.getAttributeReferentialIntegrityMap();
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetClassName() throws Exception {
    	String expected = "org.lazydog.test.model.Group";
    	EntityMappingsParser parser = EntityMappingsParser.newInstance(PATHNAME);
    	String actual = parser.getClassName();
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetObjectClassValues() throws Exception {
    	Set<String> expected = new HashSet<String>();
    	expected.add("groupOfUniqueNames");
    	expected.add("top");
    	EntityMappingsParser parser = EntityMappingsParser.newInstance(PATHNAME);
    	Set<String> actual = parser.getObjectClassValues();
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPropertyAttributeMap() throws Exception {
    	Map<String,String> expected = new HashMap<String,String>();
    	expected.put("accounts", "uniqueMember");
    	expected.put("description", "description");
    	expected.put("name", "cn");
    	EntityMappingsParser parser = EntityMappingsParser.newInstance(PATHNAME);
    	Map<String,String> actual = parser.getPropertyAttributeMap();
    	assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyFetchTypeMap() throws Exception {
    	Map<String,FetchType> expected = new HashMap<String,FetchType>();
    	expected.put("accounts", FetchType.EAGER);
    	EntityMappingsParser parser = EntityMappingsParser.newInstance(PATHNAME);
    	Map<String,FetchType> actual = parser.getPropertyFetchTypeMap();
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetPropertyTargetEntityClassNameMap() throws Exception {
    	Map<String,String> expected = new HashMap<String,String>();
    	expected.put("accounts", "org.lazydog.test.model.Account");
    	EntityMappingsParser parser = EntityMappingsParser.newInstance(PATHNAME);
    	Map<String,String> actual = parser.getPropertyTargetEntityClassNameMap();
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSearchBase() throws Exception {
    	String expected = "o=test,ou=system";
    	EntityMappingsParser parser = EntityMappingsParser.newInstance(PATHNAME);
    	String actual = parser.getSearchBase();
    	assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSearchScope() throws Exception {
    	SearchScope expected = SearchScope.SUBTREE;
    	EntityMappingsParser parser = EntityMappingsParser.newInstance(PATHNAME);
    	SearchScope actual = parser.getSearchScope();
    	assertEquals(expected, actual);
    }

    @Test
    public void testNewInstance() throws Exception {
    	EntityMappingsParser.newInstance(PATHNAME);
    }

    @Test(expected=ParsingException.class)
    public void testNewInstanceNullPathname() throws Exception {
    	EntityMappingsParser.newInstance(null);
    }

    @Test(expected=ParsingException.class)
    public void testNewInstanceEmptyPathname() throws Exception {
    	EntityMappingsParser.newInstance("");
    }
}
