package kaungmyatmin.com.moneymanager.custom;

import com.trio.moneymanager.FragIndividualUsage;
import com.trio.moneymanager.FragUsage;
import com.trio.moneymanager.R;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	private Context context;
	private Activity activity;
	private String [] titles = null;
	public ViewPagerAdapter(FragmentManager fm, Context context,
			Activity activity) {
		super(fm);
		this.context = context;
		this.activity = activity;
		titles = context.getResources().getStringArray(R.array.tab_names);

	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new FragUsage(context);
		case 1:
			return new FragIndividualUsage(context, activity);
		
		default:
			return null;

		}

	}

	@Override
	public CharSequence getPageTitle(int position) {
	
		return titles[position];
	}
	@Override
	public int getCount() {

		return 2;
	}

}
