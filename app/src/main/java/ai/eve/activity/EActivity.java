package ai.eve.activity;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import ai.eve.EApplication;
import ai.eve.volley.NetroidError;
import ai.eve.volley.NetworkError;
import ai.eve.volley.NoConnectionError;
import ai.eve.volley.ServerError;
import ai.eve.volley.TimeoutError;

/**
 * <p>
 * 使用此框架的工程Activity需继承此类
 * </p>
 * 
 * <p>
 * 作用：
 * </p>
 * <ol>
 * <li>HTTP异常的统一处理</li>
 * <li>框架Context实例提供，若不继承此类，使用某些功能会出现NullException</li>
 * </ol>
 * 
 * 
 * @author Eve-Wyong
 * @version 2015-2-25 下午3:32:54
 * @Copyright 2014 EVE. All rights reserved.
 */
public class EActivity extends FragmentActivity {
	private Dialog dialog;
	
    public Dialog getDialog() {
		return dialog;
	}
	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}
	@Override
	protected void onResume() {
		super.onResume();
		EApplication.mContext = this;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mNetReceiver!=null){
			unregisterReceiver(mNetReceiver);
		}
	}

	private HandleRequestError handler;
	
	public void setHandleRequestError(HandleRequestError handler){
		this.handler = handler;
	}
	
	public void handleRequestError(NetroidError error) {
		if(error instanceof TimeoutError){
			if(handler!=null){
				handler.timeOutError();
			}else{
				Toast.makeText(this, "TimeoutError", Toast.LENGTH_SHORT).show();
			}
		}else if(error instanceof NoConnectionError){
			if(handler!=null){
				handler.notConnetedError();
			}else{
				Toast.makeText(this, "NoConnectionError", Toast.LENGTH_SHORT).show();
			}
		}else if(error instanceof ServerError){
			if(handler!=null){
				handler.serverError();
			}else{
				Toast.makeText(this, "ServerError", Toast.LENGTH_SHORT).show();
			}
		}else if(error instanceof NetworkError){
			if(handler!=null){
				handler.networkError();
			}else{
				Toast.makeText(this, "NetworkError", Toast.LENGTH_SHORT).show();
			}
		}else{
			if(handler!=null){
				handler.otherError();
			}else{
				Toast.makeText(this, "otherError", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public interface HandleRequestError{
		void notConnetedError();
		void timeOutError();
		void serverError();
		void networkError();
		void otherError();
	}
	public void setHandleNetStateListener(final HandleNetStateListener listener){
		mNetReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				String action = intent.getAction();
				if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
					ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
					if(netInfo != null && netInfo.isAvailable()) {
						String name = netInfo.getTypeName();
						if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){
							//WiFi网络
							if(listener!=null){
								listener.wifi();
							}
						}else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){
							//有线网络
							if(listener!=null){
								listener.ethernet();
							}
						}else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){
							//3g网络
							if(listener!=null){
								listener.mobile();
							}
						}
					} else {
						//网络断开
						if(listener!=null){
							listener.unConnected();
						}
					}
				}

			}
		};
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetReceiver, mFilter);
	}

	public interface HandleNetStateListener{
		void wifi();
		void ethernet();
		void mobile();
		void unConnected();
	}

	private BroadcastReceiver mNetReceiver;
}
