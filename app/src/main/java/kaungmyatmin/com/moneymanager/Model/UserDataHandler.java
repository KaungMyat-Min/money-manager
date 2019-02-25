package kaungmyatmin.com.moneymanager.Model;

import java.util.Calendar;

import com.trio.moneymanager.DB.ValHolder;
import com.trio.moneymanager.POJO.UserData;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class UserDataHandler {
	private Context context;

	public UserDataHandler(Context context) {
		this.context = context;

	}

	public void saveUserProfile(UserData data) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor prefEditor = pref.edit();
		prefEditor.putString(ValHolder.KEY_USERNAME, data.getUserName());
		prefEditor.putFloat(ValHolder.KEY_SALARY, data.getSalary());
		prefEditor.putFloat(ValHolder.KEY_SAVE_AMT, data.getSaveAmt());
		prefEditor.putFloat(ValHolder.KEY_COST_CLOTH, data.getCost_cloth());
		prefEditor.putFloat(ValHolder.KEY_COST_INTERNET,
				data.getCost_internet());
		prefEditor.putFloat(ValHolder.KEY_COST_SMS, data.getCost_sms());
		prefEditor.putFloat(ValHolder.KEY_COST_TALK, data.getCost_talk());
		prefEditor.putFloat(ValHolder.KEY_COST_TRANSPORT,
				data.getCost_transport());
		prefEditor.putBoolean(ValHolder.KEY_IS_DAILY, data.isDaily());

		String[] additionalCostTitles = data.getAddition_cost_titiles();
		if (additionalCostTitles != null) {
			float[] additionalCostValues = data.getAddition_cost_values();
			int i;
			for (i = 0; i < additionalCostTitles.length; i++) {
				prefEditor.putString(ValHolder.TITLE + i,
						additionalCostTitles[0]);
				prefEditor.putFloat(ValHolder.VALUE + i,
						additionalCostValues[0]);
			}
			prefEditor.putInt(ValHolder.KEY_ADDITIONAL_COST_SIZE, i);
		}
		prefEditor.apply();

	}

	public UserData getUserProfile() {
		UserData data = new UserData();
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		data.setUserName(pref.getString(ValHolder.KEY_USERNAME, "PhiDoRa"));
		data.setSalary(pref.getFloat(ValHolder.KEY_SALARY, 0.0f));
		data.setSaveAmt(pref.getFloat(ValHolder.KEY_SAVE_AMT, 0.0f));
		data.setCost_cloth(pref.getFloat(ValHolder.KEY_COST_CLOTH, 0.0f));
		data.setCost_internet(pref.getFloat(ValHolder.KEY_COST_INTERNET, 0.0f));
		data.setCost_sms(pref.getFloat(ValHolder.KEY_COST_SMS, 0.0f));
		data.setCost_talk(pref.getFloat(ValHolder.KEY_COST_TALK, 0f));
		data.setCost_transport(pref
				.getFloat(ValHolder.KEY_COST_TRANSPORT, 0.0f));
		data.setDaily(pref.getBoolean(ValHolder.KEY_IS_DAILY, false));
		int size = pref.getInt(ValHolder.KEY_ADDITIONAL_COST_SIZE, 0);
		if (size != 0) {
			String titles[] = new String[size - 1];
			float values[] = new float[size - 1];
			for (int i = 0; i < titles.length; i++) {
				titles[i] = pref.getString(ValHolder.TITLE + i, null);
				values[i] = pref.getFloat(ValHolder.VALUE + i, 0.0f);
			}
			data.setAddition_cost_titiles(titles);
			data.setAddition_cost_values(values);
		}
		return data;
	}

	public float getMinSaveable(float salary, float houseRent, float costCloth,
			float costTransport, boolean isDaily) {
		float saveAmt;
		float remainIncome;
		if (!isDaily) {
			remainIncome = salary
					- (houseRent + costCloth + costTransport * 365 / 12);
		} else {
			remainIncome = salary
					- (houseRent * 12 / 365 + costCloth * 12 / 365 + costTransport);
		}

		if (remainIncome > salary / 2) {
			saveAmt = salary / 3;
		} else {
			saveAmt = remainIncome;
		}
		return saveAmt;
	}

	public float getDailySaveAmt(float saveAmt, float... costs) {
		float needToSave = saveAmt * 12 / 365;
		for (float cost : costs) {
			needToSave += cost;
		}
		return needToSave;
	}

	public float convertToDaily(float value) {
		return value * 12 / 365;
	}

	public int getDailyMaxUse() {
		int max = 0;
		Calendar calendar = Calendar.getInstance();

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		int daily = pref.getInt(ValHolder.KEY_MAX_USE_PER_DAY, 2000);
		int tDaily = daily * calendar.get(Calendar.DAY_OF_MONTH);
		int tUse = new UsageHandler(context, ValHolder.TABLE_MONEY_USAGE)
				.getTotalUsageForMonth(calendar.get(Calendar.MONTH),
						calendar.get(Calendar.YEAR));
		max = tDaily - tUse;
		return max;
	}

	public Object getUserData(String key, String type) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		if (type.equals("String")) {
			return pref.getString(key, null);
		} else if (type.equals("float")) {
			return pref.getFloat(key, 0.0f);
		} else if (type.equals("long") || type.equals("Long")) {
			return pref.getLong(key, 0L);
		} else {
			return null;
		}

	}

}
