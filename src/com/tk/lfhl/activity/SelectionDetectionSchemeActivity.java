package com.tk.lfhl.activity;

import java.util.List;

import com.android.db.DBHelper;
import com.android.utils.MyToask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 选择检测方案
 * @author Administrator
 *continuousnegativepressure
 *continuouspositivepressure.png
 */
public class SelectionDetectionSchemeActivity extends Activity implements OnClickListener{
	
	private ImageView iv_photo;
	private Button btn_ok_and_back,back;
	private int iv_photo_id;
	private String btn_content;
	private DBHelper db;
	private int type_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection_detection_scheme);
		db = new DBHelper(getApplicationContext());
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		btn_ok_and_back = (Button) findViewById(R.id.btn_ok_and_back);
		back = (Button) findViewById(R.id.back);
		btn_ok_and_back.setOnClickListener(this);
		back.setOnClickListener(this);
		iv_photo_id = (Integer) getIntent().getExtras().get("bg_type");
		btn_content = (String) getIntent().getExtras().get("btn_content");
		type_id = (Integer) getIntent().getExtras().get("type_id");
		iv_photo.setImageResource(iv_photo_id);
		btn_ok_and_back.setText("确定选择"+btn_content+"并且进入检测");
		
	}
	@Override
	public void onClick(View v) {
		if(v==btn_ok_and_back){
			 db.updateLAST_TABLE(MainActivity.last_id.get(0), MainActivity.last_id.get(1), btn_content);
			 startActivity(new Intent(getApplicationContext(), AirLeakageTestActivity.class));
		}
		if(v==back ){
			MyToask.showTextToast("请重新选择方案",getApplicationContext() , 0);
			finish();
		}
	}

}
