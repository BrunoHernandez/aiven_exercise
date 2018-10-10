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

### Producer

The producer program must be configured by editing the file:

    producer.properties

The configuration values are hardcoded to more or less arbitrary values, except
for the bootstrap.servers (introduce URL of Aiven service) and the security
settings: location of truststore, location of keystore and passwords.

TODO: This installation would be secured by a private key and the passwords
should be encrypted.

Start a producer instance from the developer host by running:

    ./producer_start.sh

The producer application has arguments which can be set or left empty (use
default values). The values for the arguments for current run will be listed
after starting the program.

## Content

- This directory contains this README file and various utility scripts to build
  and run the example.
- The directory "java" contains the Maven project with Java source code.
