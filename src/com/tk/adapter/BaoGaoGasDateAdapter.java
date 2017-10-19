package com.tk.adapter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.db.DBHelper;
import com.ramy.minervue.service.GasDetector;
import com.tk.lfhl.activity.AirLeakageTestActivity;
import com.tk.lfhl.activity.BaoGaoActivity;
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
import android.widget.TextView;
import android.widget.Toast;

public class BaoGaoGasDateAdapter extends BaseAdapter{
	protected static final String TAG = "AirLeakageTestActivity";
	private Context mContent;
	private List<GasDate> list;
	public BaoGaoGasDateAdapter(Context context,List<GasDate> list) {
		this.mContent = context;
		this.list = list;
	}
	@Override
	public int getCount() {
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MyHolder mHolder =null;
		if(convertView==null){
			mHolder = new MyHolder();
			convertView = LayoutInflater.from(mContent).inflate(R.layout.listview_baogao_gas_more_xml, null);
			mHolder.tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);
			mHolder.tv_gas_date = (TextView) convertView.findViewById(R.id.tv_gas_date);
			convertView.setTag(mHolder);
		}else{
			mHolder  = (MyHolder) convertView.getTag();
		}
		mHolder.tv_msg.setText(list.get(position).getMsg());
		mHolder.tv_gas_date.setText(list.get(position).getGasDate()+"ppm");
		return convertView;
	}
	class MyHolder{
		TextView tv_msg;
		TextView tv_gas_date;
	}
	public void setList(List<GasDate> list){
		this.list = list;
		notifyDataSetChanged();
	}

}
