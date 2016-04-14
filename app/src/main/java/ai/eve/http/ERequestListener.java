package ai.eve.http;

import android.content.Context;

import ai.eve.EApplication;
import ai.eve.activity.EActivity;
import ai.eve.activity.EToolBarActivity;
import ai.eve.util.ELog;
import ai.eve.volley.Listener;
import ai.eve.volley.NetroidError;

public abstract class ERequestListener<T> extends Listener<T>{
	
	private Context mContext;
	
	public ERequestListener(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public void onError(NetroidError error) {
		super.onError(error);
		if(EApplication.mContext instanceof  EActivity){
			((EActivity) EApplication.mContext).handleRequestError(error);
		}else if(EApplication.mContext instanceof EToolBarActivity){
			((EToolBarActivity) EApplication.mContext).handleRequestError(error);
		}
		ELog.E(error.getMessage());
	}

	@Override
	public void onSuccess(T response) {
		if(mContext==null)return;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		try{
			if(mContext==null)return;
			if(EApplication.mContext instanceof  EActivity){
				if(((EActivity) EApplication.mContext).getDialog()!=null){
					((EActivity) EApplication.mContext).getDialog().show();
				}
			}else if(EApplication.mContext instanceof EToolBarActivity){
				if(((EToolBarActivity) EApplication.mContext).getDialog()!=null){
					((EToolBarActivity) EApplication.mContext).getDialog().show();
				}
			}
		}catch(Exception e){
			ELog.E(e.getMessage());
		}
	}
	
	@Override
	public void onFinish() {
		super.onFinish();
		try{
			if(mContext==null)return;
			if(EApplication.mContext instanceof  EActivity){
				if(((EActivity) EApplication.mContext).getDialog()!=null){
					((EActivity) EApplication.mContext).getDialog().dismiss();
				}
			}else if(EApplication.mContext instanceof EToolBarActivity){
				if(((EToolBarActivity) EApplication.mContext).getDialog()!=null){
					((EToolBarActivity) EApplication.mContext).getDialog().dismiss();
				}
			}

		}catch(Exception e){
			ELog.E(e.getMessage());
		}
	}

}
