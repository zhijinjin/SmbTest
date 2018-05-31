package com.smb.sbmlibrary;


import com.smb.sbmlibrary.util.SearchShareFile;

import java.net.InetAddress;

import jcifs.smb.SmbFile;

/**
 * Created by zhijinjin (951507056@qq.com)
 * on 2018/5/30.
 */

public class SmbUtil {


    private static SmbUtil smbUtil;
    //共享电脑的登陆用户名
    public  String SHARE_NAME = "";
    //共享电脑的登陆用户密码
    public  String SHARE_PWD = "";
    InetAddress bindAddr;
    private String[] types = new String[]{};
    private SearchShareFile.OnSearchShareFile searchShareFileLisener;
    private LoadShareFile.ShareFileLoadLesner onLoadShareFileLisner;
    private int pc= 1;

    public static SmbUtil getInstence(){
        if (smbUtil==null){
            smbUtil = new SmbUtil();
        }
        return smbUtil;
    }

    private SmbUtil(){
        System.setProperty("jcifs.smb.client.dfs.disabled", "true");
    }

    /**
     * 查询共享文件
     * @param url 共享文件路径 例如：192.168.1.101/迅雷下载/
     */
    public void searchShareFile(String url){
        SearchShareFile searchShareFile = new SearchShareFile();
        searchShareFile.setTypes(types);
        searchShareFile.setSearchShareFileLisener(searchShareFileLisener);
        searchShareFile.execute(url);
    }

    public void loadShareFile(String url_smb,String folderName){
        loadShareFile(url_smb,folderName,1);
    }

    public void loadShareFile(String url_smb,String folderName,int pc){
        LoadShareFile loadShareFile = new LoadShareFile(folderName);
        loadShareFile.setShareFileLoadLesner(onLoadShareFileLisner);
        loadShareFile.setPC(pc);
        loadShareFile.execute(url_smb);
    }



    public String getSHARE_NAME(){
        return SHARE_NAME;
    }

    public String getSHARE_PWD(){
        return SHARE_PWD;
    }



    public SmbUtil setShareName(String name){
        this.SHARE_NAME = name;
        return smbUtil;
    }

    public SmbUtil setSharePwd(String pwd){
        this.SHARE_PWD = pwd;
        return smbUtil;
    }

    /**
     * 设置要显示的文件类型
     * @param types
     */
    public SmbUtil setTypes(String[] types){
        this.types = types;
        return smbUtil;
    }

    public SmbUtil setSearchShareFileLisener(SearchShareFile.OnSearchShareFile onSearchShareFile){
        this.searchShareFileLisener = onSearchShareFile;
        return smbUtil;
    }

    public SmbUtil setLoadShareFileLisener(LoadShareFile.ShareFileLoadLesner onLoadShareFileLisner){
        this.onLoadShareFileLisner = onLoadShareFileLisner;
        return smbUtil;
    }

    public void setPC(int pc){
        this.pc = pc;
    }
}
