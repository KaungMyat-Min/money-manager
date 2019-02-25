package kaungmyatmin.com.moneymanager.backgroundTasks;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MobileService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
   //save MObile data 
	return super.onStartCommand(intent, flags, startId);
}
}
