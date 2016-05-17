package notactivity;

import java.io.IOException;

import com.example.sssssss.R;
import com.example.sssssss.RecordFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.Toast;

public class RecordNotificationReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getStringExtra("recordNotify");
		if(action.equals("RECORD")){
			if(RecordFragment.record.isPause){
				try {
					RecordFragment.record.startRecord();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(context, "Continue record", Toast.LENGTH_SHORT).show();
			}
			else{
				RecordFragment.record.pauseRecord();
				Toast.makeText(context, "Pause record", Toast.LENGTH_SHORT).show();
			}
		}
		if(action.equals("DONE")){
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
		if(action.equals("CANCEL")){
			Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			context.sendBroadcast(it);
			if (RecordFragment.record.isPause == false){
				RecordFragment.record.pauseRecord();
			}
			RecordFragment.record.cancelRecord();
		}
	}

}
