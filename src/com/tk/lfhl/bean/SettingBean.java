package com.tk.lfhl.bean;

import java.io.Serializable;

public class SettingBean implements Serializable {
	
	private String ADDRESS;//传感器地址
	private String BUY_TIME;//购买时间
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getBUY_TIME() {
		return BUY_TIME;
	}
	public void setBUY_TIME(String bUY_TIME) {
		BUY_TIME = bUY_TIME;
	}
	public SettingBean(String aDDRESS, String bUY_TIME) {
		ADDRESS = aDDRESS;
		BUY_TIME = bUY_TIME;
	}
	public SettingBean() {
	}
	
	

}
