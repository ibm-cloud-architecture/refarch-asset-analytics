# pid=$(kubectl port-forward cassandra-0 9042:9042 --namespace greencompute &)
# kubectl cp assetList.cql -n greencompute cassandra-0:/home/assetList.cql
kubectl exec -tn greencompute -v .:/home cassandra-0 cqlsh -f /home/assetList.cql
# sleep 5
# kill -9 $pid
