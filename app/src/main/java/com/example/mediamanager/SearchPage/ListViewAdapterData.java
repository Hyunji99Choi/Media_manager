package com.example.mediamanager.SearchPage;

public class ListViewAdapterData {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private int id; //데이터 id
    private String title; //TITLE
    private String date; //촬영일자

}
