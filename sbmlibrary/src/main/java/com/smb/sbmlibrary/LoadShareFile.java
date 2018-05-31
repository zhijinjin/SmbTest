package com.smb.sbmlibrary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.smb.sbmlibrary.util.SDCardUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbSession;

/**
 * Created by zhijinjin (951507056@qq.com)
 * on 2018/5/31.
 */

public class LoadShareFile extends AsyncTask<String, Boolean, Boolean> {

    private SmbFile smbFile;
    //保存文件的文件夹的名字
    private String folderName;
    //文件总大小
    private int allSize = 0;
    //一下载大小
    private int allLoad = 0;
    private ShareFileLoadLesner fileLoadLesner;
    private long lastTime;
    private int pc=1;


    public LoadShareFile(String folderName){
        this.folderName = folderName;
    }

    //params[0] 地址，params[1]保存路径
    @Override
    protected Boolean doInBackground(String... params) {
        BufferedInputStream  in = null;
        BufferedOutputStream out = null;
        String fileName = "";
        try{
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                        "", SmbUtil.getInstence().getSHARE_NAME() ,SmbUtil.getInstence().getSHARE_PWD());
            UniAddress dc = UniAddress.getByName(params[0].substring(0,params[0].indexOf("/")));
            SmbSession.logon(dc, auth);
            smbFile = new SmbFile("smb://"+params[0],auth);
            fileName = (params[0].substring(params[0].lastIndexOf("/")+1));
            //获取文件总大小
            allSize = smbFile.getContentLength();
            //判断存储空间是否够用
            if(SDCardUtils.getFreeBytes(SDCardUtils.getStorePath())<allSize){
                sentHandlerMsg(1,"存储空间不足");
                return false;
            }
            //开始
            sentHandlerMsg(3,"");

            lastTime = new Date().getTime();

            //文件夹
            File destDir = new File(SDCardUtils.getStorePath()  + folderName);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            File localFile  = new File(SDCardUtils.getStorePath()  + folderName +"/" + fileName);

            SmbFileInputStream ins =  new SmbFileInputStream(smbFile);
            in = new BufferedInputStream(ins); // 建立smb文件输入流
            out = new BufferedOutputStream(new FileOutputStream(localFile));

            byte[] buffer = new byte[2048];
            int len = 0;
            while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
                allLoad = allLoad + len;
                if(( new Date().getTime()-lastTime)>1000*pc){
                    lastTime = new Date().getTime();
                    sentHandlerMsg(4,((allLoad*100)/allSize)+"");
                }
            }
            out.flush();
            //下载成功
        }catch (MalformedURLException e){
            sentHandlerMsg(1,e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch (SmbException e){
            sentHandlerMsg(1,e.getMessage());
            e.printStackTrace();
            return false;
        }catch (Exception e){
            sentHandlerMsg(1,e.getMessage());
            e.printStackTrace();
            return false;
        }finally{
            try {
                if(null!=out){
                    out.close();
                }
                if(null!=in){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aVoid) {
        super.onPostExecute(aVoid);
        if (aVoid){
            sentHandlerMsg(2,"");
        }
    }

    public void setShareFileLoadLesner(ShareFileLoadLesner fileLoadLesner){
        this.fileLoadLesner = fileLoadLesner;
    }

    public interface ShareFileLoadLesner{
        void onLoadStart();
        void onLoadSucces();
        void onLoading(String rate);
        void onLoadFaild(String msg);
    }

    public void setPC(int pc){
        this.pc = pc;
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
                    if(null!=fileLoadLesner){
                        fileLoadLesner.onLoadFaild(msg.getData().getString("msg"));
                    }
                    break;
                case 2: //成功
                    if(null!=fileLoadLesner){
                        fileLoadLesner.onLoadSucces();
                    }
                    break;
                case 3: //
                    if(null!=fileLoadLesner){
                        fileLoadLesner.onLoadStart();
                    }
                    break;
                case 4: //
                    if(null!=fileLoadLesner){
                        fileLoadLesner.onLoading(msg.getData().getString("msg"));
                    }
                    break;
            }
        }
    };

}
