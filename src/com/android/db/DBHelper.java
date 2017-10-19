package com.android.db;




import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tk.lfhl.bean.GasDate;
import com.tk.lfhl.bean.PeopleInfo;
import com.tk.lfhl.bean.RizhiInfo;
import com.tk.lfhl.bean.RoadWayInfo;
import com.tk.lfhl.bean.SettingBean;


import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
public class DBHelper {
	public DBManager dbManager;
	public DBHelper(Context context) {
		this.dbManager = new DBManager(context);
	}
	/**
	 * 添加一条人员信息记录
	 * @param person
	 */
	public boolean save(PeopleInfo info) {
		if(findTablePeople(info.getUserName()).size()>0){
			return false;
		}else{
			SQLiteDatabase db = dbManager.getWritableDatabase();
			db.execSQL( "insert into "+DBManager.PEOPLE_TABLE+"(" +
					"USERNAME," +
					"DEPARTMENT," +//部门
					"JOB) values(?,?,?) ",
					new Object[] {info.getUserName(),info.getDepartMent(),info.getJob()});
			db.close();
			return true;
		}
	}
	public void deletePeopleByName(String username){
		SQLiteDatabase db = dbManager.getReadableDatabase();
		db.execSQL("delete from "+DBManager.PEOPLE_TABLE+" where USERNAME='"+username+"'", new Object[] { });
		db.close();
	}

	/**
	 * 查询人员信息表
	 * @return
	 */
	public List<PeopleInfo> findTablePeople(){
		List<PeopleInfo> list = new ArrayList<PeopleInfo>();
		SQLiteDatabase db = dbManager.getReadableDatabase();
		String sql = "select * from "+DBManager.PEOPLE_TABLE;
		Cursor cursor = db.rawQuery(sql, new String[]{});
		while (cursor.moveToNext()) {
			PeopleInfo info = new PeopleInfo();
			info.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			info.setUserName(cursor.getString(cursor
					.getColumnIndex("USERNAME")));
			info.setDepartMent(cursor.getString(cursor
					.getColumnIndex("DEPARTMENT")));
			info.setJob(cursor.getString(cursor
					.getColumnIndex("JOB")));
			list.add(info);
		}
		db.close();
		cursor.close();
		return list;
	}
	/**
	 * 根据检测人员名称查询
	 * @param name
	 * @return
	 */
	public List<PeopleInfo> findTablePeople(String name){
		List<PeopleInfo> list = new ArrayList<PeopleInfo>();
		SQLiteDatabase db = dbManager.getReadableDatabase();
		String sql = "select * from "+DBManager.PEOPLE_TABLE+" where USERNAME = '"+name+"'";
		Cursor cursor = db.rawQuery(sql, new String[]{});
		while (cursor.moveToNext()) {
			PeopleInfo info = new PeopleInfo();
			info.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			info.setUserName(cursor.getString(cursor
					.getColumnIndex("USERNAME")));
			info.setDepartMent(cursor.getString(cursor
					.getColumnIndex("DEPARTMENT")));
			info.setJob(cursor.getString(cursor
					.getColumnIndex("JOB")));
			list.add(info);
		}
		db.close();
		cursor.close();
		return list;
	}
	/**
	 * 添加一条巷道信息记录
	 * @param person
	 */
	public boolean save(RoadWayInfo info) {
		if(findRoad_Way_name(info.getRoadwayName()).size()!=0){
			return false;
		}else{
			SQLiteDatabase db = dbManager.getWritableDatabase();
			db.execSQL( "insert into ROADWAY_INFORMATION(" +
					"ROADWAY_NAME," +
					"SUPPORTING_FORM," +
					"MEASUREMENT_OF_AIR_LEAKAGE_LOCATION," +
					"WIND_SPEED," +
					"ROAD_SHAPE," +
					"ROAD_HEIGHT," +
					"ROAD_WIDTH," +
					"HOLE_AREA," +
					"HOLE_LENGTH," +
					"CREATE_TIME) values(?,?,?,?,?,?,?,?,?,?) ",
					new Object[] {info.getRoadwayName(),
					info.getSupportingForm(),
					info.getMeasurementOfAirLeakageLocation(),
					info.getWindSpeed(),
					info.getShape(),
					info.getHeight(),
					info.getWidth(),
					info.getHole_area(),
					info.getHole_length(),
					System.currentTimeMillis()
					});
			db.close();
			return true;
		}

	}

	/**
	 *  查询所有记录
	 */
	public 	List<RoadWayInfo>   findROAD_TABLE() {
		List<RoadWayInfo> userList = new ArrayList<RoadWayInfo>();
		SQLiteDatabase db = dbManager.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "+DBManager.ROAD_TABLE+"", new String[]{});
		while (cursor.moveToNext()) {
			RoadWayInfo roadWayInfo = new RoadWayInfo();
			roadWayInfo.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			roadWayInfo.setRoadwayName(cursor.getString(cursor
					.getColumnIndex("ROADWAY_NAME")));
			roadWayInfo.setSupportingForm(cursor.getString(cursor
					.getColumnIndex("SUPPORTING_FORM")));
			roadWayInfo.setMeasurementOfAirLeakageLocation(cursor.getString(cursor.getColumnIndex("MEASUREMENT_OF_AIR_LEAKAGE_LOCATION")));
			roadWayInfo.setWindSpeed(cursor.getString(cursor.getColumnIndex("WIND_SPEED")));
			
			roadWayInfo.setShape(cursor.getString(cursor.getColumnIndex("ROAD_SHAPE")));
			roadWayInfo.setHeight(cursor.getString(cursor.getColumnIndex("ROAD_HEIGHT")));
			roadWayInfo.setWidth(cursor.getString(cursor.getColumnIndex("ROAD_WIDTH")));
			
			
			roadWayInfo.setHole_area(cursor.getString(cursor.getColumnIndex("HOLE_AREA")));
			roadWayInfo.setHole_length(cursor.getString(cursor.getColumnIndex("HOLE_LENGTH")));
			roadWayInfo.setCreate_time(cursor.getString(cursor.getColumnIndex("CREATE_TIME")));
			userList.add(roadWayInfo);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String times = sdf.format(new Date(Long.valueOf(roadWayInfo.getCreate_time())));
			Log.i("MATERIALINFOCOLLECT", "查询："+roadWayInfo.getId()+":"+roadWayInfo.getRoadwayName()+":"+times);
		}
		db.close();
		cursor.close();
		return userList;
	}
	/**
	 *  查询所有巷道的名称
	 */
	public List<String> findRoad_Way_name() {
		List<String> userList = new ArrayList<String>();
		SQLiteDatabase db = dbManager.getReadableDatabase();
		String sql = "select * from "+DBManager.ROAD_TABLE+"";
		Cursor cursor = db.rawQuery(sql, new String[]{});
		while (cursor.moveToNext()) {
			RoadWayInfo roadWayInfo = new RoadWayInfo();
			userList.add(cursor.getString(cursor.getColumnIndex("ROADWAY_NAME")));
			Log.i("MATERIALINFOCOLLECT", sql+"："+roadWayInfo.getId()+":"+roadWayInfo.getRoadwayName());
		}
		db.close();
		cursor.close();
		return userList;
	}

	public void deleteRoad_WayForName(String roadName){
		SQLiteDatabase db = dbManager.getReadableDatabase();
		db.execSQL("delete from "+DBManager.ROAD_TABLE+" where ROADWAY_NAME='"+roadName+"'", new Object[] { });
		db.close();
	}

	/**
	 *  根据巷道名称查询全部记录
	 */	
	public 	List<RoadWayInfo> findRoad_Way_name(String road_way_name) {
		List<RoadWayInfo> userList = new ArrayList<RoadWayInfo>();
		SQLiteDatabase db = dbManager.getReadableDatabase();
		String sql = "select * from "+DBManager.ROAD_TABLE+" where ROADWAY_NAME = '"+road_way_name+"'";
		Cursor cursor = db.rawQuery(sql, new String[]{});
		while (cursor.moveToNext()) {
			RoadWayInfo roadWayInfo = new RoadWayInfo();
			roadWayInfo.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			roadWayInfo.setRoadwayName(cursor.getString(cursor
					.getColumnIndex("ROADWAY_NAME")));
			roadWayInfo.setSupportingForm(cursor.getString(cursor
					.getColumnIndex("SUPPORTING_FORM")));
			roadWayInfo.setMeasurementOfAirLeakageLocation(cursor.getString(cursor.getColumnIndex("MEASUREMENT_OF_AIR_LEAKAGE_LOCATION")));
			roadWayInfo.setWindSpeed(cursor.getString(cursor.getColumnIndex("WIND_SPEED")));
			roadWayInfo.setShape(cursor.getString(cursor.getColumnIndex("ROAD_SHAPE")));
			roadWayInfo.setHeight(cursor.getString(cursor.getColumnIndex("ROAD_HEIGHT")));
			roadWayInfo.setWidth(cursor.getString(cursor.getColumnIndex("ROAD_WIDTH")));
			roadWayInfo.setHole_area(cursor.getString(cursor.getColumnIndex("HOLE_AREA")));
			roadWayInfo.setHole_length(cursor.getString(cursor.getColumnIndex("HOLE_LENGTH")));
			roadWayInfo.setCreate_time(cursor.getString(cursor.getColumnIndex("CREATE_TIME")));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String times = sdf.format(new Date(Long.valueOf(roadWayInfo.getCreate_time())));
			Log.i("MATERIALINFOCOLLECT", "查询："+roadWayInfo.getId()+":"+roadWayInfo.getRoadwayName()+":"+times);
			userList.add(roadWayInfo);
		}
		db.close();
		cursor.close();
		return userList;
	}



	public List<String> findLAST_TABLE(){
		List<String> userList = new ArrayList<String>();
		SQLiteDatabase db = dbManager.getReadableDatabase();
		String sql = "select * from "+DBManager.LAST_TABLE+" where ID = 1";
		Cursor cursor = db.rawQuery(sql, new String[]{});
		while (cursor.moveToNext()) {
			userList.add(cursor.getString(cursor.getColumnIndex("ROADWAY_INFORMATION")));
			userList.add(cursor.getString(cursor.getColumnIndex("PEOPLE_INFO")));
			userList.add(cursor.getString(cursor.getColumnIndex("type")));
		}
		db.close();
		cursor.close();
		return userList;


	}


	public void updateLAST_TABLE(String ROADWAY_INFORMATION_Name,String PEOPLE_INFO_Name,String type){
		SQLiteDatabase db=dbManager.getWritableDatabase();
		db.execSQL("update "+DBManager.LAST_TABLE+" set ROADWAY_INFORMATION=?,PEOPLE_INFO=?,type=?where id=1",new Object[] {ROADWAY_INFORMATION_Name,PEOPLE_INFO_Name,type});
		db.close();
	}


	public void saveRIZHI_INFO(RizhiInfo info){
		SQLiteDatabase db=dbManager.getWritableDatabase();
		db.execSQL("insert into RIZHI_INFO(" +
				"ROADWAY_NAME," +//巷道名称
				"SUPPORTING_FORM," +//支护形式
				"HOLE_AREA," +//井巷断面积
				"LOUFENG_TYPE," +//漏风方式
				"GAS_DATE," +//SF6浓度
				"TEST_TIME," +//插入时间
				"TEST_NAME," +//检测人员
				"measurementOfAirLeakageLocation," +//漏风位置
				"windSpeed," +//风速
				"ROAD_SHAPE , " +//巷道形状  2016年4月19日09:40:43
				"ROAD_HEIGHT , " +//高度	2016年4月19日09:40:46
				"ROAD_WIDTH , " +//宽度	2016年4月19日09:40:47
				"TEST_BUMEN , " +//检测部门	2016年4月19日09:40:47
				"TEST_ZHIWU , " +//检测职务	2016年4月19日09:40:47
				"hole_length " +//井巷周界长度
				") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ",
				new Object[] {info.getROADWAY_NAME(),info.getSUPPORTING_FORM(),info.getHOLE_AREA(),info.getLOUFENG_TYPE(),info.getGAS_DATE(),info.getTEST_TIME(),info.getTEST_NAME(),info.getMeasurementOfAirLeakageLocation(),info.getWindSpeed(),
				info.getROAD_SHAPE(),
				info.getROAD_HEIGHT(),
				info.getROAD_WIDTH(),
				info.getTEST_BUMEN(),
				info.getTEST_ZHIWU(),
				info.getHole_length()});
		int id = findRIZHI_INFO(info,db);//查询插入数据的索引
		List<GasDate> gasList = info.getList();
		for(int i= 0 ; i <  gasList.size();i++){
			db.execSQL("insert into "+DBManager.RIZHI_MORE_TABLE+"(" +
					"ID," +//ID
					"GAS_MSG," +//描述
					"GAS_DATE," +//气体浓度
					"ROAD_WAY_LEN,"+//巷道长度
					"ADDRESS"+//测试位置
					") values(?,?,?,?,?) ",
					new Object[] {id,
					gasList.get(i).getMsg(),
					gasList.get(i).getGasDate(),
					"",
					""
			});
		}

		db.close();
	}
	public 	List<GasDate> findGasDates(int id){
		SQLiteDatabase db = dbManager.getReadableDatabase();
		String sql = "select * from "+DBManager.RIZHI_MORE_TABLE+" where ID = '"+id+"'";
		Cursor cursor = db.rawQuery(sql, new String[]{});
		List<GasDate> list = new ArrayList<GasDate>();
		while (cursor.moveToNext()) {
			GasDate date = new GasDate();
			date.setMsg(cursor.getString(cursor.getColumnIndex("GAS_MSG")));
			date.setGasDate(Float.valueOf(cursor.getString(cursor.getColumnIndex("GAS_DATE"))));
//			date.setRoadWayLen(cursor.getString(cursor.getColumnIndex("ROAD_WAY_LEN")));
//			date.setAddress(cursor.getString(cursor.getColumnIndex("ADDRESS")));
			list.add(date);
		}
		db.close();
		cursor.close();
		return list;
	}



	public int findRIZHI_INFO(RizhiInfo info ,SQLiteDatabase db){
		String sql = "select * from RIZHI_INFO where TEST_TIME = '"+info.getTEST_TIME()+"' and GAS_DATE = '"+info.getGAS_DATE()+"' and  measurementOfAirLeakageLocation = '"+info.getMeasurementOfAirLeakageLocation()+"' and TEST_NAME ='"+info.getTEST_NAME()+"'";
		Cursor cursor = db.rawQuery(sql, new String[]{});
		int id = 0;
		while (cursor.moveToNext()) {
			id = cursor.getInt(cursor.getColumnIndex("ID"));
		}
		cursor.close();
		return id;
	}
	public List<RizhiInfo> findRIZHI_INFO(){
		List<RizhiInfo> list = new ArrayList<RizhiInfo>();
		SQLiteDatabase db = dbManager.getReadableDatabase();
		String sql = "select * from RIZHI_INFO";
		Cursor cursor = db.rawQuery(sql, new String[]{});
		while (cursor.moveToNext()) {
			RizhiInfo info = new RizhiInfo();
			info.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			info.setROADWAY_NAME(cursor.getString(cursor.getColumnIndex("ROADWAY_NAME")));
			info.setSUPPORTING_FORM(cursor.getString(cursor.getColumnIndex("SUPPORTING_FORM")));
			info.setHOLE_AREA(cursor.getString(cursor.getColumnIndex("HOLE_AREA")));
			info.setLOUFENG_TYPE(cursor.getString(cursor.getColumnIndex("LOUFENG_TYPE")));
			info.setGAS_DATE(cursor.getString(cursor.getColumnIndex("GAS_DATE")));
			info.setTEST_TIME(cursor.getString(cursor.getColumnIndex("TEST_TIME")));
			info.setTEST_NAME(cursor.getString(cursor.getColumnIndex("TEST_NAME")));
			info.setList(findGasDates(info.getId()));
			info.setMeasurementOfAirLeakageLocation(cursor.getString(cursor.getColumnIndex("measurementOfAirLeakageLocation")));
			info.setWindSpeed(cursor.getString(cursor.getColumnIndex("windSpeed")));
			info.setROAD_SHAPE(cursor.getString(cursor.getColumnIndex("ROAD_SHAPE")));
			info.setROAD_HEIGHT(cursor.getString(cursor.getColumnIndex("ROAD_HEIGHT")));
			info.setROAD_WIDTH(cursor.getString(cursor.getColumnIndex("ROAD_WIDTH")));
			info.setTEST_BUMEN(cursor.getString(cursor.getColumnIndex("TEST_BUMEN")));
			info.setTEST_ZHIWU(cursor.getString(cursor.getColumnIndex("TEST_ZHIWU")));
			info.setHole_length(cursor.getString(cursor.getColumnIndex("hole_length")));
			list.add(info);
		}
		db.close();
		cursor.close();
		return list;
	}


	public void deleteRIZHI_INFO(RizhiInfo info){
		SQLiteDatabase db=dbManager.getWritableDatabase();
		db.execSQL("delete from RIZHI_INFO where ROADWAY_NAME = '"+info.getROADWAY_NAME()+"' and TEST_TIME ='"+info.getTEST_TIME()+"' and TEST_NAME='"+info.getTEST_NAME()+"'and GAS_DATE='"+info.getGAS_DATE()+"'" );
		deleteRIZHI_INFO_MORE(info.getId());
		db.close();
	}

	public 	void deleteRIZHI_INFO_MORE(int id){
		SQLiteDatabase db = dbManager.getReadableDatabase();
		db.execSQL("delete from "+DBManager.RIZHI_MORE_TABLE+" where ID = '"+id+"' " );
		db.close();
	}


	public float[] findGAS_CALIBRATION(){
		float [] date = new float [5];
		SQLiteDatabase db = dbManager.getReadableDatabase();
		String sql = "select * from "+DBManager.GAS_CALIBRATION+" where id==1";
		Cursor cursor = db.rawQuery(sql, new String []{});
		try {
			while (cursor.moveToFirst()) {
				date[0] = Float.valueOf(cursor.getString(cursor.getColumnIndex("GAS_ZERO")));
				date[1] = Float.valueOf(cursor.getString(cursor.getColumnIndex("GAS_DATE")));
				date[2] = Float.valueOf(cursor.getString(cursor.getColumnIndex("GAS_ALARM")));
				date[3] = Float.valueOf(cursor.getString(cursor.getColumnIndex("GAS_ZERO_")));
				date[4] = Float.valueOf(cursor.getString(cursor.getColumnIndex("GAS_DATE_")));
				break;
			}
		} catch (Exception e) {
			updateGAS_CALIBRATION1("1", "1");
			updateGAS_CALIBRATION1("1", "1");
			updateGAS_CALIBRATION3("100");
			date[0]= 1;
			date[1]= 1;
			date[2]= 100;
			date[3]= 1;
			date[4]= 1;
		}
		cursor.close();
		db.close();
		return date;
	}

	public void updateGAS_CALIBRATION1(String gasZero,String gasZero_){
		SQLiteDatabase db = dbManager.getWritableDatabase();
		String sql = "update "+DBManager.GAS_CALIBRATION+" set GAS_ZERO=?,GAS_ZERO_=? where ID ==1";
		db.execSQL(sql, new String[]{gasZero,gasZero_});
		db.close();
	}
	public void updateGAS_CALIBRATION2(String gasDate,String gasDate_){
		SQLiteDatabase db = dbManager.getWritableDatabase();
		String sql = "update "+DBManager.GAS_CALIBRATION+" set GAS_DATE=?,GAS_DATE_=? where ID ==1";
		db.execSQL(sql, new String[]{gasDate,gasDate_});
		db.close();
	}
	public void updateGAS_CALIBRATION3(String gasAlarm){
		SQLiteDatabase db = dbManager.getWritableDatabase();
		String sql = "update "+DBManager.GAS_CALIBRATION+" set GAS_ALARM=? where ID ==1";
		db.execSQL(sql, new String[]{gasAlarm});
		db.close();
	}
	public void updateSETTING(SettingBean setting){
		SQLiteDatabase db = dbManager.getWritableDatabase();
		String sql = "update "+DBManager.SETTING+" set ADDRESS=? ,BUY_TIME = ?where ID ==1";
		db.execSQL(sql, new String[]{setting.getADDRESS(),setting.getBUY_TIME()});
		db.close();
	}
	public SettingBean findSETTING(){
		SQLiteDatabase db = dbManager.getReadableDatabase();
		String sql = "select * from "+DBManager.SETTING+" where id==1";
		Cursor cursor = db.rawQuery(sql, new String []{});
		SettingBean setting = new SettingBean();
		while (cursor.moveToFirst()) {
			setting.setADDRESS(cursor.getString(cursor.getColumnIndex("ADDRESS")));
			setting.setBUY_TIME(cursor.getString(cursor.getColumnIndex("BUY_TIME")));
			break;
		}
		db.close();
		return setting;
	}
}
