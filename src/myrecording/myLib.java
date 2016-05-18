package myrecording;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore.Files;
import notactivity.AudioFile;

public class myLib {



	public static ArrayList<AudioFile> getInfo(String pathfolder, Context context) {
		ArrayList<AudioFile> list = new ArrayList<AudioFile>();
		try {
			File file = new File(pathfolder);
			File[] listfile = file.listFiles();
			if(listfile.length == 0)
				return list;
			
			int n = listfile.length;
			for (int i = 0; i < n; i++) {
				if (listfile[i].isFile()) {
					String temp = listfile[i].getAbsolutePath();
					temp = temp.substring(temp.lastIndexOf("."));
					if (temp.equals(".3gp") || temp.equals(".mp3")) {
						AudioFile a = new AudioFile();
						File f = listfile[i];
						a.set_filename(f.getName());
						String date = formatTime(f);
						String[] date1 = date.split(" ");
						a.set_dateRecord(date1[0]);
						a.set_timeRecord(date1[1]);
						long size = f.length();
						if (size < 1024 * 1024)
							a.setSize(size / 1024 + "Kb");
						else
							a.setSize(size / (1024 * 1024) + "Mb");

						MediaPlayer mp = MediaPlayer.create(context, Uri.parse(f.getAbsolutePath()));
						int duration = mp.getDuration();
						SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
						a.setLenght(sdf.format(duration));
						a.set_src(f.getAbsolutePath());
						list.add(a);
					}
				}
			}

		} catch (Exception e) {
			
		}
		return list;
	}

	public static String formatTime(File f) {
		String rs = "";
		long temp = f.lastModified();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		rs = sdf.format(temp);
		return rs;
	}

}
