package ai.eve.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * <p>
 *  系统拨号
 * </p>
 * @author Eve-huoyaning
 * @version 2015-09-18 上午03:27:25
 * @Copyright 2015 EVE5. All rights reserved.
 */
public class EPhone {

    /**
     * 调用系统拨号
     * @param context
     * @param phoneNum
     */
    public static void Call(Context context, String phoneNum) {
        if (phoneNum == null || phoneNum.length() == 0) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum.trim()));
        context.startActivity(intent);
    }

    /**
     * 调用系统拨号界面
     * @param context
     * @param phoneNum
     */
    public static void LauncherPhoneApp(Context context, String phoneNum) {
        if (phoneNum == null || phoneNum.length() == 0) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNum));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
