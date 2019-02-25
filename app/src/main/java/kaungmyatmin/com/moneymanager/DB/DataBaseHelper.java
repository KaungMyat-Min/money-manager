package kaungmyatmin.com.moneymanager.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper {
	

	private SQLiteDatabase mdb;
	private Context mctx;
	private DataBaseOpener mdbHelper;

	public DataBaseHelper(Context tx) {
		mctx = tx;
	}

	public void close() {
		if (mdb != null && mdb.isOpen())
			mdb.close();
	}

	public DataBaseHelper open() throws SQLException {
		mdbHelper = new DataBaseOpener(mctx);
		if (mdb == null || !mdb.isOpen())
			mdb = mdbHelper.getWritableDatabase();

		return this;
	}

	protected long insert(String table_name, ContentValues valuesToInsert) {
		long idd = mdb.insert(table_name, null, valuesToInsert);
		return idd;
	}

	protected Cursor selectDistinct(String table_name, String[] columns,
			String whereClause) {

		Cursor cc = mdb.query(true, table_name, columns, whereClause, null,
				null, null, null, null, null);

		if (cc != null) {
			boolean b = cc.moveToFirst();
		}
		return cc;

	}

	protected Cursor select(String table_name, String whereClause) {

		Cursor cc = mdb.query(table_name, null, whereClause, null, null, null,
				null);

		if (cc != null) {
			boolean b = cc.moveToFirst();
		}
		return cc;

	}

	protected boolean update(String table_name, String whereClause,
			ContentValues valuesToUpdate) {
		return mdb.update(table_name, valuesToUpdate, whereClause, null) > 0;
	}

	public boolean delete(String table_name, String whereClause) {
		return mdb.delete(table_name, whereClause, null) > 0;

	}

	// beginning of inner class
	private class DataBaseOpener extends SQLiteOpenHelper {
		public DataBaseOpener(Context context) {
			super(context, ValHolder.DATABASE_NAME, null, ValHolder.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS AnswerTheQuestion");
			db.execSQL("create table AnswerTheQuestion ( _id int primary key, question_id text not null,_UNIT_NO text not null,_type text not null,question text not null,answer text not null,answer_fake1 text,answer_fake2 text,attempt int,success int)");
		
			db.execSQL("DROP TABLE IF EXISTS Quotes");
			db.execSQL("create table Quotes ( _id int primary key, quote_author text not null,quote_body text not null,quote_myanmar text not null,_type text not null)");
			
			db.execSQL("DROP TABLE IF EXISTS Grammar");
			db.execSQL("create table Grammar ( _id int primary key, question_id text not null,_GRAMMAR_TYPE int not null,_type text not null,question text not null,answer text not null,answer_fake1 text,answer_fake2 text,attempt int,success int,location text not null)");
			
			db.execSQL("DROP TABLE IF EXISTS UserData");
			db.execSQL("create table UserData ( _id int primary key, user_name text not null,question_id text not null,_UNIT_NO text,attempt int not null,success int not null,location text,_type text)");
			

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS AnswerTheQuestion");
			db.execSQL("create table AnswerTheQuestion ( _id int primary key, question_id text not null,_UNIT_NO text not null,_type text not null,question text not null,answer text not null,answer_fake1 text,answer_fake2 text,attempt int,success int)");
		
			db.execSQL("DROP TABLE IF EXISTS Quotes");
			db.execSQL("create table Quotes ( _id int primary key, quote_author text not null,quote_body text not null,quote_myanmar text not null,_type text not null)");
			
			db.execSQL("DROP TABLE IF EXISTS Grammar");
			db.execSQL("create table Grammar ( _id int primary key, question_id text not null,_GRAMMAR_TYPE int not null,_type text not null,question text not null,answer text not null,answer_fake1 text,answer_fake2 text,attempt int,success int,location text not null)");
			
			db.execSQL("DROP TABLE IF EXISTS UserData");
			db.execSQL("create table UserData ( _id int primary key, user_name text not null,question_id text not null,_UNIT_NO text,attempt int not null,success int not null,location text,_type text)");
				}		
	}

}
