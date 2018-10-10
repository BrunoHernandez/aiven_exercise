# aiven_exercise

Exercise with Kafka messaging and Aiven database service.

## Dependencies

The dependencies can be checked running the script

    ./dependencies.sh

This example is meant to be run in the developer's machine. The developer
host should be a Linux machine and have installed at least a JDK and Maven.
Otherwise I delegate on Maven for installing the Java-related dependencies.

## Build

This project uses Maven as build tool. It can be build by simply running

    ./build.sh

or running Maven directly on the "java" directory, for example

    cd java && mvn package

The resulting applications should be available under

    java/<application>/target/<application>.jar

after Maven has successfully shown all disregard for incremental builds and
downloaded half of the Internet.

## Applications

### Configuration

The producer and consumer programs must be configured by editing the file:

    application.properties

Change the values for "bootstrap.servers" (introduce URL of Aiven service)
and the security settings: location of truststore, location of keystore and
passwords.

The database connection capability is configured by editing the file:

    postgresql.properties

Change the values for jdbc.url to your service's URL, also the rest of security
related values such as user, password and sslrootcert (path).

Otherwise, the configuration values are hardcoded to more or less arbitrary
values.

TODO: This installation could be secured by a private key and the passwords
should be encrypted.

### Usage

Start a producer instance from the developer host by running:

    ./start.sh producer

Start a consumer instance from the developer host by running:

    ./start.sh consumer

The producer and consumer applications have arguments which can be set or left
empty (use default values). The values used for current run will be listed
after starting the program.

## Content

- This directory contains this README file and various utility scripts to build
  and run the example.
- The directory "java" contains the Maven project with Java source code.
