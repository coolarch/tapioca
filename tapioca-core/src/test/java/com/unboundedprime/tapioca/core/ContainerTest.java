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
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.List;
import com.unboundedprime.tapioca.core.exception.InvalidContextException;
import com.unboundedprime.tapioca.core.exception.InvalidInterfaceException;
import com.unboundedprime.tapioca.utilities.util.ClassPathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of the com.unboundedprime.tapioca.core.Container interface.
 */
public class ContainerTest {

	/**
	 * Test of testGetObjectThatImplementsOrNull method, of interface Container with a valid context and a valid object graph.
	 */
	@Test
	public void testGetObjectThatImplementsOrNull_ValidContextAndValidRequestedObject_ReturnValidObjectGraph () throws Throwable {
		System.out.println("testGetObjectThatImplementsOrNull_ValidContextAndValidRequestedObject_ReturnValidObjectGraph");
		
		final String contextFileName = "beans_ContainerTest_testGetObjectThatImplementsOrNull.xml";
		final String contextFileLocation = ClassPathUtils.generateClassPathPrefixForFileInSamePackageAs(this, contextFileName);
		final Container instance = Container.Builder.buildContainerFromXmlInClasspath(contextFileLocation);
		final Class<ParentTestInterface> interfaceToRequest = ParentTestInterface.class;
		
		final ParentTestInterface result = instance.getObjectThatImplementsOrNull(interfaceToRequest);
		
		assertNotNull("object must not be null", result);
		assertNotNull("child object must not be null", result.getChild());
		assertEquals("expected text must be present", "Hello World!", result.getChild().getText());
	}

	/**
	 * Test of testGetObjectThatImplementsOrNull method, of interface Container with a valid context and an invalid object graph.
	 */
	@Test
	public void testGetObjectThatImplementsOrNull_ValidContextAndInvalidRequestedObject_ReturnNull () throws Throwable {
		System.out.println("testGetObjectThatImplementsOrNull_ValidContextAndInvalidRequestedObject_ReturnNull");
		
		final String contextFileName = "beans_ContainerTest_testGetObjectThatImplementsOrNull.xml";
		final String contextFileLocation = ClassPathUtils.generateClassPathPrefixForFileInSamePackageAs(this, contextFileName);
		final Container instance = Container.Builder.buildContainerFromXmlInClasspath(contextFileLocation);
		final Class<Map> interfaceToRequest = Map.class;
		final Map result = instance.getObjectThatImplementsOrNull(interfaceToRequest);
		
		assertNull("object must be null", result);
	}

	/**
	 * Test of testGetObjectThatImplementsOrNull method, of interface Container with a valid context and a null requested interface.
	 */
	@Test(expected=InvalidInterfaceException.class)
	public void testGetObjectThatImplementsOrNull_ValidContextAndNullRequestedInterface_ThrowException () throws Throwable {
		System.out.println("testGetObjectThatImplementsOrNull_ValidContextAndNullRequestedInterface_ThrowException");
		
		final String contextFileName = "beans_ContainerTest_testGetObjectThatImplementsOrNull.xml";
		final String contextFileLocation = ClassPathUtils.generateClassPathPrefixForFileInSamePackageAs(this, contextFileName);
		final Container instance = Container.Builder.buildContainerFromXmlInClasspath(contextFileLocation);
		final Class<Map> interfaceToRequest = null;
		final Map result = instance.getObjectThatImplementsOrNull(interfaceToRequest);
		
		assertNull("object must be null", result);
	}
	
	/**
	 * Test of testGetObjectThatImplementsOrNull method, of interface Container with a valid context and a requested interface that will cause ambiguity.
	 */
	@Test(expected=AmbiguousInterfaceException.class)
	public void testGetObjectThatImplementsOrNull_ValidContextAndRequestedInterfaceThatWillGetMultiples_ThrowException () throws Throwable {
		System.out.println("testGetObjectThatImplementsOrNull_ValidContextAndRequestedInterfaceThatWillGetMultiples_ThrowException");
		
		final String contextFileName = "beans_ContainerTest_testGetObjectsThatImplement.xml";
		final String contextFileLocation = ClassPathUtils.generateClassPathPrefixForFileInSamePackageAs(this, contextFileName);
		final Container instance = Container.Builder.buildContainerFromXmlInClasspath(contextFileLocation);
		final Class<CommonGroupInterface> interfaceToRequest = CommonGroupInterface.class;
		instance.getObjectThatImplementsOrNull(interfaceToRequest);
	}
	
	/**
	 * Test of testGetObjectsThatImplement method, of interface Container with a valid context and a valid list of objects.
	 */
	@Test
	public void testGetObjectsThatImplement_ValidContextAndValidRequestedInterface_ReturnValidObjectGraph() throws Throwable {
		System.out.println("testGetObjectsThatImplement_ValidContextAndValidRequestedInterface_ReturnValidObjectGraph");

		final String contextFileName = "beans_ContainerTest_testGetObjectsThatImplement.xml";
		final String contextFileLocation = ClassPathUtils.generateClassPathPrefixForFileInSamePackageAs(this, contextFileName);
		final Container instance = Container.Builder.buildContainerFromXmlInClasspath(contextFileLocation);
		final List<CommonGroupInterface> result = instance.getObjectsThatImplement(CommonGroupInterface.class);
	
		assertEquals("resulting list must contain three (3) objects", 3, result.size());
		
		// No, we aren't trying to be thread safe, but it does provide a handy generically wrappable and incrementable object
		final Map<Class<?>, AtomicInteger> counterMap = new HashMap<Class<?>, AtomicInteger>(3);
		counterMap.put(FirstCommonGroupedClass.class, new AtomicInteger());
		counterMap.put(SecondCommonGroupedClass.class, new AtomicInteger());
		counterMap.put(ThirdCommonGroupedClass.class, new AtomicInteger());
		
		for (final Object resultObject : result) {
			counterMap.get(resultObject.getClass()).incrementAndGet();
		}
		
		assertEquals("FirstCommonGroupedClass must only be present once", 1, counterMap.get(FirstCommonGroupedClass.class).intValue());
		assertEquals("SecondCommonGroupedClass must only be present once", 1, counterMap.get(SecondCommonGroupedClass.class).intValue());
		assertEquals("ThirdCommonGroupedClass must only be present once", 1, counterMap.get(ThirdCommonGroupedClass.class).intValue());
	}
		
	/**
	 * Test of testGetObjectsThatImplement method, of interface Container with a valid context and an invalid requested interface.
	 */
	@Test
	public void testGetObjectsThatImplement_ValidContextAndInvalidRequestedInterface_ReturnEmptyList() throws Throwable {
		System.out.println("testGetObjectsThatImplement_ValidContextAndInvalidRequestedInterface_ReturnEmptyList");

		final String contextFileName = "beans_ContainerTest_testGetObjectsThatImplement.xml";
		final String contextFileLocation = ClassPathUtils.generateClassPathPrefixForFileInSamePackageAs(this, contextFileName);
		final Container instance = Container.Builder.buildContainerFromXmlInClasspath(contextFileLocation);
		final List<Map> result = instance.getObjectsThatImplement(Map.class);
	
		assertNotNull("resulting list may not be null", result);
		assertEquals("resulting list must contain no objects", 0, result.size());
	}
		
	/**
	 * Test of testGetObjectsThatImplement method, of interface Container with a valid context and a null requested interface.
	 */
	@Test(expected=InvalidInterfaceException.class)
	public void testGetObjectsThatImplement_ValidContextAndNullRequestedInterface_ThrowException() throws Throwable {
		System.out.println("testGetObjectsThatImplement_ValidContextAndNullRequestedInterface_ThrowException");

		final String contextFileName = "beans_ContainerTest_testGetObjectsThatImplement.xml";
		final String contextFileLocation = ClassPathUtils.generateClassPathPrefixForFileInSamePackageAs(this, contextFileName);
		final Container instance = Container.Builder.buildContainerFromXmlInClasspath(contextFileLocation);
		final Class<Map> interfaceToRequest = null;

		final List<Map> result = instance.getObjectsThatImplement(interfaceToRequest);
	}
	
	/**
	 * Test of containsObjectThatImplements method, of interface Container with a valid context and a valid requested interface.
	 */
	@Test
	public void testContainsObjectThatImplements_ValidContextAndValidRequestedInterface_ReturnTrue() throws Throwable {
		System.out.println("testContainsObjectThatImplements_ValidContextAndValidRequestedInterface_ReturnTrue");

		final String contextFileName = "beans_ContainerTest_testGetObjectsThatImplement.xml";
		final String contextFileLocation = ClassPathUtils.generateClassPathPrefixForFileInSamePackageAs(this, contextFileName);
		final Container instance = Container.Builder.buildContainerFromXmlInClasspath(contextFileLocation);
		final Class<FirstCommonGroupedClass> interfaceToRequest = FirstCommonGroupedClass.class;
		boolean expResult = true;

		boolean result = instance.containsObjectThatImplements(interfaceToRequest);
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of containsObjectThatImplements method, of interface Container with a valid context and an invalid requested interface.
	 */
	@Test
	public void testContainsObjectThatImplements_ValidContextAndInvalidRequestedInterface_ReturnFalse() throws Throwable {
		System.out.println("testContainsObjectThatImplements_ValidContextAndInvalidRequestedInterface_ReturnFalse");

		final String contextFileName = "beans_ContainerTest_testGetObjectsThatImplement.xml";
		final String contextFileLocation = ClassPathUtils.generateClassPathPrefixForFileInSamePackageAs(this, contextFileName);
		final InputStream inputStream = this.getClass().getResourceAsStream(contextFileLocation);
		final Container instance = Container.Builder.buildContainerFromXmlOnInputStream(inputStream);
		final Class<Map> interfaceToRequest = Map.class;
		boolean expResult = false;

		boolean result = instance.containsObjectThatImplements(interfaceToRequest);
		
		assertEquals(expResult, result);
	}
	
	/**
	 * Test of containsObjectThatImplements method, of interface Container with a valid context and a null requested interface.
	 */
	@Test(expected=InvalidInterfaceException.class)
	public void testContainsObjectThatImplements_ValidContextAndNullRequestedInterface_ThrowException() throws Throwable {
		System.out.println("testContainsObjectThatImplements_ValidContextAndNullRequestedInterface_ReturnTrue");

		final String contextFileName = "beans_ContainerTest_testGetObjectsThatImplement.xml";
		final String contextFileLocation = ClassPathUtils.generateClassPathPrefixForFileInSamePackageAs(this, contextFileName);
		final Container instance = Container.Builder.buildContainerFromXmlInClasspath(contextFileLocation);
		final Class<FirstCommonGroupedClass> interfaceToRequest = null;

		boolean result = instance.containsObjectThatImplements(interfaceToRequest);
	}
	
	/**
	 * Test of Builder.buildContainerFromXmlOnInputStream method, of class Container.Builder with a null inputstream.
	 */
	@Test(expected=InvalidContextException.class)
	public void testBuilderBuildContainerFromXml_NullInputStream_ThrowException() throws Throwable {
		System.out.println("testBuilderBuildContainerFromXml_NullInputStream_ThrowException");

		final Container instance = Container.Builder.buildContainerFromXmlOnInputStream(null);
	}
	
	/**
	 * Test of Builder.buildContainerFromXmlInClasspath method, of class Container.Builder with a null string.
	 */
	@Test(expected=InvalidContextException.class)
	public void testBuilderBuildContainerFromXmlInClasspath_NullString_ThrowException() throws Throwable {
		System.out.println("testBuilderBuildContainerFromXmlInClasspath_NullString_ThrowException");

		final Container instance = Container.Builder.buildContainerFromXmlInClasspath(null);
	}
		
	/**
	 * Test of the Builder constructor, of class Container.Builder.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void testBuilderConstructor_NoArguments_ThrowException() throws Throwable {
		System.out.println("testBuilderConstructor_NoArguments_ThrowException");

		final Container.Builder builder = new Container.Builder();
	}
}
