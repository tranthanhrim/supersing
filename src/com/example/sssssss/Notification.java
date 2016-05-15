package com.example.sssssss;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.TextView;

public class Notification extends Activity {

	public static TextView txtState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		txtState = (TextView)findViewById(R.id.txtState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setListeners(RemoteViews view){
        //listener 1
        Intent record = new Intent(this,MainActivity.class);
        record.putExtra("DO", "record");
        PendingIntent btn1 = PendingIntent.getActivity(this, 0, record, 0);
        view.setOnClickPendingIntent(R.id.btnRecord, btn1);
    }
	
	public static void setText(String value){
		txtState.setText(value);
	}
}
