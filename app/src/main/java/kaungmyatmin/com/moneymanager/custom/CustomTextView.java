package kaungmyatmin.com.moneymanager.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView{

	
	public CustomTextView(Context context) {
		super(context);
		init();
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	

	public CustomTextView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
		}

	private void init(){
	
		Typeface mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SmartZawgyi_v3.ttf");
		setTypeface(mTypeface);	

	}
	
	
	
	
}
