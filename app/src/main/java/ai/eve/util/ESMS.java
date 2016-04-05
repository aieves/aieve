package ai.eve.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.List;

/**
 * <p>
 *  短信工具类 （发送短信、启动短信应用、截获短信）
 * </p>
 * @author Eve-huoyaning
 * @version 2015-09-18 上午09:47:25
 * @Copyright 2015 EVE5. All rights reserved.
 */
public class ESMS {
	/**
     * 发送短信
     * @param context
     * @param phoneNum
     * @param smsContent
     */
    public static void SendMessage(Context context, String phoneNum, String smsContent) {
       SendMessage(context, phoneNum, smsContent,true);
    }
    /**
     * 发送短信
     * @param context
     * @param phoneNum
     * @param smsContent
     */
    public static void SendMessage(Context context, String phoneNum, String smsContent,final boolean toast) {
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sentIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, new Intent("SENT_SMS_ACTION"), 0);
        PendingIntent deliverIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, new Intent("DELIVER_SMS_ACTION"), 0);
        smsManager.sendTextMessage(phoneNum, null, smsContent, sentIntent, deliverIntent);
        context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        if (toast) {
                            Toast.makeText(context.getApplicationContext(), "短信发送成功", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        if (toast) {
                            Toast.makeText(context.getApplicationContext(), "短信发送失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }, new IntentFilter("SENT_SMS_ACTION"));
        context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        if (toast) {
                            Toast.makeText(context.getApplicationContext(), "对方已经收到短信", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        }, new IntentFilter("DELIVER_SMS_ACTION"));
    }
    /**
     * 群发短信
     * @param context
     * @param phoneNums
     * @param smsContent
     */
    public static void SendMessage(Context context, List<String> phoneNums, String smsContent) {
        SendMessage(context, phoneNums, smsContent,true);
    }
    /**
     * 群发短信
     * @param context
     * @param phoneNums
     * @param smsContent
     */
    public static void SendMessage(Context context, List<String> phoneNums, String smsContent,boolean toast) {
        if (phoneNums != null && phoneNums.size() != 0) {
            SmsManager smsManager = SmsManager.getDefault();
            for (int i = 0; i < phoneNums.size(); i++) {
                smsManager.sendTextMessage(phoneNums.get(i), null, smsContent, null, null);
            }
            if (toast) {
                Toast.makeText(context.getApplicationContext(), "短信发送成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 启动短信应用， 进入短信应用界面
     * @param context
     * @param smsContent
     */
    public static void LaunchMsgApp(Context context, String smsContent) {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("smsContent", smsContent);
        context.startActivity(intent);
    }

    /**
     * 截获短信
     * @param context
     * @param filterPhoneNum 待拦截的手机号
     */
    public static void InterceptMessage(Context context, String filterPhoneNum) {
        Intent intent = new Intent(context, eSMSService.class);
        intent.putExtra("filter", filterPhoneNum);
        context.startService(intent);
    }

}
