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
	  
	        // ��1��:������������ͷָ��MediaRecorder  
	        StartActivity.mCamera.unlock();  
	        StartActivity. mMediaRecorder.setCamera(StartActivity.mCamera);  
	  
	        // ��2��:ָ��Դ  
	        StartActivity.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);  
	        StartActivity.mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);  
	  
	        // ��3��:ָ��CamcorderProfile(��ҪAPI Level 8���ϰ汾)  
//	      mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));  
	            // ��3��:���������ʽ�ͱ����ʽ(��Ե���API Level 8�汾)  
	        StartActivity.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);   
	        StartActivity.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);   
	        StartActivity.mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);  
	  
	  
	        // ��4��:ָ������ļ�  
	       // mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());  
	        StartActivity.mMediaRecorder.setOutputFile( MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString());
	           
	        // ��5��:ָ��Ԥ�����  
	        StartActivity.mMediaRecorder.setPreviewDisplay(StartActivity.mPreview.getHolder().getSurface());  
	  
	        // ��6��:������������׼��MediaRecorder  
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
	        // ���recorder����  
	    	StartActivity.mMediaRecorder.reset();   
	        // �ͷ�recorder����  
	    	StartActivity.mMediaRecorder.release();   

	    	StartActivity.mMediaRecorder = null;  
	        // Ϊ����ʹ����������ͷ  
	        StartActivity.mCamera.lock();   
	    }  
	}  
}
