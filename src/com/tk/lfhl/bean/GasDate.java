package com.tk.lfhl.bean;

import java.io.Serializable;

public class GasDate implements Serializable{

	/**
	 * ���ݱ����
	 */
	private static final long serialVersionUID = 1L;
	private String msg;//����
	private float gasDate;//����Ũ��
	
 
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