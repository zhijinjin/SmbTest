package com.smb.sbmlibrary.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import android.os.Environment;
import android.util.Log;

/**
 *
 * 根据后缀名返回文件类型
 *
 * @author zyq
 *
 */
public class FileUtil
{
	private static String type = "*/*";
	public static String ip = "127.0.0.1";
	public static String deviceDMRUDN = "0";
	public static String deviceDMSUDN = "0";
	public static int port = 0;


	public static String getFileType(String uri)
	{
		if (uri == null)
		{
			return type;
		}

		if (uri.endsWith(".mp3"))
		{
			return "audio/mpeg";
		}

		if (uri.endsWith(".mp4"))
		{
			return "video/mp4";
		}

		return type;
	}

	public static String getDeviceDMRUDN()
	{
		return deviceDMRUDN;
	}

	public static String getDeviceDMSUDN()
	{
		return deviceDMSUDN;
	}

	/**
	 *
	 * @return 创建成功返回true。否则false。
	 */
	public static boolean mkdir(String name)
	{
		boolean bool = false;

		boolean state = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (state)
		{
			File f = Environment.getExternalStorageDirectory();
			String path = f.getPath();
			String dir = path+"/"+name+"/";
			File file = new File(dir);
			if (!file.exists())
			{
				bool = file.mkdir();
			}
			else
			{
				Log.i("", "-----------"+dir+"已存在----------------");
			}

		}
		else
		{
			Log.e("", "-----------------外部存储器不可用----------------");
		}


		return bool;
	}


	public final static byte[] load(String fileName)
	{
		try {
			FileInputStream fin=new FileInputStream(fileName);
			return load(fin);
		}
		catch (Exception e) {
			Debug.warning(e);
			return new byte[0];
		}
	}

	public final static byte[] load(File file)
	{
		try {
			FileInputStream fin=new FileInputStream(file);
			return load(fin);
		}
		catch (Exception e) {
			Debug.warning(e);
			return new byte[0];
		}
	}

	public final static byte[] load(FileInputStream fin)
	{
		byte readBuf[] = new byte[512*1024];

		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			int readCnt = fin.read(readBuf);
			while (0 < readCnt) {
				bout.write(readBuf, 0, readCnt);
				readCnt = fin.read(readBuf);
			}

			fin.close();

			return bout.toByteArray();
		}
		catch (Exception e) {
			Debug.warning(e);
			return new byte[0];
		}
	}

	/** 根据文件的名字判断是不是xml文件，是返回true，否则返回false */
	public final static boolean isXMLFileName(String name)
	{
		if (StringUtil.hasData(name) == false){
			return false;
		}
		String lowerName = name.toLowerCase();
		return lowerName.endsWith("xml");
	}


}
