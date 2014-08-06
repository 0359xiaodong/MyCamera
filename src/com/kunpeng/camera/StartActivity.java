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
 * 1.ʵ����������岼��
 * 2.���չ����Լ�Ԥ�������ܵ�ʵ��
 * @author magiwen
 *
 */
public class StartActivity extends Activity {
	
	
	
	//private static final String TAG = "MyLogCat";
	private static final String TAG = "TAG-AndroidCameraActivity";  
    
    public static final int MEDIA_TYPE_IMAGE = 1;   
    public static final int MEDIA_TYPE_VIDEO = 2;   

	 /** ���   **/
    public static Camera mCamera;  
    /** Ԥ������  **/
    public static CameraPreview mPreview;
    /** ����ͼ  **/
    ImageView ThumbsView;
    /** ��ǰ����ͼλ�ã�Ĭ��Ϊ�ֻ����λ����ʱû������ͼ **/
    private Uri mUri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
   /**���������**/
    private FrameLayout mFrameLayout;
    /** MediaPlayer **/
    private MediaPlayer mPlayer;
    /** ���հ�ť  **/
    Button BtnCapture;
    /**װ��Ʒ��ť**/
    private Button Decorate;
    /**��Ƶ���չ���ת����ť**/
    private ImageView video;
    /**�ع�ȵ���**/
    private Button exposure;
    /**����ư�ť**/
    private ImageView flash;
    /**�ж�����ƿ���**/
    public static boolean  flash_open=true;
    Property test01=new Property();
    /**�������**/
    private Camera.Parameters parameters ;
    /**¼���**/
    public static MediaRecorder mMediaRecorder;
    private videoActivity _video;
    /**¼��ʼ���**/
    private boolean isRecording = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** ���ر�����  **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /** ����״̬��  **/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /** ��������**/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.test01);
         System.out.println("\n");
        /** Ӳ�����  **/
        if(CheckCameraHardware(this)==false)
        {
            Toast.makeText(this, "�ܱ�Ǹ�������豸���ܲ�֧������ͷ���ܣ�", Toast.LENGTH_SHORT).show();
            return;
        }
         
        /** ��ȡ���  **/
        mCamera=getCameraInstance();
        /** ��ȡԤ������   **/
        mPreview = new CameraPreview(this, mCamera); 
        mFrameLayout= (FrameLayout)findViewById(R.id.PreviewView); 
        mFrameLayout.addView(mPreview); 
        mCamera.startPreview();
        
        /**����ƹ��ܵ�ʵ��
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
				/*���������򿪣������رգ������ͼƬ��Ϊ�ر�ͼƬ*/
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

       
        /**���հ�ť����ʵ��
         * date 2014.8.4
         * **/
        BtnCapture= (Button) findViewById(R.id.takePhoto);
        BtnCapture.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) 
            {
                /** ʹ��takePicture()�����������  **/
            	
                mCamera.autoFocus(new AutoFocusCallback()
                {
                	
                    /** �Զ��۽��۽����������  **/
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
         
        
        
        /** �������ͼʵ��  **/
         
        ThumbsView = (ImageView)findViewById(R.id.ThumbsView);
        
        ThumbsView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) 
            {
                /** ʹ��Uri���ʵ�ǰ����ͼ  **/
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
        
        /**װ��Ʒ��ť����ʵ��**///δ���������
        /*Decorate=(Button)findViewById(R.id.Decorate);
        Decorate.setOnClickListener(new OnClickLister(){
        	 public void onClick(View v) 
             {
              
        }
        });*/
        /**��Ƶ����ת��**/
     video=(ImageView) findViewById(R.id.ChangeCamera);
     video.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {  
            if (isRecording) {  
                // ֹͣ¼���ͷ�camera  
                mMediaRecorder.stop();   
                _video.releaseMediaRecorder();   

                mCamera.lock();   

                // ֪ͨ�û�¼����ֹͣ  
                /*video.setText("��ʼ¼��");*/  

                isRecording = false;  
            } else {  
                // ��ʼ����Ƶcamera  
                if (_video.prepareVideoRecorder()) {  
                    mMediaRecorder.start();  

                    // ֪ͨ�û�¼���ѿ�ʼ   
                    /*video.setText("ֹͣ¼��"); */ 

                    isRecording = true;  
                } else {  
                    // ׼��δ����ɣ��ͷ�camera  
                    _video.releaseMediaRecorder();  
                }  
            }  
        }  
    });  
		    	 
    
    
/**OnCreate over**/    
} 

    /** ���Żص��ӿ�  **/
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
     
    
    
    /** ���ջص��ӿ�  **/
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
                /** �洢��Ƭ  **/
                FileOutputStream fos = new FileOutputStream(mPictureFile); 
                fos.write(mData); 
                fos.close(); 
                /** ��������ͼ  **/
                Bitmap mBitmap=BitmapFactory.decodeByteArray(mData, 0, mData.length);
                ThumbsView.setImageBitmap(mBitmap);
                /** ��ȡ����ͼUri  **/
                mUri=StorageHelper.getOutputUri(mPictureFile);
                /**ֹͣԤ��**/
                mCamera.stopPreview();
                /**��ʼԤ��**/
                mCamera.startPreview();
            } catch (FileNotFoundException e) { 
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
     
     
    /** �ٷ�����İ�ȫ�ط�������ͷ�ķ���  **/
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
     
    /** ����豸�Ƿ�֧������ͷ  **/
    private boolean CheckCameraHardware(Context mContext)
    {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        { 
            // ����ͷ���� 
            return true; 
        } else { 
            // ����ͷ������ 
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
