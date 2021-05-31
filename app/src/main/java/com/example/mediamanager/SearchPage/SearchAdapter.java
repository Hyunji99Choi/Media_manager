package com.example.mediamanager.SearchPage;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.mediamanager.R;
import java.util.ArrayList;


public class SearchAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList list;

    public SearchAdapter(Activity context, ArrayList list) {
        this.context = context;
        this.list = list;
    } //생성자

    @Override
    public int getCount() {
        return list.size();
    } //배열 크기를 반환

    @Override
    public Object getItem(int position) {
        return list.get(position);
    } //배열에 아이템을 현재 위치값을 넣어 가져옴.

    @Override
    public long getItemId(int position) {
        return ((ListViewAdapterData)list.get(position)).getId();
    } //위치값을 반환, 아이디를 반환해도됨.

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate = context.getLayoutInflater(); //inflate
        View rowView = inflate.inflate(R.layout.view_listview,null);

        TextView id = rowView.findViewById(R.id.id); //요소 연결
        TextView title = rowView.findViewById(R.id.title);
        TextView date = rowView.findViewById(R.id.date);;

        ListViewAdapterData listdata = (ListViewAdapterData)list.get(position);
        id.setText((String) Integer.toString(listdata.getId())); //데이터 세팅
        title.setText((String) listdata.getTitle());
        date.setText((String) listdata.getDate());

        return rowView;
    }

    public void resetAll(ArrayList list){
        this.list = list;
        this.notifyDataSetChanged();
    } //변경된 데이터 list에 적용
}
