package notactivity;

import java.io.IOException;

import com.example.sssssss.RecordFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RecordNotificationReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getStringExtra("recordNotify");
		if (action.equals("DONE")){
			try {
				if (RecordFragment.record.isPause == false){
					RecordFragment.record.pauseRecord();
				}
				RecordFragment.record.finishRecord();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (action.equals("CANCEL")){
			if (RecordFragment.record.isPause == false){
				RecordFragment.record.pauseRecord();
			}
			RecordFragment.record.cancelRecord();
		}
	}

}
