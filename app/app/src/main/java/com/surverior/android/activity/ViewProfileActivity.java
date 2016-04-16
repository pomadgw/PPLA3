package com.surverior.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.surverior.android.R;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.SQLiteHandler;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.SurveriorRequest;
import com.surverior.android.helper.TokenHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Teddy Pranatyo on 4/16/2016.
 */
public class ViewProfileActivity extends Activity {
    private static final String TAG = ViewProfileActivity.class.getSimpleName();

    private TextView name;
    private TextView gender;
    private TextView birthdate;
    private TextView job;
    private TextView province;
    private TextView city;

    private Button btnLogout;

    private SQLiteHandler db;
    private TokenHandler tokendb;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        name = (TextView) findViewById(R.id.name);
        gender = (TextView) findViewById(R.id.gender);
        birthdate = (TextView) findViewById(R.id.birthdate);
        job = (TextView) findViewById(R.id.job);
        province = (TextView) findViewById(R.id.province);
        city = (TextView) findViewById(R.id.city);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // session manager
        session = new SessionManager(getApplicationContext());

        String token = session.getToken();
        Log.d("MainActivity", "AAA:" + token);
        tokendb = new TokenHandler(token, session);

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        if (session.getBoolean("INCOMPLETE_DATA")) {
            Intent intent = new Intent(ViewProfileActivity.this,
                    ProfileActivity.class);
            startActivity(intent);
            finish();
        }

        SurveriorRequest req;

        req = new SurveriorRequest(Request.Method.GET, AppConfig.URL_GET_USER_DATA, tokendb,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONObject jUser = jObj.getJSONObject("user");

                            Log.d(TAG, "response: " + response);
                            name.setText(jUser.getString("name"));
                            String temp = jUser.getString("gender");
                            if(temp.equals("m")){temp="Male";}
                            else{temp="Female";}
                            gender.setText(temp);
                            birthdate.setText(jUser.getString("birth_date"));
                            job.setText(jUser.getString("profession"));
                            province.setText(jUser.getString("province"));
                            city.setText(jUser.getString("city"));
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(req, "get_user");

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

        /**
         * Logging out the user. Will set isLoggedIn flag to false in shared
         * preferences Clears the user data from sqlite users table
         * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        session.removeToken();

        // Launching the login activity
        Intent intent = new Intent(ViewProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
