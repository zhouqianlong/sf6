package com.tk.lfhl.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.android.db.DBHelper;
import com.android.utils.MyToask;
import com.tk.adapter.SimpleSpinnerAdapter;
import com.tk.lfhl.bean.RoadWayInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
/**
 * 巷道信息
 * @author Administrator
 *
 */
public class RoadwayInformationActivity extends Activity implements OnClickListener,OnItemSelectedListener,TextWatcher{
	private TextView tv_height ,tv_mj,tv_gasQ,et_distance;//高度   面积 气体释放流量
	private Spinner sp_diaoyong,sp_type;//下拉列表
	private DBHelper db ;
	private List<String> roadInfo_list_string;
	private SimpleSpinnerAdapter roadSpinnerAdapter;
	private EditText et_roadwayName,et_supportingForm,et_measurementOfAirLeakageLocation,et_windSpeed,et_hole_area,et_hole_length;
	private EditText et_height;//高度
	private EditText et_width;//宽度
	private Button btn_save ,btn_back,btn_delete;
	private String [] type = {"梯形巷道","矩形巷道","半圆拱巷道","三心拱巷道"};
	private String [] heighType = {"梯形半高处宽度B(m)","矩形宽度B（m）","半圆拱半高处宽度B（m）","三心拱半高处宽度B（m）"};
	private RoadWayInfo info = null;
	private List<String> nameList = new ArrayList<String>();
	private DecimalFormat df = new DecimalFormat("0.00");
	private int typePositon = 0; //巷道形状  0-3 "梯形巷道","矩形巷道","半圆拱巷道","三心拱巷道"
	private float mj = 0;//面积
	private float q = 0;//气体释放流量
	private float minDistance = 0 ;//最小距离
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roadway_information);
		initView();
	
	}
	
	private void initView() {
		db = new DBHelper(getApplicationContext());
		roadInfo_list_string  = db.findRoad_Way_name();
		et_roadwayName = (EditText) findViewById(R.id.et_roadwayName);
		et_height = (EditText) findViewById(R.id.et_height);
		et_height.addTextChangedListener(this);
		et_width = (EditText) findViewById(R.id.et_width);
		et_width.addTextChangedListener(this);
		et_supportingForm = (EditText) findViewById(R.id.et_supportingForm);
		et_windSpeed = (EditText) findViewById(R.id.et_windSpeed);
		et_windSpeed.addTextChangedListener(this);
		et_measurementOfAirLeakageLocation = (EditText) findViewById(R.id.et_measurementOfAirLeakageLocation);
		et_hole_area = (EditText) findViewById(R.id.et_hole_area);
		et_hole_length = (EditText) findViewById(R.id.et_hole_length);
		btn_save= (Button) findViewById(R.id.btn_save);
		btn_back= (Button) findViewById(R.id.btn_back);
		btn_delete= (Button) findViewById(R.id.btn_delete);
		sp_diaoyong = (Spinner) findViewById(R.id.sp_diaoyong);
		sp_type = (Spinner) findViewById(R.id.sp_type);
		tv_height = (TextView) findViewById(R.id.tv_height);
		tv_mj = (TextView) findViewById(R.id.tv_mj);
		tv_gasQ = (TextView) findViewById(R.id.tv_gasQ);
		et_distance = (TextView) findViewById(R.id.et_distance);
		for(String str:type){//添加默认形状
			nameList.add(str);
		}
		sp_type.setAdapter(new SimpleSpinnerAdapter(nameList,getApplicationContext()));
		sp_type.setOnItemSelectedListener(this);
		btn_save.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		sp_diaoyong.setOnItemSelectedListener(this);
		roadSpinnerAdapter = new SimpleSpinnerAdapter(roadInfo_list_string,getApplicationContext());
		sp_diaoyong.setAdapter(roadSpinnerAdapter);
		sp_diaoyong.setSelection(roadInfo_list_string.indexOf(MainActivity.last_id.get(0)));
 
	}
	//string 为要判断的字符串 
	public static boolean isConSpeCharacters(String string){ 
	    if(string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*","").length()==0){ 
	        //不包含特殊字符 
	        return false; 
	    } 
	    return true; 
	} 
	@Override
	public void onClick(View v) {
		if(v == btn_save){
			String roadwayName =  et_roadwayName.getText().toString();
			String supportingForm = et_supportingForm.getText().toString();
			String measurementOfAirLeakageLocation =et_measurementOfAirLeakageLocation.getText().toString();
			String windSpeed= et_windSpeed.getText().toString();
			String hole_area  =et_hole_area.getText().toString();
			String hole_length = et_hole_length.getText().toString();
			String height = et_height.getText().toString();
			String width = et_width.getText().toString();
			if(roadwayName.isEmpty()){ 
				MyToask.showTextToast("巷道名称不能为空", getApplicationContext(), 0);
				return ;
			}
			
			if(supportingForm.isEmpty()){
				MyToask.showTextToast("支护形式不能为空", getApplicationContext(), 0);
				return ;
			}
			if(height.isEmpty()){
				MyToask.showTextToast("巷道净高度不能为空", getApplicationContext(), 0);
				return ;
			}
			if(width.isEmpty()){
				MyToask.showTextToast("宽度不能为空", getApplicationContext(), 0);
				return ;
			}
//			if(measurementOfAirLeakageLocation.isEmpty()){
//				MyToask.showTextToast("测漏风位置不能为空", getApplicationContext(), 0);
//				return ;
//			}
			if(windSpeed.isEmpty()){
				MyToask.showTextToast("风速不能为空", getApplicationContext(), 0);
				return ;
			}
//			if(hole_area.isEmpty()){
//				MyToask.showTextToast("井巷端面积不能为空", getApplicationContext(), 0);
//				return ;
//			}
//			if(hole_length.isEmpty()){
//				MyToask.showTextToast("井巷周界长度不能为空", getApplicationContext(), 0);
//				return ;
//			}
			
			if(isConSpeCharacters(roadwayName)==true){
				MyToask.showTextToast("巷道名称不能输入非法字符", getApplicationContext(), 0);
				return ;
			}
			if(isConSpeCharacters(supportingForm)==true){
				MyToask.showTextToast("支护形式不能输入非法字符", getApplicationContext(), 0);
				return ;
			}
			
			if(db.save(new RoadWayInfo(roadwayName, supportingForm, measurementOfAirLeakageLocation, windSpeed,String.valueOf(typePositon),height,width, hole_area, hole_length))){
				MyToask.showTextToast("保存成功！", getApplicationContext(), 0);
			}else{
				MyToask.showTextToast("巷道名已存在，请修改巷道名称后保存！", getApplicationContext(), 0);
			}
			roadSpinnerAdapter.setList(db.findRoad_Way_name());
			roadSpinnerAdapter.notifyDataSetChanged();

		}
		if(v== btn_back){
			finish();
		}
		if(v== btn_delete){ 
			if(info!=null){
				new AlertDialog.Builder(RoadwayInformationActivity.this).setTitle("删除提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						db.deleteRoad_WayForName(info.getRoadwayName());
						info = null;
						MyToask.showTextToast("删除成功！", getApplicationContext(), 0);
						roadSpinnerAdapter.setList(db.findRoad_Way_name());
						roadSpinnerAdapter.notifyDataSetChanged();

					}
				}).setNegativeButton("取消", null).setMessage("确定删除“"+info.getRoadwayName()+"”的信息?").show();
			}else{
				MyToask.showTextToast("请选择巷道，然后删除！", getApplicationContext(), 0);
			}

		}
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent==sp_diaoyong){
			TextView tv = (TextView) view;
			List<RoadWayInfo> list = 	db.findRoad_Way_name(tv.getText().toString());
			if(list.size()>0){
				info = list.get(0);
				list = null;
				db.updateLAST_TABLE(info.getRoadwayName(), MainActivity.last_id.get(1), MainActivity.last_id.get(2));
				et_roadwayName.setText(info.getRoadwayName());
				et_supportingForm.setText(info.getSupportingForm());
				et_measurementOfAirLeakageLocation.setText(info.getMeasurementOfAirLeakageLocation());
				et_windSpeed.setText(info.getWindSpeed());
				sp_type.setSelection(Integer.valueOf(info.getShape()));
				et_height.setText(info.getHeight());
				et_width.setText(info.getWidth());
				et_hole_area.setText(info.getHole_area());
				et_hole_length.setText(info.getHole_length());
				typePositon = Integer.valueOf(info.getShape());
				tv_height.setText("4."+heighType[position]);
				calcArea();
				calcQ();
				calcMindistance();
			} 
		}
		if(parent==sp_type){
			typePositon = position;
			tv_height.setText("4."+heighType[position]);
			calcArea();
			calcQ();
			calcMindistance();
		}
	}
	
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		MyToask.showTextToast("巷道信息没有数据,请添加数据", getApplicationContext(), 0);
		et_roadwayName.setText("");
		et_supportingForm.setText("");
		et_measurementOfAirLeakageLocation.setText("");
		et_windSpeed.setText("");
		sp_type.setSelection(0);
		et_height.setText("");
		et_width.setText("");
		et_hole_area.setText("");
		et_hole_length.setText("");
		db.updateLAST_TABLE("", MainActivity.last_id.get(1), MainActivity.last_id.get(2));
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		calcArea();
		calcQ();
		calcMindistance();
	}
	@Override
	public void afterTextChanged(Editable s) {
		
	}
	/**
	 * 计算面积
	 */
	public void calcArea(){
		float height = 0;
		float width = 0;
		try {
			height = Float.valueOf(et_height.getText().toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		try {
			width = Float.valueOf(et_width.getText().toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if(typePositon==0||typePositon==1){//矩形和梯形S=H*B
			mj = height*width;
		}else if(typePositon==2){//半圆
			mj = width*(height-0.11f*width);
		}else{//三心
			mj = width*(height-0.07f*width);
		}
		tv_mj.setText(df.format(mj));
	}
	/**
	 * 计算气体释放流量
	 */
	public void calcQ(){
		//5* 10-6 *S*V *103
		float speed = 0;
		try {
			speed = Float.valueOf(et_windSpeed.getText().toString());
		} catch (NumberFormatException e) {
			speed=0;
		}
		q = 5*0.000001f*speed*60*1000*mj;
		tv_gasQ.setText(df.format(q));
		
		
	}
	/**
	 * 计算最小距离 L=32*S/U;
	 */
	public void calcMindistance(){
		double u = Math.sqrt(mj);
		if(typePositon==0||typePositon==1){//梯形  矩形 
			u = u *4.16;
		}else if(typePositon==3){//三心
			u = u *4.10;
		} else{
			u = u *3.84;
		}
		if(u==0){
			minDistance = 0;
		}else{
			
			minDistance =  (float) ((float) (32f*mj)/u); 
		}
		et_distance.setText(df.format(minDistance));
		
	}
}
