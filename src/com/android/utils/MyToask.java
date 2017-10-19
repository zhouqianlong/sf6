package com.android.utils;

import android.content.Context;
import android.widget.Toast;

public class MyToask {

	private static Toast toast = null;

	public static void showTextToast(String msg,Context context,int time) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, time);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
}
