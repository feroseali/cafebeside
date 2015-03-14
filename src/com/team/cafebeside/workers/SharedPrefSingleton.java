package com.team.cafebeside.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPrefSingleton{
	
	private static SharedPrefSingleton mInstance;
	private Context mContext;
	private SharedPreferences myPreference;
	
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
	}
	
	public void writePreference(String key, boolean value){
	     Editor mEditor = myPreference.edit();
	     mEditor.putBoolean(key, value);
	     mEditor.commit();
	}
	
	public boolean getLoggedInPreference(String key){
		return myPreference.getBoolean(key, false);
	}

}
