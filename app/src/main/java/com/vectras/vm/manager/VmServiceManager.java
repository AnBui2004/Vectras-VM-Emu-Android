package com.vectras.vm.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.vectras.vm.MainService;

public class VmServiceManager {
    public static boolean isKillingService;

    public static void stopService(Context context) {
        isKillingService = true;
        new Thread(() -> {
            if (!ProcessManager.isQemuRunning(context)) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    MainService.stopService();
                    isKillingService = false;
                });
            }
        }).start();
    }
}
