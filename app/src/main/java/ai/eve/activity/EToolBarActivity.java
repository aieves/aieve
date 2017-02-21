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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import ai.eve.EApplication;
import ai.eve.R;
import ai.eve.volley.NetroidError;
import ai.eve.volley.NetworkError;
import ai.eve.volley.NoConnectionError;
import ai.eve.volley.ServerError;
import ai.eve.volley.TimeoutError;
import ai.eve.widget.EToolBar;

/**
 * Created by Administrator on 2016/1/26.
 */
public class EToolBarActivity extends ActionBarActivity {
    private FrameLayout content;
    public EToolBar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        layout.setOrientation(LinearLayout.VERTICAL);
        mToolBar = new EToolBar(this);
        mToolBar.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        content = new FrameLayout(this);
        content.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        layout.addView(mToolBar);
        layout.addView(content);

        super.setContentView(layout);

        mToolBar.initTooBar(this);
    }


    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        content.removeAllViews();
        content.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        content.removeAllViews();
        content.addView(view, params);
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private Dialog dialog;

    public Dialog getDialog() {
        return dialog;
    }
    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EApplication.mContext = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            unregisterReceiver(mNetReceiver);
        }catch (Exception e){
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
    private HandleNetStateListener listener;
    public void setHandleNetStateListener(HandleNetStateListener listener){
        this.listener = listener;
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

    private BroadcastReceiver mNetReceiver = new BroadcastReceiver() {

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
}

