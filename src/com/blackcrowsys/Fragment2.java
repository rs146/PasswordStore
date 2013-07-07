package com.blackcrowsys;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment2 extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
		return inflater.inflate(R.layout.fragment2, container, false);
	}

}