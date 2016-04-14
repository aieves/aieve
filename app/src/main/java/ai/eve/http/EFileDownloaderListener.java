package ai.eve.http;

import android.app.ProgressDialog;

import ai.eve.EApplication;
import ai.eve.activity.EActivity;
import ai.eve.activity.EToolBarActivity;
import ai.eve.util.ELog;
import ai.eve.volley.Listener;
import ai.eve.volley.NetroidError;

public abstract class EFileDownloaderListener<T> extends Listener<T>{
	
	@Override
	public void onError(NetroidError error) {
		super.onError(error);
		if(EApplication.mContext instanceof EActivity){
			((EActivity) EApplication.mContext).handleRequestError(error);
		}else if(EApplication.mContext instanceof EToolBarActivity){
			((EToolBarActivity) EApplication.mContext).handleRequestError(error);
		}
		ELog.E(error.getMessage());
	}

	private ProgressDialog eDialog;

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		if(eDialog==null){
			eDialog = new ProgressDialog(EApplication.mContext);
			eDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			eDialog.setCanceledOnTouchOutside(false);
		}
		eDialog.show();
	}
	@Override
	public void onSuccess(T response) {
		if(eDialog.isShowing()){
			eDialog.dismiss();
		}
		eDialog=null;
	}
	@Override
	public void onFinish() {
		super.onFinish();
		if(eDialog!=null){
			eDialog.dismiss();
		}
	}

	@Override
	public void onProgressChange(long fileSize, long downloadedSize) {
		super.onProgressChange(fileSize, downloadedSize);
		eDialog.setMax((int)fileSize);
		eDialog.setProgress((int)downloadedSize);
	}
}
