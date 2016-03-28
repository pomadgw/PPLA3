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

        nowTime -= 120; // check two minutes before expire

        return expiredTime <= nowTime;
    }

    public int getExpire() {
        return tokenObj.getExpire();
    }

    public String getToken() {
        Log.d(TAG, "Token lama: " + token);

        if (isExpired()) {
            String oldToken = token;
            updatingToken = true;
            renew();
            // TODO: karena volley bersifat async,
            //       harus dicari cara agar tokennya
            //       ter-update sebelum dipakai
            //       Belum tahu jika cara ini bisa dipakai...
            Log.d(TAG, "Blocking to get token...");
            long start = System.currentTimeMillis();
            long end = 0;
            while (oldToken.equals(token)) {
                end = System.currentTimeMillis();
                // If token is not updated for 30 seconds...
                if (end - start >= 30 * 1000) {
                    break;
                }
            }
            Log.d(TAG, "Stop blocking");
        }
        Log.d(TAG, "Token baru: " + token);
        return token;
    }

    // isChecked: if it's true, it is already checked that the token is expired
    public void renew() {
        final String oldToken = token;
        Log.d(TAG, "Memperbarui token");
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.URL_RENEW_TOKEN,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            // ambil token baru!
                            JSONObject jObj = new JSONObject(response);
                            Log.d(TAG, "JSON: " + jObj);
                            token = jObj.getString("token");
                            decode();
                            if (session != null) {
                                Log.d(TAG, "Memperbarui token ke session");
                                session.setToken(token);
                                session.setTokenExpire(getExpire());
                                Log.d(TAG, "New token expired at: " + getExpire());
                            }
                            Log.d(TAG, "New token: " + token);
                            updatingToken = false;
                        } catch(JSONException e) {
                            e.printStackTrace();
                            updatingToken = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updatingToken = false;
                        Log.d(TAG, "Error memperbarui token");
                        NetworkResponse response = error.networkResponse;

                        Log.d(TAG, "ERROR: " + error.toString());
                        Log.d(TAG, "ERROR: is null? " + (error.networkResponse == null));

                        if(response != null){
                            try {
                                JSONObject jObj = new JSONObject(new String(response.data));
                                Log.d(TAG, (String)jObj.get("message"));
                                switch (response.statusCode) {
                                    case 401:
                                    case 500:
                                        Log.d(TAG, "Tidak diberi akses!");
                                        break;
                                    default:
                                        Log.d(TAG, "Error " + response.statusCode);
                                }
                            } catch(JSONException e) {
                            }
                            //Additional cases
                        }
                    }
                }) {

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

//            @Override
//            protected VolleyError parseNetworkError(VolleyError volleyError) {
//                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
//                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
//                    volleyError = error;
//                }
//
//                return volleyError;
//            }
        };

        AppController.getInstance().addToRequestQueue(strReq, "get_new_token");

//        if (session != null) {
//            Log.d(TAG, "Memperbarui token ke session");
//            session.setToken(token);
//        }
    }
}
