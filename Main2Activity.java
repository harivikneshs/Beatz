package com.devlab.beatz;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String mov=getIntent().getStringExtra("movie");
        ListView l=(ListView)findViewById(R.id.songs);
        String[] dummy={mov};
        ArrayAdapter<String> aa=new ArrayAdapter<String>(this,R.layout.layout,dummy);
        l.setAdapter(aa);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String url = "https://aozoeky4dglp5sh0-zippykid.netdna-ssl.com/wp-content/uploads/2018/10/ubuntu-18.10-on-a-chromebook.jpg";
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription(((TextView)view).toString());
                request.setTitle("Beatz");
// in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ((TextView)view).toString());

// get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                Toast.makeText(getApplicationContext(),"Downloading",Toast.LENGTH_LONG).show();
            }
        });
    }
}
