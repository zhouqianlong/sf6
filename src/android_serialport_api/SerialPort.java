package android_serialport_api;

import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
	private static final String TAG = "SerialPort";
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;

	static {
		System.loadLibrary("serial_ports");
	}

	public SerialPort(File device,String path,int baudrate,int flags)
			throws SecurityException, IOException {
		if ((!device.canRead()) || (!device.canWrite())) {
			try {
				Process su = Runtime.getRuntime().exec("/system/bin/su");
				String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
						+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || (!device.canRead())
						|| (!device.canWrite()))
					throw new SecurityException();
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}
		}
		
		this.mFd = open(device.getAbsolutePath(), baudrate, flags);
		if (this.mFd == null) {
			Log.e("SerialPort", "native open returns null");
			throw new IOException();
		}
		this.mFileInputStream = new FileInputStream(this.mFd);
		this.mFileOutputStream = new FileOutputStream(this.mFd);
	}
	
	
	public SerialPort(File device, int baudrate, int flags)
			throws SecurityException, IOException {
		if ((!device.canRead()) || (!device.canWrite())) {
			try {
				Process su = Runtime.getRuntime().exec("/system/bin/su");
				String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
						+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || (!device.canRead())
						|| (!device.canWrite()))
					throw new SecurityException();
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}
		}

		this.mFd = open(device.getAbsolutePath(), baudrate, flags);
		if (this.mFd == null) {
			Log.e("SerialPort", "native open returns null");
			throw new IOException();
		}
		this.mFileInputStream = new FileInputStream(this.mFd);
		this.mFileOutputStream = new FileOutputStream(this.mFd);
	}

	public InputStream getInputStream() {
		return this.mFileInputStream;
	}

	public OutputStream getOutputStream() {
		return this.mFileOutputStream;
	}

	private static native FileDescriptor open(String paramString,
			int paramInt1, int paramInt2);

	
//    public boolean openSerialPortString path,int baudrate,int databits, int stopbits, char parity
//        Log.i(TAG, "openSerialPort,path:" + path + " ,baudrate:" + baudrate + " ,databits:" +databits  
//                                            + " ,stopbits:" + stopbits + " ,parity:" + parity);  
//        if(isRunning){  
//            Log.i(TAG, "openSerialPort,the serial port is running");  
//            return false;  
//        }  
//        mFd = studioJni.serialPortOpen(path, baudrate,databits,stopbits,parity);  
//        if(mFd != null){  
//            isRunning = true;  
//            mFileInputStream = new FileInputStream(mFd);    
//            mFileOutputStream = new FileOutputStream(mFd);   
//        }else{  
//            Log.i(TAG, "openSerialPort," + "the deivce is null");  
//        }  
//        return isRunning;  
//    }
	public native void close();
}