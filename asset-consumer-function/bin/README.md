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

## Compendium
* [Serverless kubeless template](https://medium.com/bitnami-perspectives/deploying-a-kubeless-function-using-serverless-templates-2d03f49b70e2)
