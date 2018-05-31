package com.smb.sbmlibrary;

import jcifs.smb.SmbFile;

/**
 * Created by zhijinjin (951507056@qq.com)
 * on 2018/5/31.
 */

public class MediaItem {
    private String name;
    private String url_Smb;
    private String url_Http;


    public String getUrl_Smb() {
        return url_Smb;
    }

    public void setUrl_Smb(String url_Smb) {
        this.url_Smb = url_Smb;
    }

    public String getUrl_Http() {
        return url_Http;
    }

    public void setUrl_Http(String url_Http) {
        this.url_Http = url_Http;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
