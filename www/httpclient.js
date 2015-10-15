/**
 * exec(successCallback, errorCallback, "AsyncHttpClient", "action", [params]);
 */
var exec = require('cordova/exec'),
  cordova = require('cordova');

var asynchttpclient = {
  pluginName: "HttpClient",
  test: function() {
    exec(function(e) {
      console.log(e);
    }, null, this.pluginName, "test", []);
  },
  get: function(url, success, error, options) {
    options = typeof options === "undefined" ? options = {} : options;
    if(!options.headers) {
      options.headers = {};
    }
    exec(success, error, this.pluginName, "get", [url, options]);
  },
  post: function(url, params, success, error, options) {
    options = typeof options === "undefined" ? options = {} : options;
    if(!options.headers) {
      options.headers = {};
    }
    exec(success, error, this.pluginName, "post", [url, params, options]);
  },
};

module.exports = asynchttpclient;
