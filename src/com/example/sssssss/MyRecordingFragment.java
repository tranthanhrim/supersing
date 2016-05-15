package com.example.sssssss;

import java.io.File;



import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import myrecording.Listfile_myrecording;
import myrecording.Playfile_myrecording;

public class MyRecordingFragment extends Fragment implements OnClickListener {

	FragmentTransaction _transaction;
	Listfile_myrecording _frag_listfile;
	Playfile_myrecording _frag_playfile;
	ImageButton _showlist, _playfile;
	String _pathfolder = "";
	String _pathfile = "";
	LinearLayout _linearlayout;
	int index_of_frag = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		_frag_listfile = new Listfile_myrecording();
		_frag_playfile = new Playfile_myrecording();
		_frag_listfile.setParent(this);
		_frag_playfile.setParent(this);
		_pathfolder = Environment.getExternalStorageDirectory() +"/Record";
		File f = new File(_pathfolder);
		if(!f.exists())
			f.mkdir();
		_frag_listfile.setPathFolder(_pathfolder);
		
		replaceFragment(0);
		index_of_frag = 0;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		_linearlayout = (LinearLayout)inflater.inflate(R.layout.activity_my_recording_fragment, null);
		_showlist = (ImageButton) _linearlayout.findViewById(R.id.imbt_listfile_myrecording);
		_playfile = (ImageButton) _linearlayout.findViewById(R.id.imbt_playfile_myrecording);
		
		_showlist.setOnClickListener(this);
		_playfile.setOnClickListener(this);
		return _linearlayout;
	}


	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch(id){
		case R.id.imbt_listfile_myrecording:
			
			replaceFragment(0);
			//Toast.makeText(getActivity(), index_of_frag + "", Toast.LENGTH_LONG).show();
			break;
		case R.id.imbt_playfile_myrecording:
			
			replaceFragment(1);
			//Toast.makeText(getActivity(), index_of_frag + "", Toast.LENGTH_LONG).show();
			break;
		}

	}


	public void replaceFragment(int index) {
		Fragment newfrg = new Fragment();
		switch(index){
		case 0:
			newfrg = _frag_listfile;
			index_of_frag = 0;
			break;
		case 1:
			index_of_frag = 1;
			newfrg = _frag_playfile;
			break;
		default:newfrg = _frag_listfile;
				
		}
		_transaction = getFragmentManager().beginTransaction();
		_transaction.replace(R.id.content_frag_myrecording, newfrg);
		_transaction.commit();
	}

	public String get_pathfile() {
		return _pathfile;
	}

	public void set_pathfile(String _pathfile) {
		this._pathfile = _pathfile;
	}

	public int getIndex_of_frag() {
		return index_of_frag;
	}

	public void setIndex_of_frag(int index_of_frag) {
		this.index_of_frag = index_of_frag;
	}
	
	

}
