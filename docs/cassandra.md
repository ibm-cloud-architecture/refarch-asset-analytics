# Cassandra Summary

## Deploying on "Docker for desktop" kubernetes
We assume install Docker Edge and enabled kubernetes (see this [note](https://docs.docker.com/docker-for-mac/kubernetes/)).
* create cassandra service
```
$ kubectl create -f chart/cassandra-service.yaml
```
* Create one persistence volume to keep data for cassandra
```
$
$ kubectl create -f chart/local-volumes.yaml
```
* Create the statefulSet
```
$ kubectl create -f chart/cassandra-statefulset.yaml
# Verify the installation
$ kubectl exec -ti  cassandra-0 -- nodetool status
```

## Deployment on ICP
You need a Kubernetes ICP cluster. Then use the yaml config files under charts folder to configure a Service to cassandra is exposed externally, create static persistence volume and statefulSet to deploy cassandra images.
* Connect to ICP.
We are using one namespace called 'greencompute'
* create cassandra service
```
$ kubectl create -f chart/cassandra-service.yaml -n greencompute
$ kubectl get svc cassandra -n greencompute

NAME        TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)    AGE
cassandra   ClusterIP   10.10.10.185 <none>        9042/TCP   12h
```
* Create persistence volumes to keep data for cassandra
```
$ kubectl create -f chart/local-volumes.yaml -n greencompute
$ kubectl get pv -n greencompute | grep cassandra
cassandra-data-1  1Gi  RWO  Recycle   Bound       greencompute/cassandra-data-cassandra-0 12h
cassandra-data-2  1Gi  RWO  Recycle   Available                                           12h
cassandra-data-3  1Gi  RWO  Recycle   Available    
```
* Create the statefulset:
Modify the namespace used in the yaml if you are using your own namespace name for the following element:
```yaml
   env:
     - name: CASSANDRA_SEEDS
       value: cassandra-0.cassandra.greencompute.svc.cluster.local
```


```
$ kubectl create -f chart/cassandra-statefulset.yaml  -n greencompute
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
--  Address          Load       Tokens       Owns (effective)  Host ID                               Rack
UN  192.168.212.174  257.29 KiB  256          100.0%            ea8acc49-1336-4941-b122-a4ef711ca0e6  Rack1
```

## Define Asset Table Structure
Using the csql tool we can create space and table. To use `cqlsh` connect to cassandra pod:
```
$ kubectl exec -tin greencompute cassandra-0 cqlsh
```
You are now in cqlsh shell and you can define assets table under keyspace `assetmonitoring`:

```
sqlsh>  create keyspace assetmonitoring with replication={'class':'SimpleStrategy', 'replication_factor':1};
sqlsh> use assetmonitoring;
sqlsh:assetmonitoring> create TABLE assets(id text PRIMARY KEY, os text, type text, ipaddress text, version text, antivirus text, current int, rotation int, pressure int, temperature int);
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

### Some useful CQL command
```
# modify a table structure adding a column
cqlsh> alter table assets add flowRate bigint;

# change column type
cqlsh> alter table assets alter name type text;
```
