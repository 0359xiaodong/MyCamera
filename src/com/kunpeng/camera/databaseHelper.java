package com.kunpeng.camera;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper{
	
	private static final String DB_NAME = "mydata.db"; //���ݿ����� 
    private static final int version = 1; //���ݿ�汾 
    
    public databaseHelper(Context context) { 
        super(context, DB_NAME, null, version); 
        // TODO Auto-generated constructor stub 
        String sql = "create table user(username varchar(20) not null , password varchar(60) not null );";
		
        StartActivity.db.execSQL(sql); 
    }

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		String sql = "create table user(username varchar(20) not null , password varchar(60) not null );";
		
        StartActivity.db.execSQL(sql); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	} 
  
   

}
