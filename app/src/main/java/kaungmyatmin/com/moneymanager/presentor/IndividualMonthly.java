package kaungmyatmin.com.moneymanager.presentor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import kaungmyatmin.com.moneymanager.CustomMarkerView;
import kaungmyatmin.com.moneymanager.DB.DataBaseAdaptor;
import kaungmyatmin.com.moneymanager.DB.ValHolder;
import kaungmyatmin.com.moneymanager.Models.UsageHandler;
import kaungmyatmin.com.moneymanager.POJO.UsageData;
import kaungmyatmin.com.moneymanager.R;
import kaungmyatmin.com.moneymanager.custom.CustomExpandableListAdaptor;
import kaungmyatmin.com.moneymanager.custom.CustomExpandableListAdaptorIndividualDaily;
import kaungmyatmin.com.moneymanager.presentor.common.BaseActivity;

public class IndividualMonthly extends BaseActivity {

	private ArrayList<Entry> values;
	private ArrayList<String> xVals;
	private LineChart usage;
	private ExpandableListView parentList;
	
	private boolean checkDaily = true;
	private int month, year;
	private String title;

	private ArrayList<UsageData> parents = new ArrayList<>();
	private HashMap<String, ArrayList<UsageData>> childs = new HashMap<>();
	private Calendar calendar = Calendar.getInstance();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_monthly);
		Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolBar);
		usage = (LineChart) findViewById(R.id.use_chart_useage);
		parentList = (ExpandableListView) findViewById(R.id.frag_daily_expandableList);
		
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		month = bundle.getInt(ValHolder.MONTH);
		year = bundle.getInt(ValHolder.YEAR);
		title = bundle.getString(ValHolder.TITLE);

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		
		customizedCharts(usage);
		populateCharts(checkDaily);
		populateList();

	}
	private void populateUI() {

		if (childs != null && childs.size() > 0) {
			childs.clear();
			parents.clear();
		}
		
		DataBaseAdaptor dbAdaptor = new DataBaseAdaptor(this,
				ValHolder.TABLE_MONEY_USAGE);

		dbAdaptor.open();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		String sql = String
				.format(getString(R.string.sql_select_individual_daily_order_millisecond),
						day, month, year);
		Cursor c = dbAdaptor.selectWithRawQuery(sql, null);
		if (c != null && c.moveToLast()) {
			while (!c.isBeforeFirst()) {
				int amt = c.getInt(c.getColumnIndex(ValHolder.SUM));
				String title = c.getString(1);
				String sql2 = String
						.format(getString(R.string.sql_select_individual_daily_for_chlid),
								day, month, year, title);
				Cursor cc = dbAdaptor.selectWithRawQuery(sql2, null);
				if (cc != null && cc.moveToLast()) {
					ArrayList<UsageData> childData = new ArrayList<>();

					while (!cc.isBeforeFirst()) {
						int amt2 = cc.getInt(cc.getColumnIndex(ValHolder.AMT));
						String reason = cc.getString(cc
								.getColumnIndex(ValHolder.REASON));
						int hour = cc.getInt(cc.getColumnIndex(ValHolder.HOUR));
						int min = cc
								.getInt(cc.getColumnIndex(ValHolder.MINUTE));
						int id = cc.getInt(cc.getColumnIndex(ValHolder.ID));
						UsageData data = new UsageData();
						data.setTitle(title);
						data.setAmount(amt2);
						data.setReason(reason);
						data.setHour(hour);
						data.setMinute(min);
						data.setId(id);
						childData.add(data);

						cc.moveToPrevious();
					}

					cc.close();
					childs.put(title, childData);

				}
				UsageData data = new UsageData();
				data.setAmount(amt);
				data.setTitle(title);
				parents.add(data);
				c.moveToPrevious();
			}

		}
		c.close();
		sql = String.format(
				getString(R.string.sql_select_individual_total), day,
				month, year,title);
		c = dbAdaptor.selectWithRawQuery(sql, null);
		UsageData data = new UsageData();
		data.setAmount(c.getInt(0));
		data.setTitle("စုစုေပါင္း");
		parents.add(data);

		c.close();
		dbAdaptor.close();

		CustomExpandableListAdaptor adapter = new CustomExpandableListAdaptor(
				this, this, parents, childs);
		parentList.setAdapter(adapter);

	}
	
	private void populateList(){
		HashMap<String, ArrayList<UsageData>> childs = new HashMap<>();
		 ArrayList<Entry> parentValues = (ArrayList<Entry>) values.clone();
	
		 DataBaseAdaptor dbAdaptor = new DataBaseAdaptor(this,
				ValHolder.TABLE_MONEY_USAGE);

		dbAdaptor.open();
		for(String day : xVals){
		String sql = String
				.format(getString(R.string.sql_select_individual_daily_for_chlid),
						Integer.valueOf(day), month, year, title);
		Cursor cc = dbAdaptor.selectWithRawQuery(sql, null);
		if (cc != null && cc.moveToLast()) {
			ArrayList<UsageData> childData = new ArrayList<>();

			while (!cc.isBeforeFirst()) {
				int amt2 = cc.getInt(cc.getColumnIndex(ValHolder.AMT));
				String reason = cc.getString(cc
						.getColumnIndex(ValHolder.REASON));
				int hour = cc.getInt(cc.getColumnIndex(ValHolder.HOUR));
				int min = cc
						.getInt(cc.getColumnIndex(ValHolder.MINUTE));
				int id = cc.getInt(cc.getColumnIndex(ValHolder.ID));
				
				UsageData data = new UsageData();
				data.setTitle(title);
				data.setAmount(amt2);
				data.setReason(reason);
				data.setHour(hour);
				data.setMinute(min);
				data.setId(id);
				childData.add(data);

				cc.moveToPrevious();
			}

			cc.close();
			childs.put(day, childData);
		}
		}
		
		String sql = String.format(
				getString(R.string.sql_select_individual_total),
				month, year,title);
		Cursor c = dbAdaptor.selectWithRawQuery(sql, null);
		Entry data = new Entry(c.getInt(0), 32);
		parentValues.add(data);
		
		
		CustomExpandableListAdaptorIndividualDaily adapter = new CustomExpandableListAdaptorIndividualDaily(this, this, parentValues, xVals, childs);
		
		
		parentList.setAdapter(adapter);
		
	}
	private void populateCharts(boolean checkDaily) {
		if (checkDaily) {
			UsageHandler usageHandler = new UsageHandler(this, null);
			Object[] rawData = usageHandler.getLineData(checkDaily,
					ValHolder.TYPE_INDIVIDUAL_DAILY_USAGE, month, year, title);
			if (rawData != null) {
				values = (ArrayList<Entry>) rawData[0];
				xVals = (ArrayList<String>) rawData[1];
				LineDataSet dataSet = new LineDataSet(values, "Total Usage");
				dataSet.setDrawCircles(true);
				dataSet.setCircleColor(Color.YELLOW);
				dataSet.setDrawCircleHole(false);
				
				
				dataSet.setCircleSize(5f);
				dataSet.setDrawFilled(true);
				dataSet.setColor(Color.YELLOW);
				dataSet.setFillColor(getResources().getColor(
						R.color.line_chart_fill_color));
				// dataSet.setValueTextSize(20);
				dataSet.setDrawValues(false);
				dataSet.setDrawHighlightIndicators(false);

				ArrayList<LineDataSet> dataSets = new ArrayList<>();
				dataSets.add(dataSet);
				LineData data = new LineData(xVals, dataSets);
				usage.setData(data);

				// usage.setDoubleTapToZoomEnabled(true);
				usage.animateY(3000, Easing.EasingOption.EaseInBounce);
			}
		}
	}
	
private void customizedCharts(LineChart chart){
		
	chart.setDrawGridBackground(false);
	chart.setDrawBorders(false);
	chart.setBorderWidth(0);
	chart.getLegend().setEnabled(false);
	chart.setDoubleTapToZoomEnabled(false);
	chart.setPinchZoom(false);
	chart.setDescription(title);
	chart.setDescriptionTextSize(16f);

	CustomMarkerView mv = new CustomMarkerView(this, R.layout.marker);
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
}
