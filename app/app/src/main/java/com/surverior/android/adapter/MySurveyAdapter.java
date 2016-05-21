package com.surverior.android.adapter;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.surverior.android.R;
import com.surverior.android.activity.MainActivity;
import com.surverior.android.helper.Survey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Azhar Fauzan on 5/14/16.
 */
public class MySurveyAdapter extends RecyclerView.Adapter<MySurveyAdapter.SurveyViewHolder> {
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private List<Survey> surveyList;

    public MySurveyAdapter(List<Survey> surveyList) {
        this.surveyList = surveyList;
    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

    @Override
    public void onBindViewHolder(SurveyViewHolder surveyViewHolder, int i) {
        Survey q = surveyList.get(i);
        surveyViewHolder.name.setText(q.getName());
        surveyViewHolder.description.setText(q.getDescription());
        surveyViewHolder.download.setImageResource(R.drawable.ic_get_app_black_24dp);
    }

    @Override
    public SurveyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_mysurvey, viewGroup, false);

        return new SurveyViewHolder(itemView);
    }


    public class SurveyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView name;
        protected TextView description;
        protected ImageView download;

        public SurveyViewHolder(View v) {
            super(v);
            name =  (TextView) v.findViewById(R.id.cardlabel_name);
            description = (TextView)  v.findViewById(R.id.cardlabel_description);
            download = (ImageView) v.findViewById(R.id.download_survey);
            Log.d("DIBUAT","OK");
            download.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            Toast.makeText(view.getContext(),"Downloaded survey " + name.getText(),Toast.LENGTH_LONG).show();
            startDownload(view.getContext());
        }

        public void startDownload(Context c){
            mNotifyManager = (NotificationManager) c.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(c);
            mBuilder.setContentTitle("Download");
            mBuilder.setContentText("Download in progress");
            mBuilder.setSmallIcon(R.drawable.ic_get_app_black_24dp);
            new DownloadTask().execute("http://zarfan.web.id/foto/Azhar.jpg");
        }
    }

        private  class DownloadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {
            Log.d("DOWNLOAD","OK");
            Log.d("DOWNLOAD","" + sUrl[0]);
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, "Download/mysurvey.jpg");

                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();

                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                fileOutput.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                mBuilder.setProgress(100, values[0], false);
                mNotifyManager.notify(1, mBuilder.build());
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mBuilder.setProgress(100, 0, false);
                mNotifyManager.notify(1, mBuilder.build());
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                mBuilder.setContentText("Download complete");
                // Removes the progress bar
                mBuilder.setProgress(0, 0, false);
                mNotifyManager.notify(1, mBuilder.build());

            }
        }


    // For debugging
}
