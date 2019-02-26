package kaungmyatmin.com.moneymanager.Models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import kaungmyatmin.com.moneymanager.DB.DataBaseAdaptor;
import kaungmyatmin.com.moneymanager.DB.ValHolder;
import kaungmyatmin.com.moneymanager.POJO.UsageData;
import kaungmyatmin.com.moneymanager.R;


public class UsageHandler {
	private DataBaseAdaptor dbAdaptor;
	private Context context;
	private HashMap<String, String> titles;

	public UsageHandler(Context context, String tableName) {
		this.context = context;
		dbAdaptor = new DataBaseAdaptor(context, tableName);
		init();
	}

	private void init() {
		titles = new HashMap<>();
		Resources res = context.getResources();
		String[] titlesEng = res.getStringArray(R.array.catagories_drawables);
		String[] titlesMyanmar = res.getStringArray(R.array.catagories_titles);
		for (int i = 0; i < titlesEng.length; i++) {
			titles.put(titlesEng[i], titlesMyanmar[i]);
		}

	}

	public int getTotalUsageForMonth(int month, int year) {
		int total = 0;
		dbAdaptor.open();
		String sql = String.format(
				context.getString(R.string.sql_select_total_amt_until_today),
				month, year);
		
		Cursor cc = dbAdaptor.selectWithRawQuery(sql, null);
		if(cc!=null && cc.moveToFirst()){
			total = cc.getInt(cc.getColumnIndex(ValHolder.SUM));
		}
		return total;
	}

	public Object[] getLineData(boolean isDaily, int type, Object... args) {
		Object[] data = null;

		String sql = null;
		if (isDaily && type == ValHolder.TYPE_TOTAL_USAGE) {
			sql = context.getString(R.string.sql_select_total_daily_usage);
			sql = String.format(sql, args);
		} else if (!isDaily && type == ValHolder.TYPE_TOTAL_USAGE) {
			sql = context.getString(R.string.sql_select_total_monthly_usage);//now
			sql = String.format(sql,2016);
		} else if (isDaily && type == ValHolder.TYPE_TOTAL_SAVE) {
			sql = context.getString(R.string.sql_select_daily_save);
			
		} else if (!isDaily && type == ValHolder.TYPE_TOTAL_SAVE) {
			sql = context.getString(R.string.sql_select_monthly_save);
			sql = String.format(sql, args);
		} else if (isDaily && type == ValHolder.TYPE_INDIVIDUAL_DAILY_USAGE) {
			sql = context
					.getString(R.string.sql_select_individual_daily_line_chart);
			sql = String.format(sql, args);
		}
		dbAdaptor.open();
		Cursor cc = dbAdaptor.selectWithRawQuery(sql, null);
		if (cc != null && cc.moveToLast()) {
			ArrayList<Entry> usage = new ArrayList<>();
			ArrayList<String> xVals = new ArrayList<>();
			int i = 0;
			while (!cc.isBeforeFirst()) {
				Entry entry = new Entry(cc.getInt(0), i++);
				usage.add(entry);
				xVals.add(String.valueOf(cc.getInt(1)));
				cc.moveToPrevious();

			}

			data = new Object[2];
			data[0] = usage;
			data[1] = xVals;

			cc.close();
			dbAdaptor.close();

		}

		return data;
	}

	public Object[] getBarData(int type) {
		Object[] data = null;

		String[] images = { "bus.png", "cafe.png", "cloth.png", "comedy.png",
				"food.png", "gift.png", "gift2.png", "home.png", "taxi.png",
				"taxi2.png", "sport.png" };
		String sql = null;
		if (type == ValHolder.TYPE_INDIVIDUAL_DAILY_USAGE) {
			sql = context.getString(R.string.sql_select_individual_daily);
		} else if (type == ValHolder.TYPE_INDIVIDUAL_MONTHLY_USAGE) {
			sql = context.getString(R.string.sql_select_total_monthly_usage);
		}
		dbAdaptor.open();
		Cursor cc = dbAdaptor.selectWithRawQuery(sql, null);
		if (cc != null && cc.moveToLast()) {
			ArrayList<BarEntry> usage = new ArrayList<>();
			ArrayList<String> xVals = new ArrayList<>();
			int i = 0;
			while (!cc.isBeforeFirst()) {
				BarEntry entry = new BarEntry(cc.getInt(0), i++);
				usage.add(entry);

				xVals.add(images[i]);
				// xVals.add(String.valueOf(i));
				// xVals.add(String.valueOf(cc.getInt(1)));
				cc.moveToPrevious();

			}

			data = new Object[2];
			data[0] = usage;
			data[1] = xVals;

			cc.close();
			dbAdaptor.close();
		}
		return data;
	}

	public Object[] getBarData(long millisecond) {

		Object[] data = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millisecond);
		// calendar.set(2015, 5, 24);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);

		String sql = context.getString(R.string.sql_select_individual_daily);
		sql = String.format(sql, day, month, year);

		dbAdaptor.open();
		Cursor cc = dbAdaptor.selectWithRawQuery(sql, null);
		if (cc != null && cc.moveToLast()) {
			ArrayList<BarEntry> usage = new ArrayList<>();
			ArrayList<String> xVals = new ArrayList<>();
			int i = 0;
			while (!cc.isBeforeFirst()) {
				BarEntry entry = new BarEntry(cc.getInt(cc
						.getColumnIndex(ValHolder.SUM)), i++);
				usage.add(entry);
				// String key =
				// cc.getString(cc.getColumnIndex(ValHolder.TITLE));
				// String title = titles.get(key);
				String title = cc.getString(cc.getColumnIndex(ValHolder.TITLE));
				xVals.add(title);
				// xVals.add(images[i]);

				cc.moveToPrevious();

			}

			data = new Object[2];
			data[0] = usage;
			data[1] = xVals;

			cc.close();
			dbAdaptor.close();

		}

		return data;

	}

	public long saveUsage(String title, String reason, int amt,
			Calendar calendar) {
		ContentValues valuesToInsert = new ContentValues();
		valuesToInsert.put(ValHolder.USER_NAME, "uid1");
		valuesToInsert.put(ValHolder.TITLE, title);
		valuesToInsert.put(ValHolder.REASON, reason);
		valuesToInsert.put(ValHolder.AMT, amt);
		valuesToInsert.put(ValHolder.YEAR, calendar.get(Calendar.YEAR));
		valuesToInsert.put(ValHolder.MONTH, calendar.get(Calendar.MONTH));
		valuesToInsert.put(ValHolder.DAY, calendar.get(Calendar.DAY_OF_MONTH));
		valuesToInsert.put(ValHolder.HOUR, calendar.get(Calendar.HOUR));
		valuesToInsert.put(ValHolder.MINUTE, calendar.get(Calendar.MINUTE));
		valuesToInsert.put(ValHolder.MILLISECOND, calendar.getTimeInMillis());
		dbAdaptor.open();
		long id = dbAdaptor.insert(valuesToInsert);
		dbAdaptor.close();
		return id;

	}
	
	public boolean deleteWithId(int id){
		String whereClause = "id = "+id;
		dbAdaptor.open();
		boolean check =  dbAdaptor.delete(whereClause);
		dbAdaptor.close();
		return check;
	}

	public boolean updateUsage(String whereClause, UsageData data) {
		ContentValues valuesToUpdate = new ContentValues();
		valuesToUpdate.put(ValHolder.TITLE, data.getTitle());
		valuesToUpdate.put(ValHolder.AMT, data.getAmount());
		valuesToUpdate.put(ValHolder.MINUTE, data.getMinute());
		valuesToUpdate.put(ValHolder.HOUR, data.getHour());
		valuesToUpdate.put(ValHolder.DAY, data.getDay());
		valuesToUpdate.put(ValHolder.MONTH, data.getMonth());
		valuesToUpdate.put(ValHolder.YEAR, data.getYear());
		valuesToUpdate.put(ValHolder.MILLISECOND, data.getMilliSecond());
		dbAdaptor.open();
		boolean check = dbAdaptor.update(whereClause, valuesToUpdate);
		dbAdaptor.close();
		return check;

	}

}
