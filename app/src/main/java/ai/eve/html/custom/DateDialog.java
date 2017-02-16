package ai.eve.html.custom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.DatePicker;

import com.alibaba.fastjson.JSON;

import ai.eve.html.webutil.model.DateDialogParam;

/**
 *
 * @Description: TODO
 * @author WYONG  
 * @date 2014-3-21 下午4:14:31 
 * @version V1.0
 *
 */
public class DateDialog extends BaseDialog implements OnDateSetListener{
	private Context mContext;
	private String param;
	private DatePickerDialog dialog;
	private DateDialogParam dialogParam;
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
	
	public DateDialog(Context context,String param){
		super(context);
		this.mContext = context;
		this.param = param;
		init();
	}
	private void init() {
		dialogParam = (DateDialogParam)JSON.parseObject(param, DateDialogParam.class);
		Calendar d = Calendar.getInstance(Locale.CHINA);
		sdf=new SimpleDateFormat("yyyy-MM-dd");
		String dstr=dialogParam.defaultDate;  
		try {
			Date date = sdf.parse(dstr);
			d.setTime(date);
			int year=d.get(Calendar.YEAR);
			int month=d.get(Calendar.MONTH);
			int day=d.get(Calendar.DAY_OF_MONTH);
			dialog=new DatePickerDialog(mContext,this,year,month,day);
			if(dialogParam.title!=null && !dialogParam.title.equals("")){
				dialog.setTitle(dialogParam.title);
			}else{
				dialog.setTitle("请选择日期");
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
			
		}catch (ParseException e) {
			e.printStackTrace();
		} 
	}


	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		d.set(Calendar.YEAR, year);
		d.set(Calendar.MONTH, monthOfYear);
		d.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	}
	public void show(){
		dialog.show();
	}
}
