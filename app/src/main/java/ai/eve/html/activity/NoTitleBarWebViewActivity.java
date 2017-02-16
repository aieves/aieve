package ai.eve.html.activity;

import android.os.Bundle;

import ai.eve.activity.EActivity;
import ai.eve.html.webutil.EWebView;


public class NoTitleBarWebViewActivity  extends EActivity {
    private EWebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new EWebView(this);
        super.setContentView(mWebView);

        String url = getIntent().getStringExtra("url");
        
        if(url == null){
        	return;
        }
        mWebView.loadUrl(url);
    }
}
