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
package com.unboundedprime.tapioca.core.exception;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of the com.unboundedprime.tapioca.core.exception.InvalidInterfaceException class.
 */
public class InvalidInterfaceExceptionTest {

	/**
	 * Test of default constructor, of class InvalidInterfaceException.
	 */
	@Test
	public void testInvalidInterfaceExceptionConstructor_NoArguments_ConstructionSuccessful() {
		System.out.println("testInvalidInterfaceExceptionConstructor_NoArguments_ConstructionSuccessful");
		
		final Throwable instance = new InvalidInterfaceException();
		assertNotNull("instance may not be null", instance);
	}
	
	/**
	 * Test of message only constructor, of class InvalidInterfaceException.
	 */
	@Test
	public void testInvalidInterfaceExceptionConstructor_MessageArgument_ValidMessagePresent() {
		System.out.println("testInvalidInterfaceExceptionConstructor_MessageArgument_ValidMessagePresent");
		
		final String expectedResult = "test message";
		final Throwable instance = new InvalidInterfaceException(expectedResult);
		
		assertSame("message must be the same", expectedResult, instance.getMessage());
	}
	
	/**
	 * Test of throwable only constructor, of class InvalidInterfaceException.
	 */
	@Test
	public void testInvalidInterfaceExceptionConstructor_ThrowableArgument_ValidThrowablePresent() {
		System.out.println("testInvalidInterfaceExceptionConstructor_ThrowableArgument_ValidThrowablePresent");
		
		final Throwable expectedResult = new NullPointerException();
		final Throwable instance = new InvalidInterfaceException(expectedResult);
		
		assertSame("throwable must be the same", expectedResult, instance.getCause());
	}
	
	/**
	 * Test of message and throwable combination constructor, of class InvalidInterfaceException.
	 */
	@Test
	public void testInvalidInterfaceExceptionConstructor_ThrowableAndMessageArgument_ValidMessageAndThrowablePresent() {
		System.out.println("testInvalidInterfaceExceptionConstructor_ThrowableAndMessageArgument_ValidMessageAndThrowablePresent");
		
		final Throwable expectedThrowableResult = new NullPointerException();
		final String expectedMessageResult = "test message";
		final Throwable instance = new InvalidInterfaceException(expectedMessageResult, expectedThrowableResult);
		
		assertSame("message must be the same", expectedMessageResult, instance.getMessage());
		assertSame("throwable must be the same", expectedThrowableResult, instance.getCause());
	}
}
