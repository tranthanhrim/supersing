//package myrecording;
//
//import java.io.File;
//import java.util.ArrayList;
//
//import com.example.sssssss.MyRecordingFragment;
//import com.example.sssssss.R;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Fragment;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.os.SystemClock;
//import android.view.ContextMenu;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ContextMenu.ContextMenuInfo;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TabHost;
//import android.widget.Toast;
//import notactivity.AudioFile;
//import notactivity.CustomAdapter;
//import android.widget.AdapterView.AdapterContextMenuInfo;
//import android.widget.AdapterView.OnItemClickListener;
//
//public class Listfile_myrecording extends Fragment {
//
//	ListView listview;
//	EditText rename;
//	ArrayList<AudioFile> _listfile = new ArrayList<AudioFile>();
//	String _pathfolder = "";
//	int index_selected = -1;
//	CustomAdapter aa;
//	AlertDialog renameDialog;
//	AlertDialog deleteDialog;
//
//	LinearLayout _linearlayout;
//	MyRecordingFragment _parent;
//	Handler _handler;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		_linearlayout = (LinearLayout) inflater.inflate(R.layout.activity_listfile_myrecording, null);
//
//		listview = (ListView) _linearlayout.findViewById(R.id.listView);
//		// _listfile = myLib.getInfo(_pathfolder, getActivity());
//		// handler///
//		_handler = new Handler() {
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				Bundle bundle = msg.getData();
//				int n = bundle.getInt("n");
//				if (n == -1) {
//					Toast.makeText(getActivity(), "Error load file", Toast.LENGTH_LONG);
//				} else {
//					if (_listfile.size() != 0)
//						_listfile.clear();
//					for (int i = 0; i < n; i++) {
//						AudioFile f = (AudioFile) bundle.getSerializable(i + "");
//						_listfile.add(f);
//						updatelistview(_listfile);
//						if (_listfile.size() > 0){
//							if(_parent.get_pathfile().equals(""))
//								_parent.set_pathfile(_listfile.get(0).get_src());
//						}
//							
//					}
//				}
//
//			}
//		};
//
//		readfile();
//
//		aa = new CustomAdapter(this.getActivity(), R.layout.activity_item_of_listfile, _listfile);
//		listview.setAdapter(aa);
//		
//
//		registerForContextMenu(listview);
//		// setHasOptionsMenu(true);
//		listview.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				String pathfile = _listfile.get(position).get_src();
//
//				_parent.set_pathfile(pathfile);
//				_parent.replaceFragment(1);
//			}
//
//		});
//
//		//// dialog///
//		// +++ rename
//		LayoutInflater factory = LayoutInflater.from(getActivity());
//		View DialogViewrename = factory.inflate(R.layout.custom_dialog_rename, null);
//		rename = (EditText) DialogViewrename.findViewById(R.id.et_name);
//
//		renameDialog = new AlertDialog.Builder(getActivity()).create();
//		renameDialog.setView(DialogViewrename);
//		DialogViewrename.findViewById(R.id.bt_save).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				EditText et_name = (EditText) renameDialog.findViewById(R.id.et_name);
//
//				String path = _listfile.get(index_selected).get_src();
//				String oldname = get_filename1(path);
//				int k = path.lastIndexOf("/");
//				String newpath = path.substring(0, k + 1); // path đã loại tên
//															// file cũ
//				String type = path.substring(path.lastIndexOf("."));
//				String newname = et_name.getText().toString();
//				if (newname.equals("")) {
//					newname = oldname;
//					showToast("File's name is empty");
//				} else {
//					newpath += newname + type;
//					File f_old = new File(path);
//					File f_new = new File(newpath);
//					if (f_new.exists()) {
//						showToast("File existed!");
//					} else {
//						if (f_old.renameTo(f_new))
//							showToast("File renamed successfully!");
//						else
//							showToast("Error when file renamed!");
//					}
//
//					renameDialog.dismiss();
//					updatelistview();
//					et_name.setText("");
//				}
//			}
//		});
//		DialogViewrename.findViewById(R.id.bt_cancel).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				renameDialog.dismiss();
//			}
//		});
//		// +++ delete
//		View DialogViewdelete = factory.inflate(R.layout.custom_dialog_delete, null);
//		deleteDialog = new AlertDialog.Builder(getActivity()).create();
//		deleteDialog.setView(DialogViewdelete);
//		DialogViewdelete.findViewById(R.id.bt_deleteyes).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String path = _listfile.get(index_selected).get_src();
//				File f = new File(path);
//				if (f.delete())
//					showToast("File deleted successfully!");
//				else
//					showToast("Error when file deleted!");
//				updatelistview();
//				deleteDialog.dismiss();
//			}
//		});
//		DialogViewdelete.findViewById(R.id.bt_deleteno).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				deleteDialog.dismiss();
//			}
//		});
//		return _linearlayout;
//	}
//
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//		index_selected = info.position;
//		File f = new File(_listfile.get(index_selected).get_src());
//		switch (item.getItemId()) {
//		case R.id.item_delete: {
//			deleteDialog.show();
//			break;
//		}
//		case R.id.item_rename: {
//			renameDialog.show();
//			rename.setText(get_filename1(_listfile.get(index_selected).get_src()));
//			break;
//		}
//		}
//		return super.onContextItemSelected(item);
//	}
//
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//		// TODO Auto-generated method stub
//		getActivity().getMenuInflater().inflate(R.menu.myrecording_item_of_alertdialog, menu);
//		super.onCreateContextMenu(menu, v, menuInfo);
//	}
//
//	public void setPathFolder(String content) {
//		this._pathfolder = content;
//	}
//
//	public void setParent(MyRecordingFragment parent) {
//		_parent = parent;
//	}
//
//	private void showToast(String content) {
//		Toast.makeText(getActivity(), content, Toast.LENGTH_LONG).show();
//	}
//
//	public void updatelistview() {
//		// _listfile = myLib.getInfo(_pathfolder, getActivity());
//		readfile();
//		aa = new CustomAdapter(getActivity(), R.layout.activity_item_of_listfile, _listfile);
//		listview.setAdapter(aa);
//	}
//
//	private void updatelistview(ArrayList<AudioFile> list) {
//		aa = new CustomAdapter(getActivity(), R.layout.activity_item_of_listfile, list);
//		listview.setAdapter(aa);
//	}
//
//	public void readfile() {
//
//		Thread th = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				SystemClock.sleep(100);
//				ArrayList<AudioFile> temp = myLib.getInfo(_pathfolder, getActivity());
//				Bundle bundle = new Bundle();
//				if (temp != null || temp.size() != 0) {
//					bundle.putInt("n", temp.size());
//					for (int i = 0; i < temp.size(); i++) {
//						bundle.putSerializable(i + "", temp.get(i));
//					}
//				} else
//					bundle.putInt("n", -1);
//				Message msg = _handler.obtainMessage();
//				msg.setData(bundle);
//				_handler.sendMessage(msg);
//			}
//		});
//		th.start();
//	}
//
//	private String get_filename(String path) {
//		String filename = path.substring(path.lastIndexOf("/") + 1);
//		// filename +=" ";
//		return filename;
//	}
//
//	private String get_filename1(String path) {
//		// không lấy đuôi
//		String filename = path.substring(path.lastIndexOf("/") + 1);
//		filename = filename.substring(0, filename.lastIndexOf("."));
//		// filename +=" ";
//		return filename;
//	}
//
//}
