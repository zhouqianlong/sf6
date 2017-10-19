package com.tk.lfhl.activity;

import java.util.List;

import com.android.db.DBHelper;
import com.android.utils.MyToask;
import com.android.utils.PlayAudio;
import com.mediatek.engineermode.io.EmGpio;
import com.ramy.minervue.service.GasDetector;
import com.tk.lfhl.bean.SettingBean;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 主界面
 */
public class MainActivity extends Activity implements OnClickListener,OnLongClickListener{
	public  int value;//六氟化硫ppm
	private SelectPicPopupWindow selectPicPopupWindow;
	public static final String TAG = "SF6";
	public static List<String> last_id;//最后一次设置的每张表的id位置（巷道表，人员表，漏风方式表）
	public float [] list;//零点 标气 报警点
	public float xsGasValue =1;//系数
	private View include;
	private TextView tv_title;
	private Button btn_roadway_information;//巷道信息
	private Button btn_personnel_information;//人员信息
	private Button btn_air_leakage_way;//漏风方式
	private Button btn_test_report;//检测报告
	private Button btn_info;//检测报告
	private Button btn_setting;//浓度
	public  EditText et_gasvalue;//模拟浓度
	private DBHelper db;
	private boolean mainStatu = false;
	public GasDetector detector =  GasDetector.getInstance();
	public static MainActivity getInstance = null;
	private PlayAudio playAudio;
	public SettingBean sttb;
	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		playAudio = new PlayAudio(getApplicationContext());
		mainStatu = true;
		getInstance= this;
		db= new DBHelper(getApplicationContext());
		detector.close();
		//		startBackThread();
		setContentView(R.layout.activity_main);
		initView();
	}
	@Override
	public void onBackPressed() {
//		exit();
		return;
	}
	@Override 
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		EmGpio.setGpioDataHigh(216);//关气泵
		EmGpio.setGpioDataHigh(217);//关电源
		EmGpio.gpioUnInit();//释放gpio控制
		last_id = db.findLAST_TABLE();//查询最后一次的信息

		sttb = db.findSETTING();
		for(String a:last_id){
			Log.i("SF6", "last:"+a);
		}

		shuaxinBiaoding();
		//		btn_setting.performClick();
	}
	public void shuaxinBiaoding(){
		list = db.findGAS_CALIBRATION();
		xsGasValue = getBJ();
	}
	public float positio = 0;
	public float zero = 0;//零点输入
	public float zero_ = 0;//零点检测
	public float biaoqi = 0;//标气输入
	public float biaoqi_ = 0;//标气检测浓度
	public float getBJ(){
		if(list==null){
			return 1;
		}else{
			zero    = list[0];//零点输入
			zero_   = list[3];
			biaoqi  = list[1];
			biaoqi_ = list[4];
		}
		float x = biaoqi - zero;//标气输入值  -零点输入值
		float y = biaoqi_- zero_;
		if(y==0){
			return 1 ;
		}
		Log.i("GAS_CH4", "零点输入:"+zero+" 标气输入:"+biaoqi+"零点检测实际值:"+zero_+"标气检测实际值:"+biaoqi_);
		return  x/y;
	}
	/**
	 * 后台刷新气体
	 */
	//	public void startBackThread(){
	//		Runnable runnable = new Runnable() {
	//			@Override
	//			public void run() {
	//				if(detector.open()){
	//					Log.e(TAG, "打开串口成功.");
	//					isopen = true;
	//					GasDetector.CMD_SEND_TYPE = GasDetector.GAS_VALUE;
	//					if (!detector.doTurnOnDevice(GasDetector.READ_GAS_data)) {
	//						Log.e(TAG, "Error turning on GasDetector.");
	//						detector.close();
	//					}else{ 
	//						Log.e(TAG, "通讯成功");
	//						mhHandler.sendEmptyMessage(GasDetector.GAS_VALUE);
	//					}
	//				}else{
	//					detector.close();
	//					Log.e(TAG, "打开串口失败.");
	//				}
	//			}
	//		};
	//		Thread thread = new Thread(runnable);
	//		thread.start();
	// 
	//	}

	/**
	 * 模拟通讯成功
	 * @return
	 */
	public float testGasValue(int values){
		float gas = 0;
		//解析气体浓度
		MainActivity.getInstance.value = values;
		GasDetector.lastReplyString = values+"";
		GasDetector.lastReplyData = values;
		GasDetector.lastReplyCmd = GasDetector.GAS_VALUE;
		gas = ((detector.getLastReplyData()+positio)-zero_)*xsGasValue+zero;
		return gas;
	}

	public float getSF6GasData(){

		if(detector.open()){
			Log.e(TAG, "打开串口成功.");
			isopen = true;
			GasDetector.CMD_SEND_TYPE = GasDetector.GAS_VALUE;
			detector.initLastReplyData();//初始化浓度
			if (!detector.doTurnOnDevice(GasDetector.READ_GAS_data)) {
				Log.e(TAG, "Error turning on GasDetector.");
				detector.close();
				return -100;
			}else{ 
				if(detector.getLastReplyData()==-1){
					return -1;
				}
//				float value = ((detector.getLastReplyData()+positio)-zero_)*xsGasValue+zero;//校验
				float value = 0;
				Log.i("sf6", "sf6 value:"+value);
				if(value>10000){
					//重新上电
					EmGpio.gpioInit();
					EmGpio.setGpioOutput(217);//电源开关输出
					EmGpio.setGpioDataHigh(217);//关闭电源
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					EmGpio.setGpioDataLow(217);//开电源
					EmGpio.gpioUnInit();//释放gpio控制
				}else  if(value>list[2]){
					playAudio.playSounds();
				}else if(value<0){
					value=0;
				}
				return value;
			}
		}else{
			detector.close();
			return -101;
		}
	}
	public float getSF6GasData(int address){
		if(detector.open()){
			Log.e(TAG, "打开串口成功.");
			isopen = true;
			GasDetector.CMD_SEND_TYPE = GasDetector.GAS_VALUE;
			detector.initLastReplyData();//初始化浓度
			if (!detector.doTurnOnDevice(GasDetector.READ_GAS_data,address)) {
				Log.e(TAG, "Error turning on GasDetector.");
				detector.close();
				return -100;
			}else{ 

				//				int value = (int) (detector.getLastReplyData()*xsGasValue);
				//(GasMainFragement.gasInitGAS-GasMainActivity.getIntances().zero_)*xs+GasMainActivity.getIntances().zero
				if(detector.getLastReplyData()==-1){
					return -1;
				}
				float value = (((detector.getLastReplyData()+positio)-zero_)*xsGasValue+zero);
				Log.i("MMM", "positio:"+positio+"   value:"+value+" xsGasValue:"+xsGasValue+" 零点输入:"+zero+" 标气输入:"+biaoqi+"零点检测实际值:"+zero_+"标气检测实际值:"+biaoqi_);
				//				Message msg = Message.obtain();
				//				msg.what=8880911;
				//				msg.obj="初始浓度:"+(detector.getLastReplyData()+positio)+"\n校准浓度:"+value+" \n校准系数:"+xsGasValue+" \n零点输入:"+zero+" \n标气输入:"+biaoqi+"\n零点检测实际值:"+zero_+"\n标气检测实际值:"+biaoqi_;
				//				mhHandler.sendMessage(msg);
				if(value>10000){
					//重新上电
					EmGpio.gpioInit();
					EmGpio.setGpioOutput(217);//电源开关输出
					EmGpio.setGpioDataHigh(217);//关闭电源
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					EmGpio.setGpioDataLow(217);//开电源
					EmGpio.gpioUnInit();//释放gpio控制
				}else{
					if(value>list[2]){
						//						Toast.makeText(getApplicationContext(), "请注意SF6浓度超标",0).show();
						playAudio.playSounds();
					}
				}
				return value;
			}
		}else{
			detector.close();
			return -101;
		}
	}

	//	/**
	//	 * 后台刷新气体
	//	 */
	//	public void startBackThread(){
	//		new Thread(new Runnable() {
	//			@Override
	//			public void run() {
	//				while (mainStatu) {
	//					try {
	//						Thread.sleep(2000);
	//						if(detector.open()){
	//							Log.e(TAG, "打开串口成功.");
	//							isopen = true;
	//							GasDetector.CMD_SEND_TYPE = GasDetector.GAS_VALUE;
	//							if (!detector.doTurnOnDevice(GasDetector.READ_GAS_data)) {
	//								Log.e(TAG, "Error turning on GasDetector.");
	//								detector.close();
	//							}else{ 
	//								Log.e(TAG, "通讯成功");
	//								mhHandler.sendEmptyMessage(GasDetector.GAS_VALUE);
	//							}
	//						}else{
	//							detector.close();
	//							Log.e(TAG, "打开串口失败.");
	//						}
	//
	//					} catch (InterruptedException e) {
	//						e.printStackTrace();
	//					}
	//
	//				}
	//				mainStatu= true;
	//				startBackThread();
	//			}
	//		}).start();
	//	}
	/**
	 * 初始化控件+设置逻辑事件
	 */
	private void initView() {
		include = findViewById(R.id.il_title);
		tv_title = (TextView) include.findViewById(R.id.tv_title);
//		tv_title.setText("tv_title");


		btn_roadway_information = (Button) findViewById(R.id.btn_roadway_information);
		btn_personnel_information = (Button) findViewById(R.id.btn_personnel_information);
		btn_air_leakage_way = (Button) findViewById(R.id.btn_air_leakage_way);
		btn_test_report = (Button) findViewById(R.id.btn_test_report);
		btn_setting = (Button) findViewById(R.id.btn_setting);
		btn_info = (Button) findViewById(R.id.btn_info);

		btn_roadway_information.setOnClickListener(this);
		btn_personnel_information.setOnClickListener(this);
		btn_air_leakage_way.setOnClickListener(this);
		btn_test_report.setOnClickListener(this);
		btn_setting.setOnClickListener(this);
		btn_info.setOnClickListener(this);
		btn_setting.setOnLongClickListener(this);
		et_gasvalue = (EditText) findViewById(R.id.et_gasvalue);
	}
	private boolean isopen = false;
	@Override
	public void onClick(View v) {
		if(v==btn_roadway_information){
			//			MyToask.showTextToast("加载巷道信息",getApplicationContext() ,Toast.LENGTH_SHORT);
			startActivity(new Intent(getApplicationContext(), RoadwayInformationActivity.class));
		}

		if(v==btn_personnel_information){
			//			MyToask.showTextToast("加载人员信息",getApplicationContext() ,Toast.LENGTH_SHORT);
			startActivity(new Intent(getApplicationContext(), PeopleInfoActivity.class));
		}
		if(v==btn_air_leakage_way){
			//			MyToask.showTextToast("加载漏风方式",getApplicationContext() ,Toast.LENGTH_SHORT);
			startActivity(new Intent(getApplicationContext(), VentilationLeakageActivity.class));
		}

		if(v==btn_test_report){//activity_baogao
			startActivity(new Intent(getApplicationContext(), ListBaoGaoActivity.class));
			//			MyToask.showTextToast("加载检测报告",getApplicationContext() ,Toast.LENGTH_SHORT);
		}
		if(v==btn_info){//activity_baogao
			startActivity(new Intent(getApplicationContext(), DriverInfoActivity.class));
		}
		//		if(v==btn_sf6_ppm){
		//			selectPicPopupWindow = new SelectPicPopupWindow(MainActivity.this, itemsOnClick);
		//				//显示窗口
		//			selectPicPopupWindow.showAtLocation(MainActivity.this.findViewById(R.id.btn_sf6_ppm), Gravity.TOP|Gravity.RIGHT, 10, 230); //设置layout在PopupWindow中显示的位置
		//		}
		if(v==btn_setting){
			startSettingActivity();
			//			btn_setting.setText("正在获取浓度...");
			//			new Thread(new Runnable() {
			//				@Override
			//				public void run() {
			//					Message msg = Message.obtain();
			//					msg.arg1 =getSF6GasData(); 
			//					msg.what=1;
			//					mhHandler.sendMessage(msg);
			//				}
			//			}).start();


		}
	}
	//为弹出窗口实现监听类
	private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			selectPicPopupWindow.dismiss();
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EmGpio.setGpioDataHigh(216);//关气泵
		EmGpio.setGpioDataHigh(217);//关电源
		EmGpio.gpioUnInit();//释放gpio控制
		mainStatu = false;
		getInstance = null;
		Log.i("onDestroy", "mainStatu = false;");
		System.exit(1);
	}
	@Override
	public boolean onLongClick(View v) {
		//		if(v==btn_sf6_ppm){
		//			new AlertDialog.Builder(MainActivity.this).setTitle("提示").setPositiveButton("是",  new DialogInterface.OnClickListener(){
		//
		//				@Override
		//				public void onClick(DialogInterface dialog, int which) {
		//					
		//					startActivity(new Intent(getApplicationContext(), SensorManagerActivity.class));
		//					
		//				}}).setNeutralButton("否",null).setMessage("是否进行气体标定?").show();
		//		}
		return false;
	}


	Handler mhHandler = new Handler(){
		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {

			if(msg.what == GasDetector.GAS_VALUE){ 
				if(value/xsGasValue>list[2]){
					Toast.makeText(getApplicationContext(), "请注意SF6浓度超标",0).show();
					playAudio.playSounds();
				}
				btn_setting.setText("SF6浓度\n"+(int)(value/xsGasValue)+"ppm");
			}

			if(msg.what==1){
				int value = msg.arg1;
				if(value==-1){
					btn_setting.setText("SF6浓度获取失败");
				}else{
					if(value/xsGasValue>list[2]){
						Toast.makeText(getApplicationContext(), "请注意SF6浓度超标",0).show();
						playAudio.playSounds();
					}
					btn_setting.setText("SF6浓度\n"+(int)(value/xsGasValue)+"ppm");
				}
			}

			if(msg.what==0){
				isExit = false;
			}


			if(msg.what==8880911){
				//				Toast.makeText(getApplicationContext(), msg.obj.toString(),0).show();
				MyToask.showTextToast(msg.obj.toString(),getApplicationContext() ,Toast.LENGTH_SHORT);
			}		
		};
	};






	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mhHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override  
	public boolean onOptionsItemSelected(MenuItem item) {  
		// TODO Auto-generated method stub  
		switch(item.getItemId()){  

		case R.id.action_setting:

			//		Intent settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
			//		settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			//				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);	
			//		startActivity(settings);
			startSettingActivity();

			break;

		default:  
			break;  
		}  
		return super.onOptionsItemSelected(item);  
	}
	private void startSettingActivity() {
//		Intent settings = new Intent(getApplicationContext(),SettingActivity.class);
//		settings.putExtra("flag", true);
//		startActivity(settings);

				final EditText inputServer = new EditText(this);
				String digits = "0123456789";
				//限制只能输入数字
				inputServer.setKeyListener(DigitsKeyListener.getInstance(digits));   
				InputMethodManager inputManager =(InputMethodManager)inputServer.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				new AlertDialog.Builder(this) 
				.setTitle("请输入密码") 
				.setMessage("密码") 
				.setView(inputServer)
				.setPositiveButton("确定",new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int whichButton) { 
						String inputName = inputServer.getText().toString();
						if(inputName.equals("2013110110"))
						{
							Intent settings = new Intent(getApplicationContext(),SettingActivity.class);
							settings.putExtra("flag", false);
							startActivity(settings);
		
						}
						else if(inputName.equals("23759"))
						{
							Intent settings = new Intent(getApplicationContext(),SettingActivity.class);
							settings.putExtra("flag", true);
							startActivity(settings);
		
						}
						else
						{
							Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
		
						}
					} 
				}) 
				.setNegativeButton("取消", new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int whichButton) { 
						//取消按钮事件 
						//	finish(); 
					} 
				})
				.show();
	}  
}

