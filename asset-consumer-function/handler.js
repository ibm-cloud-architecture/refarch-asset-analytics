'use strict';

require('dotenv').config();

const _ = require('lodash');
var Request = require("request");

module.exports = {

  consumeAssetEvent(event, context) {
    // call asset manager microservice with a post.
    return new Promise(function(resolve,reject){
      console.log("line 1" + event.data);
      Request.post({
             "headers": {"content-type": "application/json"},
             "url": "http://assetmanager-service:9080/assetmanager/assets",
             "body": JSON.stringify(event.data)
           }, function(error, response, body) {
             console.log("line 2");
             if (error) {
               console.log("line 4");
               reject(error);
             }
             else{
               console.log("line 5");
               resolve(response.statusCode);
             }
           });
    })
  },
};
