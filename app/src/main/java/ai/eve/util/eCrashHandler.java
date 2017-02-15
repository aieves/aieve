package ai.eve.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import ai.eve.EApplication;
import ai.eve.http.ERequest;

public class eCrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "eCrashHandler";
	/** 系统默认的UncaughtException处理类 */
	private UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private volatile static eCrashHandler INSTANCE;

	/** 程序的Context对象 */
	private Context mContext;

	/** 用来存储设备信息和异常信息 */
	private Map<String, String> infos = new HashMap<String, String>();
	/** 用于格式化日期,作为日志文件名的一部分 */
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	/** 保证只有一个CrashHandler实例 */
	private eCrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static eCrashHandler getInstance() {
		if (INSTANCE == null) {
			synchronized (eCrashHandler.class){
				if(INSTANCE==null){
					INSTANCE = new eCrashHandler();
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else { // 如果自己处理了异常，则不会弹出错误对话框，则需要手动退出app
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @return true代表处理该异常，不再向上抛异常，
	 *         false代表不处理该异常(可以将该log信息存储起来)然后交给上层(这里就到了系统的异常处理)去处理，
	 *         简单来说就是true不会弹出那个错误提示框，false就会弹出
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "程序出现异常,即将退出....", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// saveCrashInfo(ex);
		// 保存日志文件并上传
		postReport(saveCrashInfo(ex));
		return false;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回字符串,传送到服务器
	 */
	private String saveCrashInfo(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		return sb.toString();
	}

	// 使用HTTP Post 发送错误报告到服务器
	private void postReport(String string) {
		// 在上传的时候还可以将该app的version，该手机的机型等信息一并发送的服务器，
		// Android的兼容性众所周知，所以可能错误不是每个手机都会报错，还是有针对性的去debug比较好
		ERequest e = new ERequest(null);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("errorStr", string);
		params.put("title", EApplication.errorTitle);
		params.put("emails", EApplication.errorEmails);
		e.doPost(EApplication.errorlogURL, params);
	}

}
