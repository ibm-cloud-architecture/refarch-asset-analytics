# Asset Injection as Function

## Table of Contents

* [Introduction](#introduction)
* [Pre-requisite](#pre-requisites)
* [Deploying the App](#deploying-the-app)
    + [On IBM Cloud Private](#ibm-cloud-private)
    + [Locally on Docker Edge](#docker-edge)
* [Validating the consumeAssetEvent](validating-the-consumeassetevent)
* [Compendium](#compendium)

## Introduction

This project aims to demonstrate how to develop a kafka listener in NodeJS as a function as a service which consumes asset events posted on a Kafka topic named `asset-topic`. This application in turn calls the [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) to persist the assets in [***Cassandra***](http://cassandra.apache.org/). The function is implemented in NodeJS and deployed to [kubeless](https://kubeless.io/).


## Pre-requisites

1. Install Kubeless CLI and Kubeless on your Kubernetes cluster.

You can find the installation steps [here](https://kubeless.io/docs/quick-start/)

Note: If you are installing Kubeless on IBM Private Cloud 3.1.0 or higher, run the below command after creating the namespace `kubeless` and before deploying the kubeless on your IBM Cloud Private Cluster.

```
$ git clone https://github.com/ibm-cloud-architecture/refarch-asset-analytics.git

$ cd refarch-asset-analytics

$ cd deployments

$ kubectl apply -f image_policy.yaml -n kubeless

$ cd ..

```

2. Install the serverless framework with the command.
```
$ npm install -g serverless
$ npm install -g serverless-kubeless
```

## Deploying the App

### IBM Cloud Private

To deploy the application on [IBM Cloud Private](https://www.ibm.com/cloud/private), please follow the below instructions.

#### Asset Manager Microservice

Before deploying the consume asset function, make sure [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) is up and running. This is important because `Consume Asset function` interacts with the `POST   /assetmanager/assets` API to post the Asset data that comes from the [Asset Event Producer](../asset-event-producer). To deploy the [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) on IBM Cloud Private, follow the instructions [here](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/blob/microprofile/docs/icp.md).

#### Deploying the Consume Asset function

Cosume Asset function is a [kubeless](https://kubeless.io/) function defined in NodeJS. To deploy this fuction, we need kubeless and serverless framework which can be installed as shown in the [Pre-requisite](#pre-requisite).

*How it works?*

- The Cosume Asset function is a Kafka listener defined in NodeJS.
- It consumes assets that are posted on Kafka topic `asset-topic` and these are posted by [Asset Event Producer](../asset-event-producer)
- Once the assets are consumed by this function, it hits the POST API `/assetmanager/assets` of [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) which in turn persists this data in [Cassandra](http://cassandra.apache.org/).

This function is deployed on Kubeless and you can install it following the instructions [here](https://kubeless.io/docs/quick-start/).

Clone the repository if you didnot do it before.

```
$ git clone https://github.com/ibm-cloud-architecture/refarch-asset-analytics.git
```

To deploy the function, follow the below steps.

```
$ cd refarch-asset-analytics

$ cd asset-consumer-function

$ kubeless function deploy consumeasset --runtime nodejs6 --from-file handler.js  --handler handler.consumeAssetEvent --namespace greencompute --env ASSET_URL=http://assetmanager-service:9080/assetmanager/assets --dependencies package.json
```

Once deployed you can access the logs using the below command.

```
$ kubeless function logs consumeasset -n greencompute
```

You can also see the list of kubeless functions as follows.

```
$ kubeless function list -n greencompute
NAME        	NAMESPACE   	HANDLER                  	RUNTIME	DEPENDENCIES               	STATUS
consumeasset	greencompute	handler.consumeAssetEvent	nodejs6	lodash: ^4.17.11           	1/1 READY
            	            	                         	       	mocha: ^5.2.0
            	            	                         	       	request: ^2.88.0
            	            	                         	       	serverless-kubeless: ^0.4.0
            	            	                         	       	sinon: ^7.1.0
            	            	                         	       	dotenv: ^6.1.0
```

#### Deploying the Kafka Cluster

You can find detailed instructions on how to deploy ZooKeeper [here](https://github.com/ibm-cloud-architecture/refarch-eda/blob/master/deployments/zookeeper/README.md) and Kafka [here](https://github.com/ibm-cloud-architecture/refarch-eda/blob/master/deployments/kafka/README.md)

To keep it simple, follow below steps.

```
$ git clone https://github.com/ibm-cloud-architecture/refarch-eda.git

$ cd refarch-eda

$ cd deployments/zookeeper

$ ./deployZookeeper.sh

$ cd ../kafka/

$ ./deployKafka.sh

$ cd ../..
```

**Note**: If you are deploying to IBM Cloud Private 3.1.0 or higher, make sure you deploed the image policy. If not, run the below command.

```
$ kubectl apply -f ./static/image_policy.yaml -n greencompute
```

This installs ZooKeeper and Kafka on your Docker Edge.

- Add the below entry in `/etc/hosts` 
```
<Your Host IP>       gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local
```

To test if your Kafka cluster is configured correctly, follow the instructions [here](https://github.com/ibm-cloud-architecture/refarch-eda/blob/master/deployments/kafka/README.md).

#### Creating Kafka trigger

To use the existing kafka cluster with kubeless, we need to deploy the below Kafka consumer and the Kafka Trigger CRD. Make sure `KAFKA_BROKERS` pointing to the right URL of your Kafka service.

Come back to `refarch-asset-analytics/asset-consumer-function` and deploy the trigger.

```
$ cd refarch-asset-analytics

$ cd asset-consumer-function

$ kubectl create -f KafkaTrigger/ConsumerAndTrigger.yml
```

Now let us create a Kafka topic `asset-topic` which the [Asset Event Producer](../asset-event-producer) and [Asset Consumer function](./) uses to produce and consume assets.

Run the below command to create `asset-topic`

```
$ kubeless trigger kafka create consumeasset --function-selector created-by=kubeless,function=consumeasset --trigger-topic asset-topic -n greencompute
```
You will then see something like below.

```
INFO[0000] Kafka trigger consumeasset created in namespace greencompute successfully!
```

To validate the function, see [Validating the consumeAssetEvent](validating-the-consumeassetevent)

To delete the function, run the below command.

```
$ kubeless function delete consumeasset --namespace greencompute
```

### Docker Edge

To deploy the application on Docker Edge, please follow the below instructions.

#### Asset Manager Microservice

Before deploying the consume asset function, make sure [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) is up and running. This is important because `Consume Asset function` interacts with the `POST   /assetmanager/assets` API to post the Asset data that comes from the [Asset Event Producer](../asset-event-producer). To deploy the [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) locally on Docker Edge, follow the instructions [here](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/blob/microprofile/docs/dockeredge.md).

#### Deploying the Consume Asset function

Cosume Asset function is a [kubeless](https://kubeless.io/) function defined in NodeJS. To deploy this fuction, we need kubeless and serverless framework which can be installed as shown in the [Pre-requisite](#pre-requisite).

*How it works?*

- The Cosume Asset function is a Kafka listener defined in NodeJS.
- It consumes assets that are posted on Kafka topic `asset-topic` and these are posted by [Asset Event Producer](../asset-event-producer)
- Once the assets are consumed by this function, it hits the POST API `/assetmanager/assets` of [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) which in turn persists this data in [Cassandra](http://cassandra.apache.org/).

This function is deployed on Kubeless and you can install it following the instructions [here](https://kubeless.io/docs/quick-start/).

Clone the repository if you didnot do it before.

```
$ git clone https://github.com/ibm-cloud-architecture/refarch-asset-analytics.git
```

To deploy the function, follow the below steps.

```
$ cd refarch-asset-analytics

$ cd asset-consumer-function

$ kubeless function deploy consumeasset --runtime nodejs6 --from-file handler.js  --handler handler.consumeAssetEvent --namespace greencompute --env ASSET_URL=http://assetmanager-service:9080/assetmanager/assets --dependencies package.json
```

Once deployed you can access the logs using the below command.

```
$ kubeless function logs consumeasset -n greencompute
```

You can also see the list of kubeless functions as follows.

```
$ kubeless function list -n greencompute
NAME        	NAMESPACE   	HANDLER                  	RUNTIME	DEPENDENCIES               	STATUS
consumeasset	greencompute	handler.consumeAssetEvent	nodejs6	lodash: ^4.17.11           	1/1 READY
            	            	                         	       	mocha: ^5.2.0
            	            	                         	       	request: ^2.88.0
            	            	                         	       	serverless-kubeless: ^0.4.0
            	            	                         	       	sinon: ^7.1.0
            	            	                         	       	dotenv: ^6.1.0
```

#### Deploying the Kafka Cluster

You can find detailed instructions on how to deploy ZooKeeper [here](https://github.com/ibm-cloud-architecture/refarch-eda/blob/master/deployments/zookeeper/README.md) and Kafka [here](https://github.com/ibm-cloud-architecture/refarch-eda/blob/master/deployments/kafka/README.md)

To keep it simple, follow below steps.

```
$ git clone https://github.com/ibm-cloud-architecture/refarch-eda.git

$ cd refarch-eda

$ cd deployments/zookeeper

$ ./deployZookeeper.sh

$ cd ../kafka/

$ ./deployKafka.sh

$ cd ../../..
```

This installs ZooKeeper and Kafka on your Docker Edge.

- Add the below entry in `/etc/hosts` 
```
127.0.0.1       gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local
```

To test if your Kafka cluster is configured correctly, follow the instructions [here](https://github.com/ibm-cloud-architecture/refarch-eda/blob/master/deployments/kafka/README.md).

#### Creating Kafka trigger

To use the existing kafka cluster with kubeless, we need to deploy the below Kafka consumer and the Kafka Trigger CRD. Make sure `KAFKA_BROKERS` pointing to the right URL of your Kafka service.

Come back to `refarch-asset-analytics/asset-consumer-function` and deploy the trigger.

```
$ cd refarch-asset-analytics

$ cd asset-consumer-function

$ kubectl create -f KafkaTrigger/ConsumerAndTrigger.yml
```

Now let us create a Kafka topic `asset-topic` which the [Asset Event Producer](../asset-event-producer) and [Asset Consumer function](./) uses to produce and consume assets.

Run the below command to create `asset-topic`

```
$ kubeless trigger kafka create consumeasset --function-selector created-by=kubeless,function=consumeasset --trigger-topic asset-topic -n greencompute
```
You will then see something like below.

```
INFO[0000] Kafka trigger consumeasset created in namespace greencompute successfully!
```

To validate the function, see [Validating the consumeAssetEvent](validating-the-consumeassetevent)

To delete the function, run the below command.

```
$ kubeless function delete consumeasset --namespace greencompute
```

## Validating the consumeAssetEvent

To validate the above function, you can send the asset data from the [Asset Event Producer](../asset-event-producer) and see if it is persisted in your Cassandra database.

Follow these steps to validate it.

```
$ cd refarch-asset-analytics

$ cd asset-event-producer

$ mvn compile

$ mvn exec:java@Simulator
```

Once you do this, you see some assets being sent by the producer as follows.

```
11:44:59.974 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Pump Simulator sending 5 new asset event to gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local:32224 every 10000 ms
11:44:59.974 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Kafka server: gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local:32224
11:45:00.019 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Send Asset: {"id":"1c0ab94c-98be-4f36-a692-170d8d30678f","os":"Raspbian","version":"0.0.1","type":"ESP","ipAddress":"172.16.0.0","antivirus":"v2.3","rotation":286,"current":105,"pressure":344,"flowRate":78,"temperature":137,"riskRating":-1,"latitude":"30.307182","longitude":"-97.755996","creationDate":"2018-11-14T11:44:59-06"}
11:45:00.186 [kafka-producer-network-thread | producer-1] INFO  org.apache.kafka.clients.Metadata - Cluster ID: wmOHkRLoSn250JCzStviYA
11:45:00.230 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Receive partition id= 0 offset= 0
11:45:01.235 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Send Asset: {"id":"003359e0-fc48-4c70-afc8-c8f64d6436e0","os":"Raspbian","version":"0.0.1","type":"ESP","ipAddress":"172.16.0.1","antivirus":"v2.3","rotation":176,"current":13,"pressure":110,"flowRate":4,"temperature":7,"riskRating":-1,"latitude":"30.307182","longitude":"-97.755996","creationDate":"2018-11-14T11:45:01-06"}
11:45:01.241 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Receive partition id= 0 offset= 1
11:45:02.244 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Send Asset: {"id":"69385bda-443f-4c94-b56c-a2795ca52dec","os":"Raspbian","version":"0.0.1","type":"ESP","ipAddress":"172.16.0.2","antivirus":"v2.3","rotation":290,"current":114,"pressure":979,"flowRate":3,"temperature":241,"riskRating":-1,"latitude":"30.307182","longitude":"-97.755996","creationDate":"2018-11-14T11:45:02-06"}
11:45:02.250 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Receive partition id= 0 offset= 2
11:45:03.254 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Send Asset: {"id":"6a01ee3f-f264-4a0c-91a3-42c5a88f1f3a","os":"Raspbian","version":"0.0.1","type":"ESP","ipAddress":"172.16.0.3","antivirus":"v2.3","rotation":29,"current":74,"pressure":737,"flowRate":23,"temperature":207,"riskRating":-1,"latitude":"30.307182","longitude":"-97.755996","creationDate":"2018-11-14T11:45:03-06"}
11:45:03.259 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Receive partition id= 0 offset= 3
11:45:04.264 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Send Asset: {"id":"0f241c72-9076-4fd7-a76f-133c9fa6dc7e","os":"Raspbian","version":"0.0.1","type":"ESP","ipAddress":"172.16.0.4","antivirus":"v2.3","rotation":53,"current":101,"pressure":446,"flowRate":2,"temperature":70,"riskRating":-1,"latitude":"30.307182","longitude":"-97.755996","creationDate":"2018-11-14T11:45:04-06"}
11:45:04.269 [ibm.cte.esp.PumpSimulator.main()] INFO  PumpSimulator - Receive partition id= 0 offset= 4
11:45:05.270 [ibm.cte.esp.PumpSimulator.main()] INFO  o.a.k.clients.producer.KafkaProducer - [Producer clientId=producer-1] Closing the Kafka producer with timeoutMillis = 9223372036854775807 ms.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 11.820 s
[INFO] Finished at: 2018-11-14T11:45:05-06:00
[INFO] Final Memory: 11M/183M
[INFO] ------------------------------------------------------------------------
```

Now, see if these assets are persisted in your database.

```
$ kubectl exec -ti cassandra-0 cqlsh -n greencompute
```

Enter the below commands

```
cqlsh> use assetmonitoring ;
cqlsh:assetmonitoring> select * from assets;
```

You see the above assets in your database.

## Compendium
* [Deploy Kubeless to Kubernetes cluster](https://kubeless.io/docs/quick-start/)
* [Use an existing Kafka cluster with Kubeless](https://kubeless.io/docs/use-existing-kafka/)
* [Kubeless - Quick Start](https://serverless.com/framework/docs/providers/kubeless/guide/quick-start/)
* [Serverless framework](https://www.npmjs.com/package/serverless)
* [Serverless kubeless template](https://medium.com/bitnami-perspectives/deploying-a-kubeless-function-using-serverless-templates-2d03f49b70e2)
* [IBM Cloud Private version 3.1.0 Helm instructions](https://www.ibm.com/support/knowledgecenter/SSBS6K_3.1.0/app_center/create_helm_cli.html)
* [IBM Cloud Private CLI tools](https://www.ibm.com/support/knowledgecenter/SSBS6K_3.1.0/manage_cluster/cli_commands.html)
