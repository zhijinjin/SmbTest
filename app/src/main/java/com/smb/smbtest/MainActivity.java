package com.smb.smbtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.smb.sbmlibrary.LoadShareFile;
import com.smb.sbmlibrary.MediaItem;
import com.smb.sbmlibrary.SmbUtil;
import com.smb.sbmlibrary.service.PlayFileService;
import com.smb.sbmlibrary.util.SearchShareFile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchShareFile.OnSearchShareFile,
        MyRecycViewAdapter.OnItemClickListener,LoadShareFile.ShareFileLoadLesner {

    MyRecycViewAdapter adapter;
    RecyclerView listView;
    private static final int MY_PERMIEAD_CONTACTS = 100;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText ed= findViewById(R.id.edittext);
        listView = findViewById(R.id.listview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        listView.setLayoutManager(manager);
        adapter = new MyRecycViewAdapter(this);
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        checkPermiss();

        SmbUtil.getInstence().setShareName("951507056@qq.com")
                .setSharePwd("zhi123456")
                .setTypes(new String[]{".mp4",".mp3",".html",".rmvb"})
                .setSearchShareFileLisener(this)
                .setLoadShareFileLisener(this);

        Intent intent = new Intent(this, PlayFileService.class);
        startService(intent);

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmbUtil.getInstence().searchShareFile(ed.getText().toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, PlayFileService.class);
        stopService(intent);
    }

    @Override
    public void onSearchShareFileResult(ArrayList<MediaItem> result) {
        adapter.setmData(result);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onSearchShareFileFaild(final String e) {
        Toast.makeText(MainActivity.this,e,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(int position) {
        String url = adapter.getItem(position).getUrl_Http();
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), getMedraType(url));
            startActivity(intent);
    }

    @Override
    public void onLongClick(int position) {
        SmbUtil.getInstence().loadShareFile(adapter.getItem(position).getUrl_Smb(),"共享文件下载");
    }

    private String getMedraType(String url){
        String type = "";
        if(url.endsWith(".mp3")){
            type = "audio/x-mpeg";
        }else if(url.endsWith(".mp4")||url.endsWith(".rmvb")){
            type = "video/*";
        }else if(url.endsWith(".html")){
            type = "text/*";
        }
        return type;
    }


    @Override
    public void onLoadStart() {
        Log.i(TAG, "onLoadStart: ");
        Toast.makeText(MainActivity.this,"开始下载",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadSucces() {
        Log.i(TAG, "onLoadSucces: ");
        Toast.makeText(MainActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading(String rate) {
        Log.i(TAG,"下载："+rate+"%");
    }

    @Override
    public void onLoadFaild(String msg) {
        Log.i(TAG, "onLoadFaild: "+msg);
        Toast.makeText(MainActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
    }


    private void checkPermiss(){
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 显示给用户的解释


            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE ,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMIEAD_CONTACTS);
            }
        }
    }

}
