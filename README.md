# Manufacturing Asset Predictive Maintenance

This project is part of the 'IBM Hybrid Analytics and Big Data Architecture' reference architecture implementation, available at https://github.com/ibm-cloud-architecture/refarch-analytics. This set of projects presents an end to end solution to enable predictive maintenance capabilities on manufacturing assets.
The problem space is related to continuous operation and service on manufacturing asset like [Electrical Submersible Pump](https://en.wikipedia.org/wiki/Submersible_pump), but any assets with sensors can be considered with the same of solution.

The use case is also adaptable and the architecture, solution components can be used for a security treat analysis on a park of devices or assets connected to a company intranet: real time events come from device that need to be aggregated and correlated and analytics run can be performed on historical data to assess security risk. Alert can be published to dashboard user interface.  

We are presenting best practices around data management, real team streaming, cassandra high availability, microservices and serverless implementation.    

## Table of contents
<details><summary><h2> </h2></summary>
<ul>
<li><a href="#use-case">Use case</a></li>
<li><a href="#system-context">System Context</a> to present the solution components</li>
<li><a href="#deployment">Deployment to kubernetes cluster like IBM Cloud Private</a></li>
<li>Related sub projects - repositories </li>
<ul>
<li><a href="asset-consumer/README.md">Event consumers</a></li>
<li><a href="asset-event-producer/README.md">Event producers simulator</a> to simulate pump events for demonstration purpose.</li>
<li><a href="asset-dashboard-ui/README.md">Angular 6 user interface to present dashboard</a> to present a mix of static and real time data.</li>
<li><a href="asset-dashboard-bff/README.md">Dashboard BFF</a> to present a mix of static and real time data.</li>
<li><a href="https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice">Asset management microservice</a> used to expose REST api in front of Cassandra persistence</li>
</ul>
<li><a href="docs/demo.md">Demonstration script</a></li>
<li><a href="docs/analytics/README.md">Analytics model</a></li>
<li>Future readings</li>
<ul>
<li><a href="https://github.com/ibm-cloud-architecture/refarch-analytics/tree/master/docs/kafka">Kafka related</a></li>
<li><a href="docs/cassandra/readme.md">Cassandra</a></li>
<li><a href="https://github.com/ibm-cloud-architecture/refarch-integration/tree/master/docs/service-mesh">Microservice mesh</a></li>
</ul>
</ul>
</details>

## Use Case
### The challenge
The adoption of IoT, smart devices, in manufacturing industry brings opportunity to predict future maintenance on high-cost equipment before failure. The cost control for maintenance operation combined with optimizing device utilization are continuous challenges engineers are facing.  For IT architects, the challenge is to address how to prepare to support adopting artificial intelligence capacity to support new business opportunities? How to support real-time data analytics at scale combined with big data and microservice architecture?

### The solution
A set of geographically distributed electrical submersible pumps (can apply to any manufacturing IoT equipment) are sending a data stream about important physical measurements that need to be processed in real-time and persisted in big data storage. By adding traditional analytics combined with unstructured data as field engineer's reports, it is possible to build a solution that delivers a risk of failure ratio, in real time, from measurements received.

The solution combines key performance indicators aggregation, real-time reporting to a web-based dashboard user interface component, risk scoring microservice, and big data sink used by data scientists to develop and tune analytics and machine learning models.  

Data are continuously persisted in a document oriented database, we selected [Cassandra](http://cassandra.apache.org/) as a data sink. The real time event processing is supported by [Kafka](http://kafka.apache.org/) and Kafka streaming. The microservices are done in Java, one in microprofile and one in Java. The data science work is done using [ICP for Data](https://www.ibm.com/analytics/cloud-private-for-data) and data science experience.

### The Benefits
The solution gives visibility to analysts and executive about the real time status of the devices in the grids, with an aggregate view of the ones at risk. The device operation was increased by 15% and the unpredictable failure rate decreased by 85%. The model added diagnostic capabilities to help field engineers to deliver better maintenance.

## System Context
The processing starts by the continuous event flow emitted by a set of monitored devices. The event platform offers pub-subs capabilities and flow processing to aggregate and correlate events so dash board monitoring can be implement on the stateful operators. Data sink are used to keep Asset information, physical measurements over time and maintenance reports. Risk scoring service is deployed as a REST operation, build from a model developed by analytics and machine learning capabilities. Assets are exposed via microservice.

![](docs/system-ctx.png)

1. The application logic is split between the backend for frontend component, and the different microservice or streaming operators. The BFF is a web app exposing a user interface and the business logic to serve end users. For example when a new device or pump is added to the grid, a record is pushed to the user interface. All the real time metrics per device are also visible on the user interface. The supporting project is [the Dashboard BFF](asset-dashboard-bff/README.md)
1. The user interface is Angular 6 and the dash board look like the following wireframe:
![](docs/dashboard-wireframe.png)  
and the project is under the [asset-dashboard-ui](./asset-dashboard-ui) folder.
1. Manage CRUD operation on the assets. See [the Asset manager microservice code.](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice)
1. Pump Simulator is a java program running on developer laptop or on a server but external to ICP. The approach is to address communication to brokers deployed on ICP. The code and guidance are in the [asset-event-producer project](https://github.com/ibm-cloud-architecture/refarch-asset-analytics/tree/master/asset-event-producer#pump-simulator).

The following diagram illustrates the IBM Cloud Private, kubernetes deployment we are using in this solution. You will find the same components as in the system context above, with added elements for data management and data scientists using [ICP for Data](https://www.ibm.com/analytics/cloud-private-for-data).

![](docs/icp-deployment.png)

## Deployment
We propose two deployments: one for quick validation in a development laptop (tested on Mac) and one on IBM Cloud Private cluster.
### Pre-requisites
* Clone this project to get all the kubernetes deployment files and source code of the different components.
* Clone the [asset management microservice](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice) implementation
* Clone the master project ([Reference Architecture Analytics](https://github.com/ibm-cloud-architecture/refarch-analytics))to get Zookeeper and Kafka deployment manifests.
* Access to a kubernetes deployment for development, for example on Mac, we use [Docker Edge](https://docs.docker.com/docker-for-mac/install/#download-docker-for-mac) distribution. You can use [this article](https://rominirani.com/tutorial-getting-started-with-kubernetes-with-docker-on-mac-7f58467203fd) to install Docker Edge and enable kubernetes.

For test and 'production' deployment you need to have access to a kubernetes cluster like IBM Cloud Private.
* Login to your cluster
We are providing a script `./scripts/validateConfig.sh` to help you assessing the dependencies, and create the 'greencompute' namespace if it does not exist.

### Install Zookeeper for development
The zookeeper manifests are defined in the project [refarch-analytics](https://github.com/ibm-cloud-architecture/refarch-asset-analytics) under the `deployments/zookeeper/dev` folder. We are using our own docker images and the Dockerfile to build this image is in `deployments/zookeeper`. The image is already pushed to the Docker Hub under ibmcase account.

We are providing a script to install zookeeper as a kubernetes Deployment. Run the following command:
```
$ pwd
> ...../refarch-analytics/deployments/zookeeper
$ ./deployZoopeeker.sh

$ kubectl get pods -n greencompute
NAME                            READY     STATUS    RESTARTS   AGE
gc-zookeeper-57dc5679bb-bh29q   1/1       Running   0          1m
```

Once installed you do not need to reinstall it. We are also delivering a script to remove zookeeper when you are done using it. (./removeZookeeper.sh)

### Install Kafka for development
For kafka the manifests are in the same [project](https://github.com/ibm-cloud-architecture/refarch-asset-analytics) under the `deployments/kafka/dev` folder. We are using the google image: `gcr.io/google_samples/k8skafka:v1`.

We are also providing the same type of script to deploy Kafka:
```
$ pwd
> ...../refarch-analytics/deployments/kafka
$ ./deployKafka.sh
$ kubectl get pods -n greencompute
NAME                            READY     STATUS    RESTARTS   AGE
gc-kafka-0                      1/1       Running   0          2m
gc-zookeeper-57dc5679bb-bh29q   1/1       Running   0          10m
```

### Verify Kafka is connected to zookeeper
Connect to kafka container and use the scripts inside kafka bin folder:

```
$ kubectl exec  -ti gc-kafka-0 /bin/bash -n greencompute
kafka@gc-kafka-0:/$ cd /opt/kafka/bin
kafka@gc-kafka-0:/$./kafka-topics.sh --create  --zookeeper gc-client-zookeeper-svc.greencompute.svc.cluster.local:2181 --replication-factor 1 --partitions 1 --topic text-topic
```
This previous command create a `text-topic` and to assess existing topics use:
```
kafka@gc-kafka-0:/$./kafka-topics.sh --list --zookeeper gc-client-zookeeper-svc.greencompute.svc.cluster.local:2181
```

The URL of the zookeeper match the hostname defined when deploying zookeeper service:
```
$ kubectl describe svc gc-client-zookeeper-svc
```

### Verify pub/sub works with text messages
Two scripts exist in the `scripts` folder that are using kafkacat tool. You need to add the following in your hostname resolution configuration (DSN or /etc/hosts), matching you ip adress of your laptop.
```
192.168.1.89 gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local
```

Start the consumer in a terminal window
```
$ ./scripts/consumetext.sh
```
And start the producer in a second terminal:
```
$ ./script/producetext.sh
```
You should see the text:
```
try to send some text
to the text-topic
Let see...
% Reached end of topic text-topic [0] at offset 3
```

### Deploy Cassandra
The manifests for cassandra are in this project under the `deployment/cassandra` folder. One script is also delivered to deploy automatically Cassandra for development purpose.
```
$ ./scripts/deployCassandra.sh

$ kubectl describe statefulset cassandra -n greencompute
$ kubectl get pods -n greencompute
```
We also recommend to be familiar with [this kubernetes tutorial on how to deploy Cassandra with Stateful Sets](https://kubernetes.io/docs/tutorials/stateful-application/cassandra/).

### Deploy the solution
The steps are not yet automated:
* We need to deploy the asset manager microservice first. 

### ICP Deployment
We want within this project to dig into the detail of workload deployment and address resiliency for each of those components. The diagram below presents the deployment of runtime components as well as Zookeeper, Kafka and Cassandra clusters deployment inside k8s:

![](docs/asset-sol-k8s-depl.png)

* For high availability we need three masters, three proxies, 3 managers and at least 6 workers.
* Cassandra is deployed with 3 replicas and uses NFS based persistence volume so leverage shareable filesystems.
* Kafka is deployed with 3 replicas with anti affinity to avoid to have two pods on same node and also on the same node as Zookeeper's ones.
* Zookeeper is deployed with 3 replicas with anti affinity to avoid to have two pods on same node and on the same node as Kafka.   
This constraint explains the 6 workers.
* The component of the solution are deployed with at least 3 replicas: Asset manager microservice, dashboard BFF, and asset consumer/cassandra-injector.

* Get the admin security token and then use it in the set-credentials command below:

```
kubectl config set-cluster green-cluster --server=https://169.47.77.137:8001 --insecure-skip-tls-verify=true
kubectl config set-context green-cluster-context --cluster=green-cluster
kubectl config set-credentials admin --token=eyJ0...Ptg
kubectl config set-context green-cluster-context --user=admin --namespace=greencompute
kubectl config use-context green-cluster-context
```
We have added a script to support those commands so, once you run the script, just getting the security token for the admin user should be enough. See script named `scripts/connectToCluster.sh`


### Deploying Cassandra
There is no Cassandra helm chart currently delivered with ICP Helm catalog. We are using volume, service and statefuleset deployment files from the `deployments/cassandra` project folder and the installation instructions are [detailed here](./docs/cassandra/readme.md). In the readme, we also describe the potential architecture decisions around deploying Cassandra for high availability.

You can use our yaml files to deploy to ICP. (See the files under `deployments/cassandra` and instructions in [this section](https://github.com/ibm-cloud-architecture/refarch-asset-analytics/blob/master/docs/cassandra/readme.md#using-our-configurations))

When the pods are up and running use the [following commands](https://github.com/ibm-cloud-architecture/refarch-asset-analytics/blob/master/docs/cassandra/readme.md#define-assets-table-structure-with-cql) to create the needed keyspace and tables for the solution to run.

### Deploying Kafka
We are presenting different deployment models, all based on container: with docker, docker Edge with local kubernetes for your development environment, IBM Cloud Private for dev or staging. See details [in this note.](
  https://github.com/ibm-cloud-architecture/refarch-analytics/tree/master/docs/kafka#run-kafka-in-docker)

For ICP, the new [IBM Event Stream]() product is used, as it is built on top of Kafka and brings very nice capabilities.

### Solution Deployment on ICP
Each component of the solution is compiled and packaged as container. Here is the list of supported deployment:
* [Asset management microservice deployment](https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice)
* [Asset consumer and injector to Cassandra](asset-consumer/README.md#deploy)
* [Dashboard backend for frontend](asset-dashboard-bff/README.md#deploy)

Finally the [pump simulator](asset-event-producer/readme.md) is a standalone java application used to produce different type of event. It does not need to be deployed to kubernetes.

### Troubleshooting
As we are deploying different solutions into kubernetes we group [Troubleshooting notes here](https://github.com/ibm-cloud-architecture/refarch-integration/blob/master/docs/icp/troubleshooting.md) and [a technology summary here](https://jbcodeforce.github.io/#/studies)

## Contributors

* Lead development [Jerome Boyer](https://www.linkedin.com/in/jeromeboyer/)
* [Amaresh Rajasekharan for the data science part](https://www.linkedin.com/in/amaresh-rajasekharan/)
* [Hemankita Perabathini for the asset management microservice and ICP](https://www.linkedin.com/in/hemankita-perabathini/)
* [Zach Silverstein](https://www.linkedin.com/in/zsilverstein/)


Please [contact me](mailto:boyerje@us.ibm.com) for any questions.
