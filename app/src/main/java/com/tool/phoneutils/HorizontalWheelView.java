package com.tool.phoneutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wlhuang on 08/08/2016.
 */
public class HorizontalWheelView extends View{
    private static final String TAG = HorizontalWheelView.class.getName();
    private static final int SHOW_COUNT = 4;
    private static final float TEXT_SIZE = 15.0f;
    private static final int SELECTED_RANGE = 1000;
    private static final int X_DISTANCE = 200;
    private int showCount = SHOW_COUNT;
    private int selectedRange = SELECTED_RANGE;
    private int space = X_DISTANCE;
    private Paint mTextPaint = null;
    private Context context;
    private float mDensity;
    private Scroller mScroller = null;
    private int mLastX = 0;
    private int mMove = 0;
    private int xPosition = 0;
    private Bitmap pointBitmap = null;
    private int pointBitmapHeight = 50;
    private int widthSize;
    private int heightSize;

    class WheelItem{
        TextView textView;
        String text;
        int X;
        int height;
        Rect bounds;
    }
    private ArrayList<WheelItem> wheelItems;
    public HorizontalWheelView(Context context) {
        super(context);
        init(context);
    }

    public HorizontalWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizontalWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mMove = mLastX;
                xPosition = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = (int) event.getX();
                int deltaX = xPosition - offsetX;
                Log.i(TAG, "onTouchEvent: deltaX="+deltaX);
                Log.i(TAG, "onTouchEvent: widthSize="+widthSize);
                mMove = (mLastX + deltaX);
                changeValue(mMove);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastX = mMove;
                break;
        }

        return true;
    }

    void changeValue(int mMove){
        if (wheelItems != null){
//            Log.i(TAG, "changeValue: mMove="+mMove);
            smoothScrollTo(mMove);
        }
    }

    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx) {
        int dx = fx - mScroller.getFinalX();
        smoothScrollBy(dx);
    }

    //X轴移动
    public void smoothScrollBy(int dx){
        mScroller.startScroll(mScroller.getFinalX(),mScroller.getFinalY(),dx,mScroller.getCurrY(),8);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightSize);
    }

    void init(Context context){
        this.context = context;
        mScroller = new Scroller(context);
        DisplayMetrics dm =getResources().getDisplayMetrics();
        mDensity = dm.density;
        selectedRange = dm.widthPixels /2;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(TEXT_SIZE * mDensity);

        pointBitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.arrow_icon);
    }

    public void setTexts(ArrayList<String> texts) {
        wheelItems = new ArrayList<>();
        space = 0;
        for (int i = 0; i < texts.size(); i++) {
            WheelItem wheelItem = new WheelItem();
            wheelItem.text = texts.get(i);
            wheelItem.textView = new TextView(context);
            Rect bounds = new Rect();
            mTextPaint.getTextBounds(wheelItem.text, 0, wheelItem.text.length(), bounds);
            wheelItem.bounds = bounds;
            Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
            wheelItem.X = space;
            space += X_DISTANCE * 2;//添加间隔
            wheelItem.height = fontMetrics.bottom - fontMetrics.top;
            heightSize = fontMetrics.bottom - fontMetrics.top + 10 * 2;
            wheelItems.add(wheelItem);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (wheelItems!=null){
            canvas.drawBitmap(pointBitmap,selectedRange - mScroller.getCurrX(),pointBitmapHeight,null);
            for (WheelItem wheelItem : wheelItems) {
                drawText(canvas,wheelItem);
            }
            widthSize = canvas.getWidth();
        }
    }

    void drawText(Canvas canvas,WheelItem wheelItem){
        if (wheelItem.X - X_DISTANCE - mLastX < selectedRange && wheelItem.X + X_DISTANCE - mLastX > selectedRange){
            mTextPaint.setColor(Color.GREEN);
            Log.d(TAG, "drawText: text="+wheelItem.text);
        }else {
            mTextPaint.setColor(Color.WHITE);
        }
        canvas.drawText(wheelItem.text,wheelItem.X,pointBitmapHeight+wheelItem.height,mTextPaint);
    }
}
