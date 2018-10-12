# Event Producers
A set of Kafka Consumers to test Kafka deployment and support the Asset analytics solution.

Update 10/11/2018 - *Author: [Jerome Boyer](https://www.linkedin.com/in/jeromeboyer/)*  

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
## Basic pub/subscribe
The Java code to send text messages and consume them with remote kafka broker is under the `src/main/java/` classes `ibm.cte.kafka.play.SimpleConsumer` and `ibm.cte.kafka.play.SimpleProducer`


## Pump Simulator
The Pump simulator can generate two events:
* when new asset added to the grid of devices
* give new asset measurements every n seconds

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
The alternate is to use a Java to JSON serializer and send a String. We prefer to use this method as it is simpler.
```
GsonBuilder builder = new GsonBuilder();
Gson gson = builder.create();
String s = gson.toJson(a);
ProducerRecord<String, Object> record = new ProducerRecord<>(topic, a.getId(), s);
kafkaProducer.send(record);
```
