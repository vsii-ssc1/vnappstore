package com.ionicframework.starter.plugin;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.ionicframework.starter.PackageHandler;
import com.ionicframework.starter.Utils;

public class AppPlugin extends CordovaPlugin{
	private Activity context;
	private CallbackContext callbackContext = null;
	@Override
	public boolean execute(String action, JSONArray arguments, CallbackContext callbackContext) {
		this.callbackContext = callbackContext; 
		if(this.cordova != null)
			context = this.cordova.getActivity();
		System.out.println("------------------------"+ action);
		try {
			if(action.equals("getVersion")){
				String version = getVersion();
				sendUpdate(version);
				return true;
			} else if("check".trim().equals(action.trim())){
//				String url = "http://www.apkmirror.com/apk/google-inc/maps/maps-9-31-2-release/maps-9-31-2-3-android-apk-download/";
				String url = "http://cygnus.androidapksfree.com/hulk/com.bbm_v2.13.1.14-1854_Android-4.0.3.apk";
				try {
		        	Utils.download(context,"", url,"", "AppDownload", "", "", "");
//					JSONObject obj = arguments.getJSONObject(0);
					String packageName = "com.ibm.cio.be.hyb.ios.blueagents";//obj.optString("moduleName");
					
					System.out.println("------------------------"+ packageName);
					boolean isInstalled = Utils.appInstalledOrNot(packageName, context);
					int statusApp = 0;
//					String versionNameOfItem = obj.optString("version");;
					if(isInstalled){
						System.out.println("------------------------");
						statusApp = 1;
//						if(!"".equalsIgnoreCase(versionNameOfItem)){
////							String versionNameApp = Utils.getVersionByPackageName(context, packageName);
////							int c = Utils.compareVersion(versionNameApp, versionNameOfItem);
////							if(c < 0){
////								statusApp = 2;
////							} else if(c == 0){
////								statusApp = 3;
////							}
//						}
					}else {
						System.out.println("------------------------>>>>>>>");
						statusApp = 0; 
//									
					}
					
//					obj.put("status", statusApp);
					sendUpdate(statusApp+"");
					return true;
				} catch (Exception e) {
					System.out.println("------------------------>>>>>>>" + e.getMessage());
					return false;
					
				}
			} else if(action.equals("open")){
				
//					Utils.runApplicationByPackageName(context, arguments.getString(0));
		
			} else if(action.equals("updateApp")){
				
				final String url = arguments.getString(0);
				final String cookies = arguments.getString(1);
				final String userId = arguments.getString(2);
				final String pass = arguments.getString(3);
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
//			        	Utils.download(context,"", url,"", "AppDownload", cookies, userId, pass);    				
					}
				};
		
				context.runOnUiThread(runnable);
		
			} else if(action.equals(PackageHandler.ACTION_PACKAGE)){
			} else if(action.equals("cancelDownload")){
				// TODO
//				Utils.statusDownload = false;
//				JSONObject jsonObject = new JSONObject();
//				jsonObject.put("statusDownload", Utils.statusDownload);
//				sendUpdate(jsonObject);
				return true;
			} else if(action.equals("getDeviceSerial")){
				 String serial = ""; 
		
				 try {
					 
				     final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		
				     String tmDevice, tmSerial, androidId;
				     androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
				     System.out.println(androidId);
		
				     tmDevice = "" + tm.getDeviceId();
				     tmSerial = "" + tm.getSimSerialNumber();
				     System.out.println(tmDevice);
				     System.out.println(tmSerial);
				     UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
				     String deviceId = deviceUuid.toString();
				     System.out.println(deviceId);
						
				 } catch (Exception ignored) {
						TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
						serial = tManager.getDeviceId();
				 }
			} else {
				System.out.println("-------------");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("-------------" + e.getMessage());
		}
		return false;
	}
	public String getVersion() {
		String version = "0";
//		version = Utils.getVersionAppStore(context);
		return version;
	}
	public String getDeviceName() {
		  String manufacturer = Build.MANUFACTURER;
		  String model = Build.MODEL;
		  if (model.startsWith(manufacturer)) {
		    return capitalize(model);
		  } else {
		    return capitalize(manufacturer) + " " + model;
		  }
		}


	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}
	public void sendUpdate(JSONObject obj) {
		if (this.callbackContext != null) {
			this.callbackContext.success(obj);
		}
	}
	public void sendUpdate(String message) {
		if (this.callbackContext != null) {
			this.callbackContext.success(message);
		}
	}
}
