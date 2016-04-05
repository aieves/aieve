package ai.eve;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Properties;

import ai.eve.http.EFileDownloader;
import ai.eve.http.EImageLoader;
import ai.eve.util.CookieHelper;
import ai.eve.util.eCrashHandler;
import ai.eve.volley.Network;
import ai.eve.volley.RequestQueue;
import ai.eve.volley.cache.BitmapImageCache;
import ai.eve.volley.cache.DiskCache;
import ai.eve.volley.stack.HurlStack;
import ai.eve.volley.toolbox.BasicNetwork;

/**
 *
 * <p>
 * 使用此框架的工程需继承此类
 * </p>
 * <p>
 * 作用：
 * </p>
 * <ol>
 * <li>DEBUG模式的切换</li>
 * <li>本地测试数据的存放路径初始化</li>
 * <li>远程服务器的地址初始化</li>
 * <li>多异步任务的管理类初始化</li>
 * </ol>
 *
 * <p>
 * 子类需覆盖的变量:
 * </p>
 * <ul>
 * <li>DEBUG(本地数据与远程数据之间的切换开关)</li>
 * <li>localURL(本地数据存放地址(基于assert目录))</li>
 * <li>remoteURL(远程数据地址)</li>
 * </ul>
 *
 * @author Eve-Wyong
 * @version 2015-2-23 下午11:03:06
 * @Copyright 2014 EVE. All rights reserved.
 */
public class EApplication extends Application {
    //11111
	public static boolean OPEN_LOG = true;

	/**
	 * DEBUG(本地数据与远程数据之间的切换开关)
	 */
	public static boolean DEBUG = false;
	/**
	 * localURL(本地数据存放地址(基于assets目录))
	 */
	public static String localURL = "";
	/**
	 * remoteURL(远程数据地址)
	 */
	public static String remoteURL = "";
	/**
	 * remoteImgURL(远程图片地址)
	 */
	public static String remoteImgURL = "";
	/**
	 * remoteURL(远程数据地址)
	 */
	public static String errorlogURL = "";

	public static String errorTitle = "";

	public static String errorEmails = "";

	/**
	 * 上下文，可直接使用
	 */
	public static Context mContext;

	/**
	 * 请求队列，一般不操作此类
	 */
	public static RequestQueue mQueue;
	/**
	 * 请求线程池大小
	 */
	public static int THREADPOOLSIZE = 8;

	/**
	 * 可自定义缓存目录，默认context.getCacheDir()+"/http"目录
	 */
	public static File mCacheFile;

	/**
	 * 图片加载工具
	 */
	private static EImageLoader mImageLoader;

	/**
	 * 请求缓存大小，默认10M
	 */
	public static int FILECACHESIZE = 10 * 1024 * 1024;
	/**
	 * 请求缓存过期时间，单位：分钟
	 */
	public static int FILE_CACHEEXPIRETIME = 20;

	/**
	 * 图片缓存大小，默认10M
	 */
	public static int IMAGECACHESIZE = 10 * 1024 * 1024;
	/**
	 * 图片缓存过期时间，单位：分钟
	 */
	public static int IMAGE_CACHEEXPIRETIME = 20;

	/**
	 * 文件下载工具
	 */
	private static EFileDownloader mDownloader;
	/**
	 * 下载线程数，最好不超过3
	 */
	public static int DOWNTHREADSIZE = 3;

	private Properties properties;

	/**
	 * 公钥（用于加密数据）
	 */
	public static String  rsaPublicKey="";
	/**
	 * 私钥（用于解密服务端传回数据）
	 */
	public static String  rsaPrivateKey="";
	
	@Override
	public void onCreate() {
		super.onCreate();
		initAPPProper();

		mContext = this.getApplicationContext();
		Network network = new BasicNetwork(new HurlStack(), HTTP.UTF_8);

		if (mCacheFile == null) {
			mCacheFile = mContext.getCacheDir();
		}
		// 设置缓冲池、缓存目录、缓存大小，待确认是否可行
		mQueue = new RequestQueue(network, THREADPOOLSIZE, new DiskCache(
				mCacheFile, FILECACHESIZE));
		// start and waiting requests.
		mQueue.start();
		initImageLoader(getApplicationContext());
		eCrashHandler crashHandler = eCrashHandler.getInstance();
		// 注册crashHandler
		crashHandler.init(getApplicationContext());
		
	}

	private void initAPPProper() {
		properties = new Properties();
		/* 通过类加载器,来加载配置文件 */
		try {
			InputStream in = getClass().getClassLoader().getResourceAsStream("conf/app.properties");
			if (in == null) {
				return;
			}
			properties.load(in);
			if (properties.containsKey("DEBUG")) {
				DEBUG = Boolean.valueOf(properties.getProperty("DEBUG"));
			}
			if (properties.containsKey("localURL")) {
				localURL = properties.getProperty("localURL");
			}
			if (properties.containsKey("remoteURL")) {
				remoteURL = properties.getProperty("remoteURL");
			}
			if (properties.containsKey("errorlogURL")) {
				errorlogURL = properties.getProperty("errorlogURL");
			}
			if (properties.containsKey("errorTitle")) {
				errorTitle = properties.getProperty("errorTitle");
			}
			if (properties.containsKey("errorEmails")) {
				errorEmails = properties.getProperty("errorEmails");
			}
			if (properties.containsKey("remoteImgURL")) {
				remoteImgURL = properties.getProperty("remoteImgURL");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getCookies() {
		return CookieHelper.getCookies(mContext);
	}

	public static void setCookies(HashMap<String, HttpCookie> c) {
		CookieHelper.setCookies(mContext, c);
	}

	public static void clearCookies() {
		CookieHelper.clearCookies(mContext);
	}

	public static String getReqCookies() {
		return CookieHelper.getReqCookies(mContext);
	}

	public static EImageLoader getImageLoader() {
		if (mImageLoader == null) {
			mImageLoader = new EImageLoader(EApplication.mQueue,
					new BitmapImageCache(IMAGECACHESIZE));
		}
		return mImageLoader;
	}

	public static EFileDownloader getFileDownloader() {
		if (mDownloader == null) {
			mDownloader = new EFileDownloader(EApplication.mQueue,
					DOWNTHREADSIZE);
		}
		return mDownloader;
	}

	private static void initImageLoader(Context context) {
		// 这个配置方法是自定义的，你可以自己调优这个配置，也可以调用默认的配置 在这个方法
		// ImageLoaderConfiguration.createDefault(this);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				// 线程的优先级数
				.denyCacheImageMultipleSizesInMemory()
				// 默认情况下是允许多个图片存在在缓存中，调用这个方法将否认这一做法。(个人理解，具体看源码)
				.discCache(new UnlimitedDiscCache(mCacheFile))
				.discCacheFileNameGenerator(new Md5FileNameGenerator())// 用于磁盘缓存，生成文件名
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 线程队列的加载方式，LIFO
																// last in first
																// out 后进先出，FIOF
																// first in
																// first out
																// 先进先出
				// .writeDebugLogs()//启动详细记录ImageLoader的工作日志
				.build();// 构建
		// 需要初始化ImageLoader这个配置
		ImageLoader.getInstance().init(config);
	}
}