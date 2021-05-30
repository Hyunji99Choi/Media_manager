package com.example.mediamanager;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    Activity context;
    ArrayList list;

    //이미지 비디오를 구분할 상수
    String IMAGE = "image";
    String VIDEO = "video";

    //생성자
    public GridAdapter(Activity context, ArrayList list){
        this.context=context;
        this.list=list;
    }

    //어뎁터에 사용되는 데이터 개수 리턴
    @Override
    public int getCount() {
        return list.size();
    }

    //position에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    //position에 잇는 데이터의 id 리턴(null로 구현해도 됨)
    //gridView는 id값이 딱히 필요 없기 때문에 신경쓰지 않음.
    @Override
    public long getItemId(int position) {
        return position;
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 view 리턴.
    //getCount값이 0이면 호출하지 않는다.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate = context.getLayoutInflater();
        View rowView = inflate.inflate(R.layout.view_gridview,null);

        //xml를 inflate하고 각 요소 연결
        ImageView imageView = rowView.findViewById(R.id.imageView);
        VideoView videoView = rowView.findViewById(R.id.videoView);

        gridViewAdapterData data = (gridViewAdapterData)list.get(position);
        if(data.getKind().equals(IMAGE)){ //view 내용(미디어)이 이미지일 경우
            imageView.setImageURI(Uri.parse(data.getUrl()));
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE); //video view는 닫기(안보이기)

        }else if(data.getKind().equals(VIDEO)){ //미디어가 영상일 경우
            MediaController mc = new MediaController(context);
            videoView.setMediaController(mc); //video controller 연결(재생/멈춤 등)
            videoView.setVideoPath(data.getUrl());
            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE); //image view는 닫기(안보이기)

            //많은 동영상이 한꺼번에 재생되면 오류가
            //생기기 때문에 재생 시작은 해주지 않는다.
            //videoView.start();
        }
        return rowView;
    }

    //gridview 내용이 변경될때마다 호출하려고 만든 메소드
    public void resetAll(ArrayList list){
        this.list = list;
        this.notifyDataSetChanged();
    }
}
