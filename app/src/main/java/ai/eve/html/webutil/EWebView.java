package ai.eve.html.webutil;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class EWebView extends WebView{

	private JsInterface jsInterface;
	
	public EWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	public EWebView(Context context) {
		super(context);
		init(context);
	}
	public EWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	private void init(Context mContext) {
		jsInterface = new JsInterface(this, mContext);
	}

	public void initUI(){
		jsInterface.initUI();
	}

	public void setData(String data){
		jsInterface.setData(data);
	}
}
