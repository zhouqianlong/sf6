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

public class TestActivity extends Activity implements OnClickListener{
	protected static final String TAG = "AirLeakageTestActivity";
	public  boolean isFlag = false;
	private List<Float> calDataList = new ArrayList<Float>();//计算稳定数据的集合
	private TextView tv_sf6ppm;//浓度提示   
	private TextView tv_sf6time;//剩余检测时间
	private LinearLayout ll_map;//局部负压菜单、局部正压菜单、折线图
	private XYMultipleSeriesDataset dataset = null;
	private XYMultipleSeriesRenderer renderer = null;
	private Button btn_qibang;//气泵开关控制
	private DecimalFormat   df   =new   DecimalFormat("0.00");  
	public int chancun;
	private TextView tv_msg;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	List<AirTestBean> list = new ArrayList<AirTestBean>();
	public long sf6time = 0;
	public Button btn_stop_start;
	public Button btn_clear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		tv_msg = (TextView) findViewById(R.id.tv_msg);
		ll_map = (LinearLayout) findViewById(R.id.ll_map);
		tv_sf6ppm = (TextView) findViewById(R.id.tv_sf6ppm);
		tv_sf6time = (TextView) findViewById(R.id.tv_sf6time);
		btn_qibang = (Button) findViewById(R.id.btn_qibang);
		btn_stop_start = (Button) findViewById(R.id.btn_stop_start);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		
		btn_stop_start.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		btn_qibang.setOnClickListener(this);
		startMap();

	}

	protected void onDestroy() {
		super.onDestroy();
		isFlag = false;
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	};
	float lastData = -1;//最后一次检测到的值
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				tv_sf6ppm.setText("SF6浓度："+(int)MainActivity.getInstance.value/MainActivity.getInstance.xsGasValue+" ppm");//value/xsGasValue
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
				
				tv_sf6ppm.setText("SF6浓度："+msg.arg1/100f+" ppm");//value/xsGasValue
				if(lastData==-1){
					lastData = msg.arg1/100f;
					tv_msg.setText(tv_msg.getText()+"("+count+")\t"+msg.arg1/100f+" ppm\t"+sdf.format(new Date())+"\n", TextView.BufferType.EDITABLE);
				}
				if(lastData!=msg.arg1/100f){
					tv_msg.setText(tv_msg.getText()+"("+count+")\t"+msg.arg1/100f+" ppm\t"+sdf.format(new Date())+"\n", TextView.BufferType.EDITABLE);
					lastData = msg.arg1/100f;
				}

			}
		 
			if(msg.what==6){
				try {
					new AlertDialog.Builder(TestActivity.this).setMessage("传感器通讯异常,请联系管理员,").setPositiveButton("确定",null).show();
					isFlag = false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if(msg.what==80){
				String strTime = (String) msg.obj;
				tv_sf6time.setText("下次检测倒计时:"+strTime);
			}
		}
	};
	boolean flg = false;

	boolean isStop = false;//是否暂停  true 停止   false 启动
	@Override
	public void onClick(View v) { 

	  
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
		
		if(v==btn_stop_start){
			if(btn_stop_start.getText().equals("暂停")){
				isStop = true;
				btn_stop_start.setText("启动");//暂停
			}else{
				isStop = false;
				btn_stop_start.setText("暂停");//启动
			}
		}
		
		
		if(btn_clear==v){
			tv_msg.setText("");
		}

	}
	public boolean HIDEN = false;
	public boolean SHOW = true;
 
 
	/****************************************************画图代码START***************************************************/
	public static int X_NUMBER = 6;//横坐标数量  时间刻度
	public static int Y_NUMBER = 5;//纵坐标数量	 浓度刻度
	public static double X_START_GAS = 0.0;
	public static double X_END_GAS = 100.0;
	int count = 0;
	public void startMap(){//传递按钮  通过按钮反馈当前测试情况                    //传递当前数据的位置 
		count=0;
		isFlag = true; 
		tv_sf6time.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			private int errorCount;

			@Override
			public void run() {
				try {
					
					list.add(new AirTestBean("",0, System.currentTimeMillis()-1000));
					while(isFlag){
						if(isStop){
							Thread.sleep(100);
							continue;
						}
						float num = getGasData();//获取浓度
						if(num==-1||num==-100){
							errorCount++;
							if(errorCount>=3){
								mHandler.sendEmptyMessage(6);//连续错误3次退出
							}else{
								continue;
							}

						}
						count++;
						
						list.add(new AirTestBean("",num, System.currentTimeMillis()));
						Message msg = Message.obtain();
						msg.what=2;
						msg.obj = list;
						msg.arg1 = (int) (num*100);

						mHandler.sendMessage(msg);//绘制一次图形
						calDataList.add((float)num);
						errorCount= 0;
						startTimeCount();//开启计时器
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

	//计时器
	private void startTimeCount() {
		if(flg == false){
			flg = true;
			new Thread(new Runnable() {//时间倒计时检测

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
						double calResult = 0;
						calResult=calDate(calDataList,2);
						if(calResult>=0){//测试稳定成功保存数据
							Message msg2 = Message.obtain();
							msg2.obj = calResult;
							msg2.what=3;
							mHandler.sendMessage(msg2);
							calDataList.clear();
						} 
					flg = false;
				}
			}).start();
		}
	}

	public float getGasData(){
		return MainActivity.getInstance.getSF6GasData();//模拟测试浓度
//		return  (int)(Math.random()*2)+1;//模拟测试浓度
	}
 
	public double calDate(List<Float> data,int count){
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
	    	List<Map.Entry<String, Float>> infoIds = new ArrayList<Map.Entry<String, Float>>(  
					hash.entrySet());
	    	// 排序  
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




	float maxData = 0;
	private View createMap(List<AirTestBean> list) throws Exception {
		if(list.get(list.size()-1).getGasDate()>maxData){
			maxData = list.get(list.size()-1).getGasDate();
		}

		String[] titles = new String[] { ""};
		Date [] time = new Date[list.size()]; 	//表格数据
		double [] gasdata = new double[list.size()];//表格数据
		for(int i= 0 ; i < list.size(); i ++){
			time[i]=new Date(list.get(i).getTime());
			gasdata[i]= Double.valueOf(list.get(i).getGasDate());
		}
		List<Date[]> x = new ArrayList<Date[]>();
		for (int i = 0; i < titles.length; i++) {
			x.add(time);//x坐标// 时间
		}
		List<double[]> values = new ArrayList<double[]>();
		values.add(gasdata);//y坐标//  值
		int[] colors = new int[] { Color.GREEN};
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE}; 
		renderer = buildRenderer(colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
		}
		setChartSettings(renderer, "", "\t\t\t ", " ",System.currentTimeMillis()-10000,System.currentTimeMillis()+10000, 0,maxData,Color.LTGRAY, Color.LTGRAY);
		renderer.setXLabels(10);//横坐标数量 										//1426608000000     1426694400000
		renderer.setYLabels(5);
		renderer.setShowGrid(true); //设置是否在图表中显示网格
		renderer.setXLabelsColor(Color.TRANSPARENT);
		renderer.setYLabelsColor(0, Color.TRANSPARENT);
		renderer.setZoomButtonsVisible(false);//设置可以缩放
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
		EmGpio.setGpioDataHigh(216);//关气泵
		EmGpio.setGpioDataHigh(217);//关电源
		EmGpio.gpioUnInit();//释放gpio控制

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		EmGpio.gpioInit();
		boolean out216 = EmGpio.setGpioOutput(217);//电源开关输出
		boolean out217 = EmGpio.setGpioOutput(216);//气泵开关输出
		openSwitch();
	}

	/****************************************************画图代码END***************************************************/
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
