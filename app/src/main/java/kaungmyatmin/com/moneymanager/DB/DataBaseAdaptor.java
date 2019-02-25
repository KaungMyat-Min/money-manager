package kaungmyatmin.com.moneymanager.DB;

import java.util.ArrayList;

import com.trio.moneymanager.POJO.UsageData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DataBaseAdaptor {
	Context context;
	String tableName;

	public DataBaseAdaptor(Context context, String tableName) {
		this.context = context;
		this.tableName = tableName;
	}

	public long insert(String title, int amt, int day, int month, int year,long milliSecond) {
		ContentValues valuesToInsert = new ContentValues();
		valuesToInsert.put(ValHolder.TITLE, title);
		valuesToInsert.put(ValHolder.AMT, amt);
		valuesToInsert.put(ValHolder.DAY, day);
		valuesToInsert.put(ValHolder.MONTH, month);
		valuesToInsert.put(ValHolder.YEAR, year);
		valuesToInsert.put(ValHolder.DATE, milliSecond);
		return DBHelper.insert(tableName, valuesToInsert);
	}
	
	public long insert(String title,String reason, int amt, int year, int month, int day, int hour, int min,long milliSecond) {
		ContentValues valuesToInsert = new ContentValues();
		valuesToInsert.put(ValHolder.TITLE, title);
		valuesToInsert.put(ValHolder.REASON, reason);
		valuesToInsert.put(ValHolder.AMT, amt);
		valuesToInsert.put(ValHolder.YEAR, year);
		valuesToInsert.put(ValHolder.MONTH, month);
		valuesToInsert.put(ValHolder.DAY, day);
		valuesToInsert.put(ValHolder.HOUR, hour);
		valuesToInsert.put(ValHolder.MINUTE, min);
		valuesToInsert.put(ValHolder.DATE, milliSecond);
		return DBHelper.insert(tableName, valuesToInsert);
	}

	public long insert(ContentValues valuesToinsert){
		return DBHelper.insert(tableName, valuesToinsert);
	}
	public Cursor selectWithRawQuery(String sql, String [] args){
		return DBHelper.selectColumns(sql, args);
	}
	public ArrayList<UsageData> select(String whereClause,String groupBy) {
		ArrayList<UsageData> dataArray = null;
		Cursor c = DBHelper.select(tableName, whereClause,groupBy);
		if (c != null) {
			dataArray = new ArrayList<>();
			while (!c.isAfterLast()) {
				UsageData data = new UsageData();
				data.setAmount(c.getInt(c.getColumnIndex(ValHolder.AMT)));
				data.setDay(c.getInt(c.getColumnIndex(ValHolder.DAY)));
				data.setMonth(c.getInt(c.getColumnIndex(ValHolder.MONTH)));
				data.setTitle(c.getString(c.getColumnIndex(ValHolder.TITLE)));
				data.setYear(c.getInt(c.getColumnIndex(ValHolder.YEAR)));
				dataArray.add(data);
				c.moveToNext();
			}
			c.close();
		}
		return dataArray;
	}

	public ArrayList<UsageData> selectColumns(String sql, String [] selections) {
		ArrayList<UsageData> dataArray = null;
		Cursor c = DBHelper.selectColumns(sql, selections);
		if (c != null) {
			dataArray = new ArrayList<>();
			while (!c.isAfterLast()) {
				UsageData data = new UsageData();
				data.setAmount(c.getInt(c.getColumnIndex(ValHolder.AMT)));
				data.setDay(c.getInt(c.getColumnIndex(ValHolder.DAY)));
				data.setMonth(c.getInt(c.getColumnIndex(ValHolder.MONTH)));
				data.setTitle(c.getString(c.getColumnIndex(ValHolder.TITLE)));
				data.setYear(c.getInt(c.getColumnIndex(ValHolder.YEAR)));
				dataArray.add(data);
				c.moveToNext();
			}
			c.close();
		}
		return dataArray;
	}

	public void open() {
		DBHelper.open(context);

	}

	public void close() {
		DBHelper.closeDB();

	}

	public boolean update(String whereClause, ContentValues valuesToUpdate) {
		
		
		return DBHelper.update(tableName, whereClause, valuesToUpdate);

	}

	public boolean delete(String whereClause) {
		return DBHelper.delete(tableName, whereClause);
		
	}

}
