package com.surverior.android.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "AndroidHiveLogin";
	
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_TOKEN = "token";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_GENDER = "gender";
	private static final String KEY_ID = "id";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();
        Log.d(TAG, "Token session modified!");
    }

	public void set(String key, boolean val) {
		editor.putBoolean(key, val);
		editor.commit();
	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}

	public void setUser(String name, String email, String gender, String id){
        Log.d("SETUSER","TRUE");
		editor.putString(KEY_NAME, name);
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_GENDER, gender);
		editor.putString(KEY_ID, id);
        editor.commit();
	}

    public void removeSession(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public String getToken() {
        String token = pref.getString(KEY_TOKEN, "");
        return token;
    }

    public String get(String key){
        Log.d("GET",key);
        if(key.equals("name"))
            return pref.getString(KEY_NAME,"");
        else if (key.equals("email"))
            return pref.getString(KEY_EMAIL,"");
        else if (key.equals("id"))
            return pref.getString(KEY_ID,"");
        else if (key.equals("gender"))
            return pref.getString(KEY_GENDER,"");
        else
            return "FALSE";
    }

	public void updateName(String name){
		editor.remove(KEY_NAME);
		editor.putString(KEY_NAME,name);
		editor.commit();
	}


	public void remove(String key) {
		editor.remove(key);
		editor.commit();
	}

	public boolean getBoolean(String key) {
		return pref.getBoolean(key, false);
	}

    public void removeToken() {
        editor.remove(KEY_TOKEN);
        editor.commit();
    }
	
	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}


}
