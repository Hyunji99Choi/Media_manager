package com.example.mediamanager.SearchPage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediamanager.DBHelper;
import com.example.mediamanager.MediaUpdate;
import com.example.mediamanager.R;

import java.util.ArrayList;


public class SearchListview extends AppCompatActivity {

    private ListView myListView;
    DBHelper mydb;
    SearchAdapter mAdapter;

    private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_search);

        editSearch = findViewById(R.id.editSearch);

        mydb = new DBHelper(this);
        ArrayList array_List = mydb.getAllMovies(); //db 모든 데이터 가져오기
        mAdapter = new SearchAdapter(this, array_List);

        myListView = findViewById(R.id.listView1);
        myListView.setAdapter(mAdapter); //adapter 세팅

        //listview item(항목) 클릭 이벤트
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg4) {
                //클랙한 view의 id를 리턴 받는다 (adapter의 getitemid를 이용)
                int id = (int) parent.getAdapter().getItemId(position);

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id",id);
                Intent intent = new Intent(getApplicationContext(), MediaUpdate.class);
                intent.putExtras(dataBundle); //intent에 해당 id 데이터 넣기
                startActivity(intent);
            }
        });

    }

    //검색을 수행하는 메소드
    public void search(View view){
        String charText = editSearch.getText().toString();
        mAdapter.resetAll(mydb.getSearchMovies(charText,charText));
        mAdapter.notifyDataSetChanged();
    }

    //onResume 콜백 메소드로 listview 닦아주기
    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.resetAll(mydb.getAllMovies());
    }
}