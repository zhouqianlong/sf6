package com.tk.lfhl.activity;

import java.util.Calendar;

import com.android.db.DBHelper;
import com.ramy.minervue.service.GasDetector;
import com.tk.lfhl.bean.SettingBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener{ 
	public static final int SERIAZABLE = 0;//handler  ��ȡ���к�
	private DBHelper dbHelper;
	TextView tv_biaoding,tv_version,tv_about,tv_address,tv_data,tv_timesett,tv_chancun;
	String version;
	SettingBean sttb;
	private int mYear; 
	private int mMonth; 
	private int mDay;  
	String TAG ="SF6";
	private GasDetector detector =  GasDetector.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		boolean flag = getIntent().getExtras().getBoolean("flag");
		dbHelper = new DBHelper(getApplicationContext());
		 sttb = dbHelper.findSETTING();
		 LinearLayout ll_flag = (LinearLayout) findViewById(R.id.ll_flag);
		 if(flag){
			 ll_flag.setVisibility(View.VISIBLE);
		 }else{
			 ll_flag.setVisibility(View.GONE);
		 }
		tv_biaoding = (TextView) findViewById(R.id.tv_biaoding);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_about = (TextView) findViewById(R.id.tv_about);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_data = (TextView) findViewById(R.id.tv_data);
		tv_timesett = (TextView) findViewById(R.id.tv_timesett);//����ʱ��
		tv_chancun = (TextView) findViewById(R.id.tv_chancun);//�д�����
		tv_address.setOnClickListener(this);
		tv_biaoding.setOnClickListener(this);
		tv_version.setOnClickListener(this);
		tv_about.setOnClickListener(this);
		tv_data.setOnClickListener(this);
		tv_timesett.setOnClickListener(this);
		tv_chancun.setOnClickListener(this);
		if(!sttb.getBUY_TIME().equals("0")&&sttb.getBUY_TIME().indexOf("����")!=0){
			tv_data.setText(tv_data.getText().toString()+""+sttb.getBUY_TIME());
		}
		final Calendar c = Calendar.getInstance();       
		mYear = c.get(Calendar.YEAR);     
		mMonth = c.get(Calendar.MONTH);      
		mDay = c.get(Calendar.DAY_OF_MONTH);      
		try {
			PackageManager manager = getPackageManager();
			version = manager.getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tv_version.setText(tv_version.getText().toString()+"v"+version);
				
		
	}
	@Override
	public void onClick(View v) {
		if(tv_biaoding==v){
			startActivity(new Intent(getApplicationContext(), SensorManagerActivity.class));
		}
		if(tv_version== v){
			
			Toast.makeText(getApplicationContext(), "��ǰ�汾:v"+version, 0).show();
		}
		if(tv_about==v){
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(detector.open()){
						GasDetector.CMD_SEND_TYPE = GasDetector.GAS_SERIALIZABLE;
						if (!detector.doTurnOnDevice(GasDetector.READ_GAS_SER)) {
							Log.e(TAG, "Error turning on GasDetector.");
							detector.close();
						}else{ 
							String seriazible = detector.getlastReplyVersionString();
							Message msg =Message.obtain();
							msg.what=SERIAZABLE;
							msg.obj = seriazible;
							mHandler.sendMessage(msg);
							Log.e(TAG, "ͨѶ�ɹ�");
						}
					}else{
						detector.close();
						Log.e(TAG, "�򿪴���ʧ��.");
					}
				}
			}).start();
			
//			Toast.makeText(getApplicationContext(), "��Ȩ����:ú�Ƽ��������о�Ժ���޹�˾\n"+"����ʱ�䣺"+sttb.getBUY_TIME()+"\n ��������ַ��"+sttb.getADDRESS(), 0).show();
		}
		
		if(tv_address==v){
			final EditText inputServer = new EditText(this);
			int inputType = InputType.TYPE_CLASS_NUMBER ;
			inputServer.setRawInputType(inputType);
		    if(sttb.getADDRESS()!=null){
		    	inputServer.setText(sttb.getADDRESS());
		    }
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("��������ַ").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
	                .setNegativeButton("ȡ��", null);
	        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	            	sttb.setADDRESS(inputServer.getText().toString());
	            	dbHelper.updateSETTING(sttb);
	               Toast.makeText(getApplicationContext(),inputServer.getText().toString(), 0).show();
	             }
	            
	        });
	        builder.show();
		}
		if(v==tv_timesett){

			
		    final EditText inputServer = new EditText(this);
			int inputType = InputType.TYPE_CLASS_NUMBER ;
			inputServer.setRawInputType(inputType);
		    	inputServer.setText(getSharedPreferences("sf6001", 0).getInt("time",5)+"");
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("����ʱ��(����)").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
	                .setNegativeButton("ȡ��", null);
	        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
//	            	sttb.setADDRESS(inputServer.getText().toString());
//	            	dbHelper.updateSETTING(sttb);
	            	SharedPreferences sharedPreferences = getSharedPreferences("sf6001", 0);
	            	try {
						int time = Integer.valueOf(inputServer.getText().toString());
						if(time<0||time>15){
							Toast.makeText(getApplicationContext(),"����ĸ�ʽ����,������1-15֮�������!", 0).show();
						}else{
							sharedPreferences.edit().putInt("time", time).commit();
							Toast.makeText(getApplicationContext(),"���óɹ�,����ʱ���䣺"+inputServer.getText().toString()+"!", 0).show();
						}
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),"����ĸ�ʽ����,������1-15֮�����������!", 0).show();	
					}
	            	
	             }
	            
	        });
	        builder.show();
		
		}
		if(v==tv_chancun){
			
			
			final EditText inputServer = new EditText(this);
			int inputType = InputType.TYPE_CLASS_NUMBER ;
			inputServer.setRawInputType(inputType);
			inputServer.setText(getSharedPreferences("sf6001", 0).getInt("chancun",1)+"");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("�д�����(PPM)").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
			.setNegativeButton("ȡ��", null);
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences sharedPreferences = getSharedPreferences("sf6001", 0);
					try {
						int time = Integer.valueOf(inputServer.getText().toString());
						if(time<0||time>1000){
							Toast.makeText(getApplicationContext(),"����ĸ�ʽ����,������1-1000֮�������!", 0).show();
						}else{
							sharedPreferences.edit().putInt("chancun", time).commit();
							Toast.makeText(getApplicationContext(),"���óɹ�,�д�������"+inputServer.getText().toString()+"ppm!", 0).show();
						}
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),"����ĸ�ʽ����,������1-1000֮�����������!", 0).show();	
					}
					
				}
				
			});
			builder.show();
			
		}
		if(tv_data==v){
			showDialog(1);
		}
		
	}
	
	

	@Override  
	protected Dialog onCreateDialog(int id) {   
		switch (id) {   
		case 1:
			return new DatePickerDialog(this, mDateBuyListener, mYear, mMonth,mDay);
		} 
		return null;   
	}   

	@Override  
	protected void onPrepareDialog(int id, Dialog dialog) {   
		switch (id) {   
		case 1:   
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);   
			break; 
		} 
	}  
	private DatePickerDialog.OnDateSetListener mDateBuyListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateTimeDisplay(tv_data);
		}
	};
	protected void updateTimeDisplay(TextView tv) {
		tv.setText(new StringBuilder().append(mYear).append("��") 
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("��") 
				.append((mDay < 10) ? "0" + mDay : mDay).append("��"));  
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		sttb.setBUY_TIME(tv_data.getText().toString());
    	dbHelper.updateSETTING(sttb);		
	}
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==SERIAZABLE){
				Toast.makeText(getApplicationContext(), (String)msg.obj, 0).show();
			}
		};
	};
}
