package com.blackcrowsys;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.masterlogin);
	}
	
	public void onClick(View view){
		FileInputStream fIn = null;
		try {
			fIn = openFileInput("textFile.txt");
	        InputStreamReader isr = new InputStreamReader(fIn);
	        char[] inputBuffer = new char[10000];
	        String key = "";
	        int charRead;
	        while ((charRead = isr.read(inputBuffer))>0){
	        	String readString = String.copyValueOf(inputBuffer, 0, charRead);
	        	
	        	key += readString;
	        	inputBuffer = new char[10000];
	        }
	        
	        EditText tx = (EditText) findViewById(R.id.key);
	        String master = tx.getText().toString();
	        String keyInput = AeSimpleSHA1.SHA1(master);
	        
	        if (key.equals(keyInput)){
	        	Toast.makeText(getBaseContext(), "Thank you. Please login with your User Account.", Toast.LENGTH_LONG).show();
	        	Intent i = new Intent("com.blackcrowsys.PasswordStoreActivity");
	        	i.putExtra("master", master);
        		startActivity(i);
	        } else {
	        	Toast.makeText(getBaseContext(), "The master password you entered is incorrect. Please try again.", Toast.LENGTH_SHORT).show();
	        }
		}catch (IOException ioe){
			ioe.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void onPause(){
		super.onPause();
		finish();
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
}
