package com.pep.core.main;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.Status;
import com.downloader.request.DownloadRequest;
import com.pep.core.http.ApiService;
import com.pep.core.libav.PEPAudioManager;
import com.pep.core.libav.PEPAvType;
import com.pep.core.libnet.PEPDownloadManager;
import com.pep.core.libnet.PEPHttpManager;
import com.pep.core.model.BaseListModel;
import com.pep.core.model.JokeModel;
import com.pep.core.uibase.PEPProgressView;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * The type Demo presenter.
 *
 * @author sunbaixin
 */
public class DemoPresenter implements DemoContract.Presenter {
    private final DemoContract.View demoView;
    private int downLoadId;

    /**
     * Instantiates a new Demo presenter.
     *
     * @param demoView the login view
     */
    DemoPresenter(DemoContract.View demoView) {
        this.demoView = demoView;
        demoView.setPresenter(this);
    }

    @Override
    public void downStart() {
        AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
        if (downLoadId != 0 && PEPDownloadManager.getStatus(downLoadId) == Status.RUNNING) {
            return;
        }
        //获取SD卡目录
        File f = Environment.getExternalStorageDirectory();
        downLoadId = PEPDownloadManager.downLoad("https://dl.google.com/dl/android/studio/install/3.4.1.0/android-studio-ide-183.5522156-mac.dmg", f.getPath() + "/core", "mac.dmg").setOnProgressListener(progress -> {
            long endTime = System.currentTimeMillis();
            if (endTime - startTime.get() > 200) {
                int currentProgress = (int) (((double) progress.currentBytes / (double) progress.totalBytes) * 100);
                demoView.progressUpdate(currentProgress, progress);
                startTime.set(endTime);
            }

        }).start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {

            }

            @Override
            public void onError(Error error) {

            }
        });

    }

    @Override
    public void downResume() {
        PEPDownloadManager.resume(downLoadId);
    }

    @Override
    public void downPause() {
        PEPDownloadManager.pause(downLoadId);
    }

    @Override
    public void netHttp() {
        demoView.showProgress();
        PEPHttpManager.getInstance().getService(ApiService.class).getJoke(1).enqueue(new Callback<BaseListModel<JokeModel>>() {
            @Override
            public void onResponse(Call<BaseListModel<JokeModel>> call, Response<BaseListModel<JokeModel>> response) {
                assert response.body() != null;
                demoView.dismissProgress();
                if (response.body().code == 200) {
                    demoView.updateData(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseListModel<JokeModel>> call, Throwable t) {
                demoView.dismissProgress();
            }
        });
    }

    @Override
    public void audioInitUrl(PEPAudioManager pepAudioManager, String url) {
        pepAudioManager.setUrl("http://www.170mv.com/kw/antiserver.kuwo.cn/anti.s?rid=MUSIC_74693766&response=res&format=mp3|aac&type=convert_url&br=128kmp3&agent=iPhone&callback=getlink&jpcallback=getlink.mp3", PEPAvType.VIDEO_TYPE_DEF);
    }


}
