# Event Producers
A set of Kafka Consumers to test Kafka deployment and support the Asset analytics solution via a pump simulator.

Update 10/19/2018 - *Author: [Jerome Boyer](https://www.linkedin.com/in/jeromeboyer/)*  

## What you will learn
In this project we are presenting the following items:
* [How to integrate with a Kafka Broker running on ICP]
* [How to develop a kafka stream application using the stream API and stateful aggregation]()
* Use SpringBoot app to support creating different event to publish to Kafka Topics.

## Features:
* Publish asset creation event: an event to identify a new asset added to the scope of the monitoring platform
*

## Kafka
You can run kafka locally using docker-compose or by deploying it to Kubernetes on docker desktop. See the note [here](https://github.com/ibm-cloud-architecture/refarch-analytics/tree/master/docs/kafka) or to a remote cluster running on Kubernetes like [IBM Cloud Private](https://github.com/ibm-cloud-architecture/refarch-analytics/blob/master/docs/kafka/readme.md#install-zookeeper-kafka-on-icp) or IBM Cloud Container Service.

## Kafka useful APIs
Here is a list of common API to use in your producer and consumer code.

* [KafkaProducer](https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/producer/KafkaProducer.html) A Kafka client that publishes records to the Kafka cluster.  The send method is asynchronous.
* [ProducerRecord](https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/producer/ProducerRecord.html) to be published to a topic
* [RecordMetadata](https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/producer/RecordMetadata.html) metadata for a record that has been acknowledged by the server.
* [KafkaConsumer](https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/consumer/KafkaConsumer.html) a topic consumer which support:
  * transparently handles brokers failure
  * transparently adapt to partition migration within the cluster
  * support grouping for load balancing among consumers
  * maintains TCP connections to the necessary brokers to fetch data
  * subscribe to multiple topics and being part of consumer groups
  * each partition is assigned to exactly one consumer in the group
  * if a process fails, the partitions assigned to it will be reassigned to other consumers in the same group
* [ConsumerRecords](https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/consumer/ConsumerRecords.html) holds the list ConsumerRecord per partition for a particular topic.
* [ConsumerRecord](https://kafka.apache.org/11/javadoc/org/apache/kafka/clients/consumer/ConsumerRecord.html) A key/value pair to be received from Kafka. This also consists of a topic name and a partition number from which the record is being received, an offset that points to the record in a Kafka partition, and a timestamp

# Code example
## Basic text message pub/subscribe
We are providing two simple classes to test Kafka deployment using the Kafka client API. Under the `src/main/java/` folder the Java code to send text messages is `ibm.cte.kafka.play.SimpleConsumer` and the one to consume them is `ibm.cte.kafka.play.SimpleProducer`.

The code to send few line of text is simple:
```java
Producer producer =  new Producer(brokers,topic);
for (String line : textToSend) {
	  RecordMetadata recordMetadata= producer.produce(line);
	  System.out.println(" -> sent" + recordMetadata.offset());
}
```

Be sure to have access to Kafka cluster / broker servers.
* Modify the brokers IP address and port number in the file `config\config.properties`
* To build the maven `mvn compile` will compile the code.
* To start the consumer: `mvn exec:java@text-consumer`
* To start the producer: `mvn exec:java@text-producer`

The producer trace should look like:
```
this is the first line11:16:20.755 [kafka-producer-network-thread | producer-1] INFO  org.apache.kafka.clients.Metadata - Cluster ID: 4qlnD1e-S8ONpOkIOGE8mg
 -> sent0
and we like the second line too -> sent1
no at least the bye bye. line -> sent2
11:16:22.866 [ibm.cte.kafka.play.SimpleProducer.main()] INFO  o.a.k.clients.producer.KafkaProducer - [Producer clientId=producer-1] Closing the Kafka producer with timeoutMillis = 9223372036854775807 ms.
11:16:22.871 [ibm.cte.kafka.play.SimpleProducer.main()] INFO  o.a.k.clients.producer.KafkaProducer - [Producer clientId=producer-1] Closing the Kafka producer with timeoutMillis = 9223372036854775807 ms.
```

The consumer trace should have something like:
```
11:15:30.360 [ibm.cte.kafka.play.SimpleConsumer.main()] INFO  o.a.k.c.consumer.internals.Fetcher - [Consumer clientId=consumer-1, groupId=49627d3d-2cea-4a40-a0c6-9158f14ce482] Resetting offset for partition text-topic-0 to offset 0.
Received :ConsumerRecord(topic = text-topic, partition = 0, offset = 0, CreateTime = 1539972980758, serialized key size = -1, serialized value size = 22, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = this is the first line)
Received :ConsumerRecord(topic = text-topic, partition = 0, offset = 1, CreateTime = 1539972982174, serialized key size = -1, serialized value size = 31, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = and we like the second line too)
Received :ConsumerRecord(topic = text-topic, partition = 0, offset = 2, CreateTime = 1539972982589, serialized key size = -1, serialized value size = 29, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = no at least the bye bye. line)
```

## Pump Simulator
The Pump simulator can generate two types of events:
* when new asset is added to the grid of devices
* give asset measurements every n seconds following different pattern

Be sure to have access to Kafka cluster / broker servers.
* Modify the brokers IP address and port number in the file `config\config.properties`
* To build the maven `mvn compile` will compile the code.

To send Java object like Asset to a Kafka topic we may need to implement a serializer. Here is an example of such serializer.
```java
public class AssetSerializer implements Serializer<Asset> {}
  @Override
	public byte[] serialize(String s, Asset a) {
		try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(a);
            oos.close();
            byte[] b = baos.toByteArray();
            return b;
        } catch (IOException e) {
            return new byte[0];
        }
	}
}
```
The alternate is to use a Java to JSON serializer and send a String.
```
GsonBuilder builder = new GsonBuilder();
Gson gson = builder.create();
String s = gson.toJson(a);
ProducerRecord<String, Object> record = new ProducerRecord<>(topic, a.getId(), s);
kafkaProducer.send(record);
```
