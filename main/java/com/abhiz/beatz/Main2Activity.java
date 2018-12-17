package com.abhiz.beatz;

/**
 * Created by abhiz on 08-Dec-18.
 */

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    String url;
    ArrayList<String> link_text=new ArrayList<String>();
    ArrayList<Element> ls=new ArrayList<Element>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String mov=getIntent().getStringExtra("movie");
        final String movieName=getIntent().getStringExtra("movie_nm");
        Log.d("main2",movieName);
        ((TextView)findViewById(R.id.name)).setText(movieName);
        ListView l=(ListView)findViewById(R.id.songs);
        url="https://songspk.zone"+mov;
        new Main2Activity.song().execute();

        AssetManager am = getApplicationContext().getAssets();

        Typeface font = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "raleway.ttf"));
        ((TextView)findViewById(R.id.titlee)).setTypeface(font);
        ((TextView)findViewById(R.id.name)).setTypeface(font);

        ImageView back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                url= ls.get(position).attr("href").toString();
                String fileName=link_text.get(position);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription(fileName);
                request.setTitle(fileName);
                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                //Environment.DIRECTORY_DOWNLOADS
                request.setDestinationInExternalPublicDir("/beatz/"+movieName+"/", fileName+".mp3");

                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(getApplicationContext(),"Downloading",Toast.LENGTH_LONG).show();
            }
        });
    }


    public class song extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Main2Activity.this);
            mProgressDialog.setTitle("Searching Songs");
            mProgressDialog.setMessage("Searching...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            link_text.clear();
            ls.clear();
            String url2;
                try {
                    Document document = Jsoup.connect(url).get();
                    Element ee = document.select("div.page-tracklist-body").first();
                    Elements xx=ee.select("h3 > a");
                    Element link3=ee;
                    for(Element link:xx){
                        link_text.add(link.text().replace("\n", "").replace("\t", "").replace("&nbsp;", ""));
                        url2="https://songspk.zone"+link.attr("href");
                        Document document2 = Jsoup.connect(url2).get();
                        Elements eee= document2.select("a.btn.btn-block.btn-default");
                        for(Element link2:eee){
                            link3=link2;
                        }
                        ls.add(link3);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ListView l=(ListView) findViewById(R.id.songs);
            ArrayAdapter<String> a = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout, link_text);
            l.setAdapter(a);
            ((TextView)findViewById(R.id.titlee)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.titlee)).setText(a.getCount()+" Songs Found");
            l.setVisibility(View.VISIBLE);
            mProgressDialog.dismiss();
        }
    }


}