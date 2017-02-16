package ai.eve.html.custom;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import ai.eve.R;
import ai.eve.html.webutil.model.Option;

//自定义Adapter,从checkMap中获取当前Item的状态 并设置
public class ChoiceAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Option> data;
	private int layout;

	public ChoiceAdapter(Context context, ArrayList<Option> data, int layout) {
		super();
		inflater = LayoutInflater.from(context);
		this.data = data;
		this.layout = layout;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(layout, null);
		}
		CheckedTextView checktv_title = (CheckedTextView) convertView
				.findViewById(R.id.text1);
		checktv_title.setText(data.get(position).text);
		if (data.get(position).selected) {
			checktv_title.setChecked(true);
		} else {
			checktv_title.setChecked(false);
		}

		return convertView;
	}

}