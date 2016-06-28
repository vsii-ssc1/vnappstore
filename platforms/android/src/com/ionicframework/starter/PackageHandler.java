package com.ionicframework.starter;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



public class PackageHandler extends BroadcastReceiver {
	private final String TAG = "PackageHandler";
	public static final String ACTION_PACKAGE = "ACTION_PACKAGE";
	public static final String ACTION_PACKAGE_REPLACED = "ACTION_PACKAGE_REPLACED";
	public static final String ACTION_PACKAGE_REMOVED = "ACTION_PACKAGE_REMOVED";
	public static final String ACTION_PACKAGE_CHANGED = "ACTION_PACKAGE_CHANGED";
	public static final String ACTION_PACKAGE_INSTALL = "ACTION_PACKAGE_INSTALL";
	public static final String ACTION_PACKAGE_ADDED = "ACTION_PACKAGE_ADDED";
	String packageName;
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if(msg != null){
//					Bundle data = msg.getData();
//					if(data != null && data.getString("packagename")!= null){
//						Log.d(TAG, data.getString("packagename"));
						handlerApplication();
//					}
				}
			}
		}
	};
	Context context;
	private String action;
	
	public PackageHandler() {

	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("", "Intent Received: " + intent.getAction());
		Uri uri = intent.getData();
		packageName = uri != null ? uri.getSchemeSpecificPart() : "";
		Log.d(TAG, "package: -- > " + packageName);

//		Message msg = Message.obtain();
//		Bundle data = new Bundle();
//		data.putString("packagename", pkg);
//		
//		msg.what = 0;
		if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
			action = Intent.ACTION_PACKAGE_ADDED;
			
			
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
			action = Intent.ACTION_PACKAGE_REPLACED;
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_CHANGED)) {
			action = Intent.ACTION_PACKAGE_CHANGED;
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_INSTALL)) {
			action = Intent.ACTION_PACKAGE_INSTALL;
		} else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
			action = Intent.ACTION_PACKAGE_REMOVED;
		}
		handler.sendEmptyMessage(0);
	}

	private void handlerApplication() {
		try {
			String value = "";
			JSONObject object = new JSONObject();
			if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
				value = ACTION_PACKAGE_ADDED;
				
				
			} else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
				value = ACTION_PACKAGE_REMOVED;
				
			} else if (Intent.ACTION_PACKAGE_CHANGED.equals(action)) {
				value = ACTION_PACKAGE_CHANGED;
				
			}
			object.put("action", value);
			JSONObject o = new JSONObject();
			o.put("packageName", packageName);
			object.put("data", o);
			System.out.println("+=====" +o.toString());
//			AppStore.getInstance().sendObject(object);
		} catch (Exception e) {
		}

	}

}
