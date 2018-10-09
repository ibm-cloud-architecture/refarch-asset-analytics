#!/bin/bash
echo "Build the Angular App"
ng build
echo "Build the docker image"
docker build -t ibmcase/asset-dashboard-ui . -f nginx/Dockerfile
docker images
docker run --rm -p 9080:80 --name asset-dashboard-ui ibmcase/asset-dashboard-ui
