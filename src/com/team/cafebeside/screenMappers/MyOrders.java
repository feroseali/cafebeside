package com.team.cafebeside.screenMappers;

import com.team.cafebeside.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MyOrders extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders_layout);
		
        Toast.makeText(getApplicationContext(), "Now you can see your orders here!", Toast.LENGTH_LONG).show();

	}
}
