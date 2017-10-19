package com.tk.adapter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.db.DBHelper;
import com.ramy.minervue.service.GasDetector;
import com.tk.lfhl.activity.AirLeakageTestActivity;
import com.tk.lfhl.activity.BaoGaoActivity;
import com.tk.lfhl.activity.MainActivity;
import com.tk.lfhl.activity.R;
import com.tk.lfhl.bean.AirTestBean;
import com.tk.lfhl.bean.GasDate;
import com.tk.lfhl.bean.PeopleInfo;
import com.tk.lfhl.bean.RizhiInfo;
import com.tk.lfhl.bean.RoadWayInfo;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AirTestAdapter extends BaseAdapter{
	public boolean HIDEN = false;
	public boolean SHOW = true;
	protected static final String TAG = "AirLeakageTestActivity";
	private GasDetector detector =  GasDetector.getInstance();
	private Context mContent;
	private List<AirTestBean> list;
	private List<GasDate> gasDateList;
	private boolean isopen = false;
	public AirLeakageTestActivity activity;
	private int count=0 ;//完成次数
	public List<AirTestBean>  getTestList(){
		return list;
	}

	public void deleteCount() {
		this.count--;
	}
	public AirTestAdapter(Context context,List<AirTestBean> list, AirLeakageTestActivity activity) {
		this.mContent = context;
		this.list = list;
		this.activity = activity;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	int onclickID = 0;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MyHolder mHolder =null;
		if(convertView==null){
			mHolder = new MyHolder();
			convertView = LayoutInflater.from(mContent).inflate(R.layout.activity_air_leakage_test_item, null);
			mHolder.tv_info =  (TextView) convertView.findViewById(R.id.tv_info);
			mHolder.btn_start_test =  (Button) convertView.findViewById(R.id.btn_start_test);
			convertView.setTag(mHolder);
		}else{
			mHolder  = (MyHolder) convertView.getTag();
		}

		if(list.get(position).getGasDate()>=0){
			mHolder.btn_start_test.setEnabled(false);
			mHolder.btn_start_test.setText("SF6："+list.get(position).getGasDate()+"ppm");
		}else{
			if(list.get(position).getGasDate()==-1&&onclickID!=position ){
				mHolder.btn_start_test.setEnabled(true);
				mHolder.btn_start_test.setText("开始测试");
			}
		}
		mHolder.tv_info.setText(list.get(position).getInfo());
		mHolder.btn_start_test.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.setbtn_creategaogaoVisibility(View.GONE);
				Button btn = (Button) v;
				btn.setText("测试中....");
				btn.setEnabled(false);
				activity.startMap(position);
				onclickID = position;


			}
		});
		return convertView;
	}


	class MyHolder{
		TextView tv_info;
		Button btn_start_test;
	}

	public void setList(List<AirTestBean> list){
		this.list = list;
		notifyDataSetChanged();
	}
	public Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==1){
				//				btn_send1.setEnabled(false);
				//				btn_start2.setEnabled(true);
			}
			if(msg.what==2){
				//去除list里头的气体浓度和描述
				gasDateList = new ArrayList<GasDate>();
				for(int i =0 ; i < list.size();i++){
					gasDateList.add(new GasDate(String.valueOf((i+1)),list.get(i).getGasDate()));
				}
				final List<RoadWayInfo> roadWayInfos = activity.getroadWayInfos();
				final List<PeopleInfo> peopleInfos =  activity.getPeopleInfos();
				final DBHelper db = activity.getDb();
				//				btn_send2.setEnabled(false);
				new AlertDialog.Builder(mContent).setMessage("导出报告？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						RizhiInfo info = new RizhiInfo();
						info.setTEST_TIME(new Date().getTime()+"");
						info.setGAS_DATE("");
						info.setHOLE_AREA(roadWayInfos.get(0).getHole_area());
						info.setLOUFENG_TYPE(activity.getType());
						info.setROADWAY_NAME(roadWayInfos.get(0).getRoadwayName());
						info.setSUPPORTING_FORM(roadWayInfos.get(0).getSupportingForm());
						info.setTEST_NAME(peopleInfos.get(0).getUserName());
						info.setMeasurementOfAirLeakageLocation(roadWayInfos.get(0).getMeasurementOfAirLeakageLocation());
						info.setWindSpeed(roadWayInfos.get(0).getWindSpeed());

						info.setROAD_SHAPE(roadWayInfos.get(0).getShape());
						info.setROAD_HEIGHT(roadWayInfos.get(0).getHeight());
						info.setROAD_WIDTH(roadWayInfos.get(0).getWidth());
						info.setTEST_BUMEN(peopleInfos.get(0).getDepartMent());
						info.setTEST_ZHIWU(peopleInfos.get(0).getJob());

						info.setHole_length(roadWayInfos.get(0).getHole_length());
						info.setList(gasDateList);
						db.saveRIZHI_INFO(info);
						Intent mIntent = new Intent(activity, BaoGaoActivity.class);
						mIntent.putExtra("RizhiInfo",info);
						activity.startActivity(mIntent);
						Toast.makeText(activity, activity.getType()+peopleInfos.get(0).getUserName()+"成功！"+db.findRIZHI_INFO().size(), 0).show();
					}
				}).show();
			}
			super.handleMessage(msg);
		}
	};

}
