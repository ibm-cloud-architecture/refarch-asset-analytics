docker rmi -f $(docker images | grep none | awk '{print $3}')
docker rmi -f $(docker images | grep ibmcase | awk '{print $3}')
