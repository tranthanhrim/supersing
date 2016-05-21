package myrecording;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;

public class MyMedia {
	private MediaPlayer mediaPlayer;
	public static MyService p = new MyService();
	private int dur = 0;
	String _pathfile = "";
	int _longjump = 5000;
	boolean complete = false;
	public MyMedia(Context context) {

	}
	public void reset(){
		mediaPlayer.reset();
		 dur = 0;
		_pathfile = "";
		_longjump = 5000;
		complete = false;
	}
	
	public void play() {
		if (mediaPlayer != null) {
			mediaPlayer.start();
			complete = false;
		}
	}

	public void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			mediaPlayer.seekTo(0);
			complete = false;
		}
	}

	public void pause() {
		if (mediaPlayer != null) {
			mediaPlayer.pause();
			complete = false;
		}
	}

	public void previou() {
		int curr = mediaPlayer.getCurrentPosition() + _longjump;
		mediaPlayer.seekTo(curr);
		complete = false;
	}
	public void seek(int curr){
		mediaPlayer.seekTo(curr);
		complete = false;
	}
	public String updateSourse(String path) {
		complete = false;
		try {
			_pathfile = path;
			if(mediaPlayer != null ){
				mediaPlayer.pause();
			}
				mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(_pathfile);
			mediaPlayer.setLooping(true);
			mediaPlayer.prepare();
			//mediaPlayer.start();
			dur = mediaPlayer.getDuration();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _pathfile;
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public int getCurr() {
		return mediaPlayer.getCurrentPosition();
	}
	public int getDuration(){
		return dur;
	}
	public String get_pathfile() {
		return _pathfile;
	}
	public void set_pathfile(String path) {
		try {
			_pathfile = path;
			if(mediaPlayer != null && mediaPlayer.isPlaying())
				mediaPlayer.reset();
			if(mediaPlayer == null)
				mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(_pathfile);
			mediaPlayer.prepare();
			//mediaPlayer.start();
			dur = mediaPlayer.getDuration();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
