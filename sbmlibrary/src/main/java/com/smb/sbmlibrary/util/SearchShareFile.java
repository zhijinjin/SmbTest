package com.smb.sbmlibrary.util;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.smb.sbmlibrary.MediaItem;
import com.smb.sbmlibrary.SmbUtil;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbSession;

/**
 * Created by zhijinjin (951507056@qq.com)
 * on 2018/5/31.
 */

public class SearchShareFile  extends AsyncTask<String, Void, ArrayList<MediaItem>> {

    private OnSearchShareFile searchShareFileLisener;
    private String[] types = new String[]{};
    private boolean searchSucess = false;

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected ArrayList<MediaItem> doInBackground(String... params){
        ArrayList<MediaItem> list = new ArrayList<>();
        searchSucess = false;
        try{
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                    "", SmbUtil.getInstence().getSHARE_NAME() ,SmbUtil.getInstence().getSHARE_PWD());
            String ip = params[0].contains("/") ? params[0].substring(0,params[0].indexOf("/")):params[0];
            UniAddress dc = UniAddress.getByName(ip);
            SmbSession.logon(dc, auth);

            SmbFile smbFile;
            if(params[0].endsWith("/")){
                smbFile = new SmbFile("smb://"+params[0] ,auth);
            }else{
                smbFile = new SmbFile("smb://"+params[0] +"/" ,auth);
            }
            SmbFile[] fs = smbFile.listFiles();

            for (int i=0;i<fs.length;i++){
                String path = fs[i].getPath();
                if (includeType(path)){
                    try{
                        MediaItem item = new MediaItem();
                        item.setName(path.substring(path.lastIndexOf("/")+1));
                        String httpReq = "http://" +  FileUtil.ip + ":" + FileUtil.port + "/smb=";
                        item.setUrl_Load(path.substring(6));
                        item.setUrl_Open(httpReq + URLEncoder.encode(path.substring(6), "UTF-8"));
                        list.add(item);
                    }
                    catch (UnsupportedEncodingException e){
                        sentHandlerMsg(1,e.getMessage());
                        e.printStackTrace();
                        return list;
                    }
                }
            }
        }
        catch (MalformedURLException e){
            sentHandlerMsg(1,e.getMessage());
            e.printStackTrace();
            return list;
        }
        catch (SmbException e){
            sentHandlerMsg(1,e.getMessage());
            e.printStackTrace();
            return list;
        }catch (Exception e){
            e.printStackTrace();
            sentHandlerMsg(1,e.getMessage());
            return list;
        }
        searchSucess = true;
        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<MediaItem> result){
        super.onPostExecute(result);
        if (searchSucess && null!=searchShareFileLisener){
            searchShareFileLisener.onSearchShareFileResult(result);
        }
    }



    public interface OnSearchShareFile{
        /**
         * 获取共享文件成功
         * @param result 文件列表
         */
        void onSearchShareFileResult(ArrayList<MediaItem> result);

        /**
         * 获取共享文件失败
         * @param e 异常信息
         */
        void onSearchShareFileFaild(String e);
    }

    public void setSearchShareFileLisener(OnSearchShareFile onSearchShareFile){
        this.searchShareFileLisener = onSearchShareFile;
    }

    private boolean includeType(String path){
        for(int i=0;i<types.length;i++){
            if(path.endsWith(types[i])){
                return true;
            }
        }
        return false;
    }


    /**
     * 设置要显示的文件类型
     * @param types
     */
    public void setTypes(String[] types){
        this.types = types;
    }

    private void sentHandlerMsg(int what,String msgStr){
        Message msg = new Message();
        msg.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("msg",msgStr);
        msg.setData(bundle);
        myHandler.sendMessage(msg);
    }

    Handler myHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: //失败
                    if(null!=searchShareFileLisener){
                        searchShareFileLisener.onSearchShareFileFaild(msg.getData().getString("msg"));
                    }
                    break;
            }
        }
    };

}
