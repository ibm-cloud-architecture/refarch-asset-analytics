'use strict';

const _ = require('lodash');
var Request = require("request");

module.exports = {

  consumeAssetEvent(event, context) {
    // call asset manager microservice with a post.
    return new Promise(function(resolve,reject){
      require('dotenv').config();
      Request.post({
             headers: {"content-type": "application/json"},
             url: process.env.ASSET_URL,
             body: JSON.stringify(event.data) //event.data
           }, function(error, response, body) {
             if (error) {
               reject(error);
             }
             else{
               console.log(JSON.parse(body)); //(body)
               resolve(response.statusCode);
             }
           });
    })
  },
};
