package com.team.cafebeside.screenMappers;

import com.team.cafebeside.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MyBills extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bills_layout);
		
        Toast.makeText(getApplicationContext(), "Now you can see your bills here!", Toast.LENGTH_LONG).show();

	}
}
