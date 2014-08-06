package com.kunpeng.camera;


/**
 * 待修改，提交修改之后没有存储，没找到错误
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class Property {
	private static  String USERINFO;
	private String TAG="error";
	public Property()
	{
		USERINFO="res.values.userinfo";
	}
	public void writeDate(Context activity,boolean flash_open)
	{
		try{ 
            SharedPreferences perference = activity.getSharedPreferences(USERINFO, 0);
            Editor editor = perference.edit(); 
            editor.putBoolean("flashopen", flash_open);
      
            editor.commit();//未调用commit前，数据实际是没有存储进文件中的???
             
            
             
        }catch(Exception e){ 
            Log.v(TAG,"提交失败");
        } 
	}
	/*第一次打开应用时，读取本地用户信息设置*/ 
    public boolean init(Context activity){ 
        SharedPreferences perference = activity.getSharedPreferences(USERINFO, 0); 
       /* nameEdit.setText(perference.getString("name", "")); 
        ageEdit.setText(perference.getString("age", "")); */
        boolean flash_open=perference.getBoolean("flashopen",true);
        return flash_open;
    } 
} 

