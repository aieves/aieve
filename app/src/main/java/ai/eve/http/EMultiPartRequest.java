package ai.eve.http;

import ai.eve.EApplication;
import ai.eve.volley.request.MultiPartStringRequest;

/**
 * multipart-data请求，可发送带文件和字符串的请求
 * 
 * @author Eve-Wyong
 * @version 2015-2-25 下午4:26:55
 * @Copyright 2014 EVE. All rights reserved.
 */
public class EMultiPartRequest extends MultiPartStringRequest {

	/**
	 * 
	 * @param method
	 * @param url
	 * @param listener
	 */
	public EMultiPartRequest(int method, String url, ERequestListener<String> listener) {
		super(method, url, listener);
	}
	/**
	 * 将当前请求添加到请求队列等待执行.
	 */
	public void start(){
    	EApplication.mQueue.add(this);
    }
}
