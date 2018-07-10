# Manufacturing Asset Predictive Maintenance

This project is part of the 'IBM Hybrid Analytics and Big Data Architecture' solution, available at https://github.com/ibm-cloud-architecture/refarch-analytics. This project presents an end to end solution to enable predictive maintenance capabilities on manufacturing assets.
The problem space is related to continuous operation and service on manufacturing asset like [Electrical Submersible Pump](https://en.wikipedia.org/wiki/Submersible_pump), but any assets with sensors can be considered with the same of solution.

The use case is also adaptable and the architecture, solution components can be used for a security treat analysis on a park of devices or assets connected to a company intranet: real time events come from device that need to be aggregated and correlated and analytics run can be performed on historical data to assess security risk. Alert can be published to dashboard user interface.      

## Table of content
* [Use case](#use-case)
* [System Context](#system-context) to present the solution components
* [Deployment](#deployment)
* **Sub projects**
  * [Event consumers](asset-consumer/README.md)
  * [Event producers simulator](asset-event-producer/README.md) to simulate pump events for demonstration purpose.
  * [Dashboard BFF](asset-dashboard-bff/README.md) to present a mix of static and real time data.
  * [Asset manager microservice](asset-mgr-ms/README.md) to export RESTful API to manage Asset CRUD operations
* **[Demonstration script](docs/demo.md)**
* **Future readings**
  * [Kafka related](https://github.com/ibm-cloud-architecture/refarch-analytics/tree/master/docs/kafka)

## Use Case
A set of geographically distributed electrical submersible pumps (can apply to any manufacturing IoT equipments) send stream of data about important measurements, the solution aggregates key performance indicators to present in real time to a dashboard. Data are continuously persisted in a document oriented database, we selected [Cassandra](http://cassandra.apache.org/). The event processing is supported by [Kafka](http://cassandra.apache.org/) and Kafka streaming.

## System Context
The processing starts by the continuous event flow emitted by a set of monitored devices. The event platform offers pub-subs capabilities and flow processing to aggregate and correlate events so dash board monitoring can be implement on the stateful operators.

![](docs/system-ctx.png)

1. The application logic and back end for front end is a web app exposing a user interface and the business logic to serve end users. For example when a new device or pump is added to the grid, a record is pushed to the user interface. The supporting project is [the Dashboard BFF](asset-dashboard-bff/README.md)
1. Manage CRUD operation on the assets. See [the Asset manager microservice code](asset-mgr-ms/README.md)

The following diagram illustrates the IBM Cloud Private, kubernetes deployment we are doing in this solution. You will find the same components as in the system context above, with added elements for data management and data scientists.

![](docs/icp-deployment.png)

# Deployment
## Pre-requisites
You need to have access to a kubernetes cluster like IBM Cloud Private. We are providing scripts to help you validate the prerequisites.

* If not done already create a namespace named `greencompute`
* Get the admin security token and then use it in the set-credentials command below:

```
kubectl config set-cluster green-cluster --server=https://169.47.77.137:8001 --insecure-skip-tls-verify=true
kubectl config set-context green-cluster-context --cluster=green-cluster
kubectl config set-credentials admin --token=eyJ0...Ptg
kubectl config set-context green-cluster-context --user=admin --namespace=greencompute
kubectl config use-context green-cluster-context
```

## Deploying Cassandra
There is no Cassandra helm chart currently delivered with ICP Helm catalog. We are using volume, service and statefuleset deployment files from the `deployments/cassandra` folder and the instructions are [here](./docs/cassandra.md) as well as architecture discussions around deploying Cassandra for high availability.

## Deploying Kafka
We use different deployment model, all based on container: with docker, docker edge with local kubernetes, IBM Cloud Private. See details [in this note](
  https://github.com/ibm-cloud-architecture/refarch-analytics/tree/master/docs/kafka#run-kafka-in-docker)

## Solution Deployment on ICP
Each component of the solution is compiled and packaged as container. Here is the list of supported deployment:
* [Asset management microservice deployment](asset-mgr-ms/README.md#deploy)
* [Asset consumer and injector to Cassandra](asset-consumer/README.md#deploy)



* Asset event consumer
