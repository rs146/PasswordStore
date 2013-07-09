package com.blackcrowsys;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DisplayTableActivity extends Activity {
	private String username;
	private String master;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displaytable);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setUsername(getIntent().getStringExtra("username"));
		setMaster(getIntent().getStringExtra("master"));
		Button addUserButton = (Button) findViewById(R.id.addUserButton);
		addUserButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Toast.makeText(
							getBaseContext(),
							"Click to add new user accounts for this app with their own username-password combination",
							Toast.LENGTH_SHORT).show();
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// finger was lifted
				}
				return false;
			}
		});

		Button changePassButton = (Button) findViewById(R.id.changeMyPassButton);
		changePassButton.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Toast.makeText(
							getBaseContext(),
							"Click to change the login password for your account",
							Toast.LENGTH_SHORT).show();
				}
				return false;
			}
		});
	}

	public void addUser(View view) {
		Intent i = new Intent("com.blackcrowsys.AddUserActivity");
		i.putExtra("master", getMaster());
		startActivity(i);
	}

	public void addEntry(View view) {
		Intent i = new Intent("com.blackcrowsys.AddSiteActivity");
		i.putExtra("username", getUsername());
		i.putExtra("master", getMaster());
		startActivity(i);
	}

	public void viewAll(View view) {
		Intent i = new Intent("com.blackcrowsys.ViewAllActivity");
		i.putExtra("username", getUsername());
		i.putExtra("master", getMaster());
		startActivity(i);
	}

	public void logout(View view) {
		Toast.makeText(getBaseContext(), "You have logged out.",
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(getApplicationContext(), Login.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void deleteRecord(View view) {
		Intent i = new Intent("com.blackcrowsys.DeleteRecordActivity");
		i.putExtra("username", this.getUsername());
		i.putExtra("master", getMaster());
		startActivity(i);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void changePassword(View view) {
		Intent i = new Intent("com.blackcrowsys.ChangePasswordActivity");
		i.putExtra("username", this.getUsername());
		i.putExtra("master", getMaster());
		startActivity(i);
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public void onDestroy() {
		super.onDestroy();
	}

}
