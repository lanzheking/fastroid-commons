package com.honestwalker.android.commons;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.honestwalker.android.R;
import com.honestwalker.android.commons.config.ContextProperties;
import com.honestwalker.android.commons.utils.erroreport.ReportError;
import com.honestwalker.android.commons.utils.erroreport.ReportErrorSender;
import com.honestwalker.androidutils.Application;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.ImageSelector.utils.BroadcaseManager;
import com.honestwalker.androidutils.ImageSelector.utils.CrashInfoBean;
import com.honestwalker.androidutils.Init.InitStrategy;
import com.honestwalker.androidutils.equipment.TelephoneUtil;
import com.honestwalker.androidutils.system.ProcessUtil;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by honestwalker on 15-10-9.
 */
public class BaseApplication extends android.app.Application  {

    public static Context context = null;

    private static Map<Activity,Activity> signinstanceActivity = new HashMap<Activity,Activity>();

    public static String appVersion = "";
    public static String appName = "";

    /** 记录程序上一次所在的页面名称 */
    public static String lastPage = "";

    /** 当前数据库版本 */
    private final int DATABASE_VERSION = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        initApp();

        new InitStrategy(context).execute();

//        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);

        LogCat.d("PROC", "启动进程：" + ProcessUtil.getMyPid() +
                " 进程名：" + ProcessUtil.getCurProcessName(getApplicationContext()));

    }

    /** 做一些app初始化工作 */
    private void initApp() {

        appName = getResources().getString(R.string.app_name);
        appVersion = Application.getAppVersion(context, R.class);

    }

    public UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            CrashInfoBean crashInfoBean = new CrashInfoBean();
            Intent intent = new Intent();
            crashInfoBean.setProcessName(ProcessUtil.getCurProcessName(context));
            crashInfoBean.setThrowable(ex);
            intent.putExtra(CrashInfoBean.CRASH_BROADCASE_BEAN , crashInfoBean);
            BroadcaseManager.sendBroadcast(context , CrashInfoBean.CRASH_BROADCASE_ACTION , intent);
            if(ContextProperties.getConfig().sendErrorReport) {
                sendReportError(ex);
            } else {
                showError(ex);
                Application.exit(context);
            }
        }
    };

    public static void addSingleInstanceActivity(Activity activity) {
        signinstanceActivity.put(activity, activity);
    }
    @SuppressLint("NewApi")
    public static void clearAllSigninstanceActivity() {
        Iterator<Map.Entry<Activity, Activity>> iter = signinstanceActivity.entrySet().iterator();
        while(iter.hasNext()) {
            Activity act = iter.next().getValue();
            try {
                LogCat.d("exit", act.getClass().getSimpleName());
                act.finish();
            } catch (Exception e) {
            }
        }
    }

    public static void exit() {
        Application.exit(context);
        System.exit(0);
        clearAllSigninstanceActivity();
    }

    private void showError(Throwable ex) {
        if(ex.getCause() != null) {
            LogCat.e("AndroidRuntime","<================KancartException Cause=====================>");
            LogCat.e("AndroidRuntime",ex.getCause().toString());
            StackTraceElement[] stesCause = ex.getCause().getStackTrace();
            if(stesCause != null) {
                for (StackTraceElement ste : stesCause) {
                    LogCat.e("AndroidRuntime",ste.toString());
                }
            }
        } else {
            LogCat.d("AndroidRuntime" , ex.toString());
        }
    }

    /**
     * 发送错误报告
     * @param ex
     */
    public void sendReportError(Throwable ex) {

        showError(ex);

        ReportError reportError = new ReportError();
        reportError.setName(R.class.getPackage().getName());
        reportError.setClient("android");
        reportError.setRemark(TelephoneUtil.getInstance(context).getInfoDetail() + " page:" + lastPage);
        reportError.setVersion(appVersion);
        reportError.setException(ex);
        ReportErrorSender.send(getApplicationContext(), reportError);
    }

}
