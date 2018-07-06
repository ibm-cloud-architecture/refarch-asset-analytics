# Asset Dashboard Back End For Front End
This project includes a SpringBoot app to support monitoring of manufacturing assets. The code supports a set of REST verbs and direct WebSocket connection to get real time event propagation from the server to the user interface.

### Features:
* Read assets from Cassandra
* Search asset
* Expose REST api
* Consume event from Kafka and push them to User Interface

## Solution components

## Build
We use maven to compile, package and test the application.
```
mvn package
```

The Single Page application is Angular 6 and it is developed in a separate folder, but can be distributed with this application. We recommend to do so as the application is not aimed to support CORS.
The content of the dist/asset-angular-ui is copied to the src/main/resources/static folder of this project so it can be served by the application.

```
./getAngularDist.sh
```

Once built to package to a docker image run the command:
```
docker build -t ibmcase/asset-dashboard-bff .
```
Then run the image by matching the exposed port
```
docker run -ti -p 9080:9080 ibmcase/asset-dashboard-bff
```
We automate this process with the script `build.sh` which can be included in jenkins pipeline.

Two other tools are also part of this project. A web socket client to test the web socket logic and to subscribe to real time new asset events.

## Code Approach
### Springboot serving Angular
When the angular application is built, there are a set of javascript files, css, images,... that are copied with the index.html to the  `src/main/resources/static` folder. SpringBoot will serve the `index.html` file for the server URL without context. This is done by not using the @EnableWebMvc annotation and use the configuration class to define where to get the resources.

The alternate is to use NGINX and call the remote BFF server with Cross domain control.  

The RestController for the assets is providing the classical CRUD verbs for a RESTful service. See class AssetController.

The most interesting part is to be able to push new asset when assets are identified from the datasource. For that we will use WebFlux.

### Consuming Kafka message
The use case is simple: when a 'new asset event' is sent to a Kafka Topic, the BFF is one of the consumer and propagates the new asset data to the user interface (Angular) so a new row is added to the table of assets. The same applies for time based measurement events coming regularly from known assets.

There are different ways to support pushing data to user interface. In real application HTTP long polling should provide a simple and very effective solution, as it may be fine to get update every 30 seconds or minute. When the information to push are updated with high frequency and high volume then WebSocket is needed.

With WebSocket we have to take into account any proxy can restrict the use of it. The `Upgrade` http header may not pass thru, or they may close idle connection. Internal application, within the firewall will work, while public to private applications will have challenges.

The Kafka consumer code is based on the same type of code from [the kafka consumer project](../asset-consumer) but it is using kafka springboot support.  

@@@@ to continue
We also support continuous build with Google tool: "skaffold"
