package ai.eve.html.custom;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import ai.eve.R;
import ai.eve.html.webutil.model.DialogParam;


/**自定义警示对话框
 * 
 * 
 * 
 * @author zhaofy
 *
 */
public class CustomDialog extends BaseDialog implements View.OnClickListener{
	
	private static final String TAG = "CustomDialog";
	private Context mContext;
	private LayoutInflater mInflater;
	private Button mLeftBtn,mRightBtn;
	private TextView mTitleTV ;
	private FrameLayout content;
	private ImageView mImageView;
	private DialogParam dialogParam;

	public CustomDialog(Context context, String mParam) {
		super(context, R.style.eve_Theme_Dialog);
		this.mContext = context;
        dialogParam = (DialogParam) JSON.parseObject(mParam, DialogParam.class);
		init();
	}
	
	private void init() {
		mInflater = LayoutInflater.from(mContext);
		View layout = mInflater.inflate(R.layout.eve_custom_alert_dialog_ll, null);
		mLeftBtn = (Button) layout.findViewById(R.id.custom_alert_dialog_left_btn);
		mLeftBtn.setOnClickListener(this);
		mRightBtn = (Button) layout.findViewById(R.id.custom_alert_dialog_right_btn);
		mRightBtn.setOnClickListener(this);
		mTitleTV = (TextView) layout.findViewById(R.id.custom_alert_dialog_title_textview);
		content = (FrameLayout) layout.findViewById(R.id.content);
		TextView mMessageTV = new TextView(mContext);
		content.addView(mMessageTV);
		mImageView = (ImageView) layout.findViewById(R.id.custom_alert_dialog_title_img);
		setContentView(layout);
		if (dialogParam.title != null && dialogParam.title.length() > 0) {
			mTitleTV.setText(dialogParam.title);
		} else {
			mTitleTV.setText(mContext.getResources().getString(R.string.eve_title));
		}
		if (dialogParam.content != null && dialogParam.content.length() > 0) {
			mMessageTV.setText(dialogParam.content);
		} else {
			mMessageTV.setText(mContext.getResources()
					.getString(R.string.eve_title));
		}
		if (dialogParam.ok == null) {
			mLeftBtn.setVisibility(View.GONE);
		} else {
			if (dialogParam.ok.text != null && dialogParam.ok.text.length() > 0) {
				mLeftBtn.setText(dialogParam.ok.text);
			} else {
				mLeftBtn.setText(mContext.getString(R.string.eve_confrim));
			}
		}
		if (dialogParam.cancel == null) {
			mRightBtn.setVisibility(View.GONE);
		} else {
			if (dialogParam.cancel.text != null
					&& dialogParam.cancel.text.length() > 0) {
				mRightBtn.setText(dialogParam.cancel.text);
			} else {
				mRightBtn.setText(mContext.getString(R.string.eve_cancel));
			}
		}
        
	}
	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.custom_alert_dialog_left_btn) {
			if (dialogParam.ok != null && dialogParam.ok.callback != null
					&& getmOnJsCallBack() != null) {
				getmOnJsCallBack().onLoad(dialogParam.ok.callback, "1");
			}
			if (mDialogListener != null) {
				mDialogListener.OnClickOk();
			}
			this.dismiss();

		} else if (i == R.id.custom_alert_dialog_right_btn) {
			if (dialogParam.cancel != null
					&& dialogParam.cancel.callback != null) {
				getmOnJsCallBack().onLoad(dialogParam.cancel.callback, "0");
			}
			if (mDialogListener != null) {
				mDialogListener.OnCancel();
			}
			this.dismiss();

		} else {
		}
	}
	
	private OnCustomDialogListener mDialogListener;
	
	public interface OnCustomDialogListener{
		void OnClickOk();
		void OnCancel();
	}

	public void setmDialogListener(OnCustomDialogListener mDialogListener) {
		this.mDialogListener = mDialogListener;
	}

	@Override
	public void show() {
		super.show();
		WindowManager windowManager = getWindow().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int)(display.getWidth()*0.8); //设置宽度
//		lp.height = (int)(display.getHeight()*0.23); //设置高度
		getWindow().setAttributes(lp);
	}
	
	
}
