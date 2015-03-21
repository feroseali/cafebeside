package com.team.cafebeside.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class SharedPrefSingleton{
	
	private static SharedPrefSingleton mInstance;
	private Context mContext;
	private SharedPreferences myPreference;
	private SharedPreferences _uPreference;
	/*private void SharedPrefSingleton() {
	}*/

	
	public static SharedPrefSingleton getInstance(){
		if(mInstance == null){
			mInstance	=	new SharedPrefSingleton();
		}
		return mInstance;
	}
	
	public void init(Context context){
		mContext	=	context;
		myPreference=	PreferenceManager.getDefaultSharedPreferences(mContext);
		_uPreference = PreferenceManager.getDefaultSharedPreferences(mContext);
	}
	
	public void writePreference(String key, boolean value){
	     Editor mEditor = myPreference.edit();
	     mEditor.putBoolean(key, value);
	     mEditor.commit();
	}
	
	public void writeSPreference(String key, String value){
		Log.d("key in shpreferecne",key);
		Log.d("value in shpreferecne",value);
	     Editor sEditor = _uPreference.edit();
	     sEditor.putString(key, value);
	     sEditor.commit();
	}
	
	public boolean getLoggedInPreference(String key){
		return myPreference.getBoolean(key, false);
	}

	public String getLoggedInUserPreference(String key){
		return _uPreference.getString(key, null);
	}
}
