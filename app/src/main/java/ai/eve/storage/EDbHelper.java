package ai.eve.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ai.eve.EApplication;
import ai.eve.util.ELog;

/**
 *封装了对数据库表的具体操作,比如:增删改查一张表 
 */
public class EDbHelper {
	private SQLiteOpenHelper sqliteHelper = null;
	public SQLiteDatabase db = null;
	String TAG="DbHelper";
	
	public EDbHelper(Object helper) {
		this.sqliteHelper = (SQLiteOpenHelper)helper;
	    db = this.sqliteHelper.getWritableDatabase();
	}
	
	public EDbHelper(Object helper,SQLiteDatabase db) {
		this.sqliteHelper = (SQLiteOpenHelper)helper;
		this.db = db;
	}
	
	public EDbHelper(Object helper,File f) {
		this.sqliteHelper = (SQLiteOpenHelper)helper;
		this.db = SQLiteDatabase.openDatabase(f.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public EDbHelper(File f) {
		if(f!=null && f.exists()){
			sqliteHelper = new SQLiteOpenHelper(EApplication.mContext,"",null,1) {
				
				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				}
				
				@Override
				public void onCreate(SQLiteDatabase db) {
				}
			};
			this.db = 
					SQLiteDatabase.openDatabase(
							f.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		}else {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.db = 
					SQLiteDatabase.openDatabase(
							f.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		}
	}
	
	
	/**
	 * 执行一条sql语句
	 * @param sql
	 */
	public void execSQL(String sql) {
		try {
			ELog.D(sql);
			db.execSQL(sql);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			db.close();
		}
	}
	
	/**
	 * 插入一条sql
	 * @param table
	 * @param values
	 */
	public void insert(String table,ContentValues values){
		try {
			db.insert(table, null, values);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (db.isOpen()) {
				db.close();
			}
		}
	}
	
	/**
	 * 更新一条sql语句
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 */
	public void update(String table,ContentValues values,String whereClause,String[] whereArgs ){
		try {
			db.update(table, values, whereClause, whereArgs);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			db.close();
		}
	}
	/**
	 * 删除一条记录
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 */
	public void delete(String table,String whereClause,String[] whereArgs ){
		try {
			db.delete(table, whereClause, whereArgs);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			db.close();
		}
	}
	/**
	 * 查询并返回结果cursor,此处不能关闭数据库,否则cursor无法遍历结果集
	 * @param sql
	 * @return
	 */
	public Cursor rawQuery(String sql){
		try {
			ELog.D(sql);
			return db.rawQuery(sql, null);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			//db.close();
		}
	}
	/**
	 * 查询并返回结果cursor,此处不能关闭数据库,否则cursor无法遍历结果集
	 * @param sql
	 * @return
	 */
	public Cursor rawQuery(String sql, String[] args){
		try {
			ELog.D(sql);
			return db.rawQuery(sql, args);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			//db.close();
		}
	}
	/**
	 * 查询并返回单个记录结果
	 * @param sql
	 * @return
	 */
	public double rawQuerySingle(String sql){
		ELog.D(sql);
		Cursor cursor = this.rawQuery(sql);
		double result = -1;
		if (cursor.moveToNext()){
			result = cursor.getDouble(0);
		}
		return result;
	}
	
	
	/**
	 * 执行多个sql语句,事务控制
	 * @param sqls
	 * @return
	 */
	public boolean execSQL(List<String> sqls){
		try {
		    db.beginTransaction();
			for(String sql:sqls){
				ELog.D(sql);
				db.execSQL(sql);
			}
			db.setTransactionSuccessful();
			return true;
		} catch (SQLException ex) {
			return false;
		} finally {
			db.endTransaction();
			db.close();
		}
	}
	
	
	
	/**
	 * 关闭数据库
	 */
	public void close(){
		if (null!=db&&db.isOpen()) {
			this.db.close();
		}
		if (null!=sqliteHelper) {
			this.sqliteHelper.close();
		}
	}
}
