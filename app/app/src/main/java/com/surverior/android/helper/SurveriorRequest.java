package com.surverior.android.helper;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahadian.yusuf on 26/03/16.
 */
public class SurveriorRequest extends StringRequest {
    private TokenHandler token;

    public SurveriorRequest(int method, String url, TokenHandler token, Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.token = token;
    }

    public Map<String,String> getHeaders() {
        final Map<String, String> headers = new HashMap<String, String>();

        headers.put("Authorization", "Bearer " + token.getToken());

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
