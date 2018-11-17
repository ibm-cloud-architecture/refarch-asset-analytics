#!/bin/bash
echo "Build UI Dashboard"
cd asset-dashboard-ui/
pwd
ng build
echo "Build Dashboard BFF"
cd ../asset-dashboard-bff/
pwd
./scripts/build.sh
echo "Build Asset Manager Microservices"
cd ../../refarch-asset-manager-microservices
mvn package

echo "Build Asset Metrics Consumer"
