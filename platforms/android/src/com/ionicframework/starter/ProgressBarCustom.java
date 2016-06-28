/*
 * WhirlWindProgressBar.java
 * 
 * Licensed Materials - Property of IBM (C) Copyright IBM Corp. 2010. All Rights
 * Reserved. US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ionicframework.starter;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;


//import com.ibm.cio.whirlwind.activities.R;

public class ProgressBarCustom extends Dialog {
	private boolean stop = false;
	private static String TAG = "ProgressBarCustom";
	private ProgressBar progressBar;
	private int process;
	private int max;
	
	public ProgressBarCustom(Context context) {
		super(context);
		
		setCancelable(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progressbar);
		ImageView imageView = (ImageView)findViewById(R.id.download_icon);
		imageView.setBackgroundResource(android.R.drawable.stat_sys_download);
		progressBar = (ProgressBar) findViewById(R.id.progressbar_Horizontal);
	}

	public void setMax(int max) {
		progressBar.setMax(max);
		this.max = max;
	}
	public int getMax(){
		return max;
	}
	public void setProcess(int progress) {
		this.process = progress;
		
		handler.sendEmptyMessage(0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				stopDownload();
				return true;
			case KeyEvent.KEYCODE_SEARCH:
				stopDownload();
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	private void stopDownload() {
		handler.sendEmptyMessage(1);
		stop = true;
	}
	private void updatePercent(){
//		TextView percentTextView = (TextView) findViewById(R.id.percent);
//		int percent = (process*100) / max;
//		percentTextView.setText(percent + "%");
	}
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {

				progressBar.setProgress(process);
				updatePercent();
			} else if (msg.what == 1) {
				dismiss();
			}
		}
	};

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

}
