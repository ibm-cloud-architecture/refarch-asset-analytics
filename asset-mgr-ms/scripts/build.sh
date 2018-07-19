echo "Build Asset Management Microservice"
mvn clean package -Dmaven.test.skip=true
docker build -t ibmcase/asset-mgr-ms .
