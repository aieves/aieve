package ai.eve.html.webutil;

import android.content.Context;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * JS接口，可通过js调用java代码
 * <p/>
 * Description: JsInterface author WYONG date 2014-3-10 上午10:11:27 version V1.0
 */
public class JsInterface {
    private static final String TAG = "JsInterface";
    private EWebView mWebView;
    private Context mContext;
    private JavaScriptInterface jsInterface;

    //	public JsInterface(EWebView webView,JavaScriptInterfaceBase javaScriptInterface, String jsObject, Context context) {
//		this.mWebView = webView;
//		this.mContext = context;
//		mWebView.addJavascriptInterface(javaScriptInterface ,jsObject);
//		this.jsInterface = javaScriptInterface;
//		init();
//	}
    public JsInterface(EWebView webView, Context context) {
        this.mWebView = webView;
        this.mContext = context;
        jsInterface = new JavaScriptInterface(mContext,mWebView);
        mWebView.addJavascriptInterface(jsInterface,"app");
        init();
    }

    public void setData(String data) {
        jsInterface.setData(data);
    }

    public void initUI(){
        //jsInterface.UI("app");
    }

    private void init() {
        if (mWebView == null) {
            Log.d(TAG, "webView 初始化失败");
            return;
        }
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void setWebChromeClient(WebChromeClient chromeClient) {
        mWebView.setWebChromeClient(chromeClient);
    }

}
