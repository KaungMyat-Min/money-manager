package kaungmyatmin.com.moneymanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.trio.moneymanager.DB.ValHolder;
import com.trio.moneymanager.Model.UsageHandler;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class FragUsage extends Fragment {

	private Context context;
	private Button button;
	private LineChart usage, save;
	private TextView info;
	private boolean checkDaily = true;

	private ArrayList<Entry> values = new ArrayList<>();
	private ArrayList<String> xVals = new ArrayList<>();

	private String[] months;
	private Spinner spinner;

	private int month;
	private Calendar calendar;
	
	 private String Daily;
	 private String Monthly;
	
	public FragUsage(Context context) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.frag_use_save, null);
		button = (Button) v.findViewById(R.id.use_but_change);
		usage = (LineChart) v.findViewById(R.id.use_chart_useage);
		save = (LineChart) v.findViewById(R.id.use_chart_save);
		info = (TextView) v.findViewById(R.id.use_txt_info);
		spinner = (Spinner) v.findViewById(R.id.spinner_month);

		calendar = Calendar.getInstance();
		
		ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(context, R.array.array_months, R.layout.item_spinner);
		adapter.setDropDownViewResource(R.layout.item_spinner);
		spinner.setAdapter(adapter);
		
		spinner.setSelection(calendar.get(Calendar.MONTH));
		
		Daily = getString(R.string.daily);
		Monthly = getString(R.string.monthly);
		customizedCharts(usage);
		customizedCharts(save);

		setListeners();

		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		populateCharts(checkDaily);
	}

	private void customizedCharts(LineChart chart) {

		chart.setDrawGridBackground(false);
		chart.setDrawBorders(false);
		chart.setBorderWidth(0);
		chart.getLegend().setEnabled(false);
		chart.setDoubleTapToZoomEnabled(false);
		chart.setPinchZoom(false);
		chart.setDescription("");
		chart.setNoDataText("");
		chart.setNoDataTextDescription("");
		
		CustomMarkerView mv = new CustomMarkerView(context, R.layout.marker);
		chart.setMarkerView(mv);

		XAxis xAxis = chart.getXAxis();
		xAxis.setDrawLabels(true);
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawLimitLinesBehindData(false);
		xAxis.setDrawAxisLine(false);
		xAxis.setTextSize(15f);
		xAxis.setLabelsToSkip(0);

		YAxis yAxis = chart.getAxisRight();
		yAxis.setDrawLabels(false);
		yAxis.setDrawGridLines(false);
		yAxis.setDrawLimitLinesBehindData(false);

		YAxis yLeft = chart.getAxisLeft();
		yLeft.setDrawLabels(false);
		yLeft.setDrawGridLines(false);
		yLeft.setDrawLimitLinesBehindData(false);
		yAxis.setDrawAxisLine(false);
		yLeft.setDrawAxisLine(false);

	}

	private HashMap<Integer, String> monthsName = new HashMap<>();
	{
		monthsName.put(0, "Jan");
		monthsName.put(1, "Feb");
		monthsName.put(2, "Mar");
		monthsName.put(3, "Apr");
		monthsName.put(4, "May");
		monthsName.put(5, "Jun");
		monthsName.put(6, "Jul");
		monthsName.put(7, "Aug");
		monthsName.put(8, "Sep");
		monthsName.put(9, "Oct");
		monthsName.put(10, "Nov");
		monthsName.put(11, "Dec");

	}

	private void populateCharts(boolean checkDaily) {

		
		UsageHandler usageHandler = new UsageHandler(context, null);

		Object[] rawData = usageHandler.getLineData(checkDaily,
				ValHolder.TYPE_TOTAL_USAGE,calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));

		if (values != null && values.size() > 0) {
			values.clear();
			xVals.clear();
		}
		if (rawData != null) {
			values = (ArrayList<Entry>) rawData[0];
			if (checkDaily) {
				xVals = (ArrayList<String>) rawData[1];
				
			} else {
				for (String key : (ArrayList<String>) rawData[1]) {
					xVals.add(monthsName.get(Integer.valueOf(key)));
				}
			}
			CustomMarkerView.LAST_INDEX = xVals.size()-1;
		}
			LineDataSet dataSet = new LineDataSet(values, "Total Usage");
			dataSet.setDrawCircles(true);
			dataSet.setCircleColor(Color.YELLOW);
			dataSet.setDrawCircleHole(false);

			dataSet.setCircleSize(5f);
			dataSet.setDrawFilled(true);
			dataSet.setColor(Color.YELLOW);
			dataSet.setFillColor(context.getResources().getColor(
					R.color.line_chart_fill_color));
			// dataSet.setValueTextSize(20);
			dataSet.setDrawValues(false);
			dataSet.setDrawHighlightIndicators(false);

			ArrayList<LineDataSet> dataSets = new ArrayList<>();
			dataSets.add(dataSet);
			LineData data = new LineData(xVals, dataSets);
			usage.setData(data);

			// usage.setDoubleTapToZoomEnabled(true);
			usage.animateY(2000, Easing.EasingOption.EaseInBounce);

			
			int totalUse = usageHandler.getTotalUsageForMonth(
					calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

			info.setText(String.format(originalInfo, totalUse,calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)));
		}

	

	private String originalInfo = "You've used %1$s kyats in %2$s.";

	private void setListeners() {
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				checkDaily = !checkDaily;
				populateCharts(checkDaily);
				
				if(checkDaily){
					button.setText(Daily);
					spinner.setVisibility(View.VISIBLE);
				}else{
					button.setText(Monthly);
					spinner.setVisibility(View.GONE);
				}
				
				

			}
		});
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				calendar.set(Calendar.MONTH,position);
				populateCharts(checkDaily);
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		
		});
	
	}
}
