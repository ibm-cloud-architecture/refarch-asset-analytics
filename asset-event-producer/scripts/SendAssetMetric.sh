set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then 
 cd ..
fi
echo $PWD
# java -jar target/asset-event-producer-0.0.1-SNAPSHOT.jar -Dexec.mainClass="ibm.cte.esp.PumpSimulator"  -Dexec.args="asset-topic gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local:32224 event preassure_play increase 2000 4 pump01"
 mvn exec:java -Dexec.mainClass="ibm.cte.esp.PumpSimulator"  -Dexec.args="asset-topic gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local:32224 event preassure_play increase 2000 4 pump01"