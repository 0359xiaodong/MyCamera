package com.kunpeng.camera;

import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class videoActivity extends Activity implements SurfaceHolder.Callback { 

    private Button start;// 开始录制按钮 

    private MediaRecorder mediarecorder;// 录制视频的类 

    private SurfaceView surfaceview;// 显示视频的控件 

    private RelativeLayout mRelativeLayout;

    private SurfaceHolder surfaceHolder; 

    private boolean isRecording=false;

    public void onCreate(Bundle savedInstanceState) { 

        super.onCreate(savedInstanceState); 

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏 

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 

                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏 

        // 设置横屏显示 

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 

        // 选择支持半透明模式 

        getWindow().setFormat(PixelFormat.TRANSLUCENT); 

        setContentView(R.layout.videoxml); 
  
        start = new Button(this);
        
        LayoutParams params2 = new RelativeLayout.LayoutParams(134,70);

        ((android.widget.RelativeLayout.LayoutParams) params2).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        
       /* ((android.widget.RelativeLayout.LayoutParams) params2).addRule(RelativeLayout.RIGHT_OF,R.id.flashImage);
        
        ((android.widget.RelativeLayout.LayoutParams) params2).addRule(RelativeLayout.ALIGN_TOP,R.id.flashImage);*/
        
        start.setBackgroundResource(R.drawable.menu_button_sensor_off);
        
        
        //exchange.setLayoutParams(Gravity.RIGHT);
        start.setLayoutParams(params2);
        
        mRelativeLayout=(RelativeLayout)findViewById(R.id.layout);
        mRelativeLayout.addView(start);

        init(); 

    } 

 

    private void init() { 

       // start = (Button) this.findViewById(R.id._videoP); 

        start.setOnClickListener(new TestVideoListener()); 

        surfaceview = (SurfaceView) this.findViewById(R.id.sur1); 

        SurfaceHolder holder = surfaceview.getHolder();// 取得holder 

        holder.addCallback(this); // holder加入回调接口 

        // setType必须设置，要不出错. 

        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 

    } 

 

    class TestVideoListener implements OnClickListener { 

 

        @Override 

        public void onClick(View v) { 

            if (!isRecording) { 

            	start.setBackgroundResource(R.drawable.menu_button_sensor_on);
            	
            	isRecording = true;
                mediarecorder = new MediaRecorder();// 创建mediarecorder对象 

                // 设置录制视频源为Camera(相机) 

                mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); 

                // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4 

                mediarecorder 

                        .setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); 

                // 设置录制的视频编码h263 h264 

                mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP/*.VideoEncoder.H264*/); 
/*
                // 设置视频录制的分辨率。

                mediarecorder.setVideoSize(176, 144); 

                // 设置录制的视频帧率。

                mediarecorder.setVideoFrameRate(20); */

                mediarecorder.setPreviewDisplay(surfaceHolder.getSurface()); 

                // 设置视频文件输出的路径 

                mediarecorder.setOutputFile("/sdcard/love.3gp"); 

                try { 

                    // 准备录制 

                    mediarecorder.prepare(); 

                    // 开始录制 

                    mediarecorder.start(); 

                } catch (IllegalStateException e) { 

                    // TODO Auto-generated catch block 

                    e.printStackTrace(); 

                } catch (IOException e) { 

                    // TODO Auto-generated catch block 

                    e.printStackTrace(); 

                } 

            } 

            else { 
            	start.setBackgroundResource(R.drawable.menu_button_sensor_off);
            	isRecording = false;
                if (mediarecorder != null) { 

                    // 停止录制 

                    mediarecorder.stop(); 

                    // 释放资源 

                    mediarecorder.release(); 

                    mediarecorder = null; 

                } 

            } 

 

        } 

 

    } 

 

    @Override 

    public void surfaceChanged(SurfaceHolder holder, int format, int width, 

            int height) { 

        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder 

        surfaceHolder = holder; 

    } 

 

    @Override 

    public void surfaceCreated(SurfaceHolder holder) { 

        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder 

        surfaceHolder = holder; 

    } 

 

    
    @Override 

    public void surfaceDestroyed(SurfaceHolder holder) { 

        // surfaceDestroyed的时候同时对象设置为null 

        surfaceview = null; 

        surfaceHolder = null; 

        mediarecorder = null; 

    }
@Override
    protected void onStop()
    {
    	super.onStop();
    	
    	surfaceview = null; 

    	if(mediarecorder!=null){
		surfaceHolder = null; 
		mediarecorder.release();
		mediarecorder = null;
    	}
    }
@Override
	protected void onPause()
	{
		super.onPause();
		surfaceview = null; 
		 //mediarecorder.release();
		if(mediarecorder!=null)
		{

		surfaceHolder = null; 
		mediarecorder.release();
		mediarecorder = null;
		}
	}
@Override
	protected void onResume()
	{
		super.onResume();
		  surfaceview = (SurfaceView) this.findViewById(R.id.sur1); 

	      SurfaceHolder holder = surfaceview.getHolder();// 取得holder 

	      holder.addCallback(this); // holder加入回调接口 

	        // setType必须设置

	      holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 surfaceview = null; 
		if(mediarecorder!=null)
		{

	     surfaceHolder = null; 
	     mediarecorder.release();
	     mediarecorder = null; 
		}
	} 
    

} 