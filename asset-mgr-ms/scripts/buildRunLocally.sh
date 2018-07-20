echo "Build Asset Management Microservice and run it locally"
mvn clean package -Dmaven.test.skip=true exec:java@AssetMgrApp
