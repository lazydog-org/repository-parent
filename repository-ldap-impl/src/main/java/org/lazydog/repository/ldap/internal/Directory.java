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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.AttributeModificationException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Directory.
 * 
 * @author  Ron Rickard
 */
public final class Directory {

    public static final String INITIAL_CONTEXT_FACTORY = "initialContextFactory";
    public static final String PROVIDER_URL = "providerUrl";
    public static final String SECURITY_AUTHENTICATION = "securityAuthentication";
    public static final String SECURITY_CREDENTIALS = "securityCredentials";
    public static final String SECURITY_PRINCIPAL = "securityPrincipal";
    private enum AttributeAction {
            ADD,
            IGNORE,
            REMOVE,
            REPLACE;
    }
    private DirContext directoryContext;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	
    /**
     * Private constructor.
     * 
     * @param  environment  the directory environment.
     * 
     * @throws  DirectoryException  if unable to get the directory context.
     */
    private Directory(final Properties environment) throws DirectoryException {

        // Check if the directory environment is not valid.
        if (environment == null || 
            environment.getProperty(INITIAL_CONTEXT_FACTORY) == null ||
            environment.getProperty(PROVIDER_URL) == null ||
            environment.getProperty(SECURITY_AUTHENTICATION) == null ||
            environment.getProperty(SECURITY_CREDENTIALS) == null ||
            environment.getProperty(SECURITY_PRINCIPAL) == null) {
            throw new DirectoryException(
                    "Unable to get the directory context due to a missing environment setting.", null);
        }
		    
        try {

            // Get the directory context.
            this.directoryContext = getDirectoryContext(
                    environment.getProperty(INITIAL_CONTEXT_FACTORY),
                    environment.getProperty(PROVIDER_URL),
                    environment.getProperty(SECURITY_AUTHENTICATION),
                    environment.getProperty(SECURITY_CREDENTIALS),
                    environment.getProperty(SECURITY_PRINCIPAL));
        } catch (NamingException e) {
            throw new DirectoryException(
                    "Unable to get the directory context.", e, null);
        }
    }
	
    /**
     * Add the entry to the directory specified by the DN.
     * 
     * @param  dn            the distinguished name.
     * @param  attributeMap  the attribute name-attribute value map.
     * 
     * @throws  DirectoryException  if unable to add the entry.
     */
    public void addEntry(final String dn, final Map<String,Set<String>> attributeMap) throws DirectoryException {
        this.addEntry(dn, attributeMap, new HashMap<String,String>());
    }
	
    /**
     * Add the entry to the directory specified by the DN and enforce referential integrity.
     * 
     * @param  dn            		         the distinguished name.
     * @param  attributeMap  			 the attribute name-attribute value map.
     * @param  attributeReferentialIntegrityMap  the referential integrity attribute map.
     * 
     * @throws DirectoryException  if unable to add the entry.
     */
    public void addEntry(final String dn, final Map<String,Set<String>> attributeMap, final Map<String,String> attributeReferentialIntegrityMap) throws DirectoryException {
		
        Date startTime = new Date();
        logger.info("Adding the new LDAP entry '" + dn + "'.");

        try {

            // Get the attributes.
            Attributes attributes = getAttributes(attributeMap);

            // Add the entry.
            this.directoryContext.createSubcontext(dn, attributes);

            // Enforce referential integrity.
            this.enforceReferentialIntegrity(dn, attributeMap, attributeReferentialIntegrityMap);
        } catch (NameAlreadyBoundException e) {
            throw new DirectoryException(
                    "Unable to add the LDAP entry '" + dn + "' since it already exists.", e, dn);

        } catch (NamingException e) {
            throw new DirectoryException(
                    "Unable to add the LDAP entry '" + dn + "'.", e, dn);
        }

        logger.trace("Added new LDAP entry in " + duration(startTime, new Date()) + "s.");
    }

    /**
     * Convert from the Set to an Array.
     * 
     * @param  set  the set.
     * 
     * @return  an Array.
     */
    private static String[] convertSetToArray(final Set<String> set) {
        return set.toArray(new String[0]);
    }

    /**
     * Get the duration in seconds.
     * 
     * @param  startTime  the start time.
     * @param  endTime    the end time.
     * 
     * @return  the duration in seconds.
     */
    private static double duration(final Date startTime, final Date endTime) {
        return ((double)endTime.getTime() - (double)startTime.getTime()) / 1000.0;
    }

    /**
     * Enforce referential integrity.
     * 
     * @param  dn            			 the distinguished name.
     * @param  attributeMap  			 the attribute name-attribute value map.
     * @param  attributeReferentialIntegrityMap  the referential integrity attribute map.
     * 
     * @throws DirectoryException  if unable to enforce referential integrity.
     */
    private void enforceReferentialIntegrity(final String dn, final Map<String,Set<String>> attributeMap, final Map<String,String> attributeReferentialIntegrityMap) throws DirectoryException {
		
        Date startTime = new Date();
        logger.info("Enforcing referencial integrity for entry '" + dn + "'.");
		
        // Get the attribute names.
    	Set<String> attributeNames = attributeMap.keySet();
    	
    	// Loop through the attribute names.
    	for (String attributeName : attributeNames) {
    		
            // Check if the attribute requires referential integrity.
            if (isReferentialIntegrityNeeded(attributeName, attributeReferentialIntegrityMap)) {
    			
                // Get the corresponding attribute name.
                String correspondingAttributeName = getCorrespondingAttributeName(attributeName, attributeReferentialIntegrityMap);

                // Get the attribute values.
                Set<String> attributeValues = attributeMap.get(attributeName);

                // Loop through the attribute values.
                for (String attributeValue : attributeValues) {
    				
                    // Get the corresponding attribute name-attribute value map for the attribute value.
                    Set<String> correspondingAttributeNames = new HashSet<String>();
                    correspondingAttributeNames.add(correspondingAttributeName);
                    Map<String,Set<String>> correspondingAttributeMap = this.getAttributeMap(attributeValue, correspondingAttributeNames);

                    // Check if referential integrity is not in place.
                    if (correspondingAttributeMap.isEmpty()) {
						
                        // Get the corresponding attribute value.
                        Set<String> correspondingAttributeValues = new HashSet<String>();
                        correspondingAttributeValues.add(dn);
                        correspondingAttributeMap.put(correspondingAttributeName, correspondingAttributeValues);

                        // Update the corresponding entry.
                        this.updateEntry(attributeValue, correspondingAttributeMap);

                    // Check if the DN is not a corresponding attribute value.
                    } else if (!correspondingAttributeMap.get(correspondingAttributeName).contains(dn)) {

                        // Add this DN as a corresponding attribute value.
                        correspondingAttributeMap.get(correspondingAttributeName).add(dn);

                        // Update the corresponding entry.
                        this.updateEntry(attributeValue, correspondingAttributeMap);
                    }
                }
            }
    	}
    	
    	logger.trace("Referential integrity enforced in " + duration(startTime, new Date()) + "s.");
    }
	
    /**
     * Does the entry exist?
     * 
     * @param  dn  the distinguished name.
     * 
     * @return  true if the entry exists, otherwise false.
     * 
     * @throws  DirectoryException  if unable to determine if the entry exists.
     */
    public boolean entryExists(final String dn) throws DirectoryException {
        return (this.getAttributeMap(dn, new HashSet<String>() {
            private static final long serialVersionUID = 1L;
            {
                add("objectClass");
            }
        }).size() > 0) ? true : false;
    }
	
    /**
     * Evaluate if the attribute should be added, replaced, removed, or ignored.
     * 
     * @param  attribute           the attribute.
     * @param  existingAttributes  the existing attributes.
     * 
     * @return  evaluation is either add, update, remove or ignore.
     * 
     * @throws  NamingException  if unable to evaluate the attribute.
     */
    private AttributeAction evaluateAttribute(final Attribute attribute, final Attributes existingAttributes) throws NamingException {

    	// Initialize the evaluation to ignore.
    	AttributeAction evaluation = AttributeAction.IGNORE;
    	
        try {

            // Get the existing attribute for the attribute ID.
            Attribute existingAttribute = existingAttributes.get(attribute.getID());

            // Evaluate to add if the attribute is not an existing attribute.
            if (existingAttribute == null) {
                evaluation = AttributeAction.ADD;

            // Evaluate to remove if the attribute does not have values, but the existing attribute has values.
            } else if (existingAttribute.size() > 0 && attribute.size() == 0) {
                evaluation = AttributeAction.REMOVE;

            // Evaluate to replace if the attribute and existing attribute have values, but the values are not the same. 
            } else if ((existingAttribute.size() > 0 && attribute.size() > 0) && hasAttributeValueMismatch(attribute, existingAttribute)) {
                evaluation = AttributeAction.REPLACE;
            }
        } catch (NoSuchElementException e) {

            // Evaluate to add if the attribute does not exist.
            evaluation = AttributeAction.ADD;
        }

        return evaluation;
    }
	
    /**
     * Get the attribute.
     * 
     * @param  attributeName    the attribute name.
     * @param  attributeValues  the attribute values.
     * 
     * @return  the attribute.
     */
    private static Attribute getAttribute(final String attributeName, final Set<String> attributeValues) {

    	// Initialize attribute.
        Attribute attribute = new BasicAttribute(attributeName);

        // Check if the attribute values exist.
        if (attributeValues != null) {
        	
            // Loop through the attribute values.
            for (String attributeValue : attributeValues) {

                // Add the attribute value to the attribute.
                attribute.add(attributeValue);
            }
        }
        
        return attribute;
    }

    /**
     * Get the attributes.
     * 
     * @param  attributeMap  the attribute name-attribute value map.
     * 
     * @return  the attributes.
     */
    private static Attributes getAttributes(final Map<String,Set<String>> attributeMap) {

        // Initialize attributes.
        Attributes attributes = new BasicAttributes();

        // Get the attribute names.
        Set<String> attributeNames = attributeMap.keySet();

        // Loop through the attribute names.
        for (String attributeName : attributeNames) {

            // Get the attribute values.
            Set<String> attributeValues = attributeMap.get(attributeName);

            // Add the attribute name-attribute value to the attributes.
            attributes.put(getAttribute(attributeName, attributeValues));
        }

        return attributes;
    }
	
    /**
     * Get the attribute name-attribute value map.
     * 
     * @param  attributes  the attributes.
     * 
     * @return  the attribute name-attribute value map.
     * 
     * @throws  NamingException  if unable to get the attribute name-attribute value map.
     */
    private Map<String,Set<String>> getAttributeMap(final Attributes attributes) throws NamingException {

        // Initialize.
        Map<String, Set<String>> attributeMap = new HashMap<String, Set<String>>();

        // Get the attribute names.
        NamingEnumeration<String> attributeNames = attributes.getIDs();

        // Loop through the attribute names.
        while (attributeNames.hasMore()) {

            String attributeName = attributeNames.next();

            // Get the attribute.
            Attribute attribute = attributes.get(attributeName);

            // Get the attribute values.
            Set<String> attributeValues = getAttributeValues(attribute);

            // Put the attribute name-attribute value in the map.
            attributeMap.put(attributeName, attributeValues);

            logger.debug("Retrieved attribute '" + attributeName + "' value '" + attributeValues + "'.");
        }

        return attributeMap;
    }
    
    /**
     * Get the attribute name-attribute value map for the DN from the directory.
     * 
     * @param  dn              the distinguished name.
     * @param  attributeNames  the attribute names.
     * 
     * @return  the attribute name-attribute value map.
     * 
     * @throws  DirectoryException  if unable to get the attribute name-attribute value map.
     */
    public Map<String,Set<String>> getAttributeMap(final String dn, final Set<String> attributeNames) throws DirectoryException {
		
        Date startTime = new Date();
        logger.info("Getting values of attributes '" + attributeNames + "' for LDAP entry '" + dn + "'.");
		
        // Initialize the attribute name-attribute value map.
        Map<String,Set<String>> attributeMap = new HashMap<String,Set<String>>();

        try {

            // Get the attributes for the DN.
            Attributes attributes = this.directoryContext.getAttributes(dn, convertSetToArray(attributeNames));

            // Get the attribute name-attribute value map.
            attributeMap = this.getAttributeMap(attributes);
        } catch (NameNotFoundException e) {
            // Ignore.
        } catch (NamingException e) {
            throw new DirectoryException(
                    "Unable to get the attributes '" + attributeNames + "' for LDAP entry '" + dn + "'.", e, dn);
        }

        logger.trace("Attribute values retrieved in " + duration(startTime, new Date()) + "s.");

        return attributeMap;
    }

    /**
     * Get the attribute name-attribute value maps from the directory.
     * 
     * @param  filter          the filter.
     * @param  searchBase      the search base.
     * @param  searchScope     the search scope.
     * @param  attributeNames  the attribute names.
     * 
     * @return  the attribute name-attribute value maps.
     * 
     * @throws  DirectoryException  if unable to get the attribute name-attribute value maps.
     */
    public Map<String,Map<String,Set<String>>> getAttributeMaps(final String filter, final String searchBase, final SearchScope searchScope, final Set<String> attributeNames) throws DirectoryException {
    	
    	Date startTime = new Date();
    	logger.info("Getting LDAP entries for filter '" + filter + "', searchBase '" + searchBase + "', searchScope '" + searchScope + ", and attributes '" + attributeNames + "'.");
    	
    	// Initialize the attribute name-attribute value maps.
        Map<String,Map<String,Set<String>>> attributeMaps = new HashMap<String,Map<String,Set<String>>>();

    	try {
    		
            // Set the search controls to return an unlimited number of entries, to return the requested attributes,
            // to only search at the requested scope, and to take as long as needed.
            SearchControls searchControls = new SearchControls();
            searchControls.setCountLimit(0);
            searchControls.setReturningAttributes(convertSetToArray(attributeNames));
            searchControls.setSearchScope((searchScope == SearchScope.ONE) ? SearchControls.ONELEVEL_SCOPE : SearchControls.SUBTREE_SCOPE);
            searchControls.setTimeLimit(0);
	    	
            // Search for entries.
            NamingEnumeration<SearchResult> searchResults = this.directoryContext.search(searchBase, filter, searchControls);
	    	
            // Loop through the search results.
            while (searchResults.hasMore()) {
	    		
                // Get a search result.
                SearchResult searchResult = searchResults.next();
	    		
                // Get the attributes and ID value for the search result.
                Attributes attributes = searchResult.getAttributes();
                String idValue = searchResult.getNameInNamespace();

                // Get the attribute name-attribute value map.
                Map<String,Set<String>> attributeMap = this.getAttributeMap(attributes);
	    		
                // Add the map to the attribute name-attribute value maps.
                attributeMaps.put(idValue, attributeMap);
            }
    	} catch (NamingException e) {
            throw new DirectoryException(
                    "Unable to get the LDAP entries for filter '" + filter + "', searchBase '" + searchBase + 
                    "', searchScope '" + searchScope + ", and attributes '" + attributeNames + "'." , 
                    e, null);
    	}
    	
    	logger.trace(attributeMaps.size() + " LDAP entries retrieved in " + duration(startTime, new Date()) + "s.");
    	
    	return attributeMaps;
    }
    
    /**
     * Get the attribute values from the attribute.
     * 
     * @param  attribute  the attribute.
     * 
     * @return  the attribute values.
     * 
     * @throws  NamingException  if unable to get the attribute values.
     */
    private static Set<String> getAttributeValues(final Attribute attribute) throws NamingException {
		
        // Initialize.
        Set<String> attributeValues = new HashSet<String>();

        // Get the attribute value enumeration.
        NamingEnumeration<?> attributeValueEnumeration = attribute.getAll();

        // Loop through the attribute value enumeration.
        while (attributeValueEnumeration.hasMore()) {

            // Add the attribute value to the attribute values.
            attributeValues.add((String)attributeValueEnumeration.next());
        }

        return attributeValues;
    }
    
    /**
     * Get the corresponding attribute name for a specified attribute name from a attribute referential integrity map.
     * 
     * @param  attributeName                     the attribute name.
     * @param  attributeReferentialIntegrityMap  the attribute referential integrity map.
     * 
     * @return  the corresponding attribute name or null if no corresponding attribute name exists.
     */
    private static String getCorrespondingAttributeName(final String attributeName, final Map<String,String> attributeReferentialIntegrityMap) {
    	
    	// Initialize the corresponding attribute name.
    	String correspondingAttributeName = null;
    	
    	// Check if the attribute referential integrity map has values.
    	if (attributeReferentialIntegrityMap.size() > 0) {
        	
            // Get the matching attribute names.
            Set<String> matchingAttributeNames = attributeReferentialIntegrityMap.keySet();

            // Loop through the matching attribute names.
            for (String matchingAttributeName : matchingAttributeNames) {

                // Check if the matching attribute name is the attribute name.
                if (matchingAttributeName.equals(attributeName)) {

                    // Get the corresponding attribute name.
                    correspondingAttributeName = attributeReferentialIntegrityMap.get(attributeName);
                    break;
                }
            }
        }
    	
    	return correspondingAttributeName;
    }
    
    /**
     * Get the directory context.
     * 
     * @param  initialContextFactory   the initial context factory.
     * @param  providerUrl             the provider URL.
     * @param  securityAuthentication  the security authentication.
     * @param  securityCredentials     the security credentials.
     * @param  securityPrincipal       the security principal.
     * 
     * @return  the directory context.
     * 
     * @throws  NamingException  if unable to get the directory context.
     */
    private DirContext getDirectoryContext(final String initialContextFactory, final String providerUrl, final String securityAuthentication, final String securityCredentials, final String securityPrincipal) throws NamingException {

        logger.info("Directory initialized with initial context factory '" + initialContextFactory + "'.");
        logger.info("Directory initialized with provider URL '" + providerUrl + "'.");
        logger.info("Directory initialized with security authentication '" + securityAuthentication + "'.");
        logger.info("Directory initialized with security credentials 'XXX'.");
        logger.info("Directory initialized with security principal '" + securityPrincipal + "'.");
		
        // Set the context environment.
        Properties environment = new Properties();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
        environment.put(Context.PROVIDER_URL, providerUrl);
        environment.put(Context.SECURITY_AUTHENTICATION, securityAuthentication);
        environment.put(Context.SECURITY_CREDENTIALS, securityCredentials);
        environment.put(Context.SECURITY_PRINCIPAL, securityPrincipal);

        // Create and return the directory context.
        return new InitialDirContext(environment);
    }

    /**
     * Do the two attributes have different values?
     *  
     * @param  attribute1  the first attribute.
     * @param  attribute2  the second attribute.
     * 
     * @return true if the two attributes have different values, otherwise false. 
     * 
     * @throws  NamingException  if unable to determine if the two attributes have different values.
     */
    private boolean hasAttributeValueMismatch(final Attribute attribute1, final Attribute attribute2) throws NamingException {

    	// Assume the attribute values are the same.
    	boolean attributeValuesMismatch = false;
    	
    	HashSet<Object> attributeValues1 = new HashSet<Object>();
    	HashSet<Object> attributeValues2 = new HashSet<Object>();
    	
    	// Loop through the first attribute's values, adding them to a set.
        for (NamingEnumeration<?> values1 = attribute1.getAll(); values1.hasMore();) {
            attributeValues1.add(values1.next());			
        }

        // Loop through the second attribute's values, adding them to a set.
        for (NamingEnumeration<?> values2 = attribute2.getAll(); values2.hasMore();) {
            attributeValues2.add(values2.next());			
        }		

        // Check if the first attribute's values are not the same as the second attribute's values.
        if(!attributeValues1.equals(attributeValues2)) {
            attributeValuesMismatch = true;
        }

        return attributeValuesMismatch;
    }
    
    /**
     * Is referential integrity needed?
     * 
     * @param  attributeName                     the attribute name.
     * @param  attributeReferentialIntegrityMap  the attribute referential integrity map.
     * 
     * @return  true if referential integrity is needed, otherwise false.
     */
    private static boolean isReferentialIntegrityNeeded(final String attributeName, final Map<String,String> attributeReferentialIntegrityMap) {
    	return (getCorrespondingAttributeName(attributeName, attributeReferentialIntegrityMap) != null) ? true : false;
    }

    /**
     * Create a new instance of this class.
     *
     * @param  environment  the directory environment.
     * 
     * @return  a new instance of this class.
     * 
     * @throws  DirectoryException  if unable to create the directory context.
     */
    public static Directory newInstance(final Properties environment) throws DirectoryException {
        return new Directory(environment);
    }

    /**
     * Remove the entry from the directory specified by the DN.
     * 
     * @param  dn  the distinguished name.
     * 
     * @throws  DirectoryException  if unable to remove the entry.
     */
    public void removeEntry(final String dn) throws DirectoryException {
    	
    	Date startTime = new Date();
    	logger.info("Removing LDAP entry '" + dn + "'.");
    	
    	try {
			
            // Remove the entry.
            this.directoryContext.destroySubcontext(dn);
        } catch (NamingException e) {
            throw new DirectoryException(
                    "Unable to remove LDAP entry '" + dn + "'.", e, dn);
        }
		
        logger.trace("Removed new LDAP entry in " + duration(startTime, new Date()) + "s.");
    }
    
    /**
     * Update the entry specified by the DN.
     * 
     * @param  dn            the distinguished name.
     * @param  attributeMap  the attribute name-attribute value map.
     * 
     * @throws DirectoryException  if unable to update the entry.
     */
    public void updateEntry(final String dn, final Map<String,Set<String>> attributeMap) throws DirectoryException {
        this.updateEntry(dn, attributeMap, new HashMap<String,String>());
    }
	
    /**
     * Update the entry specified by the DN and enforce referential integrity.
     * 
     * @param  dn                                the distinguished name.
     * @param  attributeMap                      the attribute name-attribute value map.
     * @param  attributeReferentialIntegrityMap  the referential integrity attribute map.
     * 
     * @throws DirectoryException  if unable to update the entry.
     */
    public void updateEntry(final String dn, final Map<String,Set<String>> attributeMap, final Map<String,String> attributeReferentialIntegrityMap) throws DirectoryException {
		
        Date startTime = new Date();
        logger.info("Updating the existing LDAP entry '" + dn + "'.");

        try {
			
            // Get the attributes.
            Attributes attributes = getAttributes(attributeMap);

            // Get the existing attributes.
            Attributes existingAttributes = getAttributes(this.getAttributeMap(dn, attributeMap.keySet()));

            // Initialize the modification items.
            ArrayList<ModificationItem> modificationItems = new ArrayList<ModificationItem>();

            // Loop through the attributes.
            for (NamingEnumeration<?> attributeEnumeration = attributes.getAll(); attributeEnumeration.hasMore(); ) {
			    				
                // Get the attribute.
                Attribute attribute = (Attribute)attributeEnumeration.next();

                // Evaluate if the attribute should be added, replaced, removed, or ignored.
                AttributeAction evaluation = evaluateAttribute(attribute, existingAttributes);

                logger.debug("Evaluated attribute '" + attribute.getID() + "' as '" + evaluation + "'.");				

                switch(evaluation) {
                        case ADD:						
                                modificationItems.add(new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute));
                                break;

                        case REPLACE:						
                                modificationItems.add(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute));
                                break;		

                        case REMOVE:						
                                modificationItems.add(new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attribute));
                                break;
                }
            }

            // Update the entry.						
            this.directoryContext.modifyAttributes(dn, modificationItems.toArray(new ModificationItem[]{}));
	        
            // Enforce referential integrity.
            this.enforceReferentialIntegrity(dn, attributeMap, attributeReferentialIntegrityMap);
        } catch (AttributeModificationException e) {
            throw new DirectoryException("Unable to update the LDAP entry '" + dn + "'.", e, dn);			
        } catch (NamingException e) {
            throw new DirectoryException("Unable to update the LDAP entry '" + dn + "'.", e, dn);
        }
		
        logger.trace("Updating existing LDAP entry in " + duration(startTime, new Date()) + "s.");
    }
}
