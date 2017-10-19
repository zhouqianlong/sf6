package com.tk.lfhl.bean;

import java.io.Serializable;
import java.util.List;


public class RizhiInfo implements Serializable {
	private static final long serialVersionUID = 2117039275496261166L;
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String ROADWAY_NAME;//巷道名称
	private String SUPPORTING_FORM;//支护形式
	private String HOLE_AREA;//井巷断面积
	private String LOUFENG_TYPE;//漏风方式
	private String GAS_DATE;//SF6浓度
	private String TEST_NAME;//检测人员
	private String TEST_TIME;//插入时间
	private String measurementOfAirLeakageLocation ;//测漏风位置
	private String windSpeed ;//风速
	private String ROAD_SHAPE;//巷道形状  2016年4月19日09:40:43
	private String ROAD_HEIGHT;  //高度	2016年4月19日09:40:46
	private String ROAD_WIDTH;//宽度	2016年4月19日09:40:47
	private String TEST_BUMEN;//检测部门	2016年4月19日09:40:47
	private String TEST_ZHIWU;//检测职务	2016年4月19日09:40:47
	private String hole_length;//井巷周界长度
	private List<GasDate> list;//重复测试数据
	
	public List<GasDate> getList() {
		return list;
	}
	public void setList(List<GasDate> list) {
		this.list = list;
	}
	public String getMeasurementOfAirLeakageLocation() {
		return measurementOfAirLeakageLocation;
	}
	public void setMeasurementOfAirLeakageLocation(
			String measurementOfAirLeakageLocation) {
		this.measurementOfAirLeakageLocation = measurementOfAirLeakageLocation;
	}
	public String getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}
	public String getHole_length() {
		return hole_length;
	}
	public void setHole_length(String hole_length) {
		this.hole_length = hole_length;
	}

	public String getGAS_DATE() {
		return GAS_DATE;
	}
	public void setGAS_DATE(String gAS_DATE) {
		GAS_DATE = gAS_DATE;
	}
	public String getTEST_TIME() {
		return TEST_TIME;
	}
	public void setTEST_TIME(String tEST_TIME) {
		TEST_TIME = tEST_TIME;
	}
	public String getTEST_NAME() {
		return TEST_NAME;
	}
	public void setTEST_NAME(String tEST_NAME) {
		TEST_NAME = tEST_NAME;
	}
	public String getROADWAY_NAME() {
		return ROADWAY_NAME;
	}
	public void setROADWAY_NAME(String rOADWAY_NAME) {
		ROADWAY_NAME = rOADWAY_NAME;
	}
	public String getSUPPORTING_FORM() {
		return SUPPORTING_FORM;
	}
	public void setSUPPORTING_FORM(String sUPPORTING_FORM) {
		SUPPORTING_FORM = sUPPORTING_FORM;
	}
	public String getHOLE_AREA() {
		return HOLE_AREA;
	}
	public void setHOLE_AREA(String hOLE_AREA) {
		HOLE_AREA = hOLE_AREA;
	}
	public String getLOUFENG_TYPE() {
		return LOUFENG_TYPE;
	}
	public void setLOUFENG_TYPE(String lOUFENG_TYPE) {
		LOUFENG_TYPE = lOUFENG_TYPE;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
 
	public String getROAD_SHAPE() {
		return ROAD_SHAPE;
	}
	public void setROAD_SHAPE(String rOAD_SHAPE) {
		ROAD_SHAPE = rOAD_SHAPE;
	}
	public String getROAD_HEIGHT() {
		return ROAD_HEIGHT;
	}
	public void setROAD_HEIGHT(String rOAD_HEIGHT) {
		ROAD_HEIGHT = rOAD_HEIGHT;
	}
	public String getROAD_WIDTH() {
		return ROAD_WIDTH;
	}
	public void setROAD_WIDTH(String rOAD_WIDTH) {
		ROAD_WIDTH = rOAD_WIDTH;
	}
	public String getTEST_BUMEN() {
		return TEST_BUMEN;
	}
	public void setTEST_BUMEN(String tEST_BUMEN) {
		TEST_BUMEN = tEST_BUMEN;
	}
	public String getTEST_ZHIWU() {
		return TEST_ZHIWU;
	}
	public void setTEST_ZHIWU(String tEST_ZHIWU) {
		TEST_ZHIWU = tEST_ZHIWU;
	}
 
	public RizhiInfo() {
	}
	public RizhiInfo(String rOADWAY_NAME, String sUPPORTING_FORM,
			String hOLE_AREA, String lOUFENG_TYPE, String gAS_DATE,
			String tEST_NAME, String tEST_TIME,
			String measurementOfAirLeakageLocation, String windSpeed,
			String rOAD_SHAPE, String rOAD_HEIGHT, String rOAD_WIDTH,
			String tEST_BUMEN, String tEST_ZHIWU, String hole_length) {
		ROADWAY_NAME = rOADWAY_NAME;
		SUPPORTING_FORM = sUPPORTING_FORM;
		HOLE_AREA = hOLE_AREA;
		LOUFENG_TYPE = lOUFENG_TYPE;
		GAS_DATE = gAS_DATE;
		TEST_NAME = tEST_NAME;
		TEST_TIME = tEST_TIME;
		this.measurementOfAirLeakageLocation = measurementOfAirLeakageLocation;
		this.windSpeed = windSpeed;
		ROAD_SHAPE = rOAD_SHAPE;
		ROAD_HEIGHT = rOAD_HEIGHT;
		ROAD_WIDTH = rOAD_WIDTH;
		TEST_BUMEN = tEST_BUMEN;
		TEST_ZHIWU = tEST_ZHIWU;
		this.hole_length = hole_length;
	}
 
	
 
}
