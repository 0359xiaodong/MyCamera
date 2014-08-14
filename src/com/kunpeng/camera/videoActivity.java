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

    private Button start;// ��ʼ¼�ư�ť 

    private MediaRecorder mediarecorder;// ¼����Ƶ���� 

    private SurfaceView surfaceview;// ��ʾ��Ƶ�Ŀؼ� 

    private RelativeLayout mRelativeLayout;

    private SurfaceHolder surfaceHolder; 

    private boolean isRecording=false;

    public void onCreate(Bundle savedInstanceState) { 

        super.onCreate(savedInstanceState); 

        requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ�������� 

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 

                WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ȫ�� 

        // ���ú�����ʾ 

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 

        // ѡ��֧�ְ�͸��ģʽ 

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

        SurfaceHolder holder = surfaceview.getHolder();// ȡ��holder 

        holder.addCallback(this); // holder����ص��ӿ� 

        // setType�������ã�Ҫ������. 

        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 

    } 

 

    class TestVideoListener implements OnClickListener { 

 

        @Override 

        public void onClick(View v) { 

            if (!isRecording) { 

            	start.setBackgroundResource(R.drawable.menu_button_sensor_on);
            	
            	isRecording = true;
                mediarecorder = new MediaRecorder();// ����mediarecorder���� 

                // ����¼����ƵԴΪCamera(���) 

                mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); 

                // ����¼����ɺ���Ƶ�ķ�װ��ʽTHREE_GPPΪ3gp.MPEG_4Ϊmp4 

                mediarecorder 

                        .setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); 

                // ����¼�Ƶ���Ƶ����h263 h264 

                mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP/*.VideoEncoder.H264*/); 
/*
                // ������Ƶ¼�Ƶķֱ��ʡ�

                mediarecorder.setVideoSize(176, 144); 

                // ����¼�Ƶ���Ƶ֡�ʡ�

                mediarecorder.setVideoFrameRate(20); */

                mediarecorder.setPreviewDisplay(surfaceHolder.getSurface()); 

                // ������Ƶ�ļ������·�� 

                mediarecorder.setOutputFile("/sdcard/love.3gp"); 

                try { 

                    // ׼��¼�� 

                    mediarecorder.prepare(); 

                    // ��ʼ¼�� 

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

                    // ֹͣ¼�� 

                    mediarecorder.stop(); 

                    // �ͷ���Դ 

                    mediarecorder.release(); 

                    mediarecorder = null; 

                } 

            } 

 

        } 

 

    } 

 

    @Override 

    public void surfaceChanged(SurfaceHolder holder, int format, int width, 

            int height) { 

        // ��holder�����holderΪ��ʼ��oncreat����ȡ�õ�holder����������surfaceHolder 

        surfaceHolder = holder; 

    } 

 

    @Override 

    public void surfaceCreated(SurfaceHolder holder) { 

        // ��holder�����holderΪ��ʼ��oncreat����ȡ�õ�holder����������surfaceHolder 

        surfaceHolder = holder; 

    } 

 

    
    @Override 

    public void surfaceDestroyed(SurfaceHolder holder) { 

        // surfaceDestroyed��ʱ��ͬʱ��������Ϊnull 

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

	      SurfaceHolder holder = surfaceview.getHolder();// ȡ��holder 

	      holder.addCallback(this); // holder����ص��ӿ� 

	        // setType��������

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