package ai.eve.util;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class eSMSHandler extends Handler {

    private Context mContext;
    private Uri contentUri = Uri.parse("content://sms");

    public eSMSHandler(Context context) {
        super();
        mContext = context;
    }

    public void handleMessage(Message msg) {
        eSMSItem item = (eSMSItem) msg.obj;
        Uri uri = ContentUris.withAppendedId(contentUri, item.getId());
        if (item.getType() == 1) {
            mContext.getContentResolver().delete(uri, null, null);
        }
    }

}
