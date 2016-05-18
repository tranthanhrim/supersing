//package myrecording;
//
//import java.util.List;
//
//import com.example.sssssss.MyRecordingFragment;
//import com.example.sssssss.R;
//
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.app.ActivityManager.RunningServiceInfo;
//import android.app.Application;
//import android.app.Fragment;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.graphics.drawable.AnimationDrawable;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.os.SystemClock;
//import android.support.v4.app.NotificationCompat;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//import myrecording.MyService.MyBinder;
//
//public class Playfile_myrecording extends Fragment implements OnClickListener {
//
//	ImageView _imgv;
//	Button _btplay, _btstop, _bt_pre;
//	//AnimationDrawable _frameAnimation;
//	TextView _msclock;
//	SeekBar _seekbar;
//	Thread _thread;
//	
//	String _pathfile = "";
//	// int _longjump = 5000; // fast forward time : 5s
//	 Handler _handler;
//	boolean isrunning = false;
//	// boolean _firstPlay = true;
//	// boolean _agree = false; // cho phép gán textview
//	// boolean _replay = false;
//	MyRecordingFragment _parent;
//	RelativeLayout _relativelayout;
//	MyService myservice;
//	private ServiceConnection connection;
//	Intent intent;
//	MyBroastCast mybroastcast;
//	static boolean isfirstVisited = true;
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		_relativelayout = (RelativeLayout) inflater.inflate(R.layout.activity_playfile_myrecording, null);
//	//	_imgv = (ImageView) _relativelayout.findViewById(R.id.imv_speaker);
//		_btplay = (Button) _relativelayout.findViewById(R.id.bt_play);
//		_btstop = (Button) _relativelayout.findViewById(R.id.bt_stop);
//		_bt_pre = (Button) _relativelayout.findViewById(R.id.bt_pre);
//		_msclock = (TextView) _relativelayout.findViewById(R.id.txtclock);
//		_seekbar = (SeekBar) _relativelayout.findViewById(R.id.seekbar_playfile);
//		//_frameAnimation = (AnimationDrawable) _imgv.getDrawable();
//
//		
//		_btplay.setOnClickListener(this);
//		_btstop.setOnClickListener(this);
//		_bt_pre.setOnClickListener(this);
//
//		_msclock.setText("00:00.0");
//		_pathfile = _parent.get_pathfile();
//		
//		mybroastcast = new MyBroastCast();
//		
//		
//
//		if(isfirstVisited){
//			intent = new Intent(getActivity(), MyService.class);
//			myservice = new MyService();
//
//			connection = new ServiceConnection() {
//				@Override
//				public void onServiceDisconnected(ComponentName name) {
//					// isBound = false;
//				}
//				@Override
//				public void onServiceConnected(ComponentName name, IBinder service) {
//					MyBinder binder = (MyBinder) service;
//					myservice = binder.getService(); // lấy đối tượng MyService
//					myservice.updateSourse(_pathfile);
//					_seekbar.setMax(myservice.getDuration());
//					_seekbar.setProgress(0);
//
////					_thread = repare();
////					isrunning = true;
////					_thread.start();
//				}
//			};
//			onService();
//			Toast.makeText(getActivity(), "dqwdwqdwq", Toast.LENGTH_LONG).show();;
//		}
//
//		
//		_handler = new Handler() {
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				Bundle bundle = msg.getData();
//				// String timer = bundle.getString("timer");
//				int currPos = (int) bundle.getLong("currPos");
//				String timer = bundle.getString("timer");
//				if(currPos > 0){
//				_seekbar.setProgress(currPos);
//				_msclock.setText(timer);
//				}
//			}
//		};
//		 _seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//		 int pro = 0;
//		
//		 @Override
//		 public void onStopTrackingTouch(SeekBar seekBar) {
//
//		
//		 
//		 //_mediaplayer.seekTo(seekBar.getProgress());
//		
//		 }
//		
//		 @Override
//		 public void onStartTrackingTouch(SeekBar seekBar) {
//		 // TODO Auto-generated method stub
//		
//		 }
//		
//		 @Override
//		 public void onProgressChanged(SeekBar seekBar, int progress, boolean
//		 fromUser) {
//		 // TODO Auto-generated method stub
//			 if(myservice.getCurr() != 0)
//				 myservice.seek(progress);
//		 }
//		 });
//		
//		 return _relativelayout;
//	}
//
//	@Override
//	public void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		
//		 if (_parent.getIndex_of_frag() == 1) { // nếu pause khi nhấn home
//			 String k = _parent.get_pathfile();
//			 k = k.substring( k.lastIndexOf("/")+ 1);
//			 show(k);
//			 MyBroastCast._parent = this;
//		 }
//		
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		
////		String old_pathfile = "", new_pathfile = "";
////		
////		old_pathfile = myservice.get_pathfile();
////		new_pathfile = _parent.get_pathfile();
////		if(!isfirstVisited && !old_pathfile.equals(new_pathfile)){
////			myservice.updateSourse(_pathfile);
////			_seekbar.setMax(myservice.getDuration());
////			_seekbar.setProgress(0);
////		}
////		if(!isfirstVisited && myservice.isPlaying() && old_pathfile.equals(new_pathfile)){
////			_seekbar.setMax(myservice.getDuration());
////			_btplay.setBackgroundResource(R.drawable.play2);
////			_frameAnimation.start();
////		}
////		isfirstVisited = false;
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		int id = v.getId();
//		switch (id) {
//		case R.id.bt_play:
//			play(v);
//			break;
//		case R.id.bt_stop:
//			stop(v);
//			break;
//		case R.id.bt_pre:
//			pre(v);
//			break;
//		}
//
//	}
//
//	private void stop(View v) {
//		
//		myservice.stopmedia();
//	//	_frameAnimation.stop();
//	//	_frameAnimation.selectDrawable(0);
//		_btplay.setBackgroundResource(R.drawable.ic_myrecord_play1);
//		
//		_msclock.setText("00:00.0");
//		_seekbar.setProgress(0);
//	//	onService();
//	}
//
//	private void play(View v) {
//		String k = _parent.get_pathfile();
//		if(k.equals(""))
//			Toast.makeText(getActivity(), "Dont have file to play", Toast.LENGTH_LONG).show();
//		else{
//			k = k.substring( k.lastIndexOf("/") + 1);
//			Toast.makeText(getActivity(), k, Toast.LENGTH_LONG).show();
//		}
//			
//		if (_pathfile.equals(myservice.get_pathfile())) {
//			if (myservice.isPlaying()) {
//				myservice.pausemedia();
//		//		_btplay.setBackgroundResource(R.drawable.ic_myrecord_play1);
//		//		_frameAnimation.stop();
//		//		_frameAnimation.selectDrawable(0);
//			} else {
//				myservice.playmedia();
//		//		_btplay.setBackgroundResource(R.drawable.ic_myrecord_play2);
//		//		_btplay.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_myrecord_play2));
//		//		_frameAnimation.start();
//}
//		} else {
//			myservice.updateSourse(_pathfile);
//			myservice.seek(0);
//			}
//	}
//
//	private void pre(View v) {
//		myservice.premedia();
//	}
//
//	public Thread repare() {
//		Thread th = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while (isrunning) {
//					SystemClock.sleep(100);
//					if (myservice != null) {
//						int curr = myservice.getCurr();
//						String timer = milliSecondsToTimer(curr);
//						Bundle bundle = new Bundle();
//						bundle.putLong("currPos", curr);
//						bundle.putString("timer", timer);
//						Message msg = _handler.obtainMessage();
//						msg.setData(bundle);
//						_handler.sendMessage(msg);
//					}
//
//				}
//			}
//		});
//		return th;
//	}
//
//	public void show(String filename){
//		 NotificationCompat.Builder builder =  new NotificationCompat.Builder(getActivity());
//	        NotificationManager notificationManager =  (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//	        Intent intent = new Intent(getActivity(), getActivity().getClass());
//	        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//	        
//	        
//	        
//	        Intent stopReceive = new Intent();  
//	        stopReceive.setAction("STOP_ACTION");
//	        PendingIntent pendingIntentStop = PendingIntent.getBroadcast(getActivity(), 12345, stopReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//	        builder.addAction(R.drawable.ic_sstop_no, "", pendingIntentStop);
//
//	        Intent playReceive = new Intent();  
//	        playReceive.setAction("PLAY_ACTION");
//	        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(getActivity(), 12345, playReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//	        builder.addAction(R.drawable.ic_play_no, "", pendingIntentPlay);
//
//	        Intent preReceive = new Intent();  
//	        preReceive.setAction("PRE_ACTION");
//	        PendingIntent pendingIntentPre = PendingIntent.getBroadcast(getActivity(), 12345, preReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//	        builder.addAction(R.drawable.ic_pre_no, "", pendingIntentPre);
//	        
//	        
//	        builder.setSmallIcon(R.drawable.ic_launcher);
//	     //   builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.large_love));
//	        builder.setContentTitle(filename);
//	     //   builder.setContentText("Welcome to  Base Notification");
//	     //   builder.setSubText("Go to love-activity");
//	        builder.setAutoCancel(true); // Tự động cancel khi ấn vào notification
//	        builder.setContentIntent(pendingIntent);
//	        notificationManager.notify(1, builder.build());
//	}
//	
//	
//	
//	public MyService getMyservice() {
//		return myservice;
//	}
//
//
//	private void onService() {
//		getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
//	}
//
//	private void offService() {
//		getActivity().unbindService(connection);
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onActivityCreated(savedInstanceState);
//	}
//
//	public void setParent(MyRecordingFragment parent) {
//		_parent = parent;
//	}
//	
//	public String milliSecondsToTimer(long milliseconds) {
//		String finalTimerString = "";
//		String secondsString = "";
//		String minutesString = "";
//
//		int _hours = (int) (milliseconds / (1000 * 60 * 60));
//		int _minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
//		int _seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
//		int _millisconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) % 1000);
//		_millisconds /= 100;
//		if (_hours > 0) {
//			finalTimerString = _hours + ":";
//		}
//
//		if (_seconds < 10) {
//			secondsString = "0" + _seconds;
//		} else {
//			secondsString = "" + _seconds;
//		}
//		if (_minutes < 10) {
//			minutesString = "0" + _minutes;
//		} else {
//			minutesString = "" + _minutes;
//		}
//
//		finalTimerString = finalTimerString + minutesString + ":" + secondsString + "." + _millisconds;
//		return finalTimerString;
//	}
//
//	public long milliSecondsToSecond(long mill) {
//		return mill / 1000;
//	}
//}
