package com.surverior.android.activity;

/**
 * Created by bambang on 4/15/16.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.surverior.android.R;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.SurveriorRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment {

    private TextView name;
    private TextView phone;
    private TextView gender;
    private TextView birthdate;
    private TextView job;
    private TextView address;

    private ImageView changeName;
    private ImageView changePhone;
    private ImageView image;
    private SessionManager session;

    ImageLoader imageLoader;

    public static final String DATA_NAMA="";
    public static final String DATA_PHONE="";
    public static final String DATA_ID="";
    public static final String IMAGE="";
    public String id;

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
        phone = (TextView) rootView.findViewById(R.id.phone_frag);

        gender = (TextView) rootView.findViewById(R.id.gender_frag);
        birthdate = (TextView) rootView.findViewById(R.id.birthday_frag);
        job = (TextView) rootView.findViewById(R.id.job_frag);
        address = (TextView) rootView.findViewById(R.id.address_frag);
        image = (ImageView) rootView.findViewById(R.id.photo_frag);
        changeName = (ImageView) rootView.findViewById(R.id.changeName);
        changePhone = (ImageView) rootView.findViewById(R.id.changePhone);

        // session manager
        session = new SessionManager(getActivity().getApplicationContext());

        //set profile
        setProfile();

        //change avatar image
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                Intent intent = new Intent(getActivity(), UploadImageActivity.class);
                intent.putExtra(DATA_ID, id);
                intent.putExtra(IMAGE,bitmap);
                getActivity().startActivity(intent);
            }
        });

        //edit name
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra(DATA_NAMA, name.getText());
                getActivity().startActivity(intent);
            }
        });


        //edit phone
        changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditPhoneActivity.class);
                intent.putExtra(DATA_PHONE, phone.getText());
                getActivity().startActivity(intent);
            }
        });

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

    public void setImage(String idx) {
        String url = AppConfig.URL_PHOTO + "/" + idx + "/photo.jpg";

        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {

                    @Override
                    public void onResponse(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        // Access the RequestQueue.
        AppController.getInstance().addToRequestQueue(request);
    }

    public void setProfile(){

        final ProgressDialog loading = ProgressDialog.show(getContext(),"", "Please wait...", false, false);
        //set profile
        SurveriorRequest req;

        req = new SurveriorRequest(Request.Method.GET, AppConfig.URL_GET_USER_DATA, session,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            JSONObject jUser = jObj.getJSONObject("user");
                            loading.dismiss();
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
                            if(!jUser.getString("phone_number").equals("null")){
                                phone.setText(jUser.getString("phone_number"));
                            }else{
                                phone.setText("Enter your phone");
                            }
                            if(!jUser.getString("photo").equals("null")){
                                setImage(id);
                            }
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


//        UserHelper user = new UserHelper(getActivity().getApplicationContext());
//        Log.d("nama",user.toString());
//        name.setText(user.getName());
//        gender.setText(user.getGender());
//        birthdate.setText(user.getBirthdate());
//        job.setText(user.getJob());
//        address.setText(user.getCity()+", "+user.getProvince());
//        setImage(user.getId());
    }



}