/*
 * Author	:	Anooj Krishnan G
 * Date		:	31/01/2015
 */


package com.team.cafebeside.backgroundRunners;

import com.team.cafebeside.configs.Configuration;
import com.team.cafebeside.screenMappers.HomeActivity;
import com.team.cafebeside.screenMappers.SplashActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class WifiReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		//Log.e("RECIEVING", "BROADCAST");
		ConnectivityManager connectionManager = (ConnectivityManager) arg0
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetwork = connectionManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		//Log.e("RECIEVING", "BROADCAST with: " + wifiNetwork);
		if (wifiNetwork != null && wifiNetwork.isAvailable()) {

			WifiManager wifiManager = (WifiManager) arg0
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			//Log.e("RECIEVING", "BROADCAST INFO with: " + wifiInfo);
			
			 int deviceVersion= Build.VERSION.SDK_INT;
				String ssid = wifiInfo.getSSID().toString();

		     if (deviceVersion >= 17){
		         if (ssid.startsWith("\"") && ssid.endsWith("\"")){
		             ssid = ssid.substring(1, ssid.length()-1);
		         }
		     }

			//Log.e("CAFEBESIDE", "CONNECTED TO : " + ssid);
			//Toast.makeText(arg0, "CONNECVTED TO "+ssid, Toast.LENGTH_LONG).show();
			if (ssid != null) {
				if (ssid.trim().equals(Configuration.CAFE_WIFI_SSID.trim())) {
					//Log.e("CAFEBESIDE NOTIFICATION", "CONNECTED TO CAFE");
					Intent uplIntent = new Intent(arg0, SplashActivity.class);
					if(Configuration.IS_APP_RUNNING){
						//Log.e("ISAPPRUNNING","TRUE");
						//IF APP IS ALERADY RUNNING, DO NOTHING
						//uplIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					}else{
						uplIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						arg0.startActivity(uplIntent);
					}
					
				}
			}

		}

	}

}
