package com.kunpeng.camera;


/**
 * ���޸ģ��ύ�޸�֮��û�д洢��û�ҵ�����
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class Property {
	private static  String USERINFO;
	private String TAG="error";

	public Property(Context activity)
	{
		USERINFO="userinfo";
		
	}
	public void writeDate(Context activity,boolean flash_open)
	{
		try{ 
			SharedPreferences perference = activity.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
            Editor editor = perference.edit(); 
            editor.putBoolean("flashopen", flash_open);
            editor.commit();//δ����commitǰ������ʵ����û�д洢���ļ��е�???
           
        }catch(Exception e){ 
            Log.v(TAG,"�ύʧ��");
        } 
	}
	/*��һ�δ�Ӧ��ʱ����ȡ�����û���Ϣ����*/ 
    public boolean init(Context activity){ 
    	SharedPreferences perference = activity.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        boolean flash_open=perference.getBoolean("flashopen",false);
        return flash_open;
    } 
} 

