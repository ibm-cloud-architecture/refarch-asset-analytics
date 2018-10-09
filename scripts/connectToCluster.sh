
ICPHOST=https://172.16.50.154
ICPUSER=admin
ICPPASSWORD=notadmin
ICPCLUSTER=green-cluster
ICPNAMESPACE=greencompute

echo Retreiving security token for user: $ICPUSER at host: $ICPHOST
## get the id token

TOKEN=$(curl -s -X POST $ICPHOST:8443/idprovider/v1/auth/identitytoken -H "Content-Type:application/x-www-form-urlencoded;charset=UTF-8" -d "grant_type=password&username=$ICPUSER&password=$ICPPASSWORD&scope=openid%20email%20profile" --insecure | jq --raw-output .id_token)

echo
echo Setting kubectl enviroment
echo
## execute kubectl commands to connect to ICP environment
kubectl config set-cluster $ICPCLUSTER --server=$ICPHOST:8001 --insecure-skip-tls-verify=true
kubectl config set-context $ICPCLUSTER-context --cluster=$ICPCLUSTER
kubectl config set-credentials $ICPUSER --token=$TOKEN
kubectl config set-context $ICPCLUSTER-context --user=$ICPUSER --namespace=$ICPNAMESPACE
kubectl config use-context $ICPCLUSTER-context
