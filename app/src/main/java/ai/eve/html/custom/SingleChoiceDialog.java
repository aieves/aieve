package ai.eve.html.custom;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import ai.eve.R;
import ai.eve.html.webutil.model.Option;
import ai.eve.html.webutil.model.SingleChoiceDialogParam;

/**
 *
 * @Description: TODO
 * @author WYONG
 * @date 2014-3-21 下午4:14:55
 * @version V1.0
 *
 */
public class SingleChoiceDialog extends BaseDialog implements View.OnClickListener {
    
    private static final String TAG = "CustomDialog";
	private Context mContext;
	private LayoutInflater mInflater;
	private Button mLeftBtn,mRightBtn;
	private TextView mTitleTV ;
	private FrameLayout content;
	private ImageView mImageView;
	private SingleChoiceDialogParam dialogParam;
    private ArrayList<Option> options ;
    private int selectItem=-1;
    private ChoiceAdapter adapter;

	public SingleChoiceDialog(Context context, String mParam) {
		super(context, R.style.Theme_Dialog);
		this.mContext = context;
        dialogParam = (SingleChoiceDialogParam) JSON.parseObject(mParam, SingleChoiceDialogParam.class);
		init();
	}
	
	private void init() {
		options = dialogParam.options;
		mInflater = LayoutInflater.from(mContext);
		View layout = mInflater.inflate(R.layout.custom_alert_dialog_ll, null);
		mLeftBtn = (Button) layout.findViewById(R.id.custom_alert_dialog_left_btn);
		mLeftBtn.setOnClickListener(this);
		mRightBtn = (Button) layout.findViewById(R.id.custom_alert_dialog_right_btn);
		mRightBtn.setOnClickListener(this);
		mTitleTV = (TextView) layout.findViewById(R.id.custom_alert_dialog_title_textview);
		content = (FrameLayout) layout.findViewById(R.id.content);
		mImageView = (ImageView) layout.findViewById(R.id.custom_alert_dialog_title_img);
		setContentView(layout);
        if(dialogParam.title!=null&&dialogParam.title.length()>0){
            mTitleTV.setText(dialogParam.title);
        }else{
            mTitleTV.setText(mContext.getResources().getString(R.string.title));
        }
        if(dialogParam.ok==null){
            mLeftBtn.setVisibility(View.GONE);
        }else{
            if(dialogParam.ok.text!=null&&dialogParam.ok.text.length()>0){
                mLeftBtn.setText(dialogParam.ok.text);
            }else{
                mLeftBtn.setText(mContext.getString(R.string.confrim));
            }
        }
        if(dialogParam.cancel==null){
            mRightBtn.setVisibility(View.GONE);
        }else{
            if(dialogParam.cancel.text!=null&&dialogParam.cancel.text.length()>0){
                mRightBtn.setText(dialogParam.cancel.text);
            }else{
                mRightBtn.setText(mContext.getString(R.string.cancel));
            }
        }
        
        
       LinearLayout l = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.single_choice, null);
       final ListView list = (ListView)l.findViewById(R.id.listview);
       adapter = new ChoiceAdapter(mContext,options,R.layout.singlechoice_list_item);
       list.setAdapter(adapter);
       content.addView(l);
       list.setOnItemClickListener(new OnItemClickListener() {
    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position,
    			long id) {
    		   selectItem = position;
    	}
       });
	}
	
	public interface OnConfirmBtnClickListener{
		void onConfirmClick(int position);
	}
	
	private OnConfirmBtnClickListener clickListener;
	
	
	
	public void setClickListener(OnConfirmBtnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.custom_alert_dialog_left_btn) {
			if (clickListener != null) {
				if (selectItem == -1) {
					Toast.makeText(mContext, "请选择一项", Toast.LENGTH_SHORT).show();
					return;
				}
				clickListener.onConfirmClick(selectItem);
			}
			if (dialogParam.ok != null && dialogParam.ok.callback != null) {
				if (selectItem == -1) {
					Toast.makeText(mContext, "请选择一项", Toast.LENGTH_SHORT).show();
				} else if (options.get(selectItem) != null) {
					getmOnJsCallBack().onLoad(dialogParam.ok.callback, JSON.toJSONString(options.get(selectItem)));
				} else {
					Toast.makeText(mContext, "请重新选择", Toast.LENGTH_SHORT).show();
				}
			}
			this.dismiss();

		} else if (i == R.id.custom_alert_dialog_right_btn) {
			if (dialogParam.cancel != null && dialogParam.cancel.callback != null) {
				getmOnJsCallBack().onLoad(dialogParam.cancel.callback, "");
			}
			this.dismiss();

		} else {
		}
	}
	

}
