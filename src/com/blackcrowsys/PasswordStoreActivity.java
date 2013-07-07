package com.blackcrowsys;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import net.sqlcipher.database.SQLiteDatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordStoreActivity extends Activity {
	private String master;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setMaster(getIntent().getStringExtra("master"));
    }

	public void onClick(View view) {
		SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase dbWrite = dbAdapter.getWritableDatabase(key);
		SQLiteDatabase dbRead = dbAdapter.getReadableDatabase(key);
		EditText uName = (EditText) findViewById(R.id.userName);
		String userName = uName.getText().toString();
		EditText pWord = (EditText) findViewById(R.id.password);
		String password = pWord.getText().toString();
		String encryptedPass = null;
		try {
			encryptedPass = AeSimpleSHA1.SHA1(password);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		Cursor c = null;
		try {
			c = dbAdapter.getUser(userName, dbRead);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (c.getCount() > 0){
			if (userName.equals(c.getString(0)) && encryptedPass.equals(c.getString(1))){
				Intent i = new Intent("com.blackcrowsys.DisplayTableActivity");
				i.putExtra("username", userName);
				i.putExtra("master", getMaster());
				startActivity(i);
			} else {
				Toast.makeText(getBaseContext(), "The username & password are incorrect. Please try again.", Toast.LENGTH_LONG).show();
			}	
		} else {
			Toast.makeText(getBaseContext(), "The username & password are incorrect. Please try again.", Toast.LENGTH_LONG).show();
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