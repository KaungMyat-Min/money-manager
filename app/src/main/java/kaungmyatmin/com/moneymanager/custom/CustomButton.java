package kaungmyatmin.com.moneymanager.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button{
	public CustomButton(Context context) {
		super(context);
		init();
	}

	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	

	public CustomButton(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
		}

	private void init(){
	
		Typeface mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SmartZawgyi_v3.ttf");
		setTypeface(mTypeface);	
	}
}
