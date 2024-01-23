# Yildiz-Engine common-application

This is the official repository of The Common Application library, part of the Yildiz-Engine project.
The common application library is a set of utilities to package an application.

## Features

* Utilities to run, configure and update an application.
* ...

## Requirements

To build this module, you will need the latest java JDK and Maven 3.

## Coding Style and other information

Project website:
https://engine.yildiz-games.be

Issue tracker:
https://github.com/yildiz-online/common-application/issues

## License

All source code files are licensed under the permissive MIT license
(http://opensource.org/licenses/MIT) unless marked differently in a particular folder/file.

## Build instructions

Go to your root directory, where you POM file is located.

Then invoke maven

	mvn clean install

This will compile the source code, then run the unit tests, and finally build a jar file.

## Usage

In your maven project, add the dependency

```xml
<dependency>
    <groupId>be.yildiz-games</groupId>
    <artifactId>common-application</artifactId>
    <version>1.4.5</version>
</dependency>
```

## Contact
Owner of this repository: Grégory Van den Borre
