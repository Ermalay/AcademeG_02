package ru.novil.sergey.navigationdraweractivity.other;

import android.app.Application;

public class MyApplication extends Application {

    private String pageTokenAca = "";
    private String pageTokenAca2nd = "";

    public String getPageTokenAca(){
        return pageTokenAca;
    }

    public String getPageTokenAca2nd(){
        return pageTokenAca2nd;
    }

    public void setPageTokenAca(String sPageToken){
        pageTokenAca = sPageToken;
    }

    public void setPageTokenAca2nd(String sPageToken2nd){
        pageTokenAca2nd = sPageToken2nd;
    }
}
