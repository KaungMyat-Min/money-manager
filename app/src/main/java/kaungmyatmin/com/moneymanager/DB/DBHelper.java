package kaungmyatmin.com.moneymanager.DB;

import com.trio.moneymanager.R;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {
	private Context context;
	private static DBHelper dbHelper;

	private static SQLiteDatabase mdb;

	private DBHelper(Context context) {
		super(context, ValHolder.DATABASE_NAME, null,
				ValHolder.DATABASE_VERSION);
		this.context = context;
	}

	public static void open(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context.getApplicationContext());
		}
		if (mdb == null || !mdb.isOpen()) {
			mdb = dbHelper.getWritableDatabase();
		}
	}

	public static void closeDB() {
		if (mdb != null && mdb.isOpen()) {
			mdb.close();
		}
	}

	public static long insert(String table_name, ContentValues valuesToInsert) {
		long idd = mdb.insert(table_name, null, valuesToInsert);
		return idd;
	}

	public static boolean update(String table_name, String whereClause,
			ContentValues valuesToUpdate) {
		return mdb.update(table_name, valuesToUpdate, whereClause, null) > 0;
	}

	public static boolean delete(String table_name, String whereClause) {
		return mdb.delete(table_name, whereClause, null) > 0;

	}

	public static Cursor select(String table_name, String whereClause,
			String groupBy) {

		Cursor cc = mdb.query(table_name, null, whereClause, null, groupBy,
				null, null);
		if (cc != null) {
			boolean b = cc.moveToFirst();
		}
		return cc;
	}

	public static Cursor selectColumns(String sql, String[] selections) {
		Cursor cc = mdb.rawQuery(sql, selections);
		if (cc != null) {
			boolean b = cc.moveToFirst();
		}
		return cc;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		switch (ValHolder.DATABASE_VERSION) {
		case 1:
			db.execSQL(context.getString(R.string.db_create_data_usage));
			db.execSQL(context.getString(R.string.db_create_money_usage));
			db.execSQL(context.getString(R.string.db_create_phone_usage));
			db.execSQL(context.getString(R.string.db_create_save_debt));
			/*String[] queries = context.getResources().getStringArray(
					R.array.insert_sql);
			for (String query : queries) {
				db.execSQL(query);
			}*/
			break;
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
