package com.example.mediamanager;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
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
        if(data.getKind().equals(IMAGE)){ //미디어가 이미지일 경우
            imageView.setImageURI(Uri.parse(data.getUrl()));
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);

        }else if(data.getKind().equals(VIDEO)){ //미디어가 영상일 경우
            MediaController mc = new MediaController(context);
            videoView.setMediaController(mc);
            videoView.setVideoPath(data.getUrl());
            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);

            //많은 동영상이 한꺼번에 재생되면 오류가
            //생기기 때문에 재생 시작은 해주지 않는다.
            //videoView.start();
        }

        return rowView;
    }

    public void resetAll(ArrayList list){
        this.list = list;
        this.notifyDataSetChanged();
    }
}
