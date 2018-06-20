# Manufacturing Asset Analytics


This project is part of the 'IBM Hybrid Analytics and Big Data Architecture' solution, available at https://github.com/ibm-cloud-architecture/refarch-analytics. This project presents an end to end solution to enable predictive maintenance capabilities on manufacturing assets.
The problem space is related to continuous operation and service on manufacturing asset like [Electrical Submersible Pump](), but any assets with sensors can be on-boarded.

The use case is also adaptable and the architecture, solution components can be used for a security treat analysis on a park of devices or assets connected to a company intranet: real time events come from device that need to be aggregated and correlated and analytics run can be performed on historical data to assess security risk. Alert can be published to dashboard user interface.      

## Table of content
* [Use case](#use-case)
* [System Context](#system-context) to present the solution components
* [Future reading](#compendium)

## Use Case
## System Context
The processing starts by the continuous event flow emitted by a set of monitored devices. The event platform offers pub-subs capabilities and flow processing to aggregate and correlate events

![](docs/system-ctx.png)

The following diagram illustrates the IBM Cloud Private, kubernetes deployment we are doing in this solution:

![](docs/icp-deployment.png)

# Deployment
## Deploying Cassandra
There is no Cassandra helm chart currently delivered with ICP Helm catalog. We are using volume, service and statefuleset deployment files from the `deployments/cassandra` folder and the instructions are [here](./docs/cassandra.md)


## ICP Deployment



* Asset event consumer



# Compendium
https://blog.griddynamics.com/in-stream-processing-service-blueprint/
