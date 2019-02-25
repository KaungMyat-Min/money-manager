package kaungmyatmin.com.moneymanager;

import java.util.Calendar;

import com.trio.moneymanager.DB.ValHolder;
import com.trio.moneymanager.custom.SlidingTabLayout;
import com.trio.moneymanager.custom.ViewPagerAdapter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	private Toolbar toolBar;
	private ViewPager viewPager;
	private SlidingTabLayout tabs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		toolBar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolBar);
		 //getSupportActionBar().setCustomView(R.layout.action_title);;
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getApplicationContext(), this);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(1);
		tabs = (SlidingTabLayout) findViewById(R.id.tabs);
		tabs.setDistributeEvenly(true);
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		}); 
			tabs.setViewPager(viewPager);
		
			
			
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor editor = pref.edit();
			editor.putLong(ValHolder.KEY_LATEST_CHECK, System.currentTimeMillis());
			
			boolean isAlarmSet = pref.getBoolean(ValHolder.KEY_IS_ALARM_SET, false);
			if(!isAlarmSet){
				setAlarm();
				editor.putBoolean(ValHolder.KEY_IS_ALARM_SET, true);
			}
			editor.apply();
			
	
	}
	
	private void showNoti() {
		Intent intent = new Intent(this,
				com.trio.moneymanager.MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);	
		Notification notification = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.noti)
				.setContentTitle("HI")
				.setContentText("Need to save some Usages?")
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
				.setAutoCancel(true)
				.setContentIntent(pIntent).build();
		NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notiManager.notify(89756421, notification);

	}
	

	private void setAlarm(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 21);
		calendar.set(Calendar.MINUTE, 45);
		
		Intent intent = new Intent(MainActivity.this, com.trio.moneymanager.backgroundTasks.ReceiverNoti.class);
		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 86400, pIntent);
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		return super.onOptionsItemSelected(item);
	}
}
