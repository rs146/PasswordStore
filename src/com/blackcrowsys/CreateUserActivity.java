package com.blackcrowsys;

import net.sqlcipher.database.SQLiteDatabase;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUserActivity extends Activity{
	private String master;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setMaster(getIntent().getStringExtra("master"));
        SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase db = dbAdapter.getWritableDatabase(key);
		SQLiteDatabase dbRead = dbAdapter.getReadableDatabase(key);

        Cursor c = dbAdapter.getAllUsers(dbRead);
        if (c.getCount() > 0){
        	db.close();
        	dbRead.close();
        	Intent i = new Intent("com.blackcrowsys.PasswordStoreActivity");
        	i.putExtra("master", getMaster());
    		startActivity(i);
        }
    }
    
    public void onClick(View view){
    
    	SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase dbWrite = dbAdapter.getWritableDatabase(key);
		SQLiteDatabase dbRead = dbAdapter.getReadableDatabase(key);
    	
    	EditText uName = (EditText) findViewById(R.id.uname);
    	String userName = uName.getText().toString();
    	EditText pWord = (EditText) findViewById(R.id.pass);
    	String password = pWord.getText().toString();
    	EditText pWord2 = (EditText) findViewById(R.id.pass1);
    	String password2 = pWord2.getText().toString();
    	
    	if (password2.equals(password)){
    		
    			Cursor c = null;
            	boolean worked = true;
            	try {
        			c = dbAdapter.getUser(userName, dbRead);
        			if (c.getString(0).equals(userName)){
        	    		worked = false;	
        	    	}
        		} catch (Exception e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
             	if (worked){
            		try {
        				dbAdapter.insertUser(userName, password, dbRead);
        				dbRead.close();
        				dbWrite.close();
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
            		
            		Toast.makeText(getBaseContext(), "Thank you. Please login with this combination.", Toast.LENGTH_LONG).show();
            		Intent i = new Intent("com.blackcrowsys.PasswordStoreActivity");
            		i.putExtra("master", getMaster());
            		startActivity(i);
            	} else {
            		Toast.makeText(getBaseContext(), "This username already exists. Please add another one.", Toast.LENGTH_LONG).show();
            	}
    	} else {
    		Toast.makeText(getBaseContext(), "The passwords you entered do not match", Toast.LENGTH_LONG).show();
    	}
    }

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}
	
	public void onPause(){
		super.onPause();
		finish();
	}
	
	public void onDestroy(){
		super.onDestroy();
	}

}
