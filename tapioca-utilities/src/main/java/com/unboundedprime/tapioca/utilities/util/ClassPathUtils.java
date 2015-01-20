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
package com.unboundedprime.tapioca.utilities.util;

/**
 * Utilities to assist in dealing with the class path in tests.
 */
public class ClassPathUtils {

	/**
	 * Separator character used to separate package name segments in Java source code.
	 */
	public static final String JAVA_PACKAGE_SEPARATOR_CHAR = ".";
	
	/**
	 * Separator character used to separate package name segments in the classpath.
	 */
	public static final String CLASSPATH_PACKAGE_SEPARATOR_CHAR = "/";

	/**
	 * Generates a class path string for the package in which a class resides.
	 * @param <T> Type of the class
	 * @param classToInspect Class instance to inspect to determine its package
	 * @return String 
	 * @throws IllegalArgumentException If the argument is null
	 */
	public static <T> String generateClassPathPrefixForClass(final Class<T> classToInspect) {
		
		if (classToInspect == null) {
			throw new IllegalArgumentException("Class from which to extract classpath from may not be null.");
		}
		
		final Package classPackage = classToInspect.getPackage();
		final String packageName = classPackage.getName();
		final String pathSegment = packageName.replace(JAVA_PACKAGE_SEPARATOR_CHAR, CLASSPATH_PACKAGE_SEPARATOR_CHAR);
		final String fullyQualifiedPath = CLASSPATH_PACKAGE_SEPARATOR_CHAR + pathSegment + CLASSPATH_PACKAGE_SEPARATOR_CHAR;
		
		return fullyQualifiedPath;
	}
	
	/**
	 * Generates a full path to a file in the classpath string that is assumed to reside in the same package as the provided class.
	 * @param <T> Type of the object
	 * @param object Object from which to obtain the package path
	 * @param fileName Name of the file
	 * @return Formatted string containing the full path in the classpath that the file resides
	 * @throws IllegalArgumentException If the object or filename is null
	 */
	public static <T> String generateClassPathPrefixForFileInSamePackageAs(final Object object, final String fileName) {
		
		if (object == null) {
			throw new IllegalArgumentException("Object from which to get class and extract classpath from may not be null.");
		}
		
		if (fileName == null) {
			throw new IllegalArgumentException("Filename may not be null.");
		}
		
		final String resultingPath = generateClassPathPrefixForClass(object.getClass()) + fileName;
		
		return resultingPath;
	}

	/**
	 * Prevents instantiation of a pure utility class.
	 */
	protected ClassPathUtils() {
		throw new UnsupportedOperationException("Instantiation of pure utility classes is not allowed.");
	}
}
