package com.pep.core.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.pep.core.R;
import com.pep.core.libav.PEPAudioListener;
import com.pep.core.libav.PEPAudioManager;
import com.pep.core.libav.PEPAvType;
import com.pep.core.libav.PEPVideoFragment;
import com.pep.core.libbase.PEPBaseActivity;
import com.pep.core.libcommon.CaptureUtil;
import com.pep.core.libcommon.PermissionUtils;
import com.pep.core.libimage.PEPImageManager;
import com.pep.core.libnet.PEPHttpManager;
import com.pep.core.model.BaseListModel;
import com.pep.core.model.JokeModel;
import com.pep.core.uibase.PEPLoadingView;
import com.pep.core.uibase.PEPPhotoPageFragment;
import com.pep.core.view.LeftAnimateDemoDialog;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.pep.core.libav.PEPAvType.VIDEO_TYPE_M3U8;


/**
 * The type Main activity.
 *
 * @author sunbaixin
 */
public class DemoActivity extends PEPBaseActivity implements DemoContract.View {

    private DemoContract.Presenter presenter;
    PEPAudioManager pepAudioManager = new PEPAudioManager(this);
    private Button btnPdf;
    private Button btn_permission;
    private Button btn_pictures;
    private Button btn_camera;
    private Button btnHttp;
    private Button loadImageButton;
    private Button btnProgress;
    private Button btnLoading;
    private Button btnImagePage;
    private Button btnVideo;
    private Button btnAnimate;
    private Button btnPlay;
    private Button btnPause;
    private Button btnStop;
    private Button btnChange;
    private Button btnSpeed;
    private TextView tvTimeStart;
    private SeekBar seekBar;
    private TextView tvTimeEnd;
    private Button btnDownloadStart;
    private Button btnDownloadPause;
    private Button btnDownloadResume;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ProgressBar progressBar;
    private TextView tvCurrentProgress;
    private PEPLoadingView pepLoading;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_pictures = (Button) findViewById(R.id.btn_pictures);
        btn_permission = (Button) findViewById(R.id.btn_permission);
        btnPdf = (Button) findViewById(R.id.btn_pdf);
        btnHttp = (Button) findViewById(R.id.btn_http);
        loadImageButton = (Button) findViewById(R.id.load_image_button);
        btnProgress = (Button) findViewById(R.id.btn_progress);
        btnLoading = (Button) findViewById(R.id.btn_loading);
        btnImagePage = (Button) findViewById(R.id.btn_image_page);
        btnVideo = (Button) findViewById(R.id.btn_video);
        btnAnimate = (Button) findViewById(R.id.btn_animate);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnChange = (Button) findViewById(R.id.btn_change);
        btnSpeed = (Button) findViewById(R.id.btn_speed);
        tvTimeStart = (TextView) findViewById(R.id.tv_time_start);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        tvTimeEnd = (TextView) findViewById(R.id.tv_time_end);
        btnDownloadStart = (Button) findViewById(R.id.btn_download_start);
        btnDownloadPause = (Button) findViewById(R.id.btn_download_pause);
        btnDownloadResume = (Button) findViewById(R.id.btn_download_resume);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvCurrentProgress = (TextView) findViewById(R.id.tv_current_progress);
        pepLoading = (PEPLoadingView) findViewById(R.id.pep_loading);
    }

    @Override
    public void initData() {

        presenter = new DemoPresenter(this);
        initApplication();
        pepAudioManager.addPepAudioListener(pepAudioListener);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        progressBar.setMax(100);


        btnPdf.setOnClickListener(this);
        btnDownloadStart.setOnClickListener(this);
        btnDownloadResume.setOnClickListener(this);
        btnDownloadPause.setOnClickListener(this);
        btnHttp.setOnClickListener(this);
        btnImagePage.setOnClickListener(this);
        btnProgress.setOnClickListener(this);
        btnLoading.setOnClickListener(this);
        btnImagePage.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
        btnAnimate.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnSpeed.setOnClickListener(this);
        loadImageButton.setOnClickListener(this);
        btn_camera.setOnClickListener(this);
        btn_pictures.setOnClickListener(this);
        btn_permission.setOnClickListener(this);
    }


    /**
     * 将在在applicatin 中要初始化的方法,暂时可以忽略
     */
    private void initApplication() {

        //初始化下载工具
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(this.getApplicationContext(), config);


        //初始化LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(getApplication());

        //初始化网络请求
        PEPHttpManager.getInstance().init("https://api.apiopen.top/");

        //初始化全局json打印类
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .methodOffset(7)
                .tag("PEPCORE")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pdf:
                break;
            case R.id.btn_download_start:
                presenter.downStart();
                break;
            case R.id.btn_download_pause:
                presenter.downPause();
                break;
            case R.id.btn_download_resume:
                presenter.downResume();
                break;
            case R.id.btn_http:
                presenter.netHttp();
                break;
            case R.id.load_image_button:
                imageLoad();
                break;
            case R.id.btn_progress:
                progressLoading();
                break;
            case R.id.btn_loading:
                layoutLoading();
                break;
            case R.id.btn_image_page:

                ArrayList<String> urls = new ArrayList<>();
                urls.add("http://b-ssl.duitang.com/uploads/item/201512/07/20151207151559_n2sf3.jpeg");
                urls.add("https://b-ssl.duitang.com/uploads/item/201709/14/20170914120928_m3QLv.jpeg");
                urls.add("http://b-ssl.duitang.com/uploads/item/201209/03/20120903183740_c5Tar.jpeg");
                urls.add("http://pic40.nipic.com/20140424/18085978_175633400193_2.jpg");
                urls.add("http://b-ssl.duitang.com/uploads/blog/201401/16/20140116161553_Mus5Z.jpeg");
                urls.add("http://b-ssl.duitang.com/uploads/item/201803/27/20180327110700_ywqpv.jpg");

                PEPPhotoPageFragment pepPhotoPageFragment = new PEPPhotoPageFragment();
                pepPhotoPageFragment.addUrls(urls);
                pepPhotoPageFragment.show(getSupportFragmentManager(), "PEPPhotoPageFragment");
                break;
            case R.id.btn_video:
                PEPVideoFragment pepVideoFragment = new PEPVideoFragment();
                pepVideoFragment.setData(VIDEO_TYPE_M3U8, "https://testczpt.mypep.cn/jxw-sdk-web/saas/resource/hlsIndexM3u8.do?resId=12110011011610205001542954347676&access_token=2945d076138145ca9c88a69666e807a0");
                pepVideoFragment.show(getSupportFragmentManager(), "PEPVideoFragment");
                break;
            case R.id.btn_animate:
                LeftAnimateDemoDialog leftAnimateDemoFragment = new LeftAnimateDemoDialog();
                leftAnimateDemoFragment.show(getSupportFragmentManager(), "LeftAnimateDemoDialog");
                break;
            case R.id.btn_play:
                pepAudioManager.play();
                break;

            case R.id.btn_pause:
                pepAudioManager.pause();
                break;
            case R.id.btn_stop:
                pepAudioManager.release();
                break;
            case R.id.btn_change:
                pepAudioManager.setUrl("http://www.170mv.com/kw/antiserver.kuwo.cn/anti.s?rid=MUSIC_73396198&response=res&format=mp3|aac&type=convert_url&br=128kmp3&agent=iPhone&callback=getlink&jpcallback=getlink.mp3", PEPAvType.VIDEO_TYPE_DEF);
                pepAudioManager.play();
                break;
            case R.id.btn_speed:
                pepAudioManager.speed(4F);
                break;
            case R.id.btn_pictures://相册
                boolean granted = PermissionUtils.isGranted(this,"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA");
                if (granted){//已授权, 进行具体操作
                    CaptureUtil.openPic(this,REQUESTCODE_PICK);
                }else{
                    requestPermisson();
                }
                break;
            case R.id.btn_camera://相机
                boolean granted1 = PermissionUtils.isGranted(this,"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA");
                if (granted1){//已授权, 进行具体操作
                    outFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
                    Uri uri = CaptureUtil.getUri(this,outFile);
                    CaptureUtil.takePicture(this,uri,PHOTO_REQUEST_TAKEPHOTO);
                }else{
                    requestPermisson();
                }
                break;
            case R.id.btn_permission://权限申请
                requestPermisson();
                break;
            default:
        }
    }

    /**
     * 权限申请
     */
    public void requestPermisson(){
        PermissionUtils.permission(this,"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA")
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {

                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        if (permissionsDeniedForever.size()>0){
                            // permissionsDeniedForever: 禁止后不再提示的list, 此处可以打开一个dialog,让用户进行确认
                            Toast.makeText(DemoActivity.this,"必须打开"+permissionsDeniedForever.get(0)+"授权,软件才能正常使用",Toast.LENGTH_SHORT).show();
                            PermissionUtils.launchAppDetailsSettings(DemoActivity.this);
                        }
                    }
                }).request();
    }

    PEPAudioListener pepAudioListener = new PEPAudioListener() {
        @Override
        public void onPlayState(boolean isPlay) {

        }

        @Override
        public void onBuffering() {
            showProgress();
        }

        @Override
        public void onPlayEnd() {
            dismissProgress();
        }

        @Override
        public void onPlayReady() {
            dismissProgress();
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            dismissProgress();
            Logger.d(error.getMessage() + "===================");
        }

        @Override
        public void onProgress(int progress, int bprogress, int maxProgress, String currentTime, String durationTime) {
            seekBar.setMax(maxProgress);
            seekBar.setSecondaryProgress(bprogress);
            seekBar.setProgress(progress);
            tvTimeStart.setText(String.format("%s", currentTime));
            tvTimeEnd.setText(String.format("%s", durationTime));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void imageLoad() {
        PEPImageManager.getInstance().loadCircleCrop(this, imageView1, "http://b-ssl.duitang.com/uploads/item/201512/07/20151207151559_n2sf3.jpeg");
        PEPImageManager.getInstance().load(this, imageView2, "https://b-ssl.duitang.com/uploads/item/201709/14/20170914120928_m3QLv.jpeg");
        PEPImageManager.getInstance().load(this, imageView3, "http://b-ssl.duitang.com/uploads/item/201209/03/20120903183740_c5Tar.jpeg");
    }

    @Override
    public void progressLoading() {

        showProgress();
        new Handler().postDelayed(() -> dismissProgress(), 5000);
    }

    @Override
    public void layoutLoading() {
        pepLoading.setLoadingType(PEPLoadingView.PEP_LOADING_LOAD);
        new Handler().postDelayed(() -> pepLoading.setLoadingType(PEPLoadingView.PEP_LOADING_FINISH), 5000);
    }

    @Override
    public void progressUpdate(int currentProgress, Progress progress) {
        progressBar.setProgress(currentProgress);
        tvCurrentProgress.setText(new StringBuilder().append("当前下载:").append(progress.currentBytes).append("/").append(progress.totalBytes).append("---").append(currentProgress).append("%").toString());
    }

    @Override
    public void updateData(BaseListModel<JokeModel> baseListModel) {
        Toast.makeText(getApplicationContext(), baseListModel.result.get(0).text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(DemoContract.Presenter presenter) {
        this.presenter = presenter;
    }


    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
            if (!fromuser) {
                return;
            }

            long duration = pepAudioManager.getDuration();
            long newPosition = (duration * progress) / 1000L;
            pepAudioManager.seekTo((int) newPosition);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };



    /**
     * -------------------------------以下内容为相机和相册相关代码,可以直接copy,略微调整后使用-----------------------------------------------------
     */
    File fileCropUri ;
    File outFile;
    int output_X = 480, output_Y = 480;
    private Uri cropImageUri;
    /**
     * 选择头像相册选取
     */
    public static final int REQUESTCODE_PICK = 1;
    /**
     * 裁剪好头像-设置头像
     */
    public static final int REQUESTCODE_CUTTING = 2;
    /**
     * 选择头像拍照选取
     */
    public static final int PHOTO_REQUEST_TAKEPHOTO = 3;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  进行判断是那个操作跳转回来的，如果是裁剪跳转回来的这块就要把图片现实到View上，其他两种的话都把数据带入裁剪界面
        switch (requestCode) {
            //相册
            case REQUESTCODE_PICK:
                if(data==null)
                    return;
                fileCropUri = new File(getExternalCacheDir(),System.currentTimeMillis()+".jpg");
                cropImageUri = Uri.fromFile(fileCropUri);
                Uri newUri = Uri.parse(CaptureUtil.getPath(this, data.getData()));
                newUri = CaptureUtil.getUri(this,new File(newUri.getPath()));
                CaptureUtil.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, REQUESTCODE_CUTTING);
                break;
            //裁剪
            case REQUESTCODE_CUTTING:
                if (data != null) {
                    try {
                        Bitmap bitmap = CaptureUtil.getBitmapFromUri(CaptureUtil.getUri(this,fileCropUri),this);
                        imageView2.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            //拍照
            case PHOTO_REQUEST_TAKEPHOTO:
                fileCropUri = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
                cropImageUri = Uri.fromFile(fileCropUri);
                if(outFile!=null&&outFile.exists()) {
                    Uri uri = CaptureUtil.getUri(this,outFile);
                    CaptureUtil.cropImageUri(this, uri, cropImageUri, 1, 1, output_X, output_Y, REQUESTCODE_CUTTING);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

