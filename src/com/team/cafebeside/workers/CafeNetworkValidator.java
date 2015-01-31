package com.team.cafebeside.workers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.team.cafebeside.configs.Configuration;
import com.team.cafebeside.screenMappers.SplashActivity;

public class CafeNetworkValidator {

	public boolean isConnectedToCafeBeside(Context arg0) {
		// TODO Auto-generated method stub
		ConnectivityManager connectionManager = (ConnectivityManager) arg0
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetwork = connectionManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isAvailable()) {

			WifiManager wifiManager = (WifiManager) arg0
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();

			String ssid = wifiInfo.getSSID();
			if (ssid != null) {
				if (ssid.trim().equals(Configuration.CAFE_WIFI_SSID.trim())) {
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}

		}else{
			return false;
		}

	}

}
