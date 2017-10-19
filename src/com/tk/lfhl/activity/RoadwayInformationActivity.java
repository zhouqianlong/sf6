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
 * �����Ϣ
 * @author Administrator
 *
 */
public class RoadwayInformationActivity extends Activity implements OnClickListener,OnItemSelectedListener,TextWatcher{
	private TextView tv_height ,tv_mj,tv_gasQ,et_distance;//�߶�   ��� �����ͷ�����
	private Spinner sp_diaoyong,sp_type;//�����б�
	private DBHelper db ;
	private List<String> roadInfo_list_string;
	private SimpleSpinnerAdapter roadSpinnerAdapter;
	private EditText et_roadwayName,et_supportingForm,et_measurementOfAirLeakageLocation,et_windSpeed,et_hole_area,et_hole_length;
	private EditText et_height;//�߶�
	private EditText et_width;//���
	private Button btn_save ,btn_back,btn_delete;
	private String [] type = {"�������","�������","��Բ�����","���Ĺ����"};
	private String [] heighType = {"���ΰ�ߴ����B(m)","���ο��B��m��","��Բ����ߴ����B��m��","���Ĺ���ߴ����B��m��"};
	private RoadWayInfo info = null;
	private List<String> nameList = new ArrayList<String>();
	private DecimalFormat df = new DecimalFormat("0.00");
	private int typePositon = 0; //�����״  0-3 "�������","�������","��Բ�����","���Ĺ����"
	private float mj = 0;//���
	private float q = 0;//�����ͷ�����
	private float minDistance = 0 ;//��С����
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
		for(String str:type){//���Ĭ����״
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
	//string ΪҪ�жϵ��ַ��� 
	public static boolean isConSpeCharacters(String string){ 
	    if(string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*","").length()==0){ 
	        //�����������ַ� 
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
				MyToask.showTextToast("������Ʋ���Ϊ��", getApplicationContext(), 0);
				return ;
			}
			
			if(supportingForm.isEmpty()){
				MyToask.showTextToast("֧����ʽ����Ϊ��", getApplicationContext(), 0);
				return ;
			}
			if(height.isEmpty()){
				MyToask.showTextToast("������߶Ȳ���Ϊ��", getApplicationContext(), 0);
				return ;
			}
			if(width.isEmpty()){
				MyToask.showTextToast("��Ȳ���Ϊ��", getApplicationContext(), 0);
				return ;
			}
//			if(measurementOfAirLeakageLocation.isEmpty()){
//				MyToask.showTextToast("��©��λ�ò���Ϊ��", getApplicationContext(), 0);
//				return ;
//			}
			if(windSpeed.isEmpty()){
				MyToask.showTextToast("���ٲ���Ϊ��", getApplicationContext(), 0);
				return ;
			}
//			if(hole_area.isEmpty()){
//				MyToask.showTextToast("������������Ϊ��", getApplicationContext(), 0);
//				return ;
//			}
//			if(hole_length.isEmpty()){
//				MyToask.showTextToast("�����ܽ糤�Ȳ���Ϊ��", getApplicationContext(), 0);
//				return ;
//			}
			
			if(isConSpeCharacters(roadwayName)==true){
				MyToask.showTextToast("������Ʋ�������Ƿ��ַ�", getApplicationContext(), 0);
				return ;
			}
			if(isConSpeCharacters(supportingForm)==true){
				MyToask.showTextToast("֧����ʽ��������Ƿ��ַ�", getApplicationContext(), 0);
				return ;
			}
			
			if(db.save(new RoadWayInfo(roadwayName, supportingForm, measurementOfAirLeakageLocation, windSpeed,String.valueOf(typePositon),height,width, hole_area, hole_length))){
				MyToask.showTextToast("����ɹ���", getApplicationContext(), 0);
			}else{
				MyToask.showTextToast("������Ѵ��ڣ����޸�������ƺ󱣴棡", getApplicationContext(), 0);
			}
			roadSpinnerAdapter.setList(db.findRoad_Way_name());
			roadSpinnerAdapter.notifyDataSetChanged();

		}
		if(v== btn_back){
			finish();
		}
		if(v== btn_delete){ 
			if(info!=null){
				new AlertDialog.Builder(RoadwayInformationActivity.this).setTitle("ɾ����ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						db.deleteRoad_WayForName(info.getRoadwayName());
						info = null;
						MyToask.showTextToast("ɾ���ɹ���", getApplicationContext(), 0);
						roadSpinnerAdapter.setList(db.findRoad_Way_name());
						roadSpinnerAdapter.notifyDataSetChanged();

					}
				}).setNegativeButton("ȡ��", null).setMessage("ȷ��ɾ����"+info.getRoadwayName()+"������Ϣ?").show();
			}else{
				MyToask.showTextToast("��ѡ�������Ȼ��ɾ����", getApplicationContext(), 0);
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
		MyToask.showTextToast("�����Ϣû������,���������", getApplicationContext(), 0);
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
	 * �������
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
		if(typePositon==0||typePositon==1){//���κ�����S=H*B
			mj = height*width;
		}else if(typePositon==2){//��Բ
			mj = width*(height-0.11f*width);
		}else{//����
			mj = width*(height-0.07f*width);
		}
		tv_mj.setText(df.format(mj));
	}
	/**
	 * ���������ͷ�����
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
	 * ������С���� L=32*S/U;
	 */
	public void calcMindistance(){
		double u = Math.sqrt(mj);
		if(typePositon==0||typePositon==1){//����  ���� 
			u = u *4.16;
		}else if(typePositon==3){//����
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
