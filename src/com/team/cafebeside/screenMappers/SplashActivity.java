package com.team.cafebeside.screenMappers;

import com.team.cafebeside.R;
import com.team.cafebeside.workers.CafeNetworkValidator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
		CafeNetworkValidator validator = new CafeNetworkValidator();
		boolean isConnected = validator.isConnectedToCafeBeside(this);
		if (isConnected) {

			// For Splash
			new Handler().postDelayed(new Runnable() {
				public void run() {
					Intent mainIntent = new Intent(SplashActivity.this,
							HomeActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}
			}, delay);
		}else{
			showAlert();
		}
	}

	private void showAlert() {
		new AlertDialog.Builder(this)
				.setTitle("Alert")
				.setMessage(
						"You are not connected to CafeBeside Network !\n Exiting ..")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// DO ANY CAFEBESIDE OPERATION
								System.exit(0);
							}
						})

				.setIcon(android.R.drawable.ic_dialog_alert).show();
	}
}
