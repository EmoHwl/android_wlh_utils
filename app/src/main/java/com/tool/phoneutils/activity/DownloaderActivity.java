package com.tool.phoneutils.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tool.phoneutils.R;
import com.tool.phoneutils.download.Downloader;
import com.tool.phoneutils.download.DownloaderResponseBody;
import com.tool.phoneutils.utils.L;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloaderActivity extends AppCompatActivity implements DownloaderResponseBody.ProgressListener {

    public static final String TAG = "MainActivity";
    public static final String PACKAGE_URL = "http://192.168.1.254/Novatek/Movie/2016_0910_160632_050A.MP4";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private long breakPoints = 0L;
    private Downloader downloader;
    private File file;
    private long totalBytes;
    private long contentLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.download_button, R.id.pause_button, R.id.continue_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_button:
                // 新下载前清空断点信息
                breakPoints = 0L;
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "2016_0910_160632_050A.MP4");
                downloader = new Downloader(PACKAGE_URL, file, this);
                downloader.download(breakPoints);
                break;
            case R.id.pause_button:
                downloader.pause();
                Toast.makeText(this, "下载暂停", Toast.LENGTH_SHORT).show();
                // 存储此时的totalBytes，即断点位置。
                breakPoints = totalBytes;
                break;
            case R.id.continue_button:
                downloader.download(breakPoints);
                break;
        }
    }

    @Override
    public void onPreExecute(long contentLength) {
        // 文件总长只需记录一次，要注意断点续传后的contentLength只是剩余部分的长度
        if (this.contentLength == 0L) {
            this.contentLength = contentLength;
            progressBar.setMax((int) (contentLength / 1024));
        }
    }

    @Override
    public void update(final long totalBytes, boolean done) {
        // 注意加上断点的长度
        this.totalBytes = totalBytes + breakPoints;
        progressBar.setProgress((int) (totalBytes + breakPoints) / 1024);
        L.i(this.totalBytes + "|" + totalBytes);
    }
}
