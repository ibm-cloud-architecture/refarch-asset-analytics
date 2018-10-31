# Asset Injection as Function
The goal of this project is to develop a kafka listener in nodejs which consumes asset event posted on Kafka asset-topic, then call the asset-manager-microservice deployed in a kubernetess cluster. The function is implemented in nodejs abd deployed to [kubeless]().

## Pre-requisite
Install the serverless framework with the command. 
```
$ npm install -g serverless
$ npm install -g serverless-kubeless
```

## Create triggers
 ```
kubeless topic create asset-topic || true
kubeless function deploy assetconsumerfct --runtime nodejs6 --handler handler --from-file handler.js
kubeless trigger kafka create assetconsumerfct --function-selector created-by=kubeless,function=assetconsumerfct --trigger-topic asset-topic

 ```

## Validating the consumeAssetEvent

Before validating this function, make sure (Asset Manager Microservice)[https://github.com/ibm-cloud-architecture/refarch-asset-manager-microservice/tree/microprofile] is up and running.

```
$ git clone https://github.com/ibm-cloud-architecture/refarch-asset-analytics.git

$ cd refarch-asset-analytics

$ cd asset-consumer-function

$ npm install

$ serverless deploy function -f consumeasset

$ serverless invoke --function consumeasset --data '{"id":"DAL01", "ipAddress": "172.16.0.1", "type": "ESP", "version": "v.12", "os":"Raspbian","latitude": "32.7767", "longitude": "-96.7970", "antivirus":"v3.0","rotation":209,"pressure": 90, "flowRate": 33, "flowRate":40,"temperature": 72}' -l
```

If it is deployed correctly and it is successful, you will see a message like below.

```
Serverless: Calling function: consumeasset...
--------------------------------------------------------------------
200
```

Once deployed you can access the logs using the below command.

```
$ serverless logs -f consumeasset -t
```

You can also see the list of kubeless functions as follows.

```
$ kubeless function list -n greencompute
NAME        	NAMESPACE   	HANDLER                  	RUNTIME	DEPENDENCIES               	STATUS
consumeasset	greencompute	handler.consumeAssetEvent	nodejs6	request: ^2.88.0           	1/1 READY
            	            	                         	       	serverless-kubeless: ^0.4.0
            	            	                         	       	sinon: ^7.1.0
            	            	                         	       	dotenv: ^6.1.0
            	            	                         	       	lodash: ^4.17.11
            	            	                         	       	mocha: ^5.2.0
```

## Compendium
* [Serverless kubeless template](https://medium.com/bitnami-perspectives/deploying-a-kubeless-function-using-serverless-templates-2d03f49b70e2)
* [Kubeless - Quick Start](https://serverless.com/framework/docs/providers/kubeless/guide/quick-start/)
