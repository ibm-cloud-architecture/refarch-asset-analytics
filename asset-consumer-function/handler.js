'use strict';

let config = require('dotenv').config();

const _ = require('lodash');
var Request = require("request");

module.exports = {

  consumeAssetEvent(event, context) {
    // call asset manager microservice with a post.
    return new Promise(function(resolve,reject){
      Request.post({
             "headers": {"content-type": "application/json"},
             "url": "http://assetmanager-service:9080/assetmanager/assets",
             "body": event.data
           }, function(error, response, body) {
             if (error) {
               reject(error);
             }
             else{
               console.log(body);
               resolve(response.statusCode);
             }
           });
    })
  },
};
