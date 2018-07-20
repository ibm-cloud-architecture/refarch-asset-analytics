# Asset Manager Microservice
This is a SpringBoot app to support CRUD operations and query to manage manufacturing asset. The persistence repository is Cassandra.

### Features:
* Asset management CRUD operations to Cassandra
* Search asset API
* Expose RESTful api

## Preparation
You need to have Cassandra deployed, we present multiple choices in [this note](../docs/cassandra.md) and the scripts to prepare the database (keyspace, tables...)

## Build

## Deploy

To deploy on ICP or a kubernetes cluster use the deployment yaml file under the `deployments` folder.

## Code explanations

### Cassandra persistence using Spring Cassandra Data
We are using Reactive interface for the BFF which is also supported by [Spring data for cassandra](https://docs.spring.io/spring-data/cassandra/docs/current/reference/html/)

The configuration class leverages annotation to inject parameters coming from cassandra.properties file
There are three mandatory settings we have to configure to setup the connection for a Cassandra client: contactPoints, port, and keyspace name.

#### More Readings

* [Introduction to Spring Data Cassandra](http://www.baeldung.com/spring-data-cassandra-tutorial)

## Build
To build
We use [Google Container Tool skaffold](https://github.com/GoogleContainerTools/skaffold) tool and docker build pipeline. The image we use is the bare minimum and includes:
* OpenJDK JRE
* Alpine linux layer
* Spring boot.
* our app.

It is important to keep image size small while deploying on k8s, as scheduler can move containers between nodes.

For the build we use java builder container to make our development environment portable using container. For the build we use Maven.
Here is the dockerfile for building
```

```

By default the Maven local repository, for official Maven images, is placed inside a Docker data volume. As we do not want to download dependencies at each build, we choose to mount maven repository into docker container.
```
```
## run

## Unit tests


## Some issues
* Unsatisfied dependency expressed through field 'mockMvc'; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'org.springframework.test.web.servlet.MockMvc' available
  Read this http://www.baeldung.com/spring-nosuchbeandefinitionexception.
  Can also be linked to the missing   <artifactId>spring-boot-starter-web</artifactId> in pom.xml

* Mokito misusing.MissingMethodInvocationException

## Java memory investigation
Here is a set of things you can do to investigate a java memory consumption in the context of docker container:
```
$ docker -v
$ docker exec javaappcontainername java -version
# use jdk 1.8.40+  Native Memory Tracker!
$ docker exec javaappcontainername jcmd 1 VM.native_memory summary
```
## Compendium
* [Crafting perfect Java Docker build flow](https://codefresh.io/docker-tutorial/java_docker_pipeline)
* [Java memory usage in a docker container](http://trustmeiamadeveloper.com/2016/03/18/where-is-my-memory-java/)
*
