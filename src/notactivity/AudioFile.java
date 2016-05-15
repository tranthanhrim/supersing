package notactivity;

import java.io.Serializable;

public class AudioFile implements Serializable  {
	private String _filename;
	private String _dateRecord;
	private String _timeRecord;
	private String _src;
	private String _size;
	private String _lenght;
	
	
	public AudioFile(){
		super();
	}


	public AudioFile(String filename, String dateRecord, String timeRecord, String size, String lenght, String src) {
		super();
		this._filename = filename;
		this._dateRecord = dateRecord;
		this._timeRecord = timeRecord;
		this._size = size;
		this._lenght = lenght;
		this._src = src;
	}
	
	
	public String get_src() {
		return _src;
	}


	public void set_src(String src) {
		this._src = src;
	}


	public String get_filename() {
		return _filename;
	}
	public void set_filename(String _filename) {
		this._filename = _filename;
	}
	public String get_dateRecord() {
		return _dateRecord;
	}
	public void set_dateRecord(String _dateRecord) {
		this._dateRecord = _dateRecord;
	}
	public String get_timeRecord() {
		return _timeRecord;
	}
	public void set_timeRecord(String _timeRecord) {
		this._timeRecord = _timeRecord;
	}
	public String getSize() {
		return _size;
	}
	public void setSize(String size) {
		this._size = size;
	}
	public String getLenght() {
		return _lenght;
	}
	public void setLenght(String lenght) {
		this._lenght = lenght;
	}
	
	
}


