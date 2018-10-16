'use strict';

const _ = require('lodash');

module.exports = {
  consumeAssetEvent(event, context) {
    // call asset manager microservice with a post.
    return _.capitalize(event.data);
  },
};
