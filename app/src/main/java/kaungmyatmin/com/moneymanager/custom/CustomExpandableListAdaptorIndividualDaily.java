package kaungmyatmin.com.moneymanager.custom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.github.mikephil.charting.data.Entry;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import kaungmyatmin.com.moneymanager.POJO.UsageData;
import kaungmyatmin.com.moneymanager.R;

public class CustomExpandableListAdaptorIndividualDaily extends
		BaseExpandableListAdapter {

	private Context context;
	private HashMap<String, ArrayList<UsageData>> childs;
	private LayoutInflater inflater;
	private SimpleDateFormat dateFormat;
	private ArrayList<Entry> parentValues;
	private ArrayList<String> parentDate;
	private Activity acitvity;
	private Calendar calendar;

	public CustomExpandableListAdaptorIndividualDaily(Context context,
			Activity activity, ArrayList<Entry> parentValues,
			ArrayList<String> parentDate,
			HashMap<String, ArrayList<UsageData>> childs) {

		this.context = context;
		this.acitvity = activity;
		this.childs = childs;
		this.parentValues = parentValues;
		this.parentDate = parentDate;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dateFormat = new SimpleDateFormat("hh:mm");
		calendar = Calendar.getInstance();
	}

	@Override
	public int getGroupCount() {
		return parentValues.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		ArrayList<UsageData> dataS = null;
		if(parentDate.size()>groupPosition){
		dataS = childs.get(parentDate.get(groupPosition));
		}
		if (dataS != null) {
			return dataS.size();
		} else {
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
		TextView dateStamp = (TextView) convertView.findViewById(R.id.title);
		TextView amt = (TextView) convertView.findViewById(R.id.amt);

		if(parentDate.size()>groupPosition){
		String day = parentDate.get(groupPosition);

		calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
		dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
		dateStamp.setText(dateFormat.format(calendar.getTime()));
		}else{
			dateStamp.setText("စုစုေပါင္း");
		}
		amt.setText((int)(parentValues.get(groupPosition).getVal()) + "က်ပ္");

		int noOfChilds = getChildrenCount(groupPosition);
		if (noOfChilds > 0) {
			TextView count = (TextView) convertView.findViewById(R.id.count);
			count.setText(String.valueOf(noOfChilds));
			count.setVisibility(View.VISIBLE);
		} else {
			TextView count = (TextView) convertView.findViewById(R.id.count);
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.arrow);
			count.setVisibility(View.INVISIBLE);
			// imageView.setVisibility(View.GONE);

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
		ArrayList<UsageData> dataS = childs.get(parentDate.get(groupPosition));
		if (dataS != null) {

			UsageData data = dataS.get(childPosition);

			String text = String.format("%1$02d:%2$02d", data.getHour(),
					data.getMinute());

			time.setText(text);

			amt.setText(String.valueOf(data.getAmount()) + "က်ပ္");
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

		return false;
	}

}
