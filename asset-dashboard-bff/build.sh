#!/bin/bash
./getAngularDist.sh
mvn package
docker build -t ibmcase/asset-dashboard-bff .
docker images
docker run --rm -p 8080:8080 --name asset-dashboard-bff ibmcase/asset-dashboard-bff
