package com.tool.phoneutils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.tool.phoneutils.Utils.AppInfoUtil;
import com.tool.phoneutils.Utils.L;
import com.tool.phoneutils.okhttp3.OkHttpHelper;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.test)
    TextView test;

    @BindView(R.id.show_snack_bar)
    Button showSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        test = (TextView) findViewById(R.id.test);
        String[] s = getCpuInfo();
        String deviceInfo =getInfo()+"\n"+getMacAddress()+"\n";
        for (int i = 0; i < s.length; i++) {
            deviceInfo += "cpu "+i+" : "+s[i]+"\n";
        }
        test.setText(deviceInfo);
        L.d("isRoot"+ AppInfoUtil.exeCmdWithRoot("ls"));
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
                TSnackbar mTSnackbar = TSnackbar.make(findViewById(android.R.id.content),"Hello from TSnackBar.", TSnackbar.LENGTH_LONG);
                View snackbarView = mTSnackbar.getView();
                mTSnackbar.setActionTextColor(Color.WHITE);
                mTSnackbar.setIconLeft(R.mipmap.ic_launcher, 24);
                snackbarView.setBackgroundColor(Color.parseColor("#CC00CC"));
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mTSnackbar.show();
            }
        });

        getHostName("http://192.168.1.254/FW9660A.bin");
    }
    public static void SnackbarAddView(Snackbar snackbar,int layoutId,int index) {
        View v = snackbar.getView();//获取snackbar的View(其实就是SnackbarLayout)
        ViewGroup.LayoutParams vl = v.getLayoutParams();
        CoordinatorLayout.LayoutParams cl = new CoordinatorLayout.LayoutParams(vl.width,vl.height);
        cl.gravity = Gravity.TOP;
        v.setBackgroundColor(Color.GREEN);
        v.setLayoutParams(cl);
        Snackbar.SnackbarLayout snackbarLayout=(Snackbar.SnackbarLayout)v;//将获取的View转换成SnackbarLayout
        View add_view = LayoutInflater.from(v.getContext()).inflate(layoutId,null);//加载布局文件新建View
        snackbarLayout.addView(add_view,index);//将新建布局添加进snackbarLayout相应位置
    }

    public void showCusToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.warning_layout,null);
        TextView content = (TextView) layout.findViewById(R.id.toast_content);
        Toast toast = new Toast(MainActivity.this);
        toast.setGravity(Gravity.TOP,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void showFromTop(){

    }

    public static String getHostName(String srcUrl){
        String temp = srcUrl.replace("//","#");
        String[] splitStr = temp.split("/");
        srcUrl = splitStr[0];
        srcUrl = srcUrl.replace("#","//");
        return srcUrl;
    }

    /**
     * 获取IMEI号，IESI号，手机型号
     */
    private String getInfo() {
        TelephonyManager mTm = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = android.os.Build.MODEL; // 手机型号
        String mtyb= android.os.Build.BRAND;//手机品牌
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        String s= "手机IMEI号："+imei+"\n手机IESI号："+imsi+"\n手机型号："+mtype+"\n手机品牌："+mtyb+"\n手机号码："+numer+"\n"+"分辨率："+getHeightAndWidth()+"\n";
        String memory = "可用内存："+getAvailMemory()+"\n" + "总共内存："+getTotalMemory();
        String abi = "";
        String[] abis;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ){
            abis = Build.SUPPORTED_ABIS;
        }else{
            abis = new String[]{Build.CPU_ABI,Build.CPU_ABI2};
        }


        for (int i = 0; i < abis.length; i++) {
            abi += abis[i]+"    ";
        }
        abi += "\n";
        Log.i("text", "Build.SUPPORTED_ABIS = "+abi.toString()+s+memory);
        return  "Build.SUPPORTED_ABIS = "+abi.toString()+s+memory;
    }
    /**
     * .获取手机MAC地址
     * 只有手机开启wifi才能获取到mac地址
     */
    private String getMacAddress(){
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
     * @return
     */
    public String getHeightAndWidth(){
        int width= Resources.getSystem().getDisplayMetrics().widthPixels;
        int height=Resources.getSystem().getDisplayMetrics().heightPixels;
        String str=width+" * "+height+"";
        return str;
    }
}
