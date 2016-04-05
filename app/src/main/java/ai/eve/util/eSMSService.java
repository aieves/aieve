package ai.eve.util;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

/**
 * <p>
 *  短信服务类
 * </p>
 * @author Eve-huoyaning
 * @version 2015-09-18 下午02:00:25
 * @Copyright 2015 EVE5. All rights reserved.
 */
public class eSMSService extends Service {

    private ContentObserver mObserver;
    private String filter;
    private Uri contentUri = Uri.parse("content://sms");

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        filter = intent.getStringExtra("filter");
        addSMSObserver();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void addSMSObserver() {
        ContentResolver contentResolver = getContentResolver();
        Handler eSMSHandler = new eSMSHandler(this);
        mObserver = new eSMSObserver(contentResolver, eSMSHandler, filter);
        contentResolver.registerContentObserver(contentUri, true, mObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
        System.exit(0);
    }
}
