package com.mj.sophixproject;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by lenovo on 2017/7/23.
 */

public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        initHotfix();
    }

    private void initHotfix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }

        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                //.setAesKey("0123456789123456")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        String msg = new StringBuilder("").append("Mode:").append(mode)
                                .append(" Code:").append(code)
                                .append(" Info:").append(info)
                                .append(" HandlePatchVersion:").append(handlePatchVersion).toString();
                           Log.i("code","mode = "+mode+"info = "+ info);
                           // 补丁加载回调通知
                         if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Log.i("code","表明补丁加载成功");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                            EventBus.getDefault().post(new EventbusBean(code,"restartApp",msg));
                            Log.i("code","用户可以监听进入后台事件, 然后应用自杀");
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                             SophixManager.getInstance().cleanPatches();
                             Log.i("code","内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载");
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Log.i("code"," 其它错误信息, 查看PatchStatus类说明");
                        }
                        EventBus.getDefault().post(new EventbusBean(code,"update",msg));
                        Log.i("MyApp",msg);
                    }
                }).initialize();

        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}