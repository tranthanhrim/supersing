package com.example.sssssss;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import myrecording.MyBroastCast;
import myrecording.MyService;
import myrecording.myLib;
import myrecording.MyService.MyBinder;
import notactivity.AudioFile;
import notactivity.CustomAdapter;

public class MyRecordingFragment extends Fragment implements OnClickListener {

	String _pathfolder = "";
	RelativeLayout _relativeLayout;
	Button bt_play, bt_stop, bt_pre;
	static Handler _handler_readlistfile, _handler_updateseekbar;
	ArrayList<AudioFile> _listfile = new ArrayList<AudioFile>();
	//// listfile////
	ListView listview;
	CustomAdapter aa;
	AlertDialog renameDialog;
	AlertDialog deleteDialog;
	EditText et_rename;
	int index_selected = -1;
	//// Play file///
	SeekBar seekbar;
	static MyService myservice;
	 ServiceConnection connection;
	Intent intent;
	static String _pathfile = "";
	static boolean isfirstVisited = true;
	boolean isrunning = false;
	MyBroastCast mybroastcast;
	Notification _notification;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		_pathfolder = Environment.getExternalStorageDirectory() + "/Record";
		File f = new File(_pathfolder);
		if (!f.exists())
			f.mkdir();
	

	}


	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		_relativeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_my_recording_fragment, null);

		bt_play = (Button) _relativeLayout.findViewById(R.id.bt_play);
		bt_stop = (Button) _relativeLayout.findViewById(R.id.bt_stop);
		bt_pre = (Button) _relativeLayout.findViewById(R.id.bt_pre);
		listview = (ListView) _relativeLayout.findViewById(R.id.listView);
		seekbar = (SeekBar) _relativeLayout.findViewById(R.id.seekbar_playfile);

		bt_play.setOnClickListener(this);
		bt_stop.setOnClickListener(this);
		bt_pre.setOnClickListener(this);

		//if(myservice!= null)
		//myservice.resert();

		
		if (isfirstVisited) {
			mybroastcast = new MyBroastCast();
			MyBroastCast._parent = this;
			if(myservice == null){
				myservice = new MyService();
				MyService._parent = this;
			}
			intent = new Intent(getActivity(), MyService.class);
			connection = new ServiceConnection() {
				@Override
				public void onServiceDisconnected(ComponentName name) {
					// isBound = false;
				}

				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
					MyBinder binder = (MyBinder) service;
					myservice = binder.getService();
					myservice.updateSourse(_pathfile);
					seekbar.setMax(myservice.getDuration());
					seekbar.setProgress(0);
					isrunning = true;
					thread_updateseekbar();
				}
			};
			onService();
			isfirstVisited = false;
		}else{
			if(!myservice.get_pathfile().equals("")){
			seekbar.setMax(myservice.getDuration());
			seekbar.setProgress(myservice.getCurr());
			isrunning = true;
			thread_updateseekbar();}
		}
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				// _mediaplayer.seekTo(seekBar.getProgress());

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if (myservice.getCurr() != 0)
					myservice.seek(progress);
			}
		});

		_handler_readlistfile = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				int n = bundle.getInt("n");
				if (n == -1) {
					Toast.makeText(getActivity(), "Error load file", Toast.LENGTH_LONG);
				} else {
					if (_listfile.size() != 0)
						_listfile.clear();
					for (int i = 0; i < n; i++) {
						AudioFile f = (AudioFile) bundle.getSerializable(i + "");
						_listfile.add(f);
					}
					updatelistview(_listfile);
				}

			}
		};

		_handler_updateseekbar = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				int currPos = (int) bundle.getLong("currPos");
				if (currPos > 0) {
					seekbar.setProgress(currPos);
					
				}
			}
		};
		readfile();

		registerForContextMenu(listview);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				_pathfile = _listfile.get(position).get_src();
				play();
			}

		});

		//// dialog///
		// +++ rename
		LayoutInflater factory = LayoutInflater.from(getActivity());
		View DialogViewrename = factory.inflate(R.layout.custom_dialog_rename, null);
		et_rename = (EditText) DialogViewrename.findViewById(R.id.et_name);

		renameDialog = new AlertDialog.Builder(getActivity()).create();
		renameDialog.setView(DialogViewrename);
		DialogViewrename.findViewById(R.id.bt_save).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText et_name = (EditText) renameDialog.findViewById(R.id.et_name);

				String path = _listfile.get(index_selected).get_src();
				String oldname = path.substring(path.lastIndexOf("/") + 1);
				int k = path.lastIndexOf("/");
				String newpath = path.substring(0, k + 1);
				String type = path.substring(path.lastIndexOf("."));
				String newname = et_name.getText().toString();
				if (newname.equals("")) {
					newname = oldname;
					showToast("File's name is empty");
				} else {
					newpath += newname + type;
					File f_old = new File(path);
					File f_new = new File(newpath);
					if (f_new.exists()) {
						showToast("File existed!");
					} else {
						if (f_old.renameTo(f_new))
							showToast("File renamed successfully!");
						else
							showToast("Error when file renamed!");
					}

					renameDialog.dismiss();
					readfile();
					et_name.setText("");
				}
			}
		});
		DialogViewrename.findViewById(R.id.bt_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				renameDialog.dismiss();
			}
		});
		// +++ delete
		View DialogViewdelete = factory.inflate(R.layout.custom_dialog_delete, null);
		deleteDialog = new AlertDialog.Builder(getActivity()).create();
		deleteDialog.setView(DialogViewdelete);
		DialogViewdelete.findViewById(R.id.bt_deleteyes).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String path = _listfile.get(index_selected).get_src();
				File f = new File(path);
				if (f.delete())
					showToast("Successfully!");
				else
					showToast("Error!");
				readfile();
				deleteDialog.dismiss();
			}
		});
		DialogViewdelete.findViewById(R.id.bt_deleteno).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteDialog.dismiss();
			}
		});
		
		
		
		return _relativeLayout;
	}

	
	
	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.bt_play: {
			play();
			break;
		}
		case R.id.bt_stop: {
			stop();
			break;
		}
		case R.id.bt_pre: {
			pre();
			break;
		}
		}

	}

	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if(myservice!= null && !myservice.get_pathfile().equals("")){
			bt_play.setBackgroundResource(R.drawable.ic_mr_play1);
			if(myservice.isPlaying()){
				bt_play.setBackgroundResource(R.drawable.ic_mr_play2);
			}
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (myservice != null && myservice.isPlaying()){
			String k = _pathfile;
			 k = k.substring( k.lastIndexOf("/")+ 1);
			show(k);
		}
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		index_selected = info.position;
		File f = new File(_listfile.get(index_selected).get_src());
		switch (item.getItemId()) {
		case R.id.item_delete: {
			deleteDialog.show();
			break;
		}
		case R.id.item_rename: {
			renameDialog.show();
			String path = _listfile.get(index_selected).get_src();
			et_rename.setText(path.substring(path.lastIndexOf("/") + 1));
			break;
		}
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getActivity().getMenuInflater().inflate(R.menu.myrecording_item_of_alertdialog, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	private void showToast(String content) {
		Toast.makeText(getActivity(), content, Toast.LENGTH_LONG).show();
	}

	public void readfile() {

		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(100);
				ArrayList<AudioFile> temp = myLib.getInfo(_pathfolder, getActivity());
				Bundle bundle = new Bundle();
				if (temp != null) {
					bundle.putInt("n", temp.size());
					for (int i = 0; i < temp.size(); i++) {
						bundle.putSerializable(i + "", temp.get(i));
					}
				} else
					bundle.putInt("n", -1);
				Message msg = _handler_readlistfile.obtainMessage();
				msg.setData(bundle);
				_handler_readlistfile.sendMessage(msg);
			}
		});
		th.start();
	}

	public void thread_updateseekbar() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isrunning) {
					SystemClock.sleep(500);
					if (myservice != null) {
						int curr = myservice.getCurr();
						Bundle bundle = new Bundle();
						bundle.putLong("currPos", curr);
						Message msg = _handler_updateseekbar.obtainMessage();
						msg.setData(bundle);
						_handler_updateseekbar.sendMessage(msg);

					}

				}
			}
		});
		th.start();
	}

	private void updatelistview(ArrayList<AudioFile> list) {
		aa = new CustomAdapter(getActivity(), R.layout.activity_item_of_listfile, list);
		listview.setAdapter(aa);
	}

	private void onService() {
		getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}

	private void offService() {
		getActivity().unbindService(connection);
	}

	private void stop() {
		myservice.stopmedia();
		bt_play.setBackgroundResource(R.drawable.ic_mr_play1);
		seekbar.setProgress(0);
	}

	private void play() {
		String k = _pathfile;
		if (k.equals(""))
			Toast.makeText(getActivity(), "Let select file to play", Toast.LENGTH_LONG).show();
		else {

			if (_pathfile.equals(myservice.get_pathfile())) {
				if (myservice.isPlaying()) {
					myservice.pausemedia();
					bt_play.setBackgroundResource(R.drawable.ic_mr_play1);

				} else {
					myservice.playmedia();
					bt_play.setBackgroundResource(R.drawable.ic_mr_play2);
				}
			} else {
				myservice.updateSourse(_pathfile);
				seekbar.setMax(myservice.getDuration());
				seekbar.setProgress(0);
				myservice.seek(0);
				myservice.playmedia();

				k = k.substring(k.lastIndexOf("/") + 1);
				Toast.makeText(getActivity(), k, Toast.LENGTH_LONG).show();
				bt_play.setBackgroundResource(R.drawable.ic_mr_play2);
			}
		}
	}
	public  void clearNotification(){
    	String ns = getActivity().NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getActivity().getSystemService(ns);
        nMgr.cancel(6495);
    }
	private void pre() {
		myservice.premedia();
	}
	public MyService getMyservice(){
		return myservice;
	}
	
	public void show(String filename){
		
		NotificationCompat.Builder builder =  new NotificationCompat.Builder(getActivity());
	        NotificationManager notificationManager =  (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
	        Intent intent = new Intent(getActivity(), getActivity().getClass());
	        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        
	        
	        RemoteViews contentView = new RemoteViews(getActivity().getPackageName(), R.layout.custom_notification_playfile);
	        
	        contentView.setTextViewText(R.id.tv_filename_no, filename);
	        
	        Intent stopReceive = new Intent();  
	        stopReceive.setAction("STOP_ACTION");
	        PendingIntent pendingIntentStop = PendingIntent.getBroadcast(getActivity(), 12345, stopReceive, PendingIntent.FLAG_UPDATE_CURRENT);
	        contentView.setOnClickPendingIntent(R.id.bt_stop_no, pendingIntentStop);
	        
	        //  builder.addAction(R.drawable.ic_sstop_no, "", pendingIntentStop);

	        Intent playReceive = new Intent();  
	        playReceive.setAction("PLAY_ACTION");
	        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(getActivity(), 12345, playReceive, PendingIntent.FLAG_UPDATE_CURRENT);
	        contentView.setOnClickPendingIntent(R.id.bt_play_no, pendingIntentPlay);

	        Intent preReceive = new Intent();  
	        preReceive.setAction("PRE_ACTION");
	        PendingIntent pendingIntentPre = PendingIntent.getBroadcast(getActivity(), 12345, preReceive, PendingIntent.FLAG_UPDATE_CURRENT);
	        contentView.setOnClickPendingIntent(R.id.bt_pre_no, pendingIntentPre);
	        
	        builder.setSmallIcon(R.drawable.ic_launcher);
	     //   builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.large_love));
	     //   builder.setContentTitle(filename);
	     //   builder.setContentText("Welcome to  Base Notification");
	     //   builder.setSubText("Go to love-activity");
	        builder.setAutoCancel(true); 
	        builder.setContentIntent(pendingIntent);
	      //  builder.build();
	        _notification = builder.build();
	       _notification.contentView = contentView;
	     //   builder.build().contentView = contentView;
	        notificationManager.notify(6495, _notification);
	}
	
	public void setwhencompete(){
			seekbar.setProgress(0);
			bt_play.setBackgroundResource(R.drawable.ic_mr_play1);
	}
	
	public String getPathfile(){
		return myservice.get_pathfile();
	}

}
