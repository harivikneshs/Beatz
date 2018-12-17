package com.abhiz.beatz;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);




        Timer t= new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(),home.class);
                try {
                    ConnectivityManager conn=null;
                            conn= ((ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE));
                    if ((conn!=null) && (conn.getActiveNetworkInfo().isConnected()==true))
                        startActivity(i);
                    else {
                        Toast.makeText(getApplicationContext(), "Connect to Internet and Try again", Toast.LENGTH_LONG).show();
                       wait(1000);
                    }
                }
                catch (Exception e){
                   e.printStackTrace();
                }

                finish();
            }
        },2000);


    }
}
