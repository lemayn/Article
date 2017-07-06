package com.example.leon.article.Activity.video;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.leon.article.Activity.video.permission.PermissionsActivity;
import com.example.leon.article.Activity.video.permission.PermissionsChecker;
import com.example.leon.article.Activity.video.videocompress.CompressListener;
import com.example.leon.article.Activity.video.videocompress.Compressor;
import com.example.leon.article.Activity.video.videocompress.InitListener;
import com.example.leon.article.Activity.video.videorecord.CameraActivity;
import com.example.leon.article.R;

import java.io.File;


public class UpUserVideoActivity extends AppCompatActivity {

    private String currentInputVideoPath = "";
    private String currentOutputVideoPath = "/mnt/sdcard/videokit/out.mp4";
    private String cmd = "-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
            "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 16:9 " + currentOutputVideoPath;

    private Double videoLength = 0.00;//视频时长 s
    private Compressor mCompressor;
    private static final int REQUEST_CODE_FOR_PERMISSIONS = 0;//
    private static final int REQUEST_CODE_FOR_RECORD_VIDEO = 1;//录制视频请求码
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED = 2;//视频录制成功
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_FAILED = 3;//视频录制出错
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_CANCEL = 4;//取消录制
    public static final String INTENT_EXTRA_VIDEO_PATH = "intent_extra_video_path";//录制的视频路径

    //相机权限,录制音频权限,读写sd卡的权限,都为必须,缺一不可
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_user_video);

        init();
        PermissionsChecker mChecker = new PermissionsChecker(getApplicationContext());
        if (mChecker.lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE_FOR_PERMISSIONS, PERMISSIONS);
        }

        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                Log.v("FiDo", "load library succeed");
            }

            @Override
            public void onLoadFail(String reason) {
                Log.i("FiDo", "load library fail:" + reason);
            }
        });

    }

    private void init() {
        findViewById(R.id.bt_recording).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recording
                recording();
            }
        });
    }

    private void recording() {
        //录制
        CameraActivity.startActivityForResult(this, REQUEST_CODE_FOR_RECORD_VIDEO);
    }

    private void execCommand(String cmd) {
        File mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }
        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                Log.i("FiDo", "success " + message);
                Toast.makeText(getApplicationContext(), R.string.compress_succeed, Toast.LENGTH_SHORT).show();
                hideProgress();
            }

            @Override
            public void onExecFail(String reason) {
                Log.i("FiDo", "fail " + reason);
                hideProgress();
            }

            @Override
            public void onExecProgress(String message) {
                Log.i("FiDo", "progress " + message);
                showProgress("", "压缩中...", -1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_PERMISSIONS) {
            //权限申请
            if (PermissionsActivity.PERMISSIONS_DENIED == resultCode) {
                //权限未被授予，退出应用
                finish();
            } else if (PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
                //权限被授予
                //do nothing
            }
        } else if (requestCode == REQUEST_CODE_FOR_RECORD_VIDEO) {
            //录制视频
            if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED) {
                //录制成功
                String videoPath = data.getStringExtra(INTENT_EXTRA_VIDEO_PATH);
                Log.i("FiDo", "onActivityResult: "+videoPath);
                if (!TextUtils.isEmpty(videoPath)) {
                    currentInputVideoPath = videoPath;
                    MediaMetadataRetriever retr = new MediaMetadataRetriever();
                    retr.setDataSource(currentInputVideoPath);
                    String time = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);//获取视频时长
                    execCommand("-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                            "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 16:9 " + currentOutputVideoPath);
                    //7680
                    try {
                        videoLength = Double.parseDouble(time)/1000.00;
                    } catch (Exception e) {
                        e.printStackTrace();
                        videoLength = 0.00;
                    }
                    Log.v("FiDo", "videoLength = "+videoLength + "s");

                }
            } else if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_FAILED) {
                //录制失败
                currentInputVideoPath = "";
            }
        }
    }


    private void showProgress(String title, String message, int theme) {
        if (mProgressDialog == null) {
            if (theme > 0)
                mProgressDialog = new ProgressDialog(this, theme);
            else
                mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
        }

        if (!TextUtils.isEmpty(title))
            mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

}
