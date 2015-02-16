package com.team.cafebeside.networkEngine;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class PostMan {

String response1 = "";
	
	public String post(String json,String url){
		 
		try{
			
		HttpClient httpclient = new DefaultHttpClient();
		
	    HttpPost httppost = new HttpPost(url);	    
	    StringEntity mEntity=new StringEntity(json);
	   
	    httppost.setEntity(mEntity);
	    
	    httppost.setHeader("json",json.toString());
        httppost.getParams().setParameter("jsonpost",mEntity);
	    
	    httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-type", "application/json");
        HttpResponse httpResponse = httpclient.execute(httppost);
        HttpEntity resEntityGet = httpResponse.getEntity();  
        
        if (resEntityGet != null) {  
	        // do something with the response
	       response1 = EntityUtils.toString(resEntityGet);
	       Log.e("RESPONSEEEE", "RESPOSN " +response1);
	    }
	    
		}
		catch(Exception e){
			Log.e("ERRRRRR", "CafeBeside" +e);
		}
		return response1;
	}

}