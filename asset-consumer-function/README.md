# Asset Injection as Function
The goal of this project is to develop a kafka listener in nodejs which consumes asset event posted on Kafka asset-topic, then call the asset-manager-microservice deployed in a kubernetess cluster. The function is implemented in nodejs abd deployed to [kubeless]().

## Pre-requisite
Install the serverless framework with the command
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

## Compendium
* [Serverless kubeless template](https://medium.com/bitnami-perspectives/deploying-a-kubeless-function-using-serverless-templates-2d03f49b70e2)
* [Kubeless - Quick Start](https://serverless.com/framework/docs/providers/kubeless/guide/quick-start/)
