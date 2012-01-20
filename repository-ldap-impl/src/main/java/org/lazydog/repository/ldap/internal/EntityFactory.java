package org.lazydog.repository.ldap.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Entity factory.
 * 
 * @author  Ron Rickard
 */
public final class EntityFactory<T> {

    private Class<T> entityClass;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<Class<?>> supportedTypes;

    /**
     * Private constructor.
     * 
     * @param  entityClass     the entity class.
     * @param  supportedTypes  the supported types.
     */
    private EntityFactory(Class<T> entityClass, List<Class<?>> supportedTypes) {
        this.entityClass = entityClass;
        this.supportedTypes = supportedTypes;
        logger.info("Entity factory initialized with entity " + entityClass + ".");
        logger.info("Entity factory initialized with supported types '" + supportedTypes + "'.");
    }
	
    /**
     * Create the entity.
     * 
     * @param  propertyMap  the property name-property value map.
     * 
     * @return  the created entity.
     * 
     * @throws  EntityFactoryException  if unable to create an entity.
     */
    public T createEntity(Map<String,Object> propertyMap) throws EntityFactoryException {

        Date startTime = new Date();
        logger.info("Creating a new entity of type " + this.entityClass + ".");

        T  entity;

        try {

            // Get a new instance of the entity class.
            entity = this.entityClass.newInstance();

            // Get the property names.
            Set<String> propertyNames = propertyMap.keySet();

            // Loop through the property names.
            for (String propertyName : propertyNames) {

                // Get the property value.
                Object propertyValue = propertyMap.get(propertyName);

                logger.debug("Setting property '" + propertyName + "' to '" + propertyValue + "'.");

                // Set the property value for the entity.
                this.setPropertyValue(entity, propertyName, propertyValue);
            }
        }
        catch (IllegalAccessException e) {
            throw new EntityFactoryException("Unable to instantiate the entity " + this.entityClass + ".", e, this.entityClass, null);
        }
        catch (InstantiationException e) {
            throw new EntityFactoryException("Unable to instantiate the entity " + this.entityClass + ".", e, this.entityClass, null);
        }

        logger.trace("Entity created in " + duration(startTime, new Date()) + "s.");

        return entity;
    }

    /**
     * Get the duration in seconds.
     * 
     * @param  startTime  the start time.
     * @param  endTime    the end time.
     * 
     * @return  the duration in seconds.
     */
    private static double duration(Date startTime, Date endTime) {
        return ((double)endTime.getTime() - (double)startTime.getTime()) / 1000.0;
    }
	
    /**
     * Get the accessor method name.
     * 
     * @param  propertyName  the class' property name.
     * 
     * @return  the accessor method name.
     */
    private static String getAccessorMethodName(String propertyName) {
        return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    /**
     * Get the accessor method's return type for the specified property.
     * 
     * @param  propertyName  the property name.
     * 
     * @return  the accessor method's return type.
     * 
     * @throws  EntityFactoryException  if unable to get the acessor method's return type.
     */
    public Class<?> getAccessorReturnType(String propertyName) throws EntityFactoryException {
        return this.getMethod(getAccessorMethodName(propertyName)).getReturnType();
    }
	
    /**
     * Get the method identified by the method name in the entity class.
     * 
     * @param  methodName  the method name.
     * 
     * @return  the method.
     * 
     * @throws  EntityFactoryException  if unable to get the method.
     */
    private Method getMethod(String methodName) throws EntityFactoryException {

        // Get the methods of the entity class.
        Method[] methods = this.entityClass.getMethods();

        // Loop through the methods.
        for (Method method : methods) {

            // Check if the entity class' method name is equal to the desired method name.
            if (method.getName().equals(methodName)) {
                return method;
            }
        }

        throw new EntityFactoryException(
                "Method '" + methodName + "' not found in entity " + this.entityClass + ".", 
                this.entityClass, methodName);
    }

    /**
     * Get the mutator method name.
     * 
     * @param  propertyName  the class' property name.
     * 
     * @return  the mutator method name.
     */
    private static String getMutatorMethodName(String propertyName) {
        return "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

    /**
     * Get the mutator method's parameter type for the specified property.
     * 
     * @param  propertyName  the property name.
     * 
     * @return  the mutator method's parameter type.
     * 
     * @throws  EntityFactoryException  if unable to get the mutator method's parameter type.
     */
    public Class<?> getMutatorParameterType(String propertyName) throws EntityFactoryException {
        return this.getMethod(getMutatorMethodName(propertyName)).getParameterTypes()[0];
    }

    /**
     * Get the property name-property value map for the entity.
     *  
     * @param  entity         the entity.
     * @param  propertyNames  the property names.
     * 
     * @return  the property name-property value map.
     * 
     * @throws  EntityFactoryException  if unable to get the property name-property value map.
     */
    public Map<String,Object> getPropertyMap(T entity, Set<String> propertyNames) throws EntityFactoryException {

        Date startTime = new Date();
        logger.info("Getting values of properties '" + propertyNames + "' for entity " + this.entityClass + ".");

        // Initialize the property name-property value map.
        Map<String,Object> propertyMap = new HashMap<String,Object>();

        // Loop through the property names.
        for (String propertyName : propertyNames) {

            // Get the property value.
            Object propertyValue = this.getPropertyValue(entity, propertyName);

            // Add the property value-property name to the map.
            propertyMap.put(propertyName, propertyValue);

            logger.debug("Retrieved property '" + propertyName + "' value '" + propertyValue + "'.");
        }

        logger.trace("Property values retrieved in " + duration(startTime, new Date()) + "s.");

        return propertyMap;
    }
	
    /**
     * Get the value of the property from the entity.
     * 
     * @param  entity        the entity.
     * @param  propertyName  the property name.
     * 
     * @return  the value of the property.
     * 
     * @throws  EntityFactoryException  if unable to get the value of the property.
     */
    public Object getPropertyValue(T entity, String propertyName) throws EntityFactoryException {

        // Get the property value by invoking the accessor method for the property.
        String methodName = getAccessorMethodName(propertyName);
        Method method = this.getMethod(methodName);
        Object propertyValue = this.invokeMethod(entity, method, null);

        // Check if the property value is not an expected type.
        if (!this.isExpectedType(propertyValue, method.getReturnType())) {
            throw new EntityFactoryException(
                    "Actual return type for property '" + propertyName + 
                    "' does not match the expected return type '" +  method.getReturnType() + "'.", 
                    this.entityClass, methodName);
        }

        return propertyValue;
    }

    /**
     * Invoke the method.  The method will be either a mutator or accessor method.
     * 
     * @param  entity          the entity.
     * @param  method          the method.
     * @param  parameterValue  the value of the parameter for a mutator method or null for an accessor method.
     * 
     * @return  null for a mutator method or the return value for an accessor method.
     * 
     * @throws  EntityFactoryException  if unable to invoke the method.
     */
    private Object invokeMethod(T entity, Method method, Object parameterValue) throws EntityFactoryException {

        // Initialize.
        Object returnValue = null;

        // Get the method name.
        String methodName = method.getName();

        try {

            // Check if this is a mutator method.
            if (parameterValue != null) {

                // Check if the method's return type is not void.
                if (method.getReturnType() != Void.TYPE) {
                    throw new EntityFactoryException(
                            "Expecting VOID for method '" + methodName + "' return type.", 
                            this.entityClass, methodName);
                }

                // Check if the method does not have exactly 1 argument.
                if (method.getParameterTypes().length != 1) {
                    throw new EntityFactoryException(
                            "Method '" + methodName + "' should only have one argument.", 
                            this.entityClass, methodName);
                }

                // Invoke the method.
                method.invoke(entity, parameterValue);
            }
			
            // Otherwise, this is an accessor method.
            else {

                // Check if the method's return type is void.
                if (method.getReturnType() == Void.TYPE) {
                    throw new EntityFactoryException(
                            "Not expecting VOID for method '" + methodName + "' return type.", 
                            this.entityClass, methodName);
                }
				
                // Check if the method has any arguments.
                if (method.getParameterTypes().length != 0) {
                    throw new EntityFactoryException(
                            "Method '" + methodName + "' should not have any arguments.", 
                            this.entityClass, methodName);
                }
				
                // Invoke the method.
                returnValue = method.invoke(entity);
            }
        }
        catch (IllegalAccessException e) {
            throw new EntityFactoryException(
                    "Unable to invoke method '" + methodName + "' on entity " + this.entityClass + ".", 
                    e, this.entityClass, methodName);
        }
        catch(InvocationTargetException e) {
            throw new EntityFactoryException(
                    "Unable to invoke method '" + methodName + "' on entity " + this.entityClass + ".", 
                    e, this.entityClass, methodName);
        }

        return returnValue;
    }
	
    /**
     * Is the value an expected type?
     * 
     * @param  value         the value.
     * @param  expectedType  the expected type.
     * 
     * @return  true if the value is an expected type, otherwise false.
     */
    private boolean isExpectedType(Object value, Class<?> expectedType) {

        // Initialize.
        boolean valid = false;

        // Null is a valid value.
        if (value == null) {
            valid = true;
        }

        // Check if the value is an expected type.
        else if (expectedType.isInstance(value)) {

            // Check if the value is a supported type.
            valid = isSupportedType(value);
        }

        return valid;
    }
	
    /**
     * Is the value a supported type?
     * 
     * @param  value  the value.
     * 
     * @return  true if the value is a supported type, otherwise false.
     */
    @SuppressWarnings("unchecked")
    private boolean isSupportedType(Object value) {

        // Initialize.
        boolean isSupportedType = false;

        // Loop through the supported types.
        for (Class<?> supportedType : this.supportedTypes) {

            // Check if the value is a supported type.
            if (supportedType.isInstance(value)) {
                isSupportedType = true;
            }
			
            // Check if the value is a Set.
            else if (Set.class.isInstance(value)) {

                // Check if the set has values.
                if (((Set)value).size() > 0) {

                    // Check if an element of the set is a supported type.
                    isSupportedType = isSupportedType(((Set)value).toArray()[0]);
                }
                else {
                    isSupportedType = true;
                }
            }
        }

        return isSupportedType;
    }
	
    /**
     * Create a new instance of this class.
     * 
     * @param  entityClass     the entity class.
     * @param  supportedTypes  the supported types.
     * 
     * @return  a new instance of this class.
     */
    public static <T> EntityFactory<T> newInstance(Class<T> entityClass, List<Class<?>> supportedTypes) {
        return new EntityFactory<T>(entityClass, supportedTypes);
    }

    /**
     * Set the value of the property for the entity.
     * 
     * @param  entity         the entity.
     * @param  propertyName   the property name.
     * @param  propertyValue  the property value.
     * 
     * @throws  EntityFactoryException  if unable to set the value of the property.
     */
    private void setPropertyValue(T entity, String propertyName, Object propertyValue) throws EntityFactoryException {

        // Get the mutator method for the property.
        String methodName = getMutatorMethodName(propertyName);
        Method method = this.getMethod(methodName);

        // Check if the property value is an expected type.
        if (!this.isExpectedType(propertyValue, method.getParameterTypes()[0])) {
            throw new EntityFactoryException(
                    "Actual parameter type for property '" + propertyName + 
                    "' does not match the expected parameter type '" +  method.getParameterTypes()[0] + "'.", 
                    this.entityClass, methodName);
        }

        // Invoke the mutator method.
        this.invokeMethod(entity, method, propertyValue);
    }
}
