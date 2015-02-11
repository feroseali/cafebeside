package com.team.cafebeside.screenMappers;

import com.team.cafebeside.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginPage extends Activity{

	private Button mSignIn;			// Login button
	private Button mSignUp;			// Register Button
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//Login Button & Action
		mSignIn	=	(Button) findViewById(R.id.btnSingIn);
		final Intent mHome	=	new Intent(this,HomeActivity.class);
		mSignIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(mHome);
			}
		});
		
		//Register Button & Action
		mSignUp	= (Button) findViewById(R.id.btnSingUp);
		mSignUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mReg = new Intent(getApplicationContext(),RegisterPage.class);
				startActivity(mReg);
			}
		});
		
		
		
		
	}
}
