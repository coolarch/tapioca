/**
 * Tapioca - Tiny API Providing Inversion of Control Architecture
 * Copyright (C) 2011 by Unbounded Prime, LLC
 * All Rights Reserved
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package com.unboundedprime.tapioca.core.impl;

import com.unboundedprime.tapioca.core.exception.AmbiguousInterfaceException;
import com.unboundedprime.tapioca.core.Container;
import com.unboundedprime.tapioca.core.exception.InvalidContextException;
import com.unboundedprime.tapioca.core.exception.InvalidInterfaceException;
import java.beans.XMLDecoder;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the Container interface in the Tapioca container framework.
 */
public class DefaultContainerImpl implements Container {

	/**
	 * Graph of objects discovered from the context.
	 */
	private final Map<Class, Map<Object, Object>> objects = new HashMap<Class, Map<Object, Object>>();

	/**
	 * Constructs a new DefaultContainerImpl based on a XML context in the class path.
	 * @param classPathContextPath Textual path to the context XML resource in the classpath
	 * @throws InvalidContextException If the context XML could not be interpreted
	 */
	public DefaultContainerImpl(final String classPathContextPath) throws InvalidContextException {
		if (classPathContextPath == null) {
			throw new InvalidContextException("classPathContextPath may not be null.");
		}
		
		final InputStream inputStream = DefaultContainerImpl.class.getResourceAsStream(classPathContextPath);
		
		this.processObjects(inputStream);
	}

	/**
	 * Constructs a new DefaultContainerImpl based on a XML context provided in the input stream.
	 * @param inputStream Input containing the XML content representing the context configuration
	 * @throws InvalidContextException If the context XML could not be interpreted
	 */
	public DefaultContainerImpl(final InputStream inputStream) throws InvalidContextException {
		if (inputStream == null) {
			throw new InvalidContextException("Context file must be valid.");
		}

		this.processObjects(inputStream);
	}

	/**
	 * Processes a context and discovers the root level objects inside.
	 * @param inputStream Input containing the XML content representing the context configuration
	 * @throws InvalidContextException If the context XML could not be interpreted
	 */
	private void processObjects(final InputStream inputStream) throws InvalidContextException {
		final XMLDecoder xmlDecoder = new XMLDecoder(inputStream, this);

		boolean anotherObjectMightBeAvailable = true;
		
		while (anotherObjectMightBeAvailable) {
			anotherObjectMightBeAvailable = this.attemptSingleObjectExtraction(xmlDecoder);
		}

		xmlDecoder.close();
	}

	/**
	 * Attempts the extraction of a single object from the XML decoder.
	 * @param xmlDecoder Decoder from which to attempt to extract the object
	 * @return truth of whether another object might be available
	 */
	private boolean attemptSingleObjectExtraction(final XMLDecoder xmlDecoder) {
		Object object = null;
		
		try {
			object = xmlDecoder.readObject();
		} catch (ArrayIndexOutOfBoundsException ex) {
			object = null;
		}

		if (object != null) {
			catalogObject(object);
		}
		
		return (object != null);
	}

	/**
	 * Catalogs a single object in the objects structure for all determined interfaces that it implements.
	 * @param object Object to catalog
	 */
	private void catalogObject(final Object object) {
		final Class objectClass = object.getClass();
		final Class[] interfaces = objectClass.getInterfaces();

		for (final Class interfaceClass : interfaces) {
			if (this.objects.containsKey(interfaceClass)) {
				final Object existingObject = this.objects.get(interfaceClass);
			}
			
			this.storeObjectForType(object, interfaceClass);
		}
		
		this.storeObjectForType(object, object.getClass());
	}

	/**
	 * Stores a single object for a specific type into the objects structure.
	 * @param object Object to store
	 * @param classOrInterface Class or interface under which it should be cataloged
	 */
	private void storeObjectForType(final Object object, Class<?> classOrInterface) {
		if (!this.objects.containsKey(classOrInterface)) {
			this.objects.put(classOrInterface, new IdentityHashMap<Object, Object>());
		}
		
		final Map<Object, Object> currentObjectMap = this.objects.get(classOrInterface);
		
		if (!currentObjectMap.containsKey(object)) {
			currentObjectMap.put(object, object);
		}
	}

	/**
	 * @see com.unboundedprime.tapioca.core.Container#getObjectThatImplementsOrNull(java.lang.Class) 
	 */
	public <T> T getObjectThatImplementsOrNull(Class<T> classOrInterfaceToExtract) throws AmbiguousInterfaceException, InvalidInterfaceException {
		
		if (classOrInterfaceToExtract == null) {
			throw new InvalidInterfaceException("classOrInterfaceToExtract may not be null");
		}
		
		T objectInstance = null;

		final Map<Object, Object> objectMap = this.objects.get(classOrInterfaceToExtract);
		
		if (objectMap == null) {
			return null;			
		}
		
		if (objectMap.size() > 1) {
			final String errorMessage = "Unable to locate unique object instance for class or interface '" + classOrInterfaceToExtract.getName() + "'.";
			throw new AmbiguousInterfaceException(errorMessage);
		}

		if (!objectMap.isEmpty()) {
			objectInstance = (T) new ArrayList<Object>(objectMap.values()).get(0);
		}

		return objectInstance;
	}

	/**
	 * @see com.unboundedprime.tapioca.core.Container#getObjectsThatImplement(java.lang.Class) 
	 */
	public <T> List<T> getObjectsThatImplement(Class<T> classOrInterfaceToExtract) throws InvalidInterfaceException {
		
		if (classOrInterfaceToExtract == null) {
			throw new InvalidInterfaceException("classOrInterfaceToExtract may not be null");
		}

		final List<T> objectsList = new ArrayList<T>();
		
		final Map<Object, Object> objectMap = this.objects.get(classOrInterfaceToExtract);
		
		if (objectMap != null) {
			objectsList.addAll((Collection<T>)objectMap.values());
		}
		
		return objectsList;
	}

	/**
	 * @see  com.unboundedprime.tapioca.core.Container#containsObjectThatImplements(java.lang.Class) 
	 */
	public <T> boolean containsObjectThatImplements(Class<T> classOrInterface) throws InvalidInterfaceException {
		
		if (classOrInterface == null) {
			throw new InvalidInterfaceException("classOrInterface may not be null");
		}

		// TODO - Optimize this implementation to not use getObjectsThatImplement
		return !this.getObjectsThatImplement(classOrInterface).isEmpty();
	}
}
