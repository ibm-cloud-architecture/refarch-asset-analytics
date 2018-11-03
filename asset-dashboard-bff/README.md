# Asset Dashboard Back End For Front End
This project includes a SpringBoot app to support a set of back end for front end pattern REST APIs for the Angular single page app. The code supports a set of REST verbs and direct WebSocket connection to get real time event propagation from the server to the user interface. The Angular app is in this folder: [../asset-dashboard-ui](../asset-dashboard-ui).

### Features:
* [Serve the Angular](#springboot-serving-angular) single page application.
* Integrate with asset management microservice deployed in kubernetes cluster.
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
We automate this process with the script `build.sh` which can be included in a Jenkins pipeline.

Two other tools are also part of this project. A web socket client to test the web socket logic and to subscribe to real time new asset events.

## Deploy
To deploy on ICP or a kubernetes cluster use the deployment yaml file under the `deployments` folder.

## Code Approach
### Springboot serving Angular

The way to serve angular app is first to compile the Angular TypeScripts code under source folder part of the Springboot
 classpath. To keep the component separated we use a script to get the compiled javascripts and copy it under `src/main/resources/static` folder.
In traditional SpringBoot web application the `@EnableWebMvc` annotation is used to auto configure the beans and pages to serve. 
In our case, we need to add a Spring configuration class that defines a resource handler on the root url: '/', and references what
to serve at that url... the index.html file (by default) in the static path.  
```java
@Configuration
@EnableConfigurationProperties(BffProperties.class)
public class BffConfig implements WebMvcConfigurer {

	// serve the SPA compiled in the src/main/resource/static folder
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/").addResourceLocations("classpath:/static/");
    }

}
```

### Defining configuration from yaml file
The BffProperties class is built to get the configuration from the application.yml of `src/main/resources`. This file defines application properties.
```
server:
  port: 8081 
spring:
  kafka:
    bootstrap-servers: gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local:32224

app:
  assetsTopic: asset-topic
  assetManager:
    host: assetmgr.greencompute.ibmcase.com
    port: 32544
    protocol: http
    baseUrl: /assetmanager
```
To be able to load then at runtime the hierarchical properties need to be map to classes. For example the app, includes subclass for assetManager that is mapped to a Java class

```java
public static class AssetManager {
		private String host;
		private String port;
		private String protocol;
		private String baseUrl;
		// ...
}
```

### REST Api
The RestController for the assets is providing some simple read verbs (See class AssetController.java):
```java
@RestController()
@RequestMapping(value="/assets")
public class AssetController {
//...
}
```
### WebSocket to UI
There are multiple solutions to get new asset events or asset metrics events from Kafka. Nornally the simplest and scalable one is to use a pull mechanism from Angular to call
the `/assets` URL every x seconds. As HTTP is connectionless, meaning the connection is closed as soon as the request is sent.  So this approach is viable when the number of
users is important.

In the case of very few instances of a dashboard running in parallel, it may be interesting to keep websocket connection open between the web browser and the server.
WebSocket is a lightweight layer above TCP. To send message over the socket, the current approach is to use Streaming Text Oriented Message Protocol ([STOMP](https://en.wikipedia.org/wiki/Streaming_Text_Oriented_Messaging_Protocol)).

We are reusing the approach presented in [this spring.io article](https://spring.io/guides/gs/messaging-stomp-websocket/) but the implementation is slightly different:
* the client is initiating the connection when the user enter the dashboard asset page and push on the `Start Monitoring Asset` button.
* the message handling controller is 

### Consuming Kafka message
The use case is simple: when a 'new asset event' is sent to a Kafka Topic, the BFF is one of the consumer and propagates the new asset data to the user interface (Angular) so a new row is added to the table of assets. The same applies for time based measurement events coming regularly from known assets.

There are different ways to support pushing data to user interface. In real application HTTP long polling should provide a simple and very effective solution, as it may be fine to get update every 30 seconds or minute. When the information to push are updated with high frequency and high volume then WebSocket is needed.

With WebSocket we have to take into account any proxy can restrict the use of it. The `Upgrade` http header may not pass thru, or they may close idle connection. Internal application, within the firewall will work, while public to private applications will have challenges.

The Kafka consumer code is based on the same type of code from [the kafka consumer project](../asset-consumer) but it is using kafka springboot API instead.
The code uses the MessageListener.   

@@@@ to continue
