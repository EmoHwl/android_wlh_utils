package com.tool.phoneutils.activity;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tool.phoneutils.R;
import com.tool.phoneutils.download.DownloaderResponseBody;
import com.tool.phoneutils.download2.DataListInteractor;
import com.tool.phoneutils.download2.Downloader;
import com.tool.phoneutils.utils.L;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloaderActivity extends AppCompatActivity implements DownloaderResponseBody.ProgressListener {

    public static final String TAG = "MainActivity";
    public static final String PACKAGE_URL = "https://www.charlesproxy.com/assets/release/4.0.1/charles-proxy-4.0.1-win64.msi";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private long breakPoints = 0L;
    private com.tool.phoneutils.download2.Downloader downloader;
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
                //http://192.168.1.254/Novatek/Movie/2016_0918_230955_002A.MP4
                progressBar.setMax((int) (contentLength / 1024));
                String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "ODM";
                file = new File(filePath, "charles-proxy-4.0.1-win64.msi");
                downloader = new Downloader(PACKAGE_URL, file, new DataListInteractor.DownloadRequestCallBack() {
                    @Override
                    public void onLoading(int progress) {
//                        L.i(""+progress);
                        progressBar.setProgress((int) (progress) / 1024);
                    }

                    @Override
                    public void onStart(int progress) {
//                        L.i(""+progress);
                    }

                    @Override
                    public void onUserCancel(String result) {
                        L.i(""+result);
                    }

                    @Override
                    public void onFinishAndStitch() {
                        L.i("onFinishAndStitch");

                    }

                    @Override
                    public void onStitch() {
                        L.i("onFinishAndStitch");
                    }

                    @Override
                    public boolean checkMemory(Long fileSize) {
                        return true;
                    }

                    @Override
                    public void onError(Throwable ex) {
                        L.e(""+ex.getMessage());
                    }
                });
                downloader.download();
                break;
            case R.id.pause_button:
                downloader.cancelDownload();
                Toast.makeText(this, "下载暂停", Toast.LENGTH_SHORT).show();
                // 存储此时的totalBytes，即断点位置。
                break;
            case R.id.continue_button:
                downloader.download();
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
