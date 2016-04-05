package ai.eve.util;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class eSMSObserver extends ContentObserver {

	public final String TAG = "SMSObserver";

	private final String[] PROJECTION = new String[] { eSMSConstant._ID,// 0
			eSMSConstant.TYPE,// 1
			eSMSConstant.ADDRESS,// 2
			eSMSConstant.BODY,// 3
			eSMSConstant.DATE,// 4
			eSMSConstant.THREAD_ID,// 5
			eSMSConstant.READ,// 6
			eSMSConstant.PROTOCOL // 7
	};
	private final String SELECTION = eSMSConstant._ID
			+ " > %s"
			+
			// " and " + SMS.PROTOCOL + " = null" +
			// " or " + SMS.PROTOCOL + " = " + SMS.PROTOCOL_SMS + ")" +
			" and (" + eSMSConstant.TYPE + " = " + eSMSConstant.MESSAGE_TYPE_INBOX + " or "
			+ eSMSConstant.TYPE + " = " + eSMSConstant.MESSAGE_TYPE_SENT + ")";

	private static final int COLUMN_INDEX_ID = 0;
	private static final int COLUMN_INDEX_TYPE = 1;
	private static final int COLUMN_INDEX_PHONE = 2;
	private static final int COLUMN_INDEX_BODY = 3;
	private static final int COLUMN_INDEX_PROTOCOL = 7;

	private static final int MAX_NUMS = 10;
	private static int MAX_ID = 0;

	private ContentResolver mResolver;
	private Handler mHandler;
	
	private boolean flag = false;
	
	private String filterNumber;
	
	public eSMSObserver(ContentResolver contentResolver, Handler handler, String filterNumber) {
		super(handler);
		this.filterNumber = filterNumber;
		this.mResolver = contentResolver;
		this.mHandler = handler;
	}

	@Override
	public void onChange(boolean selfChange) {
		Log.i(TAG, "onChange : " + selfChange + "; " + MAX_ID + "; "
				+ SELECTION);
		super.onChange(selfChange);

		Cursor cursor = mResolver.query(eSMSConstant.CONTENT_URI, PROJECTION,
				String.format(SELECTION, MAX_ID), null, null);
  
		int id, type, protocol;
		String phone, body;
		Message message;
		eSMSItem item;

		int iter = 0;
		boolean hasDone = false;

		while (cursor.moveToNext()) {
			id = cursor.getInt(COLUMN_INDEX_ID);
			type = cursor.getInt(COLUMN_INDEX_TYPE);
			phone = cursor.getString(COLUMN_INDEX_PHONE);
			body = cursor.getString(COLUMN_INDEX_BODY);
			protocol = cursor.getInt(COLUMN_INDEX_PROTOCOL);

			if (hasDone) {
				MAX_ID = id;
				break;
			}
			if (protocol == eSMSConstant.PROTOCOL_SMS && body != null) {
				hasDone = true;
				if(phone.equals(filterNumber)){
					flag = true;
				}else{
					flag = false;
				}
				if(!flag){
					return;
				}
				item = new eSMSItem();
				item.setId(id);
				item.setType(type);
				item.setPhone(phone);
				item.setBody(body);
				item.setProtocol(protocol);

				message = new Message();
				message.obj = item;
				
				mHandler.sendMessage(message);
			} else {
				if (id > MAX_ID)
					MAX_ID = id;
			}
			if (iter > MAX_NUMS)
				break;
			iter++;
		}
	}
}
