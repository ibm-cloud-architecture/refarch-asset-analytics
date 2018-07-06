set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then 
 cd ..
fi
echo $PWD
mvn exec:java -Dexec.mainClass="ibm.cte.esp.PumpSimulator"
