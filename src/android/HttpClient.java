package com.pedox.plugin.HttpClient;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.Iterator;


public class HttpClient extends CordovaPlugin {


    /**
     * Constructor.
     */
    public HttpClient() {
    }

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action          The action to execute.
     * @param args            JSONArry of arguments for the plugin.
     * @param callbackContext The callback id used when calling back into JavaScript.
     * @return True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        final Activity context = this.cordova.getActivity();

        if(action.equals("get"))
        {
            this.getRequest(context, args.getString(0), args.getJSONObject(1), callbackContext);
            return true;
        }

        if(action.equals("post"))
        {
            JSONObject data = args.getJSONObject(1);
            RequestParams params = new RequestParams();

            for (Iterator<String> i = data.keys(); i.hasNext();) {
                String key = i.next();
                try {
                    Object value = data.get(key);
                    params.put(key, value);
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }
            this.postRequest(context, args.getString(0), args.getJSONObject(2), params, callbackContext);
            return true;
        }

        if (action.equals("test")) {
            callbackContext.success("it works !");
        } else {
            return false;
        }
        return true;
    }

    /**
     * Get request
     * @param context
     * @param url
     * @param callbackContext
     */
    private void getRequest(final Activity context, String url, JSONObject headers, final CallbackContext callbackContext) throws JSONException {
        final AsyncHttpClient client = new AsyncHttpClient();
        this.setArgument(client, headers);

        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                HttpClient.handleResult(false, statusCode, headers, responseString, callbackContext);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                HttpClient.handleResult(true, statusCode, headers, responseString, callbackContext);
            }
        });

    }

    /**
     * Post Request
     * @param context
     * @param url
     * @param params
     * @param callbackContext
     */
    public void postRequest(final Activity context, String url, JSONObject headers, RequestParams params, final CallbackContext callbackContext) throws JSONException {
        final AsyncHttpClient client = new AsyncHttpClient();
        this.setArgument(client, headers);
        client.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                HttpClient.handleResult(false, statusCode, headers, responseString, callbackContext);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                HttpClient.handleResult(true, statusCode, headers, responseString, callbackContext);
            }
        });
    }

    private static void handleResult(boolean success, int statusCode, Header[] headers, String responseString, CallbackContext callbackContext)
    {
        JSONObject result = new JSONObject();
        try {
            /** Set header **/
            JSONObject headerParam = new JSONObject();
            for (Header param : headers) {
                headerParam.put(param.getName(), param.getValue());
            }
            result.put("result", responseString);
            result.put("code", statusCode);
            result.put("header", headerParam);
            if(success == true) {
                callbackContext.success(result);
            } else {
                callbackContext.error(result);
            }
        } catch (JSONException e) {
            callbackContext.error(0);
            e.printStackTrace();
        }
    }


    /**
     * Set Header Param
     *
     * @param client
     * @return
     */
    private AsyncHttpClient setArgument(AsyncHttpClient client, JSONObject headers) throws JSONException {
        client.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2243.0 Safari/537.36");

        JSONObject _headers = headers.getJSONObject("headers");

        for (Iterator<String> i = _headers.keys(); i.hasNext();) {
            String key = i.next();
            try {
                Object value = _headers.get(key);
                client.addHeader(key, (String) value);
            } catch (JSONException e) {
                // Something went wrong!
            }
        }
        return client;
    }

}
