package kaungmyatmin.com.moneymanager.backgroundTasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverNoti extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, ServiceNoti.class);
		context.startService(service);
		
		
	}

}
