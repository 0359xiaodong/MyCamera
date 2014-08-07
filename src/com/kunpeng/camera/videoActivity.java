package com.kunpeng.camera;

import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/*
import java.io.IOException;



import android.media.MediaRecorder;
import android.provider.MediaStore;
import android.util.Log;


public class videoActivity{
	
	 private Camera mCamera;  
	 private CameraPreview mPreview;
	 private static final String TAG = "TAG-AndroidCameraActivity"; 
	 public videoActivity()
	 {
		 this.prepareVideoRecorder();
	 }
	 public boolean prepareVideoRecorder() {  
	  	  
	        StartActivity.mMediaRecorder = new MediaRecorder();  
	  
	        // 第1步:解锁并将摄像头指向MediaRecorder  
	        StartActivity.mCamera.unlock();  
	        StartActivity. mMediaRecorder.setCamera(StartActivity.mCamera);  
	  
	        // 第2步:指定源  
	        StartActivity.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);  
	        StartActivity.mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);  
	  
	        // 第3步:指定CamcorderProfile(需要API Level 8以上版本)  
//	      mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));  
	            // 第3步:设置输出格式和编码格式(针对低于API Level 8版本)  
	        StartActivity.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);   
	        StartActivity.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);   
	        StartActivity.mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);  
	  
	  
	        // 第4步:指定输出文件  
	       // mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());  
	        StartActivity.mMediaRecorder.setOutputFile( MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString());
	           
	        // 第5步:指定预览输出  
	        StartActivity.mMediaRecorder.setPreviewDisplay(StartActivity.mPreview.getHolder().getSurface());  
	  
	        // 第6步:根据以上配置准备MediaRecorder  
	        try {  
	        	StartActivity.mMediaRecorder.prepare();  
	        } catch (IllegalStateException e) {  
	            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());  
	            releaseMediaRecorder();  
	            return false;  
	        } catch (IOException e) {  
	            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());  
	            releaseMediaRecorder();  
	            return false;  
	        }  
	        return true;  
	    } 

	public void releaseMediaRecorder() {  
	    if (StartActivity.mMediaRecorder != null) {  
	        // 清除recorder配置  
	    	StartActivity.mMediaRecorder.reset();   
	        // 释放recorder对象  
	    	StartActivity.mMediaRecorder.release();   

	    	StartActivity.mMediaRecorder = null;  
	        // 为后续使用锁定摄像头  
	        StartActivity.mCamera.lock();   
	    }  
	}  
}
*/
public class videoActivity extends Activity implements OnClickListener, SurfaceHolder.Callback {
    MediaRecorder recorder;
    SurfaceHolder holder;
    boolean recording = false;
    FrameLayout mFrameLayout; 
    public static CameraPreview mPreview;
    public static Camera mCamera=getCameraInstance();
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    recorder = new MediaRecorder();
    initRecorder();
    setContentView(R.layout.test02);
    
    
    
    SurfaceView cameraView = (SurfaceView) findViewById(R.id.sur);
    holder = cameraView.getHolder();
    holder.addCallback(this);
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    surfaceCreated(holder);
    cameraView.setClickable(true);
    cameraView.setOnClickListener(this);

}

private void initRecorder() {
    recorder.setAudioSource(/*MediaRecorder.AudioSource.DEFAULT*/MediaRecorder.AudioSource.MIC);
    recorder.setVideoSource(/*MediaRecorder.VideoSource.DEFAULT*/MediaRecorder.AudioSource.MIC);

    CamcorderProfile cpHigh = CamcorderProfile
            .get(CamcorderProfile.QUALITY_HIGH);
    recorder.setProfile(cpHigh);
    recorder.setOutputFile("/sdcard/videocapture_example.mp4");
    recorder.setMaxDuration(50000); // 50 seconds
    recorder.setMaxFileSize(5000000); // Approximately 5 megabytes
}

private void prepareRecorder() {
    recorder.setPreviewDisplay(holder.getSurface());

    try {
        recorder.prepare();
    } catch (IllegalStateException e) {
        e.printStackTrace();
        finish();
    } catch (IOException e) {
        e.printStackTrace();
        finish();
    }
}

public void onClick(View v) {
    if (recording) {
        recorder.stop();
        recording = false;

        // Let's initRecorder so we can record again
        initRecorder();
        prepareRecorder();
    } else {
        recording = true;
        recorder.start();
    }
}

public void surfaceCreated(SurfaceHolder holder) {
    prepareRecorder();
}

public void surfaceChanged(SurfaceHolder holder, int format, int width,
        int height) {
}

public void surfaceDestroyed(SurfaceHolder holder) {
    if (recording) {
        recorder.stop();
        recording = false;
    }
    recorder.release();
    finish();
}

public static Camera getCameraInstance(){ 
    Camera c = null; 
    try { 
        c = Camera.open();
    } 
    catch (Exception e){
    Log.d("TAG", "Error is "+e.getMessage());
    } 
    return c;
}


}