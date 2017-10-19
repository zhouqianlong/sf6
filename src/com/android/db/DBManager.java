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
	public static final String ROAD_TABLE = "ROADWAY_INFORMATION";//巷道信息表
	public static final String PEOPLE_TABLE = "PEOPLE_INFO";//人员信息表
	public static final String LAST_TABLE = "LAST_INFO";//最后一次填写内容
	public static final String RIZHI_TABLE = "RIZHI_INFO";//日志信息表
	public static final String RIZHI_MORE_TABLE = "RIZHI_INFO_GAS_MORE";//日志信息表外部表
	public static final String GAS_CALIBRATION = "GAS_CALIBRATION";//气体校准表
	public static final String SETTING = "SETTRING";//设置信息表

	public DBManager(Context context) {
		super(context,DATABASE_NAME, null, VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "创建数据库："+DATABASE_NAME);
		
		Log.i(TAG, "创建巷道信息表："+ROAD_TABLE);
		createTableROAD_TABLE(db);
		Log.i(TAG, "创建人员信息表："+PEOPLE_TABLE);
		createTablePEOPLE_TABLE(db);
		Log.i(TAG, "创建最后一次填写内容："+LAST_TABLE);
		createTableLAST_TABLE(db);
		Log.i(TAG, "创建日志信息表："+RIZHI_TABLE);
		createTableRIZHI_TABLE(db);
		Log.i(TAG, "创建日志信息表外部表："+RIZHI_MORE_TABLE);
		createTableRizhi_MORE(db);
		Log.i(TAG, "创建气体校准表："+GAS_CALIBRATION);
		createGAS_CALIBRATION(db);
		Log.i(TAG, "创建设置信息表："+SETTING);
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
				"ROADWAY_NAME text(40)," +//巷道名称
				"SUPPORTING_FORM text(40)," +//支护形式
				"MEASUREMENT_OF_AIR_LEAKAGE_LOCATION text(20)," +//测漏风位置
				"WIND_SPEED text(20), " +//风速
				"ROAD_SHAPE text(20), " +//巷道形状  2016年4月19日09:40:43
				"ROAD_HEIGHT text(20), " +//高度	2016年4月19日09:40:46
				"ROAD_WIDTH text(20), " +//宽度	2016年4月19日09:40:47
				"HOLE_AREA text(20), " +//井巷断面积
				"HOLE_LENGTH text(20), " +//井巷周界长度
				"CREATE_TIME text(20)"+//创建时间
				")";
		Log.i(TAG, "创建数据表："+ROAD_TABLE);
		db.execSQL(sql);
	}
	private void createTablePEOPLE_TABLE(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+PEOPLE_TABLE+"(" +
				"ID integer primary key autoincrement not null," +//ID
				"USERNAME text(40)," +//检测人员
				"DEPARTMENT text(40)," +//部门
				"JOB text(40)" +//职务
				")";
		db.execSQL(sql);
	}
	private void createTableLAST_TABLE(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+LAST_TABLE+"(" +
				"ID integer primary key autoincrement not null," +//ID
				""+ROAD_TABLE+" text(40)," +//最后一次保存巷道信息表id
				""+PEOPLE_TABLE+" text(40)," +//最后一次保存巷道信息表表id
				"type text(40)"+//检测方案
				")";
		db.execSQL(sql);
	}
	private void createTableRIZHI_TABLE(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+RIZHI_TABLE+"(" +
				"ID integer primary key autoincrement not null," +//ID
				"ROADWAY_NAME text(40)," + //巷道名称
				"SUPPORTING_FORM text(40)," +//支护形式
				"HOLE_AREA text(20)," +//井巷断面积
				"LOUFENG_TYPE text(20)," +//漏风方式
				"GAS_DATE  text(20)," +//SF6浓度
				"TEST_TIME text(20)," +//插入时间
				"TEST_NAME text(20)," +//检测人员
				"measurementOfAirLeakageLocation text(20)," +//测漏风位置
				"windSpeed text(20)," +//风速
				"ROAD_SHAPE text(20), " +//巷道形状  2016年4月19日09:40:43
				"ROAD_HEIGHT text(20), " +//高度	2016年4月19日09:40:46
				"ROAD_WIDTH text(20), " +//宽度	2016年4月19日09:40:47
				"TEST_BUMEN text(20), " +//检测部门	2016年4月19日09:40:47
				"TEST_ZHIWU text(20), " +//检测职务	2016年4月19日09:40:47
				"hole_length text(20)" +//井巷周界长度
				")";
		db.execSQL(sql);
	}
	/**
	 *添加日志信息表外部信息表（描述、气体浓度）
	 */
	private void createTableRizhi_MORE(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+RIZHI_MORE_TABLE+"(" +
				"ID integer not null," +//ID
				"GAS_MSG text(40)," + //描述
				"GAS_DATE text(20)," +//气体浓度
				"ROAD_WAY_LEN text(40)," + //巷道长度
				"ADDRESS text(40)" + //测试位置
				")";
		db.execSQL(sql);
	}
	private void createGAS_CALIBRATION(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+GAS_CALIBRATION+"(" +
				"ID integer primary key autoincrement not null," +//ID
				"GAS_ZERO text(40)," + //当前零点
				"GAS_ZERO_ text(40)," + //设置后零点
				"GAS_DATE text(40)," +//当前标气
				"GAS_DATE_ text(40)," +//设置后标气
				"GAS_ALARM text(40)" + //报警
				")";
		db.execSQL(sql);
		
		db.execSQL("insert into "+GAS_CALIBRATION+"(GAS_ZERO,GAS_ZERO_,GAS_DATE,GAS_DATE_,GAS_ALARM) values('1','1','1','1','100')");
	}
	private void createSettingTB(SQLiteDatabase db) {
		String sql;
		sql="CREATE TABLE "+SETTING+"(" +
				"ID integer primary key autoincrement not null," +//ID
				"ADDRESS text(40)," + //设置传感器地址
				"BUY_TIME text(40)" + //购买时间
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
			"CREATE_TIME) values('巷道一','支护XX','上面漏风','3.5','1','20','20''20m','50m',"+System.currentTimeMillis()+") ";

	/**
	 * 判断某表是否存在
	 * @param tabName 表名
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
