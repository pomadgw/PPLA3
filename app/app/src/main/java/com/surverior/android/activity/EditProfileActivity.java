package com.surverior.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.surverior.android.R;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.TokenHandler;

/**
 * Created by Azhar Fauzan Dz on 4/16/2016.
 */
public class EditProfileActivity extends Activity{
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private TextView name;
    private EditText inputName;

    private Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // session manager
        session = new SessionManager(getApplicationContext());

        String token = session.getToken();
        Log.d("MainActivity", "AAA:" + token);
        tokendb = new TokenHandler(token, session);

        if (!session.isLoggedIn()) {
            logoutUser();
        }
    }
}
