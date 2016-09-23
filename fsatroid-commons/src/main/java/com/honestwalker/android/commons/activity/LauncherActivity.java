package com.honestwalker.android.commons.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import android.content.pm.PackageManager.NameNotFoundException;

public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String mainClassName = null;
		LogCat.d("MAIN", " LauncherActivity  " );
		try {
			ActivityInfo info = this.getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
			mainClassName =info.metaData.getString("MainActivity");
			LogCat.d("MAIN", " mainClassName == " + mainClassName );

//			Intent intent = new Intent(Intent.ACTION_MAIN);
//		     intent.addCategory(Intent.CATEGORY_LAUNCHER);
//		     intent.setClass(context, MainActivity.class);
//		     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//		     Context mContext = getApplicationContext();
//		     PendingIntent contextIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

			Intent intent = new Intent(this, Class.forName(mainClassName));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();

		} catch (NameNotFoundException e) {
			ExceptionUtil.showException(e);
		} catch (ClassNotFoundException e) {
			ExceptionUtil.showException(e);
		}

//		Intent intent = new Intent(this, getMainActivity());
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		startActivity(intent);
//		finish();
	}
//	public abstract Class<? extends Activity> getMainActivity();

}
