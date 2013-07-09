package com.blackcrowsys;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import net.sqlcipher.database.SQLiteDatabase;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity {
	private String username;
	private String master;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepassword);
		setUsername(getIntent().getStringExtra("username"));
		setMaster(getIntent().getStringExtra("master"));

	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void onClick(View view) {
		SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase db = dbAdapter.getWritableDatabase(key);

		EditText currentPass = (EditText) findViewById(R.id.currentpass);
		String oldPassword = currentPass.getText().toString();
		String encryptedPassword = encryptPassword(oldPassword);
		boolean passwordCheck = checkOldPassword(encryptedPassword);

		EditText pWord = (EditText) findViewById(R.id.newpassword);
		String password = pWord.getText().toString();
		EditText pWord2 = (EditText) findViewById(R.id.newpassword1);
		String password2 = pWord2.getText().toString();

		if (passwordCheck) {
			if (password.equals(password2)) {
				if (!password.isEmpty()) {
					String encryptedPass = this.encryptPassword(password);
					dbAdapter.updateUser(this.getUsername(), encryptedPass, db);
					db.close();
					Toast.makeText(getBaseContext(),
							"Thank you. Your Password has been changed",
							Toast.LENGTH_LONG).show();
					Intent i = new Intent(
							"com.blackcrowsys.DisplayTableActivity");
					startActivity(i);
				} else {
					Toast.makeText(getBaseContext(),
							"Please enter a Password value", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(getBaseContext(),
						"The New Passwords do not match", Toast.LENGTH_LONG)
						.show();
			}

		} else {
			Toast.makeText(getBaseContext(),
					"The Current Password has been entered incorrectly",
					Toast.LENGTH_LONG).show();
		}
	}

	public String encryptPassword(String value) {
		String encryptedPass = null;
		try {
			encryptedPass = AeSimpleSHA1.SHA1(value);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return encryptedPass;
	}

	public boolean checkOldPassword(String encryptedPass) {
		SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase dbRead = dbAdapter.getReadableDatabase(key);

		Cursor c = null;
		try {
			c = dbAdapter.getUser(this.getUsername(), dbRead);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.getUsername().equals(c.getString(0))
				&& encryptedPass.equals(c.getString(1))) {
			return true;
		} else {
			return false;
		}
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}
}
