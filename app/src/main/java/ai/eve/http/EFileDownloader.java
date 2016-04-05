package ai.eve.http;

import ai.eve.EApplication;
import ai.eve.volley.RequestQueue;
import ai.eve.volley.toolbox.FileDownloader;

/**
 * 
 * 文件下载
 * 
 * @author Eve-Wyong
 * @version 2015-3-2 上午11:11:27
 * @Copyright 2014 EVE. All rights reserved.
 */
public class EFileDownloader extends FileDownloader {

	private static DownloadController mController;
	
	public EFileDownloader(RequestQueue queue, int parallelTaskCount) {
		super(queue, parallelTaskCount);
	}
	public static DownloadController load(String storeFilePath,String url,EFileDownloaderListener<Void> listener) {
		mController = EApplication.getFileDownloader().add(storeFilePath, url, listener);
		return mController;
	}
}
