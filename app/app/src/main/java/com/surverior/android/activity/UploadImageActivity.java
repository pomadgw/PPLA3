package com.surverior.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;

import com.surverior.android.R;

/**
 * Created by Azhar Fauzan Dz on 4/22/2016.
 */
public class UploadImageActivity extends Activity {

    private ImageButton pen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimage);



    }

    public void openGallery(int req_code) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select file to upload "), req_code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            if (requestCode == SELECT_FILE1) {
                selectedPath1 = getPath(selectedImageUri);
                System.out.println("selectedPath1 : " + selectedPath1);
            }

            if (requestCode == SELECT_FILE2) {
                selectedPath2 = getPath(selectedImageUri);
                System.out.println("selectedPath2 : " + selectedPath2);
            }

            tv.setText("Selected File paths : " + selectedPath1 + "," + selectedPath2);
        }
    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
