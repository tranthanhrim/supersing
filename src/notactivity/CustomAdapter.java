package notactivity;

import java.util.ArrayList;

import com.example.sssssss.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<AudioFile>{
	
	Activity context = null;
	int layoutId;
	ArrayList<AudioFile> arr = null;

	public CustomAdapter(Activity context, int layoutId, ArrayList<AudioFile> list){
		super(context, layoutId, list);
		this.context = context;
		this.layoutId = layoutId;
		this.arr = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){

		if(convertView==null){
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(layoutId, null);
		}

			AudioFile item = arr.get(position);

//			ImageView icon = (ImageView)convertView.findViewById(R.id.icon);

			TextView filename = (TextView)convertView.findViewById(R.id.filename);
			TextView date_record = (TextView)convertView.findViewById(R.id.date_record);
			TextView time_record = (TextView)convertView.findViewById(R.id.time_record);
			TextView size = (TextView)convertView.findViewById(R.id.size);
			TextView lenght = (TextView)convertView.findViewById(R.id.lenght);
			if(item.get_isselected()){
				ImageView im = (ImageView)convertView.findViewById(R.id.imv_selected);
				im.setImageResource(R.drawable.ic_selected_play);
			}
			
			filename.setText(item.get_filename());
			date_record.setText(item.get_dateRecord());
			time_record.setText(item.get_timeRecord());
			String k = item.getSize() + "";
			size.setText(k);
			k = item.getLenght() + "";
			lenght.setText(k);
			
			
//			String uri_icon = "drawable/" + item.get_icon();
//			int ImageResoure = convertView.getContext().getResources().getIdentifier(uri_icon, null, convertView.getContext().getApplicationContext().getPackageName());
//			Drawable image = convertView.getContext().getResources().getDrawable(ImageResoure);
//			icon.setImageDrawable(image);
			return convertView;
	}
}
