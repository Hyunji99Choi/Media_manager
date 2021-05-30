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

    String IMAGE = "image";
    String VIDEO = "video";

    public GridAdapter(Activity context, ArrayList list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate = context.getLayoutInflater();
        View rowView = inflate.inflate(R.layout.view_gridview,null);

        ImageView imageView = rowView.findViewById(R.id.imageView);
        VideoView videoView = rowView.findViewById(R.id.videoView);

        gridViewAdapterData data = (gridViewAdapterData)list.get(position);
        if(data.getKind().equals(IMAGE)){
            imageView.setImageURI(Uri.parse(data.getUrl()));
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);

        }else if(data.getKind().equals(VIDEO)){
            Log.w("getview","VIDEO");
            MediaController mc = new MediaController(context);
            videoView.setMediaController(mc);
            videoView.setVideoPath(data.getUrl());
            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            videoView.start();
        }

        return rowView;
    }

    public void resetAll(ArrayList list){
        this.list = list;
        this.notifyDataSetChanged();
    }
}
