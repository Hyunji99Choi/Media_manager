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
        ArrayList array_List = mydb.getGridView();
        mAdapter = new GridAdapter(this, array_List);

        gridView = findViewById(R.id.GridView);
        gridView.setAdapter(mAdapter);


    }

     @Override
     protected void onResume() {
         super.onResume();
         mAdapter.resetAll(mydb.getGridView());
     }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.register: //등록
                Toast.makeText(this,"등록",Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putInt("id",DISPLAY_INSERT);
                Intent intent1 = new Intent(this, MediaUpdate.class);
                intent1.putExtras(bundle);
                startActivity(intent1);

                return true;
            case R.id.search: //검색
                Toast.makeText(this,"검색",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this, SearchListview.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
