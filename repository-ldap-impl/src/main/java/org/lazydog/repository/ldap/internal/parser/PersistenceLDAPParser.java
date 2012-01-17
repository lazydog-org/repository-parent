package org.lazydog.repository.ldap.internal.parser;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.lazydog.repository.ldap.internal.LoggingLevel;


/**
 * Persistence LDAP XML configuration file parser.
 * 
 * @author  Ron Rickard
 */
public final class PersistenceLDAPParser extends XMLConfigurationFileParser {

    private static final String CONFIGURATION_PATHNAME = "META-INF/persistence-ldap.xml";
    private static final String SCHEMA_PATHNAME = "META-INF/xsd/persistence-ldap.xsd";

    private static enum ELEMENT_NAME {
        JNDI,
        LDAP,
        LOGGING,
        MAPPING_FILE,
        PERSISTENCE_LDAP;
    };
    private static enum ATTRIBUTE_NAME {
    	INITIAL_CONTEXT_FACTORY,
    	LEVEL,
        NAME,
        PROVIDER_URL,
        SECURITY_AUTHENTICATION,
        SECURITY_CREDENTIALS,
        SECURITY_PRINCIPAL;
    };
    private String initialContextFactory;
    private String jndiName;
    private LoggingLevel loggingLevel;
    private Set<String> mappingFileNames;
    private String providerUrl;
    private String securityAuthentication;
    private String securityCredentials;
    private String securityPrincipal;

    /**
     * Private constructor.
     * 
     * @param  configurationPathname  the pathname for the XML configuration file.
     * @param  schemaPathname		  the pathname for the XSD schema file.
     * 
     * @throws  ParsingException  if unable to validate and parse the XML configuration file.
     */
    private PersistenceLDAPParser(String configurationPathname, String schemaPathname) throws ParsingException {
    	super(configurationPathname, schemaPathname);
    }

    /**
     * Get the initial context factory.
     * 
     * @return  the initial context factory.
     */
    public String getInitialContextFactory() {
    	return this.initialContextFactory;
    }
    
    /**
     * Get the JNDI name.
     * 
     * @return  the JNDI name.
     */
    public String getJndiName() {
    	return this.jndiName;
    }

    /**
     * Get the logging level.
     * 
     * @return  the logging level.
     */
    public LoggingLevel getLoggingLevel() {
    	return this.loggingLevel;
    }
    
    /**
     * Get the mapping file names.
     * 
     * @return  the mapping file names.
     */
    public Set<String> getMappingFileNames() {
    	return this.mappingFileNames;
    }

    /**
     * Get the provider URL.
     * 
     * @return  the provider URL.
     */
    public String getProviderUrl() {
    	return this.providerUrl;
    }

    /**
     * Get the security authentication.
     * 
     * @return  the security authentication.
     */
    public String getSecurityAuthentication() {
    	return this.securityAuthentication;
    }
    
    /**
     * Get the security credentials.
     * 
     * @return  the security credentials.
     */
    public String getSecurityCredentials() {
    	return this.securityCredentials;
    }

    /**
     * Get the security principal.
     * 
     * @return  the security principal.
     */
    public String getSecurityPrincipal() {
    	return this.securityPrincipal;
    }

    /**
     * Is this a JNDI setup?
     * 
     * @return  true if this is a JNDI setup, otherwise false.
     */
    public boolean isJndiSetup() {
    	return (this.jndiName != null) ? true : false;
    }
    
    /**
     * Create a new instance of this class.
     *
     * @return  a new instance of this class.
     * 
     * @throws  ParsingException  if unable to validate and parse the pathname.
     */
    public static PersistenceLDAPParser newInstance() throws ParsingException {
        return new PersistenceLDAPParser(CONFIGURATION_PATHNAME, SCHEMA_PATHNAME);
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
    public static PersistenceLDAPParser newInstance(String configurationPathname) throws ParsingException {
        return new PersistenceLDAPParser(configurationPathname, SCHEMA_PATHNAME);
    }

    /**
     * Parse the XML configuration file.
     *
     * @param  inputStream  the input stream for the XML configuration file.
     *
     * @throws  XMLStreamException  if unable to parse the input stream.
     */
    @Override
    protected void parse(InputStream inputStream) throws XMLStreamException {

        // Declare.
        XMLEventReader reader = null;
        
        // Initialize.
        this.mappingFileNames = new HashSet<String>();

        try {

            // Get the XML configuration file reader.
            XMLInputFactory factory = XMLInputFactory.newInstance();
            reader = factory.createXMLEventReader(inputStream);

            // Loop through the XML events.
            while (reader.hasNext()) {

                // Get the next event.
                XMLEvent event = reader.nextEvent();

                // Check if the event is a start element.
                if (event.isStartElement()) {

                    switch(getElementName(ELEMENT_NAME.class, event.asStartElement())) {

                        case JNDI:

                            // Get the JNDI name.
                            this.jndiName = getAttributeData(event, ATTRIBUTE_NAME.NAME);
                            break;

                        case LDAP:

                            // Get the initial context factory, provider URL, security authentication,
                            // security credentials, and security principal.
                            this.initialContextFactory = getAttributeData(event, ATTRIBUTE_NAME.INITIAL_CONTEXT_FACTORY);
                            this.providerUrl = getAttributeData(event, ATTRIBUTE_NAME.PROVIDER_URL);
                            this.securityAuthentication = getAttributeData(event, ATTRIBUTE_NAME.SECURITY_AUTHENTICATION);
                            this.securityCredentials = getAttributeData(event, ATTRIBUTE_NAME.SECURITY_CREDENTIALS);
                            this.securityPrincipal = getAttributeData(event, ATTRIBUTE_NAME.SECURITY_PRINCIPAL);
                            break;

                        case LOGGING:
                            
                            this.loggingLevel = LoggingLevel.getLoggingLevel(getAttributeData(event, ATTRIBUTE_NAME.LEVEL));
                            break;
                        	
                        case MAPPING_FILE:

                            // Get the mapping file name.
                            String mappingFileName = getElementData(reader.nextEvent());

                            // Add the mapping file name to the mapping file names.
                            this.mappingFileNames.add(mappingFileName);
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
                }
                catch (XMLStreamException e) {
                    // Ignore.
                }
            }
        }
    }
}
