package com.kunpeng.camera;

import java.io.IOException;



import android.media.MediaRecorder;
import android.provider.MediaStore;
import android.util.Log;


public class videoActivity{
	
	/* private Camera mCamera;  
	 private CameraPreview mPreview;*/
	 private static final String TAG = "TAG-AndroidCameraActivity"; 
	 private videoActivity()
	 {
		 
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
