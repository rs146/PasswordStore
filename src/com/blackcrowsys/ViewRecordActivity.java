package com.blackcrowsys;

import java.io.UnsupportedEncodingException;

import net.sqlcipher.database.SQLiteDatabase;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ViewRecordActivity extends Activity{
	static int count = 1;
	private String username;
	private String master;
	private long id;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewrecord);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setUsername(getIntent().getStringExtra("username"));
		setMaster(getIntent().getStringExtra("master"));
		setId((long) getIntent().getIntExtra("id", 0));
		
		if (getId() != 0){
			// set up database
			SQLiteDatabase.loadLibs(this);
			String key = getMaster();
			DBAdapter dbAdapter = new DBAdapter(this);
			SQLiteDatabase dbWrite = dbAdapter.getWritableDatabase(key);
			
			// get site record
			Cursor c = dbAdapter.getSite(getId(), dbWrite);
			
			
			// close db
			dbWrite.close();
			
			// set all edit texts to the Cursor values.
			EditText title = (EditText) findViewById(R.id.viewsitename);
			title.setText(c.getString(0));
			EditText URL = (EditText) findViewById(R.id.viewURL);
			URL.setText(c.getString(1));
			EditText userName = (EditText) findViewById(R.id.viewusername);
			String uName = decode(c.getString(2));
			userName.setText(uName);
			String revPass = this.decode(c.getString(3));
			EditText password = (EditText) findViewById(R.id.viewsitePass);
			password.setTransformationMethod(new PasswordTransformationMethod());
			password.setText(revPass);
			EditText myComments = (EditText) findViewById(R.id.viewmyComments);
			myComments.setText(c.getString(4));
			c.close();
		} else {
			Toast.makeText(getBaseContext(), "There was a problem with the id number of this entry.", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void showhide(View view){
		EditText passText = (EditText) findViewById(R.id.viewsitePass);
		if ((ViewRecordActivity.count % 2) == 1){
			passText.setTransformationMethod(null);
		} else {
			passText.setTransformationMethod(new PasswordTransformationMethod());
		}
		ViewRecordActivity.count++;
	}
	
	/**
	 * Method called when user clicks 'Save Changes' button.
	 * Saves the changes made by a user to the entry using
	 * the id as the where clause.
	 */
	public void save(View view){
		// set up database
		SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase dbWrite = dbAdapter.getWritableDatabase(key);
		
		EditText etitle = (EditText) findViewById(R.id.viewsitename);
		EditText eURL = (EditText) findViewById(R.id.viewURL);
		EditText euserName = (EditText) findViewById(R.id.viewusername);
		EditText epassword = (EditText) findViewById(R.id.viewsitePass);
		EditText emyComments = (EditText) findViewById(R.id.viewmyComments);
		
		String title = getString(etitle);
		String URL = getString(eURL);
		String userName = getString(euserName);
		String password = getString(epassword);
		String myComments = getString(emyComments);
		
		boolean success = dbAdapter.updateSite(getId(), title, userName, password, getUsername(), URL, myComments, dbWrite);
		
		if (success){
			Toast.makeText(getBaseContext(), "The entry has been updated.", Toast.LENGTH_LONG).show();		
			Intent i = new Intent("com.blackcrowsys.DisplayTableActivity");
			i.putExtra("username", getUsername());
			i.putExtra("master", getMaster());
			startActivity(i);
		} else {
			Toast.makeText(getBaseContext(), "The entry was not updated. Please try again.", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * @param EditText
	 * @return String in the EditText
	 * Returns the String value of the text contained in an EditText widget.
	 */
	public String getString(EditText tx){
		return tx.getText().toString();
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the master
	 */
	public String getMaster() {
		return master;
	}
	/**
	 * @param master the master to set
	 */
	public void setMaster(String master) {
		this.master = master;
	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	public void onDestroy(){
		super.onDestroy();
		ViewRecordActivity.count = 1;
	}
	
	/**
	 * 
	 * Decodes a Byte64 encoded String. 
	 * @param word
	 * @return String decoded.
	 */
	public String decode(String word){
		byte[] data1 = Base64.decode(word, Base64.DEFAULT);
	    String decodedWord = null;
	    try {
	        decodedWord = new String(data1, "UTF-8");
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    return decodedWord;
	}

}
