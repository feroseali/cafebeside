package com.team.cafebeside;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SpalshActivity extends Activity {
	private int count = 2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// For Splash
        new Handler().postDelayed(new Runnable(){
		 public void run() {
             Intent mainIntent = new Intent(SpalshActivity.this,HomeActivity.class);
             SpalshActivity.this.startActivity(mainIntent);
             SpalshActivity.this.finish();
         }
     }, count);
	}	
	
}
