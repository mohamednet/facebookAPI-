package com.example.ali.smallmobileappapi.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ali.smallmobileappapi.MainActivity;
import com.example.ali.smallmobileappapi.R;
import com.example.ali.smallmobileappapi.dataalbum.Album;
import com.example.ali.smallmobileappapi.dataalbum.Albumadapter;
import com.example.ali.smallmobileappapi.dataalbum.Photoadapter;
import com.example.ali.smallmobileappapi.dataphoto.Photo;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ListPhoto extends AppCompatActivity {
   private String album_id;
   private String values;
   private TextView txt;
   private List<String> url_imagealbum;

    private RecyclerView recyclerView;
    private Photoadapter photoadapter;
    final ArrayList<Photo> photos =  new ArrayList<Photo>();
    Button btnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        album_id =(String) getIntent().getStringExtra("album_id");
        OnClick();
       // 185438311500082/photos?fields=picture&limit=100
        btnd = (Button) findViewById(R.id.buttondawn);
        btnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Photo x:photos) {
                    Downloadtasl downloadtasl =  new Downloadtasl(x.getId());
                    downloadtasl.execute(x.getUrl_photo());
                }

            }
        });

    }
    String src ;
    public void OnClick(){
        Bundle parameters = new Bundle();
        parameters.putString("fields", "images");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+album_id+"/photos",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject =  response.getJSONObject();
                        try {
                            JSONArray array = jsonObject.getJSONArray("data");
                            for(int i = 0; i < array.length(); i++) {
                                JSONObject onephoto = array.getJSONObject(i);
                              /*  albums.add(new Album(oneAlbum.getString("created_time"),oneAlbum.getString("id"),
                                        oneAlbum.getString("name"),
                                        oneAlbum.getJSONObject("picture").getJSONObject("data").getString("url")));
                                //get your values
                                // this will return you the album's name.*/
                                try {
                                    JSONArray aray = onephoto.getJSONArray("images");
                                    for(int j = 0; j<= 1; j++) {
                                        JSONObject onphoto = aray.getJSONObject(j);
                                        src =(String) onphoto.getString("source");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                photos.add( new Photo(onephoto.getString("id"),src));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerView = (RecyclerView) findViewById(R.id.rv_photolist);
                        photoadapter = new Photoadapter(ListPhoto.this,photos);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ListPhoto.this));
                        recyclerView.setAdapter(photoadapter);
                    }
                }
        ).executeAsync();
    }



    File new_folder;
    File inpute_file;
    public class Downloadtasl extends AsyncTask<String,Integer,String> {
        String textView;

        public Downloadtasl(String textView) {
            this.textView = textView;
        }

        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... param) {
            String path = param[0];
            int file_lenght = 0;
            try {
                URL url =  new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_lenght = urlConnection.getContentLength();
                new_folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"facebookphoto");
                if(!new_folder.exists()){
                    new_folder.mkdir();
                }
                inpute_file =  new File(new_folder,textView+".jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(inpute_file);

                while((count=inputStream.read(data))!=-1){
                    total+=count;
                    outputStream.write(data,0,count);
                    int prograss = (int)total*100/file_lenght;
                    publishProgress(prograss);
                }
                inputStream.close();
                outputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "Waiting Download complete.....";
        }

        @Override
        protected void onPreExecute() {
            progressDialog =  new ProgressDialog(ListPhoto.this);
            progressDialog.setTitle("Download in Progress.....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            String path =inpute_file.getPath();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }
    }
}
