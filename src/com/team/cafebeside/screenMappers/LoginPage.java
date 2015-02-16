package com.team.cafebeside.screenMappers;

import org.json.JSONObject;

import com.team.cafebeside.R;
import com.team.cafebeside.configs.Configuration;
import com.team.cafebeside.networkEngine.HttpRequestWorker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends Activity{
	public ProgressDialog progress;
	private EditText username,userpass;
	public static String usermail = null;
	public static String upassword = null;
	private Button mSignIn;			// Login button
	private Button mSignUp;			// Register Button
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		username = (EditText) findViewById(R.id.etUserName);
		userpass = (EditText) findViewById(R.id.etPass);

		//Login Button & Action
		mSignIn	=	(Button) findViewById(R.id.btnSingIn);
		//final Intent mHome	=	new Intent(this,HomeActivity.class);
		mSignIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				usermail = username.getText().toString();
				upassword = userpass.getText().toString();
				if(!usermail.equals("") && !upassword.equals("")){

					new AttemptLogin().execute();
				}
				else{
					Toast.makeText(getApplicationContext(), "Please enter your login details.",Toast.LENGTH_LONG).show();
				}

					//Log.e("","");
					new AttemptLogin().execute();
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
	
	private void showAlert(String title,String message){
		new AlertDialog.Builder(this)
	    .setTitle(title)
	    .setMessage(message)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            //DO ANY CAFEBESIDE OPERATION
	        }
	     })
	    
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
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
	
	

	/*********async task *************/
	
	class AttemptLogin extends AsyncTask<String, String, String> {


        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(getBaseContext(), "REACHED", Toast.LENGTH_LONG);


            progress = new ProgressDialog(LoginPage.this);
            progress.setTitle("Processing");
            progress.setMessage("Wait while loading...");
            progress.setIndeterminate(false);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... args) {

        	try{
            	JSONObject mObject = new JSONObject();
            	mObject.put("username", username);
            	mObject.put("password",userpass);
            	
            	HttpRequestWorker mWorker = new HttpRequestWorker();

            	 response = mWorker.PostRequest("http://192.168.0.2/cafebeside/login.php", mObject.toString(), false);
   
            	 return response;
            	
                }catch(Exception e){
                	return null;
                }

            

        }

        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            //progress.setProgress(Integer.parseInt(progress[0]));
        }
        protected void onPostExecute(String fserve) {
        	Log.e("response",fserve);
            progress.dismiss();

            if(response.equals("Success")){
            	Configuration.USERNAME	=	usermail;
        		Intent mBeside = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(mBeside);
        	}else{

        		Intent mBeside = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(mBeside);
        		showAlert("Alert","Invalid UserName or Password");
        	}
        }

    }
	
	
	
	
}
