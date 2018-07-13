#!/bin/bash
set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then
 cd ..
fi
./scripts/getAngularDist.sh
mvn package
docker build -t ibmcase/asset-dashboard-bff .
docker images
# docker run --rm -p 8080:8080 --name asset-dashboard-bff ibmcase/asset-dashboard-bff
