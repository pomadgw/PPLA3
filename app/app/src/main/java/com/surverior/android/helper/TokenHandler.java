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
import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
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
    private Map<String,Object> decodedPayload;
    private final static String TAG = "TokenHandler";
    private SessionManager session;

    public TokenHandler(String token, SessionManager session) {
        this.token = token;
        this.session = session;
        decode();
    }

    public TokenHandler(String token) {
        this(token, null);
    }

    public void decode() {
        try {
            decodedPayload = new JWTVerifier(AppConfig.JWT_SECRET).verify(token);

            for (String key : decodedPayload.keySet()) {
                Log.d(TAG, key + " " + decodedPayload.get(key));
            }
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.w(TAG, "Algoritma untuk hashingnya nggak ada?? " + e.getMessage());
        } catch(InvalidKeyException e) {
            e.printStackTrace();
            Log.w(TAG, "Salah kunci: " + e.getMessage());
        } catch(IOException e) {
            e.printStackTrace();
            Log.w(TAG, "Masalah dengan IO (jaringan?): " + e.getMessage());
        } catch(SignatureException e) {
            e.printStackTrace();
            Log.w(TAG, "Error tanda tangan: " + e.getMessage());
        } catch(JWTExpiredException e) {
            Log.w(TAG, "Error token JWT sudah kadaluarsa: " + e.getMessage());
            renew();
        } catch(JWTVerifyException e) {
            e.printStackTrace();
            Log.w(TAG, "Error menverifikasi JWT: " + e.getMessage());
        }
    }

    public Object get(String key) {
        return decodedPayload.get(key);
    }

    public boolean isExpired() {
        long expiredTime = getExpire();
        long nowTime = System.currentTimeMillis() / 1000L;

        Date exp = new Date(expiredTime * 1000);
        Date now = new Date(nowTime * 1000);

        Log.d(TAG, "Expired at " + exp);
        Log.d(TAG, "Now is     " + now);

        nowTime -= 120; // check two minutes before expire

        return expiredTime <= nowTime;
    }

    public int getExpire() {
        Object tmp = decodedPayload.get("exp");
        if (tmp == null) {
            Log.d(TAG, "!! WHAT? !!");
        }
        return (Integer) tmp;
    }

    public String getToken() {
        return token;
    }

//    public void renew() {
//        renew(false);
//    }

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
                            if (session != null) {
                                Log.d(TAG, "Memperbarui token ke session");
                                session.setToken(token);
                            }
                            Log.d(TAG, "New token: " + token);
                            decode();
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
