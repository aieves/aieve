package ai.eve.http;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import ai.eve.util.ESecurity;
import ai.eve.volley.AuthFailureError;
import ai.eve.volley.Listener;
import ai.eve.volley.Request.Method;

/**
 * 
 * 请求入口，建议使用此类，不直接使用EStringRequest/EMultiPartRequest等
 * 
 * @author Eve-Wyong
 * @version 2015-3-3 下午3:38:25
 * @Copyright 2014 EVE. All rights reserved.
 */
public class ERequest<T> {

	private ERequestListener<T> mListener;

	/**
	 * listener，作为回调，必传，且不能为空
	 * 
	 * @param listener
	 */
	public ERequest(ERequestListener<T> listener) {
		if (listener == null) {
			listener = new ERequestListener<T>(null) {

				@Override
				public void onSuccess(T response) {
				}
			};
		}
		this.mListener = listener;
	}

	/**
	 * 发送GET请求，默认不使用缓存
	 * 
	 * @param url
	 * @param cache
	 *            是否使用缓存
	 */
	public void doGet(String url, boolean cache) {
		doGet(url,null);
	}

	/**
	 * 发送GET请求，默认不使用缓存
	 * 
	 * @param url
	 */
	public void doGet(String url, HashMap<String, String> params) {
		doGet(url,params,false);
	}

	/**
	 * 发送GET请求，默认不使用缓存
	 *
	 * @param url
	 * @param params
	 * @param cache 是否使用缓存
	 */
	public void doGet(String url, HashMap<String, String> params,boolean cache) {
		EStringRequest sr = null;
		if(params!=null && params.size()>0){
			StringBuilder encodedParams = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				try{
					encodedParams.append(entry.getKey());
					encodedParams.append('=');
					encodedParams.append((java.net.URLEncoder.encode(entry.getValue(), "utf-8")));
					encodedParams.append('&');
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			sr = new EStringRequest(Method.GET, url+"?"+encodedParams.toString(), (ERequestListener<String>)mListener);
		}else{
			sr = new EStringRequest(Method.GET, url, (ERequestListener<String>)mListener);
		}
		sr.setForceUpdate(!cache);
		sr.start();
	}

	/**
	 * 发送POST请求，默认不使用缓存
	 * 
	 * @param url
	 * @param params
	 */
	public void doPost(String url, final HashMap<String, String> params) {
		doPost(url, params, false, false);
	}
	/**
	 * 发送POST请求，默认不使用缓存
	 *
	 * @param url
	 * @param params
	 * @param encrypt  是否使用加密
	 * @param cache  是否使用缓存
	 */
	public void doPost(String url, final HashMap<String, String> params, final boolean encrypt,boolean cache) {
		EStringRequest sr = new EStringRequest(Method.POST, url,(ERequestListener<String>) mListener) {
			@Override
			public Map<String, String> getParams() throws AuthFailureError {
				if(encrypt){
					HashMap<String,String> tmpParams = new HashMap<>();
					for (Map.Entry<String, String> entry : params.entrySet()) {
						try {
							tmpParams.put(entry.getKey(), URLEncoder.encode(ESecurity.Encrypt(entry.getValue()),"utf-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					return tmpParams;
				}else{
					HashMap<String,String> tmpParams = new HashMap<>();
					for (Map.Entry<String, String> entry : params.entrySet()) {
						try {
							tmpParams.put(entry.getKey(), URLEncoder.encode(entry.getValue(),"utf-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					return tmpParams;
				}
			}
		};
		sr.setForceUpdate(!cache);
		sr.start();
	}

	/**
	 * 发送POST请求，包括字符串和文件，默认不使用缓存
	 *
	 * @param url
	 * @param params
	 * @param files
	 */
	public void doPost(String url, final HashMap<String, String> params,final HashMap<String, File> files) {
		doPost(url, params, files, false, false);
	}

	/**
	 * 发送POST请求，包括字符串和文件，默认不使用缓存
	 *
	 * @param url
	 * @param params
	 * @param files
	 * @param encrypt  是否使用加密
	 * @param cache   是否使用缓存
	 */
	public void doPost(String url, final HashMap<String, String> params,final HashMap<String, File> files ,final boolean encrypt,boolean cache) {
		EMultiPartRequest sr = new EMultiPartRequest(Method.POST, url,
				(ERequestListener<String>)mListener) {
			@Override
			public Map<String, String> getParams() throws AuthFailureError {
				if(encrypt){
					HashMap<String,String> tmpParams = new HashMap<>();
					for (Map.Entry<String, String> entry : params.entrySet()) {
						try {
							tmpParams.put(entry.getKey(),URLEncoder.encode(ESecurity.Encrypt(entry.getValue()),"utf-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					return tmpParams;
				}else{
					return params;
				}
			}

			@Override
			public Map<String, File> getFiles() {
				return files;
			}
		};
		sr.setForceUpdate(!cache);
		sr.start();
	}



	/**
	 * 发送JSON请求，默认不使用缓存
	 *
	 * @param url
	 * @param jsonObject
	 * @param cache
	 */
	public void doJSONGet(String url, JSONObject jsonObject, boolean cache) {
		EJSONObjectRequest sr = new EJSONObjectRequest(Method.GET, url,jsonObject,
				(Listener<JSONObject>) mListener) {
		};
		sr.setForceUpdate(!cache);
		sr.start();
	}
	/**
	 * 发送JSON请求，默认不使用缓存
	 *
	 * @param url
	 * @param jsonObject
	 * @param cache
	 */
	public void doJSONPost(String url, JSONObject jsonObject, boolean cache) {
		EJSONObjectRequest sr = new EJSONObjectRequest(Method.POST, url,jsonObject,
				(Listener<JSONObject>) mListener) {
		};
		sr.setForceUpdate(!cache);
		sr.start();
	}

}
