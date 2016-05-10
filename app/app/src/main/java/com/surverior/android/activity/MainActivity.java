package com.surverior.android.activity;

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

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{
	private static final String TAG = "SurveriorActivity";
	private String FRAG_TAG;

//	private TextView txtName;
//	private TextView txtEmail;
	private Button btnViewProfile;
//	private Button btnLogout;
	private Bundle extra;

	private SQLiteHandler db;
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

//		txtName = (TextView) findViewById(R.id.name);
//		txtEmail = (TextView) findViewById(R.id.email);
//		btnViewProfile = (Button) findViewById(R.id.btnViewProfile);
//			btnLogout = (Button) findViewById(R.id.btnLogout);

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
				displayView(1);
			}else if (extra.getString("FROM_IMAGE")!= null){
				getIntent().removeExtra("FROM_IMAGE");
				displayView(2);
			}else if (extra.getString("FROM_NAME") != null){
				getIntent().removeExtra("FROM_NAME");
					displayView(2);
			}else if (extra.get("FROM_PHONE") != null){
					getIntent().removeExtra("FROM_PHONE");
					displayView(2);
			}else if (extra.getString("FROM_CRITERIA")!= null){
				getIntent().removeExtra("FROM_CRITERIA");
				displayView(1);
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

	@Override
	public void onDrawerItemSelected(View view, int position) {
		displayView(position);
	}

	private void displayView(int position) {
		Fragment fragment = null;
		String title = getString(R.string.app_name);
		switch (position) {
			case 0:
				fragment = new TimelineFragment();
				title = getString(R.string.title_timeline);
				FRAG_TAG = "TIMELINE";
				break;
			case 1:
				fragment = new MySurveyFragment();
				title = getString(R.string.title_mysurvey);
				FRAG_TAG = "SURVEY";
				break;
			case 2:
				fragment = new ProfileFragment();
				title = getString(R.string.title_profile);
				FRAG_TAG = "PROFILE";
				break;
			case 3:
				logoutUser();
				break;
			default:
				break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.container_body, fragment, FRAG_TAG);
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
