package com.surverior.android.activity;

/**
 * Created by bambang on 4/15/16.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.surverior.android.R;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.SurveriorRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {

    private TextView name;
    private TextView gender;
    private TextView birthdate;
    private TextView job;
    private TextView address;

    private ImageView image;
    private SessionManager session;

    public static final String DATA_NAMA="";
    public static final String DATA_ID="";
    private String id;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        name = (TextView) rootView.findViewById(R.id.name_frag);
        gender = (TextView) rootView.findViewById(R.id.gender_frag);
        birthdate = (TextView) rootView.findViewById(R.id.birthday_frag);
        job = (TextView) rootView.findViewById(R.id.job_frag);
        address = (TextView) rootView.findViewById(R.id.address_frag);
        image = (ImageView) rootView.findViewById(R.id.photo_frag);

        // session manager
        session = new SessionManager(getActivity().getApplicationContext());

        //change avatar image
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadImageActivity.class);
                intent.putExtra(DATA_ID, id);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        SurveriorRequest req;

        req = new SurveriorRequest(Request.Method.GET, AppConfig.URL_GET_USER_DATA, session,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONObject jUser = jObj.getJSONObject("user");

                            //Log.d(TAG, "response: " + response);
                            name.setText(jUser.getString("name"));
                            String temp = jUser.getString("gender");
                            if(temp.equals("m")){temp="Male";}
                            else{temp="Female";}
                            gender.setText(temp);
                            birthdate.setText(jUser.getString("birth_date"));
                            job.setText(jUser.getString("profession"));
                            address.setText(jUser.getString("city")+", "+ jUser.getString("province"));
                            id=jUser.getString("id");
                        } catch (JSONException e) {
                           // Log.d(TAG, e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(req, "get_user");

        //edit name
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra(DATA_NAMA, name.getText());
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

//        if (!session.isLoggedIn()) {
//            logoutUser();
//        }

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}