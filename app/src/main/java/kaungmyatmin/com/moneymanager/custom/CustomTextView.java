package kaungmyatmin.com.moneymanager.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


import kaungmyatmin.com.moneymanager.R;


public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

    }


    private void init() {

        Typeface mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SmartZawgyi_v3.ttf");
        setTypeface(mTypeface);

    }


}
