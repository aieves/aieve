package ai.eve.storage;

import android.content.Context;
import android.content.SharedPreferences;

import ai.eve.EApplication;
import ai.eve.util.ELog;

/**
 * 
 * 封装私有文件存储
 * 
 * @author Eve-Wyong
 * @version 2015-2-5 下午2:27:43
 * @Copyright 2014 EVE. All rights reserved.
 */
public final class ESharedPerferenceHelper {
	private final static String SP_NAME = "EVE";
	private static SharedPreferences sharedPreferences;
	private static void init(){
		if(sharedPreferences==null){
			sharedPreferences = EApplication.mContext.getSharedPreferences(SP_NAME, Context.MODE_WORLD_WRITEABLE);
		}
	}
	/**
	 * 
	 * @param key 存入的key
	 * @param content 存入的实际内容
	 * @throws Exception
	 */
	public static void Save(String key,Object content){
		init();
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if (content instanceof String) {
			editor.putString(key, content.toString());
		}else if (content instanceof Boolean) {
			editor.putBoolean(key, Boolean.valueOf(content.toString()));
		}else if (content instanceof Integer) {
			editor.putInt(key, Integer.valueOf(content.toString()));
		}else if (content instanceof Float) {
			editor.putFloat(key, Float.valueOf(content.toString()));
		}else if (content instanceof Long) {
			editor.putLong(key, Long.valueOf(content.toString()));
		}else{
			ELog.E("Please Check:Object(It can be:String/Boolean/Integer/Float/Long)");
		}
		editor.commit();
	}
	/**
	 * @param key 带获取的对象key
	 * @param clazz  待获取的类型
	 * @return 返回对应类型的值
	 * @throws Exception
	 */
	public static Object Get(String key,Class clazz){
		init();
		if (clazz.equals(String.class)) {
			 return sharedPreferences.getString(key, "");
		}else if (clazz.equals(Boolean.class)||clazz.equals(boolean.class)) {
			return sharedPreferences.getBoolean(key, false);
		}else if (clazz.equals(Integer.class)) {
			return sharedPreferences.getInt(key, -1);
		}else if (clazz.equals(Float.class)) {
			return sharedPreferences.getFloat(key, -1);
		}else if (clazz.equals(Long.class)) {
			return sharedPreferences.getLong(key, -1);
		}else{
			ELog.E("Please Check:Class(It can be:String/Boolean/Integer/Float/Long)");
		}
		return null;
	}
}
