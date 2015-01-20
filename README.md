# What is Tapioca?

Tapioca is designed to provide an extremely thin layer on top of existing, albeit obscure, portions of the standard Java class library in order to provide an IoC container.

# Does Tapioca stand for anything?

Yes: (T)iny (A)PI (P)roviding (I)nversion (o)f (c)ontrol (a)rchitecture

# What are the goals of Tapioca?

* No external dependencies for the core library, other than the core library jar itself
* Core functionality always small (below 20 KiB)
* Rely on standard functionality that is already part of the core Java library whenever possible
* Do one thing, and do it well

# How do I use it?

## Introduction

Tapioca is designed to provide an extremely thin layer on top of existing, albeit obscure, portions of the standard Java class library in order to provide an IoC container. Tapioca does not try to be, nor is it, the Spring framework. If you need a high quality and full featured IoC/DI container, by all means investigate using Spring or Google's Guice.

If you need an extremely lightweight container that has zero external dependencies, Tapioca is what you need.

## Getting the library

This tutorial is targeted at users of Maven, however, those that are using build systems other than those that support Maven artifact repositories, you can download the tapioca-core.jar (and optionally tapioca-utilities.jar) from this web site and include it in your projects classpath by manual means.

To use the tapioca-core library, add the following to the dependencies section of your pom.xml file:

```xml
<dependency>
	<groupId>com.unboundedprime.tapioca</groupId>
	<artifactId>tapioca-core</artifactId>
	<version>1.0.0</version>
</dependency>
```

To use the tapioca-utilities add-on, add the following to dependencies section of your pom.xml file:

```xml
<dependency>
	<groupId>com.unboundedprime.tapioca</groupId>
	<artifactId>tapioca-utilities</artifactId>
	<version>1.0.0</version>
</dependency>
```

## Using the library

### Creating the context file

First you will want to create a context file and include it somewhere in your classpath where it may be accessed by your program. In Maven, this can be accomplished by placing it in a sub-folder of your choosing that is below the src/main/resources/ directory in your project's project directory.

### Example context syntax

A context file example below uses a format accepted by the java.beans.XMLDecoder class that ships with the standard Java classpath. It is further documented at the article Long Term Persistence of JavaBeans Components: XML Schema. The Tapioca library acts as a facade to this loader.

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<java>

	<object id="childClassInstance" class="com.unboundedprime.tapioca.core.ChildTestClass">
		<void property="text">
			<string>Hello World!</string>
		</void>
	</object>

	<object id="parentClassInstance" class="com.unboundedprime.tapioca.core.ParentTestClass">
		<void property="child">
			<object idref="childClassInstance"/>
		</void>
	</object>

</java>
```

### Loading the context into a container instance

```java
final Container instance = Container.Builder.buildContainerFromXmlInClasspath("context.xml");
```

### Extract an implementation

You can extract from the context based one of the interfaces it implements, or the class itself.

```java
final MyService myService = instance.getObjectThatImplementsOrNull(MyService.class);
```

### Extract all that implement

You can extract a List of multiple implementations from the container as follows:

```java
final List<MyService> myServices = instance.getObjectsThatImplement(MyService.class);
```

This option allows for multiple implementations to be discovered for dynamic use in your application.

# Current Status

Tapioca 1.0.0 has been released into the Maven central repository. A non-Maven downloadable version is also being prepared and will be made available here in the downloads section shortly.