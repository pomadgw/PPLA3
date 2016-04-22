package com.surverior.android.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by rahadian.yusuf on 26/03/16.
 */
public class TokenHandler {
    private String token;
    private final static String TAG = "TokenHandler";
    private SessionManager session;
    private JWTToken tokenObj;
    private boolean updatingToken;

    public TokenHandler(String token, SessionManager session) {
        this.token = token;
        this.session = session;
        updatingToken = false;
        decode();
    }

    public TokenHandler(String token) {
        this(token, null);
    }

    public void decode() {
        try {
            Log.d(TAG, "Decoding...");
            tokenObj = new JWTToken(token);
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.w(TAG, "Algoritma untuk hashingnya nggak ada?? " + e.getMessage());
        } catch(InvalidKeyException e) {
            e.printStackTrace();
            Log.w(TAG, "Salah kunci: " + e.getMessage());
        } catch(SignatureException e) {
            e.printStackTrace();
            Log.w(TAG, "Error menverifikasi JWT: " + e.getMessage());
        }
    }

    public boolean isExpired() {
        long expiredTime = tokenObj.getExpire();
        long nowTime = System.currentTimeMillis() / 1000L;

        Date exp = new Date(expiredTime * 1000);
        Date now = new Date(nowTime * 1000);

        Log.d(TAG, "Expired at " + exp);
        Log.d(TAG, "Now is     " + now);

        nowTime += 120; // check two minutes before expire

        Log.d(TAG, "LExpired at " + expiredTime);
        Log.d(TAG, "LNow is     " + nowTime);

        Log.d(TAG, "IS EXPIRED? => " + (nowTime > expiredTime));

        return nowTime > expiredTime;
    }

    public int getExpire() {
        return tokenObj.getExpire();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        decode();
        session.setToken(token);
    }

    // isChecked: if it's true, it is already checked that the token is expired
    public void renew() {
        final String oldToken = token;
        Log.d(TAG, "Memperbarui token");
        StringRequest strReq;
        RequestFuture<String> future = RequestFuture.newFuture();
        strReq = new StringRequest(Request.Method.GET, AppConfig.URL_RENEW_TOKEN, future, future) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", oldToken);

                return params;
            }

            public Map<String,String> getHeaders() {
                final Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", "Bearer " + oldToken);

                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, "get_new_token");

        try {
            String response = future.get(60, TimeUnit.SECONDS); // Tunggu 60 detik untuk mendapatkan token baru
            JSONObject jObj = new JSONObject(response);
            Log.d(TAG, "JSON: " + jObj);
            token = jObj.getString("token");
            decode();
            if (session != null) {
                Log.d(TAG, "Memperbarui token ke session");
                session.setToken(token);
                Log.d(TAG, "New token expired at: " + getExpire());
            }
            Log.d(TAG, "New token: " + token);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch(TimeoutException e) {
            Log.d(TAG, "ERROR: timeout... " + e.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
            VolleyError error = (VolleyError) e.getCause();
            if(error.networkResponse != null)
                Log.d(TAG, "ERROR ExecutionException: " + new String(error.networkResponse.data));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
