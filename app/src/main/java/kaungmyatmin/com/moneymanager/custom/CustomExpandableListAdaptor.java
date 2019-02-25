package kaungmyatmin.com.moneymanager.custom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.Text;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import kaungmyatmin.com.moneymanager.POJO.UsageData;
import kaungmyatmin.com.moneymanager.R;

public class CustomExpandableListAdaptor extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<UsageData> parents;
	private HashMap<String, ArrayList<UsageData>> childs;
	private LayoutInflater inflater;
	private SimpleDateFormat dateFormat;

	private Activity acitvity;

	public CustomExpandableListAdaptor(Context context, Activity activity,
			ArrayList<UsageData> parents,
			HashMap<String, ArrayList<UsageData>> childs) {

		this.context = context;
		this.acitvity = activity;
		this.parents = parents;
		this.childs = childs;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dateFormat = new SimpleDateFormat("hh:mm");
	}

	@Override
	public int getGroupCount() {
		return parents.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		ArrayList<UsageData> dataS = childs.get(parents.get(groupPosition)
				.getTitle());
		if (dataS != null){
			return dataS.size();
		}else{
		return 0;
		}

	}

	@Override
	public Object getGroup(int groupPosition) {

		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_list_group, null);
		}
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView amt = (TextView) convertView.findViewById(R.id.amt);
		
		

		UsageData data = parents.get(groupPosition);
		title.setText(data.getTitle());
		amt.setText(data.getAmount() + "က်ပ္");
		
		int noOfChilds = getChildrenCount(groupPosition); 
		if(noOfChilds > 0) {
			TextView count = (TextView) convertView.findViewById(R.id.count);
			count.setText(String.valueOf(noOfChilds));
			count.setVisibility(View.VISIBLE);
		}
		else{
			TextView count = (TextView) convertView.findViewById(R.id.count);
			ImageView imageView =(ImageView) convertView.findViewById(R.id.arrow);
			count.setVisibility(View.INVISIBLE);
			//imageView.setVisibility(View.GONE);
			
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_list_child, null);

		}
		TextView time = (TextView) convertView.findViewById(R.id.time);
		TextView amt = (TextView) convertView.findViewById(R.id.child_amt);
		TextView reason = (TextView) convertView.findViewById(R.id.reason);
		ArrayList<UsageData> dataS = childs.get(parents.get(groupPosition)
				.getTitle());
		if (dataS != null) {

			UsageData data = dataS.get(childPosition);

			String text = String.format("%1$02d:%2$02d", data.getHour(),
					data.getMinute());

			setListeneron_txt_time(time);
			time.setText(text);

			amt.setText(String.valueOf(data.getAmount())+"က်ပ္");
			convertView.setVisibility(View.VISIBLE);

			String reasonStr = data.getReason();
			if (!reasonStr.equals("")) {

				reason.setText(reasonStr);
				reason.setVisibility(View.VISIBLE);
			} else {

				reason.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;
	}

	
	
	private void setChildListeners() {/*
									
									  
									  button.setOnClickListener(new
									  View.OnClickListener() {
									  
									  @Override public void onClick(View v) {
									  if (!isEditable) { //
									  coverView.setVisibility(View.GONE);
									  time.setClickable(true);
									  amt.setEnabled(true); isEditable = true;
									  } else { //
									  coverView.setVisibility(View.VISIBLE);
									  time.setClickable(false);
									  amt.setEnabled(false); isEditable =
									  false;
									  
									  }
									  
									  } });
									 
	*/}

	private void showTimePickerDialog(TextView time) {
		final TextView timet = time;
		String[] data = timet.getText().toString().split(":");
		int hour = Integer.valueOf(data[0]);
		int minute = Integer.valueOf(data[1]);
		new TimePickerDialog(acitvity,
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						timet.setText(String.format("%1$02d:%2$02d", hourOfDay,
								minute));
					}
				}, hour, minute, false).show();
	}

	//
	//
	// private methods starts here
	//
	//

	private void setListeneron_txt_time(TextView time) {/*
		final TextView text = time;
		time.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();

				new TimePickerDialog(context,
						new TimePickerDialog.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								text.setText(hourOfDay + ":" + minute);

							}
						}, calendar.get(Calendar.HOUR), calendar
								.get(Calendar.MINUTE), false).show();

			}
		});
	*/}
}
