package com.surverior.android.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.surverior.android.app.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
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

            //TODO: Apa yang harus dilakukan jika ada kesalahan?
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

        nowTime -= 60; // check one minute before expire

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
        renew();
        return token;
    }

    public void renew() {
        if (isExpired()) {
            String oldToken = token;
            StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.URL_RENEW_TOKEN,
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            try {
                                // ambil token baru!
                                JSONObject jObj = new JSONObject(response);
                                token = jObj.getString("token");
                                decode();
                            } catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // ...
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);

                    return params;
                }

                public Map<String,String> getHeaders() {
                    final Map<String, String> headers = new HashMap<>();

                    headers.put("Authorization", "Bearer " + token);

                    return headers;
                }
            };

            if (session != null) {
                session.setToken(token);
            }
        }
    }
}
