package notactivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

public class MergeFile {
	public void merge(ArrayList<String> listOfFiles) throws IOException{
		ArrayList<Movie> listMovie = new ArrayList<Movie>();
		for(int i = 0; i < listOfFiles.size(); i++){
			listMovie.add(MovieCreator.build(listOfFiles.get(i)));
		}
		
		//Movie movieA = MovieCreator.build(listOfFiles[3]);
	    //Movie movieB = MovieCreator.build(listOfFiles[4]);
	    final Movie finalMovie = new Movie();

	    List<Track> audioTracks = new ArrayList<Track>();
	    for(Movie movie : listMovie){
        	for (Track track : movie.getTracks()) {
                if (track.getHandler().equals("soun")) {
                    audioTracks.add(track);
                }
            }
        }
	    
	    finalMovie.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
	    final Container container = new DefaultMp4Builder().build(finalMovie);
	    
	    String mergeFileName = Environment.getExternalStorageDirectory() + "/Record" +
    							"/merge_" + System.currentTimeMillis() + ".3gp";
	    File mergedFile = new File(mergeFileName);
	    final FileOutputStream fos = new FileOutputStream(mergedFile);
	    FileChannel fc = new RandomAccessFile(mergedFile, "rw").getChannel();
	    container.writeContainer(fc);
	    fc.close();
	}
}
