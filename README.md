# Manufacturing Asset Analytics

This project is part of the 'IBM Hybrid Analytics and Big Data Architecture' solution, available at https://github.com/ibm-cloud-architecture/refarch-analytics. This project presents an end to end solution to enable predictive maintenance capabilities on manufacturing assets.
The problem space is related to continuous operation and service on manufacturing asset like [Electrical Submersible Pump](), but any assets with sensors can be on-boarded.

The use case is also adaptable and the architecture, solution components can be used for a security treat analysis on a park of devices or assets connected to a company intranet: real time events come from device that need to be aggregated and correlated and analytics run can be performed on historical data to assess security risk. Alert can be published to dashboard user interface.      

## Table of content
* [Use case](#use-case)
* [System Context](#system-context) to present the solution components
* **Sub projects**
  * [Event consumers](asset-consumer/README.md)
  * [Event producers simulator](asset-event-producer/README.md)
  * [Dashboard BFF](asset-dashboard-bff/README.md)
  * [Asset manager microservice](asset-mgr-ms/README.md)
* **Future readings**
  * [Kafka related]()

## Use Case
A set of geographically distributed electrical submersible pumps (can apply to any manufacturing IoT equipments) send stream of data about important measurements.

## System Context
The processing starts by the continuous event flow emitted by a set of monitored devices. The event platform offers pub-subs capabilities and flow processing to aggregate and correlate events so dash board monitoring can be implement on the stateful operators.

![](docs/system-ctx.png)

1. The application logic and back end for front end is a web app exposing a user interface and the business logic to serve end users. For example when a new device or pump is added to the grid, a record is pushed to the user interface. The supporting project is [the Dashboard BFF](asset-dashboard-bff/README.md)
1. Manage CRUD operation on the assets. See [the Asset manager microservice code](asset-mgr-ms/README.md)

The following diagram illustrates the IBM Cloud Private, kubernetes deployment we are doing in this solution. You will find the same components as in the system context above, with added elements for data management and data scientists.

![](docs/icp-deployment.png)

# Deployment
## Deploying Cassandra
There is no Cassandra helm chart currently delivered with ICP Helm catalog. We are using volume, service and statefuleset deployment files from the `deployments/cassandra` folder and the instructions are [here](./docs/cassandra.md)


## ICP Deployment



* Asset event consumer
