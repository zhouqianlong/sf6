package com.android.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SDSQLiteOpenHelper{

	public static final String DATABASE_NAME="SFSIX.db";
	public static final int VERSION=13;
	private static final String TAG = "SF6";
	public static final String ROAD_TABLE = "ROADWAY_INFORMATION";//�����Ϣ��
	public static final String PEOPLE_TABLE = "PEOPLE_INFO";//��Ա��Ϣ��
	public static final String LAST_TABLE = "LAST_INFO";//���һ����д����
	public static final String RIZHI_TABLE = "RIZHI_INFO";//��־��Ϣ��
	public static final String RIZHI_MORE_TABLE = "RIZHI_INFO_GAS_MORE";//��־��Ϣ���ⲿ��
	public static final String GAS_CALIBRATION = "GAS_CALIBRATION";//����У׼��
	public static final String SETTING = "SETTRING";//������Ϣ��

	public DBManager(Context context) {
		super(context,DATABASE_NAME, null, VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "�������ݿ⣺"+DATABASE_NAME);
		
		Log.i(TAG, "���������Ϣ��"+ROAD_TABLE);
		createTableROAD_TABLE(db);
		Log.i(TAG, "������Ա��Ϣ��"+PEOPLE_TABLE);
		createTablePEOPLE_TABLE(db);
		Log.i(TAG, "�������һ����д���ݣ�"+LAST_TABLE);
		createTableLAST_TABLE(db);
		Log.i(TAG, "������־��Ϣ��"+RIZHI_TABLE);
		createTableRIZHI_TABLE(db);
		Log.i(TAG, "������־��Ϣ���ⲿ��"+RIZHI_MORE_TABLE);
		createTableRizhi_MORE(db);
		Log.i(TAG, "��������У׼��"+GAS_CALIBRATION);
		createGAS_CALIBRATION(db);
		Log.i(TAG, "����������Ϣ��"+SETTING);
		createSettingTB(db);
		db.execSQL("insert into "+LAST_TABLE+"("+ROAD_TABLE+","+PEOPLE_TABLE+",type) values('','','')");

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(tabIsExist(RIZHI_TABLE,db)){
			db.execSQL("DROP TABLE "+RIZHI_TABLE);
			createTableRIZHI_TABLE(db);
		}
		Log.i(TAG, "onUpgrad=====oldVersion:"+oldVersion+"newVersion:"+newVersion);
	}


	private void createTableROAD_TABLE(SQLiteDatabase db) {
		String sql="CREATE TABLE "+ROAD_TABLE+"(" +
				"ID integer primary key autoincrement not null," +//ID
				"ROADWAY_NAME text(40)," +//�������
				"SUPPORTING_FORM text(40)," +//֧����ʽ
				"MEASUREMENT_OF_AIR_LEAKAGE_LOCATION text(20)," +//��©��λ��
				"WIND_SPEED text(20), " +//����
				"ROAD_SHAPE text(20), " +//�����״  2016��4��19��09:40:43
				"ROAD_HEIGHT text(20), " +//�߶�	2016��4��19��09:40:46
				"ROAD_WIDTH text(20), " +//���	2016��4��19��09:40:47
				"HOLE_AREA text(20), " +//��������
				"HOLE_LENGTH text(20), " +//�����ܽ糤��
				"CREATE_TIME text(20)"+//����ʱ��
				")";
		Log.i(TAG, "�������ݱ�"+ROAD_TABLE);
		db.execSQL(sql);
	}
	private void createTablePEOPLE_TABLE(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+PEOPLE_TABLE+"(" +
				"ID integer primary key autoincrement not null," +//ID
				"USERNAME text(40)," +//�����Ա
				"DEPARTMENT text(40)," +//����
				"JOB text(40)" +//ְ��
				")";
		db.execSQL(sql);
	}
	private void createTableLAST_TABLE(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+LAST_TABLE+"(" +
				"ID integer primary key autoincrement not null," +//ID
				""+ROAD_TABLE+" text(40)," +//���һ�α��������Ϣ��id
				""+PEOPLE_TABLE+" text(40)," +//���һ�α��������Ϣ���id
				"type text(40)"+//��ⷽ��
				")";
		db.execSQL(sql);
	}
	private void createTableRIZHI_TABLE(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+RIZHI_TABLE+"(" +
				"ID integer primary key autoincrement not null," +//ID
				"ROADWAY_NAME text(40)," + //�������
				"SUPPORTING_FORM text(40)," +//֧����ʽ
				"HOLE_AREA text(20)," +//��������
				"LOUFENG_TYPE text(20)," +//©�緽ʽ
				"GAS_DATE  text(20)," +//SF6Ũ��
				"TEST_TIME text(20)," +//����ʱ��
				"TEST_NAME text(20)," +//�����Ա
				"measurementOfAirLeakageLocation text(20)," +//��©��λ��
				"windSpeed text(20)," +//����
				"ROAD_SHAPE text(20), " +//�����״  2016��4��19��09:40:43
				"ROAD_HEIGHT text(20), " +//�߶�	2016��4��19��09:40:46
				"ROAD_WIDTH text(20), " +//���	2016��4��19��09:40:47
				"TEST_BUMEN text(20), " +//��ⲿ��	2016��4��19��09:40:47
				"TEST_ZHIWU text(20), " +//���ְ��	2016��4��19��09:40:47
				"hole_length text(20)" +//�����ܽ糤��
				")";
		db.execSQL(sql);
	}
	/**
	 *�����־��Ϣ���ⲿ��Ϣ������������Ũ�ȣ�
	 */
	private void createTableRizhi_MORE(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+RIZHI_MORE_TABLE+"(" +
				"ID integer not null," +//ID
				"GAS_MSG text(40)," + //����
				"GAS_DATE text(20)," +//����Ũ��
				"ROAD_WAY_LEN text(40)," + //�������
				"ADDRESS text(40)" + //����λ��
				")";
		db.execSQL(sql);
	}
	private void createGAS_CALIBRATION(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+GAS_CALIBRATION+"(" +
				"ID integer primary key autoincrement not null," +//ID
				"GAS_ZERO text(40)," + //��ǰ���
				"GAS_ZERO_ text(40)," + //���ú����
				"GAS_DATE text(40)," +//��ǰ����
				"GAS_DATE_ text(40)," +//���ú����
				"GAS_ALARM text(40)" + //����
				")";
		db.execSQL(sql);
		
		db.execSQL("insert into "+GAS_CALIBRATION+"(GAS_ZERO,GAS_ZERO_,GAS_DATE,GAS_DATE_,GAS_ALARM) values('1','1','1','1','100')");
	}
	private void createSettingTB(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+SETTING+"(" +
				"ID integer primary key autoincrement not null," +//ID
				"ADDRESS text(40)," + //���ô�������ַ
				"BUY_TIME text(40)" + //����ʱ��
				")";
		db.execSQL(sql);
		db.execSQL("insert into "+SETTING+"(ADDRESS,BUY_TIME) values('0','0')");
	}
	public final String testInsertValues = "insert into ROADWAY_INFORMATION(" +"ROADWAY_NAME," +
			"SUPPORTING_FORM," +
			"MEASUREMENT_OF_AIR_LEAKAGE_LOCATION," +
			"WIND_SPEED," +
			"ROAD_SHAPE," +
			"ROAD_HEIGHT," +
			"ROAD_WIDTH," +
			"HOLE_AREA," +
			"HOLE_LENGTH," +
			"CREATE_TIME) values('���һ','֧��XX','����©��','3.5','1','20','20''20m','50m',"+System.currentTimeMillis()+") ";

	/**
	 * �ж�ĳ���Ƿ����
	 * @param tabName ����
	 * @return
	 */
	public boolean tabIsExist(String tabName,SQLiteDatabase db){
		boolean result = false;
		if(tabName == null){
			return false;
		}
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"'" ;
			cursor = db.rawQuery(sql, null);
			if(cursor.moveToNext()){
				int count = cursor.getInt(0);
				if(count>0){
					result = true;
				}
			}

		} catch (Exception e) {
		}                
		return result;
	}
}
