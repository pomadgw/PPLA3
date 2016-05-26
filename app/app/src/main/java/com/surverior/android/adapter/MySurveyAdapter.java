package com.surverior.android.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.surverior.android.R;
import com.surverior.android.app.AppConfig;
import com.surverior.android.helper.SessionManager;
import com.surverior.android.helper.Survey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Azhar Fauzan on 5/14/16.
 */
public class MySurveyAdapter extends RecyclerView.Adapter<MySurveyAdapter.SurveyViewHolder> {
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private List<Survey> surveyList;

    private String token;

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
        surveyViewHolder.id = q.getID();

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
        protected int id;

        public SurveyViewHolder(View v) {
            super(v);
            name =  (TextView) v.findViewById(R.id.cardlabel_name);
            description = (TextView)  v.findViewById(R.id.cardlabel_description);
            download = (ImageView) v.findViewById(R.id.download_survey);
            download.setOnClickListener(this);
            //take token
            SessionManager session = new SessionManager(v.getContext());
            token = session.getToken();
        }

        @Override
        public void onClick(final View view) {
            Toast.makeText(view.getContext(),"Downloading survey " + name.getText(),Toast.LENGTH_LONG).show();
            startDownload(view.getContext());
        }

        public void startDownload(Context c){
            mNotifyManager = (NotificationManager) c.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(c);
            String [] names = name.getText().toString().split(" ");
            String namaFile=names[0];
            if(names.length > 1){
                namaFile=names[0]+"_"+names[1];
            }
            mBuilder.setContentTitle("Download " + namaFile+".xls");
            mBuilder.setContentText("Download in progress");
            mBuilder.setSmallIcon(R.drawable.ic_get_app_black_24dp);
            String url = AppConfig.URL_DOWNLOAD_SURVEY;
            new DownloadTask(name.getText().toString(),c.getApplicationContext()).execute(url+id+"/answers/xls?token="+token);

        }
    }

        private  class DownloadTask extends AsyncTask<String, Integer, String> {
            private Context mContext;
            private PendingIntent pIntent;
            private String name;
            public DownloadTask(String name, Context context){
                this.name = name;
                this.mContext = context;
            }
            @Override
            protected String doInBackground(String... sUrl) {
                Log.d("DOWNLOAD","OK");
                try {
                    URL url = new URL(sUrl[0]);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    Log.d("ISEMPTY","" + url.getContent().toString());
                    
                    InputStream inputStream = urlConnection.getInputStream();
                    File sdcard = Environment.getExternalStorageDirectory();
                    String [] names = name.split(" ");
                    String namaFile=names[0];
                    if(names.length > 1){
                        namaFile=names[0]+"_"+names[1];
                    }
                    File file = new File(sdcard, "Download/"+namaFile+".xls");
                    FileOutputStream fileOutput = new FileOutputStream(file);

                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setData(Uri.fromFile(file));

                    pIntent = PendingIntent.getActivity(this.mContext,0,intent,0);

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
                    mNotifyManager.notify(1,  mBuilder.setContentIntent(pIntent).build());

                }
        }

}
