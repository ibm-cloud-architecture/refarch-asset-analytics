'use strict';

require('dotenv').config();

const _ = require('lodash');
var Request = require("request");

var url = process.env.ASSET_URL;
var event = {"id":process.env.id, "os":process.env.os, "type":process.env.type, "ipAddress": process.env.ipAddress, "version": process.env.version, "antivirus": process.env.antivirus};

module.exports = {

  testasset(event){
    return new Promise(function(resolve,reject){
      Request.post({
             "headers": {"content-type": "application/json"},
             "url": url,
             "body": JSON.stringify(event)
           }, function(error, response, body) {
             if(!event.id){
               reject('id cannot be null');
             }
             else if (error) {
               reject(error);
             }
             else{
               console.dir(JSON.parse(body));
               resolve(response.statusCode);
             }
           });
    })
  },

  consumeAssetEvent(event, context) {
    // call asset manager microservice with a post.
    return _.capitalize(event.data);
  },
};
