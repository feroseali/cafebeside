
package com.team.cafebeside.screenMappers;

import com.team.cafebeside.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author Little Adam
 *
 */
public class SplashActivity extends Activity {
	private int delay = 2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// For Splash
        new Handler().postDelayed(new Runnable(){
		 public void run() {
             Intent mainIntent = new Intent(SplashActivity.this,HomeActivity.class);
             SplashActivity.this.startActivity(mainIntent);
             SplashActivity.this.finish();
         }
     }, delay);
	}	
	
}
