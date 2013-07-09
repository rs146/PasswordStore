package com.blackcrowsys;

import net.sqlcipher.database.SQLiteDatabase;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddSiteActivity extends Activity {
	private String username;
	private String master;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addsite);
		setMaster(getIntent().getStringExtra("master"));
		setUsername(getIntent().getStringExtra("username"));
	}

	public void onClick(View view) {
		String password1 = this
				.getStringFromEditText((EditText) findViewById(R.id.sitePass));
		String password2 = this
				.getStringFromEditText((EditText) findViewById(R.id.sitePass1));
		String title = this
				.getStringFromEditText((EditText) findViewById(R.id.sitename));
		String URL = this
				.getStringFromEditText((EditText) findViewById(R.id.URL));
		String username = this
				.getStringFromEditText((EditText) findViewById(R.id.usernameAdd));
		String comments = this
				.getStringFromEditText((EditText) findViewById(R.id.myComments));

		if (!title.isEmpty()) {
			if (password1.equals(password2)) {
				SQLiteDatabase.loadLibs(this);
				String key = getMaster();
				DBAdapter dbAdapter = new DBAdapter(this);
				SQLiteDatabase dbWrite = dbAdapter.getWritableDatabase(key);
				SQLiteDatabase dbRead = dbAdapter.getReadableDatabase(key);

				dbAdapter.insertSite(title, username, password1, getUsername(),
						URL, comments, dbWrite);
				dbRead.close();
				dbWrite.close();

				Toast.makeText(getBaseContext(), "Your entry has been saved",
						Toast.LENGTH_SHORT).show();
				Intent i = new Intent("com.blackcrowsys.DisplayTableActivity");
				i.putExtra("username", getUsername());
				i.putExtra("master", getMaster());
				startActivity(i);
			} else {
				Toast.makeText(getBaseContext(), "The passwords do not match",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getBaseContext(),
					"Please enter a title for this entry", Toast.LENGTH_SHORT)
					.show();
		}

	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStringFromEditText(EditText tx) {
		return tx.getText().toString();
	}
}
