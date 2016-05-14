package com.surverior.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
	private static final String TAG = "SurveriorActivity";
	private String FRAG_TAG;

	private Bundle extra;

	private SQLiteHandler db;
	private SessionManager session;

	Toolbar mToolbar;
	NavigationView navigationView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Menampilkan toolbar
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.title_timeline));

		//Set the fragment initially
		TimelineFragment fragment = new TimelineFragment();
		android.support.v4.app.FragmentTransaction fragmentTransaction =
				getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.container_body, fragment);
		fragmentTransaction.commit();

		//Set Navigation Drawer
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.getMenu().getItem(0).setChecked(true);

		//How to change elements in the header programatically
		View headerView = navigationView.getHeaderView(0);
		TextView emailText = (TextView) headerView.findViewById(R.id.email);
		emailText.setText("newemail@email.com");

		navigationView.setNavigationItemSelectedListener(this);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());


		// session manager
		session = new SessionManager(getApplicationContext());

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

		req = new SurveriorRequest(Request.Method.GET, AppConfig.URL_GET_USER_DATA, session,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						try {
							JSONObject jObj = new JSONObject(response);

							Log.d(TAG, "response: " + response);
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

	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
//		Bundle extra = getIntent().getExtras();
//		if(extra!=null) {
//			if (extra.getString("FROM_QUESTION_LIST") != null) {
//				displayView(1);
//			}
//		}
		Log.e("ONRESUMEFRAG","JALAN");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e("ONRESUME","JALAN");
		extra = getIntent().getExtras();
		Log.e("EKSTRA",""+extra);
		if(extra!=null) {
//			Log.e("FROM_QUESTION",extra.getString("FROM_QUESTION_LIST"));
			if (extra.getString("FROM_QUESTION_LIST") != null) {
				getIntent().removeExtra("FROM_QUESTION_LIST");
				//displayView(1);
			}else if (extra.getString("FROM_IMAGE")!= null){
				getIntent().removeExtra("FROM_IMAGE");
				//displayView(2);
			}else if (extra.getString("FROM_NAME") != null){
				getIntent().removeExtra("FROM_NAME");
				//displayView(2);
			}else if (extra.get("FROM_PHONE") != null){
				getIntent().removeExtra("FROM_PHONE");
				//displayView(2);
			}else if (extra.getString("FROM_CRITERIA")!= null){
				getIntent().removeExtra("FROM_CRITERIA");
				//displayView(1);
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);//must store the new intent unless getIntent() will return the old one
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		String title = getString(R.string.app_name);

		if (id == R.id.nav_timeline) {
			//Set the fragment initially
			TimelineFragment fragment = new TimelineFragment();
			title = getString(R.string.title_timeline);
			FRAG_TAG = "TIMELINE";
			android.support.v4.app.FragmentTransaction fragmentTransaction =
					getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment, FRAG_TAG);
			fragmentTransaction.commit();
		} else if (id == R.id.nav_mysurvey) {
			//Set the fragment initially
			MySurveyFragment fragment = new MySurveyFragment();
			title = getString(R.string.title_mysurvey);
			FRAG_TAG = "SURVEY";
			android.support.v4.app.FragmentTransaction fragmentTransaction =
					getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment, FRAG_TAG);
			fragmentTransaction.commit();
		} else if (id == R.id.nav_profile) {
			//Set the fragment initially
			ProfileFragment fragment = new ProfileFragment();
			title = getString(R.string.title_profile);
			FRAG_TAG = "PROFILE";
			android.support.v4.app.FragmentTransaction fragmentTransaction =
					getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment, FRAG_TAG);
			fragmentTransaction.commit();
		} else if (id == R.id.nav_logout) {
			logoutUser();
		}

		// set the toolbar title
		getSupportActionBar().setTitle(title);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
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
