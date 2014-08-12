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
 * 1.ʵ����������岼��
 * 2.���չ����Լ�Ԥ�������ܵ�ʵ��
 * @author magiwen
 *
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class StartActivity extends Activity {
	
	
	public static int cameraPosition;
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
    /**ǰ�ú�������ͷ**/
    private ImageView exchange;
    private boolean IsBack=true;
    /**װ��Ʒ��ť**/
    private Button Decorate;
    /**��Ƶ���չ���ת����ť**/
    private Button video;
    /**�ع�ȵ���**/
    private Button exposure;
    
    
    //private SurfaceView mSurfaceView;
    /**����ư�ť**/
    private ImageView flash;
    /**�ж�����ƿ���**/
    public  static boolean  flash_open=true;
    public  Property test1=new Property(StartActivity.this);
    /**�������**/
    private Camera.Parameters parameters ;
    /**¼���**/
    public static MediaRecorder mMediaRecorder;
   // private videoActivity _video=new videoActivity();
    /**¼��ʼ���**/
    private boolean isRecording = false;
    //���ݿ�
    public static SQLiteDatabase db = null;
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
        /** Ӳ�����  **/
        if(CheckCameraHardware(this)==false)
        {
            Toast.makeText(this, "�ܱ�Ǹ�������豸���ܲ�֧������ͷ���ܣ�", Toast.LENGTH_SHORT).show();
            return;
        }
      /**��ȡ�û�����**/
      //flash_open=test1.init(StartActivity.this);
      Log.v("������Ϣ",""+flash_open);
       /**��ȡ���**/
       mCamera=getCameraInstance(findBackCamera()); 
       /** ��ȡԤ������  **/
       mCamera.startPreview();
      mFrameLayout= (FrameLayout)findViewById(R.id.PreviewView); 
     // mSurfaceView=(SurfaceView)findViewById(R.id.svCameraPreview);
      mPreview= new CameraPreview(this, mCamera);
      mFrameLayout.addView(mPreview);
      
    
      
      
      
       /**ǰ������ͷת��**/
       exchange=(ImageView)findViewById(R.id.ChangeCamera);
       exchange.setOnClickListener(new OnClickListener(){
    	   
 		@Override
 		public void onClick(View v) {
 			// TODO Auto-generated method stub
 			mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
            mCamera.release();//�ͷ���Դ
            mCamera = null;//ȡ��ԭ������ͷ   
            mFrameLayout.removeView(mPreview);//���Ԥ����
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
      
       
        /**���ݿ�ĵ���**/
       /* databaseHelper database = new databaseHelper(StartActivity.this);//��δ���ŵ�Activity���в���this 
        
        db = database.getReadableDatabase();
        ContentValues cv = new ContentValues();//ʵ����һ��ContentValues����װ�ش����������cv.put("username","Jack Johnson");//����û��� 
        cv.put("flashopen",""+flash_open);
        cv.put("user", "");//������� 
        db.insert("user",null,cv);//ִ�в������
        Cursor c = db.query("user",null,null,null,null,null,null);//��ѯ������α� 
        if(c.moveToFirst()){//�ж��α��Ƿ�Ϊ�� 
            for(int i=0;i<c.getCount();i++){ 
                c.move(i);//�ƶ���ָ����¼ 
                //String username = c.getString(c.getColumnIndex("user");                
            } 
            
        }
        String nowState = c.getString(c.getColumnIndex("flashopen")); 
        Boolean flashtmp=new Boolean(nowState);
        flash_open=flashtmp;*/
        
        /**����ƹ��ܵ�ʵ��
         * @date 2014.8.5
         * **/
        flash=(ImageView)findViewById(R.id.flashImage);
        flash.setOnClickListener(new OnClickListener(){  	
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
				
			/*���������򿪣������رգ������ͼƬ��Ϊ�ر�ͼƬ*/
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
        /**��Ƶ¼��**/
     video=(Button) findViewById(R.id._videoP);
     video.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View v) {  
			Intent intent=new Intent(StartActivity.this,videoActivity.class);
			//intent.setClass(StartActivity.this,videoActivity.class);
			startActivity(intent);
		}
    }); 
     /**�ع�ȵ���**/
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
            mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
            mCamera.release();//�ͷ���Դ
            mCamera = null;//ȡ��ԭ������ͷ 
            mFrameLayout.removeView(mPreview);//���Ԥ����
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
         mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
         mCamera.release();//�ͷ���Դ
         mCamera = null;//ȡ��ԭ������ͷ 
         mFrameLayout.removeView(mPreview);//���Ԥ����
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
	//��Ӳ˵�ѡ��
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
