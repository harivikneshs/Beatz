package com.devlab.beatz;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(ConnectivityManager.EXTRA_NO_CONNECTIVITY.equals("noConnectivity"))
           Toast.makeText(this,"Connect to Internet",Toast.LENGTH_LONG);

        final ListView l=(ListView) findViewById(R.id.lview);
        EditText e=(EditText)findViewById(R.id.search);
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!ConnectivityManager.EXTRA_NO_CONNECTIVITY.equals("noConnectivity")) {
                    ArrayList<String> t = findmovie(s.toString());
                    ArrayAdapter<String> a = new ArrayAdapter<String>(getApplicationContext(), R.layout.layout, t);
                    l.setAdapter(a);
                    l.setVisibility(View.VISIBLE);
                }

            }
        });

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text=((TextView)view).getText().toString();
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                i.putExtra("movie",text);
                startActivity(i);
                Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

            }
        });

    }

    private ArrayList<String> findmovie(String s) {

        ArrayList<String> ls=new ArrayList<String>();
        Document d=null;
        for(int pg=1;pg<26;pg++) {


            try {
                 d = Jsoup.connect("http://www.ssaurel.com/blog" + "/" + s.toLowerCase() + "?page=" +Integer.toString(pg)).get();

            } catch (Exception e) {
                e.printStackTrace();
            }

            Elements e=d.select("s");
            for(Element link:e){
                String mov=link.toString().replace("\n", "").replace("\t", "").replace("&nbsp;", "");
                String sname=mov.split("-")[0].replaceAll(" ","");
                if(s.matches("*"+sname+"*"));
                    ls.add(sname);


            }


        }

        return ls;
    }

}
