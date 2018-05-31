package com.smb.sbmlibrary.listener;


import com.smb.sbmlibrary.http.HTTPRequest;

public interface HTTPRequestListener
{
	/** http请求接收 */
	public void httpRequestRecieved(HTTPRequest httpReq);

}
