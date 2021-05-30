package com.example.mediamanager;

public class gridViewAdapterData {

    public String getKind() { return kind; }
    public void setKind(String kind) { this.kind = kind; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    private String url; //미디어 위치
    private String kind; //미디어가 영상인지 동영상인지 구분
}
