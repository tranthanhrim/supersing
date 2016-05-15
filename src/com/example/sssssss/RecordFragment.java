package com.example.sssssss;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import notactivity.GifView;
import notactivity.Record;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;

public class RecordFragment extends Fragment {


	final Fragment context = this;

	Button btnCancel,btnDone,btnRecord;
   	Chronometer chrWatch;
   	
   	public static Record record = new Record();
   	GifView gif;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//LinearLayout record_layout = (LinearLayout)inflater.inflate(R.layout.activity_record_fragment, null);
		RelativeLayout record_layout = (RelativeLayout)inflater.inflate(R.layout.activity_record_fragment, null);
		
		record.context = context;
		
		File f = new File(Environment.getExternalStorageDirectory() + "/Record");
        if(!f.isDirectory()){
        	f.mkdir();
        }
        
        
        gif = (GifView)record_layout.findViewById(R.id.gifview);
        gif.stop();
        
        btnCancel=(Button)record_layout.findViewById(R.id.btnCancel);
        btnDone=(Button)record_layout.findViewById(R.id.btnDone);
        btnRecord=(Button)record_layout.findViewById(R.id.btnRecord);
        chrWatch=(Chronometer)record_layout.findViewById(R.id.chrWatch);
              
        record.chrWatch = chrWatch;
        btnDone.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        
        if (Record.isRecording){
        	if (Record.isPause){
        		chrWatch.setBase(SystemClock.elapsedRealtime() + Record.timeWhenStopped);
        		btnDone.setVisibility(View.VISIBLE);
    	        btnCancel.setVisibility(View.VISIBLE);
        	}
        	else
        	{
        		btnRecord.setSelected(!btnRecord.isSelected());
        		//long x = -System.currentTimeMillis();
        		//long currentTime = x - Record.timeWhenStopped;
        		//chrWatch.setBase(SystemClock.elapsedRealtime() + Record.timeWhenStopped + currentTime);
        		//gif.start();
        	}
        }
        
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {         
         	   btnRecord.setSelected(!btnRecord.isSelected());
         	  
         	  if(btnRecord.isSelected())
         	  {
 	    		  btnCancel.setVisibility(View.INVISIBLE);
 	    		  btnDone.setVisibility(View.INVISIBLE);
 	    		  gif.start();
 	    		  
 	    		  try {
 	    			  record.startRecord();
 	    		  }
 	    		  catch (IllegalStateException e) {
 	    			  e.printStackTrace();
 	    		  }
 	    		  catch (IOException e) {
 	    			  e.printStackTrace();
 	    		  }    		  
         	  }
         	  else
         	  {     		  	  
 	    		  record.pauseRecord();
 	    		  btnCancel.setVisibility(View.VISIBLE);
	    		  btnDone.setVisibility(View.VISIBLE);
	    		  gif.stop();
         	  }
         	  
         	  
            }
         });
         
         btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	try {
	         	  	record.finishRecord();
	         	  	btnCancel.setVisibility(View.INVISIBLE);
	         	  	btnDone.setVisibility(View.INVISIBLE);
            	}
           		catch (IOException e) {
             	  	e.printStackTrace();
           		}                         
            }
         });
         
         btnCancel.setOnClickListener(new View.OnClickListener() {
 			
 			@Override
 			public void onClick(View arg0) {
 				record.cancelRecord(btnCancel, btnDone);
 			}
 		});
         
         /*String action = (String)getActivity().getIntent().getExtras().getString("DO");
         if (action.equals("record")) {
             Toast.makeText(context.getActivity(), "record", Toast.LENGTH_LONG).show();
         }*/
		return record_layout;
	}
}
