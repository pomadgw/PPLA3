/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.surverior.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.surverior.android.R;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.SQLiteHandler;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.TokenHandler;

public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    // boolean error = jObj.getBoolean("error");
                    String token = jObj.getString("token");

//                    Log.d(TAG, "Token:" + token);

                    TokenHandler tokenObj = new TokenHandler(token);

                    Log.d(TAG, "Token:" + tokenObj.getToken());
                    Log.d(TAG, "Expired:" + tokenObj.getExpire());

                    // Check for error node in json
                    //if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        session.setToken(token);
                        session.setTokenExpire(tokenObj.getExpire());

                        // Now store the user in SQLite
                    //    String uid = jObj.getString("uid");

                    //    JSONObject user = jObj.getJSONObject("user");
                    //    String name = user.getString("name");
                    //    String email = user.getString("email");
//                        String gender = user.getString("gender");
//                        String birth_date = user.getString("birth_date");
//                        String profession = user.getString("profession");
//                        String city = user.getString("city");
//                        String province = user.getString("province");
//                        String created_at = user
//                                .getString("created_at");

                        // Inserting row in users table
                    //    db.addUser(name, email, uid/*,gender,birth_date,profession,city,province, created_at*/);

                        // Launch main activity
                        if(name.equals("null")){
                            Intent intent = new Intent(LoginActivity.this,
                                    ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        // Error in login. Get the error message
                    //    String errorMsg = jObj.getString("error_msg");
                    //    Toast.makeText(getApplicationContext(),
                    //            errorMsg, Toast.LENGTH_LONG).show();
                    //}
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String str = error.getMessage();
                if (error.networkResponse != null) {
                    try {
                        JSONObject jObj = new JSONObject(new String(error.networkResponse.data));
                        Log.e(TAG, "Login Error JSON: " + jObj);
                        if(jObj.getInt("status_code") == 401) {
                            str = getResources().getString(R.string.error_unauthorized);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON");
                    }
                }
                Log.e(TAG, "Login Error: " + str);
                Toast.makeText(getApplicationContext(),
                        str, Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
