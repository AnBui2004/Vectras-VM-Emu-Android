package com.vectras.vm.crashtracker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.vectras.qemu.MainSettingsManager;
import com.vectras.vm.AppConfig;
import com.vectras.vm.utils.FileUtils;

import java.io.File;
import java.util.LinkedHashMap;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final String TAG = "CrashHandler";
    private final Context context;
    private final Thread.UncaughtExceptionHandler defaultHandler;

    public CrashHandler(Context ctx) {
        context = ctx.getApplicationContext();
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Log.e(TAG, "uncaughtException: ", e);

        LinkedHashMap<String, String> head = CrashTrackerUtils.getClientInfo(context, System.currentTimeMillis(), false);

        StringBuilder builder = new StringBuilder();

        for (String key : head.keySet()) {
            if (builder.length() != 0)
                builder.append("\n");
            builder.append(key);
            builder.append(" :    ");
            builder.append(head.get(key));
        }

        builder.append("\n\n");
        builder.append(Log.getStackTraceString(e));

        File file = new File(AppConfig.lastCrashLogPath);

        FileUtils.writeToFile(file.getParent(), file.getName(), builder.toString());
        MainSettingsManager.setShowLastCrashLog(context.getApplicationContext(), true);

        defaultHandler.uncaughtException(t, e);
    }
}