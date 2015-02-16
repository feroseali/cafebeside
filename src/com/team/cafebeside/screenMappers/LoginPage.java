package com.team.cafebeside.screenMappers;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.team.cafebeside.R;
import com.team.cafebeside.networkEngine.PostMan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
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
	private final String DB_NAME = "ferose_cafebeside.db";
	private SQLiteDatabase db = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS LOGIN(NAME VARCHAR(50),EMAIL VARCHAR(30),PHONE VARCHAR(15),PASSWORD VARCHAR(15),PRIMARY KEY(EMAIL))");
		db.execSQL("delete from LOGIN");
		
		username = (EditText) findViewById(R.id.etUserName);
		userpass = (EditText) findViewById(R.id.etPass);

		//Login Button & Action
		mSignIn	=	(Button) findViewById(R.id.btnSingIn);
		//final Intent mHome	=	new Intent(this,HomeActivity.class);
		mSignIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
		final JSONObject mJson = new JSONObject();
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(LoginPage.this);
            progress.setTitle("Processing");
            progress.setMessage("Wait while loading...");
            progress.setIndeterminate(false);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... args) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					usermail = username.getText().toString();
					upassword = userpass.getText().toString();

					if (usermail.matches("") || upassword.matches("")) {
						Toast.makeText(getBaseContext(), "Please fill the fileds first!",Toast.LENGTH_LONG).show();
		        		showAlert("Alert","Invalid UserName or Password");

					} else {

						try {
			        		mJson.put("username", usermail);
			        		mJson.put("password",upassword);

							final String json_string = mJson.toString();

							//PostMan mSender = new PostMan();

							Thread thread = new Thread(new Runnable() {
								@Override
								public void run() {
									try {
										// Your code goes here
										PostMan mSender = new PostMan();
										response = mSender.post(json_string,"http://feroseali.in/cafebeside/login.php");

										if (!response.contains("Failed")) {

											try {

												JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
												String mName = object
														.getString("name");
												String mEmail = object
														.getString("email");
												String mPhn = object
														.getString("phone");
												String mPassword = object
														.getString("password");

												mName = DatabaseUtils
														.sqlEscapeString(mName);
												mEmail = DatabaseUtils
														.sqlEscapeString(mEmail);
												mPhn = DatabaseUtils
														.sqlEscapeString(mPhn);
												mPassword = DatabaseUtils
														.sqlEscapeString(mPassword);

												db.execSQL("INSERT INTO LOGIN VALUES("
														+ mName
														+ ","
														+ mEmail
														+ ","
														+ mPhn
														+ ","
														+ mPassword + ")");
												Intent intent1 = new Intent(getApplicationContext(),HomeActivity.class);
												startActivity(intent1);
												finish();

											} catch (Exception e) {
												Toast.makeText(
														getBaseContext(),
														"Check Internet Availability",
														Toast.LENGTH_SHORT)
														.show();
											}

										} else {
											Toast.makeText(getBaseContext(),
													"Login Failed",
													Toast.LENGTH_LONG).show();
											username.setText("");
											userpass.setText("");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});

							thread.start();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}
			});
			return null;  

        }

        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            //progress.setProgress(Integer.parseInt(progress[0]));
        }
        protected void onPostExecute(String fserve) {
        	Log.e("response",fserve);
            progress.dismiss();
    
            
        }

    }
	
	
	
	
}
