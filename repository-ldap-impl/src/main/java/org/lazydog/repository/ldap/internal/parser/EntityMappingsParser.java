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
package org.lazydog.repository.ldap.internal.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.lazydog.repository.ldap.internal.FetchType;
import org.lazydog.repository.ldap.internal.SearchScope;

/**
 * Entity mappings XML configuration file parser.
 * 
 * @author  Ron Rickard
 */
public final class EntityMappingsParser extends XMLConfigurationFileParser {

    private static final String SCHEMA_PATHNAME = "META-INF/xsd/entity-mappings.xsd";

    private static enum ELEMENT_NAME {
    	ATTRIBUTE,
    	ENTITY,
    	ENTITY_MAPPINGS,
        MAPPING,
        OBJECT_CLASS,
        PROPERTY,
        SEARCH_BASE,
        SEARCH_SCOPE,
        VALUE;
    };
    private static enum ATTRIBUTE_NAME {
    	CLASS_NAME,
    	FETCH_TYPE,
    	NAME,
    	REFERENTIAL_INTEGRITY,
    	TARGET_ENTITY,
    	VALUE;
    };
    private Map<String,String> attributeReferentialIntegrityMap;
    private String className;
    private Set<String> objectClassValues;
    private Map<String,String> propertyAttributeMap;
    private Map<String,FetchType> propertyFetchTypeMap;
    private Map<String,String> propertyTargetEntityClassNameMap;
    private String searchBase;
    private SearchScope searchScope;
    
    /**
     * Private constructor.
     * 
     * @param  configurationPathname  the pathname for the XML configuration file.
     * @param  schemaPathname		  the pathname for the XSD schema file.
     * 
     * @throws  ParsingException  if unable to validate and parse the XML configuration file.
     */
    private EntityMappingsParser(final String configurationPathname, final String schemaPathname) throws ParsingException {
    	super(configurationPathname, schemaPathname);
    }
    
    /**
     * Get the attribute referential integrity map.
     * 
     * @return  the attribute referential integrity map.
     */
    public Map<String,String> getAttributeReferentialIntegrityMap() {
    	return this.attributeReferentialIntegrityMap;
    }

    /**
     * Get the class name.
     * 
     * @return  the class name.
     */
    public String getClassName() {
    	return this.className;
    }

    /**
     * Get the object class values.
     * 
     * @return  the object class values.
     */
    public Set<String> getObjectClassValues() {
    	return this.objectClassValues;
    }
    
    /**
     * Get the property attribute map.
     * 
     * @return  the property attribute map.
     */
    public Map<String,String> getPropertyAttributeMap() {
    	return this.propertyAttributeMap;
    }

    /**
     * Get the property target entity class name map.
     * 
     * @return  the property target entity class name map.
     */
    public Map<String,String> getPropertyTargetEntityClassNameMap() {
    	return this.propertyTargetEntityClassNameMap;
    }
    
    /**
     * Get the property fetch type map.
     * 
     * @return  the property fetch type map.
     */
    public Map<String,FetchType> getPropertyFetchTypeMap() {
    	return this.propertyFetchTypeMap;
    }
    
    /**
     * Get the search base.
     * 
     * @return  the search base.
     */
    public String getSearchBase() {
    	return this.searchBase;
    }
    
    /**
     * Get the search scope.
     * 
     * @return  the search scope.
     */
    public SearchScope getSearchScope() {
        return this.searchScope;
    }
    
    /**
     * Create a new instance of this class.
     *
     * @param  configurationPathname  the pathname for the XML configuration file.
     * 
     * @return  a new instance of this class.
     * 
     * @throws  ParsingException  if unable to validate and parse the pathname.
     */
    public static EntityMappingsParser newInstance(final String configurationPathname) throws ParsingException {
        return new EntityMappingsParser(configurationPathname, SCHEMA_PATHNAME);
    }

    /**
     * Parse the XML configuration file.
     *
     * @param  inputStream  the input stream for the XML configuration file.
     *
     * @throws  XMLStreamException  if unable to parse the input stream.
     */
    @Override
    protected void parse(final InputStream inputStream) throws XMLStreamException {

        // Declare.
        XMLEventReader reader = null;
        
        // Initialize.
        this.attributeReferentialIntegrityMap = new HashMap<String,String>();
        this.propertyAttributeMap = new HashMap<String,String>();
        this.propertyFetchTypeMap = new HashMap<String,FetchType>();
        this.propertyTargetEntityClassNameMap = new HashMap<String,String>();
        this.objectClassValues = new HashSet<String>();

        try {

            // Get the XML configuration file reader.
            XMLInputFactory factory = XMLInputFactory.newInstance();
            reader = factory.createXMLEventReader(inputStream);
            
            // Initialize the mapping properties.
            String attributeName = null;
            String objectClassValue = null;
            String propertyName = null;
            String referentialIntegrity = null;
            String targetEntityClassName = null;
            FetchType fetchType = null;

            // Loop through the XML events.
            while (reader.hasNext()) {

                // Get the next event.
                XMLEvent event = reader.nextEvent();

                // Check if the event is a start element.
                if (event.isStartElement()) {

                    switch(getElementName(ELEMENT_NAME.class, event.asStartElement())) {

                        case ATTRIBUTE:

                            // Get the LDAP attribute name.
                            attributeName = getAttributeData(event, ATTRIBUTE_NAME.NAME);
                            referentialIntegrity = getAttributeData(event, ATTRIBUTE_NAME.REFERENTIAL_INTEGRITY);
                            break;

                        case ENTITY:

                            // Get the class name.
                            this.className = getAttributeData(event, ATTRIBUTE_NAME.CLASS_NAME);
                            break;

                        case MAPPING:

                            // Clear the mapping properties.
                            attributeName = null;
                            propertyName = null;
                            referentialIntegrity = null;
                            targetEntityClassName = null;
                            fetchType = null;
                            break;
                            
                        case PROPERTY:
	                    	
                            // Get the class property name, the property's target entity class name,
                            // and the fetch type.
                            propertyName = getAttributeData(event, ATTRIBUTE_NAME.NAME);                
                            targetEntityClassName = getAttributeData(event, ATTRIBUTE_NAME.TARGET_ENTITY); 
                            if (targetEntityClassName != null) {
                                fetchType = (getAttributeData(event, ATTRIBUTE_NAME.FETCH_TYPE) != null) ?
                                        FetchType.getFetchType(getAttributeData(event, ATTRIBUTE_NAME.FETCH_TYPE)) :
                                        FetchType.LAZY;
                            }                  	
                            break;
	                    
                        case SEARCH_BASE:
                        	
                            // Get the search base.
                            this.searchBase = getAttributeData(event, ATTRIBUTE_NAME.VALUE);
                            break;
                        
                        case SEARCH_SCOPE:
                            
                            // Get the search scope.
                            this.searchScope = SearchScope.getSearchScope(getAttributeData(event, ATTRIBUTE_NAME.VALUE));
                            break;
                            
                        case VALUE:
                        	
                            // Get the object class value.
                            objectClassValue = getElementData(reader.nextEvent());
                            break;
                    }

                // Check if the event is an end element.
                } else if (event.isEndElement()) {

                    switch(getElementName(ELEMENT_NAME.class, event.asEndElement())) {

                        case MAPPING:
                        	
                            // Add the entries to the maps.
                            this.propertyAttributeMap.put(propertyName, attributeName);
                            if (targetEntityClassName != null) {
                                    this.propertyTargetEntityClassNameMap.put(propertyName, targetEntityClassName);
                                    this.propertyFetchTypeMap.put(propertyName, fetchType);
                            }
                            if (referentialIntegrity != null) {
                                    this.attributeReferentialIntegrityMap.put(attributeName, referentialIntegrity);
                            }
                            break;
                        	
                        case VALUE:
                        	
                            // Add the object class value.
                            this.objectClassValues.add(objectClassValue);
                            break;
                    }
                }
            }
        }
        finally {

            // Check if the reader exists.
            if (reader != null) {

                try {
                    
                    // Close the reader.
                    reader.close();
                } catch (XMLStreamException e) {
                    // Ignore.
                }
            }
        }
    }
}
