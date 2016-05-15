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

	private static File[] getListFile(String path) {
		File file = new File(path);
		File[] f = file.listFiles();
		return f;
	}

	public static ArrayList<AudioFile> getInfo(String pathfolder, Context context) {
		try {
			ArrayList<AudioFile> list = new ArrayList<AudioFile>();

			File[] listfile = myLib.getListFile(pathfolder);
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
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						a.setLenght(sdf.format(duration));
						a.set_src(f.getAbsolutePath());
						list.add(a);
					}
				}
			}

			return list;
		} catch (Exception e) {
			return null;
		}
	}

	public static String formatTime(File f) {
		String rs = "";
		long temp = f.lastModified();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		rs = sdf.format(temp);
		return rs;
	}

}
