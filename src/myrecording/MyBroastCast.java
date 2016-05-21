package myrecording;

import com.example.sssssss.MyRecordingFragment;
import com.example.sssssss.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class MyBroastCast extends BroadcastReceiver {

	public static MyRecordingFragment _parent = new MyRecordingFragment();
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if ("PLAY_ACTION".equals(action)) {
			if(_parent.getMyservice().isPlaying()){
				_parent.getMyservice().pausemedia();
				_parent.show(_parent.getfilename(), 0);
				_parent.setStatePlayButton(0);
			}
			else{
				_parent.getMyservice().playmedia();
				_parent.show(_parent.getfilename(), 1);
				_parent.setStatePlayButton(1);
			}
		}
		if ("STOP_ACTION".equals(action)) {
			_parent.getMyservice().stopmedia();
			_parent.show(_parent.getfilename(), 0);
			_parent.setStatePlayButton(0);
			_parent.setwhencompete(); 
		}
		if ("EXIT_ACTION".equals(action)) {
			_parent.clearNotification(1);
		}
		if ("PRE_ACTION".equals(action)) {
			_parent.getMyservice().premedia();
		}
		
	}

}
