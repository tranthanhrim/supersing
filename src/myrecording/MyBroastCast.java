package myrecording;

import com.example.sssssss.MyRecordingFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroastCast extends BroadcastReceiver {

	public static MyRecordingFragment _parent = new MyRecordingFragment();
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if ("PLAY_ACTION".equals(action)) {
			if(_parent.getMyservice().isPlaying())
				_parent.getMyservice().pausemedia();
			else
			_parent.getMyservice().playmedia();
		}
		if ("STOP_ACTION".equals(action)) {
			_parent.getMyservice().stopmedia();
		}
		if ("PRE_ACTION".equals(action)) {
			_parent.getMyservice().premedia();
		}
		
	}

}
