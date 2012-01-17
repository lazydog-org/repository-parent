package org.lazydog.repository.ldap.internal.parser;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;


/**
 * XML configuration file parser.
 * 
 * @author  Ron Rickard
 */
public abstract class XMLConfigurationFileParser {

    /**
     * Private constructor.
     */
    @SuppressWarnings("unused")
    private XMLConfigurationFileParser() {	
    }
	
    /**
     * Protected constructor.
     * 
     * @param  configurationPathname  the pathname for the XML configuration file.
     * @param  schemaPathname		  the pathname for the XSD schema file.
     * 
     * @throws  ParsingException  if unable to validate and parse the XML configuration file.
     */
    protected XMLConfigurationFileParser(String configurationPathname, String schemaPathname) 
    	throws ParsingException {

        
        InputStream configurationStream = null;
        InputStream schemaStream = null;

        try {

            // Get the configuration input stream and the schema input stream.
            configurationStream = getInputStream(configurationPathname);
            schemaStream = getInputStream(schemaPathname);
            
            // Validate the XML configuration file.
            validate(configurationStream, schemaStream);
        }
        catch (Exception e) {
            throw new ParsingException(
                    "Unable to validate the XML configuration file " + configurationPathname + ".", 
                    e, configurationPathname, schemaPathname);
        }
        finally {
            
            // Check if the configuration input stream exists.
            if (configurationStream != null) {
                try {
                    
                    // Close the configuration input stream.
                    configurationStream.close();
                }
                catch (IOException e) {
                    // Ignore.
                }
            }
            
            // Check if the schema input stream exists.
            if (schemaStream != null) {
            	try {
            		
                    // Close the schema input stream.
                    schemaStream.close();
            	}
            	catch(IOException e) {
                    // Ignore.
            	}
            }
        }

        try {

            // Get the configuration input stream.
            configurationStream = getInputStream(configurationPathname);

            // Parse the XML configuration file.
            this.parse(configurationStream);
        }
        catch (Exception e) {
            throw new ParsingException(
                    "Unable to parse the configuration file " + configurationPathname + ".", 
                    e, configurationPathname, schemaPathname);
        }
        finally {

            // Check if the configuration input stream exists.
            if (configurationStream != null) {
                try {

                    // Close the input stream.
                    configurationStream.close();
                }
                catch (IOException e) {
                    // Ignore.
                }
            }
        }
    }

    /**
     * Get the attribute data.
     *
     * @param  event          the XML event.
     * @param  attributeName  the attribute name.
     *
     * @return  the attribute data.
     */
    protected static <T extends Enum<T>> String getAttributeData(XMLEvent event, T attributeName) {
    	return (event.asStartElement().getAttributeByName(new QName(attributeName.toString().toLowerCase().replaceAll("_", "-"))) != null) ?
                event.asStartElement().getAttributeByName(new QName(attributeName.toString().toLowerCase().replaceAll("_", "-"))).getValue() :
                null;
    }

    /**
     * Get the element data.
     *
     * @param  event  the XML event.
     *
     * @return  the element data.
     */
    protected static String getElementData(XMLEvent event) {
        return event.asCharacters().getData();
    }

    /**
     * Get the element name.
     *
     * @param  enumType  the enumeration type of the element name.
     * @param  element   the end element.
     *
     * @return  the element name.
     */
    protected static <T extends Enum<T>> T getElementName(Class<T> enumType, EndElement element) {
        return getElementName(enumType, element.getName().getLocalPart());
    }

    /**
     * Get the element name.
     *
     * @param  enumType  the enumeration type of the element name.
     * @param  element   the start element.
     *
     * @return  the element name.
     */
    protected static <T extends Enum<T>> T getElementName(Class<T> enumType, StartElement element) {
        return getElementName(enumType, element.getName().getLocalPart());
    }

    /**
     * Get the element name.
     *
     * @param  enumType  the enumeration type of the element name.
     * @param  name      the name.
     *
     * @return  the element name.
     */
    @SuppressWarnings("unchecked")
    protected static <T extends Enum<T>> T getElementName(Class<T> enumType, String name) {
        return (T)Enum.valueOf((Class<? extends Enum>)enumType, name.toUpperCase().replaceAll("-", "_"));
    }
    
    /**
     * Get the configuration input stream.
     * 
     * @return  the configuration input stream.
     */
    private static InputStream getInputStream(String pathname) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(pathname);
    }

    /**
     * Parse the XML configuration file.
     *
     * @param  inputStream  the input stream for the XML configuration file.
     *
     * @throws  XMLStreamException  if unable to parse the input stream.
     */
    protected abstract void parse(InputStream inputStream) throws XMLStreamException;

    /**
     * Validate the XML configuration file.
     *
     * @param  configurationStream  the input stream for the XML configuration file.
     * @param  schemaStream         the input stream for the XSD schema file.
     * 
     * @throws  IOException   if unable to validate the configuration input stream
     *                        due to an IO error.
     * @throws  SAXException  if unable to validate the configuration input stream
     *                        due to a STAX parser error.
     */
    private static void validate(InputStream configurationStream, InputStream schemaStream) throws IOException, SAXException {

        // Validate the configuration input stream.
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(schemaStream));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(configurationStream));
    }
}
