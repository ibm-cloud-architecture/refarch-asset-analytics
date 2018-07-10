echo "Validate configuration"
if [ -z "$(docker version)" ]; then
  echo "docker needs to run locally";
  exit
fi

if [ -z "$(kubectl version)" ]; then
  echo "kubectl should be installed";
  exit
fi

nspace=$(kubectl get namespace | grep greencompute)
if [ -z "$nspace" ]; then
  echo "Green compute namespace not found, so let add it!...."
  kubectl create namespace greencompute
fi
echo "Green compute namespace found"

cass=$(kubectl get pods | grep cassandra  | cut -d ' ' -f1)
if [ -z "$cass"  ]; then
  echo "cassandra should be installed";
  exit
fi
echo "Cassandra is deployed in your k8s cluster as " $cass

zoo=$(kubectl get pods | grep zookeeper  | cut -d ' ' -f1)
if [ -z "$zoo"  ]; then
  echo "zookeeper should be installed";
  exit
fi
echo "zookeeper is deployed in your k8s cluster as " $zoo

kaf=$(kubectl get pods | grep  kafka  | cut -d ' ' -f1)
if [ -z "$kaf"  ]; then
  echo "Kafka should be installed";
  exit
fi
echo "Kafka is deployed in your k8s cluster as " $kaf
