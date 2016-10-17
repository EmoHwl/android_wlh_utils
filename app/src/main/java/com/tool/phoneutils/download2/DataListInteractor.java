package com.tool.phoneutils.download2;

import android.content.Context;


import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * Created by wlhuang on 16-5-30.
 */
public interface DataListInteractor {

    interface RequestCallBack<T> {
        /**
         * 200返回数据
         *
         * @param data
         */
        void onSuccess(T data);

        /**
         * 请求成功，返回失败数据
         *
         * @param result
         */
        void onFailed(String result);

        /**
         * 本地异常
         *
         * @param ex
         */
        void onError(Throwable ex);
    }

    ;

    void setCancelDownload();

    boolean getCancelDownload();

    void setCancelStitch();

    boolean getCancelStitch();

    boolean isDownloading();

    /**
     * 加载数据
     * 显示本地
     *
     * @param requestCallBack
     */
    Future loadDatas(RequestCallBack requestCallBack, boolean isLocal);

    Future loadLocalDatas(RequestCallBack requestCallBack);


    /**
     * 获取本地文件数据
     *
     * @param filePath 本地路径 根据路径 分别加载不同的内容
     * @return 文件名
     */
    ArrayList<String> getLocalDatas(String filePath);

    void stopThreadPool();

    interface DownloadRequestCallBack {
        /**
         * 下载中
         */
        void onLoading(int progress);

        /**
         * 开始下载
         */
        void onStart(int progress);

        /**
         * 中断
         */
        void onUserCancel(String result);

        /**
         * 完成下载
         */
        void onFinishAndStitch();

        /**
         * 视频拼接
         */
        void onStitch();

        boolean checkMemory(Long fileSize);

        //异常
        void onError(Throwable ex);
    }

    /**
     * 删除本地
     *
     * @param localPaths
     */
    boolean deleteLocalDatas(String... localPaths);
}
