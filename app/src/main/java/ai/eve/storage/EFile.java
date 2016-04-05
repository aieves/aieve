package ai.eve.storage;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import org.apache.http.util.EncodingUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import ai.eve.util.ELog;

/**
 * <p>
 * 文件操作工具类
 * </p>
 * 
 * @author EVE-hanym
 * @version 2015-9-11
 * @Copyright 2015 EVE. All rights reserved.
 */
public class EFile {

	/**
	 * 读取res/raw下的内容
	 * 
	 * @param context
	 *            当前页面的上下文
	 * @param fileId
	 *            当前资源的id,如R.raw.health，其中"health"是文件的名字
	 * @return 读取到的文本内容
	 */
	public static InputStream GetInputStreamfromRaw(Context context, int fileId) {
		String result = "";
		try {
			InputStreamReader inputReader = new InputStreamReader(context
					.getResources().openRawResource(fileId));
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			result = "";
			while ((line = bufReader.readLine()) != null)
				result += line;

			return new ByteArrayInputStream(result.getBytes("UTF-8"));
		} catch (Exception e) {
			ELog.E(e.getMessage());
		}
		return new StringBufferInputStream(result);
	}
	
	/**
	 * 读取assets的内容
	 * 
	 * @param context
	 *            当前页面的上下文
	 * @param fileName
	 *            文件的名字，如果带有多目录如"txt/health.txt"，txt是assets的下个目录
	 * @return assets的文本内容
	 */
	public static InputStream GetInputStreamFromAssets(Context context,
			String fileName) {
		String result = "";
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
			return new ByteArrayInputStream(result.getBytes("UTF-8"));
		} catch (Exception e) {
			ELog.E(e.getMessage());
		}
		return new StringBufferInputStream(result);
	}

	/**
	 * 判断是否存在sdcard
	 * 
	 * @return
	 */
	public static boolean IsSDCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取sdcard绝对路径
	 * 
	 * @return sd卡的路径
	 */
	public static String GetSDCardPath() {
		String path = null;
		if (IsSDCardExist())
			path = Environment.getExternalStorageDirectory().toString();
		return path;
	}

	/**
	 * 获取外部,sdcard的剩余空间大小
	 * 
	 * @return sd卡的大小
	 */
	public static long GetFreeSpaceOfSDCard() {
		String state = Environment.getExternalStorageState();
		long result = 0;

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long availCount = sf.getAvailableBlocks();
			result = availCount * blockSize;
		}

		return result;
	}

	/**
	 * * 保存内容（以私有文件形式保存） * 此文件保存在Data.app.程序包.file目录下面。
	 * 
	 * @param context
	 *            当前上下文 *
	 * @param fileName
	 *            文件名称 *
	 * @param content
	 *            文件内容 *
	 * @throws Exception
	 */
	public static void SavePrivateFile(Context context, String fileName,
			String content) throws Exception {
		FileOutputStream outputStream = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		outputStream.write(content.getBytes());
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * * 保存内容（以私有文件形式保存+追加） *此文件保存在Data.app.程序包.file目录下面。
	 * 
	 * @param context
	 *            当前上下文 *
	 * @param fileName
	 *            文件名称 *
	 * @param content
	 *            文件内容 *
	 * @throws Exception
	 */
	public static void SavePrivateFileAppendable(Context context,
			String fileName, String content) throws Exception {
		FileOutputStream outputStream = context.openFileOutput(fileName,
				Context.MODE_APPEND);
		outputStream.write(content.getBytes());
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 保存内容（允许其他应用读取）此文件保存在Data.app.程序包.file目录下面。
	 * 
	 * @param context
	 *            当前上下文
	 * @param fileName
	 *            文件名称
	 * @param content
	 *            文件内容
	 * @throws Exception
	 */
	public static void SaveReadable(Context context, String fileName,
			String content) throws Exception {
		FileOutputStream outputStream = context.openFileOutput(fileName,
				Context.MODE_WORLD_READABLE);
		outputStream.write(content.getBytes());
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 返回uri的真实路径
	 * 
	 * @param mContext
	 *            当前上下文
	 * @param fileUrl
	 *            文件的url路径
	 * @return
	 */
	public static String GetRealPath(Context mContext, Uri fileUrl) {
		String result = null;
		String[] projection = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(mContext, fileUrl, projection,
				null, null, null);
		Cursor cursor = loader.loadInBackground();

		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
			cursor.close();
		}

		return result;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param dirPath
	 *            文件夹所在路径
	 * @return
	 */
	public static File MKDirs(String dirPath) {
		if (dirPath == null)
			return null;

		File path1 = new File(dirPath);
		if (!path1.exists()) {
			// 若不存在，创建目录，可以在应用启动的时候创建
			path1.mkdirs();
		}
		return path1;
	}

	/**
	 * 创建文件,如果文件存在,则不进行操作 /mnt/sdcard/eve/eve.prop
	 * 
	 * @param filePath
	 *            文件的绝对路径
	 * @return
	 */
	public static File CreateFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists())
			try {
				findParent(filePath);
				file.createNewFile();
			} catch (IOException e) {
				ELog.E(e.getMessage());
			}

		return file;
	}

	private static File findParent(String s) {
		String parentPath = s.substring(0, s.lastIndexOf("/"));
		File file = new File(parentPath);
		if (!file.exists()) {
			MKDirs(parentPath);
		}
		return file;
	}

	/**
	 * 查找文件
	 * 
	 * @param file
	 *            所要查找的文件目录
	 * @param key_search
	 *            关键词
	 * @return
	 */
	public static List<File> FindFile(File file, String key_search) {
		List<File> list = new ArrayList<File>();
		if (file.isDirectory()) {
			File[] all_file = file.listFiles();
			if (all_file != null) {
				for (File tempf : all_file) {
					if (tempf.isDirectory()) {
						if (tempf.getName().toLowerCase()
								.lastIndexOf(key_search) > -1) {
							list.add(tempf);
						}
						list.addAll(FindFile(tempf, key_search));
					} else {
						if (tempf.getName().toLowerCase()
								.lastIndexOf(key_search) > -1) {
							list.add(tempf);
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径
	 * @param newPath
	 *            String 复制后路径
	 * @return boolean
	 */
	public static boolean CopyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
			return true;
		} catch (Exception e) {
			ELog.E("复制单个文件错误");
			ELog.E(e.getMessage());
			return false;
		}
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：
	 * @param newPath
	 *            String 复制后路径 如：
	 * @return boolean
	 */
	public static boolean CopyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					CopyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
			return true;
		} catch (Exception e) {
			ELog.E("复制整个文件夹内容操作出错");
			ELog.E(e.getMessage());
			return false;
		}

	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 true
			return true;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将字符串保存在file中
	 * 
	 * @param filePath
	 *            要存放数据的文件路径
	 * @param content
	 *            数据
	 */
	public static boolean Save2File(String filePath, String content) {
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {

				findParent(filePath);
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			byte[] contentInBytes = content.getBytes();

			fos.write(contentInBytes);
			fos.flush();
			fos.close();

			return true;
		} catch (IOException e) {
			ELog.E(e.getMessage());
			return false;
		}
	}

	/**
	 * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
	 * 
	 * @param distFile
	 *            指定文件
	 * @param res
	 *            原字符串
	 * @return
	 */
	public static boolean SaveString2File(File distFile, String res) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));

			char buf[] = new char[1024]; // 字符缓冲区
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 从文件中读取数据
	 * 
	 * @param file
	 *            要读取的文件
	 * @return
	 */
	public static byte[] ReadFromFile(File file) {
		int len = 1024;
		byte[] buffer = new byte[len];
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int nrb = 0; // read up to len bytes

			nrb = fis.read(buffer, 0, len);
			while (nrb != -1) {
				baos.write(buffer, 0, nrb);
				nrb = fis.read(buffer, 0, len);
			}
			buffer = baos.toByteArray();
		} catch (Exception e1) {
			ELog.E(e1.getMessage());
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				ELog.E(e.getMessage());
			}
		}
		return buffer;

	}

	/**
	 * 根据文件名称读取应用程序下files目录下的文件内容
	 * 
	 * @param context
	 *            当前上下文
	 * @param fileName
	 *            指的是保存在/data/data/应用名称/files/文件名（文件名包括后缀名）
	 * @return 文件内容的btye[]
	 * @throws Exception
	 */
	public static byte[] ReadAppPrivateFile(Context context, String fileName) {
		try {
			FileInputStream inputStream = null;

			inputStream = context.openFileInput(fileName);

			byte[] buffer = new byte[1024];
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			int len = -1;
			while ((len = inputStream.read(buffer)) != -1) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			byte[] data = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.close();
			inputStream.close();
			return data;
		} catch (Exception e) {
			ELog.E(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return long
	 */
	public static long GetFolderSize(File file) {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					size = size + GetFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			ELog.E(e.getMessage());
		}
		return size;
	}

	/**
	 * 压缩文件
	 * 
	 * @param srcFile
	 *            要压缩的文件目录
	 * @param zipFilePath
	 *            压缩后的文件存放路径（绝对路径）
	 * @return
	 */
	public static boolean Zip(File srcFile, String zipFilePath) {
		boolean flag = false;
		try {
			if (!srcFile.exists())
				throw new RuntimeException(" 要压缩的文件不存在");

			Project prj = new Project();
			Zip zip_ = new Zip();
			zip_.setProject(prj);
			zip_.setDestFile(new File(zipFilePath));

			FileSet fileSet = new FileSet();
			fileSet.setProject(prj);
			fileSet.setDir(srcFile);
			zip_.addFileset(fileSet);

			zip_.execute();
			flag = true;
		} catch (Exception ex) {
			ELog.E(ex.getMessage());
		}
		return flag;
	}

	/**
	 * 解压文件
	 * 
	 * @param srcFile
	 *            压缩的文件
	 * @param outputDirectory
	 *            解压后文件的存放路径（绝对路径），文件目录
	 * @return
	 */
	public static boolean UnZip(File srcFile, String outputDirectory) {
		boolean flag = false;
		int buffer = 1024;
		try {
			ZipFile zipFile = new ZipFile(srcFile);
			Enumeration e = zipFile.getEntries();
			ZipEntry zipEntry = null;
			createDirectory(outputDirectory, "");
			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				System.out.println("unzip " + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName().trim();
					name = name.substring(0, name.length() - 1);
					File f = new File(outputDirectory + File.separator + name);
					if (!f.exists()) {
						f.mkdir();
					}
				} else {
					String fileName = zipEntry.getName();
					fileName = fileName.replace('\\', '/');
					if (fileName.indexOf("/") != -1) {
						createDirectory(outputDirectory, fileName.substring(0,
								fileName.lastIndexOf("/")));
						fileName = fileName
								.substring(fileName.lastIndexOf("/") + 1);
					}
					File f = new File(outputDirectory + File.separator
							+ zipEntry.getName());

					if (!f.exists()) {
						findParent(outputDirectory);
						f.createNewFile();
					}

					InputStream in = zipFile.getInputStream(zipEntry);
					FileOutputStream out = new FileOutputStream(f);
					byte[] by = new byte[buffer];
					int c;
					while ((c = in.read(by)) != -1) {
						out.write(by, 0, c);
					}
					in.close();
					out.close();
				}
			}
			flag = true;
		} catch (Exception ex) {
			ELog.E(ex.getMessage());
		}
		return flag;
	}

	/**
	 * 创建目录
	 * 
	 * @param directory
	 *            主目录
	 * @param subDirectory
	 *            子目录
	 */
	private static void createDirectory(String directory, String subDirectory) {
		String dir[];
		File fl = new File(directory);
		try {
			if (subDirectory == "" && fl.exists() != true) {
				fl.mkdir();
			} else if (subDirectory != "") {
				dir = subDirectory.replace('\\', '/').split("/");
				for (int i = 0; i < dir.length; i++) {
					File subFile = new File(directory + File.separator + dir[i]);
					if (subFile.exists() == false) {
						subFile.mkdir();
					}
					directory += File.separator + dir[i];
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 获取app缓存路径
	 * 
	 * @param context
	 *            当前上下文
	 * @return
	 */
	public static String GetCacheDirectory(Context context) {
		String cacheDirPath = null;
		File cacheDir = context.getCacheDir();// getCacheDir();
		if (cacheDir != null) {
			cacheDirPath = cacheDir.getAbsolutePath();
		}
		return cacheDirPath;
	}

}
