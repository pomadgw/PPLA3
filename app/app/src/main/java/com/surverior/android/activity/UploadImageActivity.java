package com.surverior.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.surverior.android.R;
import com.surverior.android.app.AppConfig;
import com.surverior.android.app.AppController;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.SurveriorRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Azhar Fauzan Dz on 4/22/2016.
 */
public class UploadImageActivity extends AppCompatActivity{

    private ImageButton pen;
    private Button save;
    private ImageView photo;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private SessionManager session;
    private File file;
    private String id;
    private Bitmap image;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimage);

        photo = (ImageView) findViewById(R.id.photo);
        pen = (ImageButton) findViewById(R.id.pen);
        back = (ImageView) findViewById(R.id.back);
        save = (Button) findViewById(R.id.save);

        // session manager
        session = new SessionManager(getApplicationContext());

        //getId from previous Activity
        Intent intent = getIntent();
        id = intent.getStringExtra(ProfileFragment.DATA_ID);
        image = (Bitmap) intent.getParcelableExtra(ProfileFragment.IMAGE);

        //setImage
        photo.setImageBitmap(image);

        //button Click
        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose photo/image
                showFileChooser();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadImageActivity.this,
                        MainActivity.class);
                intent.putExtra("FROM_IMAGE","OK");
                startActivity(intent);

                finish();

              //  onBackPressed();

            }
        });

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        if(bitmap ==null){
            Toast.makeText(UploadImageActivity.this, "Please choose your photo first" , Toast.LENGTH_LONG).show();
        }else {
            //Showing the progress dialog
            final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
            SurveriorRequest stringRequest = new SurveriorRequest(Request.Method.POST, AppConfig.URL_UPDATE, session,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Disimissing the progress dialog
                            loading.dismiss();
                            //Showing toast message of the response
                            Toast.makeText(UploadImageActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Dismissing the progress dialog
                            loading.dismiss();

                            //Showing toast
                            Toast.makeText(UploadImageActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                    String image = getStringImage(bitmap);

                    //Creating parameters
                    Map<String, String> params = new Hashtable<String, String>();

                    //Adding parameters
                    params.put("photo", image);

                    //returning parameters
                    return params;
                }
            };

            //Adding request to the queue
            AppController.getInstance().addToRequestQueue(stringRequest);
        }

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                file = new File (filePath.getPath());
                //Setting the Bitmap to ImageView
                photo.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}