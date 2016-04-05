package ai.eve.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 自定义标题栏(左中右)
 *
 * @author Eve-Wyong
 * @version 2015-2-21 下午5:53:55
 * @Copyright 2014 EVE. All rights reserved.
 */
public class EToolBar extends Toolbar {
    private Context context;

    //设置中间文字颜色
    private int titleTextColor = 0xFF000000;
    //设置中间文字大小
    private int titleTextSize = 17;
    //title背景色（状态栏颜色设置与title背景色保持一致）
    private  int titleBackgroudColor = 0xFF7AC5CD;
    private int statusBarColor = -1;

    public EToolBar(Context context) {
        super(context);
        this.context = context;
        initTooBar(context);
    }

    public EToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initTooBar(context);
    }

    public EToolBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initTooBar(context);
    }

    public void initTooBar(final Context context) {
        //设置toolbar背景颜色
        setBackgroundColor(titleBackgroudColor);
        if (Build.VERSION.SDK_INT >= 21) {
            ((Activity) context).getWindow().setStatusBarColor(titleBackgroudColor);
        }
        ((ActionBarActivity) context).setSupportActionBar(this);
        setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               ((Activity)context).onBackPressed();
            }
        });
    }



    private TextView title;

    public TextView getTitleTextView() {
        return title;
    }

    /**
     * 默认标题栏中间为TextView，通过此方法设置文字
     *
     * @param titleCenter
     */
    public void setCenterTitle(String titleCenter) {
        if(title==null){
            title = new TextView(context);
            title.setSingleLine(true);
            title.setTextSize(titleTextSize);
            title.setTextColor(titleTextColor);
            LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            p.gravity = Gravity.CENTER;
            this.addView(title, p);
        }
        title.setText(titleCenter);
    }

    /**
     * 默认标题栏中间为TextView，通过此方法设置文字
     *
     * @param titleCenter
     * @param color
     * @param size
     */
    public void setCenterTitle(String titleCenter, int color, int size) {
        if(title==null){
            title = new TextView(context);
            title.setSingleLine(true);
            title.setGravity(Gravity.CENTER);
            LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            p.gravity = Gravity.CENTER;
            addView(title, p);
        }
        title.setTextSize(size);
        title.setText(titleCenter);
        title.setTextColor(color);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        ((ActionBarActivity) context).setSupportActionBar(this);
        setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).onBackPressed();
            }
        });
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= 21) {
            if(statusBarColor==-1){
                ((Activity) context).getWindow().setStatusBarColor(color);
            }else{
                ((Activity) context).getWindow().setStatusBarColor(statusBarColor);
            }
        }
    }

    public void setStatusBarColor(int statusBarColor) {
        this.statusBarColor = statusBarColor;
    }
}
