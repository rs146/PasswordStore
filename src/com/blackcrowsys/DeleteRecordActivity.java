package com.blackcrowsys;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteRecordActivity extends Activity {
	private String master;
	private String username;
	private List<String> siteNames;
	private Integer id;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deleterecord);
		setUsername(getIntent().getStringExtra("username"));
		setMaster(getIntent().getStringExtra("master"));

		// Database key
		SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase dbRead = dbAdapter.getReadableDatabase(key);

		// get all entries
		Cursor c = dbAdapter.getSitesForDeletion(getUsername(), dbRead);
		siteNames = new ArrayList<String>();

		if (c.moveToFirst()) {
			do {
				SiteView sv = new SiteView(c.getInt(0), c.getString(1));
				siteNames.add(sv.toString());
			} while (c.moveToNext());
		}

		dbRead.close();
		c.close();
		// set the spinner to the list of site titles
		Spinner s1 = (Spinner) findViewById(R.id.spinner2);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, siteNames);
		s1.setAdapter(adapter);
		s1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int index = arg0.getSelectedItemPosition();
				String siteid = siteNames.get(index);
				int ind = siteid.indexOf(" -");
				setId(Integer.parseInt(siteid.substring(0, ind)));
				Toast.makeText(
						getBaseContext(),
						"You have selected entry "
								+ getId()
								+ ": "
								+ siteNames.get(index).substring(ind + 2,
										siteid.length()), Toast.LENGTH_SHORT)
						.show();
				setFields();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public void setFields() {
		// Database key
		SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase dbRead = dbAdapter.getReadableDatabase(key);

		Cursor c = dbAdapter.getSite(getId(), dbRead);
		dbRead.close();

		String myComments = c.getString(4);
		TextView tx = (TextView) findViewById(R.id.displaycomments);
		tx.setText(myComments);
		c.close();
	}

	/**
	 * 
	 * @param view
	 */
	public void delete(View view) {
		// Database key
		SQLiteDatabase.loadLibs(this);
		String key = getMaster();
		DBAdapter dbAdapter = new DBAdapter(this);
		SQLiteDatabase dbRead = dbAdapter.getReadableDatabase(key);

		boolean success = dbAdapter.deleteSite(getId(), dbRead);

		if (success) {
			Toast.makeText(getBaseContext(),
					"The selected entry has been deleted", Toast.LENGTH_SHORT)
					.show();
			finish();
		} else {
			Toast.makeText(
					getBaseContext(),
					"Unfortunately the selected entry has not been deleted. Please try again.",
					Toast.LENGTH_LONG).show();
		}
		dbRead.close();
	}

	/**
	 * @return the master
	 */
	public String getMaster() {
		return master;
	}

	/**
	 * @param master
	 *            the master to set
	 */
	public void setMaster(String master) {
		this.master = master;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * Decodes a Byte64 encoded String.
	 * 
	 * @param word
	 * @return String decoded.
	 */
	public String decode(String word) {
		byte[] data1 = Base64.decode(word, Base64.DEFAULT);
		String decodedWord = null;
		try {
			decodedWord = new String(data1, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decodedWord;
	}

	public void onDestroy() {
		super.onDestroy();
	}

}
