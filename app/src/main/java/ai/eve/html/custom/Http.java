package ai.eve.html.custom;

import ai.eve.html.webutil.model.HttpParam;
import ai.eve.http.ERequest;
import ai.eve.http.ERequestListener;
import android.content.Context;

import com.alibaba.fastjson.JSON;

/**
 * @author WYONG
 * @version V1.0
 * @Description: TODO
 * @date 2014-3-21 下午4:14:43
 */
public class Http extends BaseFrame {
	private Context mContext;
	private HttpParam httpParam;

	public Http(Context mContext, String param) {
		this.mContext = mContext;
		httpParam = (HttpParam) JSON.parseObject(param, HttpParam.class);
	}

	public void get() {
		ERequestListener<String> listener = new ERequestListener<String>(mContext) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				getmOnJsCallBack().onLoad(httpParam.success,response.toString());
			}
		};
		ERequest req = new ERequest(listener);
		req.doGet(httpParam.url, httpParam.data);
		
	}
	public void post() {
		ERequestListener<String> listener = new ERequestListener<String>(mContext) {
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				getmOnJsCallBack().onLoad(httpParam.success,response.toString());
			}
		};
		ERequest req = new ERequest(listener);
		req.doPost(httpParam.url, httpParam.data);
	}

}
