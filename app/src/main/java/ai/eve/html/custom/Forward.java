package ai.eve.html.custom;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import ai.eve.html.activity.NoTitleBarWebViewActivity;
import ai.eve.html.activity.TitleBarWebViewActivity;
import ai.eve.html.webutil.model.ForwardParam;

/**
 * Activity跳转
 * 
 * @Description: TODO
 * @author WYONG
 * @date 2014-3-21 下午4:15:16
 * @version V1.0
 * 
 */
public class Forward {
	private Context mContext;
	private ForwardParam forwardParam;

	public Forward(Context mContext, String param) {
		this.mContext = mContext;
		forwardParam = (ForwardParam) JSON.parseObject(param,
				ForwardParam.class);
	}

	public void startWebView() {
		try {
			if (forwardParam != null) {
				Intent intent = new Intent();
				if(forwardParam.title==null || TextUtils.isEmpty(forwardParam.title)){
					intent.setClass(mContext, NoTitleBarWebViewActivity.class);
				}else if(forwardParam.title.endsWith("#")){
					intent.setClass(mContext, TitleBarWebViewActivity.class);
					intent.putExtra("title", forwardParam.title);
					intent.putExtra("back", 0);
				}else{
					intent.setClass(mContext, TitleBarWebViewActivity.class);
					intent.putExtra("title", forwardParam.title);
				}
				intent.putExtra("url", forwardParam.url);
				mContext.startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
