package com.tool.phoneutils.activity;
import java.util.List;


import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;

import com.tool.phoneutils.R;
import com.tool.phoneutils.cview.AppCycleScrollAdapter;
import com.tool.phoneutils.cview.CycleScrollView;

/**
 * Created by wlhuang on 26/08/2016.
 */
public class CycleActivity extends Activity{
    private CycleScrollView<PackageInfo> mCycleScrollView;
    private AppCycleScrollAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cycle_view);

        mCycleScrollView = ((CycleScrollView<PackageInfo>) this.findViewById(R.id.cycle_scroll_view));

        /**
         * Get APP list and sort by update time.
         */
        List<PackageInfo> list = this.getPackageManager()
                .getInstalledPackages(0);

        mAdapter = new AppCycleScrollAdapter(list, mCycleScrollView, this);

    }
}