# Asset Dasboard Back End For Front End
SpringBoot app to support monitoring of manufactoring assets

### Features:
* Read assets from Cassandra
* Search asset
* Expose REST api


## Build
We use maven to compile, package and test the application.
```
mvn package
```

The Single Page application is Angular 6 and it is developed in a separate folder, but can be distributed with this application.
The content of the dist/asset-angular-ui is copied to the src/main/resources/static folder of this project so it can be served by the application.

```
./getAngularDist.sh
```

To package the docker file
```
docker build -t ibmcase/asset-dashboard-bff .
```
Then run it with matching the exposed port
```
docker run -ti -p 9080:9080 ibmcase/asset-dashboard-bff
```
We automate this process with the script `build.sh` which can be included in jenkins pipeline.

## Code Approach
### Springboot serving Angular
When the angular application is built, there are a set of javascript files, css, images,... that are copied with the index.html to the  `src/main/resources/static` folder. SpringBoot will serve the `index.html` file for the server URL without context. This is done by not using the @EnableWebMvc annotation and use the configuration class to define where to get the resources.

The alternate is to use NGINX and call the remote BFF server with Cross domain control.  

The RestController for the assets is providing the classical CRUD verbs for a RESTful service. See class AssetController.

The most interesting part is to be able to push new asset when assets are identified from the datasource. For that we will use WebFlux.

@@@@ to continue
We also support continuous build with Google tool: "skaffold"
