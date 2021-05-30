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
    TextView title;
    TextView date;
    TextView path;
    TextView memo;

    TextView kind;
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
            if (Value > DISPLAY_INSERT) { //검색 리스트뷰에서 접근
                Cursor rs = mydb.getData(Value);
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

                title.setText((CharSequence) t);
                date.setText((CharSequence) d);
                path.setText((CharSequence) p);
                memo.setText((CharSequence) m);
                kind.setText((CharSequence) k);

                mediaLayout.setVisibility(View.VISIBLE);
                urlBut.setText("변경"); //버튼 TEXT 변경

                if(k.equals(IMAGE)){
                    imageView.setImageURI(Uri.parse(p));
                    imageView.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                }else if(k.equals(VIDEO)){
                    Log.w("동영상","viedo");
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

    public void insert(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //int Value = extras.getInt("id");
            //if (Value > 0) {
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
            //}
        }
    }

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

    public void mediaButton(View view) {
        //권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // 권한이 허용되지 않음, 권한 요청
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/* video/*");
            startActivityForResult(intent, GET_GALLERY_MEDIA);

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_MEDIA && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedMediaUri = data.getData();
            if(selectedMediaUri.toString().contains(IMAGE)){
                kind.setText(IMAGE);
                imageView.setImageURI(selectedMediaUri);
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
            }else if(selectedMediaUri.toString().contains(VIDEO)){
                Log.w("동영상","viedo 입력");
                kind.setText(VIDEO);
                videoView.setVideoPath(String.valueOf(selectedMediaUri));
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                videoView.start();

            }

            path.setText(selectedMediaUri.toString());
        }
    }

    public String getPathFromURI(Uri ContentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver()
                .query(ContentUri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            res = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return res;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //권한을 허용 했을 경우
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/* video/*");
            startActivityForResult(intent, GET_GALLERY_MEDIA);

        }
    }


}
