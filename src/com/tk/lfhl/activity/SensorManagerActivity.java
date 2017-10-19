package com.tk.lfhl.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.db.DBHelper;
import com.mediatek.engineermode.io.EmGpio;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SensorManagerActivity extends Activity implements OnCheckedChangeListener,OnClickListener{
	private CheckBox cb_1,cb_2,cb_3;
	private Button btn_ok,btn_back,btn_gasadd,btn_gasremove,btn_reset;
	private DBHelper db;
	private EditText gasData1,gasData2,gasData3;
	private Button btn_qibang;//���ÿ��ؿ���
	protected static final String TAG = "AirLeakageTestActivity";
	private boolean isClose = true;
	private TextView tv_sf6data,debug_content;
	private String debugContent;
	boolean isRunn = true;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_manager);
		EmGpio.gpioInit();
		boolean out216 = EmGpio.setGpioOutput(217);//��Դ�������
		boolean out217 = EmGpio.setGpioOutput(216);//���ÿ������
		openSwitch();
		db = new DBHelper(this);
		cb_1 = (CheckBox) findViewById(R.id.cb_1);
		cb_2 = (CheckBox) findViewById(R.id.cb_2);
		cb_3 = (CheckBox) findViewById(R.id.cb_3);
		btn_reset = (Button) findViewById(R.id.btn_reset);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_gasadd = (Button) findViewById(R.id.btn_gasadd);
		btn_gasremove = (Button) findViewById(R.id.btn_gasremove);
		tv_sf6data = (TextView) findViewById(R.id.tv_sf6data);
		debug_content = (TextView) findViewById(R.id.debug_content);
		cb_1.setOnCheckedChangeListener(this);
		cb_2.setOnCheckedChangeListener(this);
		cb_3.setOnCheckedChangeListener(this);
		cb_1.setChecked(true);
		gasData1 = (EditText) findViewById(R.id.gasData1);
		gasData2 = (EditText) findViewById(R.id.gasData2);
		gasData3 = (EditText) findViewById(R.id.gasData3);
		btn_qibang = (Button) findViewById(R.id.btn_qibang);
		btn_qibang.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_gasadd.setOnClickListener(this);
		btn_gasremove.setOnClickListener(this);
		btn_reset.setOnClickListener(this);
		float [] list = db.findGAS_CALIBRATION();
		gasData1.setText(list[0]+"");
		gasData2.setText(list[1]+"");
		gasData3.setText(list[2]+"");
		testSf6Gas();
	}
	
	public void testSf6Gas(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (isRunn) {
					try {
						float data = MainActivity.getInstance.getSF6GasData();
//						float data = MainActivity.getInstance.testGasValue(3);
						Message msg = Message.obtain();
						msg.what=1;
						msg.obj = data;
						mHandler.sendMessage(msg);
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}
	private float sf6initPPM = 0;
	private float sf6PPm = -1;
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sf6PPm = (Float) msg.obj;//У׼Ũ��
				sf6initPPM= MainActivity.getInstance.detector.getLastReplyData()+MainActivity.getInstance.positio;//��ʼŨ��
				if(msg.arg1==-1){
					tv_sf6data.setText("��ǰ�޷���ȡSF6Ũ��,���⴫�����Ƿ�������");
				}else{
					tv_sf6data.setText("��ǰSF6Ũ��Ϊ:"+sf6PPm+"ppm \n"+sdf.format(new Date()));
				}
			}
			debugContent="У����Ũ��:"+sf6PPm+"\n"+"δУ���Ũ�ȣ�"+sf6initPPM+"\n"
			+"������룺"+MainActivity.getInstance.zero+"\t �����:"+MainActivity.getInstance.zero_
			+"\n�������룺"+MainActivity.getInstance.biaoqi+"\t �������:"+MainActivity.getInstance.biaoqi_
			+"\nУ��ϵ��:"+MainActivity.getInstance.getBJ()
			+"\nУ��Ũ�� = (��ʼŨ��-�����)*ϵ��+�������\n"
			+"("+sf6initPPM+"-"+MainActivity.getInstance.zero_+")*"+MainActivity.getInstance.getBJ()+"+"+MainActivity.getInstance.zero
			+"\nϵ����ʽ:==============\n (�������� -�������)/(����ʵ��-���ʵ��)\n"
			+"("+MainActivity.getInstance.biaoqi+"-"+MainActivity.getInstance.zero+")/("+MainActivity.getInstance.biaoqi_+"-"+MainActivity.getInstance.zero_+")";
			debug_content.setText(debugContent);
		};
	};
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView==cb_1){
			if(isChecked){
				cb_2.setChecked(false);
				cb_3.setChecked(false);
			}
		}
		if(buttonView==cb_2){
			if(isChecked){
				cb_1.setChecked(false);
				cb_3.setChecked(false);
			}
		}
		if(buttonView==cb_3){
			if(isChecked){
				cb_1.setChecked(false);
				cb_2.setChecked(false);
			}
		}
	}
	@Override
	public void onClick(View v) {
		if(btn_ok==v){
			if(sf6initPPM<0){
				Toast.makeText(getApplicationContext(), "ͨѶʧ��,�޷��궨", 0).show();
				return;
			}
			if(cb_1.isChecked()){
				try {
					if(Float.valueOf(gasData1.getText().toString())>=0&&Float.valueOf(gasData1.getText().toString())<=1000){
						if(sf6initPPM == MainActivity.getInstance.biaoqi_){
							Toast.makeText(getApplicationContext(), "�����������ʵ�ʼ��Ũ��ֵ:"+sf6initPPM+"ppm,������궨ʵ�ʼ��Ũ��ֵ��ͬ,�޷��궨!", 0).show();	
							return;
						}
						if(Float.valueOf(gasData1.getText().toString()) ==MainActivity.getInstance.biaoqi ){
							Toast.makeText(getApplicationContext(), "���������������Ũ��ֵ�������������Ũ��ֵ��ͬ,�޷��궨!", 0).show();	
							return;
						}
						
						
						db.updateGAS_CALIBRATION1(gasData1.getText().toString(),sf6initPPM+"");
						Toast.makeText(getApplicationContext(), "�����������궨�ɹ�", 0).show();
					}else{
						Toast.makeText(getApplicationContext(), "��������ʽ����,������0-1000������", 0).show();	
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "��������ʽ����,������0-1000������", 0).show();
				}
				
			}
			if(cb_2.isChecked()){
				try {
					if(Float.valueOf(gasData2.getText().toString())>=0&&Float.valueOf(gasData2.getText().toString())<=1000){
						if(sf6initPPM == MainActivity.getInstance.zero_){
							Toast.makeText(getApplicationContext(), "�����������ʵ�ʼ��Ũ��ֵ:"+sf6initPPM+"ppm,�����궨ʵ�ʼ��Ũ��ֵ��ͬ,�޷��궨!", 0).show();	
							return;
						}
						if(Float.valueOf(gasData2.getText().toString()) ==MainActivity.getInstance.zero ){
							Toast.makeText(getApplicationContext(), "���������������Ũ��ֵ�������������Ũ��ֵ��ͬ,�޷��궨!", 0).show();	
							return;
						}
						db.updateGAS_CALIBRATION2(gasData2.getText().toString(),sf6initPPM+"");
						Toast.makeText(getApplicationContext(), "������������궨�ɹ�", 0).show();
					}else{
						Toast.makeText(getApplicationContext(), "���������ʽ����,������0-1000������", 0).show();	
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "���������ʽ����,������0-1000������", 0).show();
				}
			}
			if(cb_3.isChecked()){
				
				try {
					if(Float.valueOf(gasData3.getText().toString())>=0&&Float.valueOf(gasData3.getText().toString())<=1000){
						db.updateGAS_CALIBRATION3(gasData3.getText().toString());
						Toast.makeText(getApplicationContext(), "�������򱨾���궨�ɹ�", 0).show();
					}else{
						Toast.makeText(getApplicationContext(), "�����������ʽ����,������0-1000������", 0).show();	
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "�����������ʽ����,������0-1000������", 0).show();	
				
				}
				
				
			}
			MainActivity.getInstance.shuaxinBiaoding();
		}
		if(v==btn_reset){
			db.updateGAS_CALIBRATION1("1","1");
			db.updateGAS_CALIBRATION2("1","1");
			db.updateGAS_CALIBRATION3("100");
			Toast.makeText(getApplicationContext(), "���óɹ���", 0).show();
			MainActivity.getInstance.shuaxinBiaoding();
			finish();
		}
		if(btn_back==v){
			finish();
		}
		if(v==btn_qibang){
			if(btn_qibang.getText().equals("������")){
				//				Toast.makeText(getApplicationContext(), "������", 0).show();
				openGas();
				isClose = true;
				btn_qibang.setText("�ر�����");//��ʾ�ر�
			}else{
				EmGpio.setGpioDataHigh(216);//������
				isClose = false;
				btn_qibang.setText("������");//��ʾ��
				//				Toast.makeText(getApplicationContext(), "������", 0).show();
			}
		}
		if(v==btn_gasremove){
			MainActivity.getInstance.positio =MainActivity.getInstance.positio-10; 
		}
		if(v==btn_gasadd){
			MainActivity.getInstance.positio =MainActivity.getInstance.positio+10;
		}
	};
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		EmGpio.setGpioDataHigh(216);//������
		EmGpio.setGpioDataHigh(217);//�ص�Դ
		EmGpio.gpioUnInit();//�ͷ�gpio����
		isRunn = false;
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
							Log.i(TAG, "����Դ"+i);
							Thread.sleep(i);
							EmGpio.setGpioDataHigh(217);//�رյ�Դ
							if(10-i>1){
								Log.i(TAG, "�ص�Դ"+(10-i));
								Thread.sleep(10-i);
							}else{
								continue;
							}
						}
					}
					EmGpio.setGpioDataLow(217);//����Դ
					Log.i(TAG, "����Դ");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void openGas(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					int i = 0;
					while (i<=10) {
						i++;
						for(int k= 0 ; k< 9;k++){
							if(isClose==false){
								return;
							}
							EmGpio.setGpioDataLow(216);//������
							Log.i(TAG, "������"+i);
							Thread.sleep(i);
							EmGpio.setGpioDataHigh(216);//�ر�����
							if(10-i>1){
								Log.i(TAG, "������"+(10-i));
								Thread.sleep(10-i);
							}else{
								continue;
							}
						}
					}
					EmGpio.setGpioDataLow(216);//������
					Log.i(TAG, "������");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
