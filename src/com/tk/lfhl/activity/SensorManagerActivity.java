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
	private Button btn_qibang;//气泵开关控制
	protected static final String TAG = "AirLeakageTestActivity";
	private boolean isClose = true;
	private TextView tv_sf6data,debug_content;
	private String debugContent;
	boolean isRunn = true;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sensor_manager);
		EmGpio.gpioInit();
		boolean out216 = EmGpio.setGpioOutput(217);//电源开关输出
		boolean out217 = EmGpio.setGpioOutput(216);//气泵开关输出
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
				sf6PPm = (Float) msg.obj;//校准浓度
				sf6initPPM= MainActivity.getInstance.detector.getLastReplyData()+MainActivity.getInstance.positio;//初始浓度
				if(msg.arg1==-1){
					tv_sf6data.setText("当前无法获取SF6浓度,请检测传感器是否正常！");
				}else{
					tv_sf6data.setText("当前SF6浓度为:"+sf6PPm+"ppm \n"+sdf.format(new Date()));
				}
			}
			debugContent="校验后的浓度:"+sf6PPm+"\n"+"未校验的浓度："+sf6initPPM+"\n"
			+"零点输入："+MainActivity.getInstance.zero+"\t 零点检测:"+MainActivity.getInstance.zero_
			+"\n标气输入："+MainActivity.getInstance.biaoqi+"\t 标气检测:"+MainActivity.getInstance.biaoqi_
			+"\n校正系数:"+MainActivity.getInstance.getBJ()
			+"\n校正浓度 = (初始浓度-零点检测)*系数+零点输入\n"
			+"("+sf6initPPM+"-"+MainActivity.getInstance.zero_+")*"+MainActivity.getInstance.getBJ()+"+"+MainActivity.getInstance.zero
			+"\n系数公式:==============\n (标气输入 -零点输入)/(标气实际-零点实际)\n"
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
				Toast.makeText(getApplicationContext(), "通讯失败,无法标定", 0).show();
				return;
			}
			if(cb_1.isChecked()){
				try {
					if(Float.valueOf(gasData1.getText().toString())>=0&&Float.valueOf(gasData1.getText().toString())<=1000){
						if(sf6initPPM == MainActivity.getInstance.biaoqi_){
							Toast.makeText(getApplicationContext(), "六氟化硫零点实际检测浓度值:"+sf6initPPM+"ppm,与标气标定实际检测浓度值相同,无法标定!", 0).show();	
							return;
						}
						if(Float.valueOf(gasData1.getText().toString()) ==MainActivity.getInstance.biaoqi ){
							Toast.makeText(getApplicationContext(), "六氟化硫零点输入浓度值不能与标气输入浓度值相同,无法标定!", 0).show();	
							return;
						}
						
						
						db.updateGAS_CALIBRATION1(gasData1.getText().toString(),sf6initPPM+"");
						Toast.makeText(getApplicationContext(), "六氟化硫零点标定成功", 0).show();
					}else{
						Toast.makeText(getApplicationContext(), "零点输入格式有误,请输入0-1000的整数", 0).show();	
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "零点输入格式有误,请输入0-1000的整数", 0).show();
				}
				
			}
			if(cb_2.isChecked()){
				try {
					if(Float.valueOf(gasData2.getText().toString())>=0&&Float.valueOf(gasData2.getText().toString())<=1000){
						if(sf6initPPM == MainActivity.getInstance.zero_){
							Toast.makeText(getApplicationContext(), "六氟化硫标气实际检测浓度值:"+sf6initPPM+"ppm,与零点标定实际检测浓度值相同,无法标定!", 0).show();	
							return;
						}
						if(Float.valueOf(gasData2.getText().toString()) ==MainActivity.getInstance.zero ){
							Toast.makeText(getApplicationContext(), "六氟化硫标气输入浓度值不能与零点输入浓度值相同,无法标定!", 0).show();	
							return;
						}
						db.updateGAS_CALIBRATION2(gasData2.getText().toString(),sf6initPPM+"");
						Toast.makeText(getApplicationContext(), "六氟化硫标气标定成功", 0).show();
					}else{
						Toast.makeText(getApplicationContext(), "标气输入格式有误,请输入0-1000的整数", 0).show();	
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "标气输入格式有误,请输入0-1000的整数", 0).show();
				}
			}
			if(cb_3.isChecked()){
				
				try {
					if(Float.valueOf(gasData3.getText().toString())>=0&&Float.valueOf(gasData3.getText().toString())<=1000){
						db.updateGAS_CALIBRATION3(gasData3.getText().toString());
						Toast.makeText(getApplicationContext(), "六氟化硫报警点标定成功", 0).show();
					}else{
						Toast.makeText(getApplicationContext(), "报警点输入格式有误,请输入0-1000的整数", 0).show();	
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "报警点输入格式有误,请输入0-1000的整数", 0).show();	
				
				}
				
				
			}
			MainActivity.getInstance.shuaxinBiaoding();
		}
		if(v==btn_reset){
			db.updateGAS_CALIBRATION1("1","1");
			db.updateGAS_CALIBRATION2("1","1");
			db.updateGAS_CALIBRATION3("100");
			Toast.makeText(getApplicationContext(), "重置成功！", 0).show();
			MainActivity.getInstance.shuaxinBiaoding();
			finish();
		}
		if(btn_back==v){
			finish();
		}
		if(v==btn_qibang){
			if(btn_qibang.getText().equals("打开气泵")){
				//				Toast.makeText(getApplicationContext(), "开气泵", 0).show();
				openGas();
				isClose = true;
				btn_qibang.setText("关闭气泵");//显示关闭
			}else{
				EmGpio.setGpioDataHigh(216);//关气泵
				isClose = false;
				btn_qibang.setText("打开气泵");//显示打开
				//				Toast.makeText(getApplicationContext(), "关气泵", 0).show();
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
		EmGpio.setGpioDataHigh(216);//关气泵
		EmGpio.setGpioDataHigh(217);//关电源
		EmGpio.gpioUnInit();//释放gpio控制
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
							EmGpio.setGpioDataLow(217);//开电源
							Log.i(TAG, "开电源"+i);
							Thread.sleep(i);
							EmGpio.setGpioDataHigh(217);//关闭电源
							if(10-i>1){
								Log.i(TAG, "关电源"+(10-i));
								Thread.sleep(10-i);
							}else{
								continue;
							}
						}
					}
					EmGpio.setGpioDataLow(217);//开电源
					Log.i(TAG, "开电源");
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
							EmGpio.setGpioDataLow(216);//开气泵
							Log.i(TAG, "开气泵"+i);
							Thread.sleep(i);
							EmGpio.setGpioDataHigh(216);//关闭气泵
							if(10-i>1){
								Log.i(TAG, "关气泵"+(10-i));
								Thread.sleep(10-i);
							}else{
								continue;
							}
						}
					}
					EmGpio.setGpioDataLow(216);//开气泵
					Log.i(TAG, "开气泵");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
