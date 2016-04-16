package com.surverior.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{
	private static final String TAG = "SurveriorActivity";

	private TextView txtName;
	private TextView txtEmail;
	private Button btnViewProfile;
	private Button btnLogout;

	private SQLiteHandler db;
	private TokenHandler tokendb;
	private SessionManager session;

	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Menampilkan toolbar
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		//Menampilkan Navigation Bar
		drawerFragment = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragment.setDrawerListener(this);
		// display the first navigation drawer view on app launch
		displayView(0);

		txtName = (TextView) findViewById(R.id.name);
		txtEmail = (TextView) findViewById(R.id.email);
		btnViewProfile = (Button) findViewById(R.id.btnViewProfile);
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
			Intent intent = new Intent(MainActivity.this,
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
		btnViewProfile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ViewProfileActivity.class);
				startActivity(intent);
				finish();
			}
		});
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDrawerItemSelected(View view, int position) {
		displayView(position);
	}

	private void displayView(int position) {
		Fragment fragment = null;
		String title = getString(R.string.app_name);
		switch (position) {
			case 0:
				fragment = new SurveyFragment();
				title = getString(R.string.title_survey);
				break;
			case 1:
				fragment = new CoinsFragment();
				title = getString(R.string.title_coins);
				break;
			case 2:
				fragment = new ProfileFragment();
				title = getString(R.string.title_profile);
				break;
			default:
				break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment);
			fragmentTransaction.commit();

			// set the toolbar title
			getSupportActionBar().setTitle(title);
		}
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
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
