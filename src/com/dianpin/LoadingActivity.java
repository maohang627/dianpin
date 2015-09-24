package com.dianpin;


import android.app.Activity;

import android.os.Bundle;

import com.thestore.microstore.R;



/**
 * 启动界面
 * 
 * 
 * 
 */
public class LoadingActivity extends Activity{
	private static final String TAG = "LoadingActivity";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_activity);
		
	}	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	

}
