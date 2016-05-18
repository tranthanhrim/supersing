package myrecording;


import java.io.IOException;

import com.example.sssssss.MyRecordingFragment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.widget.Toast;

public class MyService extends Service {
	public static MyRecordingFragment _parent = new MyRecordingFragment();
	
	private MyMedia myPlayer;
	private IBinder binder;
	
	@Override
	public void onCreate() {
		try {
			myPlayer = new MyMedia(this);
			myPlayer.p = this;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		binder = new MyBinder();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		myPlayer.stop();
		return super.onUnbind(intent);
	}

	public class MyBinder extends Binder {
		public MyService getService() {
			return MyService.this;
		}
	}

	public String get_pathfile() {
		if(myPlayer != null)
		return myPlayer.get_pathfile();
		else
			return "";
	}
	public void resert(){
		myPlayer.reset();
	}
	public void set_pathfile(String _pathfile) {
		myPlayer.set_pathfile(_pathfile);
	}

	public String updateSourse(String path) {
		if(myPlayer != null)
			return myPlayer.updateSourse(path);
		else{
			myPlayer = new MyMedia(this);
			return myPlayer.updateSourse(path);
		}
		
	}
	public void playmedia() {
		myPlayer.play();
	}
	public void stopmedia() {
		myPlayer.stop();
	}
	public void pausemedia() {
		myPlayer.pause();
	}
	public void premedia() {
		myPlayer.previou();
	}
	public boolean isPlaying(){
		return myPlayer.isPlaying();
	}
	public void seek(int curr){
		myPlayer.seek(curr);
	}
	public int getCurr(){
		return myPlayer.getCurr();
	}
	public int getDuration(){
		return myPlayer.getDuration();
	}

}

