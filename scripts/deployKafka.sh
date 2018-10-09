
#!/bin/bash
set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then
 cd ..
fi
nspace=greencompute
echo "Install zookeeper..."
echo "... verify volumes"
zkpv=$(kubectl get pv | grep greencompute-zookeeper-pv | awk '{print $1}')
if [ -z "$zkpv" ]
then
  echo "create persistence volume for zookeeper"
  kubectl apply -f ./deployments/zookeeper/zookeeper-pv.yaml
fi

echo "... verify services"
zksv=$(kubectl get svc --namespace $nspace | grep greencompute-zookeeper-service | awk '{print $1}')
if [ -z "$zksv" ]
then
  echo "create service for zookeeper"
  kubectl apply -f ./deployments/zookeeper/zookeeper-service.yaml  --namespace $nspace
fi

echo "... verify statefulset"
zk=$(kubectl get statefulset --namespace $nspace | grep greencompute-zookeeper | awk '{print $1}')
if [ -z "$zk" ]
then
  echo "create zookeeper statefulset"
  kubectl apply -f ./deployments/zookeeper/zookeeper-statefulset.yaml  --namespace $nspace
fi
echo "Install Kafka..."
echo "... verify services"
kksv=$(kubectl get svc --namespace $nspace | grep greencompute-kafka-svc | awk '{print $1}')
if [ -z "$kksv" ]
then
  echo "create service for Kafka"
  kubectl apply -f ./deployments/kafka/kafka-service.yaml  --namespace $nspace
fi
echo "... verify Deployment"
kafka=$(kubectl get deployment --namespace $nspace | grep greencompute-kafka | awk '{print $1}')
if [ -z "$zk" ]
then
  echo "create kafka deployment"
  kubectl apply -f ./deployments/kafka/kafka-statefulset.yaml  --namespace $nspace
fi
