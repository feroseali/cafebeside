package com.team.cafebeside.workers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.team.cafebeside.configs.Configuration;

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

			int deviceVersion = Build.VERSION.SDK_INT;
			String ssid = wifiInfo.getSSID().toString();

			if (deviceVersion >= 17) {
				if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
					ssid = ssid.substring(1, ssid.length() - 1);
				}
			}
			if (ssid != null) {
				if (ssid.trim().equals(Configuration.CAFE_WIFI_SSID.trim())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		} else {
			return false;
		}

	}

}
