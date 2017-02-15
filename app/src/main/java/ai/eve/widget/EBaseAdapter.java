package ai.eve.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import ai.eve.R;

/**
 * Created by wyong on 2016/5/25.
 */
public abstract class EBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mList;
    protected int mLayoutId;

    public EBaseAdapter(Context context, List<T> list,int layoutId) {
        this.mContext = context;
        this.mList = list;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = viewHolder.get(mContext, convertView, parent, mLayoutId, position);
        convert(holder,getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(viewHolder holder,T t);
}
