# Kafka Consumers
This project includes a set of standalone code, to consume records from a Kafka Topics. Most of the running java classes are for testing purpose. One of the class is used in the proposed asset analytics solution by listening to two types of record: a new asset is added, a new measurement event comes from an known asset and and persists those records into Cassandra for historical persistence and do analytics modeling.

This application is done using SpringBoot Kafka api.

### Features:
* Consume Asset from Kafka topic
* Persist asset to Cassandra
* Expose REST api


## Springboot
One of the deployable app is implemented using Springboot kafka. As you can see in the `pom.xml` we are using spring boot starter, and starter-test which add libraries for Junit, and Mockito.

 The consumer is packaged within container and can be deployed to a kubernetes cluster like IBM Cloud Private.
 

 ## Accessing remote cassandra deploy in kubernetes
 When running on you development machine you can use a remote cassandra to test your application. Use the port-forwarding command.
 
 ```
# first get cassandra pod name
$ kubectl get pods --namespace greencompute | grep cassandra
 
# then get exposed node port
$ kubectl describe pod cassandra-0 --namespace greencompute
 
# then do local port forwarding to cassandra
$  kubectl port-forward cassandra-0 9042:9042 --namespace greencompute
 ```
 See also the Cassandra [article for preparing the environment](../docs/cassandra.md) for the asset analytic. solution: 
 