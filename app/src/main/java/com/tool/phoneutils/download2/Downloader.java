package com.tool.phoneutils.download2;

import android.content.Context;
import android.text.format.Formatter;

import com.tool.phoneutils.CustomApplication;
import com.tool.phoneutils.ThreadManager.ThreadPoolManager;
import com.tool.phoneutils.utils.L;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by wlhuang on 12/09/2016.
 */
public class Downloader {
    public static final String TAG = Downloader.class.toString();
    private DataListInteractor.DownloadRequestCallBack downloadRequestCallBack;
    private String urlStr;
    private File destination;
    private ThreadPoolManager threadPoolManager;
    private long curDownCount;
    private int threadCount = 3;
    private HashMap<String, Future> futureHashMap = new HashMap<>();
    private HashMap<String, DownloadThread> downloadThreadHashMap = new HashMap<>();
    public Context context;

    public Downloader(String url, File destination, DataListInteractor.DownloadRequestCallBack downloadRequestCallBack) {
        this.urlStr = url;
        this.destination = destination;
        this.downloadRequestCallBack = downloadRequestCallBack;
        threadPoolManager = new ThreadPoolManager();
        this.context = CustomApplication.appContext;
        threadCount = Runtime.getRuntime().availableProcessors();
        if (threadCount <= 0) {
            threadCount = 1;
        } else if (threadCount > 3) {
            threadCount = 3;
        }
    }

    public void cancelDownload() {
        if (downloadThreadHashMap != null) {
            for (String key : downloadThreadHashMap.keySet()) {
                DownloadThread downloadThread = downloadThreadHashMap.get(key);
                if (downloadThread != null) {
//                    downloadThread.disConnect();
                    downloadThread = null;
                }
                futureHashMap.get(key).cancel(true);
            }
        }
    }

    public Future download() {
        return threadPoolManager.getCachedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        int length = conn.getContentLength();
                        RandomAccessFile randomAccessFile = new RandomAccessFile(destination.getPath(), "rwd");
                        randomAccessFile.setLength(length);
                        randomAccessFile.close();
                        L.i("fileSize = " + length);
                        L.i("fileSize = " + destination.length());
                        int blockSize = length / threadCount;
                        for (int threadId = 1; threadId <= threadCount; threadId++) {
                            L.i("blockSize = " + Formatter.formatFileSize(context, blockSize));
                            int startIndex = blockSize * (threadId - 1);
                            int endIndex = blockSize * threadId - 1;

                            if (threadId == threadCount) {
                                endIndex = length - 1;
                            }

                            L.i("线程【" + threadId + "】开始下载：" + startIndex + "---->" + endIndex);
                            DownloadThread downloadThread = new DownloadThread(destination.getPath(), threadId, startIndex, endIndex);
//                            downloadThread.start();
                            Future future = threadPoolManager.getCachedThreadPool().submit(downloadThread);
                            String key = destination.getName() + threadId;
                            futureHashMap.put(key, future);
                            downloadThreadHashMap.put(key, downloadThread);
                            L.i("当前活动的线程数：" + downloadThreadHashMap.size());
                        }
                    } else {
                        L.i("下载失败！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    L.e(" " + e.getMessage());
                    L.i("发生异常,下载失败!");
                }
            }
        });
    }

    public class DownloadThread extends Thread {
        private String path;
        private int threadId;
        private long startIndex;
        private long endIndex;
        private HttpURLConnection httpURLConnection;
        private long totalLength;

        /**
         * 构造方法
         *
         * @param path       下载文件的路径
         * @param threadId   下载文件的线程
         * @param startIndex 下载文件开始的位置
         * @param endIndex   下载文件结束的位置
         */
        DownloadThread(String path, int threadId, int startIndex, int endIndex) {
            this.path = path;
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            totalLength = destination.length();
        }

        public void disConnect() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }).start();
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            RandomAccessFile randomAccessFile = null;
            FileChannel fileChannel = null;
            String name = destination.getName();
            long total = 0;//累计进度
            try {
                long downloadNewIndex = PreferencesManager.getInstance().getLong(name + threadId);
                L.i("downloadNewIndex = " + downloadNewIndex);
                if (downloadNewIndex > 0) {
                    long alreadyDown = downloadNewIndex - startIndex;
                    curDownCount += alreadyDown;
                    startIndex = downloadNewIndex;
                }
                L.i("线程【" + threadId + "】开始下载数据区间:" + startIndex + "------>" + endIndex);

                URL url = new URL(urlStr);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
                httpURLConnection.connect();
                int code = httpURLConnection.getResponseCode();
                int length = httpURLConnection.getContentLength();
                L.i("download length = " + Formatter.formatFileSize(context, length));
                int showProgress = 1000;
                if (code == 206 || code == 200) {
                    inputStream = httpURLConnection.getInputStream();
                    randomAccessFile = new RandomAccessFile(path, "rws");
                    randomAccessFile.seek(startIndex);
//                    fileChannel = randomAccessFile.getChannel();
//                    MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startIndex, length);
                    int len = 0;
                    byte[] buffer = new byte[10 * 1024];
                    while ((len = inputStream.read(buffer)) != -1) {
//                        mappedByteBuffer.put(buffer, 0, len);
                        randomAccessFile.write(buffer, 0, len);
                        total += len;
                        curDownCount += len;
                        downloadRequestCallBack.onLoading((int) ((curDownCount * 100) / totalLength));
                        if (showProgress == 1000) {
                            L.i("线程【" + threadId + "】当前下载数据区间:" + (startIndex + total));
                            L.i("线程【" + threadId + "】当前已下载数据:" + Formatter.formatFileSize(context, total));
                            L.i("当前已下载数据:" + Formatter.formatFileSize(context, curDownCount));
                            showProgress = 0;
                        } else {
                            showProgress++;
                        }
                    }
                    if (fileChannel != null) {
                        fileChannel.close();
                    }
                    inputStream.close();
                    randomAccessFile.close();
                    L.i("线程【" + threadId + "】下载完毕");
                    downloadThreadHashMap.remove(name + threadId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                L.i("线程【" + threadId + "】下载出现异常！！ ex = " + e.getMessage() + " line =" + e.getStackTrace()[0].getLineNumber());
            } finally {
                L.i("save =" + (startIndex + total));
                PreferencesManager.getInstance().saveLong(name + threadId, startIndex + total);
                if (downloadThreadHashMap.size() == 0) {
                    L.i("clear mark");
                    for (int i = 1; i <= threadCount; i++) {
                        String key = name + i;
                        L.i("key = " + key);
                        PreferencesManager.getInstance().clearKey(key);
                    }
                    PreferencesManager.getInstance().saveString(name, "true");//下载完成,拼接前标记,便于未完成拼接文件,重新拼接
                    downloadRequestCallBack.onFinishAndStitch();
                }
                try {
                    if (fileChannel != null) {
                        fileChannel.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
