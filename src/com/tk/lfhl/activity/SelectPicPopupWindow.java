package com.tk.lfhl.activity;




import com.ramy.minervue.service.GasDetector;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

public class SelectPicPopupWindow extends PopupWindow implements OnClickListener{
	public static final String TAG = "SF6";

	private GasDetector detector =  GasDetector.getInstance();
	private boolean isopen = false;
	private Button  Read1,Read2,Read3,Read4,Read5,Read6;
	private View mMenuView;
	public Context context;
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(context, "ѭ�����͵�"+msg.arg1+"��",0).show();
		};
	};

	public SelectPicPopupWindow(final Activity context,OnClickListener itemsOnClick) {
		super(context);
		detector.close();
		this.context =context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.bottomdialog, null);
		Read1 =  (Button) mMenuView.findViewById(R.id.Read1);
		Read2 =  (Button) mMenuView.findViewById(R.id.Read2);
		Read3 =  (Button) mMenuView.findViewById(R.id.Read3);
		Read4 =  (Button) mMenuView.findViewById(R.id.Read4);
		Read5 =  (Button) mMenuView.findViewById(R.id.Read5);
		Read6 =  (Button) mMenuView.findViewById(R.id.Read6);
		Read1.setOnClickListener(this);
		Read2.setOnClickListener(this);
		Read3.setOnClickListener(this);
		Read4.setOnClickListener(this);
		Read5.setOnClickListener(this);
		Read6.setOnClickListener(this);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		//���ð�ť����
		//����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		//����SelectPicPopupWindow��������Ŀ�
		this.setWidth(w/2+50);
		//����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		//����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.mystyle);
		//ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0000000000);
		//����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		//mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});



	}


	@Override
	public void onClick(View v) {

		if(v==Read1){
//			Toast.makeText(context, "ѭ������100��",0).show();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
//					for(int i = 0 ;  i < 1000;i--){
						if(detector.open()){
							Log.e(TAG, "�򿪴��ڳɹ�.");
							isopen = true;
							GasDetector.CMD_SEND_TYPE = GasDetector.GAS_TYPE;
							if (!detector.doTurnOnDevice(GasDetector.READ_GASNAME)) {
								Log.e(TAG, "Error turning on GasDetector.");
								detector.close();
							}else{ 
								Log.e(TAG, "ͨѶ�ɹ�");
								//							Read1.performClick();
							}
						}else{
							detector.close();
							Log.e(TAG, "�򿪴���ʧ��.");
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
//						Message msg = Message.obtain();
//						msg.arg1 = i+1;
//						msg.what=1;
//						mHandler.sendMessage(msg);
//					}
				}
			}).start();
		}
		if(v==Read2){
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(detector.open()){
						Log.e(TAG, "�򿪴��ڳɹ�.");
						isopen = true;
						GasDetector.CMD_SEND_TYPE = GasDetector.GAS_SERIALIZABLE;
						if (!detector.doTurnOnDevice(GasDetector.READ_GAS_SER)) {
							Log.e(TAG, "Error turning on GasDetector.");
							detector.close();
						}else{ 
							Log.e(TAG, "ͨѶ�ɹ�");
						}
					}else{
						detector.close();
						Log.e(TAG, "�򿪴���ʧ��.");
					}
				}
			}).start();
		}
		if(v==Read3){
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(detector.open()){
						Log.e(TAG, "�򿪴��ڳɹ�.");
						isopen = true;
						GasDetector.CMD_SEND_TYPE = GasDetector.GAS_STATU;
						if (!detector.doTurnOnDevice(GasDetector.READ_GAS_STATU)) {
							Log.e(TAG, "Error turning on GasDetector.");
							detector.close();
						}else{ 
							Log.e(TAG, "ͨѶ�ɹ�");
						}
					}else{
						detector.close();
						Log.e(TAG, "�򿪴���ʧ��.");
					}
				}
			}).start();
		}
		if(v==Read4){
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(detector.open()){
						Log.e(TAG, "�򿪴��ڳɹ�.");
						isopen = true;
						GasDetector.CMD_SEND_TYPE = GasDetector.GAS_VSERSIO;
						if (!detector.doTurnOnDevice(GasDetector.READ_GAS_Vserion)) {
							Log.e(TAG, "Error turning on GasDetector.");
							detector.close();
						}else{ 
							Log.e(TAG, "ͨѶ�ɹ�");
						}
					}else{
						detector.close();
						Log.e(TAG, "�򿪴���ʧ��.");
					}
				}
			}).start();
		}
		if(v==Read5){
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(detector.open()){
						Log.e(TAG, "�򿪴��ڳɹ�.");
						isopen = true;
						GasDetector.CMD_SEND_TYPE = GasDetector.GAS_VALUE;
						if (!detector.doTurnOnDevice(GasDetector.READ_GAS_data)) {
							Log.e(TAG, "Error turning on GasDetector.");
							detector.close();
						}else{ 
							Log.e(TAG, "ͨѶ�ɹ�");
						}
					}else{
						detector.close();
						Log.e(TAG, "�򿪴���ʧ��.");
					}
				}
			}).start();
		}
		if(v==Read6){
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(detector.open()){
						Log.e(TAG, "�򿪴��ڳɹ�.");
						isopen = true;
						if (!detector.doTurnOnDevice(GasDetector.READ_GAS_data_setting)) {
							Log.e(TAG, "Error turning on GasDetector.");
							detector.close();
						}else{ 
							Log.e(TAG, "ͨѶ�ɹ�");
						}
					}else{
						detector.close();
						Log.e(TAG, "�򿪴���ʧ��.");
					}
				}
			}).start();
		} 
	}

}
