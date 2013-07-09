package com.blackcrowsys;

import net.sqlcipher.database.SQLiteDatabase;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddUserActivity extends Activity {
	private String master;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.adduser);
		setMaster(getIntent().getStringExtra("master"));
	}

	public void onClick(View view) {
		SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase db = dbAdapter.getWritableDatabase(key);
		SQLiteDatabase dbRead = dbAdapter.getReadableDatabase(key);

		EditText uName = (EditText) findViewById(R.id.username);
		String userName = uName.getText().toString();
		EditText pWord = (EditText) findViewById(R.id.password);
		String password = pWord.getText().toString();
		EditText pWord2 = (EditText) findViewById(R.id.password1);
		String password2 = pWord2.getText().toString();

		if (password2.equals(password)) {
			if (!password2.isEmpty()) {
				Cursor c = null;
				boolean worked = true;
				try {
					c = dbAdapter.getUser(userName, dbRead);
					if (c.getString(0).equals(userName)) {
						worked = false;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (worked) {
					try {
						dbAdapter.insertUser(userName, password, db);
						db.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Toast.makeText(
							getBaseContext(),
							"Thank you. Another user account has been created.",
							Toast.LENGTH_LONG).show();
					Intent i = new Intent(
							"com.blackcrowsys.DisplayTableActivity");
					startActivity(i);
				} else {
					Toast.makeText(
							getBaseContext(),
							"This username already exists. Please add another one.",
							Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(getBaseContext(),
						"Please enter a Password value", Toast.LENGTH_LONG)
						.show();
			}
		} else {
			Toast.makeText(getBaseContext(),
					"The passwords you entered do not match", Toast.LENGTH_LONG)
					.show();
		}
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}
}
