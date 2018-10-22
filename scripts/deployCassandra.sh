#!/bin/bash
set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then
 cd ..
fi
if [ $# -eq 1 ]
then
  mode=$1
else
  mode="dev"
fi
nspace="greencompute"
svc=$(kubectl get svc  --namespace $nspace | grep cassandra-svc)
if [ -z "$svc" ]; then
  echo "Cassandra service not found under namespace " $nspace
  kubectl apply -f ./deployments/cassandra/cassandra-service.yaml --namespace $nspace
fi
echo "Found cassandra service: " $svc " under namespace " $nspace


pvs=$(kubectl get pv  --namespace $nspace | grep cassandra-data )
if [ -z "$pvs" ]; then
  echo "Cassandra Persistence volume not found... "
  if [ "$mode" = "dev"  ]
  then
    kubectl apply -f ./deployments/cassandra/dev/cassandra-volumes.yaml --namespace $nspace
  else
    kubectl apply -f ./deployments/cassandra/prod/cassandra-volumes.yaml --namespace $nspace
  fi

  echo sleep to be sure PVs are created
  sleep 10
fi
echo "Found cassandra PVs "
echo $pvs


sfs=$(kubectl get statefulset --namespace $nspace | grep cassandra)
if [ -z "$sfs" ]; then
  echo "Cassandra Statefulset not found... "
  if [ "$mode" = "dev"  ]
  then
    kubectl apply -f ./deployments/cassandra/dev/cassandra-statefulset.yaml --namespace $nspace
  else
    kubectl apply -f ./deployments/cassandra/prod/cassandra-statefulset.yaml --namespace $nspace
  fi
fi
echo "Found cassandra statefulset "
kubectl get statefulsets -n $nspace

kubectl get pods -o wide -n $nspace
