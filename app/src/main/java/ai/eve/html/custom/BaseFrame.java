package ai.eve.html.custom;

/**
 * 
 *
 * @Description: TODO
 * @author WYONG  
 * @date 2014-3-21 下午4:14:19 
 * @version V1.0
 *
 */
public class BaseFrame {

	private OnJsCallBack mOnJsCallBack;

	public void setOnJsCallBack(OnJsCallBack onJsCallBack) {
		this.mOnJsCallBack = onJsCallBack;
	}

	public OnJsCallBack getmOnJsCallBack() {
		return mOnJsCallBack;
	}
}
