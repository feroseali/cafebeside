package com.team.cafebeside.screenMappers;


import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team.cafebeside.R;
import com.team.cafebeside.configs.ServerConnector;
import com.team.cafebeside.networkEngine.AsyncResponse;
import com.team.cafebeside.networkEngine.AsyncWorker;

public class RegisterPage extends Activity implements AsyncResponse{
	
	public ProgressDialog progress;
	private EditText uname,uemail,umobile,upass,ucpass;
	private Button submit_button;
	public String unm,umail,umob,upwd,ucpwd;
	private AsyncWorker mAsyncWrkr = new AsyncWorker(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAsyncWrkr.delegate = this;		
		setContentView(R.layout.activity_reg);
		uname = (EditText) findViewById(R.id.uname);
		uemail = (EditText) findViewById(R.id.uemail);
		umobile = (EditText) findViewById(R.id.umobile);
		upass = (EditText) findViewById(R.id.upass);
		ucpass = (EditText) findViewById(R.id.ucpass);
		
		submit_button = (Button)findViewById(R.id.regbtn);
	
		submit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					unm = uname.getText().toString();
					umail = uemail.getText().toString();
					umob = umobile.getText().toString();
					upwd = upass.getText().toString();
					ucpwd = ucpass.getText().toString();

	 				if((unm.length()==0) || (umail.length()==0) || (umob.length()==0) || (upwd.length()==0) || (ucpwd.length()==0) ){			
						Toast.makeText(getApplicationContext(), "please enter something", Toast.LENGTH_LONG).show();
						Log.e("Async","Async Task Not Started");
					}
					else{		
						Log.e("Name",unm);
						Log.e("Mail",umail);
						Log.e("Mob",umob);
						Log.e("Pwd",ucpwd);
						Log.e("Start AsyncTask","Async Task Started now");
					JSONObject mObject = new JSONObject();
					mObject.put("username", uname.getText().toString());
					mObject.put("email", uemail.getText().toString());
					mObject.put("mobile", umobile.getText().toString());
					mObject.put("password", upass.getText().toString());
					mAsyncWrkr = new AsyncWorker(v.getContext());
					mAsyncWrkr.delegate=RegisterPage.this;
					mAsyncWrkr.execute(ServerConnector.REGISTER_CUSTOMER, mObject.toString());
					//finish();
					}
				} catch (Exception ex) {
				}		

			}
		});
		
	}
	
	 @Override
	    public void onBackPressed() {
	    	// TODO Auto-generated method stub
	    	//super.onBackPressed();
	    	new AlertDialog.Builder(this)
	        .setTitle("Alert")
	        .setMessage("Are you sure you want exit ?")
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                // continue with delete
	            	System.exit(0);
	            }
	         })
	        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                // do nothing
	            }
	         })
	        .setIcon(android.R.drawable.ic_dialog_alert)
	         .show();
	    	
	    }

		private void showAlert(String title, String message) {
			new AlertDialog.Builder(this)
					.setTitle(title)
					.setMessage(message)
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// DO ANY CAFEBESIDE OPERATION
								}
							})

					.setIcon(android.R.drawable.ic_dialog_alert).show();
		}
	 
	 
	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		Log.e("OnActivity-return", "" + output);
		if(!output.trim().equals("Success")){
			showAlert("Alert", "Registration Failed!");
			uname.setText("");
			uemail.setText("");
			umobile.setText("");
			upass.setText("");
			ucpass.setText("");
		}else{
			Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
			Intent loginIntent	=	new Intent(this,LoginPage.class);
			startActivity(loginIntent);
			finish();
		}		
	}
	 

}