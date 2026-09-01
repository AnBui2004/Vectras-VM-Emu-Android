package com.vectras.vm.manager;

import android.content.Context;

import com.vectras.qemu.MainSettingsManager;
import com.vectras.vm.settings.ItemSettingsSelector;
import com.vectras.vterm.Terminal2;

public class QemuManager {
    public static final int DEFAULT_REFRESH_RATE = 60;
    public static final int DEFAULT_LOW_REFRESH_RATE = 30;

    // Probing runs "qemu-system-* -vnc help" in a proot shell (1-3s) and
    // the answer cannot change while the app runs, so cache it per arch.
    private static final java.util.Map<String, Boolean> refreshRateSupportCache = new java.util.HashMap<>();

    public static boolean isSupportSetRefreshRate(Context context) {
        String arch = MainSettingsManager.getArch(context);
        synchronized (refreshRateSupportCache) {
            Boolean cached = refreshRateSupportCache.get(arch);
            if (cached != null) return cached;
        }
        boolean supported = new Terminal2(context).executeOnThisThread(getQemuExecutableFile(context) + " -vnc help").contains("refresh-rate");
        synchronized (refreshRateSupportCache) {
            refreshRateSupportCache.put(arch, supported);
        }
        return supported;
    }

    public static boolean isSupportAcpiTable(Context context) {
        String currentArch = MainSettingsManager.getArch(context);

        return currentArch.equals(MainSettingsManager.X86_64_ARCH) || currentArch.equals(MainSettingsManager.I386_ARCH);
    }

    public static int getAppropriateRefreshRate(Context context, String params, int max) {
        int result = DEFAULT_REFRESH_RATE;

        if (params.contains("-vga vmware")
                || params.contains(" vmware-svga")
                || params.contains("-vga qxl")
                || params.contains(" qxl-vga")) result = 75;

        if (params.contains(" virio-vga") || params.contains(" virio-gpu")) result = max;

        int refreshRateSetting = Integer.parseInt(ItemSettingsSelector.getVncRefreshRateValue(MainSettingsManager.getVncRefreshRate(context)));

        result = Math.min(result, refreshRateSetting);

        return result;
    }

    public static String getQemuExecutableFile(Context context) {
        if (MainSettingsManager.getArch(context).equals(MainSettingsManager.I386_ARCH))
            return "qemu-system-i386";
        else if (MainSettingsManager.getArch(context).equals(MainSettingsManager.ARM64_ARCH))
            return "qemu-system-aarch64";
        else if (MainSettingsManager.getArch(context).equals(MainSettingsManager.PPC_ARCH))
            return "qemu-system-ppc";

        return "qemu-system-x86_64";
    }
}
