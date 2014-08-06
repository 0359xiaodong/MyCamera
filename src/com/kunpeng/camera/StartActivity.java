package com.kunpeng.camera;

//import com.example.activity01.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 1.实现主界面大体布局
 * 2.拍照功能以及预览区功能的实现
 * @author magiwen
 *
 */
public class StartActivity extends Activity {
	
	
	
	//private static final String TAG = "MyLogCat";
	private static final String TAG = "TAG-AndroidCameraActivity";  
    
    public static final int MEDIA_TYPE_IMAGE = 1;   
    public static final int MEDIA_TYPE_VIDEO = 2;   

	 /** 相机   **/
    public static Camera mCamera;  
    /** 预览界面  **/
    public static CameraPreview mPreview;
    /** 缩略图  **/
    ImageView ThumbsView;
    /** 当前缩略图位置，默认为手机相册位置暂时没有缩略图 **/
    private Uri mUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
   /**相机拍摄区**/
    private FrameLayout mFrameLayout;
    /** MediaPlayer **/
    private MediaPlayer mPlayer;
    /** 拍照按钮  **/
    Button BtnCapture;
    /**装饰品按钮**/
    private Button Decorate;
    /**视频拍照功能转换按钮**/
    private ImageView video;
    /**曝光度调整**/
    private Button exposure;
    /**闪光灯按钮**/
    private ImageView flash;
    /**判断闪光灯开关**/
    public static boolean  flash_open=true;
    Property test01=new Property();
    /**相机参数**/
    private Camera.Parameters parameters ;
    /**录像机**/
    public static MediaRecorder mMediaRecorder;
    private videoActivity _video;
    /**录像开始与否**/
    private boolean isRecording = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 隐藏标题栏  **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /** 隐藏状态栏  **/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /** 禁用锁屏**/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.test01);
         System.out.println("\n");
        /** 硬件检查  **/
        if(CheckCameraHardware(this)==false)
        {
            Toast.makeText(this, "很抱歉，您的设备可能不支持摄像头功能！", Toast.LENGTH_SHORT).show();
            return;
        }
         
        /** 获取相机  **/
        mCamera=getCameraInstance();
        /** 获取预览界面   **/
        mPreview = new CameraPreview(this, mCamera); 
        mFrameLayout= (FrameLayout)findViewById(R.id.PreviewView); 
        mFrameLayout.addView(mPreview); 
        mCamera.startPreview();
        
        /**闪光灯功能的实现
         * 
         * @date 2014.8.5
         * **/
        
       
        flash=(ImageView)findViewById(R.id.flashImage);
        flash.setVisibility(View.VISIBLE);
        flash.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 flash_open=test01.init(StartActivity.this);
				/*如果摄像机打开，点击后关闭，闪光灯图片变为关闭图片*/
				if(flash_open==true)
				{
					parameters= mCamera.getParameters();
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					mCamera.setParameters(parameters);
					flash_open=false;
					
					
				}
				
				else
				{
					parameters= mCamera.getParameters();
            		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
					mCamera.setParameters(parameters);
					flash_open=true;
					
					
				}
				test01.writeDate(StartActivity.this, flash_open);
			}
        	
        	
        });

       
        /**拍照按钮功能实现
         * date 2014.8.4
         * **/
        BtnCapture= (Button) findViewById(R.id.takePhoto);
        BtnCapture.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) 
            {
                /** 使用takePicture()方法完成拍照  **/
            	
                mCamera.autoFocus(new AutoFocusCallback()
                {
                	
                    /** 自动聚焦聚焦后完成拍照  **/
                    @Override
                    public void onAutoFocus(boolean isSuccess, Camera camera) 
                    {
                        if(isSuccess&&camera!=null)
                        {
                        		mCamera.takePicture(mShutterCallback, null, mPicureCallback);
                        		if(flash_open==true)
                				{
                        			parameters= mCamera.getParameters();
                					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                					mCamera.setParameters(parameters);
                				}
                				
                				else
                				{
                					parameters= mCamera.getParameters();
                            		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                					mCamera.setParameters(parameters);
                				}
                        }
                    }
                     
                });
            }
        });
         
        
        
        /** 相机缩略图实现  **/
         
        ThumbsView = (ImageView)findViewById(R.id.ThumbsView);
        
        ThumbsView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) 
            {
                /** 使用Uri访问当前缩略图  **/
                Intent intent = new Intent(Intent.ACTION_VIEW); 
                
                Log.v(TAG,"mUri"+mUri);
                intent.setDataAndType(mUri, "image/*");
                
                if(mUri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                {
                	return;
                }
                startActivity(intent);
            }
        });
        
        /**装饰品按钮功能实现**///未完待续……
        /*Decorate=(Button)findViewById(R.id.Decorate);
        Decorate.setOnClickListener(new OnClickLister(){
        	 public void onClick(View v) 
             {
              
        }
        });*/
        /**视频拍照转换**/
     video=(ImageView) findViewById(R.id.ChangeCamera);
     video.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {  
            if (isRecording) {  
                // 停止录像，释放camera  
                mMediaRecorder.stop();   
                _video.releaseMediaRecorder();   

                mCamera.lock();   

                // 通知用户录像已停止  
                /*video.setText("开始录像");*/  

                isRecording = false;  
            } else {  
                // 初始化视频camera  
                if (_video.prepareVideoRecorder()) {  
                    mMediaRecorder.start();  

                    // 通知用户录像已开始   
                    /*video.setText("停止录像"); */ 

                    isRecording = true;  
                } else {  
                    // 准备未能完成，释放camera  
                    _video.releaseMediaRecorder();  
                }  
            }  
        }  
    });  
		    	 
    
    
/**OnCreate over**/    
} 

    /** 快门回调接口  **/
    private ShutterCallback mShutterCallback=new ShutterCallback()
    {
        @Override
        public void onShutter() 
        {
            mPlayer=new MediaPlayer();
            mPlayer=MediaPlayer.create(StartActivity.this,R.raw.shutter);
            mPlayer.start();
        }
    };
     
    
    
    /** 拍照回调接口  **/
    private PictureCallback mPicureCallback=new PictureCallback()
    {
        @Override
        public void onPictureTaken(byte[] mData, Camera camera) 
        {
            File mPictureFile = StorageHelper.getOutputFile(StorageHelper.MEDIA_TYPE_IMAGE); 
            if (mPictureFile == null){  
                return; 
            }
            try { 
                /** 存储照片  **/
                FileOutputStream fos = new FileOutputStream(mPictureFile); 
                fos.write(mData); 
                fos.close(); 
                /** 设置缩略图  **/
                Bitmap mBitmap=BitmapFactory.decodeByteArray(mData, 0, mData.length);
                ThumbsView.setImageBitmap(mBitmap);
                /** 获取缩略图Uri  **/
                mUri=StorageHelper.getOutputUri(mPictureFile);
                /**停止预览**/
                mCamera.stopPreview();
                /**开始预览**/
                mCamera.startPreview();
            } catch (FileNotFoundException e) { 
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
     
     
    /** 官方建议的安全地访问摄像头的方法  **/
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
     
    /** 检查设备是否支持摄像头  **/
    private boolean CheckCameraHardware(Context mContext)
    {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        { 
            // 摄像头存在 
            return true; 
        } else { 
            // 摄像头不存在 
            return false; 
        } 
    }
     
    @Override
    protected void onPause() {
        super.onPause();
        if(mCamera!=null)
        {
            mCamera.release();
            mCamera=null;
        }
    }
 
    @Override
    protected void onResume() 
    {
        super.onResume();
        if(mCamera==null)
        {
            mCamera = getCameraInstance();
            mCamera.startPreview(); 
        }
        
    }

   
}
