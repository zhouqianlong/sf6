package com.tk.lfhl.bean;

import java.io.Serializable;

public class PeopleInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5724458758309849622L;
	private int id;
	private String userName;//检测人员
	private String departMent;//部门
	private String job;//职务
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDepartMent() {
		return departMent;
	}
	public void setDepartMent(String departMent) {
		this.departMent = departMent;
	}
	public PeopleInfo(String userName, String departMent,String job) {
		this.userName = userName;
		this.departMent = departMent;
		this.job = job;
	}
	public PeopleInfo() {
	}
}
