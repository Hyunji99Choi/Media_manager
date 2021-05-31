package com.example.mediamanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MediaUpdate extends AppCompatActivity {

    private final int GET_GALLERY_MEDIA = 200;

    private DBHelper mydb;
    TextView title, date, path, memo, kind;

    String IMAGE = "image";
    String VIDEO = "video";

    LinearLayout mediaLayout;
    ImageView imageView;
    VideoView videoView;
    Button urlBut;
    int id = 0;

    int DISPLAY_INSERT = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inset_update_disply);

        //각 요소 연결
        title = findViewById(R.id.editTextTitle);
        date = findViewById(R.id.editTextDate);
        path = findViewById(R.id.editTextUrl);
        memo = findViewById(R.id.editTextMemo);
        kind = findViewById(R.id.textViewKind);

        mediaLayout = findViewById(R.id.mediaViewLayout);
        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        urlBut = findViewById(R.id.urlButton);

        mydb = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > DISPLAY_INSERT) { // 검색 리스트뷰에서 접근했을 때
                Cursor rs = mydb.getData(Value); // id로 데이터 가져오기
                id = Value;
                rs.moveToFirst();
                String t = rs.getString(rs.getColumnIndex(DBHelper.MOVIES_COLUMN_TITLE));
                String d = rs.getString(rs.getColumnIndex(DBHelper.MOVIES_COLUMN_DATE));
                String m = rs.getString(rs.getColumnIndex(DBHelper.MOVIES_COLUMN_MEMO));
                String p = rs.getString(rs.getColumnIndex(DBHelper.MOVIES_COLUMN_PATH));
                String k = rs.getString(rs.getColumnIndex(DBHelper.MOVIES_COLUMN_KINDS));
                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b = findViewById(R.id.save);
                b.setVisibility(View.INVISIBLE); // no insert.

                //데이터 세팅
                title.setText((CharSequence) t);
                date.setText((CharSequence) d);
                path.setText((CharSequence) p);
                memo.setText((CharSequence) m);
                kind.setText((CharSequence) k);

                //미디어 view가 있는 layout을 visible
                mediaLayout.setVisibility(View.VISIBLE);
                urlBut.setText("변경"); //버튼 TEXT 변경

                if(k.equals(IMAGE)){ //db에 저장된 파일경로 미디어가 이미지
                    imageView.setImageURI(Uri.parse(p));
                    imageView.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                }else if(k.equals(VIDEO)){ //db에 저장된 파일경로 미디어가 동영상
                    MediaController mc = new MediaController(this);
                    videoView.setMediaController(mc);
                    videoView.setVideoPath(p);
                    videoView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    videoView.start();
                }
            }
        }
    }

    //등록 버튼 클릭시, db에 insert
    public void insert(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title.getText().toString();
            date.getText().toString(); path.getText().toString();
            if (mydb.insertMovie(title.getText().toString(), date.getText().toString(),
                    path.getText().toString(), memo.getText().toString(), kind.getText().toString())) {
                Toast.makeText(getApplicationContext(), "추가되었음", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "추가되지 않았음", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    //정보 tkrwp 버튼 클릭시, db에 delete
    public void delete(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                mydb.deleteMovie(id);
                Toast.makeText(getApplicationContext(), "삭제되었음", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "삭제되지 않았음", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //정보 변경 버튼 클릭시, db에 update
    public void edit(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateMovie(id, title.getText().toString(), date.getText().toString(),
                        path.getText().toString(), memo.getText().toString(), kind.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "수정되었음", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "수정되지 않았음", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //이미지나 동영상 등록 버튼 클릭시
    public void mediaButton(View view) {
        //권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // 권한이 허용되지 않음, 권한 요청
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            //암묵적 인텐트, 갤러리에서 이미지 또는 동영상 가져오기
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/* video/*");
            startActivityForResult(intent, GET_GALLERY_MEDIA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //갤러리에서 돌아왔을때 호출
        if (requestCode == GET_GALLERY_MEDIA && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedMediaUri = data.getData();
            if(selectedMediaUri.toString().contains(IMAGE)){ //선택한게 이미지
                kind.setText(IMAGE); //미디어 종류를 gone설정된 textview에 세팅
                imageView.setImageURI(selectedMediaUri); //이미지 세팅
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
            }else if(selectedMediaUri.toString().contains(VIDEO)){ //선택한게 동영상
                kind.setText(VIDEO); //나중에 db 저장을 위함
                videoView.setVideoPath(String.valueOf(selectedMediaUri)); //동영상세팅
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                videoView.start();
            }
            path.setText(selectedMediaUri.toString()); // 파일 경로명 세팅
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //권한을 허용 했을 경우
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/* video/*");
            startActivityForResult(intent, GET_GALLERY_MEDIA); //갤러리 암묵적 인텐트
        }
    }
}
