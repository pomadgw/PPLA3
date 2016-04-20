package com.surverior.android.helper;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahadian.yusuf on 26/03/16.
 */
public class SurveriorRequest extends StringRequest {
    private SessionManager session;

    public SurveriorRequest(int method, String url, SessionManager session, Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.session = session;
        this.setRetryPolicy(new RetryWithNewToken(session));
    }

    public Map<String,String> getHeaders() {
        final Map<String, String> headers = new HashMap<String, String>();

        headers.put("Authorization", "Bearer " + session.getToken());

        return headers;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError){
        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }

        return volleyError;
    }

}