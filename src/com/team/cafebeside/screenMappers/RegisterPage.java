package com.team.cafebeside.screenMappers;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.team.cafebeside.R;

public class RegisterPage extends Activity {
	private String RegArray[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reg);
		
		Toast.makeText(getApplicationContext(), "Sign up your account",Toast.LENGTH_LONG ).show();
		
		Button submit_button = (Button) findViewById(R.id.signup);
	
		submit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				LinearLayout lnm = (LinearLayout) findViewById(R.id.reg_form);
				
				int childcount = lnm.getChildCount();
				// TODO Auto-generated method stub
				for (int i=0; i < childcount-1; i++){
					EditText Ev = (EditText) lnm.getChildAt(i);
					//Log.i("val", Ev.getText().toString());
				    RegArray[i]= Ev.getText().toString(); 
				   // do whatever you would want to do with this View
				}
				
/*				Gson gson = new Gson();
				gson.toJson(RegArray);*/
				/*final JSONObject mJson = new JSONObject();

				try {

					mJson.put("User Name", RegArray[0]);
					mJson.put("Email",RegArray[1]);
					mJson.put("Mobile",RegArray[2]);
					mJson.put("Password",RegArray[3]);
					mJson.put("Confirm Password",RegArray[4]);

					String json_string=mJson.toString();

					String url="http://atharvacemp.in/android/register.php";
					// Posting Json String to Server
					DefaultHttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);
					StringEntity se;
					try {
						se = new StringEntity(json_string);
						se.setContentType("application/json;charset=UTF-8");
						se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
						httppost.setEntity(se);
						HttpResponse httpresponse = httpclient.execute(httppost);
						if(httpresponse){
							Toast.makeText(getApplicationContext(), "POST Success ", Toast.LENGTH_LONG).show();
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					 				

					

					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				for(int j=0;j<childcount-1;j++){
					Log.i("Register Form", RegArray[j]);
				}
			}
		});
		
	}
}