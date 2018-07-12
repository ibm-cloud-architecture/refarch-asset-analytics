echo "Build Asset Management Microservice"
mvn package -Dmaven.test.skip=true
docker build -t ibmcase:asset-mgr-ms .