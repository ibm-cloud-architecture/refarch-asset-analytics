#!/bin/bash
set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then
 cd ..
fi
echo "1. Get Angular App to Static folder"
./scripts/getAngularDist.sh
echo "2. Package the BFF spring app"
mvn package
echo "3. Build the docker images"
docker build -t ibmcase/gc-dashboard-bff .
echo "4 Push to docker hub"
docker images
docker login
docker push ibmcase/gc-dashboard-bff

