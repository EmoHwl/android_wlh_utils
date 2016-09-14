package com.tool.phoneutils.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.meetme.android.horizontallistview.sample.MainHorizontalActivity;
import com.tool.phoneutils.R;
import com.tool.phoneutils.cview.HorizontalWheelView;
import com.tool.phoneutils.cview.ObservableScrollView;
import com.tool.phoneutils.cview.ScrollSelectedView;
import com.tool.phoneutils.utils.AppInfoUtil;
import com.tool.phoneutils.utils.L;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    @BindView(R.id.show_snack_bar)
    Button showSnackBar;
    @BindView(R.id.medium)
    Button mediumBtn;
    @BindView(R.id.horizontal)
    Button horizontal;
    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.observable_scroll_view)
    ObservableScrollView observableScrollView;
    ArrayList<TextView> textViews = new ArrayList<>();
    @BindView(R.id.horizontal_wheel_view)
    HorizontalWheelView horizontalWheelView;
    @BindView(R.id.scroll_selected_view)
    ScrollSelectedView scrollSelectedView;
    @BindView(R.id.download_btn)
    Button downloadBtn;
    private int curSelected = -1;
    private int mScrollValue = 0;
    private float mDensityWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String[] s = getCpuInfo();
        String deviceInfo = getInfo() + "\n" + getMacAddress() + "\n";
        for (int i = 0; i < s.length; i++) {
            deviceInfo += "cpu " + i + " : " + s[i] + "\n";
        }
        L.d("isRoot" + AppInfoUtil.exeCmdWithRoot("ls"));
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpHelper.postString();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        showSnackBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TSnackbar mTSnackbar = TSnackbar.make(findViewById(android.R.id.content), "Hello from TSnackBar.", TSnackbar.LENGTH_LONG);
                View snackbarView = mTSnackbar.getView();
                mTSnackbar.setActionTextColor(Color.WHITE);
                mTSnackbar.setIconLeft(R.mipmap.ic_launcher, 24);
                snackbarView.setBackgroundColor(Color.parseColor("#CC00CC"));
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                mTSnackbar.show();
            }
        });

        mDensityWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        horizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CycleActivity.class);
                startActivity(intent);
            }
        });
//        getHostName("http://192.168.1.254/FW9660A.bin");

//        horizontal.post(new Runnable() {
//            @Override
//            public void run() {
        addInitData();
        addData();
//            }
//        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,DownloaderActivity.class);
                startActivity(intent);
            }
        });

    }

    public void addData() {
        ArrayList<String> texts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String s = "test num " + i;
            texts.add(s);
        }

        scrollSelectedView.addChildren(texts);
    }

    public void addInitData() {
        for (int i = 0; i < 10; i++) {
            View layout = getLayoutInflater().inflate(R.layout.marque_item, null);
            TextView tv = (TextView) layout.findViewById(R.id.tv);
            int[] location = new int[2];
            tv.getLocationOnScreen(location);
            L.i("addInitData: tv=" + layout.getWidth() + "|" + location[1]);
            tv.setText("param num " + i);
            textViews.add(tv);
            linearLayout.addView(layout);
        }
        observableScrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//                Log.i("onScrollChanged", "onScrollChanged: x="+ x+ " | y="+y+" | oldx="+oldx+" | oldy="+oldy);
//                Log.i("onScrollChanged", "onScrollChanged: "+" linearLayout.getChildAt(2).getX()="+linearLayout.getChildAt(2).getX()+" observableScrollView.getScrollX()="+observableScrollView.getScrollX());
                for (int i = 0; i < textViews.size(); i++) {
                    TextView textView = textViews.get(i);
                    float poiX = linearLayout.getChildAt(i).getX();
                    float LEFT = (poiX - observableScrollView.getScrollX());
                    float RIGHT = (poiX + textView.getWidth() - observableScrollView.getX());
                    Log.i(TAG, "poiX = " + poiX);
                    Log.i(TAG, "textView.getWidth()=" + textView.getWidth());
                    Log.i(TAG, "mDensityWidth = " + mDensityWidth);
                    Log.i(TAG, "LEFT = " + LEFT);
                    Log.i(TAG, "RIGHT = " + RIGHT);
                    Log.i(TAG, "\n");
                    if (LEFT < mDensityWidth && RIGHT > mDensityWidth) {
                        textView.setTextColor(Color.GREEN);
                    } else {
                        textView.setTextColor(Color.WHITE);
                    }
                }
            }
        });
//        observableScrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                int x,y;
//                int dx = 0,dy = 0;
//                switch (motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        x = (int) motionEvent.getX();
//                        y = (int) motionEvent.getY();
//                        Log.i(TAG,"x"+x+" | y"+y);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        dx = (int) motionEvent.getX();
//                        dy = (int) motionEvent.getY();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        Log.i(TAG,"dx"+dx+" | dy"+dy);
//                        break;
//                }
//                return true;
//            }
//        });
    }

    public static void SnackbarAddView(Snackbar snackbar, int layoutId, int index) {
        View v = snackbar.getView();//获取snackbar的View(其实就是SnackbarLayout)
        ViewGroup.LayoutParams vl = v.getLayoutParams();
        CoordinatorLayout.LayoutParams cl = new CoordinatorLayout.LayoutParams(vl.width, vl.height);
        cl.gravity = Gravity.TOP;
        v.setBackgroundColor(Color.GREEN);
        v.setLayoutParams(cl);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) v;//将获取的View转换成SnackbarLayout
        View add_view = LayoutInflater.from(v.getContext()).inflate(layoutId, null);//加载布局文件新建View
        snackbarLayout.addView(add_view, index);//将新建布局添加进snackbarLayout相应位置
    }

    public void showCusToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.warning_layout, null);
        TextView content = (TextView) layout.findViewById(R.id.toast_content);
        Toast toast = new Toast(MainActivity.this);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void showFromTop() {

    }

    public static String getHostName(String srcUrl) {
        String temp = srcUrl.replace("//", "#");
        String[] splitStr = temp.split("/");
        srcUrl = splitStr[0];
        srcUrl = srcUrl.replace("#", "//");
        return srcUrl;
    }

    /**
     * 获取IMEI号，IESI号，手机型号
     */
    private String getInfo() {
        TelephonyManager mTm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = Build.MODEL; // 手机型号
        String mtyb = Build.BRAND;//手机品牌
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        String s = "手机IMEI号：" + imei + "\n手机IESI号：" + imsi + "\n手机型号：" + mtype + "\n手机品牌：" + mtyb + "\n手机号码：" + numer + "\n" + "分辨率：" + getHeightAndWidth() + "\n";
        String memory = "可用内存：" + getAvailMemory() + "\n" + "总共内存：" + getTotalMemory();
        String abi = "";
        String[] abis;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            abis = Build.SUPPORTED_ABIS;
        } else {
            abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }


        for (int i = 0; i < abis.length; i++) {
            abi += abis[i] + "    ";
        }
        abi += "\n";
        Log.i("text", "Build.SUPPORTED_ABIS = " + abi.toString() + s + memory);
        return "Build.SUPPORTED_ABIS = " + abi.toString() + s + memory;
    }

    /**
     * .获取手机MAC地址
     * 只有手机开启wifi才能获取到mac地址
     */
    private String getMacAddress() {
        String result = "";
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        Log.i("text", "手机macAdd:" + result);
        return result;
    }

    /**
     * 手机CPU信息
     */
    private String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        Log.i("text", "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        return cpuInfo;
    }


    /**
     * 获取android当前可用内存大小
     */
    private String getAvailMemory() {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化
    }

    /**
     * 获得系统总内存
     */
    private String getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 获得手机屏幕宽高
     *
     * @return
     */
    public String getHeightAndWidth() {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        String str = width + " * " + height + "";
        return str;
    }
}
