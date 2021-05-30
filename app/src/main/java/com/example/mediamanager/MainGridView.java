 package com.example.mediamanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediamanager.SearchPage.SearchListview;

import java.util.ArrayList;

 public class MainGridView extends AppCompatActivity {

    int DISPLAY_INSERT = 0;

     DBHelper mydb;
     GridView gridView;
     GridAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_gridview);

        mydb = new DBHelper(this);
        //db에서 최근 추가된 미디어 중 최대16개의 데이터를 가져옴
        ArrayList array_List = mydb.getGridView();
        mAdapter = new GridAdapter(this, array_List);

        //gridview에 adapter 연결
        gridView = findViewById(R.id.GridView);
        gridView.setAdapter(mAdapter);


    }

    //액티비티가 전경에 위치할 때
    //즉, 화면이 다시 켜질때 실행
    //db에 대한 변경사항을 반영
     @Override
     protected void onResume() {
         super.onResume();
         //gridview 다시 setting
         mAdapter.resetAll(mydb.getGridView());
     }


    //메뉴 inflate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //메뉴 클릭
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.register: //등록
                Toast.makeText(this,"등록",Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putInt("id",DISPLAY_INSERT); //intent에 등록 이벤트라는 정보를 담음.
                Intent intent1 = new Intent(this, MediaUpdate.class);
                intent1.putExtras(bundle);
                startActivity(intent1); //등록 액티비티 실행

                return true;
            case R.id.search: //검색
                Toast.makeText(this,"검색",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, SearchListview.class);
                startActivity(intent2); //검색 액티비티 실행
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
