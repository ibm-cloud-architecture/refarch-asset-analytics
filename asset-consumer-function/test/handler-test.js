var chai = require('chai');
var expect = chai.expect; // we are using the "expect" style of Chai
var request = require('request');
var sinon = require('sinon');
var consumeAsset = require('.././handler');

var event = {"id":"test", "os":"test", "type":"test", "ipAddress": "test", "version": "test", "antivirus": "test"}

var assert = require('assert');
describe('Basic Mocha String Test', function () {

  before(function(){
    const responseObject = {
      statusCode: 200,
      headers: {
        'content-type': 'application/json'
      }
    };
    sinon
      .stub(request, 'post')
      .yields(null, responseObject, JSON.stringify(event));
  });

  after(function(){
    request.post.restore();
  });

  it('should return 200 if Post is successful', function(done) {
        this.timeout(10000);
        var result = consumeAsset.testasset(event);

        result.then(function(data){
          expect(data).to.equal(200);
          done();
        }, function(error){
          assert.fail(error);
          done();
        });
      });
});
