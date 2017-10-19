package com.tk.lfhl.activity;

import java.util.List;

import com.android.db.DBHelper;
import com.android.utils.ListUtils;
import com.android.utils.MyToask;
import com.tk.adapter.SimpleSpinnerAdapter;
import com.tk.lfhl.bean.PeopleInfo;
import com.tk.lfhl.bean.RoadWayInfo;

/**
 * 漏风方式
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 漏风方式
 * @author Administrator
 *
 */
public class VentilationLeakageActivity extends Activity implements OnClickListener,OnItemSelectedListener{
	private Button btn_LocalPositivePressure;//正压
	private Button btn_LocalNegativePressure;//负压
	private Button btn_test;//模拟测试
	private Button btn_ContinuousPositivePressure;//连续正压
	private Button btn_ContinuousNegativePressure;//连续负压
	private Spinner sp_road,sp_people;
	private DBHelper db;
	private String db_type;
	private TextView tv_fangan;
	private SimpleSpinnerAdapter roadSpinnerAdapter,sp_peopleAdapter;
	private List<String> roadInfo_list_string;
	private List<PeopleInfo> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ventilation_leakage);
		db = new DBHelper(getApplicationContext());
		btn_LocalPositivePressure = (Button) findViewById(R.id.btn_LocalPositivePressure);
		btn_LocalNegativePressure = (Button) findViewById(R.id.btn_LocalNegativePressure);
		btn_ContinuousPositivePressure = (Button) findViewById(R.id.btn_ContinuousPositivePressure);
		btn_ContinuousNegativePressure = (Button) findViewById(R.id.btn_ContinuousNegativePressure);
		btn_test = (Button) findViewById(R.id.btn_test);
		sp_road = (Spinner) findViewById(R.id.sp_road);
		sp_people = (Spinner) findViewById(R.id.sp_people);
		tv_fangan = (TextView) findViewById(R.id.tv_fangan);
		btn_LocalPositivePressure.setOnClickListener(this);//正压漏风
		btn_LocalNegativePressure.setOnClickListener(this);//负压漏风
		btn_ContinuousPositivePressure.setOnClickListener(this);//连续正压漏风
		btn_ContinuousNegativePressure.setOnClickListener(this);//连续负压漏风
		btn_test.setOnClickListener(this);//模拟测试
		roadInfo_list_string  = db.findRoad_Way_name();

		sp_road.setOnItemSelectedListener(this);
		roadSpinnerAdapter = new SimpleSpinnerAdapter(roadInfo_list_string,getApplicationContext());
		sp_road.setAdapter(roadSpinnerAdapter);
		sp_road.setSelection(roadInfo_list_string.indexOf(MainActivity.last_id.get(0)));


		list = db.findTablePeople();
		List<String> listName = ListUtils.formatPeopleInfoNameList(list);
		sp_peopleAdapter = new SimpleSpinnerAdapter(listName, getApplicationContext());
		sp_people.setAdapter(sp_peopleAdapter);
		sp_people.setOnItemSelectedListener(this);
		sp_people.setSelection(listName.indexOf(MainActivity.last_id.get(1)));
	}
	@Override
	public void onClick(View v) {
		
		if(v==btn_test){
			startActivity(new Intent(getApplicationContext(), TestActivity.class));
			return;
		}
		
		
		Intent mIntent = new Intent(getApplicationContext(), SelectionDetectionSchemeActivity.class);
		if(v == btn_LocalPositivePressure){
//			MyToask.showTextToast(btn_LocalPositivePressure.getText().toString(), getApplicationContext(), 0);
			mIntent.putExtra("bg_type",R.drawable.localpositivepressure);
			mIntent.putExtra("btn_content",message(1));
			mIntent.putExtra("type_id",1);
		}
		if(v == btn_LocalNegativePressure){
//			MyToask.showTextToast(btn_LocalNegativePressure.getText().toString(), getApplicationContext(), 0);
			mIntent.putExtra("bg_type",R.drawable.localnegativepressure);
			mIntent.putExtra("btn_content",message(2));
			mIntent.putExtra("type_id",2);
		}
		if(v == btn_ContinuousPositivePressure){
			MyToask.showTextToast(btn_ContinuousPositivePressure.getText().toString(), getApplicationContext(), 0);
			mIntent.putExtra("bg_type",R.drawable.continuouspositivepressure);
			mIntent.putExtra("btn_content",message(3));
			mIntent.putExtra("type_id",3);
		}
		if(v == btn_ContinuousNegativePressure){
			MyToask.showTextToast(btn_ContinuousNegativePressure.getText().toString(), getApplicationContext(), 0);
			mIntent.putExtra("bg_type",R.drawable.continuousnegativepressure);
			mIntent.putExtra("btn_content",message(4));
			mIntent.putExtra("type_id",4);
		}
		startActivity(mIntent);
		
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		db_type = db.findLAST_TABLE().get(2);
		if(db_type.equals("")){
			tv_fangan.setText("请选择漏风检测方案");
		}else{
			tv_fangan.setText("已选择："+db_type);
			if(db_type.equals(message(1))){
				btn_LocalPositivePressure.setTextColor(Color.RED);
				btn_LocalNegativePressure.setTextColor(Color.BLACK);
				btn_ContinuousPositivePressure.setTextColor(Color.BLACK);
				btn_ContinuousNegativePressure.setTextColor(Color.BLACK);
			}if(db_type.equals(message(2))){
				btn_LocalPositivePressure.setTextColor(Color.BLACK);
				btn_LocalNegativePressure.setTextColor(Color.RED);
				btn_ContinuousPositivePressure.setTextColor(Color.BLACK);
				btn_ContinuousNegativePressure.setTextColor(Color.BLACK);
			}if(db_type.equals(message(3))){
				btn_LocalPositivePressure.setTextColor(Color.BLACK);
				btn_LocalNegativePressure.setTextColor(Color.BLACK);
				btn_ContinuousPositivePressure.setTextColor(Color.RED);
				btn_ContinuousNegativePressure.setTextColor(Color.BLACK);
			}if(db_type.equals(message(4))){
				btn_LocalPositivePressure.setTextColor(Color.BLACK);
				btn_LocalNegativePressure.setTextColor(Color.BLACK);
				btn_ContinuousPositivePressure.setTextColor(Color.BLACK);
				btn_ContinuousNegativePressure.setTextColor(Color.RED);
			}

		}
	}

	public static String message(int type){
		String message = "";
		switch (type) {
		case 1:
			message = "正压漏风";
			break;
		case 2:
			message = "负压漏风";
			break;
		case 3:
			message = "连续正压漏风";
			break;
		case 4:
			message = "连续负压漏风";
			break;
		default:
			break;
		}
		return message;
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent==sp_road){
			try {
				TextView tv = (TextView) view;
				List<RoadWayInfo> list = 	db.findRoad_Way_name(tv.getText().toString());
				RoadWayInfo info = list.get(0);
				db.updateLAST_TABLE(info.getRoadwayName(), MainActivity.last_id.get(1), MainActivity.last_id.get(2));
				MainActivity.last_id = db.findLAST_TABLE();//查询最后一次的信息
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "含有特殊字符,无法选中", 0).show();
				e.printStackTrace();
			}
		}
		if(parent==sp_people){
			TextView tv = (TextView) view;
			list = db.findTablePeople();
			PeopleInfo info = 	ListUtils.findPeopleByName(tv.getText().toString(), list);
			db.updateLAST_TABLE(MainActivity.last_id.get(0), info.getUserName(), MainActivity.last_id.get(2));
			MainActivity.last_id = db.findLAST_TABLE();//查询最后一次的信息
		}		

	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
