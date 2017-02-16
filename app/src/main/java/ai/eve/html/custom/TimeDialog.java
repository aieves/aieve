package ai.eve.html.custom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSON;

import ai.eve.html.webutil.model.TimeDialogParam;

/**
 *
 * @Description: TODO
 * @author WYONG  
 * @date 2014-3-21 下午4:15:49 
 * @version V1.0
 *
 */
public class TimeDialog extends BaseDialog implements OnTimeSetListener {
	private Context mContext;
	private String param;
	private TimePickerDialog dialog;
	private TimeDialogParam dialogParam;
	private SimpleDateFormat sdf;
	
	private Calendar d = Calendar.getInstance(Locale.CHINA);
	
	private OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			getmOnJsCallBack().onLoad(dialogParam.ok.callback,sdf.format(d.getTime()));
		}
	};
	private OnClickListener onCancel = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			getmOnJsCallBack().onLoad(dialogParam.cancel.callback,sdf.format(d.getTime()));
		}
	};

	public TimeDialog(Context context, String param) {
		super(context);
		this.mContext = context;
		this.param = param;
		init();
	}

	private void init() {
		dialogParam = (TimeDialogParam) JSON.parseObject(param,
				TimeDialogParam.class);
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		sdf = new SimpleDateFormat("HH:mm");
		String dstr = dialogParam.defaultTime;
		try {
			Date date = sdf.parse(dstr);
			cal.setTime(date);
			int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			dialog = new TimePickerDialog(mContext, this, hourOfDay, minute,
					true);
			if (dialogParam.title != null && !dialogParam.title.equals("")) {
				dialog.setTitle(dialogParam.title);
			}
			if(dialogParam.ok!=null){
				if(dialogParam.ok.text!=null&&dialogParam.ok.text.length()>0){
		            dialog.setButton(dialogParam.ok.text, onClick);
		        }else{
		            dialog.setButton("确定", onClick);
		        }
	        }
			if(dialogParam.cancel!=null){
				if(dialogParam.ok.text!=null&&dialogParam.cancel.text.length()>0){
		            dialog.setButton2(dialogParam.cancel.text, onCancel);
		        }else{
		            dialog.setButton2("取消", onCancel);
		        }
	        }
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		d.set(Calendar.HOUR_OF_DAY, hourOfDay);
		d.set(Calendar.MINUTE, minute);
	}

	public void show() {
		dialog.show();
	}
}
