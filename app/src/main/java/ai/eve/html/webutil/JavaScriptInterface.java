package ai.eve.html.webutil;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import ai.eve.html.custom.CustomDialog;
import ai.eve.html.custom.DateDialog;
import ai.eve.html.custom.Forward;
import ai.eve.html.custom.Http;
import ai.eve.html.custom.MultiChoiceDialog;
import ai.eve.html.custom.OnJsCallBack;
import ai.eve.html.custom.Redirect;
import ai.eve.html.custom.SingleChoiceDialog;
import ai.eve.html.custom.TimeDialog;


/**
 * Created with IntelliJ IDEA.
 * User: shuaizhimin
 * Date: 2015/6/19
 * Time: 14:59
 */
public  class JavaScriptInterface implements OnJsCallBack {

    protected static final String TAG = JavaScriptInterface.class.getSimpleName();
    protected WebView webView;
    protected Context mContext;

    public JavaScriptInterface(Context context, WebView webView) {
        this.webView = webView;
        this.mContext = context;
    }

    private String data;

    public void setData(String data) {
        this.data = data;
    }

    @JavascriptInterface
    public String getData() {
        return data;
    }

    @JavascriptInterface
    public void back() {
        ((Activity) mContext).finish();
    }

    /**
     * 单选框
     *
     * @param param
     */
    @JavascriptInterface
    public void singleChoose(final String param) {
        SingleChoiceDialog singleChoiceDialog = new SingleChoiceDialog(
                mContext, param);
        singleChoiceDialog.setOnJsCallBack(JavaScriptInterface.this);
        singleChoiceDialog.show();
    }

    /**
     * 多选框
     *
     * @param param
     */
    @JavascriptInterface
    public void multiChoose(final String param) {
        Log.d(TAG, "多选");
        MultiChoiceDialog choiceDialog = new MultiChoiceDialog(mContext,
                param);
        choiceDialog.setOnJsCallBack(JavaScriptInterface.this);
        choiceDialog.show();
    }

    /**
     * 日期选择对话框 "{ 'defaultDate':'2013-11-11', 'callback':'cancelfun' }";
     *
     * @param param
     */
    @JavascriptInterface
    public void dateDialog(String param) {
        DateDialog dateDialog = new DateDialog(mContext, param);
        dateDialog.setOnJsCallBack(JavaScriptInterface.this);
        dateDialog.show();
    }

    /**
     * 时间选择对话框
     *
     * @param param
     */
    @JavascriptInterface
    public void timeDialog(String param) {
        TimeDialog timeDialog = new TimeDialog(mContext, param);
        timeDialog.setOnJsCallBack(JavaScriptInterface.this);
        timeDialog.show();
    }

    /**
     * 显示默认dialog
     *
     * @param param
     *            var param = "{ \'title':\'提示', 'content':'是否打印发片？',
     *            'ok':'{ 'text':'是','callback':'okfun' }', 'cancel':'{
     *            'text':'否','callback':'cancelfun' }' }";
     */
    @JavascriptInterface
    public void alert(String param) {
        CustomDialog customDialog = new CustomDialog(mContext, param);
        customDialog.setOnJsCallBack(JavaScriptInterface.this);
        customDialog.show();
    }

    /**
     * 通知
     *
     * @param message
     */
    @JavascriptInterface
    public void echo(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * js实现activity跳转
     *
     * @param param
     */
    @JavascriptInterface
    public void redirect(String param) {
        Redirect redirect = new Redirect(mContext, param);
        redirect.startActivity();
    }
    @JavascriptInterface
    public void forward(String param){
    	Forward forward = new Forward(mContext, param);
    	forward.startWebView();
    }
    
    /**
     * get方式发送请求
     */
    @JavascriptInterface
    public void get(final String param) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            public void run() {
                Http http = new Http(mContext, param);
                http.setOnJsCallBack(JavaScriptInterface.this);
                http.get();
            }
        });
    }

    /**
     * post方式发送请求
     *
     * @param
     */
    @JavascriptInterface
    public void post(final String param) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            public void run() {
                Http http = new Http(mContext, param);
                http.setOnJsCallBack(JavaScriptInterface.this);
                http.post();
            }
        });
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
