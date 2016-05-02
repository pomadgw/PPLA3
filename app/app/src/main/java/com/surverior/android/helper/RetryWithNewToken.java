package com.surverior.android.helper;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rahadian.yusuf on 20/04/16.
 */
public class RetryWithNewToken extends DefaultRetryPolicy {
    private SessionManager session;
    public RetryWithNewToken(SessionManager session) {
        super();
        this.session = session;
    }
    @Override
    public void retry(VolleyError error) throws VolleyError {
        Log.d("SurveriorRequest", "Retry... ");
        if (error.networkResponse != null && error.networkResponse.statusCode == 401)
        {
            try {
                JSONObject jObj = new JSONObject(new String(error.networkResponse.data));
                if (jObj.has("token")) {
                    session.setToken(jObj.getString("token"));
                }
            } catch (JSONException e) {
                Log.d("SurveriorRequest", "ERROR JSON: " + e);
                e.printStackTrace();
            }
        }

        super.retry(error);
    }
}