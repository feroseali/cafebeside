package com.team.cafebeside.screenMappers;

import com.team.cafebeside.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginPage extends Activity{

	private Button mSignIn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mSignIn	=	(Button) findViewById(R.id.btnSingIn);
		final Intent mHome	=	new Intent(this,HomeActivity.class);
		mSignIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(mHome);
			}
		});
	}
}
