package notactivity;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import com.example.sssssss.MainActivity;
import com.example.sssssss.NavigationDrawerFragment;
import com.example.sssssss.R;

import android.R.bool;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class Record {
	public Fragment context;
	public Chronometer chrWatch;
	public GifView gif;
	public Button btnDone, btnCancel, btnRecord;
	
	public static Button btnRecordNot;
	
	private MediaRecorder recorder;
   	private String outputFile = null;  	   	
   	public long timeWhenStopped = 0;
   	public boolean isRecording = false;
   	public boolean isPause = false;
   	
   	private ArrayList<String> listFile;
   	NotificationManager manager;
   	Notification myNotication;
	
	public void startRecord() throws IllegalStateException, IOException{
    	outputFile = Environment.getExternalStorageDirectory() + "/Record"
						+ "/record_" + System.currentTimeMillis() + ".3gp";
    	  	
    	if (recorder != null){
    		recorder.release();
    	}
    	
    	File fileOut = new File(outputFile);
    	if (fileOut != null){
    		fileOut.delete(); //Overwrite existing file;
    	}
    	
    	recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(outputFile);
        
        if(isRecording == false){
        	listFile = new ArrayList<String>();
        	listFile.add(outputFile);
        	isRecording = true;
        	isHideDrawerToggle(true);
        }
        else{
        	listFile.add(outputFile);
        }

		recorder.prepare();
		recorder.start();
		continueWatch();
		isPause = false;
		makeNotification();
		gif.start();
		btnCancel.setVisibility(View.INVISIBLE);
		btnDone.setVisibility(View.INVISIBLE);
		btnRecord.setSelected(true);
    }
    
    public void finishRecord() throws IOException{
    	resetWatch();
    	isRecording = false;
    	System.out.println(listFile.size());
    	if (listFile.size() > 1){
    		MergeFile mergeMachine = new MergeFile();
        	mergeMachine.merge(listFile);
        	//delete split-file
        	for(int i = 0; i < listFile.size(); i++){
        		File f = new File(listFile.get(i));
        		f.delete();
        	}
    	}
    	clearNotification();
    	btnCancel.setVisibility(View.INVISIBLE);
 	  	btnDone.setVisibility(View.INVISIBLE);
 	  	isHideDrawerToggle(false);
 	  	Toast.makeText(context.getActivity(), "Finished!",Toast.LENGTH_SHORT).show();
    }
    
    public void cancelRecord(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());

        //builder.setTitle("Confirm");
        builder.setMessage("Are you sure want to cancel record?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            	
            	for(int i = 0; i < listFile.size(); i++){
            		File f = new File(listFile.get(i));
            		f.delete();
            	}
            	resetWatch();
            	isRecording = false;
            	clearNotification();
            	btnCancel.setVisibility(View.INVISIBLE);
         	  	btnDone.setVisibility(View.INVISIBLE);
         	  	isHideDrawerToggle(false);
            	Toast.makeText(context.getActivity(), "Canceled!",Toast.LENGTH_SHORT).show();
            	
            	//MainActivity.this.finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    
    public void pauseRecord(){
    	recorder.stop();
    	recorder.release();
    	pauseWatch();
    	isPause = true;
    	makeNotification();
    	gif.stop();
    	btnCancel.setVisibility(View.VISIBLE);
    	btnDone.setVisibility(View.VISIBLE);
    	btnRecord.setSelected(false);
    }
    
    public void pressBackButton(){
    	//System.out.println(listFile.size());
    	if (listFile != null && isRecording == true){
    		for(int i = 0; i < listFile.size(); i++){
        		File f = new File(listFile.get(i));
        		f.delete();
        	}
    	}
    }
    
    public void resetWatch(){
    	chrWatch.stop();
    	timeWhenStopped = 0;
    	chrWatch.setBase(SystemClock.elapsedRealtime());
    }
    
    public void pauseWatch(){
    	chrWatch.stop();
    	timeWhenStopped = chrWatch.getBase() - SystemClock.elapsedRealtime();
    }
    
    public void continueWatch(){
    	chrWatch.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
    	chrWatch.start();
    }
    
    public void setCurrentTime(){
    	timeWhenStopped = chrWatch.getBase() - SystemClock.elapsedRealtime();
    }
    
    
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void makeNotification(){
    	manager = (NotificationManager)context.getActivity().getSystemService(context.getActivity().NOTIFICATION_SERVICE);
		Intent intent = new Intent(context.getActivity(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Builder builder = new Notification.Builder(context.getActivity());

		Button btnRecordNot = (Button)context.getActivity().findViewById(R.id.btnRecordNot);
    	//btnRecordNot.setSelected(true);
		
		RemoteViews contentView = new RemoteViews(context.getActivity().getPackageName(), R.layout.custom_notification);
		setListeners(contentView);
//		Intent switchIntent = new Intent("RECORD_NOTIFY");
//		switchIntent.putExtra("recordNotify", "DONE");
//	    PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context.getActivity(), 0, switchIntent, 0);
//	    contentView.setOnClickPendingIntent(R.id.btnDoneNot, pendingSwitchIntent);
		
        builder.setAutoCancel(false);
        if (isPause){
        	//builder.setContentTitle("is Pause");
        	//com.example.sssssss.Notification.txtState.setText("is Pause");
        }
        else{
        	//com.example.sssssss.Notification.txtState.setText("is Recording");
        }
        //builder.setContentText("You have a new message");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        //builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        //builder.setAutoCancel(true);
        builder.build();

        myNotication = builder.getNotification();
        myNotication.contentView = contentView;
        manager.notify(0, myNotication);
    }
    
    /*@TargetApi(Build.VERSION_CODES.JELLY_BEAN) private void makeNotification(){
    	manager = (NotificationManager)context.getActivity().getSystemService(context.getActivity().NOTIFICATION_SERVICE);
		Intent intent = new Intent(context.getActivity(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Builder builder = new Notification.Builder(context.getActivity());

		RemoteViews contentView = new RemoteViews(context.getActivity().getPackageName(), R.layout.custom_notification);
		//setListeners(contentView);
		
        builder.setAutoCancel(false);
        if (Record.isPause){
        	//builder.setContentTitle("is Pause");
        	//com.example.sssssss.Notification.txtState.setText("is Pause");
        }
        else{
        	//com.example.sssssss.Notification.txtState.setText("is Recording");
        }
        //builder.setContentText("You have a new message");
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        //builder.setSubText("This is subtext...");   //API level 16
        builder.setNumber(100);
        //builder.setAutoCancel(true);
        //builder.build();

        myNotication = builder.getNotification();
        myNotication.contentView = contentView;
        manager.notify(0, myNotication);
    }*/
    
    private void clearNotification(){
    	String ns = context.getActivity().NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) context.getActivity().getSystemService(ns);
        nMgr.cancel(0);
    }
    
    private void setListeners(RemoteViews view){

    	//btnRecordNot.setSelected(true);
    	
    	Intent intentRecord = new Intent("RECORD_NOTIFY");
    	intentRecord.putExtra("recordNotify", "RECORD");
	    PendingIntent pendingIntentRecord = PendingIntent.getBroadcast(context.getActivity(), 0, intentRecord, 0);
	    view.setOnClickPendingIntent(R.id.btnRecordNot, pendingIntentRecord);
	    if(isPause){
	    	view.setTextViewText(R.id.txtState, "Pause");
	    }
	    else{
	    	view.setTextViewText(R.id.txtState, "Recording...");
	    }
	    
    	
    	Intent intentDone = new Intent("RECORD_NOTIFY");
    	intentDone.putExtra("recordNotify", "DONE");
	    PendingIntent pendingIntentDone = PendingIntent.getBroadcast(context.getActivity(), 1, intentDone, 0);
	    view.setOnClickPendingIntent(R.id.btnDoneNot, pendingIntentDone);
	    
//	    Uri myUri = Uri.parse("http://www.google.com");
//	    Intent intentCancel = new Intent("RECORD_NOTIFY", myUri,context.getActivity(), MainActivity.class);
//	    intentCancel.putExtra("recordNotify", "CANCEL");
//	    intentCancel.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntentCancel = PendingIntent.getActivity(context.getActivity(), 2, intentCancel, PendingIntent.FLAG_UPDATE_CURRENT);
		
	    Intent intentCancel = new Intent("RECORD_NOTIFY");
	    //Intent intentCancel = new Intent("RECORD_NOTIFY",myUri, context.getActivity(), MainActivity.class);
	    intentCancel.putExtra("recordNotify", "CANCEL");
	    PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(context.getActivity(), 2, intentCancel, 0);
	    view.setOnClickPendingIntent(R.id.btnCancelNot, pendingIntentCancel);
	    
	    
    }
    
    private void isHideDrawerToggle(boolean isHide){
    	ActionBar actionBar = context.getActivity().getActionBar();
    	if (isHide){
    		actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(false);
    	}
    	else{
    		actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
    	}
        
    } 
}
