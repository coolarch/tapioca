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
package com.unboundedprime.tapioca.core;

import com.unboundedprime.tapioca.core.exception.AmbiguousInterfaceException;
import com.unboundedprime.tapioca.core.exception.InvalidContextException;
import com.unboundedprime.tapioca.core.exception.InvalidInterfaceException;
import com.unboundedprime.tapioca.core.impl.DefaultContainerImpl;
import java.io.InputStream;
import java.util.List;

/**
 * Main container facade interface in the Tapioca container framework.
 */
public interface Container {
	
	/**
	 * Gets a specific instance of an object from the container.
	 * @param <T> Type of the class to extract
	 * @param classOrInterfaceToExtract Class of the class, or interface for which to obtain an implementation
	 * @return Object instance or null, if there is no implementer of the interface, or the actual object
	 */
	<T> T getObjectThatImplementsOrNull(final Class<T> classOrInterfaceToExtract) throws AmbiguousInterfaceException, InvalidInterfaceException;
	
	/**
	 * Gets a set of object instances from the container that implement a specific interface, or are an instance of a class.
	 * @param <T> Type of the class to extract
	 * @param classOrInterfaceToExtract Class of the class, or interface for which to obtain an implementation
	 * @return Object instance or null, if there is no implementer of the interface, or the actual object
	 */
	<T> List<T> getObjectsThatImplement(final Class<T> classOrInterfaceToExtract) throws InvalidInterfaceException;
	
	/**
	 * Determines whether an object is available in the container that implements the specified interface, or is an instance of the specified class.
	 * @param <T> Type of the class or interface for which to check
	 * @param classOrInterface Class of the class, or interface for which to determine the availability of an implementation
	 */
	<T> boolean containsObjectThatImplements(final Class<T> classOrInterface) throws InvalidInterfaceException;

	/**
	 * Builder by which instances of the Tapioca container are instantiated.
	 */
	public static final class Builder {
		
		/**
		 * Builds a container instance using a JavaBeans XML configuration file provided in an InputStream.
		 * @param inputStream InputStream from which to obtain the configuration
		 * @return Container instance containing the fully wired object graph context
		 * @throws InvalidContextException If the provided context is malformed
		 */
		public static Container buildContainerFromXmlOnInputStream(final InputStream inputStream) throws InvalidContextException {
			return new DefaultContainerImpl(inputStream);
		}
		
		/**
		 * Builds a container instance using a JavaBeans XML configuration file located on the classpath referenced in a string.
		 * @param inputStream String containing the fully qualified location in the classpath from which to obtain the configuration
		 * @return Container instance containing the fully wired object graph context
		 * @throws InvalidContextException If the provided context is malformed
		 */
		public static Container buildContainerFromXmlInClasspath(final String xmlFileFromClasspath) throws InvalidContextException {
			return new DefaultContainerImpl(xmlFileFromClasspath);
		}
		
		/**
		 * Prevents construction of a pure utility class.
		 */
		protected Builder() {
			throw new UnsupportedOperationException("Instantiation of Builder is not allowed.");
		}
	}
}
