package com.tk.lfhl.activity;

import java.io.File;

import com.mediatek.engineermode.io.EmGpio;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DriverInfoActivity extends Activity implements OnClickListener{
	Button btn_shuominshu,btn_version,btn_test,btn_test2;
	TextView tv_test2,tv_test3;
	private boolean isDestory = false;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isDestory = false;
		EmGpio.gpioInit();
		boolean out216 = EmGpio.setGpioOutput(217);//��Դ�������
		boolean out217 = EmGpio.setGpioOutput(216);//���ÿ������
		setContentView(R.layout.activity_info);
		View include = findViewById(R.id.il_title);
		TextView tv_title = (TextView) include.findViewById(R.id.tv_title);
		btn_version = (Button) findViewById(R.id.btn_version);
		tv_title.setText("������Ϣ");
		btn_shuominshu = (Button) findViewById(R.id.btn_shuominshu);
		btn_test2 = (Button) findViewById(R.id.btn_test2);
		btn_test = (Button) findViewById(R.id.btn_test);
		btn_shuominshu.setOnClickListener(this);
		btn_test.setOnClickListener(this);
		btn_test2.setOnClickListener(this);
		tv_test2 = (TextView) findViewById(R.id.tv_test2);
		tv_test3 = (TextView) findViewById(R.id.tv_test3);
		String version = null;
		try {
			PackageManager manager = getPackageManager();
			version = manager.getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btn_version.setText(btn_version.getText().toString()+"v"+version);
		openSwitch();
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isDestory = true;
		EmGpio.setGpioDataHigh(217);//�رյ�Դ
		EmGpio.setGpioDataHigh(216);//������
		EmGpio.gpioUnInit();//�ͷ�gpio����
	}

	public void openSwitch(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int i = 0;
					while (i<=10) {
						i++;
						for(int k= 0 ; k<9;k++){
							EmGpio.setGpioDataLow(217);//����Դ
							Thread.sleep(i);
							EmGpio.setGpioDataHigh(217);//�رյ�Դ
							if(10-i>1){
								Thread.sleep(10-i);
							}else{
								break;
							}
						}
					}
					EmGpio.setGpioDataLow(217);//����Դ
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	int address = 0;
	@Override
	public void onClick(View v) {
		if(v==btn_shuominshu){
			startActivity(new Intent(getApplicationContext(), InstructionsActivity.class));
		}
		if(v==btn_test){
			btn_test.setEnabled(false);
			new Thread(new Runnable() {

				@Override
				public void run() {

					for(int i = 0 ;  i< 6000000;i++){
						if(isDestory)
							break;
						Message msg = Message.obtain();
						msg.what=1;
						msg.obj=MainActivity.getInstance.getSF6GasData();
						mHandler.sendMessage(msg);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					mHandler.sendEmptyMessage(2);
				}
			}).start();
		}
		if(v==btn_test2){
			btn_test2.setEnabled(false);
			new Thread(new Runnable() {

				@Override
				public void run() {

					for(int i = 0 ;  i< 600;i++){
						if(isDestory)
							break;
						Message msg = Message.obtain();
						msg.what=4;
						address ++;
						if(address==255)
							address=1;
						msg.obj=MainActivity.getInstance.getSF6GasData(address);
						mHandler.sendMessage(msg);
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					mHandler.sendEmptyMessage(3);
				}
			}).start();
		}

	}
	int error_count = 0;
	int count = 0 ;
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				count++;
				float value = (Float) msg.obj;
				if(value==-100){
					btn_test.setText(count+")ͨѶʧ��,����ԭ������:\n 1.���е�������,�����޷�ͨѶ\n 2.��·���ӽӴ��������� \n ");	
				}else if(value==-101){
					btn_test.setText(count+")���ڴ�ʧ��");
				}else{
					btn_test.setText(count+") ͨѶ�ɹ�:��������Ũ��Ϊ"+value+"PPM");
				}
				if(value<0){
					error_count++;
				}
				tv_test3.setText("��"+count+"�β��� ,���������"+error_count+"��");
			}
			if(msg.what==2){
				btn_test.setEnabled(true);
				btn_test.setText("�������");
			}
			if(msg.what==3){
				btn_test2.setEnabled(true);
				btn_test2.setText("�������");
			}
			if(msg.what==4){
				count++;
				float value = (Float) msg.obj;
				if(value==-100){
					tv_test2.setText(count+")ͨѶʧ��,����ԭ������:\n 1.���е�������,�����޷�ͨѶ\n 2.��·���ӽӴ��������� \n ");	
				}else if(value==-101){
					tv_test2.setText(count+")���ڴ�ʧ��");
				}else{
					btn_test2.setText("��"+count+"��δ�ҵ���ַ "+value+"PPM");
				}
				if(value>=0){
					tv_test2.setText(count+")��������ַ��"+address);
				}
			}
		};
	};
}
