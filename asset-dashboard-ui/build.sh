#!/bin/bash
ng build
docker build -t ibmcase/asset-dashboard-ui . -f nginx/Dockerfile
docker images
docker run --rm -p 9080:80 --name asset-dashboard-ui ibmcase/asset-dashboard-ui
