package kaungmyatmin.com.moneymanager.custom;

import java.util.ArrayList;
import com.trio.moneymanager.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGridAdaptor extends BaseAdapter {

	private ArrayList<String> titles;
	private ArrayList<String> drawables;
	private Context context;

	public CustomGridAdaptor(Context context, ArrayList<String> titles,
			ArrayList<String> drawables) {
		this.titles = titles;
		this.drawables = drawables;
		this.context = context;

	}

	@Override
	public int getCount() {
		return titles.size();
	}

	@Override
	public Object getItem(int position) {

		return titles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_grid, null);
		}
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.image);
			TextView title = (TextView) convertView.findViewById(R.id.title);

			imageView.setImageResource((context.getApplicationContext()
					.getResources().getIdentifier(drawables.get(position),
					"drawable", "com.trio.moneymanager")));
			title.setText(titles.get(position));

		
		return convertView;
	}

}
