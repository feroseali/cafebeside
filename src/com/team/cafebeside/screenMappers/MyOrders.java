package com.team.cafebeside.screenMappers;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.team.cafebeside.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MyOrders extends Activity {
	
	HttpClient httpclient;
	HttpGet request;
	HttpResponse response;
	String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orders_layout);
		
        Toast.makeText(getApplicationContext(), "Now you can see your orders here!", Toast.LENGTH_LONG).show();

     // URL of PHP Script

     		url = "http://192.168.221.1/cafebeside/";

     		// TextView to display result

     		TextView result = (TextView) findViewById(R.id.tvResult);

     		// Try to connect using Apache HttpClient Library
     		try {
     			httpclient = new DefaultHttpClient();
     			request = new HttpGet(url);
     			response = httpclient.execute(request);
     		}

     		catch (Exception e) {
     			// Code to handle exception
     		}

     		// response code
     		try {
     			BufferedReader rd = new BufferedReader(new InputStreamReader(
     					response.getEntity().getContent()));
     			String line = "";
     			while ((line = rd.readLine()) != null) {

     				// Appending result to textview
     				result.append(line);
     			}
     		} catch (Exception e) {
     			// Code to handle exception
     		}
	}
}
