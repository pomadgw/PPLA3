package com.surverior.android.activity;

/**
 * Created by bambang on 4/15/16.
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.surverior.android.R;
import com.surverior.android.helper.SessionManager;


public class ProfileFragment extends Fragment {

    private TextView name;
    private TextView gender;
    private TextView birthdate;
    private TextView job;
    private TextView address;

    private ImageView image;



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