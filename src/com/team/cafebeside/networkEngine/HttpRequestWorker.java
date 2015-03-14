package com.team.cafebeside.networkEngine;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.team.cafebeside.configs.Configuration;

import android.util.Log;

/**
 * Created by anoojkrishnang on 14/12/14.
 * 
 */
public class HttpRequestWorker {
	
	

	/*
	 * Method: GetRequest
	 * @param: url:String,isHeaderRequired:boolean
	 * @Desc : url				: URL to connect the server, 
	 *         isHeaderRequired	: Set username as Header for every request after Login.
	 */
	public String GetRequest(String url, boolean isHeaderRequired) {

		try {
			HttpClient mClient = new DefaultHttpClient();
			HttpGet mHttpGet = new HttpGet(url);
			if (isHeaderRequired) {
				mHttpGet.setHeader("username", Configuration.USERNAME);
			}
			ResponseHandler<String> mResponseHandler = new BasicResponseHandler();
			String mStatus = mClient.execute(mHttpGet, mResponseHandler);
			Log.e("RESPONSE", "" + mStatus);
			return mStatus;
		} catch (Exception ex) {
			return "Failed " + ex;
		}
	}

	/*
	 * Method: POSTRequest
	 * @param: url:String, content:String, isHeaderRequired:boolean
	 * @Desc : url				: URL to connect the server,
	 *         content			: JSON Content to send, 
	 *         isHeaderRequired	: Set username as Header for every request after Login.
	 */
	public String PostRequest(String url, String content,
			boolean isHeaderRequired) {
		try {
			String mStatus = null;
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost mHttpPost = new HttpPost(url);
			StringEntity se = new StringEntity(content);
			se.setContentType("application/json");
			mHttpPost.setEntity(se);
			mHttpPost.setHeader(Configuration.GLOBAL_HEADER_KEY, content);
			if (isHeaderRequired) {
				mHttpPost.setHeader("username", Configuration.USERNAME);
			}

			HttpResponse httpresponse = httpclient.execute(mHttpPost);
			mStatus = EntityUtils.toString(httpresponse.getEntity());

			Log.e("RESPONSE", "" + mStatus);

			return mStatus;
		} catch (Exception ex) {
			return "Failed " + ex;
		}
	}
}
