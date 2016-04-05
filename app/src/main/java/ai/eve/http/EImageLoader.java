package ai.eve.http;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import ai.eve.EApplication;
import ai.eve.volley.NetworkResponse;
import ai.eve.volley.RequestQueue;
import ai.eve.volley.request.ImageRequest;
import ai.eve.volley.toolbox.ImageLoader;

public class EImageLoader extends ImageLoader {

	public static final String RES_ASSETS = "file:///android_asset://";
	public static final String RES_SDCARD = "file:///android_sdcard://";
	private static final String RES_HTTP = "http://";
	private static final String RES_HTTPS = "https://";

	private static final String baseUrl = EApplication.remoteImgURL;

	private static AssetManager assetManager;

	public EImageLoader(RequestQueue queue, ImageCache imageCache) {
		super(queue, imageCache);
	}

	@Override
	public ImageRequest buildRequest(String requestUrl, int maxWidth,int maxHeight) {
		ImageRequest request;
		if (requestUrl.startsWith(RES_ASSETS)) {
			if (assetManager == null) {
				assetManager = EApplication.mContext.getAssets();
			}
			request = new ImageRequest(requestUrl.substring(RES_ASSETS.length()), maxWidth,maxHeight) {
				@Override
				public NetworkResponse perform() {
					try {
						return new NetworkResponse(toBytes(assetManager.open(getUrl())),HTTP.UTF_8);
					} catch (IOException e) {
						return new NetworkResponse(new byte[1], HTTP.UTF_8);
					}
				}
			};
		} else if (requestUrl.startsWith(RES_SDCARD)) {
			request = new ImageRequest(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + requestUrl.substring(RES_SDCARD.length()), maxWidth,maxHeight) {
				@Override
				public NetworkResponse perform() {
					try {
						return new NetworkResponse(toBytes(new FileInputStream(getUrl())), HTTP.UTF_8);
					} catch (IOException e) {
						return new NetworkResponse(new byte[1], HTTP.UTF_8);
					}
				}
			};
		} else if (requestUrl.startsWith(RES_HTTP) || requestUrl.startsWith(RES_HTTPS)) {
			request = new ImageRequest(requestUrl, maxWidth, maxHeight);
		} else {
			return null;
		}
		request.setCacheExpireTime(TimeUnit.MINUTES, EApplication.IMAGE_CACHEEXPIRETIME);
		return request;
	}

	private byte[] toBytes(InputStream inputStream) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			out.write(buffer);
			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 适合加载网络图片,SD卡图片,assets下图片
	 * @param imageView
	 * @param url 通过URL前缀区分网络、SD卡、ASSETS图片来源
	 * @param defaultImageResId
	 * @param errorImageResId
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static ImageContainer loadImage(ImageView imageView, String url,int defaultImageResId, int errorImageResId, int maxWidth,int maxHeight) {
		return EApplication.getImageLoader().get(baseUrl+url, EImageLoader.getImageListener(imageView, defaultImageResId, errorImageResId), maxWidth, maxHeight);
	}
	/**
	 * 适合加载网络图片,SD卡图片,assets下图片
	 * @param imageView
	 * @param url 通过URL前缀区分网络、SD卡、ASSETS图片来源
	 * @param defaultImageResId
	 * @param errorImageResId
	 * @return
	 */
	public static ImageContainer loadImage(ImageView imageView, String url,int defaultImageResId, int errorImageResId) {
		return EApplication.getImageLoader().get(baseUrl+url, EImageLoader.getImageListener(imageView, defaultImageResId, errorImageResId), 0, 0);
	}

	/**
	 * 仅仅适合加载网络图片
	 * @param imagePath
	 * @param imageView
	 * @param defaultImageResId
	 * @param errorImageResId
	 * @param config
	 */
	public static void loadImage(String imagePath, ImageView imageView,int defaultImageResId, int errorImageResId, Bitmap.Config config) {
		com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.showStubImage(defaultImageResId);
		builder.showImageOnFail(errorImageResId);
		builder.bitmapConfig(config);
		builder.cacheInMemory();
		builder.cacheOnDisc();
		DisplayImageOptions displayImageOptions = builder.build();
		imageLoader.displayImage(baseUrl+imagePath, imageView, displayImageOptions);
	}
	/**
	 * 仅仅适合加载网络图片
	 * @param imagePath
	 * @param imageView
	 * @param defaultImageResId
	 * @param errorImageResId
	 */
	public static void loadImage(String imagePath, ImageView imageView,int defaultImageResId, int errorImageResId) {
		com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.showStubImage(defaultImageResId);
		builder.showImageOnFail(errorImageResId);
		builder.cacheInMemory();
		builder.cacheOnDisc();
		DisplayImageOptions displayImageOptions = builder.build();
		imageLoader.displayImage(baseUrl+imagePath, imageView, displayImageOptions);
	}
}
