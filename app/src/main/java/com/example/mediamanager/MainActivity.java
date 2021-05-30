package com.example.mediamanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Thread thread;

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

                //메인화면인 gridview 액티비티 실행
                startActivity(new Intent(MainActivity.this, MainGridView.class));
                //표지 액티비티 종료
                finish();
            }
        });
        thread.start();
    }

    // 닫기 버튼 클릭 콜백 함수
    public void close(View view){
        //버튼 클릭시 thread 취소(페이지가 두번 켜지는것을 방지)
        thread.interrupt();

        startActivity(new Intent(MainActivity.this, MainGridView.class));
        finish(); //표지 액티비티 종료
    }
}