package com.tool.phoneutils.cview;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tool.phoneutils.R;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wlhuang on 11/08/2016.
 */
public class MarqueeView extends RecyclerView {
    Thread thread = null;
    AtomicBoolean shouldContinue = new AtomicBoolean(false);
    Handler mHandler;
    private static float mDensity;
    private static int curSelected = 3;

    public MarqueeView(Context context) {
        super(context);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        mDensity  = Resources.getSystem().getDisplayMetrics().density;
        //主线程的handler，用于执行Marquee的滚动消息
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1://不论是竖直滚动还是水平滚动，都是偏移5个像素
                        MarqueeView.this.scrollBy(5, 5);
                        break;
                }
            }
        };
        if (thread == null) {
            thread = new Thread() {
                public void run() {
                    while (shouldContinue.get()) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg=mHandler.obtainMessage();
                        msg.what=1;
                        msg.sendToTarget();
                    }
                    //退出循环时清理handler
                    mHandler=null;
                }
            };
        }
    }

    @Override
    /**
     * 在附到窗口的时候开始滚动
     */
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        shouldContinue.set(true);
        init();
//        thread.start();
    }

    @Override
    /**
     * 在脱离窗口时处理相关内容
     */
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopMarquee();
    }

    /**
     * 停止滚动
     */
    public void stopMarquee() {
        shouldContinue.set(false);
        thread = null;
    }
    /**
     * Adapter类
     */
    public static class InnerAdapter extends Adapter<InnerAdapter.MarqueHolder> {
        private static final String TAG = InnerAdapter.class.getName();
        private List<String> mData;
        private int size;
        private LayoutInflater mLayoutInflater;
        public InnerAdapter(List<String> data,Context context) {
            mData = data;
            size=mData.size();
            mLayoutInflater=LayoutInflater.from(context);
        }

        @Override
        public MarqueHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view=mLayoutInflater.inflate(R.layout.marque_item,parent,false);

            return new MarqueHolder(view);
        }

        @Override
        public void onBindViewHolder(MarqueHolder holder, int position) {
            holder.tv.setText(mData.get(position%size));
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
        /**
         ** ViewHolder类
         **/
        static class MarqueHolder extends RecyclerView.ViewHolder {
            TextView tv;

            public MarqueHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.tv);
            }

        }
    }

}
