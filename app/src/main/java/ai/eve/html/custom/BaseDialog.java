package ai.eve.html.custom;




import android.app.Dialog;
import android.content.Context;

import ai.eve.R;

/**
 * 
 *
 * @Description: TODO
 * @author WYONG  
 * @date 2014-3-21 下午4:14:03 
 * @version V1.0
 *
 */
public class BaseDialog extends Dialog {

	private OnJsCallBack mOnJsCallBack;

	public void setOnJsCallBack(OnJsCallBack onJsCallBack) {
		this.mOnJsCallBack = onJsCallBack;
	}

	public BaseDialog(Context context) {
		super(context);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
	}

	public OnJsCallBack getmOnJsCallBack() {
		return mOnJsCallBack;
	}

}
