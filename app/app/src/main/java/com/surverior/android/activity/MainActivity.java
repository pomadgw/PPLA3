package com.surverior.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

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

public class MainActivity extends Activity {
	private static final String TAG = "SurveriorActivity";

	private TextView txtName;
	private TextView txtEmail;
	private Button btnLogout;

	private SQLiteHandler db;
	private TokenHandler tokendb;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtName = (TextView) findViewById(R.id.name);
		txtEmail = (TextView) findViewById(R.id.email);
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

		SurveriorRequest req;

		req = new SurveriorRequest(Request.Method.GET, AppConfig.URL_GET_USER_DATA, tokendb,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jObj = new JSONObject(response);
                            JSONObject jUser = jObj.getJSONObject("user");

							Log.d(TAG, "response: " + response);
							txtName.setText(jUser.getString("name"));
							txtEmail.setText(jUser.getString("email"));
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
		// Fetching user details from SQLite
//		HashMap<String, String> user = db.getUserDetails();

//		String name = user.get("name");
//		String email = user.get("email");

		// Displaying the user details on the screen

		// Logout button click event
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

		// db.deleteUsers();
        session.removeToken();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
