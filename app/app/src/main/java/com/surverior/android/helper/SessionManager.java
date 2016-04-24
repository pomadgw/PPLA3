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

    public String getToken() {
        String token = pref.getString(KEY_TOKEN, "");
        return token;
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
