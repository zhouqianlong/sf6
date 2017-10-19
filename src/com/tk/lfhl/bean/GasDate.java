package com.tk.lfhl.bean;

import java.io.Serializable;

public class GasDate implements Serializable{

	/**
	 * 数据表对象
	 */
	private static final long serialVersionUID = 1L;
	private String msg;//描述
	private float gasDate;//气体浓度
	
 
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public float getGasDate() {
		return gasDate;
	}
	public void setGasDate(float gasDate) {
		this.gasDate = gasDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
 
	public GasDate(String msg, float gasDate) {
		this.msg = msg;
		this.gasDate = gasDate;
	}
	public GasDate() {
	}
 
	
}