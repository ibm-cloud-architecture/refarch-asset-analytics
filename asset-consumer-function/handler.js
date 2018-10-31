'use strict';

const _ = require('lodash');
var Request = require("request");

module.exports = {

  consumeAssetEvent(event, context) {
    // call asset manager microservice with a post.
    return new Promise(function(resolve,reject){
      require('dotenv').config();
      Request.post({
             "headers": {"content-type": "application/json"},
             "url": process.env.ASSET_URL,
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
