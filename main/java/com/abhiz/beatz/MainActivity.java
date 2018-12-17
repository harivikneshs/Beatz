package com.abhiz.beatz;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String url = "https://songspk.zone/browse/bollywood-albums-col/a?page=1";
    String base_url= "https://songspk.zone";
    String m_name,songLink;
    ImageView bg;
    TextView tit;
    ProgressDialog mProgressDialog;
    ArrayList<String> link_text=new ArrayList<String>();
    ArrayList<Element> ls=new ArrayList<Element>();
    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView search_button=(ImageView)findViewById(R.id.imageView2) ;
        //Button search_button = (Button) findViewById(R.id.searchbutton);
        //Button search_button2 = (Button) findViewById(R.id.searchbutton2);
        final ImageView search_button2=(ImageView)findViewById(R.id.imageView3) ;
        final EditText e=(EditText)findViewById(R.id.search);
        final EditText e2=(EditText)findViewById(R.id.search2);
        final ListView l=(ListView) findViewById(R.id.lview);
        final ListView l2=(ListView) findViewById(R.id.lview2);
        final TabLayout tl=(TabLayout) findViewById(R.id.tab);
        bg=(ImageView)findViewById(R.id.banner);
        tit=(TextView) findViewById(R.id.title);


        final InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(e,
                InputMethodManager.SHOW_FORCED);

        AssetManager am = getApplicationContext().getAssets();

          font = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "raleway.ttf"));
          e.setTypeface(font);
          e2.setTypeface(font);
          tit.setTypeface(font);


        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               //Toast.makeText(getApplicationContext(),Integer.toString(tab.getPosition()),Toast.LENGTH_LONG).show();
                if(tab.getPosition()==0){
                    ((LinearLayout)findViewById(R.id.song)).setVisibility(View.GONE);
                    ((LinearLayout)findViewById(R.id.mov)).setVisibility(View.VISIBLE);
                    l.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    tit.setVisibility(View.GONE);
                    bg.setImageResource(R.drawable.cd);
                    bg.setVisibility(View.VISIBLE);
                    e.requestFocus();
                    inputManager.showSoftInput(e,
                            InputMethodManager.SHOW_FORCED);
                }
                if(tab.getPosition()==1){
                    ((LinearLayout)findViewById(R.id.mov)).setVisibility(View.GONE);
                    ((LinearLayout)findViewById(R.id.song)).setVisibility(View.VISIBLE);
                    l2.setVisibility(View.GONE);
                    l.setVisibility(View.GONE);
                    tit.setVisibility(View.GONE);
                    bg.setImageResource(R.drawable.ic_baseline_queue_music_24px);
                    bg.setVisibility(View.VISIBLE);
                    e2.requestFocus();
                    inputManager.showSoftInput(e2,
                            InputMethodManager.SHOW_FORCED);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





        search_button.requestFocus();


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        e.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                l.setVisibility(View.GONE);
                l2.setVisibility(View.GONE);
                e2.setText("");
                return false;
            }
        });


        e2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                l.setVisibility(View.GONE);
                l2.setVisibility(View.GONE);
                e.setText("");
                return false;
            }
        });

        // Capture button click
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                m_name = e.getText().toString().toLowerCase();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if(!m_name.isEmpty())
                new movie().execute();
            }
        });

        search_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_name = e2.getText().toString().toLowerCase();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if(!m_name.isEmpty())
                new song().execute();
            }
        });

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text= ls.get(position).attr("href").toString();   //=((TextView)view).getText().toString();
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                i.putExtra("movie",text);
                i.putExtra("movie_nm",ls.get(position).text());
                Log.d("namee",ls.get(position).text());
                Log.d("namee",text);
                //l.setVisibility(View.GONE);
                startActivity(i);
                //Toast.makeText(getApplicationContext(),Integer.toString(position),Toast.LENGTH_LONG).show();

            }
        });

        l2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                songLink= ls.get(position).attr("href").toString();   //=((TextView)view).getText().toString();
                m_name=ls.get(position).text().toString();
                new songDownload().execute();
                //l2.setVisibility(View.GONE);
            }
        });

    }


    // Movie search AsyncTask
    public class movie extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Searching Movie");
            mProgressDialog.setMessage("Searching Movies...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            link_text.clear();
            ls.clear();
            for(int pg=1;pg<2;pg++) {
                try {
                   // pg=55;
                    url=base_url+"/browse/bollywood-albums-col/" + m_name.toLowerCase().charAt(0) + "?page=" +Integer.toString(pg);
                    Document document = Jsoup.connect(url).get();
                    Elements e=document.select("a[href]");
                    /*if (document.select("div.archive-empty") != null)
                        Toast.makeText(getApplicationContext(),"found empty",Toast.LENGTH_LONG).show();*/
                    for(Element link:e){
                        String mov=link.text().toString().toLowerCase().replace("\n", "").replace("\t", "").replace("&nbsp;", "");
                        String sname=mov.split("-")[0].replaceAll(" ","");
                        if(sname.contains(m_name)) {
                            link_text.add(link.text().replace("\n", "").replace("\t", "").replace("&nbsp;", ""));
                            ls.add(link);
                            Log.d("urll",link.attr("href"));
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ListView l=(ListView) findViewById(R.id.lview);
            ArrayAdapter<String> a = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout, link_text);
            l.setAdapter(a);
            l.setVisibility(View.VISIBLE);
            tit.setText(a.getCount()+" Movies Found");
            bg.setVisibility(View.GONE);
            tit.setVisibility(View.VISIBLE);
            mProgressDialog.dismiss();
            //Toast.makeText(this,a.getItem(0),Toast.LENGTH_LONG).show();
        }
    }

    //Song search async task
    public class song extends AsyncTask<Void, Void, Void> {
        ArrayList<String> link_text2=new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Searching Song");
            mProgressDialog.setMessage("Searching...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            link_text.clear();
            ls.clear();
            String t=null;
            Elements xx,yy;
            try {
                url=base_url+"/search?q=" + m_name.toLowerCase();
                Document document = Jsoup.connect(url).get();
                Element e=document.select("div.single-songs.list-group.page-cat-wrap").first();
                if(e != null){
                     xx=e.select("h3 > a");
                     yy=e.select("p > a");
                }
                else
                    return null;
                for (Element link:xx){
                    link_text.add(link.text().replace("\n", "").replace("\t", "").replace("&nbsp;", ""));
                    ls.add(link);
                }
                int i=0;
                for (Element link:yy){
                    t = link_text.get(i++) + " (" + link.text().replace("\n", "").replace("\t", "").replace("&nbsp;", "") + ")";
                    link_text2.add(t);
                }

            }
            catch (IOException e) {
                    e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ListView l=(ListView) findViewById(R.id.lview2);
            ArrayAdapter<String> a = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout, link_text2);
            l.setAdapter(a);
            l.setVisibility(View.VISIBLE);
            bg.setVisibility(View.GONE);
            tit.setText(a.getCount()+" Songs Found");
            tit.setVisibility(View.VISIBLE);
            mProgressDialog.dismiss();
            //Toast.makeText(this,a.getItem(0),Toast.LENGTH_LONG).show();
        }
    }


    //Downloading songs of song query result
    public class songDownload extends AsyncTask<Void, Void, Void> {
        Element songFinalLink;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Getting Songs");
            mProgressDialog.setMessage("Starting...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url2;
            Element link3=null;
            try {
                url2=base_url+songLink;
                Document document2 = Jsoup.connect(url2).get();
                Elements ee= document2.select("a.btn.btn-block.btn-default");
                for(Element link2:ee){
                    link3=link2;
                }
                songFinalLink=link3;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            String fileName=m_name;
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(songFinalLink.attr("href").toString()));
            request.setDescription(fileName);
            request.setTitle(fileName);
            // in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            //Environment.DIRECTORY_DOWNLOADS
            request.setDestinationInExternalPublicDir("/beatz/", fileName+".mp3");

            // get download service and enqueue file
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            Toast.makeText(getApplicationContext(),"Downloading",Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
            bg.setVisibility(View.GONE);
        }
    }

}