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
svc=$(kubectl get svc  --namespace $nspace | grep dashboard-bff-svc)
if [ -z "$svc" ]; then
  echo "dashboard-bff service not found under namespace " $nspace
  if [ "$mode" = "dev"  ]
  then
    kubectl apply -f ./deployments/dev/dashboard-bff-service.yml --namespace $nspace
  else
    kubectl apply -f ./deployments/prod/dashboard-bff-service.yml --namespace $nspace
    kubectl apply -f ./deployments/prod/dashboard-bff-ingress.yml --namespace $nspace
  fi
fi
echo "Found dashboard-bff service: " $svc " under namespace " $nspace


sfs=$(kubectl get deployment --namespace $nspace | grep dashboard-bff)
if [ -z "$sfs" ]; then
  echo "dashboard-bff Deployment not found... "
  if [ "$mode" = "dev"  ]
  then
    kubectl apply -f ./deployments/dev/dashboard-bff.yml --namespace $nspace
  else
    kubectl apply -f ./deployments/prod/dashboard-bff.yml --namespace $nspace
  fi
fi
echo "Found dashboard-bff Deployment "
kubectl get deployment -n $nspace

kubectl get pods -o wide -n $nspace
