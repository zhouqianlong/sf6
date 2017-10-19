/**
 * 
 */
package com.ramy.minervue.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android_serialport_api.SerialPort;

import com.android.utils.MyToask;
import com.tk.lfhl.activity.MainActivity;
import com.tk.lfhl.bean.Cmd;

/**
 * @author tao
 * 
 */
public class GasDetector {
	public static  int CMD_SEND_TYPE = -1;//Э������
	public static  int GAS_TYPE = 0x0080;//��������������
	public static  int GAS_SERIALIZABLE = 0x0086;//�������ӵ������ͺ�
	public static  int GAS_STATU = 0x0009;//������������ǰ״ֵ̬
	public static  int GAS_VSERSIO = 0x0084;//������������ǰ״ֵ̬
	public static  int GAS_VALUE = 0x000A;//��������������Ũ��
	
	
	
	
	private static final String TAG = "SF6";
	private static final GasDetector instance = new GasDetector();
	private static final String DEVICE_PATH = "/dev/ttyMT0";
	private static final int BAUDRATE = 2400;//19200
	protected SerialPort mSerialPort;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	byte[] buffer;
	String [] rBuffer;

	private boolean isOpened;
	private ExecutorService executorService;

	public static int lastReplyCmd = -1;//����
	public static int lastReplyData = -1;//Ũ��
	public static String lastReplyString;//Ӧ������ Ũ��
	private String lastReplyVersionString;//Ӧ������  ���к�

	private boolean error_status =false;

	public int getLastReplyCmd() {
		return lastReplyCmd;
	}
	public int getLastReplyData() {
		return lastReplyData;
	}
	public void initLastReplyData() {
		lastReplyData = -1;
	}
	public String getLastReplyString() {
		return lastReplyString;
	}
	public String getlastReplyVersionString() {
		return lastReplyVersionString;
	}

	private GasDetector() {
		buffer = new byte[64];
		rBuffer = new String[128];
		this.isOpened = false;
		this.mSerialPort = null;
		this.executorService = Executors.newFixedThreadPool(1);
	}
	//���ݹؼ��ַָ����һ����¼
	public byte[] splitByte(int key,byte[] buffer,int bufferLen){
		int position = -1;//��¼���һ�γ��ֵ�λ��
		for(int i = 0 ; i< bufferLen;i++){
			if(buffer[i]==key){
				position = i;
			}
		}
		byte[] data = new byte[bufferLen-position];
		//[58, 51, 55, 48, 51, 48, 48, 56, 48, 48, 48, 48, 52, 65, 55, 13, 10, 58, 51, 55, 48, 51, 48, 56, 53, 51, 52, 68, 52, 54, 50, 68, 53, 51, 52, 54, 51, 54, 50, 48, 54, 69, 13, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		for(int i = 0 ; i < data.length;i++){
			data[i] = buffer[i+position];
		}
		return data;
	}

	String check ="";
	SPReadTask task;
	private int readWithTimeout(long timeout) {
		Future<Integer> future = executorService.submit(task);
		Cmd cmd = new Cmd();//��������
		if(executorService == null||executorService.isShutdown()) 	
			this.executorService = Executors.newFixedThreadPool(1);
		try {
			Integer readByte = future.get(timeout, TimeUnit.MILLISECONDS);//&0x7f
			task.buffer_len = 0;
			isRead = false;
			for(int i =0 ; i < readByte;i++){
//								Log.i(TAG, "buffer:"+buffer[i]+"//"+getDecordOneZero(buffer[i]));//��żУ��
//				buffer[i]|=getDecordOneZero(buffer[i]);
				buffer[i] = (byte) (buffer[i]&0x7f);//
			}
			//ȥ���ظ���������һ������   
			//����Э��涨һ������   ��ʼλΪ ":" (�ֺ�)  ��Ӧ��buffer���е�ʵ��ֵ Ϊ ��58��  
			byte[] splitbuffer = splitByte(58, buffer, readByte);
			cmd.setTenDate(splitbuffer,splitbuffer.length);
			Log.d(TAG, "��������:"+MyNumberUtils.toHexs(splitbuffer, splitbuffer.length));
			if(CMD_SEND_TYPE == GAS_TYPE){//��������������
				String[] buffer = cmd.getCmdDate();
				if(buffer[1].equals("03")&&buffer[2].equals("08")){
					StringBuffer sb = new StringBuffer();
					for(int i = 3 ; i < buffer.length-1;i++){
						sb.append((char)Integer.parseInt(MyNumberUtils.sixteen_to_ten(buffer[i])+""));
					}
					Log.i(TAG,sb.toString());//
					this.lastReplyString = sb.toString();
					this.lastReplyCmd = GAS_TYPE;
				}

			}
			if(CMD_SEND_TYPE==GAS_SERIALIZABLE){//����������Ψһ���к�
				String[] buffer = cmd.getCmdDate();
				if(buffer[1].equals("03")&&buffer[2].equals("08")){//[37, 03, 08, 31, 30, 00, 06, 01, C7, 00, 00, A3]
					StringBuffer sb = new StringBuffer();
					int heard1 = MyNumberUtils.sixteen_to_ten(buffer[3]);
					int heard2 = MyNumberUtils.sixteen_to_ten(buffer[4]);
					int heard3 = MyNumberUtils.sixteen_to_ten(buffer[5]);
					int heard4 = MyNumberUtils.sixteen_to_ten(buffer[6]);
					int heard56 = Integer.valueOf(buffer[7]+buffer[8], 16);//01C7
					char h1 = (char)Integer.parseInt(heard1+"");
					char h2 = (char)Integer.parseInt(heard2+"");
					String h3,h4 = "";
					if(heard3<10)
						h3 = "0"+heard3;
					else
						h3 = String.valueOf(heard3);
					if(heard4<10)
						h4 = "0"+heard4;
					else
						h4 = String.valueOf(heard4);
					sb.append(h1+""+h2+"-"+h3+""+h4+"-"+heard56);
					Log.i(TAG,sb.toString());//
					this.lastReplyVersionString = sb.toString();
					this.lastReplyCmd = GAS_SERIALIZABLE;
				}
			}
			if(CMD_SEND_TYPE==GAS_STATU){//������������ǰ״ֵ̬
				String[] buffer = cmd.getCmdDate();//[37, 03, 02, 00, C0, FE]
				if(buffer[1].equals("03")&&buffer[2].equals("02")){
					StringBuffer sb = new StringBuffer();
					String statuDate = MyNumberUtils.hexStringToBinary(buffer[3]+buffer[4]);
					Log.i(TAG,statuDate.toString());//
					this.lastReplyString = statuDate.toString();
					this.lastReplyCmd = GAS_STATU;
				}
			}
			if(CMD_SEND_TYPE==GAS_VSERSIO){//��������������汾
				//��������������汾
				String[] buffer = cmd.getCmdDate();
				if(buffer[1].equals("03")&&buffer[2].equals("04")){
					StringBuffer sb = new StringBuffer();
					for(int i = 3 ; i < buffer.length-1;i++){
						sb.append((char)Integer.parseInt(MyNumberUtils.sixteen_to_ten(buffer[i])+""));
					}
					Log.i(TAG,sb.toString());//
					this.lastReplyString = sb.toString();
					this.lastReplyCmd = GAS_VSERSIO;
				}
			}
			
			if(CMD_SEND_TYPE==GAS_VALUE){//��������Ũ��
				//��������Ũ��
				String[] buffer = cmd.getCmdDate();
				if(buffer[1].equals("03")&&buffer[2].equals("02")){
					StringBuffer sb = new StringBuffer();
					//ģ������
//					int data  = Integer.valueOf(MainActivity.getInstance.et_gasvalue.getText().toString());
//					sb.append(Integer.valueOf(""+data, 10));//01C7
					sb.append(Integer.valueOf(buffer[3]+buffer[4], 16));//01C7
					Log.i(TAG,"SF6Ũ�ȣ�"+sb.toString());//
					int values =  Integer.parseInt(sb.toString());
					MainActivity.getInstance.value = values;
					this.lastReplyString = values+"";
					this.lastReplyData = values;
					this.lastReplyCmd = GAS_VALUE;
					return readByte;
				}
				return readByte;
			}


//
//			Message msg = Message.obtain();
//			msg.what=1;
//			//			msg.obj = "16������ʾ"+cmd.getStringHexDate()+"\n10������ʾ"+cmd.getStringTenDate()+"\nASCII��ʾ"+cmd.getStringAsiccDate()+"\n"+cmd.getStringArrayAsiccDate();
//			msg.obj=cmd.getStringArrayAsiccDate()+"\n"+lastReplyString;
//			mHandler.sendMessage(msg);

			if(readByte==10){
				this.lastReplyCmd = GAS_TYPE;//  ��ȡ���������Ƴɹ�
				//				this.lasReplyDataAD = ;
				this.lastReplyString = "";
			}


			return readByte;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} 
		catch (TimeoutException e) {
			e.printStackTrace();
			error_status  = true;
			Log.d(TAG, future.cancel(true)+"("+task.id+")TIME OUT SP read:" + MyNumberUtils.toHex(buffer, readLen));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	

	public String [] getArrayBuffer(String buffer){//370308534D462D534636206E
		String [] myBuffer = new String [buffer.length()/2];
		int len = 0;
		for(int i = 0; i < buffer.length();i++){
			myBuffer[len] = String.valueOf((buffer.charAt(i)+""+buffer.charAt(i+1)));
			len++;
			i++;
		}
		return myBuffer;
	}
	//false  Ϊż��
	private static byte getDecordOneZero(byte num) {
		byte number =   (byte) num;//�ȴ����������
		byte srcnumber =number;
		printHexString(srcnumber);//��ӡ
		int count =0;//������
		for(int i = 0 ; i < 8 ; i ++ ){
			byte number2  =   (byte) (number&0x01);// 0   ��ʱ����
			if(number2==1){
				count++;
			}
			number = (byte) (number>>1);
			//			printInfo(number);
		}

		if(count%2==0){//�Ƿ���ż��
			srcnumber= 0;
		}else{
			srcnumber =(byte) 0x80;
		}
//
//		num  =  (byte) (num&0x80);
//		if(srcnumber==num){
//			return true;
//		}else{
//			return false;
//		}
		return srcnumber;

	}

	private static String GetResult(String resultCmd) throws Exception {
		String[]chars=resultCmd.split(" ");
		StringBuffer sbsix = new StringBuffer();
		//�����ת����10��������
		for(int i =0 ; i <chars.length;i++ ){
			sbsix.append(MyNumberUtils.sixteen_to_ten(chars[i])+" ");//���һ�λ����һ��ռ2���ַ��Ŀո�
		}
		//��10��������ת�����ַ���
		String result = MyNumberUtils.ASCII_To_String(sbsix.toString());
		//		System.out.println("�ַ�����"+result);
		String [] s  =result.split(":");
		String indexResult =s[s.length-1];
		StringBuffer sb = new StringBuffer();
		for(int i = 1 ; i <= indexResult.length()-4;i++){//��������У���  + 2���ַ��Ŀո�
			sb.append(indexResult.charAt(i-1));
			if(i%2==0){
				sb.append(" ");	
			}
		}
		String[] sbr = sb.toString().split(" ");
		//			System.out.println(sb.toString());
		sb = null;
		sb = new StringBuffer();
		for(int i = 0 ; i < sbr.length;i++){
			if(!sbr[i].equals("\r\n")){
				sb.append(MyNumberUtils.sixteen_to_ten(sbr[i])+" ");
			}
		}
		//			System.out.println(sb.toString());
		System.out.println(MyNumberUtils.ASCII_To_String(sb.toString()));
		return MyNumberUtils.ASCII_To_String(sb.toString());

	}



	private boolean sendAndRecvWithRetry(byte[] cmd, int retry) {//[58, 51, 55, 48, 51, 48, 48, 56, 48, 48, 48, 48, 52, 65, 55, 13, 10]
		StringBuffer getsb = new StringBuffer();
		for( int i = 0 ; i < cmd.length;i++){
			getsb.append(getCheckOneZero(cmd[i]));
		}
		byte[] cmd2 = MyNumberUtils.fromHex(getsb.toString());
		getsb = new StringBuffer();
		for(int i =0; i < cmd2.length;i++){
			getsb.append(cmd2[i]+",");
		}
//		Log.d(TAG, "����ת��д��:"+getsb.toString()+"\\"+MyNumberUtils.toHexs(cmd2, cmd2.length));
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		for(int i =0;i<cmd.length;i++){
			sb.append(cmd[i]+",");
			sb2.append(MyNumberUtils.ten_to_Sixteen(cmd[i])+",");
		}
		if (!isOpened || cmd == null || cmd.length <= 0) {
			return false;
		}
		byte[] toSend = new byte[cmd.length];
//		buffer[i]|=getDecordOneZero(buffer[i]);
		System.arraycopy(cmd, 0, toSend, 0, cmd.length);//Ĭ�Ϸ�ʽ
		//		System.arraycopy(cmd2, 0, toSend, 0, cmd2.length);//��żУ�鷽ʽ
		int count = 0;
		while (count < retry) {
			count++;
			try {
				Log.d(TAG, "��������:"+sb.toString()+"\\"+MyNumberUtils.toHexs(cmd, cmd.length));
				sb = new StringBuffer();
				for(int i = 0 ; i < toSend.length;i++){
					toSend[i]|=getDecordOneZero(toSend[i]);
					sb.append(toSend[i]+",");
				}
				Log.d(TAG, "��������:"+sb.toString()+"\\"+MyNumberUtils.toHexs(toSend, toSend.length));
				this.mOutputStream.write(toSend);
				if (this.readWithTimeout(2001) > 0) {
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static GasDetector getInstance() {
		return instance;
	}

	public synchronized boolean open() {
		if (isOpened) {
			return true;
		}

		try {
			this.mSerialPort = new SerialPort(new File(DEVICE_PATH), BAUDRATE,
					0);
			isOpened = true;
			this.mOutputStream = this.mSerialPort.getOutputStream();
			this.mInputStream = this.mSerialPort.getInputStream();
			task = new SPReadTask();
			return true;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized void close() {
		if (this.mSerialPort != null) {
			this.mSerialPort.close();
		}
		this.mSerialPort = null;
		this.isOpened = false;
	}

	public static final String READ_GASNAME   		 ="0300800004";//���������ͺ�
	public static final String READ_GAS_SER   		 ="0300860004";//��������Ψһ���к�
	public static final String READ_GAS_STATU 		 ="0300090001";//����������ǰ״̬��Ϣ
	public static final String READ_GAS_Vserion      ="0300840002";//������������汾
	public static final String READ_GAS_data   		 ="03000A0001";//������������Ũ��
	public static final String READ_GAS_data_setting ="0300050001";//������������Ũ�ȵ��趨ֵ


	private byte[]  getCMD(int address,String cmd) {
		StringBuffer sb = new StringBuffer();//����ַ+����+У��λ��
		String add = MyNumberUtils.ten_to_Sixteen(address);
		Log.i(TAG, "��������ַ��"+add);
		if(add.length()<2){//����
			sb.append("0"+add);
		}else{
			sb.append(add);
		}
		sb.append(cmd);//����
		sb.append(MyNumberUtils.getCheckSum(MyNumberUtils.fromHex(sb.toString())));//У���
		byte [] sixdate = MyNumberUtils.getTen(sb.toString());//ת��Ϊ16����byte[]����
		byte[] sendCmd = new byte[sixdate.length+3];//�����������   ͷ+����+β    = 3λ
		sendCmd[0] = 0x3a;
		System.arraycopy(sixdate, 0, sendCmd, 1, sixdate.length);//�ӵڶ���λ�ÿ�ʼ����
		sendCmd[sixdate.length+1] = 0x0d; 
		sendCmd[sixdate.length+2] = 0x0a;
		return sendCmd;
	}
	int address  = 0; 

	/**
	 * ���Զ�ȡ����������
	 */
	public synchronized boolean doTurnOnDevice(String doCmd) {	//3a,35,33,30,33,30,30,38,30,30,30,30,34,41,39,0d,0a
		int address  = Integer.valueOf(MainActivity.getInstance.sttb.getADDRESS());//��������ַ 
//		if(address>=255){
//			address=0;
//		}
//		address++;
//		MyToask.showTextToast("��ַ:"+address,MainActivity.getInstance.getApplicationContext(), 0);
		return this.sendAndRecvWithRetry(getCMD(address, doCmd), 1);//[58, 51, 55, 48, 51, 48, 48, 56, 48, 48, 48, 48, 52, 65, 55, 13, 10]
	}
	public synchronized boolean doTurnOnDevice(String doCmd,int address) {	//3a,35,33,30,33,30,30,38,30,30,30,30,34,41,39,0d,0a
		return this.sendAndRecvWithRetry(getCMD(address, doCmd), 3);//[58, 51, 55, 48, 51, 48, 48, 56, 48, 48, 48, 48, 52, 65, 55, 13, 10]
	}
	int readLen;
	public  boolean isRead = false;
	class SPReadTask implements Callable<Integer> {
//		byte[] checkBuffer = new byte[512];
		int buffer_len = 0;//����������
		int id = (int)(Math.random()*9000000) + 1;
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Integer call() throws Exception {

			while (true) {
								Thread.sleep(200);
				//				Log.e(TAG, "Read:One:"+readLen);
				//				Log.e(TAG, "SP read Read:One :" + BinaryUtils.toHex(sp_buffer, buffer_len));
				if(isRead==true){
					return 1;
				}
				readLen = mInputStream.read(buffer);
				//				//1�� �����ݷ��뵽����������д���  ���ҽ��� �ؼ�֡������֤
//				System.arraycopy(buffer, 0, checkBuffer, buffer_len, readLen);
//				buffer_len = buffer_len+readLen;
				Log.e(TAG,"��ȡ��"+MyNumberUtils.toHex(buffer, readLen));
//				if(check()){
//					isRead = true;//�Ѷ�
				isRead = true;
					return readLen;
//				}
//				if(buffer_len>=512){
//					throw new Exception();  
//				}
			}

		}
//		private boolean check() {
//			for(int i = 0 ; i < checkBuffer.length-1;i++){
//				if(checkBuffer[i]==13&&checkBuffer[i+1]==10){
//					return true;
//				}
//			}
//			return false;
//		}
	}

	Context mContext = null;
	public void setContext(Context context){
		this.mContext = context;
	}


	private  String getCheckOneZero(byte num) {
		byte number =   (byte) (num&0x7f);//�ȴ����������
		byte srcnumber =number;
		//		printHexString(srcnumber);
		int count =0;//������
		for(int i = 0 ; i < 8 ; i ++ ){
			byte number2  =   (byte) (number&0x01);// 0   ��ʱ����
			if(number2==1){
				count++;
			}
			number = (byte) (number>>1);
			//			printInfo(number);
		}

		if(count%2==0){//�Ƿ���ż��
			srcnumber = (byte) (srcnumber&0x7f);
		}else{
			srcnumber = (byte) (srcnumber|0x80);
		}
		return printHexString(srcnumber);
	}

	public static  String printHexString( byte  b) { 
		String hex = Integer.toHexString(b& 0xFF); 
		if (hex.length() == 1) { 
			hex = '0' + hex; 
		} 
		return hex.toUpperCase(); 
	}


	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			try {
				new AlertDialog.Builder(mContext).setMessage(""+msg.obj.toString()).setPositiveButton("ȷ��", null).show();
			} catch (Exception e) {
				new AlertDialog.Builder(MainActivity.getInstance).setMessage(""+msg.obj.toString()).setPositiveButton("ȷ��", null).show();
			}
		};
	};
} 
