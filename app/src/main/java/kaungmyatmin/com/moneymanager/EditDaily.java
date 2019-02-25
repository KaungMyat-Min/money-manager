package kaungmyatmin.com.moneymanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.trio.moneymanager.DB.DataBaseAdaptor;
import com.trio.moneymanager.DB.ValHolder;
import com.trio.moneymanager.Model.UsageHandler;
import com.trio.moneymanager.POJO.UsageData;
import com.trio.moneymanager.custom.CustomExpandableListAdaptor;
import com.trio.moneymanager.custom.CustomGridAdaptor;

public class EditDaily extends ActionBarActivity {

	private GridView gridView;
	private Button showGrid;
	private Animation slideUP, slideDown;
	private boolean isShownGrid = false;
	private boolean isGoneGrid = true;
	private ArrayList<String> titleList, drawableList;

	private ImageView imageView;
	private TextView imageTitle;

	private Button time;
	private EditText amt, reason;

	private Calendar calendar = Calendar.getInstance();
	private SimpleDateFormat dateFormat;
	private int year, month, day;
	private Animation shake;
	private Animation zoom;
	private RelativeLayout newContaner;
	private UsageHandler usageHandler;

	private ExpandableListView parentList;
	private TextView datePick;

	private ArrayList<UsageData> parents = new ArrayList<>();
	private HashMap<String, ArrayList<UsageData>> childs = new HashMap<>();
	private HashMap<String, String> hashMap;

	private boolean isEdit = false;
	private int _id;

	private ActionMode.Callback callback;
	private int groupPosition = -1;
	private int childPosition = -1;

	private String show,hide;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_daily);
		Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolBar);
		ActionBar actionbar = getSupportActionBar();
		actionbar.setCustomView(R.layout.marker);
		actionbar.setDisplayHomeAsUpEnabled(true);

		datePick = (TextView) actionbar.getCustomView();
		gridView = (GridView) findViewById(R.id.frag_daily_grid_type);
		showGrid = (Button) findViewById(R.id.frag_daily_but_types);
		imageView = (ImageView) findViewById(R.id.new_image_show);
		imageTitle = (TextView) findViewById(R.id.new_image_title);
		time = (Button) findViewById(R.id.new_time);
		amt = (EditText) findViewById(R.id.new_amt);
		reason = (EditText) findViewById(R.id.new_reason);
		parentList = (ExpandableListView) findViewById(R.id.frag_daily_expandableList);

		newContaner = (RelativeLayout) findViewById(R.id.frag_daily_new_container);

		usageHandler = new UsageHandler(getApplicationContext(),
				ValHolder.TABLE_MONEY_USAGE);

		slideUP = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_up);
		slideDown = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.slide_down);
		shake = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.shake);
		zoom = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zoom);

		init();

		String text = String.format("%1$02d:%2$02d",
				calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
		time.setText(text);

		CustomGridAdaptor adapter = new CustomGridAdaptor(this, titleList,
				drawableList);
		gridView.setAdapter(adapter);

		populateUI();
		setListeners();

	}

	private void init() {

		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		year = bundle.getInt(ValHolder.YEAR);
		month = bundle.getInt(ValHolder.MONTH);
		day = bundle.getInt(ValHolder.DAY);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);

		String[] titles = getResources().getStringArray(
				R.array.catagories_titles);
		String[] drawables = getResources().getStringArray(
				R.array.catagories_drawables);
		titleList = new ArrayList<>();
		drawableList = new ArrayList<>();

		for (String title : titles) {
			titleList.add(title);
		}

		for (String drawable : drawables) {
			drawableList.add(drawable);
		}

		hashMap = new HashMap<>();
		for (int j = 0; j < titles.length; j++) {
			hashMap.put(titles[j], drawables[j]);
		}

		show = getString(R.string.category_show);
		hide = getString(R.string.category_hide);
	}

	private void populateUI() {

		if (parents != null && parents.size() > 0) {
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
				getString(R.string.sql_select_total_amt_of_one_day), day,
				month, year);
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

	private void populateNewTest(int AMT, String REASON, String title) {
		imageView.setImageResource(getApplicationContext().getResources()
				.getIdentifier(hashMap.get(title), "drawable",
						"com.trio.moneymanager"));
		imageTitle.setText(title);

		String text = String.format("%1$02d:%2$02d",
				calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));

		time.setText(text);
		amt.setText(Integer.toString(AMT));
		if (!REASON.equals("") || REASON != null) {
			reason.setText(REASON);
		}

	}

	private void setListeners() {

		callback = new ActionMode.Callback() {

			@Override
			public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode arg0) {
				newContaner.startAnimation(zoom);
				imageView.setImageResource(R.drawable.question_mark);
				imageTitle.setText("");
				amt.setText("");
				reason.setText("");

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.action_mode, menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				int id = item.getItemId();
				switch (id) {
				case R.id.action_mode_edit:
					saveUsage();
					break;
				case R.id.action_mode_delete:
					deleteUsage();
					break;
				}
				mode.finish();
				return true;
			}
		};

		datePick.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showMyDialog(0);

			}
		});
		showGrid.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				showHideGridView();
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				imageTitle.setText(titleList.get(position));
				imageView.setImageResource(getApplicationContext()
						.getResources().getIdentifier(
								drawableList.get(position), "drawable",
								"com.trio.moneymanager"));
				imageView.startAnimation(shake);

			}

		});

		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showHideGridView();

			}
		});

		time.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showMyDialog(0);

			}
		});

		parentList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				long packedPosition = parentList
						.getExpandableListPosition(position);
				int type = ExpandableListView.getPackedPositionType(id);
				if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
					int groupPosition = ExpandableListView
							.getPackedPositionGroup(packedPosition);
					int childPosition = ExpandableListView
							.getPackedPositionChild(packedPosition);
					

					startSupportActionMode(callback);

					EditDaily.this.groupPosition = groupPosition;
					EditDaily.this.childPosition = childPosition;
					prepareToEdit();

					return true;
				}
				
				return true;

			}
		});

		parentList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				UsageData data = childs.get(
						parents.get(groupPosition).getTitle()).get(
						childPosition);
				int hour = data.getHour();
				int minute = data.getMinute();
				int amt = data.getAmount();
				String reason = data.getReason();
				String title = data.getTitle();
				calendar.set(Calendar.HOUR, hour);
				calendar.set(Calendar.MINUTE, minute);

				populateNewTest(amt, reason, title);
				return true;
			}
		});
	}

	private void prepareToEdit() {

		UsageData data = childs.get(parents.get(groupPosition).getTitle()).get(
				childPosition);
		int hour = data.getHour();
		int minute = data.getMinute();
		int amt = data.getAmount();
		String reason = data.getReason();
		String title = data.getTitle();
		_id = data.getId();
		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, minute);

		populateNewTest(amt, reason, title);

		isEdit = true;
	}

	private void deleteUsage() {
		UsageData data = childs.get(parents.get(groupPosition).getTitle()).get(
				childPosition);
		int id = data.getId();
		boolean check = usageHandler.deleteWithId(id);
		if (check) {
			Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					populateUI();
				}
			}, 400);

		} else {
			Toast.makeText(this, "*Can\'t delete", Toast.LENGTH_SHORT).show();
		}

	}

	private void showHideGridView() {
		if (!isShownGrid) {
			gridView.startAnimation(slideUP);
			gridView.setVisibility(View.VISIBLE);
			isShownGrid = true;
			showGrid.setText(hide);
		} else {
			gridView.startAnimation(slideDown);
			gridView.setVisibility(View.GONE);
			isShownGrid = false;
			showGrid.setText(show);
		}
	}

	private void showMyDialog(int id) {

		new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				String text = String.format("%1$02d:%2$02d",
						calendar.get(Calendar.HOUR),
						calendar.get(Calendar.MINUTE));
				time.setText(text);

			}
		}, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false)
				.show();
	}

	@Override
	public void onBackPressed() {
		if (isShownGrid) {
			showHideGridView();
		} else {
			super.onBackPressed();
			overridePendingTransition(R.anim.fade_in, R.anim.slide_down_true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.edit_menu, menu);

		return true;
	}

	/**/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_ok:
			saveUsage();

			break;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			overridePendingTransition(R.anim.fade_in, R.anim.slide_down_true);
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveUsage() {
		String titleStr = imageTitle.getText().toString();
		String reasonStr = reason.getText().toString();
		String amtStr = amt.getText().toString();

		boolean isSaved = false;
		if (!titleStr.equals("") && !amtStr.equals("")) {
			int _amt = Integer.valueOf(amtStr);
			if (!isEdit) {
				isSaved = usageHandler.saveUsage(titleStr, reasonStr, _amt,
						calendar) > 0;
			} else {
				UsageData data = new UsageData();
				data.setTitle(titleStr);
				data.setReason(reasonStr);
				data.setAmount(_amt);
				data.setHour(calendar.get(Calendar.HOUR));
				data.setMinute(calendar.get(Calendar.MINUTE));
				data.setDay(calendar.get(Calendar.DAY_OF_MONTH));
				data.setMonth(calendar.get(Calendar.MONTH));
				data.setYear(calendar.get(Calendar.YEAR));
				data.setMilliSecond(calendar.getTimeInMillis());
				isSaved = usageHandler.updateUsage("id = " + _id, data);
				isEdit = false;
			}
		}

		if (isSaved) {
			newContaner.startAnimation(zoom);
			imageView.setImageResource(R.drawable.question_mark);
			;
			imageTitle.setText("");
			amt.setText("");
			reason.setText("");
			Toast.makeText(this, "Data has been saved", Toast.LENGTH_SHORT)
					.show();
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					populateUI();

				}
			}, 1000);

		} else {

			if (titleStr.equals("")) {
				imageView.startAnimation(zoom);
			}
			if (amtStr.equals("")) {
				amt.startAnimation(zoom);
				amt.requestFocus();
			}
			Toast.makeText(this, "Data cannot be saved", Toast.LENGTH_LONG)
					.show();
		}

	}
}
