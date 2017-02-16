package ai.eve.html.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import ai.eve.R;
import ai.eve.html.webutil.model.MultiChoiceDialogParam;
import ai.eve.html.webutil.model.Option;


/**
 * 
 * @Description: TODO
 * @author WYONG
 * @date 2014-3-21 下午4:14:55
 * @version V1.0
 * 
 */
public class MultiChoiceDialog extends BaseDialog implements
		View.OnClickListener {
	/*
	 * private Context mContext; private String mTitleString; private String
	 * leftBtn ; private String rightBtn; private AlertDialog.Builder builder;
	 * private ArrayList<Option> options ; private MultiChoiceDialogParam
	 * dialogParam;
	 * 
	 * public MultiChoiceDialog(Context context, String param) { super(context,
	 * android.R.style.Theme_Dialog); this.mContext = context; dialogParam =
	 * (MultiChoiceDialogParam
	 * )JSON.parseObject(param,MultiChoiceDialogParam.class); init(); }
	 * 
	 * private void init() { options = dialogParam.options;
	 * if(dialogParam.title!=null && !dialogParam.title.equals("")){
	 * mTitleString = dialogParam.title; }else{ mTitleString=
	 * mContext.getResources().getString(R.string.cancel); }
	 * if(dialogParam.ok!=null && dialogParam.ok.text!=null &&
	 * !dialogParam.ok.text.equals("")){ leftBtn=dialogParam.ok.text; }else{
	 * leftBtn = mContext.getResources().getString(R.string.confrim); }
	 * if(dialogParam.cancel!=null && dialogParam.cancel.text!=null &&
	 * !dialogParam.cancel.text.equals("")){ rightBtn=dialogParam.cancel.text;
	 * }else{ rightBtn = mContext.getResources().getString(R.string.cancel); }
	 * 
	 * builder = new AlertDialog.Builder(mContext); boolean [] bary = new
	 * boolean [options.size()]; String [] a =new String [options.size()]; for
	 * (int i = 0; i < options.size(); i++) { a[i]= options.get(i).text;
	 * bary[i]=options.get(i).selected; }
	 * 
	 * builder.setTitle(mTitleString); builder.setMultiChoiceItems(a , bary, new
	 * DialogInterface.OnMultiChoiceClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which, boolean
	 * isChecked) { options.get(which).selected = isChecked; } });
	 * 
	 * 
	 * if(dialogParam.ok!=null){ builder.setPositiveButton(leftBtn, new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * if(dialogParam.ok.callback.length()>0){
	 * getmOnJsCallBack().onLoad(dialogParam
	 * .ok.callback,JSON.toJSONString(getSelected())); } } }); }
	 * if(dialogParam.cancel!=null){ builder.setNegativeButton(rightBtn, new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * if(dialogParam.cancel.callback.length()>0){
	 * getmOnJsCallBack().onLoad(dialogParam.cancel.callback,""); } } }); } }
	 * 
	 * public void show(){ builder.show(); }
	 * 
	 * private ArrayList<Option> getSelected(){ ArrayList<Option> os = new
	 * ArrayList<Option>(); for (Option option : options) { if(option.selected){
	 * os.add(option); } } return os; }
	 */

	private static final String TAG = "CustomDialog";
	private Context mContext;
	private LayoutInflater mInflater;
	private Button mLeftBtn, mRightBtn;
	private TextView mTitleTV;
	private FrameLayout content;
	private ImageView mImageView;
	private MultiChoiceDialogParam dialogParam;
	private ArrayList<Option> options;
	private ChoiceAdapter adapter;

	public MultiChoiceDialog(Context context, String mParam) {
		super(context, R.style.eve_Theme_Dialog);
		this.mContext = context;
		dialogParam = (MultiChoiceDialogParam) JSON.parseObject(mParam,
                              MultiChoiceDialogParam.class);
		init();
	}

	private void init() {
		options = dialogParam.options;
		mInflater = LayoutInflater.from(mContext);
		View layout = mInflater.inflate(R.layout.eve_custom_alert_dialog_ll, null);
		mLeftBtn = (Button) layout
				.findViewById(R.id.custom_alert_dialog_left_btn);
		mLeftBtn.setOnClickListener(this);
		mRightBtn = (Button) layout
				.findViewById(R.id.custom_alert_dialog_right_btn);
		mRightBtn.setOnClickListener(this);
		mTitleTV = (TextView) layout
				.findViewById(R.id.custom_alert_dialog_title_textview);
		content = (FrameLayout) layout.findViewById(R.id.content);
		mImageView = (ImageView) layout
				.findViewById(R.id.custom_alert_dialog_title_img);
		setContentView(layout);
		if (dialogParam.title != null && dialogParam.title.length() > 0) {
			mTitleTV.setText(dialogParam.title);
		} else {
			mTitleTV.setText(mContext.getResources().getString(R.string.eve_title));
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

		LinearLayout l = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.eve_multi_choice, null);
		final ListView list = (ListView) l.findViewById(R.id.listview);
		adapter = new ChoiceAdapter(mContext, options,
				R.layout.eve_multichoice_list_item);
		list.setAdapter(adapter);
		content.addView(l);
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				options.get(position).selected = ((CheckedTextView) view).isChecked();
			}
		});
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.custom_alert_dialog_left_btn) {
			if (dialogParam.ok != null && dialogParam.ok.callback != null) {
				getmOnJsCallBack().onLoad(dialogParam.ok.callback,
						JSON.toJSONString(getSelected()));
			}
			this.dismiss();

		} else if (i == R.id.custom_alert_dialog_right_btn) {
			if (dialogParam.cancel != null
					&& dialogParam.cancel.callback != null) {
				getmOnJsCallBack().onLoad(dialogParam.cancel.callback, "");
			}
			this.dismiss();

		} else {
		}
	}

	private ArrayList<Option> getSelected() {
		ArrayList<Option> os = new ArrayList<Option>();
		for (Option option : options) {
			if (option.selected) {
				os.add(option);
			}
		}
		return os;
	}

}
