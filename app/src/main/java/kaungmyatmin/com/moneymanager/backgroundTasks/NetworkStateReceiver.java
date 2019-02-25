package kaungmyatmin.com.moneymanager.backgroundTasks;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkStateReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		/*MobileDataHandler mobileDataHandler = new MobileDataHandler(context);
		MobileData data = mobileDataHandler.getInternetUsage();
		Intent i = new Intent(context,com.trio.moneymanager.MainActivity.class);
		i.putExtra("tx", data.gettX());
		i.putExtra("rx", data.getrX());
		i.setAction("Net");
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		*/
	}

}
