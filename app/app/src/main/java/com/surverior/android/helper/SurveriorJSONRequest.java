package com.surverior.android.helper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rahadian.yusuf on 22/04/16.
 */
public class SurveriorJSONRequest extends JsonObjectRequest {
    private SessionManager session;

    public SurveriorJSONRequest(String url,
                                JSONObject jsonBody,
                                SessionManager session,
                                Response.Listener<JSONObject> listener,
                                Response.ErrorListener errorListener) {
        super(url, jsonBody, listener, errorListener);
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
