package com.meetme.android.horizontallistview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.tool.phoneutils.cview.MarqueeView;
import com.tool.phoneutils.R;

import java.util.ArrayList;

public class MainHorizontalActivity extends Activity {

    //初始化数据
    private ArrayList<String> data = new ArrayList<String>() {
        {
            add("第1条");
            add("第2条");
            add("第3条");
            add("第4条");
            add("第5条");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_marquee);
        MarqueeView marqueeView = (MarqueeView) findViewById(R.id.marqueView);
        LinearLayoutManager lm=new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        marqueeView.setLayoutManager(lm);
        marqueeView.setAdapter(new MarqueeView.InnerAdapter(data,this));

    }

//    private HorizontalListView mHlvSimpleList;
//    private HorizontalListView mHlvCustomList;
//    private HorizontalListView mHlvCustomListWithDividerAndFadingEdge;
//
//    private String[] mSimpleListValues = new String[] { "Android", "iPhone", "WindowsMobile",
//            "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//            "Linux", "OS/2" };
//
//    private CustomData[] mCustomData = new CustomData[] {
//            new CustomData(Color.RED, "Red"),
//            new CustomData(Color.DKGRAY, "Dark Gray"),
//            new CustomData(Color.GREEN, "Green"),
//            new CustomData(Color.LTGRAY, "Light Gray"),
//            new CustomData(Color.WHITE, "White"),
//            new CustomData(Color.RED, "Red"),
//            new CustomData(Color.BLACK, "Black"),
//            new CustomData(Color.CYAN, "Cyan"),
//            new CustomData(Color.DKGRAY, "Dark Gray"),
//            new CustomData(Color.GREEN, "Green"),
//            new CustomData(Color.RED, "Red"),
//            new CustomData(Color.LTGRAY, "Light Gray"),
//            new CustomData(Color.WHITE, "White"),
//            new CustomData(Color.BLACK, "Black"),
//            new CustomData(Color.CYAN, "Cyan"),
//            new CustomData(Color.DKGRAY, "Dark Gray"),
//            new CustomData(Color.GREEN, "Green"),
//            new CustomData(Color.LTGRAY, "Light Gray"),
//            new CustomData(Color.RED, "Red"),
//            new CustomData(Color.WHITE, "White"),
//            new CustomData(Color.DKGRAY, "Dark Gray"),
//            new CustomData(Color.GREEN, "Green"),
//            new CustomData(Color.LTGRAY, "Light Gray"),
//            new CustomData(Color.WHITE, "White"),
//            new CustomData(Color.RED, "Red"),
//            new CustomData(Color.BLACK, "Black"),
//            new CustomData(Color.CYAN, "Cyan"),
//            new CustomData(Color.DKGRAY, "Dark Gray"),
//            new CustomData(Color.GREEN, "Green"),
//            new CustomData(Color.LTGRAY, "Light Gray"),
//            new CustomData(Color.RED, "Red"),
//            new CustomData(Color.WHITE, "White"),
//            new CustomData(Color.BLACK, "Black"),
//            new CustomData(Color.CYAN, "Cyan"),
//            new CustomData(Color.DKGRAY, "Dark Gray"),
//            new CustomData(Color.GREEN, "Green"),
//            new CustomData(Color.LTGRAY, "Light Gray")
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_custom);
//
//        // Get references to UI widgets
//        mHlvSimpleList = (HorizontalListView) findViewById(R.id.hlvSimpleList);
//        mHlvCustomList = (HorizontalListView) findViewById(R.id.hlvCustomList);
//        mHlvCustomListWithDividerAndFadingEdge = (HorizontalListView) findViewById(R.id.hlvCustomListWithDividerAndFadingEdge);
//
//        setupSimpleList();
//        setupCustomLists();
//    }
//
//    private void setupSimpleList() {
//        // Make an array adapter using the built in android layout to render a list of strings
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, mSimpleListValues);
//
//        // Assign adapter to the HorizontalListView
//        mHlvSimpleList.setAdapter(adapter);
//    }
//
//    private void setupCustomLists() {
//        // Make an array adapter using the built in android layout to render a list of strings
//        CustomArrayAdapter adapter = new CustomArrayAdapter(this, mCustomData);
//
//        // Assign adapter to HorizontalListView
//        mHlvCustomList.setAdapter(adapter);
//        mHlvCustomListWithDividerAndFadingEdge.setAdapter(adapter);
//    }

}
