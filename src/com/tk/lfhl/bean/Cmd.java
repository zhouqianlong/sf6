package com.tk.lfhl.bean;

import java.io.Serializable;
import java.util.Locale;

import android.util.Log;

public class Cmd implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6221321915883243778L;
	private String [] hexDate;//16��������
	private String [] tenDate;//10��������
	private String [] twoDate;//����������
	private String [] asiccDate;//�ַ�������
	private String [] cmdDate;//�����ַ���



	public String getStringHexDate() {
		StringBuffer sb = new StringBuffer();
		for(String st:hexDate){
			sb.append(st+" ");
		}
		return sb.toString();
	}
	public String getStringTenDate() {
		StringBuffer sb = new StringBuffer();
		for(String st:tenDate){
			sb.append(st+" ");
		}
		return sb.toString();
	}
	public String getStringAsiccDate() {
		StringBuffer sb = new StringBuffer();
		for(String st:asiccDate){
			sb.append(st+" ");
		}
		return sb.toString();
	}
	public String getStringArrayAsiccDate() {
		StringBuffer sb = new StringBuffer();
		for(String st:cmdDate){
			sb.append(st+" ");
		}
		Log.i("SF6", "������"+sb.toString());
		return sb.toString();
	}

	public String[] getHexDate() {
		return hexDate;
	}
	public String[] getTenDate() {
		return tenDate;
	}
	public String[] getTwoDate() {
		return twoDate;
	}
	public String[] getAsiccDate() {
		return asiccDate;
	}
	public String[] getCmdDate() {
		return cmdDate;
	}
	

	public void setTenDate(byte[] hexDate,int len) {
		String [] str = new String[len];
		for(int i = 0 ; i < len;i++){
			str[i]= String.valueOf(hexDate[i]);
		}
		setTenDate(str);
		setHexDate(toHexs(hexDate, len));
		setAsiccDate(toAscii(str, len));
		setCmdDate(arrayAscii(asiccDate, asiccDate.length));
		Log.i("CMDTest","hexDate:"+hexDate.length+"tenDate:"+tenDate+"ascii:"+asiccDate.length);
	}

	public void setHexDate(String[] hexDate) {
		this.hexDate = hexDate;
	}
	public void setHexDate(byte[] hexDate) {
		String [] str = new String[hexDate.length];
		for(int i = 0 ; i < hexDate.length;i++){
			str[i]= String.valueOf(hexDate[i]);
		}
		this.hexDate = str;
	}
	public void setTenDate(String[] tenDate) {
		this.tenDate = tenDate;
	}
	/**
	 * ����10��������
	 * ��������16���ƺ�ascii�ֽ�����  
	 * @param hexDate
	 * @param len
	 */
	public void setTwoDate(String[] twoDate) {
		this.twoDate = twoDate;
	}

	public void setAsiccDate(String[] asiccDate) {
		this.asiccDate = asiccDate;
	}

	public void setCmdDate(String[] cmdDate) {
		this.cmdDate = cmdDate;
	}








	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Cmd() {
	}
	public Cmd(String[] hexDate, String[] tenDate, String[] twoDate,
			String[] asiccDate) {
		super();
		this.hexDate = hexDate;
		this.tenDate = tenDate;
		this.twoDate = twoDate;
		this.asiccDate = asiccDate;
	}


	/**
	 *��10��������ת��Ϊʮ�����Ʊ�����ַ�����
	 * 
	 * @param data
	 *            data to hex encode.
	 * 
	 * @return ʮ�����Ʊ����ַ�����
	 */
	public static String[] toHexs(byte[] data, int length) {
		String[] sb = new String[length];
		for (int i = 0; i < length; i++) {
			String hex = Integer.toHexString(data[i]);
			if (hex.length() == 1) {
				// Append leading zero.
				sb[i] = "0"+hex.toUpperCase(Locale.getDefault());
			} else if (hex.length() == 8) {
				// Remove ff prefix from negative numbers.
				hex = hex.substring(6);
				sb[i] = hex.toUpperCase(Locale.getDefault());
			}else{
				sb[i] = hex.toUpperCase(Locale.getDefault());
			}
		}
		return sb;
	}


	/**
	 * ��10����ת����Ascii�ַ�
	 * @param ascii  			String s="22307 35806 24555 20048";//ASCII�� �ո�Ϊһ���ַ�
	 * @return								ʥ �� �� ��
	 * @throws Exception
	 */
	public static String[] toAscii(String[] chars,int length){//ASCIIת��Ϊ�ַ���
		String[] st = new String[length];
		for(int i=0;i<length;i++){ 
			st[i] = String.valueOf((char)Integer.parseInt(chars[i]));
		} 
		return st;
	}
	
	public static String[] arrayAscii(String[] chars,int length){
		String[] st = new String[length];
		int index = 0;
		for(int i = 0 ; i <length;i++){
			if(!chars[i].equals(":")){
				if(chars[i].equals("\r")){
					break;
				}
				st[index] =chars[i]+chars[i+1]; //37, 03, 08, 31, 30, 00, 06, 01, C7, 00, 00, A3
				index++;
				i++;
			}
		}
		String[] date = new String[index];
		System.arraycopy(st, 0, date, 0, index);
		return date;
	}

}
