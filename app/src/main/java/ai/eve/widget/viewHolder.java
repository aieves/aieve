package ai.eve.widget;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wyong on 2016/5/25.
 */
public class viewHolder {
    private SparseArray<View> views;

    private int mPosition;
    private View mConvertView;

    public viewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
        this.mPosition = position;
        this.views = new SparseArray<View>();
    }

    public static viewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new viewHolder(context, parent, layoutId, position);
        } else {
            viewHolder holder = (viewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public <T extends View> T getView(int viewId){
        View view= views.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (T)view;
    }
    public View getConvertView(){
        return mConvertView;
    }
}
