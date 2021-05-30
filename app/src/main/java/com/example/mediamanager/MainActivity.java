package com.example.mediamanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //Handler handler;
    //Runnable runnable;

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Intent intent = new Intent(MainActivity.this, MainGridView.class);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(runnable, 5000);
        */

    }

    @Override
    protected void onStart() {
        super.onStart();
        thread = new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(5000);
                }catch (InterruptedException e){ }

                startActivity(new Intent(MainActivity.this, MainGridView.class));
                finish();
            }
        });
        thread.start();
    }


    public void close(View view){
        //handler.removeCallbacks(runnable); // cancle

        thread.interrupt(); //cancle

        startActivity(new Intent(MainActivity.this, MainGridView.class));
        finish();
    }
}