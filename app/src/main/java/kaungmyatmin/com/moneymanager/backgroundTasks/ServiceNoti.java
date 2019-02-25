package kaungmyatmin.com.moneymanager.backgroundTasks;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import kaungmyatmin.com.moneymanager.DB.ValHolder;
import kaungmyatmin.com.moneymanager.R;
import kaungmyatmin.com.moneymanager.presentor.MainActivity;


public class ServiceNoti extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		long lastCheck = pref.getLong(ValHolder.KEY_LATEST_CHECK, 5400000L);
		long now = System.currentTimeMillis();
		long diff = now - lastCheck;
		if (diff >= 5400000) {
			showNoti();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void showNoti() {
		Intent intent = new Intent(this,
				MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);	
		Notification notification = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher_foreground)
				.setContentTitle("HI")
				.setContentText("Need to save some Usages?")
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
				.setAutoCancel(true)
				.setContentIntent(pIntent).build();
		NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notiManager.notify(89756421, notification);

	}
}
