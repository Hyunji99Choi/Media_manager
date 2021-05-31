package com.example.mediamanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Thread thread;
    boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //표지 layout 연결
    }

    @Override
    protected void onStart() {
        super.onStart();
        thread = new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(5000); //5초 대기
                }catch (InterruptedException e){ }

                //running이 true 일때만 실행 -> false: 버튼클릭,창 닫기
                if(running){
                    //메인화면인 gridview 액티비티 실행
                    startActivity(new Intent(MainActivity.this, MainGridView.class));
                    //표지 액티비티 종료
                    finish();
                }

            }
        });
        thread.start();
    }

    //표지화면에서 앱을 종료하면
    //진행중이던 쓰레드에서 액티비티 호출 막기
    //해주지 않으면 앱을 종료후에도
    //5초 경과 후 메인화면이 켜짐.
    @Override
    protected void onStop() {
        super.onStop();
        running = false;
    }

    // 닫기 버튼 클릭 콜백 함수
    public void close(View view){
        //버튼 클릭시 thread 취소(페이지가 두번 켜지는것을 방지)
        //후에 running 변수를 추가하면서 필요없어 졌다.
        //thread.interrupt();
        running = false;

        startActivity(new Intent(MainActivity.this, MainGridView.class));
        finish(); //표지 액티비티 종료
    }
}