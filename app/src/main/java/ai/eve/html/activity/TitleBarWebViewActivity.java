package ai.eve.html.activity;

import android.os.Bundle;

import ai.eve.activity.EToolBarActivity;
import ai.eve.html.webutil.EWebView;


/**
 * Created with IntelliJ IDEA.
 * User:shuaizhimin
 * Date:15-5-24
 * Time:下午1:31
 */
public class TitleBarWebViewActivity extends EToolBarActivity{

    private EWebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new EWebView(this);
        super.setContentView(mWebView);

        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        if(title==null || url == null){
        	return;
        }
        mToolBar.setTitle("");
        mToolBar.setCenterTitle(title);

        mWebView.loadUrl(url);
    }
}
