package kaungmyatmin.com.moneymanager.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;



public class CustomButton extends AppCompatButton {
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
        super(context,attrs,defStyleAttr);
        init();
		}

	private void init(){
	
		Typeface mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SmartZawgyi_v3.ttf");
		setTypeface(mTypeface);	
	}
}
