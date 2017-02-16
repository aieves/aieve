package ai.eve.html.webutil;

import android.webkit.JavascriptInterface;

/**
 * Created by wyong on 2016/6/16.
 */
public interface EJavaScriptInterface {
    void setData(String data);
    String getData();
    void UI(String jsObject);
    void HTTP(String jsObject);
}
