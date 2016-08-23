package com.tool.phoneutils.cview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tool.phoneutils.utils.L;

import java.util.ArrayList;

/**
 * Created by wlhuang on 19/08/2016.
 */
public class ScrollSelectedView extends HorizontalScrollView{
    private static final String TAG = ScrollSelectedView.class.getSimpleName();
    private static final int PADDING = 20;
    private static final int SPACE = 100;
    private boolean isLandscape;
    private int padding = PADDING;
    private int space = SPACE;
    private ArrayList<ParamsItem> paramsItems = new ArrayList<>();
    private LinearLayout linearLayout;
    private int widthRange;
    private int widthSpace;
    private int curSelected = -1;
    private Paint paint;

    static class ParamsItem{
        TextView textView;
        String string;
        int minPoi;//最小范围
        int maxPoi;//最大范围
        boolean isSelected;
        int widthRange = maxPoi - minPoi;

        @Override
        public String toString() {
            return "string="+string + "|minPoi =" +minPoi+ "|maxPoi =" +maxPoi;
        }
    }

    public ScrollSelectedView(Context context) {
        super(context);
        init(context);
    }

    public ScrollSelectedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollSelectedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context){
        DisplayMetrics dm =getResources().getDisplayMetrics();
        widthRange = dm.widthPixels;

        paint  = new Paint();

        //去锯齿
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
    }

    public ScrollSelectedView addChildren(ArrayList<String> stringArr){
        if (stringArr == null){
            new Exception("stringArr can't null");
            return this;
        }
        if (linearLayout == null){
            linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }
        if (!stringArr.isEmpty()){
            linearLayout.removeAllViews();
            paramsItems.clear();
            linearLayout.addView(getSpace());
            ParamsItem paramsItem;
            int total = widthSpace;
            for (int i = 0; i < stringArr.size(); i++) {
                paramsItem = new ParamsItem();
                String content = stringArr.get(i);
                L.i(content);
                TextView child = getTextView(content);
                if (i%2 == 0){
                    child.setBackgroundColor(Color.RED);
                }else{
                    child.setBackgroundColor(Color.GRAY);
                }
                Paint paint = new Paint();
                int textWidth = (int) paint.measureText(content);
                paramsItem.textView = child;
                paramsItem.string  = content;
                paramsItem.widthRange = textWidth + space * 2;
                L.i("paramsItem.widthRange = "+paramsItem.widthRange);
                paramsItem.minPoi = total;
                paramsItem.maxPoi = total+paramsItem.widthRange;
                if (paramsItem.minPoi < (widthRange /2) && paramsItem.maxPoi > (widthRange /2)){
                    curSelected = i;
                    child.setTextColor(Color.GREEN);
                }

                total += textWidth+space*2;
                L.i(paramsItem.toString()+ "|total="+total);
                paramsItems.add(paramsItem);
                linearLayout.addView(child);
                linearLayout.addView(getLine());
            }
            linearLayout.addView(getSpace());
            removeAllViews();
            addView(linearLayout);
        }
        return this;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        int temp = curSelected;
        for (int i = 0; i < paramsItems.size(); i++) {
            ParamsItem paramsItem = paramsItems.get(i);
            paramsItem.textView.setTextColor(Color.WHITE);
            int[] location = new int[2];
            paramsItem.textView.getLocationOnScreen(location);
            int minRange = widthRange / 2 - paramsItem.widthRange / 2;
            int maxRange = widthRange / 2 + paramsItem.widthRange / 2;
            L.i(i+"= "+location[0]+" |"+paramsItem.widthRange / 2);
            if (maxRange > location[0]&&minRange <= location[0]){
                paramsItem.textView.setTextColor(Color.GREEN);
            }
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(widthRange/2 - 130,0,widthRange + 130,padding *2,paint);
    }

    private View getSpace(){
        View v = new View(getContext());
        widthSpace = widthRange / 2 - space * 2;
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(widthSpace,10);
        v.setLayoutParams(mLayoutParams);
        return v;
    }

    private View getLine(){
        View v = new View(getContext());
        widthSpace = widthRange / 2 - space * 2;
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(1,20);
        v.setBackgroundColor(Color.BLUE);
        v.setLayoutParams(mLayoutParams);
        return v;
    }

    private TextView getTextView(String content){
        TextView textView = new TextView(getContext());
        textView.setTextColor(Color.WHITE);
        textView.setPadding(space,padding,space,padding);
        textView.setText(content);
        return textView;
    }

}
