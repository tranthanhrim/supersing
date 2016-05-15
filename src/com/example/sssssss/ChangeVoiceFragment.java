package com.example.sssssss;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ChangeVoiceFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RelativeLayout changeVoice_layout = (RelativeLayout)inflater.inflate(R.layout.activity_change_voice_fragment, null);
		return changeVoice_layout;
	}
}
