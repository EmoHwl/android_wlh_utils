package com.tool.phoneutils.cview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tool.phoneutils.R;

import java.util.ArrayList;

/**
 * Created by wlhuang on 09/08/2016.
 */
public class ParamSelectedView extends LinearLayout {

    ImageView arrowIcon;
    HorizontalWheelView horizontalWheelView;

    public ParamSelectedView(Context context) {
        super(context);
        initial(context);
    }

    public ParamSelectedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial(context);
    }

    public ParamSelectedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    void initial(Context context) {
        LayoutInflater.from(context).inflate(R.layout.param_selected_layout, this,true);
        horizontalWheelView = (HorizontalWheelView) findViewById(R.id.horizontal_wheel_view);
        arrowIcon = (ImageView) findViewById(R.id.arrow_icon);
        ArrayList<String> textList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            textList.add("test num "+i);
        }

        horizontalWheelView.setTexts(textList);
    }

}
