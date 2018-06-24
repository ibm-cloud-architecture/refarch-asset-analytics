#!/usr/bin/env bash

echo "get kafka pod name"
pod=$(kubectl get pods | grep kafka | awk '{print $1}')
echo "Send a text to " + $pod
kubectl exec $pod bash < /opt/kafka/bin/kafka-console-producer.sh --broker-list 192.168.1.89:30092 --topic test-topic << EOB
this is a message for you
and this one too
but this one...I m not sure
EOB
