# Kafka Consumers
This project includes a set of standalone code, to consume records from a Kafka Topics. Most of the running java classes are for testing purpose.
The AssetInjector class is executable as a standalone tool and its goal is  One of the class is used in the proposed asset analytics solution by listening to two types of record: a new asset is added, a new measurement event comes from an known asset and and persists those records into Cassandra for historical persistence and do analytics modeling.

This application is done using SpringBoot Kafka api.

### Features:
* Consume Asset event (new asset events, and measurement events) from Kafka topic
* Persist assets data to Cassandra cluster
* Expose REST api

## Code Explanation
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
