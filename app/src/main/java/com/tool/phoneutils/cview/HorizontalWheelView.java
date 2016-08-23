package com.tool.phoneutils.cview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import com.tool.phoneutils.R;

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
    public static final int PADDING = 10;
    private int showCount = SHOW_COUNT;
    private int selectedRange = SELECTED_RANGE;
    private int space = X_DISTANCE;
    private Paint mTextPaint = null;
    private int itemHeight;
    private Context context;
    private float mDensity;
    private Scroller mScroller = null;
    private int mLastX = 0;
    private int mMove = 0;
    private int xPosition = 0;
    private Bitmap pointBitmap = null;
    private int pointBitmapHeight = 0;
    private int padding = PADDING;
    private int widthSize;
    private int heightSize;
    private int[] mColor = new int[]{Color.GREEN,Color.BLUE,Color.RED};
    private Paint p;

    class WheelItem{
        TextView textView;
        String text;
        int X;
        int height;
        Rect bounds;
        int color;
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
//        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 得到每一个Item的高度
        Paint mPaint = new Paint();
        mPaint.setTextSize(TEXT_SIZE * mDensity);
        Paint.FontMetrics metrics =  mPaint.getFontMetrics();
        itemHeight = (int) (metrics.bottom - metrics.top) + 2 * padding;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY));
    }

    void init(Context context){
        this.context = context;
        mScroller = new Scroller(context);
        DisplayMetrics dm =getResources().getDisplayMetrics();
        mDensity = dm.density;
        selectedRange = dm.widthPixels /3;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(TEXT_SIZE * mDensity);

        pointBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.arrow_icon);
    }

    public void setTexts(ArrayList<String> texts) {
        wheelItems = new ArrayList<>();
        space = 0;
        wheelItems.clear();
        for (int i = 0; i < texts.size() && i < showCount + 1; i++) {
            WheelItem wheelItem = new WheelItem();
            wheelItem.text = texts.get(i);
            wheelItem.textView = new TextView(context);
            Rect bounds = new Rect();
            mTextPaint.getTextBounds(wheelItem.text, 0, wheelItem.text.length(), bounds);
            wheelItem.bounds = bounds;
            Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
            wheelItem.X = space;
            wheelItem.color = mColor[i%3];
            space += X_DISTANCE * 2;//添加间隔
            wheelItem.height = fontMetrics.bottom - fontMetrics.top;
            heightSize = fontMetrics.bottom - fontMetrics.top + 10 * 2;
            wheelItems.add(wheelItem);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (wheelItems!=null){
//            canvas.drawBitmap(pointBitmap,selectedRange - mScroller.getCurrX(),pointBitmapHeight,null);
            for (WheelItem wheelItem : wheelItems) {
                drawText(canvas,wheelItem);
            }
            widthSize = canvas.getWidth();
            Log.i(TAG, "onDraw: mScroll = "+mScroller.getCurrX());
        }
    }

    void drawText(Canvas canvas,WheelItem wheelItem){
        Log.i(TAG, "drawText: selectedRange="+selectedRange);
        if (wheelItem.X - (X_DISTANCE/2) - mLastX < selectedRange && wheelItem.X + (X_DISTANCE/2) - mLastX > selectedRange){
            mTextPaint.setColor(Color.GREEN);
//            Log.d(TAG, "drawText: text="+wheelItem.text);
        }else {
            mTextPaint.setColor(Color.WHITE);
        }
        canvas.drawText(wheelItem.text,wheelItem.X,pointBitmapHeight+wheelItem.height,mTextPaint);
    }
}
