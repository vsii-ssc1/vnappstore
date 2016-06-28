package com.ionicframework.starter;
//package com.ionicframework.starter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import org.apache.http.Header;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;


public class Utils {
	private static final String TAG = "Utils";
	public static boolean statusDownload = false;
	public static void download(final Context context,final String itemId, final String url, final String nameApp,
			final String moduleName, final String cookies, final String userId, final String pass) {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + moduleName + ".apk";
		final File outputFile = new File(path);

		final ProgressBarCustom dialogDownloadApp = new ProgressBarCustom(context);
		Thread t = new Thread(new Runnable() {
			public void run() {
				if (outputFile != null) {
					String linkUrl = url;
					Log.d(TAG, url);
					if (url.indexOf("http://") == -1
							&& url.indexOf("https://") == -1) {
						linkUrl = "http://" + url;
					}

					downloadFileWithNotification(context, itemId, linkUrl,nameApp, outputFile,
							dialogDownloadApp, cookies, userId, pass);


				}

			}
		});
		t.start();
	}
	
	
	/**
	 * Download APK file from url and push data into output file and display
	 * progress bar
	 * 
	 * @param context
	 * @param url
	 * @param outputFile
	 * @param progressBar
	 */
	public final static void downloadFile(final Context context, String url,
			File outputFile, ProgressBarCustom progressBar, String cookies, String userId, String pass) {
		try {
			Log.d(TAG, userId);
			URL url1 = new URL(url);
			String host = url1.getHost();
			int port = url1.getPort();
			DefaultHttpClient httpclient = Utils.getNewHttpClient();
			httpclient.getCredentialsProvider().setCredentials(
					new AuthScope(host, port),
					new UsernamePasswordCredentials(userId,
							pass));

			HttpGet httpget = new HttpGet(url);
			String urldownload = url;
			
			System.out.println("cookies = " + cookies + ", url:  " + url);
			if(	urldownload.indexOf("whirlwind/services/download") > -1
					|| urldownload.indexOf("wwAppStore/services/download") > -1){
				urldownload = urldownload.replace("download", "appfile");
			}
			
			HttpResponse response;
			response = httpclient.execute(httpget);
			StatusLine status = response.getStatusLine();
			int resCode = status.getStatusCode();
			if (resCode == 401) {

				Activity activity = (Activity)context;
				if (progressBar.isShowing())
					progressBar.handler.sendEmptyMessage(1);
				progressBar.setStop(true);
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {

						final Utils.DialogHandler handler = new Utils.DialogHandler() {
							public void callback() {
							}
						};
						

						Handler handler1 = new Handler() {
							public void handleMessage(Message msg) {
								if (msg.what == 0) {
									AlertDialog confirmDialog = Utils.getAlertDialog(context,
											"AppStore",
											"You has no permission!",
											"OK", null, handler);
									confirmDialog.show();
									
								}
							}
						};
						handler1.sendEmptyMessage(0);
					}
				});


				return;
			}

			HttpEntity entity = response.getEntity();
			
			Header[] clHeaders = response.getHeaders("Content-Length");
			
			Header header = clHeaders[0];
			int totalSize = Integer.parseInt(header.getValue());
			progressBar.setMax(totalSize);
			int downloadedSize = 0;
			if (entity != null) {
				InputStream stream = entity.getContent();
				byte buf[] = new byte[1024 * 1024];
				int numBytesRead;

				BufferedOutputStream fos = new BufferedOutputStream(
						new FileOutputStream(outputFile));
				do {
					numBytesRead = stream.read(buf);
					if (numBytesRead > 0) {
						fos.write(buf, 0, numBytesRead);
						downloadedSize += numBytesRead;
						progressBar.setProcess(downloadedSize);
					}
				} while (numBytesRead > 0 & progressBar.isShowing());

				fos.flush();
				fos.close();
				stream.close();
				progressBar.handler.sendEmptyMessage(1);
			}
		} catch (FileNotFoundException e) {
			if (progressBar.isShowing())
				progressBar.handler.sendEmptyMessage(1);
			progressBar.setStop(true);
			e.printStackTrace();
			Log.d(TAG, Log.getStackTraceString(e));
		} catch (IOException e) {
			e.printStackTrace();
			if (progressBar.isShowing())
				progressBar.handler.sendEmptyMessage(1);
			progressBar.setStop(true);

			Log.d(TAG, Log.getStackTraceString(e));
		} catch (Exception e) {
			if (progressBar.isShowing())
				progressBar.handler.sendEmptyMessage(1);
			progressBar.setStop(true);
//			e.printStackTrace();
			Log.d(TAG, Log.getStackTraceString(e));
		}
	}
	public final static void updateAppStore(final Context context,String url, String nameAppDownload,
			File outputFile, String userId, String pass) {
		try {
			URL url1 = new URL(url);
			String host = url1.getHost();
			int port = url1.getPort();
			DefaultHttpClient httpclient = Utils.getNewHttpClient();
			httpclient.getCredentialsProvider().setCredentials(
					new AuthScope(host, port),
					new UsernamePasswordCredentials(userId,
							pass));

			HttpGet httpget = new HttpGet(url);
			
			HttpResponse response;
			response = httpclient.execute(httpget);
			StatusLine status = response.getStatusLine();
			int resCode = status.getStatusCode();
			if (resCode == 401) {

				Activity activity = (Activity)context;
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {

						final Utils.DialogHandler handler = new Utils.DialogHandler() {
							public void callback() {
							}
						};
						

						Handler handler1 = new Handler() {
							public void handleMessage(Message msg) {
								if (msg.what == 0) {
									AlertDialog confirmDialog = Utils.getAlertDialog(context,
											"AppStore",
											"You has no permission!",
											"OK", null, handler);
									confirmDialog.show();
									
								}
							}
						};
						handler1.sendEmptyMessage(0);
					}
				});


				return;
			}
			HttpEntity entity = response.getEntity();
			
			Header[] clHeaders = response.getHeaders("Content-Length");
			
			Header header = clHeaders[0];
			int totalSize = Integer.parseInt(header.getValue());
			NotificationManager mNotifyManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
			mBuilder.setContentTitle(nameAppDownload)
		    .setContentText("Download in progress")
		    .setSmallIcon(R.drawable.stat_sys_download);
//		    .setAutoCancel(true);
			int downloadedSize = 0;
			if (entity != null) {
				InputStream stream = entity.getContent();
				byte buf[] = new byte[1024 * 1024];
				int numBytesRead;
				int percent = 0;
				BufferedOutputStream fos = new BufferedOutputStream(
						new FileOutputStream(outputFile));
				do {
					numBytesRead = stream.read(buf);
					if (numBytesRead > 0) {
						fos.write(buf, 0, numBytesRead);
						downloadedSize += numBytesRead;
						percent = downloadedSize*100 / totalSize;
						mBuilder.setProgress(100, percent, false);
						mNotifyManager.notify(0, mBuilder.build());			
	        			try {
	        				Thread.sleep(10);
						} catch (Exception e) {
						}
								
								
					}
				} while (numBytesRead > 0);
				mBuilder.setContentText("Download Completed");
				mBuilder.setProgress(0, 0, false);
				mNotifyManager.notify(0, mBuilder.build());
				fos.flush();
				fos.close();
				stream.close();
				if(percent == 100){
					Uri uri = Uri.fromFile(outputFile);
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(uri,
							"application/vnd.android.package-archive");
					context.startActivity(intent);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.d(TAG, Log.getStackTraceString(e));
		} catch (IOException e) {
			e.printStackTrace();

			Log.d(TAG, Log.getStackTraceString(e));
		} catch (Exception e) {
			Log.d(TAG, Log.getStackTraceString(e));
		}
	}
	// TODO
	public final static void downloadFileWithNotification(final Context context,String itemId, String url, String nameAppDownload,
			File outputFile, ProgressBarCustom progressBar, String cookies, String userId, String pass) {
		try {
			String urldownload = url;
			
			if(	urldownload.indexOf("whirlwind/services/download") > -1){
				urldownload = urldownload.replace("download", "appfile");
			}

			URL url1 = new URL(urldownload);

//			String host = url1.getHost();
//			int port = url1.getPort();
			DefaultHttpClient httpclient = Utils.getNewHttpClient();
//			httpclient.getCredentialsProvider().setCredentials(
//					new AuthScope(host, port),
//					new UsernamePasswordCredentials(userId,
//							pass));

			HttpGet httpget = new HttpGet(urldownload);
			
//			if(	urldownload.indexOf("whirlwind") > -1){
//				urldownload = urldownload.replace("whirlwind", "wwstage");
//			}
			
			
			
			HttpResponse response;
			response = httpclient.execute(httpget);
			StatusLine status = response.getStatusLine();
			int resCode = status.getStatusCode();
			Log.d(TAG, "rescode: " + resCode);
			if (resCode == 401) {

				Activity activity = (Activity)context;
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {

						final Utils.DialogHandler handler = new Utils.DialogHandler() {
							public void callback() {
							}
						};
						

						Handler handler1 = new Handler() {
							public void handleMessage(Message msg) {
								if (msg.what == 0) {
									AlertDialog confirmDialog = Utils.getAlertDialog(context,
											"AppStore",
											"You has no permission!",
											"OK", null, handler);
									confirmDialog.show();
									
								}
							}
						};
						handler1.sendEmptyMessage(0);
					}
				});


				return;
			}
			statusDownload = true;
			HttpEntity entity = response.getEntity();
			
			Header[] clHeaders = response.getHeaders("Content-Length");
			
			Header header = clHeaders[0];
			int totalSize = Integer.parseInt(header.getValue());
			// TODO start
//			Intent intent = new Intent(context, AppStore.class);
//			final PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, 0);
//			NotificationManager mNotifyManager =
//			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//			Notification notification = new Notification(R.drawable.stat_sys_download, nameAppDownload, System.currentTimeMillis());
//			notification.flags = notification.flags|Notification.FLAG_ONGOING_EVENT;
//			
//			notification.contentView = new RemoteViews(context.getApplicationContext().getPackageName(), com.ibm.cio.AppStore.R.layout.download_progress);
//			notification.contentIntent = pendingIntent;
//			notification.contentView.setImageViewResource(com.ibm.cio.AppStore.R.id.download_icon, R.drawable.stat_sys_download );
//			notification.contentView.setProgressBar(com.ibm.cio.AppStore.R.id.status_progress, 100, 0, false);
//			mNotifyManager.notify(0, notification);
			
			// TODO finish
			
			
			String osVerison = android.os.Build.VERSION.RELEASE;
			int c = compareVersion(osVerison, "4");
			
			NotificationManager mNotifyManager =
			        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = null;
			if(c >= 0){
				mBuilder = new NotificationCompat.Builder(context);
				mBuilder.setContentTitle(nameAppDownload)
			    .setContentText("Download in progress")
			    .setSmallIcon(R.drawable.stat_sys_download);
//			    .setAutoCancel(true);
				
			}
			int downloadedSize = 0;
			if (entity != null) {
				InputStream stream = entity.getContent();
				byte buf[] = new byte[1024 * 1024];
				int numBytesRead;
				int percent = 0;
				BufferedOutputStream fos = new BufferedOutputStream(
						new FileOutputStream(outputFile));
				do {
					numBytesRead = stream.read(buf);
					if (numBytesRead > 0) {
						fos.write(buf, 0, numBytesRead);
						downloadedSize += numBytesRead;
						percent = downloadedSize*100 / totalSize;
//						notification.contentView.setProgressBar(com.ibm.cio.AppStore.R.id.status_progress, 100, percent, false);
//						Log.d(TAG, "downloadedSize-->" + downloadedSize + percent);
						if(c >= 0){
							mBuilder.setProgress(100, percent, false);
							mNotifyManager.notify("AppStore", 0, mBuilder.build());
						}
						updatePercentDownload(itemId, percent, "ACTION_DOWNLOAD");

	        			try {
	        				Thread.sleep(10);
						} catch (Exception e) {
							// TODO: handle exception
						}
								
								
					}
				} while (numBytesRead > 0 && statusDownload == true);
				cancelDownload(itemId, percent, "ACTION_DOWNLOAD_COMPLETED");
				
				if(c >=0 ){
					mNotifyManager.cancel("AppStore", 0);
					mBuilder.setContentText("Download Completed");
					mBuilder.setProgress(0, 0, false);
					
					mNotifyManager.notify("AppStore", 0, mBuilder.build());
				}
				statusDownload = false;
				fos.flush();
				fos.close();
				stream.close();
				if(percent == 100){
					Uri uri = Uri.fromFile(outputFile);
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(uri,
							"application/vnd.android.package-archive");
					context.startActivity(intent);
				}
				Log.d(TAG, "finish download");
			}
		} catch (FileNotFoundException e) {
			cancelDownload(itemId, 0, "ACTION_DOWNLOAD_COMPLETED");
			statusDownload = false;
			e.printStackTrace();
			Log.d(TAG + "=====>", Log.getStackTraceString(e));
		} catch (IOException e) {
			cancelDownload(itemId, 0, "ACTION_DOWNLOAD_COMPLETED");
			statusDownload = false;
			e.printStackTrace();

			Log.d(TAG + "=====>", Log.getStackTraceString(e));
		} catch (Exception e) {
			cancelDownload(itemId, 0, "ACTION_DOWNLOAD_COMPLETED");
			statusDownload = false;
			Log.d(TAG + "=====>", Log.getStackTraceString(e));
		}
	}
	// TODO
	private static void updatePercentDownload(String itemId, int percent, String action){
	    try {
	    	JSONObject object = new JSONObject();
			object.put("action", action);
			JSONObject o = new JSONObject();
			o.put("percent", percent);
			o.put("itemId", itemId);
			object.put("data", o);
//			AppStore.getInstance().sendObject(object);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		
	}
	// TODO
	private static void cancelDownload(String itemId, int percent, String action){
	    try {
	    	JSONObject object = new JSONObject();
			object.put("action", action);
			JSONObject o = new JSONObject();
			o.put("percent", percent);
			o.put("statusDownload", statusDownload);
			o.put("itemId", itemId);
			object.put("data", o);
//			AppStore.getInstance().sendObject(object);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		
	}
	public static interface DialogHandler {
		public void callback();
	}
	public static AlertDialog getAlertDialog(final Context context,
			String title, String message, String button1, String button2,
			final DialogHandler handler) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
		alt_bld.setMessage(message).setCancelable(false);
		if (button1 != null)
			alt_bld.setPositiveButton(button1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							handler.callback();
							dialog.cancel();
						}
					});
		if (button2 != null)
			alt_bld.setNegativeButton(button2,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// Action for button 2
							dialog.cancel();
						}
					});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(title);
		return alert;
	}
	/**
	 * Run application by package name
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void runApplicationByPackageName(Context context,
			String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(
				packageName);
		intent.setAction(Intent.ACTION_VIEW);
		context.startActivity(intent);
	}
	/**
	 * Check the existence of an application
	 * 
	 * @param packageName
	 * @param context
	 * @return
	 */
	public static boolean appInstalledOrNot(String packageName, Context context) {
		PackageManager pm = context.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}
//	public static void main(String a[]){
//		System.out.println( "mail");
//		try {
//		String url = "https://lmc2.watson.ibm.com:15016/wwAppStore/services/appfile/1208/vani_faces.apk";
//		DefaultHttpClient httpclient = Utils.getNewHttpClient();
//		httpclient.getCredentialsProvider().setCredentials(
//				new AuthScope("lmc2.watson.ibm.com", 15016),
//				new UsernamePasswordCredentials("siemdt@vn.ibm.com",
//						"qaz12qaz"));
//		HttpGet httpget = new HttpGet(url);
////		httpget.setHeader("Authorization", "Basic ");
//		
//		HttpResponse response;
//		
//			response = httpclient.execute(httpget);
////			HttpEntity entity = response.getEntity();
//			
//			Header[] clHeaders = response.getHeaders("Content-Length");
//			Header header = clHeaders[0];
//			int totalSize = Integer.parseInt(header.getValue());
//			System.out.println(totalSize + "");
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		
//		
//	}
	// url = file path or whatever suitable URL you want.
	public static String getMimeType(String url)
	{
	    String type = null;
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    if (extension != null) {
	        MimeTypeMap mime = MimeTypeMap.getSingleton();
	        type = mime.getMimeTypeFromExtension(extension);
	    }
	    Log.d("MIME-TYPE: ",  type);
	    return type;
	}
	
	public static String streamToString(final InputStream is) {
		String result = null;
		if (is != null) {
			BufferedInputStream bis = new BufferedInputStream(is);
			bis.mark(Integer.MAX_VALUE);
			final StringBuilder stringBuilder = new StringBuilder();
			try {
				// stream reader that handle encoding
				final InputStreamReader readerForEncoding = new InputStreamReader(
						bis, "UTF-8");
				final BufferedReader bufferedReaderForEncoding = new BufferedReader(
						readerForEncoding);

				// stream reader that handle encoding
				bis.reset();
				final InputStreamReader readerForContent = new InputStreamReader(
						bis, "UTF-8");
				final BufferedReader bufferedReaderForContent = new BufferedReader(
						readerForContent);

				String line = bufferedReaderForContent.readLine();
				while (line != null) {
					stringBuilder.append(line);
					line = bufferedReaderForContent.readLine();
				}
				bufferedReaderForContent.close();
				bufferedReaderForEncoding.close();
			} catch (IOException e) {
				// reset string builder
				stringBuilder.delete(0, stringBuilder.length());
			}
			result = stringBuilder.toString();
		} else {
			result = null;
		}
		return result;
	}
	
	public static DefaultHttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new AppStoreSSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(params, 45000);
	        HttpConnectionParams.setSoTimeout(params,45000 );

	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
	        SchemeRegistry registry = new SchemeRegistry();
	        
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));
//	        registry.register(new Scheme("https", sf, 15014));
	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
	/**
	 * Get current version number.
	 * 
	 * @return
	 */
	public static String getVersionAppStore(Context context) {
		String version = "0.0";
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			version = pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
		}
		return version;
	}
	public static int getScreenHeight(Context context){
		int h = 0;
		
		WindowManager window = ((Activity)context).getWindowManager();
		Display d = window.getDefaultDisplay();
		h = d.getHeight();
		return h;
		
	}
	
	public static void unInstallApp(String packageName, Context context) {
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}
	
	public static String getVersionByPackageName(Context context,
			String packageName) {
		String version = null;
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					packageName, 0);
			version = pi.versionName;

		} catch (PackageManager.NameNotFoundException e) {
		}
		return version;
	}

	public static int compareVersion(String v1, String v2) {
//		Log.d(TAG, v1 + ", " + v2);
		int ret = 0;
		ret = v1.compareToIgnoreCase(v2);
		return ret;
	}
	public static int compareVersion1(String v1, String v2) {
		if(v2 == null || v1 == null)
			return 0;
		int ret = 0, l;
		String[] rArray = v1.split("\\.");
		String[] vArray = v2.split("\\.");
		if (rArray.length > vArray.length) {
			l = rArray.length;
		} else {
			l = vArray.length;
		}
		try {
			for (int j = 0; j < l; j++) {
				int ri = 0, vi = 0;
				if (rArray.length > j)
					ri = Integer.parseInt(rArray[j]);
				if (vArray.length > j)
					vi = Integer.parseInt(vArray[j]);
				if (ri < vi) {
					ret = -1;
					break;
				} else if (ri > vi) {
					ret = 1;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	public static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// always verify the host - dont check for certificate
	public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
	
}
