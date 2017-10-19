package com.tk.lfhl.bean;

import java.io.Serializable;

public class AirTestBean implements Serializable {

	private String info;
	private float gasDate;
	private long time;
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public float getGasDate() {
		return gasDate;
	}
	public void setGasDate(float gasDate) {
		this.gasDate = gasDate;
	}
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public AirTestBean(String info, float gasDate,long time) {
		this.info = info;
		this.gasDate = gasDate;
		this.time = time;
	}
	public AirTestBean() {
	}
	
	

}
