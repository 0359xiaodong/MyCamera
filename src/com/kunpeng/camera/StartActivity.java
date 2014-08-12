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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
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
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class StartActivity extends Activity {
	
	
	public static int cameraPosition;
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
    /**前置后置摄像头**/
    private ImageView exchange;
    private boolean IsBack=true;
    /**装饰品按钮**/
    private Button Decorate;
    /**视频拍照功能转换按钮**/
    private Button video;
    /**曝光度调整**/
    private Button exposure;
    
    
    //private SurfaceView mSurfaceView;
    /**闪光灯按钮**/
    private ImageView flash;
    /**判断闪光灯开关**/
    public  static boolean  flash_open=true;
    public  Property test1=new Property(StartActivity.this);
    /**相机参数**/
    private Camera.Parameters parameters ;
    /**录像机**/
    public static MediaRecorder mMediaRecorder;
   // private videoActivity _video=new videoActivity();
    /**录像开始与否**/
    private boolean isRecording = false;
    //数据库
    public static SQLiteDatabase db = null;
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
        /** 硬件检查  **/
        if(CheckCameraHardware(this)==false)
        {
            Toast.makeText(this, "很抱歉，您的设备可能不支持摄像头功能！", Toast.LENGTH_SHORT).show();
            return;
        }
      /**获取用户配置**/
      //flash_open=test1.init(StartActivity.this);
      Log.v("配置信息",""+flash_open);
       /**获取相机**/
       mCamera=getCameraInstance(findBackCamera()); 
       /** 获取预览界面  **/
       mCamera.startPreview();
      mFrameLayout= (FrameLayout)findViewById(R.id.PreviewView); 
     // mSurfaceView=(SurfaceView)findViewById(R.id.svCameraPreview);
      mPreview= new CameraPreview(this, mCamera);
      mFrameLayout.addView(mPreview);
      
    
      
      
      
       /**前后摄像头转换**/
       exchange=(ImageView)findViewById(R.id.ChangeCamera);
       exchange.setOnClickListener(new OnClickListener(){
    	   
 		@Override
 		public void onClick(View v) {
 			// TODO Auto-generated method stub
 			mCamera.stopPreview();//停掉原来摄像头的预览
            mCamera.release();//释放资源
            mCamera = null;//取消原来摄像头   
            mFrameLayout.removeView(mPreview);//清除预览区
            if(Camera.getNumberOfCameras()==2&&IsBack)
 			{
 				
            	cameraPosition=findFrontCamera();
 				if(cameraPosition==-1)
 				{
 					cameraPosition=findBackCamera();
 				}
 				mCamera=getCameraInstance(cameraPosition);
 				IsBack=false;
 				//mPreview = new CameraPreview(StartActivity.this, mCamera);        
 				}
 			else if(Camera.getNumberOfCameras()==2&&!IsBack)
 			{
 				cameraPosition=findBackCamera();
 				mCamera=getCameraInstance(cameraPosition);
 				IsBack=true;
 				
				  
 			}
 			else if(Camera.getNumberOfCameras()==1)
 			{
 				cameraPosition=findBackCamera();
 				mCamera=getCameraInstance(cameraPosition);
 				IsBack=true;
 				  
 			}
            mCamera.startPreview();
            mPreview = new CameraPreview(StartActivity.this, mCamera); 
 			//mFrameLayout= (FrameLayout)findViewById(R.id.PreviewView);
 			mFrameLayout.addView(mPreview); 
 			
 		}
 		
 			
         });
      
       
        /**数据库的调用**/
       /* databaseHelper database = new databaseHelper(StartActivity.this);//这段代码放到Activity类中才用this 
        
        db = database.getReadableDatabase();
        ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名 
        cv.put("flashopen",""+flash_open);
        cv.put("user", "");//添加密码 
        db.insert("user",null,cv);//执行插入操作
        Cursor c = db.query("user",null,null,null,null,null,null);//查询并获得游标 
        if(c.moveToFirst()){//判断游标是否为空 
            for(int i=0;i<c.getCount();i++){ 
                c.move(i);//移动到指定记录 
                //String username = c.getString(c.getColumnIndex("user");                
            } 
            
        }
        String nowState = c.getString(c.getColumnIndex("flashopen")); 
        Boolean flashtmp=new Boolean(nowState);
        flash_open=flashtmp;*/
        
        /**闪光灯功能的实现
         * @date 2014.8.5
         * **/
        flash=(ImageView)findViewById(R.id.flashImage);
        flash.setOnClickListener(new OnClickListener(){  	
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
				
			/*如果摄像机打开，点击后关闭，闪光灯图片变为关闭图片*/
			if(flash_open==true)
			{
				parameters= mCamera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(parameters);
				flash_open=false;
				test1.writeDate(StartActivity.this, flash_open);
			}
				
			else
			{
				parameters= mCamera.getParameters();
            	parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
				mCamera.setParameters(parameters);
				flash_open=true;
				test1.writeDate(StartActivity.this, flash_open);
			}
			
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
                        if(isSuccess&&camera!=null&&IsBack)
                        {
                        		
                        		if(flash_open==true)
                				{
                        			parameters= mCamera.getParameters();
                					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                					mCamera.setParameters(parameters);
                				}
                				
                				else
                				{
                					parameters= mCamera.getParameters();
                            		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                					mCamera.setParameters(parameters);
                				}
                        		mCamera.takePicture(mShutterCallback, null, mPicureCallback);
                        }
                        else
                        {
                        	parameters= mCamera.getParameters();
                    		parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        					mCamera.setParameters(parameters);
        					mCamera.takePicture(null, null, mPicureCallback);
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
        /**视频录像**/
     video=(Button) findViewById(R.id._videoP);
     video.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View v) {  
			Intent intent=new Intent(StartActivity.this,videoActivity.class);
			//intent.setClass(StartActivity.this,videoActivity.class);
			startActivity(intent);
		}
    }); 
     /**曝光度调整**/
     exposure=(Button)findViewById(R.id.setting);
     exposure.setOnClickListener(new OnClickListener()
     {
     @Override
     public void onClick(View v)
     {
    	 
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
    public static Camera getCameraInstance(int Cameraid){ 
        Camera c = null; 
        try { 
            c = Camera.open(Cameraid);
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
     protected void onStart()
     {
    	 super.onStart();
    	 flash_open=test1.init(StartActivity.this);
     }
     @Override
     protected void onRestart()
     {
    	 super.onRestart();
    	 super.onResume();
         if(mCamera==null)
         {
         	
         	 mCamera=getCameraInstance(findBackCamera()); //cameraPosition
         	 mCamera.startPreview();
         	 mPreview = new CameraPreview(StartActivity.this, mCamera); 
    		 mFrameLayout.addView(mPreview);
    
         }
     }
    @Override
    protected void onPause() {
        super.onPause();
        if(mCamera!=null)
        {
            mCamera.stopPreview();//停掉原来摄像头的预览
            mCamera.release();//释放资源
            mCamera = null;//取消原来摄像头 
            mFrameLayout.removeView(mPreview);//清除预览区
        }
        test1.writeDate(StartActivity.this, flash_open);
    }
 @Override
 protected void onStop()
 {
	 super.onStop();
	 test1.writeDate(StartActivity.this, flash_open);
	 if(mCamera!=null)
     {
         mCamera.stopPreview();//停掉原来摄像头的预览
         mCamera.release();//释放资源
         mCamera = null;//取消原来摄像头 
         mFrameLayout.removeView(mPreview);//清除预览区
     }
     test1.writeDate(StartActivity.this, flash_open);
	 
 }
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		test1.writeDate(StartActivity.this, flash_open);
	}
	@Override
    protected void onResume() 
    {
        super.onResume();
        if(mCamera==null)
        {
        	
        	 mCamera=getCameraInstance(findBackCamera()); //cameraPosition
        	 mCamera.startPreview();
        	 mPreview = new CameraPreview(StartActivity.this, mCamera); 
   			 mFrameLayout.addView(mPreview);
        	
             
        }
        //test1.writeDate(StartActivity.this, flash_open);
    }
  
	private int findFrontCamera()
	{
		
		int cameraCount=0;
		Camera.CameraInfo camerainfo=new Camera.CameraInfo();
		cameraCount=Camera.getNumberOfCameras();
		for(int cameraId=0;cameraId<cameraCount;cameraId++)
		{
			Camera.getCameraInfo(cameraId,camerainfo);
			if(camerainfo.facing==Camera.CameraInfo.CAMERA_FACING_FRONT)
				return cameraId;
		}
		return -1;
	}
   
	private int findBackCamera()
	{
		
		int cameraCount=0;
		Camera.CameraInfo camerainfo=new Camera.CameraInfo();
		cameraCount=Camera.getNumberOfCameras();
		for(int cameraId=0;cameraId<cameraCount;cameraId++)
		{
			Camera.getCameraInfo(cameraId,camerainfo);
			if(camerainfo.facing==Camera.CameraInfo.CAMERA_FACING_BACK)
				return cameraId;
		}
		return -1;
	}
	//添加菜单选项
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.start, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int item_id = item.getItemId();
		switch(item_id)
		{
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setClass(StartActivity.this,Decoration.class);
			startActivity(intent);
			StartActivity.this.finish();
			break;
		case R.id.exit:
			StartActivity.this.finish();
			break;
		}
		return true;
	}
	/*public SurfaceView getmSurfaceView() {
		return mSurfaceView;
	}
	public void setmSurfaceView(SurfaceView mSurfaceView) {
		this.mSurfaceView = mSurfaceView;
	}*/
}
