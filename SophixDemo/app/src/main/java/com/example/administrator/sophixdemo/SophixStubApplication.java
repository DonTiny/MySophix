package com.example.administrator.sophixdemo;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * Created by Administrator on 2018/11/26.
 */

public class SophixStubApplication extends MyApplication {
    private final String TAG = "SophixStubApplication";
    private final String APP_ID = "25310024";
    private final String APP_SECRET = "0163a105498abba751301bd9a6395be0";
    private final String APP_RSA = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCLElO4X40u31nqxVAu7aGN5NWPveVo1MIH6nGvEikN982HdXF229731cuxggn4d2DgGxv0Zd9M/gGo1rVCI8EVfbgd25wC1eZ44kr1R9C/XOW47NxsvbjBKjHK5+rbxn1qT4X1jEm/ftb0VbSZIopumrJKyFiiKljVip26W/cFw8J+Iz1BKkjVJfAY8EdDRAGts5N63p0RMeaIS3SoHsMaSC8BAWooI372Pwx0ulHdoEgpPQ7MZB5TJSvrxPDRsxMP7wCKDs4yEiKV3lftB5OPDitd0FTjnUOz6iiZes3MfQELg0O4sMyVhFh2k8pWvLT1RfswtaGHxZXkHJ/WOdAVAgMBAAECggEAHSM63T9P0klDipfKo/pvxc3zHMm5ngwtlwD8zqRsa5vGuSHXIwMQJTbrFYbcVe2VI38qjTsm3Pe/G0PY+EGLe9g6Ui8n8cQlJKCs+dURHHb3PpaJ3FKnufkAM7YBlAhzUGnW9EpvTSrjGJA4ZXlCLchFoLt3LorWz+7Z+yi3icJZNLFTIS5sYdIAmIFWwV5+pYgRR1cAsqJ41OGiOkXDVQ5hcd86ds8rDZP57Wgfun5FZiXGez2AhgJuwidpqC5m6tYaHVW8WTOxjgLRGxWIHY4rIhhvUjtry1uXK/hE063AHGE4sKdiW83wlGcB4EDx44U3ak4AH9IpGT4f/VHoxQKBgQDa/ZuqVIeNPjbOuZc4brHd6RzWzgdO0+4NxVSOzoQqowthE+JUkKb8AOrLfJcV7Y53bFsa6DX9WnmgVeP0mfYCGJcYz2OqYv6FCgBe13bJfr95BbRDlaZdM4I28v47OYx7xPad6a/VPUcny4k3pWj+z+/2fHLQVx3wiTuONV02jwKBgQCikxuSp2aAmHlxKLjrlIFvOC/FIcVGtsOyvW/p5cKJy94jxuOHgWs2aTgl31vfNb30+U3tPDLU23Sy+BxQVABszUN9sWL5nmpG6dgin/KcQQAGZ6Ej6tupPeCUO9XE25DJsklcQrTq1MGhKMikkT599oTj9KZBhe1NGw6JgCmBGwKBgHmQJoVa/xQs4QfIhmVRLXNeICAtpmjfxFnonJPZmAyDDaIKA62lzsw9RhlHRCdsA5XPlJNPLAlD3BhhFXWGscVgGSAwn8bJxTfc4W/Ec7BOp8NqozKRNBSwrywJn48I1y8tmyj1vCHHLmSs27MEI1Cv5SkS1DlK4tfPljvO67uJAoGAElWi02w7nB8V+xQLns3VtxRQFtxTIkF0WpEQsQEvaljqiYBhoqOO0skVAvhopQirjpgbGLkkRmslgxWGI+0FSz/u75r0vZAHDk5LCMSfGM4f0ZkYTDOtaC3JSQ5iclwMvSJ1MYl4W894stMlpaGXuZ72p7qskVEoTPzTK+CfTacCgYEAopEdUCl5t0l5CrEvWVJt18S2fCyeE1m2EzmeiKX3r/o2m6kPw3iIfiZjyGeqtbXmUO17wRLEP+XEBEaToCt5aFU5DWjxDtSXsM8D0dO79q00/6/HJZoBEJe73fBuUYkabKXDyvcYKA544+S0KF8smOpADqORbaWAIlUCBt3vXyc=";
    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(MyApplication.class)
    static class RealApplicationStub {}
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
//         MultiDex.install(this);
        initSophix();
    }
    private void initSophix() {
        String appVersion = "0.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
        }
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData(null, null, null)
                .setEnableDebug(true)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.i(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.i(TAG, "sophix preload patch success. restart app to make effect.");
                        }
                    }
                }).initialize();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
