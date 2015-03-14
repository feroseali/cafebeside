package com.team.cafebeside.networkEngine;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncWorker extends AsyncTask<String, String, String>{
	private ProgressDialog progress;
	private String response;
	private Context currentContext;
	public AsyncResponse delegate	=	null;

    public AsyncWorker(Context context) {
    	currentContext = context;
    } 
	
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(currentContext);
        progress.setTitle("Processing");
        progress.setMessage("Please wait while loading...");
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();
	}
	
	@Override
	protected String doInBackground(String... params) {
		try{
		Log.e("Param1",params[0]+" "+params[1]);
		String url	=	params[0];
		String content	=	params[1];
		
		HttpRequestWorker mWorker = new HttpRequestWorker(); 
		response =	mWorker.PostRequest(url, content, false);
		return response;
		}catch(Exception ex){
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		progress.dismiss();
		delegate.processFinish(result);
    }
}
