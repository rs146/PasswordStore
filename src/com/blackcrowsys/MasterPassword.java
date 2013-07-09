package com.blackcrowsys;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MasterPassword extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setmaster);
		java.io.File file = new java.io.File(
				"/data/data/com.blackcrowsys/files", "textFile.txt");
		if (file.exists()) {
			Intent i = new Intent("com.blackcrowsys.Login");
			startActivity(i);
		}

	}

	public void onClick(View view) {
		EditText masterPass = (EditText) findViewById(R.id.masterpass);
		EditText masterConfirm = (EditText) findViewById(R.id.masterpass1);
		String master = masterPass.getText().toString();
		String confirm = masterConfirm.getText().toString();

		if (master.equals(confirm)) {
			if (!master.isEmpty()) {
				if (master.length() > 7) {
					try {
						FileOutputStream fOut = openFileOutput("textFile.txt",
								MODE_WORLD_READABLE);
						OutputStreamWriter out = new OutputStreamWriter(fOut);
						String key = AeSimpleSHA1.SHA1(master);
						out.write(key);
						out.flush();
						out.close();
						Toast.makeText(
								getBaseContext(),
								"Thank you. Please use the master password to unlock the app the next time.",
								Toast.LENGTH_LONG).show();
						Intent i = new Intent(
								"com.blackcrowsys.CreateUserActivity");
						i.putExtra("master", master);
						startActivity(i);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Toast.makeText(
							getBaseContext(),
							"Please make sure that the master password is at least 8 characters.",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						getBaseContext(),
						"Please enter a master password value. This is critcal.",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getBaseContext(),
					"Please make sure that both passwords match.",
					Toast.LENGTH_LONG).show();
		}
	}

	public void onPause() {
		super.onPause();
		finish();
	}

	public void onDestroy() {
		super.onDestroy();
	}
}
