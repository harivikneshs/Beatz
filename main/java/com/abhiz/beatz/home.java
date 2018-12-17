package com.abhiz.beatz;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class home extends AppCompatActivity {

    ArrayList<String> mov;
    ArrayList<String> lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        AssetManager am = getApplicationContext().getAssets();

        Typeface font = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "raleway.ttf"));
        ((TextView)findViewById(R.id.beatz)).setTypeface(font);
        ((TextView)findViewById(R.id.textView2)).setTypeface(font);
        ((TextView)findViewById(R.id.prime_search)).setTypeface(font);

        EditText e=(EditText)findViewById(R.id.prime_search);
       e.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i= new Intent(getApplicationContext(),MainActivity.class);
               startActivity(i);
           }
       });

        ListView trend=(ListView)findViewById(R.id.trend);
        mov=new ArrayList<String>();
        mov.clear();
        mov.add("Sanju");
        mov.add("Thugs of Hindostan");
        lin=new ArrayList<String>();
        lin.add("/sanju-2018-hindi-mp3-songs-spp03.html");
        lin.add("/thugs-of-hindostan-hindi-2018-mp3-song-spp03.html");
        ArrayAdapter<String> a=new ArrayAdapter<String >(this,R.layout.layout,mov);
        trend.setAdapter(a);
        trend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                i.putExtra("movie",lin.get(position));
                i.putExtra("movie_nm",mov.get(position));
                startActivity(i);
            }
        });
    }
}
