package com.honestwalker.android.commons.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.honestwalker.android.APICore.API.net.cookie.ApiCookieManager;
import com.honestwalker.android.R;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.ObjectStreamIO;
import com.honestwalker.androidutils.IO.SharedPreferencesLoader;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.systembartint.SystemBarTintManager;


import java.io.IOException;
import java.util.HashMap;

public class LauncherActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("launchtime","LauncherActivity start  "+System.currentTimeMillis());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(false);
		tintManager.setStatusBarTintColor(this.getResources().getColor(R.color.none));

		String mainClassName = null;
		String welcomeClassName = null;
		LogCat.d("MAIN", " LauncherActivity  " );
		try {
			Log.i("launchtime","beforopenMain  "+System.currentTimeMillis());

			final ActivityInfo info = this.getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
			mainClassName =info.metaData.getString("MainActivity");
			welcomeClassName =info.metaData.getString("WelcomeActivity");
			LogCat.d("MAIN", " mainClassName == " + mainClassName );

//			Intent intent = new Intent(Intent.ACTION_MAIN);
//		     intent.addCategory(Intent.CATEGORY_LAUNCHER);
//		     intent.setClass(context, MainActivity.class);
//		     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//		     Context mContext = getApplicationContext();
//		     PendingIntent contextIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

			if(SharedPreferencesLoader.getInstance(this).getBoolean("isfirstopen", true)){

				HashMap<String, String> cookieMap = new HashMap<String , String>();
				try {
					ObjectStreamIO.output(this.getFilesDir().getAbsolutePath(), cookieMap, ApiCookieManager.cookieFileName);
				} catch (IOException e) {
					e.printStackTrace();
				}

				/*X5WebView webView = new X5WebView(this);
				webView.loadUrl(Server.context(this).getWeb_host()+"/room/index2/");*/

				UIHandler.init();
				//startActivity(new Intent(LauncherActivity.this, InitX5StupidWebview.class));
				final String finalMainClassName = mainClassName;
				final String finalWelcomeClassName = welcomeClassName;
				UIHandler.post(new Runnable() {
					@Override
					public void run() {
						openActivity(finalWelcomeClassName, finalMainClassName);
					}
				});
			}else{
				//openActivity(mainClassName);
				openActivity(mainClassName);
			}

			Log.i("launchtime","afteropenMain  "+System.currentTimeMillis());
			/*Intent intent = new Intent(this, Class.forName(mainClassName));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();*/

		} catch (NameNotFoundException e) {
			ExceptionUtil.showException(e);
		}

//		Intent intent = new Intent(this, getMainActivity());
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(intent);
//		finish();
	}
//	public abstract Class<? extends Activity> getMainActivity();

	private void openActivity(String mainClassName){
		Intent intent = null;
		try {
			intent = new Intent(this, Class.forName(mainClassName));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		SharedPreferencesLoader.putBoolean("isfirstopen", false);
	}
	private void openActivity(String welcomeActivity,String MainActivity){
		Intent intent = null;
		try {
			intent = new Intent(this, Class.forName(welcomeActivity));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("MainActivity",MainActivity);
			startActivity(intent);
			finish();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		SharedPreferencesLoader.putBoolean("isfirstopen", false);
	}

	boolean translucentStatus;
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		translucentStatus = on;
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("launchtime", "LauncherActivity stop  " + System.currentTimeMillis());
	}

	@TargetApi(19)
	protected boolean getTranslucentStatus() {
		return translucentStatus;
	}
}
