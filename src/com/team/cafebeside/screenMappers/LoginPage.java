package com.team.cafebeside.screenMappers;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.team.cafebeside.R;
import com.team.cafebeside.configs.Configuration;
import com.team.cafebeside.configs.ServerConnector;
import com.team.cafebeside.networkEngine.AsyncResponse;
import com.team.cafebeside.networkEngine.AsyncWorker;
import com.team.cafebeside.networkEngine.HttpRequestWorker;
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

public class LoginPage extends Activity implements AsyncResponse {
	public ProgressDialog progress;
	private EditText username, userpass;
	public static String usermail = null;
	public static String upassword = null;
	private Button mSignIn; // Login button
	private Button mSignUp; // Register Button
	private final String DB_NAME = "ferose_cafebeside.db";
	private SQLiteDatabase db = null;
	private AsyncWorker mAsyncWorker = new AsyncWorker(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mAsyncWorker.delegate = this;
		setContentView(R.layout.activity_login);

		db = openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS LOGIN(NAME VARCHAR(50),EMAIL VARCHAR(30),PHONE VARCHAR(15),PASSWORD VARCHAR(15),PRIMARY KEY(EMAIL))");
		db.execSQL("delete from LOGIN");

		username = (EditText) findViewById(R.id.etUserName);
		userpass = (EditText) findViewById(R.id.etPass);

		// Login Button & Action
		mSignIn = (Button) findViewById(R.id.btnSingIn);
		// final Intent mHome = new Intent(this,HomeActivity.class);
		mSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// new AttemptLogin().execute();
				try {
					JSONObject mObject = new JSONObject();
					mObject.put("username", username.getText().toString());
					mObject.put("password", userpass.getText().toString());
					mAsyncWorker.execute(ServerConnector.LOGIN, mObject.toString());
				} catch (Exception ex) {

				}
				// new AsyncWorker(arg0.getContext()).execute();
			}

		});

		// Register Button & Action
		mSignUp = (Button) findViewById(R.id.btnSingUp);
		mSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mReg = new Intent(getApplicationContext(),
						RegisterPage.class);
				startActivity(mReg);
			}
		});

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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		new AlertDialog.Builder(this)
				.setTitle("Alert")
				.setMessage("Are you sure you want exit ?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								System.exit(0);
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();

	}

	@Override
	public void processFinish(String output) {
		// TODO Auto-generated method stub
		Log.e("OnActivity-return", "" + output);
		if(!output.equals("Success")){
			showAlert("Alert", "Login Failed");
		}
	}


}
