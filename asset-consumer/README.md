# Kafka Consumers
This project includes a set of standalone executable java classes or nodejs app, to consume records from a Kafka Topics and do some specific processing on the events.

* [Simple Event Print](#asset-event-print)
* [Asset injector using kafka and cassandra API](#asset_injector)
* [Serverless Asset injector](#) it is listening to Kafka topic for new event and use HTTP client to post asset to the asset manager microservice.
* [Springboot ]



## Asset Event print
The class in src/main/java ibm.cte.esp.AssetEventSimplestConsumer get the AssetEvent from the asset-topic as a Kafka consumer record and parse the json object from the value string and print it to stdout. This code loop forever and get all the events publish in the topic. This simple code uses [org.apache.kafka.clients.consumer.KafkaConsumer](https://kafka.apache.org/10/javadoc/?org/apache/kafka/clients/consumer/KafkaConsumer.html) class.
The code use the config/config.properties to access remote Kafka brokers. You need to be sure to resolve the hostname (gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local) to the IP address of the kubernetes proxy, and the port (32224) is visible.

### Build and run
The maven pom defines an execution id so build and run are simple:
```
$ mvn compile
$ mvn exec:java@AssetPrint
```

## Asset Injector
The `AssetInjector` class is executable as a standalone java class and its main goal is to listen to Asset events and save them into Cassandra cluster. It is using Kafka api and Cassandra API.

The AssetInjector is packaged as container (See [Dockerfile in this project](./Dockerfile) and deployable to IBM Cloud private. We have dropped this deployment for a serverless approach see [next section]()

The following diagram illustrates how the 'Asset injector' consumes `new asset event` from kafka and persists data into the cassandra `Assets` table.

![](docs/new-asset-event-cassandra.png)

For the BFF layer to Web browser real time push pattern see [the asset dashboard BFF project](../asset-dashboard-bff)

### Code Explanation
The `ibm.cte.esp.AssetInjector.java` is a POJO which uses Kafka consumer API and Cassandra persistence API. It does three things:
1. read configuration from external properties file
1. create a cassandra DAO to persist assets to cassandra
1. start a kafka consumer to get new asset event. Loop for ever.

To externalize parameters, we use the `conf/config.properties` file which will be mapped to a ConfigMap when deployed to kubernetes cluster.

For logging, as most of the APIs used ware using [slf4j](https://www.slf4j.org) and the default implementation of [logback](https://logback.qos.ch/), we added into `src/main/resource` a logback.xml configuration file to control the logging level as the DEBUG level is too low.


#### Using Kafka client API
The code is using the KafkaConsumer class and the ConsumerRecord to get the wrapper on the event. Our code implementation is exposing a `consume()`` method that is polling message every n ms.

The approach is simple:
* load properties
* create a consumer using the properties
```
kafkaConsumer = new KafkaConsumer<>(properties);
kafkaConsumer.subscribe(Arrays.asList(KAFKA_ASSET_TOPIC_NAME));
```
* expose a consume method to be used by the injector. This method poll data every n ms
```
ConsumerRecords<String, String> records = kafkaConsumer.poll(KAFKA_POLL_DURATION);
```
* the main class loop fore ever, and use the consume() method and process (save to cassandra) the n events received
```
public void run() {
  logger.info("########### Asset Injector START ##########");
      boolean runAgain = true;
      while (runAgain) {
         List<Asset> buffer = kafkaConsumer.consume();
        // commit offset only when persisted in DB.
        if (buffer.size() >= minBatchSize) {
          try {
            insertIntoDb(buffer);
            kafkaConsumer.commitOffset();
            buffer.clear();
          } catch (Exception e) {
            e.printStackTrace();
            runAgain = false;
          }
        }
      }
      kafkaConsumer.close();
}
```

#### Build and deployment
The code is packaged as docker container using the open jdk with Alpine linux image. The `scripts/build.sh` script uses maven and docker build. The `deployments/assetconsumer.yml` defines the Kubernetes deployment, configMap, and service.

The major trick is to externalize the config.properties to define kafka and cassandra parameters into a ConfigMap.

To deploy to your connected kubernetes cluster use the following steps:
```
#1: Tag the docker image with the name of the remote docker repository, the target namespace and the name and version of the image. For example for a remote repository running in IBM Cloud Private, use something like:
greencluster.icp:8500/ibmcase/asset-consumer:v0.0.1

#2: log to docker remote repo:
$ docker login greencluster.icp:8500

#3: docker push the newly created image to the remote repository:
$ docker push greencluster.icp:8500/ibmcase/casset-consumer:v0.0.1

#4: Change the image versioning in the yaml file and apply the changes
$ kubectl apply -f deployments/assetconsumer.yml
```

#### Offset management
When dealing with heavy load storing offset in zookeeper is non advisable. To manage offset we use the [new consumer API](https://kafka.apache.org/090/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html).
The code in `ibm.cte.esp.AssetInjector` class commits offset synchronously when a specified number of assets are read from the topic and the persistence to the back end succeed.

When designing a consumer the following requirements need to be analyzed:
* Do we need to have multiple consumers running in parallel to scale horizontally: this means having multiple partitions and use fine grained control over offset persistence. If there is not such need, the High Level Consumer approach can be used and it will commit offsets for all partitions.
* Is it possible to loose message from topic? if so, when a consumer restarts it will start consuming the topic from the end of the queue.
* Do the solution is fine with at-least-once delivery or exactly-once is a must have? As the operation to store a message and the storage of offsets are two separate operations, and in case of failure between them, it is possible to have stale offsets, which will introduce duplicate messages when consumers restart to process from last known committed offset. "exactly-once" means grouping record and offset persistence in an atomic operation.
* What are the criteria to consider a message as "consumed"?  

## Serverless asset injector
The class is AssetEventInjectorHandler under the ibm.cte.esp.faas.assets. It use the kubeless event and context to get event payload. 

## Springboot kafka consumer
We also did a second implementation by using Springboot kafka. As you can see in the `pom.xml` we are using spring boot starter, and starter-test which add libraries for Junit, and Mockito.

The consumer is packaged within container using the same open-jdk and alpine image, and can be all deployed to a kubernetes cluster like IBM Cloud Private.

See [this article](https://docs.spring.io/spring-kafka/reference/htmlsingle/#_receiving_messages) for detail on how to use springboot kafka


## Accessing remote cassandra deployed in kubernetes

When running on you development machine you can use a remote cassandra to test your application. Use the port-forwarding command to map local port to remote cassandra deployed pod.

```
# first get cassandra pod name
$ kubectl get pods --namespace greencompute | grep cassandra

# then get exposed node port
$ kubectl describe pod cassandra-0 --namespace greencompute

# then do local port forwarding to cassandra
$  kubectl port-forward cassandra-0 9042:9042 --namespace greencompute
```

 See also the Cassandra [article for preparing the environment](../docs/cassandra/readme.md) for the asset analytic.
