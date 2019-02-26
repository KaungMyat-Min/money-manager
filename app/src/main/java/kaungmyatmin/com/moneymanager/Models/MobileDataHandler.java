package kaungmyatmin.com.moneymanager.Models;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.TrafficStats;
import android.net.Uri;
import android.provider.CallLog.Calls;

import kaungmyatmin.com.moneymanager.DB.ValHolder;
import kaungmyatmin.com.moneymanager.POJO.MobileData;

public class MobileDataHandler {

	private Context context;

	public MobileDataHandler(Context context) {
		this.context = context;
	}

	public MobileData getSmsUsage(Long fromMillisecond) {
		int noOfSms = 0;
		long latestChecked = 0L;
		// long data[] = new long[2];
		MobileData data = new MobileData();
		String body = null;
		String BODY = "body";
		String DATE = "date";

		String selection = "date > " + fromMillisecond;
		String sort = "date ASCE";
		Uri inboxUri = Uri.parse("content://sms/sent");
		String[] reqCols = new String[] { BODY, DATE };

		ContentResolver contentResolver = context.getContentResolver();
		Cursor c = contentResolver.query(inboxUri, reqCols, selection, null,
				null);
		if (c != null && c.moveToFirst()) {
			while (!c.isAfterLast()) {
				body = c.getString(c.getColumnIndex(BODY));
				boolean isUnicoded = isUnicoded(body);
				int size = 0;
				int length = body.replaceAll("[\\{\\}\\[\\]~^|\\\\]","//").length();
				if (isUnicoded == false) {
					if (length <= 160) {
						size = 1;
					} else {
						size = length / 153;
						if (length % 153 != 0) {
							size += 1;
						}
					}
				} else {
					if (length <= 70) {
						size = 1;
					} else {
						size = length / 63;
						if (length % 63 != 0) {
							size += 1;
						}
					}
				}
				if (c.isLast()) {
					latestChecked = c.getLong(c.getColumnIndex(DATE));
				}
				c.moveToNext();
				noOfSms += size;
			}
			c.close();
		}
		float costPerSms = (Float) (new UserDataHandler(context).getUserData(
				ValHolder.KEY_COST_SMS, "float"));
		float totalCost = noOfSms * costPerSms;
		data.setSmsCost(totalCost);
		data.setSms(noOfSms);
		data.setLastCheck(latestChecked);

		return data;
	}

	private boolean isUnicoded(String body) {
		for (char c : body.toCharArray()) {
			if (c > 255) {
				return true;
			}
		}
		return false;
	}

	public MobileData getTalkTime(Long fromMilliSecond) {
		MobileData data = new MobileData();
		int cost = 0;

		Uri uri = Uri.parse("content://call_log/calls");
		ContentResolver contentResolver = context.getContentResolver();
		String whereClause = Calls.DATE + " > " + fromMilliSecond
				+ " and " + Calls.TYPE + "=" + Calls.OUTGOING_TYPE;
		String sortClause = null;// CallLog.Calls.DATE + "AESC";
		Cursor c = contentResolver.query(uri, null, whereClause, null,
				sortClause);
		int totalDuration = 0;
		if (c != null && c.moveToFirst()) {
			float costPerMin = (Float) (new UserDataHandler(context)
					.getUserData(ValHolder.KEY_COST_TALK, "float"));
			while (!c.isAfterLast()) {
				// String callType = c.getString(c
				// .getColumnIndex(CallLog.Calls.TYPE));
				// int type = Integer.parseInt(callType);
				// if (type == CallLog.Calls.OUTGOING_TYPE) {
				String callDuration = c.getString(c
						.getColumnIndex(Calls.DURATION));
				int duration = Integer.parseInt(callDuration);
				totalDuration += duration;
				int sec = duration % 60;
				int min = (int) ((duration - sec) / 60);
				cost += sec * costPerMin / 60;
				cost += min * costPerMin;
				if (c.isLast()) {
					long latestChecked = Long.parseLong(c.getString(c
							.getColumnIndex(Calls.DATE)));
					data.setTalkCost(cost);
					data.setLastCheck(latestChecked);
					data.setTalkSec(totalDuration);
					// }
					c.moveToNext();
					// }else{
					// c.moveToNext();
				}

			}
		}
		c.close();
		return data;
	}

	public MobileData getInternetUsage() {
		MobileData data = new MobileData();
		long rxByte = TrafficStats.getMobileRxBytes();
		long txByte = TrafficStats.getMobileTxBytes();
		long rxMb = (long) rxByte / 1000;
		long txMb = (long) txByte / 1000;

		float costPerMb = (Float) new UserDataHandler(context).getUserData(
				ValHolder.KEY_COST_INTERNET, "float");
		float cost = (rxMb + txMb) * costPerMb;

		long lastCheck = System.currentTimeMillis();
		data.setLastCheck(lastCheck);
		data.setInternetCost(cost);
		data.setrX(rxMb);
		data.settX(txMb);
		return data;

	}
}
