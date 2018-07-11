# Kafka Consumers
This project includes a set of standalone executable java classes, to consume records from a Kafka Topics and do some specific processing, one of them is to persist to Cassandra.
The AssetInjector class is executable as a standalone tool and its goal is to listen to Asset events and save them into Cassandra cluster. It is using Kafka api
Another class is used to compute asset analytics as part of a kafka streaming operator. It listens to a new measurement events coming from known assets and aggregate some metrics.

The AssetInjector is packaged as container and deployed to IBM Cloud private.

### Features:
* Consume Asset event (new asset events, and measurement events) from Kafka topic
* Persist assets data to Cassandra cluster
* Expose REST api

The following diagram illustrates how the 'Asset injector' consumes`new asset event` from kafka and persists data into the cassandra `Assets` table.

![](docs/new-asset-event-cassandra.png)

## Code Explanation
The AssetInjector.java is a POJO which uses Kafka consumer API and Cassandra persistence API.

### Using Kafka API

### Springboot kafka consumer
One of the deployable app is implemented using Springboot kafka. As you can see in the `pom.xml` we are using spring boot starter, and starter-test which add libraries for Junit, and Mockito.

The consumer is packaged within container and can be deployed to a kubernetes cluster like IBM Cloud Private.

### Offset management
When dealing with heavy load storing offset in zookeeper is non advisable. To manage offset we use the [new consumer API](https://kafka.apache.org/090/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html).
The code in `ibm.cte.esp.AssetInjector` class commits offset synchronously when a specified number of assets are read from the topic and the persistence to the back end succeed.

When designing a consumer the following requirements need to be analyzed:
* Do we need to have multiple consumers running in parallel to scale horizontally: this means having multiple partitions and use fine grained control over offset persistence. If there is not need the High Level Consumer approach can be used and it will commit offsets for all partitions.
* Is it possible to loose message from topic? When a consumer restarts it will start consuming the topic from the end of the queue.
* Do the solution is fine with at-least-once delivery or exactly-once is a must. As the operation to store a message and the stage of offsets are two separate operations, and in case of failure between them, it is possible to have stale offsets, which will introduce duplicate messages when consumers restart to process from last known committed offset. "exactly-once" means grouping record and offset persistence in an atomic operation.
* what are the criteria to consider a message consumed.  

### Cassandra persistence

#### Accessing remote cassandra deployed in kubernetes
 When running on you development machine you can use a remote cassandra to test your application. Use the port-forwarding command.

```
# first get cassandra pod name
$ kubectl get pods --namespace greencompute | grep cassandra

# then get exposed node port
$ kubectl describe pod cassandra-0 --namespace greencompute

# then do local port forwarding to cassandra
$  kubectl port-forward cassandra-0 9042:9042 --namespace greencompute
```

 See also the Cassandra [article for preparing the environment](../docs/cassandra.md) for the asset analytic.
