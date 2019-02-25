package kaungmyatmin.com.moneymanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.renderer.XAxisRendererHorizontalBarChart;
import com.trio.moneymanager.DB.DataBaseAdaptor;
import com.trio.moneymanager.DB.ValHolder;
import com.trio.moneymanager.Model.UsageHandler;
import com.trio.moneymanager.Model.UserDataHandler;
import com.trio.moneymanager.POJO.UsageData;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class FragIndividualUsage extends Fragment {
	private Button datePick;
	private Button maxCanUse;
	private HorizontalBarChart usage;
	private TextView useAct, useShould;
	private ImageButton income, outcome, detail;

	private ArrayList<BarEntry> values;
	private ArrayList<String> xVals;

	private Context context;
	private Activity callerActivity;

	private SimpleDateFormat dateFormat;
	private Calendar calendar = Calendar.getInstance();

	private int year, month, day;
	private int[] colors = new int[] { 0xffe192f5, 0xffda7095, 0xff65e4b5,
			0xff67cde2, 0xffc8bfe7, 0xffbaaeae, 0xffff80ff, 0xffb9fb8c,
			0xffeea699, 0xfffdfa8a, 0xff8facf8, 0xffabcfa9, 0xffb5a2e6,
			0xfffac28f, 0xffbdd2ce, 0xff7777ec };

	private int maxUseDaily;
	private int maxUseMonthly;
	private int useRemain;
	private boolean isDaily;

	public FragIndividualUsage(Context context, Activity activity) {
		this.context = context;
		this.callerActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.frag_individual_usage, null);

		datePick = (Button) v.findViewById(R.id.frag_indi_but_date);
		usage = (HorizontalBarChart) v.findViewById(R.id.frag_indi_chart);
		detail = (ImageButton) v.findViewById(R.id.net_result);
		useAct = (TextView) v.findViewById(R.id.txt_actual_use);
		useShould = (TextView) v.findViewById(R.id.txt_should_use);
		income = (ImageButton) v.findViewById(R.id.income);
		outcome = (ImageButton) v.findViewById(R.id.outcome);
		maxCanUse = (Button) v.findViewById(R.id.max_can_use);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		maxUseDaily = pref.getInt(ValHolder.KEY_MAX_USE_PER_DAY, 2000);
		maxUseMonthly = pref.getInt(ValHolder.KEY_MAX_USE_PER_MONTH, 60000);

		// usage.animateX(2000, Easing.EasingOption.EaseInBack);
		// usage.animateY(3000);

		usage.getLegend().setEnabled(false);
		usage.setDescription("");
		usage.setDoubleTapToZoomEnabled(false);
		usage.setPinchZoom(false);
		usage.setDragEnabled(true);
		usage.setDrawValueAboveBar(true);
		usage.setSoundEffectsEnabled(true);
		usage.enableScroll();
		usage.setOnChartGestureListener(new ChartGature());
		usage.setBackground(null);
		usage.setDrawGridBackground(false);
		usage.setNoDataText("");
		usage.setNoDataTextDescription("");
		// CustomRender.setContext(this);
		// CustomRender mrenderer = (CustomRender) renderer;

		XAxis xAxis = usage.getXAxis();
		xAxis.setPosition(XAxisPosition.BOTTOM);
		xAxis.setDrawAxisLine(false);
		xAxis.setDrawGridLines(false);
		xAxis.setDrawLimitLinesBehindData(false);
		xAxis.setTextSize(20f);
		xAxis.setLabelsToSkip(0);

		YAxis yRight = usage.getAxisRight();
		yRight.setDrawAxisLine(false);
		yRight.setDrawGridLines(false);
		yRight.setDrawLimitLinesBehindData(false);
		yRight.setDrawLabels(false);
		yRight.setDrawTopYLabelEntry(false);

		YAxis yLeft = usage.getAxisLeft();

		yLeft.setDrawAxisLine(false);
		yLeft.setDrawGridLines(false);

		yLeft.setDrawLimitLinesBehindData(false);
		yLeft.setDrawLabels(false);
		yLeft.setDrawTopYLabelEntry(false);

		if (savedInstanceState != null) {
			long millisecond = savedInstanceState
					.getLong(ValHolder.MILLISECOND);
			if (millisecond != 0) {
				calendar.setTimeInMillis(millisecond);
			}
		}
		setCurrentDate();
		setListeners();
		// detail.setText(makePretty(xVals, colors));

		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		populateChart(calendar.getTimeInMillis());
		populateUI();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		outState.putLong(ValHolder.MILLISECOND, calendar.getTimeInMillis());

	}

	private void setListeners() {
		income.setOnClickListener(new fabListener());
		outcome.setOnClickListener(new fabListener());
		detail.setOnClickListener(new fabListener());

		datePick.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});

		maxCanUse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(callerActivity);
				
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setCancelable(true);
				
				dialog.setContentView(R.layout.dialog);
				final TextView amt = (TextView) dialog
						.findViewById(R.id.dialog_body);
				Button ok = (Button) dialog.findViewById(R.id.dialog_ok);
				Button cancel = (Button) dialog
						.findViewById(R.id.dialog_cancel);

				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try{
						maxUseMonthly = Integer.valueOf(amt.getText()
								.toString());
						}catch(NumberFormatException e){
							dialog.hide();
						}
						maxUseDaily = maxUseMonthly / 30;
						maxUseDaily -= maxUseDaily % 50;

						SharedPreferences pref = PreferenceManager
								.getDefaultSharedPreferences(context);

						Editor editor = pref.edit();
						editor.putInt(ValHolder.KEY_MAX_USE_PER_MONTH,
								maxUseMonthly);
						editor.putInt(ValHolder.KEY_MAX_USE_PER_DAY,
								maxUseDaily);
						editor.commit();
						populateUI();
						dialog.hide();

					}
				});

				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.hide();

					}
				});

				dialog.show();

			}
		});

	}

	private void populateUI() {

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		DataBaseAdaptor dbAdaptor = new DataBaseAdaptor(context,
				ValHolder.TABLE_MONEY_USAGE);

		dbAdaptor.open();
		String sql = String.format(
				getString(R.string.sql_select_total_amt_of_one_month), month,
				year);
		Cursor c = dbAdaptor.selectWithRawQuery(sql, null);
		int total = c.getInt(0);
		useRemain = maxUseMonthly - total;
		c.close();

		sql = String.format(
				getString(R.string.sql_select_total_amt_of_one_day), day,
				month, year);
		c = dbAdaptor.selectWithRawQuery(sql, null);
		total = c.getInt(0);

		dbAdaptor.close();

		useAct.setText(Integer.toString(total));
		useShould.setText(Integer.toString(maxUseDaily));

		/*
		 * String rawText = Integer.toString(total) + "/" + maxUseDaily;
		 * SpannableString spanner = new SpannableString(rawText); int start =
		 * rawText.indexOf("/"); int end = rawText.length(); spanner.setSpan(new
		 * RelativeSizeSpan(0.8f), start, end,
		 * SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE); spanner.setSpan(new
		 * ForegroundColorSpan(Color.BLUE), start, end,
		 * SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE); detail.setText(spanner);
		 */
		if (total < maxUseDaily) {
			useAct.setTextColor(0xff177b35);// dark green

		} else if (total == maxUseDaily) {
			useAct.setTextColor(Color.BLUE);
		} else {
			useAct.setTextColor(Color.RED);
		}

		 maxCanUse.setText("က်န္ေငြ "+Integer.toString(useRemain) + "က်ပ္");

	}

	private void populateChart(long millisecond) {
		UsageHandler usageHandler = new UsageHandler(context, null);
		Object[] rawData = usageHandler.getBarData(millisecond);
		if (rawData != null) {
			values = (ArrayList<BarEntry>) rawData[0];
			xVals = (ArrayList<String>) rawData[1];

			BarDataSet dataSet = new BarDataSet(values, "HaHa");
			dataSet.setValueTextSize(20);

			// violet ,red , green, blue, kayan, grey,pink,pale green, pale
			// brown,
			// pale yellow, naval blue, green again, indigo, oak

			dataSet.setColors(colors);
			dataSet.setValueTextColor(Color.MAGENTA);


			ArrayList<BarDataSet> dataSets = new ArrayList<>();
			dataSets.add(dataSet);
			BarData data = new BarData(xVals, dataSets);
			usage.setData(data);

			
			int height = Resources.getSystem().getDisplayMetrics().heightPixels;
			height = height * xVals.size() / 20;
			usage.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, height));
			usage.animateXY(2000, 3000);
		} else {
			usage.clear();
		}

	}

	private void setCurrentDate() {

		year = calendar.get(Calendar.YEAR);
		String monthName = calendar.getDisplayName(Calendar.MONTH,
				Calendar.SHORT, Locale.US);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		datePick.setText(new StringBuffer().append(day).append("/")
				.append(monthName).append("/").append(year).toString());

	}

	private SpannableString makePretty(ArrayList<String> list, int... colors) {
		ArrayList<Integer> lengths = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		int no = 0;
		String preFix = ". ";

		String postFix = "\n";
		for (String raw : list) {
			++no;
			if (no < 10) {
				builder.append(0).append(no).append(preFix).append(raw)
						.append(postFix);
				// lastSize = builder.length() - lastSize;
				lengths.add(builder.length());
			} else {
				builder.append(no).append(preFix).append(raw).append(postFix);

				lengths.add(builder.length());
			}
		}

		SpannableString text = new SpannableString(builder);
		int totalColor = colors.length;
		int colorIndex = 0;
		int start = 0;
		for (int end : lengths) {
			if (colorIndex < totalColor) {
				text.setSpan(new ForegroundColorSpan(colors[colorIndex++]),
						start, end, 0);
				start = end;
			} else {

				text.setSpan(new ForegroundColorSpan(colors[colorIndex = 0]),
						start, end, 0);
				colorIndex++;
				start = end;
			}
		}
		return text;

	}

	private void showDialog(int id) {

		new DatePickerDialog(
				callerActivity,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {

						calendar.set(year, monthOfYear, dayOfMonth);
						dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
						datePick.setText(dateFormat.format(calendar.getTime()));
						populateChart(calendar.getTimeInMillis());
						populateUI();

					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	private class fabListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			// id for income or outcome

			Intent i = new Intent(context,
					com.trio.moneymanager.EditDaily.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			i.putExtra(ValHolder.YEAR, calendar.get(Calendar.YEAR));
			i.putExtra(ValHolder.MONTH, calendar.get(Calendar.MONTH));
			i.putExtra(ValHolder.DAY, calendar.get(Calendar.DAY_OF_MONTH));
			context.startActivity(i);

			callerActivity.overridePendingTransition(R.anim.slide_up_true,
					R.anim.fade_out);

		}

	}

	private class ChartGature implements OnChartGestureListener {

		@Override
		public void onChartGestureStart(MotionEvent me,
				ChartGesture lastPerformedGesture) {
		}

		@Override
		public void onChartGestureEnd(MotionEvent me,
				ChartGesture lastPerformedGesture) {

		}

		@Override
		public void onChartLongPressed(MotionEvent me) {

		}

		@Override
		public void onChartDoubleTapped(MotionEvent me) {

		}

		@Override
		public void onChartSingleTapped(MotionEvent me) {
			final Entry entry = usage
					.getEntryByTouchPoint(me.getX(), me.getY());
			int xIndex = entry.getXIndex();
			String title = xVals.get(xIndex);
			Intent i = new Intent(context,
					com.trio.moneymanager.IndividualMonthly.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra(ValHolder.MONTH, calendar.get(Calendar.MONTH));
			i.putExtra(ValHolder.YEAR, calendar.get(Calendar.YEAR));
			i.putExtra(ValHolder.TITLE, title);
			context.startActivity(i);

		}

		@Override
		public void onChartFling(MotionEvent me1, MotionEvent me2,
				float velocityX, float velocityY) {

		}

		@Override
		public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

		}

		@Override
		public void onChartTranslate(MotionEvent me, float dX, float dY) {

		}

	}
}
