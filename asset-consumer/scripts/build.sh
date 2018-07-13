mvn package -Dmaven.test.skip=true
docker build -t ibmcase/asset-consumer .