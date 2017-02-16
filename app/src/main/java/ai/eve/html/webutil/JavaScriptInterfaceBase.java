package ai.eve.html.webutil;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import ai.eve.html.custom.OnJsCallBack;
import ai.eve.util.ELog;

/**
 * Created by wyong on 2016/6/16.
 */
public class JavaScriptInterfaceBase implements EJavaScriptInterface, OnJsCallBack {
    protected static final String TAG = JavaScriptInterface.class.getSimpleName();
    protected WebView webView;
    protected Context mContext;

    public JavaScriptInterfaceBase(Context context, WebView webView) {
        this.webView = webView;
        this.mContext = context;
    }

    private String data;

    @Override
    public void setData(String data) {
        this.data = data;
    }

    @Override
    @JavascriptInterface
    public String getData() {
        return data;
    }

    @Override
    public void UI(final String jsObject) {
        JavaScriptInterface_UI jsInterface = new JavaScriptInterface_UI(mContext, webView);
        webView.addJavascriptInterface(jsInterface, jsObject);
    }

    @Override
    public void HTTP(final String jsObject) {
        JavaScriptInterface_HTTP jsInterface = new JavaScriptInterface_HTTP(mContext, webView);
        webView.addJavascriptInterface(jsInterface, jsObject);
    }

    @Override
    public void onLoad(final String callBackName, final String data) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + callBackName + "('" + data + "')");
            }
        });
    }
}
