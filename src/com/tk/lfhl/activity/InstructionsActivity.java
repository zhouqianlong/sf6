package com.tk.lfhl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class InstructionsActivity extends Activity {
	private WebView wv_main;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instruction_activity);
		wv_main = (WebView) findViewById(R.id.wv_main);
		wv_main.loadUrl("file:///mnt/sdcard/1.html");
	}

}
