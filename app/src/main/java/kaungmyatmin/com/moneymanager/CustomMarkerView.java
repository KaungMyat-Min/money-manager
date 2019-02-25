package kaungmyatmin.com.moneymanager;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class CustomMarkerView extends MarkerView{

	TextView markerText;
	public static int LAST_INDEX = 150;
	private boolean isLast = false;
	public CustomMarkerView(Context context, int layoutResource) {
		super(context, layoutResource);
		
	}

	@Override
	public void refreshContent(Entry e, Highlight highlight) {
		markerText = (TextView) findViewById(R.id.marker_txt);
		int value = (int) e.getVal();
		markerText.setText(String.valueOf(value));
		if(LAST_INDEX != 0 && e.getXIndex() == LAST_INDEX){
			isLast = true;
		}
		else{
			isLast = false;
		}
		
		
	}

	@Override
	public int getXOffset(float xpos) {
		if(isLast){
			return -getWidth()-10;	
		}
		else{
			return getWidth()/5;
		}
		
	}

	@Override
	public int getYOffset(float ypos) {
		
		return -3;
	}

	
}
