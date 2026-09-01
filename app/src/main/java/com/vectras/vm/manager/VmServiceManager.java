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
            try {
                // A single check is not enough: another VM or a leftover
                // qemu process can still be visible at this instant, and
                // skipping the reset would leave isKillingService stuck
                // true - MainStartVM.startNow() waits on this flag in a
                // 3s-sleep loop, so every later VM start would hang until
                // the app is killed. Poll until QEMU is gone, with a
                // timeout as a safety net.
                long deadline = System.currentTimeMillis() + 60_000;
                while (ProcessManager.isQemuRunning(context)
                        && System.currentTimeMillis() < deadline) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            } finally {
                new Handler(Looper.getMainLooper()).post(() -> {
                    MainService.stopService();
                    isKillingService = false;
                });
            }
        }).start();
    }
}
