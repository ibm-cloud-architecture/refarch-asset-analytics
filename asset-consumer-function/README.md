# Asset Injection as Function

## Table of Contents

* [Introduction](#introduction)
* [Pre-requisite](#pre-requisite)
* [Deploying the App](#deploying-the-app)
    + [IBM Cloud Private](#ibm-cloud-private)
    + [Docker Edge](#docker-edge)
* [Compendium](#compendium)

## Introduction

This project is built to demonstrate how to build develop a kafka listener in NodeJS which consumes asset event posted on Kafka `asset-topic`. This application in turns call the [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) to persist the assets in Cassandra. The function is implemented in NodeJS and deployed to [kubeless](https://kubeless.io/).

- Based on NodeJS.
- Interacts with the Kafka and cosumes the assets.
- Posts the assets to [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) which in turn persists them in [***Cassandra***](http://cassandra.apache.org/).
- Deployment options for Docker Edge (local kubernetes cluster) and ICP.

## Pre-requisite

Install the serverless framework with the command. 
```
$ npm install -g serverless
$ npm install -g serverless-kubeless
```

## Deploying the App

### IBM Cloud Private

**TBD**

### Docker Edge

To deploy the application on Docker Edge, please follow the below instructions.

#### Asset Manager Microservice

Before deploying the consume asset function, make sure [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) is up and running. This is important because `Consume Asset function` interacts with the `POST   /assetmanager/assets` API to post the Asset data that comes from the [Asset Event Producer](../asset-event-producer). To deploy the [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) locally on Docker Edge, follow the instructions [here](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/blob/microprofile/docs/dockeredge.md).

#### Deploying the Consume Asset function

Cosume Asset function is a [kubeless](https://kubeless.io/) function defined in NodeJS. To deploy this fuction, we need serverless framework which can be installed as shown in the [Pre-requisite](#pre-requisite).

*How it works?*

- The Cosume Asset function is a Kafka listener defined in NodeJS.
- It consumes assets that are posted on Kafka topic `asset-topic` and these are posted by [Asset Event Producer](../asset-event-producer)
- Once the assets are consumed by this function, it hits the POST API `/assetmanager/assets` of [Asset Manager Microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile) which in turn persists this data in [Cassandra](http://cassandra.apache.org/). 

This function is deployed on Kubeless and you can install it following the instruction [here](https://kubeless.io/docs/quick-start/).

To deploy the function, follow the below steps.

```
$ git clone https://github.com/ibm-cloud-architecture/refarch-asset-analytics.git

$ cd refarch-asset-analytics

$ cd asset-consumer-function

$ npm install

$ serverless deploy function -f consumeasset
```

Once deployed you can access the logs using the below command.

```
$ serverless logs -f consumeasset -t
```

You can also see the list of kubeless functions as follows.

```
$ kubeless function list -n greencompute
NAME        	NAMESPACE   	HANDLER                  	RUNTIME	DEPENDENCIES               	STATUS
consumeasset	greencompute	handler.consumeAssetEvent	nodejs6	request: ^2.88.0           	1/1 READY
            	            	                         	       	serverless-kubeless: ^0.4.0
            	            	                         	       	sinon: ^7.1.0
            	            	                         	       	dotenv: ^6.1.0
            	            	                         	       	lodash: ^4.17.11
            	            	                         	       	mocha: ^5.2.0
```

#### Deploying the Kafka Cluster

You can find detailed instructions on how to deploy ZooKeeper [here](https://github.com/ibm-cloud-architecture/refarch-eda/blob/master/deployments/zookeeper/README.md) and Kafka [here](https://github.com/ibm-cloud-architecture/refarch-eda/blob/master/deployments/kafka/README.md)

To keep it simple, folloe below steps.

```
$ git clone https://github.com/ibm-cloud-architecture/refarch-eda.git

$ cd refarch-eda

$ ./deployments/zookeeper/deployZookeeper.sh

$ ./deployments/kafka/deployKafka.sh
```

This installs ZooKeeper and Kafka on your Docker Edge.

- Add the below entry in `/etc/hosts` 
```
127.0.0.1       gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local
```

To test if your Kafka cluster is configured correctly, follow the instructions [here](https://github.com/ibm-cloud-architecture/refarch-eda/blob/master/deployments/kafka/README.md)


## Compendium
* [Serverless kubeless template](https://medium.com/bitnami-perspectives/deploying-a-kubeless-function-using-serverless-templates-2d03f49b70e2)
* [Kubeless - Quick Start](https://serverless.com/framework/docs/providers/kubeless/guide/quick-start/)
