package com.tk.lfhl.activity;

import java.util.List;

import com.android.db.DBHelper;
import com.android.utils.ListUtils;
import com.android.utils.MyToask;
import com.tk.adapter.SimpleSpinnerAdapter;
import com.tk.lfhl.bean.PeopleInfo;
import com.tk.lfhl.bean.RoadWayInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * ��Ա��Ϣ
 * @author Administrator
 *
 */
public class PeopleInfoActivity extends Activity implements OnClickListener,OnItemSelectedListener{
	private Button btn_save ,btn_back,btn_delete;
	private EditText et_username;//�����Ա
	private EditText et_department;//����
	private EditText et_job;//ְ��
	private DBHelper db ;
	private List<PeopleInfo> list;
	private PeopleInfo info;
	private Spinner sp_peoplediaoyong;
	private SimpleSpinnerAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_info);
		initView();

	}
	//string ΪҪ�жϵ��ַ��� 
	public static boolean isConSpeCharacters(String string){ 
	    if(string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*","").length()==0){ 
	        //�����������ַ� 
	        return false; 
	    } 
	    return true; 
	} 
	private void initView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_department = (EditText) findViewById(R.id.et_department);
		et_job = (EditText) findViewById(R.id.et_job);
		btn_save= (Button) findViewById(R.id.btn_save);
		btn_back= (Button) findViewById(R.id.btn_back);
		btn_delete= (Button) findViewById(R.id.btn_delete);
		sp_peoplediaoyong = (Spinner) findViewById(R.id.sp_peoplediaoyong);
		btn_save.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		db = new DBHelper(getApplicationContext());
		list = db.findTablePeople();
		List<String> listName = ListUtils.formatPeopleInfoNameList(list);
		adapter = new SimpleSpinnerAdapter(listName, getApplicationContext());
		sp_peoplediaoyong.setAdapter(adapter);
		sp_peoplediaoyong.setOnItemSelectedListener(this);
		sp_peoplediaoyong.setSelection(listName.indexOf(MainActivity.last_id.get(1)));
	}
	@Override
	public void onClick(View v) {
		if(v == btn_save){
			String username =  et_username.getText().toString();
			String department = et_department.getText().toString();
			String job = et_job.getText().toString();
			if(username.isEmpty()){
				MyToask.showTextToast("�����Ա����Ϊ��", getApplicationContext(), 0);
				return ;
			}
			
			if(job.isEmpty()){
				MyToask.showTextToast("ְ����Ϊ��", getApplicationContext(), 0);
				return ;
			}
			if(department.isEmpty()){
				MyToask.showTextToast("���Ų���Ϊ��", getApplicationContext(), 0);
				return ;
			}
			if(isConSpeCharacters(username)){
				MyToask.showTextToast("�����Ա��������Ƿ��ַ�", getApplicationContext(), 0);
				return ;
			}
			if(isConSpeCharacters(job)){
				MyToask.showTextToast("ְ��������Ƿ��ַ�", getApplicationContext(), 0);
				return ;
			}
			if(isConSpeCharacters(department)){
				MyToask.showTextToast("���Ų�������Ƿ��ַ�", getApplicationContext(), 0);
				return ;
			}
			if(db.save(new PeopleInfo(username,department, job))){
				MyToask.showTextToast("����ɹ�", getApplicationContext(), 0);
			}else{
				MyToask.showTextToast("�����Ա�Ѵ��ڣ����޸ļ����Ա�󱣴棡", getApplicationContext(), 0);
			}
			list = db.findTablePeople();
			adapter.setList(ListUtils.formatPeopleInfoNameList(list));
			adapter.notifyDataSetChanged();

		}
		if(v== btn_back){
			finish();
		}
		if(v== btn_delete){ 
			if(list!=null&&info!=null){
				new AlertDialog.Builder(PeopleInfoActivity.this).setTitle("ɾ����ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						db.deletePeopleByName(info.getUserName());
						MyToask.showTextToast("ɾ���ɹ���", getApplicationContext(), 0);
						info = null;
						list = db.findTablePeople();
						adapter.setList(ListUtils.formatPeopleInfoNameList(list));
						adapter.notifyDataSetChanged();

					}
				}).setNegativeButton("ȡ��", null).setMessage("ȷ��ɾ����"+info.getUserName()+"������Ϣ?").show();
			}else{
				MyToask.showTextToast("��ѡ������Ա��Ȼ��ɾ����", getApplicationContext(), 0);
			}
		}		
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent==sp_peoplediaoyong){
			TextView tv = (TextView) view;
			list = db.findTablePeople();
			info = 	ListUtils.findPeopleByName(tv.getText().toString(), list);
			db.updateLAST_TABLE(MainActivity.last_id.get(0), info.getUserName(), MainActivity.last_id.get(2));
			if(info!=null){
				et_username.setText(info.getUserName());
				et_department.setText(info.getDepartMent());
				et_job.setText(info.getJob());
			}
		}		
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		MyToask.showTextToast("��Ա��Ϣû������,���������", getApplicationContext(), 0);
		et_username.setText("");
		et_job.setText("");
		et_department.setText("");
		db.updateLAST_TABLE(MainActivity.last_id.get(0), "", MainActivity.last_id.get(2));

	}

}
