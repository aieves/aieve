package ai.eve.http;

import java.util.concurrent.TimeUnit;

import ai.eve.EApplication;
import ai.eve.volley.request.StringRequest;

/**
 * 字符串请求
 * 
 * @author Eve-Wyong
 * @version 2015-2-25 下午4:26:55
 * @Copyright 2014 EVE. All rights reserved.
 */
public class EStringRequest extends StringRequest{

	/**
	 * 
	 * @param method 请求类型，GET or POST or other. See {@link Method}
	 * @param url 请求地址
	 * @param listener 请求回调
	 */
	public EStringRequest(int method, String url, ERequestListener<String> listener) {
		super(method, url, listener);
	}
	
	public EStringRequest(String url, ERequestListener<String> listener) {
        this(Method.GET, url, listener);
    }
	
	/**
	 * 将当前请求添加到请求队列等待执行.
	 */
	public void start(){
		this.setCacheExpireTime(TimeUnit.MINUTES, EApplication.FILE_CACHEEXPIRETIME);
    	EApplication.mQueue.add(this);
    }
	
}
