# Asset Manager Microservice
SpringBoot app to support operation for Asset management.

### Features:
* Read assets from Cassandra
* Search asset
* Expose REST api


## Build
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
