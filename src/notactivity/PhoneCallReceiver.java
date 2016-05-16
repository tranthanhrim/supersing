package notactivity;

import java.io.IOException;

import com.example.sssssss.RecordFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneCallReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		/*if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            //stop record
        }*/
		//Hangup
		if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_RINGING)) {

				Log.e ("Broadcast>>>", "Ringing");
				Toast.makeText(context, "Your recording will be paused automatically", Toast.LENGTH_LONG).show();
				RecordFragment.record.pauseRecord();
                //String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                // Ringing state
                // This code will execute when the phone has an incoming call
				
        }
		//Outgoing
		if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
				TelephonyManager.EXTRA_STATE_OFFHOOK)){
			//Log.e ("Broadcast>>>", "Call is coming!");
			
		}
		//Incoming
		if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
                TelephonyManager.EXTRA_STATE_IDLE)){
			//Toast.makeText(RecordFragment., text, duration)
			Log.e ("Broadcast>>>", "Call is finish!");
			Toast.makeText(context, "Continue record", Toast.LENGTH_LONG).show();
            try {
				RecordFragment.record.startRecord();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
