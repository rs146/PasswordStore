package com.blackcrowsys;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Base64;
import android.util.Log;

/** Helper to the database, manages versions and creation */
public class DBAdapter extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "Test";
	private static final int DATABASE_VERSION = 1;

	// Table name
	public static final String TABLE_1 = "User";
	public static final String TABLE_2 = "Sites";
	
	// Column names for Sites table
	static final String KEY_SITENAME = "sitename";
	static final String KEY_USERNAME = "username";
	static final String KEY_PASSWORD = "password";
	static final String KEY_USER = "user";
	static final String KEY_ID = "_id";
	static final String KEY_URL = "URL";
	static final String KEY_COMMENTS = "comments";
	
	// Column names for User table
	static final String KEY_UNAME = "uname";
	static final String KEY_PWORD = "pword";
	
	


	public DBAdapter(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql1 = "create table User (uname text primary key, pword text not null);";
		String sql2 = "create table Sites (_id integer primary key autoincrement, sitename text not null, URL text not null, username text not null, password text not null, comments text, user text not null, FOREIGN KEY(user) REFERENCES User(username));";
		db.execSQL(sql1);
		db.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion)
			return;

		String sql = null;
		if (oldVersion == 1) 
			sql = "alter table " + TABLE_1 + " add note text;";
		if (oldVersion == 2)
			sql = "";

		Log.d("EventsData", "onUpgrade	: " + sql);
		if (sql != null)
			db.execSQL(sql);
	}
	
	public long insertSite(String sitename, String username, String password, String user, String URL, String comments, SQLiteDatabase db){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_SITENAME, sitename);
		byte[] data = null;
		try {
			data = username.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String base64username = Base64.encodeToString(data, Base64.DEFAULT);
		
		initialValues.put(KEY_USERNAME, base64username);
		byte[] data1 = null;
		try {
			data1 = password.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String base64pass = Base64.encodeToString(data1, Base64.DEFAULT);
		initialValues.put(KEY_PASSWORD, base64pass);
		initialValues.put(KEY_USER, user);
		initialValues.put(KEY_URL, URL);
		initialValues.put(KEY_COMMENTS, comments);
		return db.insert(TABLE_2, null, initialValues);
	}
	
	public Cursor getSite(long id, SQLiteDatabase db){
		Cursor c = db.query(TABLE_2, new String[] {KEY_SITENAME, KEY_URL, KEY_USERNAME, KEY_PASSWORD, KEY_COMMENTS}, KEY_ID + "=" + id, null, null, null, null);
		if (c != null){
			c.moveToFirst();
		}
		return c;
	}
	
	public Cursor getAllSites(String username, SQLiteDatabase db){
		return db.query(TABLE_2, new String[] {KEY_SITENAME, KEY_URL, KEY_USERNAME, KEY_PASSWORD, KEY_COMMENTS, KEY_USER}, KEY_USER + "=" + "?", new String[]{username}, null, null, KEY_USERNAME);
	}
	
	public Cursor getSiteNames(String username, SQLiteDatabase db){
		return db.query(TABLE_2, new String[]{KEY_ID, KEY_SITENAME}, KEY_USER + "=" + "?", new String[]{username}, null, null, KEY_ID);
	}
	
	public Cursor getSitesForDeletion(String username, SQLiteDatabase db){
		return db.query(TABLE_2, new String[]{KEY_ID, KEY_SITENAME, KEY_USERNAME, KEY_PASSWORD}, KEY_USER + "=" + "?", new String[]{username}, null, null, KEY_ID);
	}
	
	public boolean updateSite(long id, String sitename, String username, String password, String user, String URL, String comments, SQLiteDatabase db){
		ContentValues args = new ContentValues();
		args.put(KEY_SITENAME, sitename);
		byte[] data = null;
		try {
			data = username.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String base64username = Base64.encodeToString(data, Base64.DEFAULT);
		args.put(KEY_USERNAME, base64username);
		byte[] data1 = null;
		try {
			data1 = password.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String base64pass = Base64.encodeToString(data1, Base64.DEFAULT);
		args.put(KEY_PASSWORD, base64pass);
		args.put(KEY_USER, user);
		args.put(KEY_URL, URL);
		args.put(KEY_COMMENTS, comments);
		return db.update(TABLE_2, args, KEY_ID + "=" + id, null) > 0;
	}
	
	public boolean deleteSite(long id, SQLiteDatabase db){
		return db.delete(TABLE_2, KEY_ID + "=" + id, null) > 0;
	}
	
	public long insertUser(String uname, String pword, SQLiteDatabase db) throws Exception{ 
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_UNAME, uname);
		String encryptedPass = null;
		try {
			encryptedPass = AeSimpleSHA1.SHA1(pword);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialValues.put(KEY_PWORD, encryptedPass);
		return db.insert(TABLE_1, null, initialValues);
	}
	
	public boolean deleteUser(String userName, SQLiteDatabase db){
		return db.delete(TABLE_1, KEY_UNAME + "=" + userName, null) > 0;
	}
	
	public Cursor getAllUsers(SQLiteDatabase db){
		return db.query(TABLE_1, new String[] {KEY_UNAME, KEY_PWORD}, null, null, null, null, KEY_UNAME);
	}
	
	public Cursor getUser(String userName, SQLiteDatabase db) throws SQLException{
		Cursor mCursor = db.query(true, TABLE_1, new String[] {KEY_UNAME, KEY_PWORD}, KEY_UNAME + "=" + "'" + userName + "'", null, null, null, null, null);
		if (mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public boolean updateUser(String username, String password, SQLiteDatabase db){
		ContentValues args = new ContentValues();
		args.put(KEY_UNAME, username);
		args.put(KEY_PWORD, password);
		return db.update(TABLE_1, args, KEY_UNAME + "=" + "'" + username + "'", null) > 0;
	}
}