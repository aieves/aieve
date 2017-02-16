package ai.eve.html.custom;

import java.util.Set;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;

import ai.eve.html.webutil.model.RedirectParam;

/**
 * Activity跳转
 * 
 * @Description: TODO
 * @author WYONG
 * @date 2014-3-21 下午4:15:16
 * @version V1.0
 * 
 */
public class Redirect {
	private Context mContext;
	private RedirectParam redirectParam;

	public Redirect(Context mContext, String param) {
		this.mContext = mContext;
        redirectParam = (RedirectParam) JSON.parseObject(param,
                RedirectParam.class);
	}
    
	public void startActivity() {
        if(redirectParam.action!=null){
            Intent intent = new Intent();
            intent.setAction(redirectParam.action);
            if(redirectParam.data!=null){
            	Set<String> keys = redirectParam.data.keySet();
            	for (String string : keys) {
                    intent.putExtra(string, String.valueOf(redirectParam.data.get(string)));
				}
            }
            intent.setFlags(redirectParam.flag);
            try {
                mContext.startActivity(intent);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
}
