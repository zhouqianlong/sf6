package com.android.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tk.lfhl.activity.R;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class PlayAudio {
	private SoundPool spl;
	private HashMap<Integer,Integer> spMap;
	public Activity activity;
	public Context context;
	private int type ;//报警方式
	private List<Integer> list = new ArrayList<Integer>();
	public PlayAudio(Context  context){
		this.context= context;
		initAudio();

	}

	public void setType(int type) {
		this.type = type;
	}

	public void initAudio(){
		spl = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
		spMap = new HashMap<Integer,Integer>();
		spMap.put(0, spl.load(context,R.raw.fmq, 1));
		spMap.put(1, spl.load(context,R.raw.fmq, 1));
	}
	int count = 0;
	long beginTime = -1;
	boolean isPlaying  = false;
	public void playSounds(){
		if(isPlaying==false){
			isPlaying=true;
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						spl.play(spMap.get(0),  1, 1, 0, 0, 1);
						Log.i("AUDIOTEST", "play:===缓存歌曲数："+list.size());
						Thread.sleep(1000);
						isPlaying = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	public boolean add(int value){
		for(int i = 0 ; i < list.size();i++){
			if(list.get(i).equals(value)){
				return false;
			}
		}
		Log.i("AUDIOTEST", "list.add("+value+")");	
		return list.add(value);
	}
	//	public void delete(int value){
	//		for(int i = 0 ; i < list.size();i++){
	//			if(list.get(i).equals(value)){
	//				list.remove(i);
	//				Log.i("AUDIOTEST", "list.remove("+i+")");
	//			}
	//		}
	//	}

	public void playSounds(int sound, int number,boolean a){
		AudioManager am = (AudioManager)context.getSystemService("audio");
		float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float volumnRatio = audioCurrentVolumn/audioMaxVolumn;
		//			spl.play(spMap.get(sound), volumnRatio, volumnRatio, 0, number, 1);
		spl.play(sound, 1, 1, 0, 0, 1);
		//Log.i("syso","play:"+count++);
		beginTime = System.currentTimeMillis();
	}
	public void audioRelease(){
		spl.release();
	}

}
