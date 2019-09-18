package com.engai.demo.persistence.rest;

import android.content.Context;

import com.engai.demo.App;
import com.engai.demo.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiClientHttp {

    private static final String BASE_URL = App.getResource().getString(R.string.engai);


    private static AsyncHttpClient client = new AsyncHttpClient();

    public static AsyncHttpClient getClient() {
        return client;
    }

    public static void get(Context context , String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        if (App.isNetworkConnected()) {
            params.setUseJsonStreamer(true);
            client.get(getAbsoluteUrl(url), params, responseHandler);
        }
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}