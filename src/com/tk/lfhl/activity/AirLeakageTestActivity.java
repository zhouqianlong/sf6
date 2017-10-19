package com.tk.lfhl.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.android.db.DBHelper;
import com.android.utils.NumToChinese;
import com.mediatek.engineermode.io.EmGpio;
import com.tk.adapter.AirTestAdapter;
import com.tk.lfhl.bean.AirTestBean;
import com.tk.lfhl.bean.PeopleInfo;
import com.tk.lfhl.bean.RoadWayInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AirLeakageTestActivity extends Activity implements OnClickListener{
	protected static final String TAG = "AirLeakageTestActivity";
	public  boolean isFlag = false;
	private List<Float> calDataList = new ArrayList<Float>();//�����ȶ����ݵļ���
	private List<RoadWayInfo> roadWayInfos;
	private List<PeopleInfo> peopleInfos;
	private String type;// ©�緽ʽ
	private Button btn_createbaogao1;
	private ImageView iv_photo;
	public  ListView lv_test;
	private List<String> last_id;
	private DBHelper db;
	private AirTestAdapter adapter;
	private List<AirTestBean> gaslist;
	private Button btn_next1;
	private TextView tv_sf6ppm,tv_title2;//Ũ����ʾ    ��ѹ����
	private TextView tv_sf6time;//ʣ����ʱ��
	private LinearLayout ll_next,ll_next2,ll_map;//��ѹ�˵�����ѹ�˵�������ͼ
	private XYMultipleSeriesDataset dataset = null;
	private XYMultipleSeriesRenderer renderer = null;
	private Button btn_oknext;//���޲д�,�ɽ�����һ�β���
	private Button btn_wait;//���вд潨��ȴ�
	private Button btn_autotest;//�Զ����SF6�д�
	private Button btn_manualtest;//�ֶ����SF6�д�
	private Button btn_createbaogao2;//������ϲ����ɱ���
	private Button btn_qibang;//���ÿ��ؿ���
	private DecimalFormat   df   =new   DecimalFormat("0.00");  
	private List<AirTestBean> shoudonglist = new ArrayList<AirTestBean>();
	public int chancun;
	public long sf6time = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air_leakage_test);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		db= new DBHelper(getApplicationContext());
						sf6time = getSharedPreferences("sf6001", 0).getInt("time",5)*60000;
//		sf6time = 5000;
		chancun = getSharedPreferences("sf6001", 0).getInt("chancun",1);
		last_id = db.findLAST_TABLE();//��ѯ���һ�ε���Ϣ
		roadWayInfos= db.findRoad_Way_name(last_id.get(0)); 
		peopleInfos = db.findTablePeople(last_id.get(1));
		type = last_id.get(2);
		if(roadWayInfos.size()==0){
			Toast.makeText(getApplicationContext(), "����д�����Ϣ���м��!", 0).show();
			finish();
		}
		if(peopleInfos.size()==0){
			Toast.makeText(getApplicationContext(), "����д��Ա��Ϣ���м��!", 0).show();
			finish();
		}
		lv_test = (ListView) findViewById(R.id.lv_test);
		ll_map = (LinearLayout) findViewById(R.id.ll_map);
		tv_sf6ppm = (TextView) findViewById(R.id.tv_sf6ppm);
		tv_title2 = (TextView) findViewById(R.id.tv_title2);
		tv_sf6time = (TextView) findViewById(R.id.tv_sf6time);
		iv_photo = (ImageView) findViewById(R.id.iv_photo);
		btn_qibang = (Button) findViewById(R.id.btn_qibang);
		btn_qibang.setOnClickListener(this);

		setImageView(type);

	}
	public  void  setImageView(String imagename){
		if ("��ѹ©��".equals(imagename)) {
			initAirTestList(1);
			adapter = new AirTestAdapter(AirLeakageTestActivity.this, gaslist,this);
			lv_test.setAdapter(adapter);
			ll_next2 = (LinearLayout) findViewById(R.id.ll_next2);
			ll_next2.setVisibility(View.GONE);
			btn_oknext = (Button) findViewById(R.id.btn_oknext);
			btn_wait = (Button) findViewById(R.id.btn_wait);
			btn_autotest = (Button) findViewById(R.id.btn_autotest);
			btn_manualtest = (Button) findViewById(R.id.btn_manualtest);
			btn_createbaogao2 = (Button) findViewById(R.id.btn_createbaogao2);
			btn_oknext.setVisibility(View.GONE);
			btn_wait.setVisibility(View.GONE);
			btn_createbaogao2.setVisibility(View.GONE);
			btn_wait.setOnClickListener(this);
			btn_oknext.setOnClickListener(this);
			btn_autotest.setOnClickListener(this);
			btn_manualtest.setOnClickListener(this);
			btn_createbaogao2.setOnClickListener(this);
			iv_photo.setImageResource(R.drawable.localpositivepressure);
		}
		if ("��ѹ©��".equals(imagename)) {
			shoudonglist.add(new AirTestBean("",0, System.currentTimeMillis()-60000));//ԭ��
			initAirTestList(1);
			adapter = new AirTestAdapter(AirLeakageTestActivity.this, gaslist,this);
			lv_test.setAdapter(adapter);
			ll_next = (LinearLayout) findViewById(R.id.ll_next);
			ll_next.setVisibility(View.GONE);
			btn_next1 = (Button) findViewById(R.id.btn_next1);
			btn_next1.setOnClickListener(this);
			btn_createbaogao1 = (Button) findViewById(R.id.btn_createbaogao1);
			btn_createbaogao1.setOnClickListener(this);
			iv_photo.setImageResource(R.drawable.localnegativepressure);
		}
		if ("������ѹ©��".equals(imagename)) {
			iv_photo.setImageResource(R.drawable.continuouspositivepressure);
			finish();
		}
		if ("������ѹ©��".equals(imagename)) {
			iv_photo.setImageResource(R.drawable.continuousnegativepressure);
			finish();
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		isFlag = false;
		auto_Flag = false;
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	};
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				tv_sf6ppm.setText("SF6Ũ�ȣ�"+(int)MainActivity.getInstance.value/MainActivity.getInstance.xsGasValue+" ppm");//value/xsGasValue
			}
			if(msg.what==2){
				if((List<AirTestBean>)msg.obj==null)
					return;
				ll_map.removeAllViews();
				try {
					ll_map.addView(createMap((List<AirTestBean>)msg.obj));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tv_sf6ppm.setText("SF6Ũ�ȣ�"+msg.arg1/100f+" ppm");//value/xsGasValue

			}
			if(msg.what==3){
				isFlag = false;
				if(type.equals("��ѹ©��")){
					btn_oknext.setVisibility(View.GONE);
					btn_autotest.setVisibility(View.VISIBLE);
					btn_manualtest.setVisibility(View.VISIBLE);
					setbtn_creategaogaoVisibility(View.VISIBLE);

				}else{
					//					btn_start_test.setText("���Գɹ�!"+df.format(msg.obj));
					//					btn_start_test.setEnabled(false);
					setbtn_creategaogaoVisibility(View.VISIBLE);
					tv_sf6time.setVisibility(View.GONE);
					maxData = 0;
				}
			}
			if(msg.what==4){
				ll_map.removeAllViews();
				try {
					ll_map.addView(createMap((List<AirTestBean>)msg.obj));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tv_sf6ppm.setText("SF6Ũ�ȣ�"+msg.arg1/100f+" ppm");//value/xsGasValue
				if(msg.arg1<chancun){
					btn_oknext.setVisibility(View.VISIBLE);//�޲д�  ��һ�β���
					shoudonglist.clear();
					btn_createbaogao2.setVisibility(View.VISIBLE);
					btn_wait.setVisibility(View.GONE);
					tv_title2.setVisibility(View.GONE);
					tv_sf6time.setVisibility(View.GONE);
					maxData = 0;
				}else{
					btn_wait.setVisibility(View.VISIBLE);//�вд�  ��Ҫ�ȴ�
					tv_title2.setVisibility(View.VISIBLE);
					btn_oknext.setVisibility(View.GONE);
				}
			}
			if(msg.what==5){
				ll_map.removeAllViews();
				try {
					ll_map.addView(createMap((List<AirTestBean>)msg.obj));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tv_sf6ppm.setText("SF6Ũ�ȣ�"+msg.arg1/100f+" ppm");//value/xsGasValue
				if(msg.arg1<chancun){
					btn_oknext.setVisibility(View.VISIBLE);//�޲д�  ��һ�β���
					btn_createbaogao2.setVisibility(View.VISIBLE);
					btn_autotest.setVisibility(View.GONE);
					btn_wait.setVisibility(View.GONE);
					tv_title2.setVisibility(View.GONE);
					btn_autotest.setText("�Զ����SF6�д�");
					btn_autotest.setEnabled(true);
					auto_Flag = false;
					shoudonglist.clear();
					//					tv_sf6ppm.setVisibility(View.GONE);
					maxData = 0;
				}else{
					btn_wait.setVisibility(View.VISIBLE);//�вд�  ��Ҫ�ȴ�
					tv_title2.setVisibility(View.VISIBLE);
					btn_oknext.setVisibility(View.GONE);
				}
			}
			if(msg.what==6){
				try {
					new AlertDialog.Builder(AirLeakageTestActivity.this).setMessage("������ͨѶ�쳣,����ϵ����Ա,").setPositiveButton("ȷ��",null).show();
					isFlag = false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}


			if(msg.what==80){
				String strTime = (String) msg.obj;
				tv_sf6time.setText("�´μ�⵹��ʱ:"+strTime);

			}
		}
	};
	boolean flg = false;


	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(AirLeakageTestActivity.this).setMessage("�Ƿ��˳�������?").setPositiveButton("��", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}}).setNegativeButton("��", null).show();

	}
	private boolean auto_Flag = false;//�Զ���⿪��
	@Override
	public void onClick(View v) { 

		if(v==btn_autotest){

			//			btn_autotest.setVisibility(View.GONE);
			btn_manualtest.setVisibility(View.GONE);
			//			btn_createbaogao2.setVisibility(View.GONE);
			if(auto_Flag==false){
				auto_Flag = true;
				startAutoTest(sf6time);
				btn_autotest.setEnabled(false);
				btn_autotest.setText("�Զ������(ÿ"+sf6time/1000+"����һ��)");
				Toast.makeText(getApplicationContext(), " �����Զ����...", 0).show();
			}else{
				Toast.makeText(getApplicationContext(), " �����Զ����", 0).show();
			}

		}
		if(v==btn_manualtest){//�ֶ����   
			btn_autotest.setVisibility(View.GONE);
			btn_manualtest.setVisibility(View.GONE);
			btn_createbaogao2.setVisibility(View.GONE);
			shoudongTest();
		}
		if(v==btn_next1){//��һ�β���
			isFlag = false;
			flg = false;
//			adapter.setList(addAirTestList(NumToChinese.foematInteger((gaslist.size()+2))+"���������ڲ��Ե�"+(gaslist.size()-1)));
			adapter.setList(addAirTestList(NumToChinese.foematInteger((gaslist.size()+2))+"��SF6�����ǲ���"+(gaslist.size()-1)));
			lv_test.setSelection(gaslist.size()-1);
			setbtn_creategaogaoVisibility(View.GONE);
		}
		if(v==btn_oknext){//�޲д�,��һ�β���
			isFlag = false;
			flg= false;
			adapter.setList(addAirTestList(NumToChinese.foematInteger((gaslist.size()+2))+"����ƿ�ƶ����ͷŵ�"+(gaslist.size()+1)+"��,�������ڲ��Ե�"+(gaslist.size())));
			lv_test.setSelection(gaslist.size()-1);
			setbtn_creategaogaoVisibility(View.GONE);
			btn_createbaogao2.setVisibility(View.VISIBLE);

		}
		if(v==btn_wait){
			if(auto_Flag){
				Toast.makeText(getApplicationContext(), "�����Զ����,���ĵȴ�", 0).show();
			}else{
				btn_autotest.setVisibility(View.VISIBLE);
				btn_manualtest.setVisibility(View.VISIBLE);
				btn_createbaogao2.setVisibility(View.VISIBLE);
				btn_wait.setVisibility(View.GONE);
			}
		}
		if(v==btn_createbaogao1){
			adapter.mHandler.sendEmptyMessage(2);
		}
		if(v==btn_createbaogao2){
			adapter.mHandler.sendEmptyMessage(2);
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

	}
	public boolean HIDEN = false;
	public boolean SHOW = true;

	public boolean initAirTestList(int size){
		gaslist = new ArrayList<AirTestBean>();
		if(size<=0){
			return false;
		}
		for(int i = 0 ; i < size;i++){
			int position  = i +1;
			gaslist.add(new AirTestBean("����SF6�����ǲ���"+position,-1,System.currentTimeMillis()));
		}
		return true;

	}
	public  List<AirTestBean>  addAirTestList(String content){
		gaslist.add(new AirTestBean(content,-1,System.currentTimeMillis()));
		return gaslist;
	}
	public  List<AirTestBean>  deleteAirTestList(){
		if(gaslist.size()<=1){
			new AlertDialog.Builder(AirLeakageTestActivity.this).setMessage("������������һ�Σ�").setPositiveButton("ȷ��", null).show();
			return gaslist;
		}else{
			if(gaslist.get(gaslist.size()-1).getGasDate()!=-1){
				adapter.deleteCount();
			}
			gaslist.remove(gaslist.size()-1);
		}
		return gaslist;
	}
	public List<RoadWayInfo> getroadWayInfos(){
		return roadWayInfos;
	}
	public List<PeopleInfo> getPeopleInfos() {
		return peopleInfos;
	}
	public void setPeopleInfos(List<PeopleInfo> peopleInfos) {
		this.peopleInfos = peopleInfos;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public DBHelper getDb() {
		return db;
	}
	public void setbtn_creategaogaoVisibility(int visibility){
		if(visibility==View.VISIBLE){
			if(type.equals("��ѹ©��")){
				ll_next2.setVisibility(View.VISIBLE);
			}else{
				ll_next.setVisibility(View.VISIBLE);
			}
		}else{
			if(type.equals("��ѹ©��")){
				ll_next2.setVisibility(View.INVISIBLE);
			}else{
				ll_next.setVisibility(View.INVISIBLE);
			}
		}
	}
	/****************************************************��ͼ����START***************************************************/
	public static int X_NUMBER = 6;//����������  ʱ��̶�
	public static int Y_NUMBER = 5;//����������	 Ũ�ȿ̶�
	public static double X_START_GAS = 0.0;
	public static double X_END_GAS = 100.0;
	public void startMap(final int position){//���ݰ�ť  ͨ����ť������ǰ�������                    //���ݵ�ǰ���ݵ�λ�� 
		isFlag = true; 
		tv_sf6time.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					List<AirTestBean> list = new ArrayList<AirTestBean>();
					list.add(new AirTestBean("",0, System.currentTimeMillis()-1000));
					while(isFlag){
						float num = getGasData();//��ȡŨ��
						if(num==-1||num==-100){
							errorCount++;
							if(errorCount>=3){
								mHandler.sendEmptyMessage(6);//��������3���˳�
							}else{
								continue;
							}

						}
						list.add(new AirTestBean("",num, System.currentTimeMillis()));
						Message msg = Message.obtain();
						msg.what=2;
						msg.obj = list;
						msg.arg1 = (int) (num*100);

						mHandler.sendMessage(msg);//����һ��ͼ��
						calDataList.add((float)num);
						errorCount= 0;
						startTimeCount(position);//������ʱ��
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

	//��ʱ��
	private void startTimeCount(final int position) {
		if(flg == false){
			flg = true;
			new Thread(new Runnable() {//ʱ�䵹��ʱ���

				@Override
				public void run() {
					long startTime = System.currentTimeMillis()+sf6time;
					long postionTime =0;
					while ((postionTime=startTime-System.currentTimeMillis())>=500&&flg) {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
							String strTime = sdf.format(new Date(postionTime));
							Message message = Message.obtain();
							message.obj = strTime;
							message.what=80;
							mHandler.sendMessage(message);
							Thread.sleep(800);
						} catch (InterruptedException e) {
						}
					}
					if(position!=-1&&flg){
						double calResult = 0;
						calResult=calDate(calDataList,2);
						if(calResult>=0){//�����ȶ��ɹ���������
							Message msg2 = Message.obtain();
							msg2.obj = calResult;
							msg2.what=3;
							mHandler.sendMessage(msg2);
							gaslist.get(position).setGasDate(Float.valueOf(df.format(calResult)));
							calDataList.clear();
						} 

					}
					flg = false;
				}
			}).start();
		}
	}
	private void startAutoTest(final long time){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				while(auto_Flag){
					try {
						float num = getGasData();//ģ�����Ũ��
						shoudonglist.add(new AirTestBean("",num, System.currentTimeMillis()));
						Message msg = Message.obtain();
						msg.what=5;
						msg.obj = shoudonglist;
						msg.arg1 = (int) (num*100);
						mHandler.sendMessage(msg);
						startTimeCount(-1);//������ʱ��
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}
	public void shoudongTest(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				float num = getGasData();//ģ�����Ũ��
				shoudonglist.add(new AirTestBean("",num, System.currentTimeMillis()));
				Message msg = Message.obtain();
				msg.what=4;
				msg.obj = shoudonglist;
				msg.arg1 = (int) (num*100);
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	int errorCount = 0;
	public float getGasData(){
		return MainActivity.getInstance.getSF6GasData();//ģ�����Ũ��
		//		return  (int)(Math.random()*8)+100;//ģ�����Ũ��
	}

	//	public double calDate(List<Float> data){
	//		//����5%
	//		if(data==null)
	//			return -1;
	//		int datasize = data.size();
	//		if(data.size()<3)
	//			return -1;
	//		float data1,data2,data3;
	//		data1 = data.get(datasize-1);
	//		data2 = data.get(datasize-2);
	//		data3 = data.get(datasize-3);
	//		 double result = (data1+data2+data3)/3;//����ƽ��ֵ
	//		if(Math.abs(data1-data2)<=(data1*0.05)){//1 2�Ƚ����
	//			if(Math.abs(data2-data3)<=(data2*0.05)){// 2 3�Ƚ����
	//				if(Math.abs(data1-data3)<=(data3*0.05)){//1 3�Ƚ����
	//					System.out.println("data1+data2+data3"+result);
	//					return result;  //����ƽ��ֵ 
	//				}
	//			}
	//		}
	//		return -1;
	//	}
	public double calDate(List<Float> data,int count){//TODO   ��Ǭ��
		 HashMap<String, Float> hash = new HashMap<String, Float>();  
	        for (int i = 0; i < data.size(); i++) {  
	            try {  
	  
	                if (!hash.isEmpty() && hash.containsKey(data.get(i))) {  
	                    hash.put(data.get(i).toString(), hash.get(data.get(i)) + 1);  
	                } else {  
	                    hash.put(data.get(i).toString(), 1f);  
	                }  
	            } catch (Exception e) {  
	  
	            }  
	        }  
//	        Set<String> set = hash.keySet();  
//	        for (String key : set) {  
//	            System.out.println(key + "==>" + hash.get(key));  
//	        }  
//	        System.out.println("--------------�����--------------");  
	    	List<Map.Entry<String, Float>> infoIds = new ArrayList<Map.Entry<String, Float>>(  
					hash.entrySet());
	    	// ����  
			Collections.sort(infoIds, new Comparator<Map.Entry<String, Float>>() {  
			    public int compare(Map.Entry<String, Float> o1,  
			            Map.Entry<String, Float> o2) {  
			        return (int) (o2.getValue()- o1.getValue());  
			    }  
			});  
			float [] values = new float[count];
			for (int i =0; i <infoIds.size(); i++) {  
			    Entry<String,Float> ent=infoIds.get(i);  
			    System.out.println(ent.getKey()+"==>"+ent.getValue());  
			    
				if(i<count){
					values[i] = Float.valueOf(infoIds.get(i).getKey());
				}
			}  

			
			System.out.println(Arrays.toString(values));
			float sum = 0 ;
			for(int i = 0; i< count;i++){
				sum+=values[i];
			}
			return sum/count;
			

	}


	/**
	 * ð������
	 * @param numbers
	 */
	public static void bubbleSort(int[] numbers) {   
	    int temp; // ��¼��ʱ�м�ֵ   
	    int size = numbers.length; // �����С   
	    for (int i = 0; i < size - 1; i++) {   
	        for (int j = i + 1; j < size; j++) {   
	            if (numbers[i] < numbers[j]) { // ����������λ��   
	                temp = numbers[i];   
	                numbers[i] = numbers[j];   
	                numbers[j] = temp;   
	            }   
	        }   
	    }   
	}  






	float maxData = 0;
	private View createMap(List<AirTestBean> list) throws Exception {
		if(list.get(list.size()-1).getGasDate()>maxData){
			maxData = list.get(list.size()-1).getGasDate();
		}

		String[] titles = new String[] { ""};
		Date [] time = new Date[list.size()]; 	//�������
		double [] gasdata = new double[list.size()];//�������
		for(int i= 0 ; i < list.size(); i ++){
			time[i]=new Date(list.get(i).getTime());
			gasdata[i]= Double.valueOf(list.get(i).getGasDate());
		}
		List<Date[]> x = new ArrayList<Date[]>();
		for (int i = 0; i < titles.length; i++) {
			x.add(time);//x����// ʱ��
		}
		List<double[]> values = new ArrayList<double[]>();
		values.add(gasdata);//y����//  ֵ
		int[] colors = new int[] { Color.GREEN};
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE}; 
		renderer = buildRenderer(colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
		}
		setChartSettings(renderer, "", "\t\t\t ", " ",System.currentTimeMillis()-10000,System.currentTimeMillis()+10000, 0,maxData,Color.LTGRAY, Color.LTGRAY);
		renderer.setXLabels(10);//���������� 										//1426608000000     1426694400000
		renderer.setYLabels(5);
		renderer.setShowGrid(true); //�����Ƿ���ͼ������ʾ����
		renderer.setXLabelsColor(Color.TRANSPARENT);
		renderer.setYLabelsColor(0, Color.TRANSPARENT);
		renderer.setZoomButtonsVisible(false);//���ÿ�������
		renderer.setPanEnabled(false);
		dataset = buildDateDataset(titles, x, values);
		return ChartFactory.getLineChartView (this, dataset, renderer);
	}

	public static Date StringFormatDate(String StrTime){
		String dateString = StrTime;  
		try  
		{  
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //2015-03-18 13:15:12
			Date date = sdf.parse(dateString);  
			return date;
		}  
		catch (Exception e)  
		{  
			return null;
		} 
	}


	/**
	 * Builds an XY multiple series renderer.
	 * 
	 * @param colors the series rendering colors
	 * @param styles the series point styles
	 * @return the XY multiple series renderers
	 */
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}
	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(2f);
		renderer.setMargins(new int[] { 20, 30, 15, 20 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			//			r.setDisplayChartValuesDistance(25);
			renderer.addSeriesRenderer(r);
		}
	}


	/**
	 * Builds an XY multiple dataset using the provided values.
	 * 
	 * @param titles the series titles
	 * @param xValues the values for the X axis
	 * @param yValues the values for the Y axis
	 * @return the XY multiple dataset
	 */
	protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
			List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		addXYSeries(dataset, titles, xValues, yValues, 0);
		return dataset;
	}

	public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
			List<double[]> yValues, int scale) {
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i], scale);
			double[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
	}
	protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
			String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}


	protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
			List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			TimeSeries series = new TimeSeries(titles[i]);
			Date[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
		return dataset;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		EmGpio.setGpioDataHigh(216);//������
		EmGpio.setGpioDataHigh(217);//�ص�Դ
		EmGpio.gpioUnInit();//�ͷ�gpio����

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		EmGpio.gpioInit();
		boolean out216 = EmGpio.setGpioOutput(217);//��Դ�������
		boolean out217 = EmGpio.setGpioOutput(216);//���ÿ������
		openSwitch();
	}

	/****************************************************��ͼ����END***************************************************/
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
	private boolean isClose = true;
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
