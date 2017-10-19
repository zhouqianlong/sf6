package com.tk.lfhl.bean;

import java.io.Serializable;
/**
 * 巷道信息表
 * @author Administrator
 *
 */
public class RoadWayInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6327400305673152807L;
	private int id;
	private String roadwayName ;//巷道名称
	private String supportingForm ;//支护形式
	private String measurementOfAirLeakageLocation ;//测漏风位置
	private String windSpeed ;//风速
	private String shape ;//形状
	private String height ;//高度
	private String width ;//宽度
	private String hole_area ;//井巷断面积
	private String hole_length ;//井巷周界长度
	private String create_time;//创建时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoadwayName() {
		return roadwayName;
	}
	public void setRoadwayName(String roadwayName) {
		this.roadwayName = roadwayName;
	}
	public String getSupportingForm() {
		return supportingForm;
	}
	public void setSupportingForm(String supportingForm) {
		this.supportingForm = supportingForm;
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
	public String getHole_area() {
		return hole_area;
	}
	public void setHole_area(String hole_area) {
		this.hole_area = hole_area;
	}
	public String getHole_length() {
		return hole_length;
	}
	public void setHole_length(String hole_length) {
		this.hole_length = hole_length;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
 
	public String getShape() {
		return shape;
	}
	public void setShape(String shape) {
		this.shape = shape;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public RoadWayInfo() {
	}
	public RoadWayInfo(String roadwayName, String supportingForm,
			String measurementOfAirLeakageLocation, String windSpeed,
			String shape, String height, String width, String hole_area,
			String hole_length) {
		this.roadwayName = roadwayName;
		this.supportingForm = supportingForm;
		this.measurementOfAirLeakageLocation = measurementOfAirLeakageLocation;
		this.windSpeed = windSpeed;
		this.shape = shape;
		this.height = height;
		this.width = width;
		this.hole_area = hole_area;
		this.hole_length = hole_length;
	}
	
	
}
