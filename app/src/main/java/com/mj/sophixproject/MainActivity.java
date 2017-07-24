package com.mj.sophixproject;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.taobao.sophix.SophixManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv_name);
        EventBus.getDefault().register(this);
        initDialog();

    }

    /**
     *
     */
    private void initDialog() {
        dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("有新的补丁更新");
        dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                restartApplication();
            }
        });
        dialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void eventBusCallBack(final EventbusBean bean) {

        if (bean != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ("update".equals(bean.getTag())) {
                        String msg = (String) bean.getObj();
                        tv.setText("修复Bug之后的版本22222222222  " + msg);
                    } else if ("restartApp".equals(bean.getTag())) {
                        dialog.show();
                    }
                }
            });
        }
    }

    /**
     * 重启应用
     */
    private void restartApplication() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent restartIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        //退出程序
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                restartIntent); // 1秒钟后重启应用

        SophixManager.getInstance().killProcessSafely();
        final Intent intent1 = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);

    }

}
