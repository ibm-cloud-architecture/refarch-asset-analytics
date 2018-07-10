# Cassandra Summary
In this article we are presenting Cassandra integration within the solution, and how to support deployment to IBM Cloud private, addressing high availability and resiliency.

## Concepts
Here are some key concepts of Cassandra to keep in mind for this implementation:
* **Cluster**:  the set of nodes potentially deployed cross data centers, organized as a 'ring'.
* **Keyspace**: like a schema in SQL DB. It is the higher abstraction object to contain data. The important keyspace attributes are the Replication Factor, the Replica Placement Strategy and the Column Families.
* **Column Family**: they are like tables in Relational Databases. Each Column Family contains a collection of rows which are represented by a Map<RowKey, SortedMap<ColumnKey, ColumnValue>>. The key gives the ability to access related data together
* **Column** – A column is a data structure which contains a column name, a value and a timestamp. The columns and the number of columns in each row may vary in contrast with a relational database where data are well structured.

## Cassandra deployment
### Deploying on "Docker Edge for desktop" kubernetes
As pre-requisite you need Docker Edge and enable kubernetes (see this [note](https://docs.docker.com/docker-for-mac/kubernetes/)). Then you can use our script `deployCassandra.sh` under the `scripts` folder or run the following commands one by one:

* create cassandra service
```
$ kubectl create -f deployment/cassandra/cassandra-service.yaml --namespace greencompute
```
* Create one persistence volume to keep data for cassandra
```
$ kubectl create -f deployment/cassandra/cassandra-volumes.yaml
```
* Create the statefulSet
 ```
 $ kubectl create -f deployment/cassandra/cassandra-statefulset.yaml

 # Verify the installation
 $ kubectl exec -ti  cassandra-0 -- nodetool status
 ```

### Deployment on ICP
Deploying stateful distributed applications like Cassandra is not easy. You can leverage the kubernetes cluster and deploy c7a to the worker nodes.

![](cassandra-k8s.png)

The resource requirements for higher performance c7a node are:
* a minimum of 32GB RAM (JVM + Linux memory buffers to cache SSTable) + `memory.available` defined in Kubernetes Eviction policy + Resources needed by k8s components that run on every worker node (e.g. proxy)
* a minimum of 8 processor cores per Cassandra Node with 2 CPUs per core (16 vCPU in a VM)
* 4-8 GB JVM heap, recommend trying to keep heaps limited to 4 GB to minimize garbage collection pauses caused by large heaps.

#### Performance considerations
Cassandra  nodes tend  to be IO bound  rather than CPU bound:
* Upper limit of data per node <= 1.5 TB for spinning disk and <= 4 TB for SSD
* Increase the number of nodes to keep the data per node at or below the recommended capacity
* Actual data per node determined by data throughput, for high throughput need to limit the data per node.

The use of Vnodes is generally  considered  to be a good practice as they eliminate the need to perform manual token assignment, distribute workload across all nodes in a cluster when nodes are added or removed. It helps rebuilding dead nodes faster.
Vnode reduces the size of SSTables which can improve read performance. Cassandra best practices set the number of tokens per Cassandra node to 256.

#### Using our configurations
Then use the yaml config files under `deployment/cassandra` folder to configure a Service to expose Cassandra externally, create static persistence volume and statefulSet to deploy Cassandra image.

We also recommend to be familiar with [this kubernetes tutorial on how to deploy Cassandra with Stateful Sets](https://kubernetes.io/docs/tutorials/stateful-application/cassandra/).

* Connect to ICP.
We are using one namespace called 'greencompute'. You can use our script `deployCassandra.sh` under the `../scripts` folder

* create Cassandra headless service

 ```
$ kubectl create -f deployment/cassandra/cassandra-service.yaml -n greencompute
$ kubectl get svc cassandra -n greencompute

 NAME        TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)    AGE
cassandra   ClusterIP    None          <none>        9042/TCP   12h
 ```

* Create persistence volumes to keep data for cassandra
```
$ kubectl create -f deployment/cassandra/cassandra-volumes.yaml -n greencompute
$ kubectl get pv -n greencompute | grep cassandra
cassandra-data-1  1Gi  RWO  Recycle   Bound       greencompute/cassandra-data-cassandra-0 12h
cassandra-data-2  1Gi  RWO  Recycle   Available                                           12h
cassandra-data-3  1Gi  RWO  Recycle   Available    
```
* Create the **statefulset**:
Modify the namespace used in the yaml if you are using your own namespace name for the following element:
```yaml
   env:
     - name: CASSANDRA_SEEDS
       value: cassandra-0.cassandra.greencompute.svc.cluster.local
```


```
$ kubectl create -f deployment/cassandra/cassandra-statefulset.yaml  -n greencompute
$ kubectl get statefulset -n greencompute

NAME                                        DESIRED   CURRENT   AGE
cassandra                                   1         1         12h
```
* Connect to the pod

 ```
 $ kubectl get pods -o wide -n greencompute
 $ kubectl exec -tin greencompute cassandra-0 -- nodetool status

 Datacenter: DC1
 ===============
 Status=Up/Down
 |/ State=Normal/Leaving/Joining/Moving
--  Address          Load       Tokens        Owns (effective)  Host ID                               Rack
UN  192.168.212.174  257.29 KiB  256          100.0%            ea8acc49-1336-4941-b122-a4ef711ca0e6  Rack1
 ```

## High availability
Within a cluster the number of replicas in the statefulset is at least 3 but can be increased to 5 when code maintenance is needed.
The choice for persistence storage is important, and the backup and restore strategy of the storage area network used.

When creating connection to persist data into a keyspace, you specify the persistence strategy and number of replicas. In   
## Code
We have done two implementations for persisting asset data into Cassandra using Cassandra client API or SpringBoot data.
## Cassandra client
The code is under asset-consumer folder. This component is deployed as container inside a kubernetes cluster like ICP.

In the pom.xml we added the following dependencies
```
<dependency>
  <groupId>com.datastax.cassandra</groupId>
  <artifactId>cassandra-driver-core</artifactId>
  <version>3.1.4</version>
</dependency>
```
The code is CassandraRepo.java.
To connect to Cassandra we need to use a Cluster.



## Define Assets Table Structure with CQL
Using the csql tool we can create space and table. To use `cqlsh` connect to cassandra pod:
```
$ kubectl exec -tin greencompute cassandra-0 cqlsh
```
You are now in cqlsh shell and you can define assets table under keyspace `assetmonitoring`:

```
sqlsh>  create keyspace assetmonitoring with replication={'class':'SimpleStrategy', 'replication_factor':1};
sqlsh> use assetmonitoring;
sqlsh:assetmonitoring> create TABLE assets(id text PRIMARY KEY, os text, type text, ipaddress text, version text, antivirus text, current double, rotation int, pressure int, temperature int, latitude double, longitude double);
describe
```
Add an index on the asset operating system field and one on type.
```
CREATE INDEX ON assetmonitoring.assets (os);
CREATE INDEX ON assetmonitoring.assets (type);
```

If you reconnect to the pod using cqlsh you can assess the table using
```
describe tables

describe assets
```

### Some useful CQL commands
```
# modify a table structure adding a column
cqlsh> alter table assets add flowRate bigint;

# change column type
cqlsh> alter table assets alter name type text;

# list content of a a table  
cqlsh> select id,ipaddress,latitude,longitude from assets;

# delete a table
cqlsh> drop table if exists assets;
```

### Use Cassandra Java API to create objects

* Create keyspace:
 ```Java
 StringBuilder sb =
     new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
       .append(keyspaceName).append(" WITH replication = {")
       .append("'class':'").append(replicationStrategy)
       .append("','replication_factor':").append(replicationFactor)
       .append("};");

     String query = sb.toString();
     session.execute(query);
 ```
* Create table
 ```Java
 StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
          .append(TABLE_NAME).append("(")
          .append("id uuid PRIMARY KEY, ")
          .append("temperature text,")
          .append("latitude text,")
          .append("longitude text);");

        String query = sb.toString();
        session.execute(query);
 ```

 * insert data: there is no update so if you want a strict insert you need to add "IF NOT EXISTS" condition in the query.


## Future Readings
* [10 steps to set up a multi-data center Cassandra cluster on a Kubernetes platform](https://www.ibm.com/developerworks/library/ba-multi-data-center-cassandra-cluster-kubernetes-platform/index.html)
* [IBM Article: Scalable multi-node Cassandra deployment on Kubernetes Cluster](https://github.com/IBM/Scalable-Cassandra-deployment-on-Kubernetes)
