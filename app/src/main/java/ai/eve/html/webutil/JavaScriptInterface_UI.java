package ai.eve.html.webutil;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import ai.eve.html.custom.CustomDialog;

/**
 * Created by wyong on 2016/6/16.
 */
public class JavaScriptInterface_UI extends JavaScriptInterfaceBase{

    public JavaScriptInterface_UI(Context context, WebView webView) {
        super(context,webView);
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
        customDialog.setOnJsCallBack(JavaScriptInterface_UI.this);
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
}
