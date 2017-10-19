package com.tk.lfhl.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.android.db.DBHelper;
import com.tk.lfhl.bean.RizhiInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListBaoGaoActivity extends Activity{
	List<RizhiInfo> list;
	DBHelper db;
	ListView lv_date;
	MyAdapter myAdapter;
	TextView tv_file_list_nothing_to_show;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baogao_list);
		lv_date = (ListView) findViewById(R.id.lv_date);
		tv_file_list_nothing_to_show = (TextView) findViewById(R.id.tv_file_list_nothing_to_show);
		View include = findViewById(R.id.il_title);
		TextView tv_title = (TextView) include.findViewById(R.id.tv_title);
		tv_title.setText("¼ì²â±¨¸æ");

		db = new DBHelper(getApplicationContext());
		list = db.findRIZHI_INFO();
		myAdapter = new MyAdapter(list,getApplicationContext());
		lv_date.setAdapter(myAdapter);
		lv_date.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//					Toast.makeText(getApplicationContext(), list.get(position).getId()+"//"+list.get(position).getTEST_TIME(), 0).show();
				Intent mIntent = new Intent(ListBaoGaoActivity.this, BaoGaoActivity.class);
				mIntent.putExtra("RizhiInfo", list.get(position));
				startActivity(mIntent);
			}
		});
	}

	@Override
	protected void onResume() {
	
		list = db.findRIZHI_INFO();
		if(list.size()==0){
			tv_file_list_nothing_to_show.setVisibility(View.VISIBLE);
		}else{
			tv_file_list_nothing_to_show.setVisibility(View.GONE);
		}
		myAdapter.setList(list);
		myAdapter.notifyDataSetChanged();
		super.onResume();

	}
	class MyAdapter extends BaseAdapter{
		List<RizhiInfo> list;
		Context context;
		@SuppressLint("SimpleDateFormat")
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		public MyAdapter(List<RizhiInfo> list,Context context){
			this.list = list;
			this.context = context;
		}
		public void setList(List<RizhiInfo> list){
			this.list = list;
		}
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if(convertView==null){
				holder = new Holder();
				LayoutInflater inflater=LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.activity_baogao_list_item, null);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}

			RizhiInfo info = list.get(position);
			String times = sdf.format(new Date(Long.valueOf(info.getTEST_TIME())));
			holder.tv_name.setText((position+1)+":"+info.getROADWAY_NAME()+""+times+"-"+info.getTEST_NAME()+"");
			return convertView;
		}
	}
	class Holder{
		TextView tv_name;
	}
}
