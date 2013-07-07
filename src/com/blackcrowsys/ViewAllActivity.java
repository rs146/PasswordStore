package com.blackcrowsys;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;

public class ViewAllActivity extends Activity {
	private Integer idToBeSearched;
	private List<String> siteNames;
	private String username;
	private String master;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewall);
		setUsername(getIntent().getStringExtra("username"));
		setMaster(getIntent().getStringExtra("master"));
		
		// Database key
		SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase dbWrite = dbAdapter.getWritableDatabase(key);
		SQLiteDatabase dbRead = dbAdapter.getReadableDatabase(key);
		
		// get all entries
		Cursor c = dbAdapter.getSiteNames(getUsername(), dbRead);
		siteNames = new ArrayList<String>();
		
		if (c.moveToFirst()){
			do {
				SiteView sv = new SiteView(c.getInt(0), c.getString(1));
				siteNames.add(sv.toString());
			} while (c.moveToNext());
		}
		
		dbRead.close();
		// set the spinner to the list of site titles
		Spinner s1 = (Spinner) findViewById(R.id.spinner1);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, siteNames);
		s1.setAdapter(adapter);
		s1.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3){
				int index = arg0.getSelectedItemPosition();
				String siteid = siteNames.get(index);
				int ind = siteid.indexOf(" -");
				setId(Integer.parseInt(siteid.substring(0, ind)));
				Toast.makeText(getBaseContext(), "You have selected entry " + getId() + ": " + siteNames.get(index).substring(ind + 2, siteid.length()), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(getBaseContext(), "Please enter a sitename.", Toast.LENGTH_SHORT).show();
				
			}
		});
	}
	
	/**
	 * 
	 * Method is called when View Or Edit Button is Pressed.
	 * id to be searched, username, master sent to next activity.
	 * @param View view
	 */
	public void viewRow(View view){
		Intent i = new Intent("com.blackcrowsys.ViewRecordActivity");
		i.putExtra("username", getUsername());
		i.putExtra("master", getMaster());
		i.putExtra("id", getId());
		startActivity(i);
	}
	
	/**
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public Integer getId() {
		return idToBeSearched;
	}
	public void setId(Integer idToBeSearched) {
		this.idToBeSearched = idToBeSearched;
	}
}
