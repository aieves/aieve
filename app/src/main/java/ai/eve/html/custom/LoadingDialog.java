package ai.eve.html.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ProgressBar;

import ai.eve.R;

public class LoadingDialog extends Dialog {
	private static LoadingDialog customProgressDialog = null;
	// 不能关闭
	public final static int NO_CANCEL = 0X1;
	// 可以关闭
	public final static int SHOW_CANCEL = 0X2;
	private Animation transAnimation;

	public ProgressBar bar;

	public LoadingDialog(Context context) {
		super(context, R.style.dialog);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public static LoadingDialog createDialog(Context context) {
		customProgressDialog = new LoadingDialog(context, R.style.loadingDialog);
		customProgressDialog.setContentView(R.layout.progress_dialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.TOP;
		customProgressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
		customProgressDialog.setCancelable(true);
		return customProgressDialog;
	}

	@Override
	public void show() {
		super.show();
		WindowManager windowManager = getWindow().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int) (display.getWidth() * 1); // 设置宽度
		lp.y = 48;
		getWindow().setAttributes(lp);
	}


	public void delayDismiss() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(200);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dismiss();
			}
		}).start();
	}

	/**
	 * 
	 * [Summary]d setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public LoadingDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * 
	 * [Summary] setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public LoadingDialog setMessage(String strMessage) {
		/*
		 * TextView tvMsg =
		 * (TextView)customProgressDialog.findViewById(R.id.msg); if (tvMsg !=
		 * null){ tvMsg.setText(strMessage); }
		 */
		return customProgressDialog;
	}

}
