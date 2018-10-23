# pid=$(kubectl port-forward cassandra-0 9042:9042 --namespace greencompute &)
# kubectl cp assetList.cql greencompute/cassandra-0:/home/assetList.cql
kubectl exec -tn greencompute cassandra-0 bash -c "/usr/bin/cqlsh -f /home/assetList.cql"
# sleep 5
# kill -9 $pid
