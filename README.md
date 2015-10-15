# cordova-httpclient

Using [Android Asynchronous Http Client](http://loopj.com/android-async-http/)

Ugly Version

How to use it

### GET

    httpclient.get(URL, success, error, options);

### POST

    httpclient.post(URL, params, success, error, options);

### Response
for either success and error response out

Example

    {
        code: 200,
        result: '<html>...</html>',
        header: {
            Cache-Control: "private",
            Date: "Thu, 25 May 2011 05:05:59 GMT",
            X-Powered-By: "node"
        }
    }

### Options

#### Headers
for extra headers field

Example

    httpclient.post(URL, params, success, error, {
        headers: {
            'Accept-Language':'en-US,en;q=0.8,id;q=0.6,ms;q=0.4'
        }
    });
