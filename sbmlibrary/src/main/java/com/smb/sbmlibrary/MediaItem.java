package com.smb.sbmlibrary;

import jcifs.smb.SmbFile;

/**
 * Created by zhijinjin (951507056@qq.com)
 * on 2018/5/31.
 */

public class MediaItem {
    private String name;
    //下载该文件的地址
    private String url_Load;
    //在线打开文件的地址
    private String url_Open;
    //下载进度
    private int rate = 0;
    //进度条可见
    private boolean visble = false;

    public boolean isVisble() {
        return visble;
    }

    public void setVisble(boolean visble) {
        this.visble = visble;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getUrl_Load() {
        return url_Load;
    }

    public void setUrl_Load(String url_Load) {
        this.url_Load = url_Load;
    }

    public String getUrl_Open() {
        return url_Open;
    }

    public void setUrl_Open(String url_Open) {
        this.url_Open = url_Open;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
