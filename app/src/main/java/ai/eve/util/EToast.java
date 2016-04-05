package ai.eve.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wyong on 2016/1/20.
 */
public class EToast {
    public static void ShowShortMsg(Context mContext,String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    public static void ShowLongMsg(Context mContext,String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    }
    public static void ShowShortMsg(Context mContext,int msgRes){
        Toast.makeText(mContext,msgRes,Toast.LENGTH_SHORT).show();
    }

    public static void ShowLongMsg(Context mContext,int msgRes){
        Toast.makeText(mContext,msgRes,Toast.LENGTH_LONG).show();
    }
}
